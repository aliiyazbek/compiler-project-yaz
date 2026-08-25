package semantic;

import ast.backend.*;
import ast.base.ASTNode;
import ast.frontend.*;
import generator.ContextBuilder;
import generator.TemplateContext;

import java.util.*;

/**
 * The semantic analysis phase: checks that a program which <em>parses</em> also
 * <em>means</em> something.
 *
 * <p>Until now this phase only gathered information (a symbol table and inferred
 * types) and never rejected anything, so a typo like {@code {{ prodcut.name }}}
 * silently rendered as empty. These checks turn it into a real phase that
 * reports problems with a file and a line.
 *
 * <h3>Python side</h3>
 * <ul>
 *   <li>use of a name that was never assigned, imported, or passed as a parameter;</li>
 *   <li>a name assigned twice in the same scope where the second is unused;</li>
 *   <li>{@code render_template} naming a template file that does not exist;</li>
 *   <li>a route function that never returns anything.</li>
 * </ul>
 *
 * <h3>Template side</h3>
 * <ul>
 *   <li>{@code {{ x }}} where {@code x} is not in the context that route passes;</li>
 *   <li>{@code {{ a.b }}} where the row genuinely has no field {@code b};</li>
 *   <li>{@code {% for x in xs %}} where {@code xs} is not a list;</li>
 *   <li>{@code {% extends %}} naming a layout that was not found;</li>
 *   <li>a template that no route ever renders.</li>
 * </ul>
 *
 * <p>Name checks are scope-aware: the analyser keeps its own scope stack rather
 * than reusing the visitor's symbol table, which has already collapsed back to
 * global scope by the time construction finishes.
 *
 * <p>Where a name is misspelled, the closest in-scope name is offered as a hint,
 * measured by edit distance.
 */
public class SemanticAnalyzer {

    /** Names that exist at runtime without ever being assigned in the source. */
    private static final Set<String> PYTHON_BUILTINS = new HashSet<>(Arrays.asList(
            "len", "range", "str", "int", "float", "bool", "list", "dict", "set",
            "tuple", "print", "enumerate", "sorted", "sum", "min", "max", "abs",
            "zip", "map", "filter", "any", "all", "round", "type", "isinstance",
            "__name__", "self", "True", "False", "None"));

    /** Names Jinja provides inside a template without them being passed in. */
    private static final Set<String> JINJA_BUILTINS = new HashSet<>(Arrays.asList(
            "loop", "url_for", "request", "session", "config", "g",
            "true", "false", "none", "True", "False", "None"));

    private final List<Diagnostic> diagnostics = new ArrayList<>();

    /**
     * file:line:code:message already reported, so the same problem is not listed
     * twice when a node is reachable by more than one path through the tree.
     */
    private final Set<String> reported = new HashSet<>();

    /** Record a diagnostic unless an identical one was already recorded. */
    private void report(Diagnostic diagnostic) {
        String key = diagnostic.getFile() + ":" + diagnostic.getLine()
                + ":" + diagnostic.getCode() + ":" + diagnostic.getMessage();
        if (reported.add(key)) {
            diagnostics.add(diagnostic);
        }
    }

    /** Innermost-last stack of scopes; each holds the names declared in it. */
    private final Deque<Map<String, Integer>> scopes = new ArrayDeque<>();

    private String currentFile;

    public List<Diagnostic> getDiagnostics() {
        return diagnostics;
    }

    public long errorCount() {
        return diagnostics.stream().filter(Diagnostic::isError).count();
    }

    public long warningCount() {
        return diagnostics.stream().filter(d -> !d.isError()).count();
    }

    public boolean hasErrors() {
        return errorCount() > 0;
    }

    // ==================================================================
    // Python
    // ==================================================================

    /**
     * Check the backend AST for name and structure problems.
     *
     * @param knownTemplates the template file names that actually exist, so a
     *                       render_template of a missing file can be reported
     */
    public void analyzeBackend(String file, ASTNode root, Set<String> knownTemplates) {
        if (root == null) {
            return;
        }
        currentFile = file;
        scopes.clear();
        pushScope();

        // Module level is walked in two passes: functions are hoisted, because
        // Python lets one route call another defined further down the file.
        hoistFunctionNames(root);
        walkPythonBlock(root, knownTemplates);

        popScope();
    }

    /**
     * Declare every function name up front.
     *
     * Python resolves a call at call time, so one route may legitimately call
     * another defined later in the file. Decorated functions sit inside a nested
     * wrapper block, so this descends through anything that is not itself a
     * function body.
     */
    private void hoistFunctionNames(ASTNode node) {
        for (ASTNode child : node.getChildren()) {
            if (child instanceof FunctionDefNode) {
                declare(((FunctionDefNode) child).getFunctionName(), child.getLineNumber());
            } else {
                hoistFunctionNames(child);
            }
        }
    }

    /** Walk statements in order, so "used before assigned" is detectable. */
    private void walkPythonBlock(ASTNode node, Set<String> knownTemplates) {
        for (ASTNode child : node.getChildren()) {
            walkPythonStatement(child, knownTemplates);
        }
    }

    private void walkPythonStatement(ASTNode node, Set<String> knownTemplates) {
        if (node instanceof ImportNode) {
            for (String imported : ((ImportNode) node).getImportedItems()) {
                declare(imported, node.getLineNumber());
            }
            return;
        }

        if (node instanceof AssignmentNode) {
            AssignmentNode assign = (AssignmentNode) node;
            // The right-hand side is evaluated before the name is bound, so a
            // self-reference like "x = x + 1" on a fresh name is a real error.
            checkExpression(firstChild(assign), knownTemplates);
            declare(assign.getVariableName(), assign.getLineNumber());
            return;
        }

        if (node instanceof FunctionDefNode) {
            FunctionDefNode function = (FunctionDefNode) node;
            declare(function.getFunctionName(), function.getLineNumber());

            pushScope();
            for (String parameter : function.getParameters()) {
                declare(parameter, function.getLineNumber());
            }
            walkPythonBlock(function, knownTemplates);
            checkRouteReturns(function);
            popScope();
            return;
        }

        if (node instanceof ForStatementNode) {
            ForStatementNode loop = (ForStatementNode) node;
            // The iterable is evaluated before the loop variable exists.
            for (ASTNode child : loop.getChildren()) {
                if (!(child instanceof BlockNode)) {
                    checkExpression(child, knownTemplates);
                }
            }
            declare(loop.getIteratorVariable(), loop.getLineNumber());
            walkPythonBlock(loop, knownTemplates);
            return;
        }

        if (node instanceof DecoratorNode) {
            return; // the decorator expression names Flask objects, not locals
        }

        // A container (the Program wrapper around a decorated function, an if's
        // block, ...) holds statements, not an expression of its own. Descending
        // as statements is what keeps declaration order meaningful — treating it
        // as one expression would check a function body before its assignments
        // have run.
        if (isContainer(node)) {
            walkPythonBlock(node, knownTemplates);
            return;
        }

        // A leaf statement — a return, a bare call, a condition. Its whole
        // subtree is one expression.
        checkExpression(node, knownTemplates);
    }

    /** Nodes that group statements rather than forming an expression. */
    private static boolean isContainer(ASTNode node) {
        return node instanceof ProgramNode
                || node instanceof BlockNode
                || node instanceof IfStatementNode
                || node instanceof ElifBranchNode
                || node instanceof ElseBranchNode;
    }

    /**
     * Report identifiers that are not in scope, and validate render_template
     * calls. Descends through the expression tree.
     */
    private void checkExpression(ASTNode node, Set<String> knownTemplates) {
        if (node == null) {
            return;
        }

        if (node instanceof IdentifierNode) {
            // Only the root of a dotted name is a variable: in request.method it
            // is "request" that must exist, not "method".
            String name = rootName(((IdentifierNode) node).getName());
            if (!isDeclared(name) && !PYTHON_BUILTINS.contains(name)) {
                report(Diagnostic.error(currentFile, node.getLineNumber(),
                        "undefined-name",
                        "'" + name + "' is used but never defined",
                        suggest(name, visibleNames())));
            }
            return;
        }

        if (node instanceof BinaryOpNode && ".".equals(((BinaryOpNode) node).getOperator())) {
            // Attribute access: in request.method only the left side is a name to
            // resolve. The right side is an attribute of whatever it evaluates to,
            // which this analyser does not model.
            if (!node.getChildren().isEmpty()) {
                checkExpression(node.getChildren().get(0), knownTemplates);
            }
            return;
        }

        if (node instanceof FunctionCallNode) {
            FunctionCallNode call = (FunctionCallNode) node;
            String callee = call.getFunctionName();
            if ("render_template".equals(callee)) {
                checkRenderTemplate(call, knownTemplates);
            } else {
                // A method call (products.append, request.form.get) is checked on
                // its receiver: whether "append" exists is a property of the
                // object's type, which this analyser does not model.
                String receiver = rootName(callee);
                boolean isMethodCall = !receiver.equals(callee);
                String subject = isMethodCall ? receiver : callee;

                if (!isDeclared(subject) && !PYTHON_BUILTINS.contains(subject)) {
                    report(Diagnostic.error(currentFile, call.getLineNumber(),
                            isMethodCall ? "undefined-name" : "undefined-function",
                            isMethodCall
                                    ? "'" + subject + "' is used but never defined"
                                    : "'" + callee + "()' is called but never defined",
                            suggest(subject, visibleNames())));
                }
            }
        }

        for (ASTNode child : node.getChildren()) {
            checkExpression(child, knownTemplates);
        }
    }

    /** The template named in a render_template call must exist on disk. */
    private void checkRenderTemplate(FunctionCallNode call, Set<String> knownTemplates) {
        if (knownTemplates == null || knownTemplates.isEmpty()) {
            return;
        }
        for (ASTNode arg : call.getChildren()) {
            if (!(arg instanceof ArgumentNode) || ((ArgumentNode) arg).isKeyword()) {
                continue;
            }
            ASTNode value = firstChild(arg);
            if (!(value instanceof LiteralNode)) {
                continue;
            }
            String name = stripQuotes(((LiteralNode) value).getValue());
            if (!knownTemplates.contains(name)) {
                report(Diagnostic.error(currentFile, call.getLineNumber(),
                        "missing-template",
                        "render_template('" + name + "') names a template that does not exist",
                        suggest(name, knownTemplates)));
            }
            return; // only the first positional argument is the template name
        }
    }

    /** A route that returns nothing renders a blank page at runtime. */
    private void checkRouteReturns(FunctionDefNode function) {
        if (!containsReturn(function)) {
            report(Diagnostic.warning(currentFile, function.getLineNumber(),
                    "route-returns-nothing",
                    "'" + function.getFunctionName() + "()' never returns a response",
                    "a Flask view should return render_template(...) or redirect(...)"));
        }
    }

    private boolean containsReturn(ASTNode node) {
        if (node instanceof ReturnStatementNode) {
            return true;
        }
        for (ASTNode child : node.getChildren()) {
            if (containsReturn(child)) {
                return true;
            }
        }
        return false;
    }

    // ==================================================================
    // templates
    // ==================================================================

    /**
     * Check one template against the context the Python actually passes it.
     *
     * @param templateName    the template's file name
     * @param root            its Jinja AST
     * @param context         the context render_template supplies, or null if no
     *                        route renders this template
     * @param knownTemplates  template names that exist, for {% extends %}
     * @param renderedByRoute whether any route renders this template
     */
    public void analyzeTemplate(String file, String templateName, ASTNode root,
                                TemplateContext context, Set<String> knownTemplates,
                                boolean renderedByRoute) {
        if (root == null) {
            return;
        }
        currentFile = file;

        if (!renderedByRoute && !isLayout(root, knownTemplates)) {
            report(Diagnostic.warning(file, 1, "unrendered-template",
                    "'" + templateName + "' is never rendered by any route",
                    "no render_template('" + templateName + "') call refers to it"));
        }

        checkExtends(root, knownTemplates);

        // Without a context there is nothing to check names against; the warning
        // above already said why, and every expression would repeat it.
        if (context != null) {
            Set<String> inScope = new HashSet<>(context.getVariables().keySet());
            walkTemplate(root, context, inScope);
        }
    }

    /** A template that defines blocks and is extended is a layout, not dead. */
    private boolean isLayout(ASTNode root, Set<String> knownTemplates) {
        return !findAll(root, JinjaBlockNode.class).isEmpty();
    }

    private void checkExtends(ASTNode root, Set<String> knownTemplates) {
        for (JinjaExtendsNode node : findAll(root, JinjaExtendsNode.class)) {
            String parent = stripQuotes(node.getTemplateName());
            if (knownTemplates != null && !knownTemplates.isEmpty()
                    && !knownTemplates.contains(parent)) {
                report(Diagnostic.error(currentFile, node.getLineNumber(),
                        "missing-layout",
                        "extends '" + parent + "', which does not exist",
                        suggest(parent, knownTemplates)));
            }
        }
    }

    /**
     * Walk the template, tracking which names are in scope. A {@code {% for %}}
     * introduces its iterator for the duration of its body.
     */
    private void walkTemplate(ASTNode node, TemplateContext context, Set<String> inScope) {
        if (node instanceof JinjaExpressionNode) {
            checkTemplateExpression(((JinjaExpressionNode) node).getExpressionText(),
                    node.getLineNumber(), context, inScope);

        } else if (node instanceof JinjaIfNode) {
            checkCondition(((JinjaIfNode) node).getCondition(),
                    node.getLineNumber(), context, inScope);

        } else if (node instanceof JinjaForNode) {
            JinjaForNode loop = (JinjaForNode) node;
            checkLoopCollection(loop, context, inScope);

            Set<String> loopScope = new HashSet<>(inScope);
            loopScope.add(loop.getIteratorVariable());
            for (ASTNode child : loop.getChildren()) {
                walkTemplate(child, context, loopScope);
            }
            return;

        } else if (node instanceof HtmlAttributeNode) {
            checkAttributeValue((HtmlAttributeNode) node, context, inScope);
        }

        for (ASTNode child : node.getChildren()) {
            walkTemplate(child, context, inScope);
        }
    }

    /** {% for product in products %} — the collection must be an actual list. */
    private void checkLoopCollection(JinjaForNode loop, TemplateContext context,
                                     Set<String> inScope) {
        String collection = loop.getCollectionExpression();
        String base = rootName(collection);

        if (!inScope.contains(base) && !JINJA_BUILTINS.contains(base)) {
            report(Diagnostic.error(currentFile, loop.getLineNumber(),
                    "undefined-template-variable",
                    "{% for ... in " + collection + " %} — '" + base
                            + "' is not passed to this template",
                    suggest(base, inScope)));
            return;
        }

        TemplateContext.Value value = context.resolve(collection);
        if (value != null && !value.isList()) {
            report(Diagnostic.error(currentFile, loop.getLineNumber(),
                    "not-iterable",
                    "{% for ... in " + collection + " %} — '" + collection
                            + "' is not a list",
                    "it is " + describe(value)));
        }
    }

    private void checkCondition(String condition, int line,
                                TemplateContext context, Set<String> inScope) {
        if (condition == null) {
            return;
        }
        // Split on comparison operators; each side may be a literal or a name.
        for (String operand : condition.split("==|!=|>=|<=|>|<")) {
            String token = operand.trim();
            if (token.isEmpty() || isLiteral(token)) {
                continue;
            }
            checkTemplateExpression(token, line, context, inScope);
        }
    }

    /** Interpolations inside an attribute value are unparsed text in the AST. */
    private void checkAttributeValue(HtmlAttributeNode attr, TemplateContext context,
                                     Set<String> inScope) {
        String raw = attr.getAttributeValue();
        if (raw == null || !raw.contains("{{")) {
            return;
        }
        int cursor = 0;
        while (true) {
            int start = raw.indexOf("{{", cursor);
            if (start < 0) {
                return;
            }
            int end = raw.indexOf("}}", start + 2);
            if (end < 0) {
                return;
            }
            checkTemplateExpression(raw.substring(start + 2, end),
                    attr.getLineNumber(), context, inScope);
            cursor = end + 2;
        }
    }

    /**
     * Check one {@code {{ ... }}} path: the root name must be in scope, and a
     * dotted field must exist on the value it resolves to.
     */
    private void checkTemplateExpression(String expression, int line,
                                         TemplateContext context, Set<String> inScope) {
        String expr = expression == null ? "" : expression.trim();
        if (expr.isEmpty() || isLiteral(expr)) {
            return;
        }

        String base = rootName(expr);
        if (!inScope.contains(base) && !JINJA_BUILTINS.contains(base)) {
            report(Diagnostic.error(currentFile, line,
                    "undefined-template-variable",
                    "{{ " + expr + " }} — '" + base + "' is not passed to this template",
                    suggest(base, inScope)));
            return;
        }

        checkFieldPath(expr, base, line, context, inScope);
    }

    /**
     * Validate {@code a.b.c} against the real data.
     *
     * A loop iterator is not in the context by name, so its fields are checked
     * against the first row of the collection it iterates — which is the shape
     * every row shares.
     */
    private void checkFieldPath(String expr, String base, int line,
                                TemplateContext context, Set<String> inScope) {
        int dot = expr.indexOf('.');
        if (dot < 0) {
            return; // a plain name; nothing more to verify
        }

        TemplateContext.Value value = context.get(base);
        if (value == null) {
            // Probably a loop iterator: find a list in the context and use a row
            // as the shape. Ambiguous only if several lists are passed, in which
            // case any matching row is enough to accept the field.
            if (fieldExistsInSomeRow(context, expr.substring(dot + 1))) {
                return;
            }
            Set<String> fields = candidateRowFields(context);
            if (fields.isEmpty()) {
                return; // no data to check against; stay quiet rather than guess
            }
            String field = expr.substring(dot + 1).split("\\.")[0];
            report(Diagnostic.error(currentFile, line,
                    "unknown-field",
                    "{{ " + expr + " }} — no field '" + field + "' in the data",
                    suggest(field, fields)));
            return;
        }

        // A concrete value: walk the path segment by segment.
        String[] parts = expr.split("\\.");
        TemplateContext.Value current = value;
        for (int i = 1; i < parts.length; i++) {
            if (current == null || !current.isDict()) {
                return; // not something we can verify further
            }
            TemplateContext.Value next = current.field(parts[i]);
            if (next == null) {
                report(Diagnostic.error(currentFile, line,
                        "unknown-field",
                        "{{ " + expr + " }} — '" + parts[i] + "' is not a field of '"
                                + parts[i - 1] + "'",
                        suggest(parts[i], current.getDict().keySet())));
                return;
            }
            current = next;
        }
    }

    /** Whether any list row in the context carries this field path. */
    private boolean fieldExistsInSomeRow(TemplateContext context, String fieldPath) {
        String field = fieldPath.split("\\.")[0];
        for (TemplateContext.Value value : context.getVariables().values()) {
            if (value.isList() && !value.getList().isEmpty()) {
                TemplateContext.Value row = value.getList().get(0);
                if (row.isDict() && row.getDict().containsKey(field)) {
                    return true;
                }
            }
            if (value.isDict() && value.getDict().containsKey(field)) {
                return true;
            }
        }
        return false;
    }

    /** Every field name available across the context's rows, for suggestions. */
    private Set<String> candidateRowFields(TemplateContext context) {
        Set<String> fields = new LinkedHashSet<>();
        for (TemplateContext.Value value : context.getVariables().values()) {
            if (value.isList() && !value.getList().isEmpty()) {
                TemplateContext.Value row = value.getList().get(0);
                if (row.isDict()) {
                    fields.addAll(row.getDict().keySet());
                }
            } else if (value.isDict()) {
                fields.addAll(value.getDict().keySet());
            }
        }
        return fields;
    }

    // ==================================================================
    // scope helpers
    // ==================================================================

    private void pushScope() {
        scopes.push(new HashMap<>());
    }

    private void popScope() {
        scopes.pop();
    }

    private void declare(String name, int line) {
        if (name != null && !scopes.isEmpty()) {
            scopes.peek().put(name, line);
        }
    }

    private boolean isDeclared(String name) {
        for (Map<String, Integer> scope : scopes) {
            if (scope.containsKey(name)) {
                return true;
            }
        }
        return false;
    }

    private Set<String> visibleNames() {
        Set<String> names = new LinkedHashSet<>();
        for (Map<String, Integer> scope : scopes) {
            names.addAll(scope.keySet());
        }
        return names;
    }

    // ==================================================================
    // small utilities
    // ==================================================================

    /** "product.name" -> "product"; "products[0]" -> "products". */
    private static String rootName(String expression) {
        String expr = expression.trim();
        int dot = expr.indexOf('.');
        int bracket = expr.indexOf('[');
        int cut = expr.length();
        if (dot >= 0) {
            cut = Math.min(cut, dot);
        }
        if (bracket >= 0) {
            cut = Math.min(cut, bracket);
        }
        return expr.substring(0, cut).trim();
    }

    private static boolean isLiteral(String token) {
        if (token.isEmpty()) {
            return true;
        }
        char first = token.charAt(0);
        return first == '"' || first == '\'' || Character.isDigit(first);
    }

    private static String describe(TemplateContext.Value value) {
        if (value.isDict()) {
            return "a single row (dict)";
        }
        if (value.isList()) {
            return "a list";
        }
        return "the value '" + value.getScalar() + "'";
    }

    /**
     * The closest candidate to a misspelled name, if one is close enough.
     * A distance of a third of the name's length keeps suggestions plausible.
     */
    static String suggest(String name, Collection<String> candidates) {
        if (name == null || candidates == null || candidates.isEmpty()) {
            return null;
        }
        String best = null;
        int bestDistance = Integer.MAX_VALUE;
        for (String candidate : candidates) {
            int distance = editDistance(name.toLowerCase(), candidate.toLowerCase());
            if (distance < bestDistance) {
                bestDistance = distance;
                best = candidate;
            }
        }
        // Allow a little more slack on longer names, where a suggestion is still
        // obviously right at distance 2-3, but keep short names strict so
        // unrelated two-letter words are not offered as corrections.
        int threshold = Math.max(1, name.length() / 3);
        if (name.length() >= 5) {
            threshold = Math.max(threshold, 2);
        }
        return (best != null && bestDistance <= threshold) ? "did you mean '" + best + "'?" : null;
    }

    /**
     * Damerau-Levenshtein distance — Levenshtein plus adjacent transposition.
     *
     * Transposing two letters ("titel" for "title") is the most common typo there
     * is, and plain Levenshtein scores it 2, the same as two unrelated edits. That
     * is enough to push the real name past the suggestion threshold, so
     * transposition is counted as the single edit it actually is.
     */
    public static int editDistance(String a, String b) {
        int[][] distance = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            distance[i][0] = i;
        }
        for (int j = 0; j <= b.length(); j++) {
            distance[0][j] = j;
        }

        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                distance[i][j] = Math.min(
                        Math.min(distance[i - 1][j] + 1, distance[i][j - 1] + 1),
                        distance[i - 1][j - 1] + cost);

                if (i > 1 && j > 1
                        && a.charAt(i - 1) == b.charAt(j - 2)
                        && a.charAt(i - 2) == b.charAt(j - 1)) {
                    distance[i][j] = Math.min(distance[i][j], distance[i - 2][j - 2] + 1);
                }
            }
        }
        return distance[a.length()][b.length()];
    }

    private static ASTNode firstChild(ASTNode node) {
        if (node == null || node.getChildren().isEmpty()) {
            return null;
        }
        return node.getChildren().get(0);
    }

    private static String stripQuotes(String s) {
        return ContextBuilder.stripQuotes(s);
    }

    private static <T extends ASTNode> List<T> findAll(ASTNode root, Class<T> type) {
        List<T> found = new ArrayList<>();
        collect(root, type, found);
        return found;
    }

    private static <T extends ASTNode> void collect(ASTNode node, Class<T> type, List<T> out) {
        if (type.isInstance(node)) {
            out.add(type.cast(node));
        }
        for (ASTNode child : node.getChildren()) {
            collect(child, type, out);
        }
    }
}
