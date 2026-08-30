package generator;

import ast.backend.*;
import ast.base.ASTNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds, for each template, the {@link TemplateContext} that a real
 * {@code render_template(...)} call would have supplied.
 *
 * This is the "Context Data" box of the project diagram: it reads the Python AST
 * and works out <em>which values reach which template</em>.
 *
 * <p>It runs in three steps:
 * <ol>
 *   <li><b>Globals.</b> Evaluate module-level assignments into {@link TemplateContext.Value}s,
 *       so {@code products = [{...}, {...}]} becomes a real list of dicts.</li>
 *   <li><b>Routes.</b> For every {@code @app.route(...)}-decorated function, record
 *       the URL it serves so the emitter can name the generated file sensibly
 *       ({@code '/'} becomes {@code index.html}).</li>
 *   <li><b>Contexts.</b> For each {@code render_template('x.html', a=b, ...)} call,
 *       evaluate every keyword argument in the enclosing function's scope and store
 *       the resulting context under {@code x.html}.</li>
 * </ol>
 *
 * <p>Local variables inside a route function are evaluated too, so
 * {@code total = len(products)} resolves to the concrete value {@code 3} rather
 * than being dropped. Anything the evaluator cannot fold to a literal is simply
 * omitted, and the emitter renders such expressions as empty — the same
 * best-effort contract the rest of the pipeline uses.
 */
public class ContextBuilder {

    /** Module-level variables, e.g. products. */
    private final Map<String, TemplateContext.Value> globals = new LinkedHashMap<>();

    /** Template file name -> the context that render_template passed to it. */
    private final Map<String, TemplateContext> contexts = new LinkedHashMap<>();

    /** Template file name -> the route URL that renders it, e.g. "/". */
    private final Map<String, String> templateRoutes = new LinkedHashMap<>();

    /** Diagnostics collected while building, echoed into generation_log.txt. */
    private final List<String> log = new ArrayList<>();

    /**
     * The exact value objects produced by folding {@code len(...)}, tracked by
     * identity. A live server re-derives these after every mutation; matching on
     * the number alone would be wrong, since an unrelated id could share it.
     */
    private final java.util.Set<TemplateContext.Value> countValues =
            java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>());

    /** Whether this value came from a folded {@code len(...)} call. */
    public boolean isRowCount(TemplateContext.Value value) {
        return countValues.contains(value);
    }

    public Map<String, TemplateContext.Value> getGlobals() {
        return globals;
    }

    public Map<String, TemplateContext> getContexts() {
        return contexts;
    }

    public Map<String, String> getTemplateRoutes() {
        return templateRoutes;
    }

    public List<String> getLog() {
        return log;
    }

    public TemplateContext contextFor(String templateName) {
        return contexts.get(templateName);
    }

    // ------------------------------------------------------------------
    // entry point
    // ------------------------------------------------------------------

    /** Read the Python AST and populate globals, routes and per-template contexts. */
    public void build(ASTNode pythonRoot) {
        if (pythonRoot == null) {
            log.add("No Python AST available; no context data produced.");
            return;
        }

        collectGlobals(pythonRoot);
        log.add("Global data discovered: " + globals.keySet());

        collectRoutes(pythonRoot);
    }

    // ------------------------------------------------------------------
    // step 1: module-level assignments
    // ------------------------------------------------------------------

    /**
     * Evaluate top-level assignments only. Assignments nested inside function
     * bodies are handled per-route in {@link #collectRoutes}, so a local variable
     * never leaks into the global environment.
     */
    private void collectGlobals(ASTNode root) {
        for (ASTNode child : root.getChildren()) {
            if (child instanceof AssignmentNode) {
                AssignmentNode assign = (AssignmentNode) child;
                TemplateContext.Value value = evaluate(firstChild(assign), globals);
                if (value != null) {
                    globals.put(assign.getVariableName(), value);
                }
            }
        }
    }

    // ------------------------------------------------------------------
    // step 2 + 3: routes and their render_template contexts
    // ------------------------------------------------------------------

    /**
     * Walk the AST looking for route functions.
     *
     * A decorated function is represented as a wrapper block holding the
     * {@code Decorator} and the {@code FunctionDefinition} as <em>siblings</em>,
     * so the URL cannot be read from the function node itself: we track the most
     * recent decorator seen among a node's children and hand it to the function
     * that follows it.
     */
    private void collectRoutes(ASTNode node) {
        String pendingRoute = null;

        for (ASTNode child : node.getChildren()) {
            if (child instanceof DecoratorNode) {
                String url = firstQuotedString(
                        ((DecoratorNode) child).getDecoratorExpression());
                if (url != null) {
                    pendingRoute = url;
                }
                continue;
            }

            if (child instanceof FunctionDefNode) {
                processFunction((FunctionDefNode) child, pendingRoute);
                pendingRoute = null;
                continue;
            }

            collectRoutes(child);
        }
    }

    /**
     * Walk one route function: fold its local assignments into a local scope,
     * then evaluate any render_template call against that scope.
     */
    private void processFunction(FunctionDefNode function, String routeUrl) {
        Map<String, TemplateContext.Value> locals = new LinkedHashMap<>(globals);
        bindRouteParameters(function, routeUrl, locals);
        collectLocalsAndRenders(function, locals, routeUrl, function.getFunctionName());
    }

    /**
     * Bind a route's URL parameters to a representative value.
     *
     * A page such as {@code /product/<int:id>} has no single compile-time value —
     * at runtime it serves one page per id. Generating static HTML means picking a
     * representative, so we bind each parameter to the first valid one (1 for the
     * {@code <int:id>} style ids the sample app uses, which index the first row).
     * Without this the whole page renders empty, which is strictly less useful.
     */
    private void bindRouteParameters(FunctionDefNode function, String routeUrl,
                                     Map<String, TemplateContext.Value> locals) {
        if (routeUrl == null) {
            return;
        }
        List<String> declared = urlParameters(routeUrl);
        for (String parameter : function.getParameters()) {
            if (declared.contains(parameter)) {
                locals.put(parameter, TemplateContext.Value.of("1"));
                log.add("  ~ " + function.getFunctionName() + ": route parameter '"
                        + parameter + "' bound to the representative value 1"
                        + " (the page is generated for that one).");
            }
        }
    }

    /**
     * Extract the parameter names a Flask rule declares, dropping any converter
     * prefix: {@code /product/<int:id>} yields {@code [id]}.
     */
    private static List<String> urlParameters(String routeUrl) {
        List<String> names = new ArrayList<>();
        int cursor = 0;
        while (true) {
            int open = routeUrl.indexOf('<', cursor);
            if (open < 0) {
                break;
            }
            int close = routeUrl.indexOf('>', open + 1);
            if (close < 0) {
                break;
            }
            String token = routeUrl.substring(open + 1, close);
            int colon = token.indexOf(':');
            names.add(colon >= 0 ? token.substring(colon + 1) : token);
            cursor = close + 1;
        }
        return names;
    }

    /**
     * Single pass over a function body, in source order, so that a variable is
     * assigned before the render_template call that consumes it.
     */
    private void collectLocalsAndRenders(ASTNode node,
                                         Map<String, TemplateContext.Value> locals,
                                         String routeUrl,
                                         String functionName) {
        for (ASTNode child : node.getChildren()) {
            if (child instanceof AssignmentNode) {
                AssignmentNode assign = (AssignmentNode) child;
                TemplateContext.Value value = evaluate(firstChild(assign), locals);
                if (value != null) {
                    locals.put(assign.getVariableName(), value);
                }
            } else if (child instanceof FunctionCallNode
                    && "render_template".equals(((FunctionCallNode) child).getFunctionName())) {
                readRenderTemplate((FunctionCallNode) child, locals, routeUrl, functionName);
            }
            // Recurse so calls inside if/for/return bodies are still found.
            collectLocalsAndRenders(child, locals, routeUrl, functionName);
        }
    }

    private void readRenderTemplate(FunctionCallNode call,
                                    Map<String, TemplateContext.Value> scope,
                                    String routeUrl,
                                    String functionName) {
        String templateName = null;
        TemplateContext context = new TemplateContext();

        for (ASTNode arg : call.getChildren()) {
            if (!(arg instanceof ArgumentNode)) {
                continue;
            }
            ArgumentNode argument = (ArgumentNode) arg;
            ASTNode valueNode = firstChild(argument);

            if (!argument.isKeyword()) {
                if (templateName == null && valueNode instanceof LiteralNode) {
                    templateName = stripQuotes(((LiteralNode) valueNode).getValue());
                }
            } else {
                TemplateContext.Value value = evaluate(valueNode, scope);
                if (value != null) {
                    context.put(argument.getName(), value);
                } else {
                    context.declare(argument.getName());
                    log.add("  ! " + functionName + ": could not evaluate '"
                            + argument.getName() + "'; it will render as empty.");
                }
            }
        }

        if (templateName == null) {
            return;
        }

        // A template rendered from more than one route keeps its first context;
        // re-rendering it per route would need one output file per route. The
        // later route's variable names are still folded in, so a name only that
        // route passes is not reported as missing.
        if (contexts.containsKey(templateName)) {
            TemplateContext existing = contexts.get(templateName);
            for (String name : context.getDeclaredNames()) {
                existing.declare(name);
            }
            log.add("  ~ " + templateName + " is rendered by more than one route; "
                    + "keeping the context from the first (" + functionName + " is a later one).");
            return;
        }

        contexts.put(templateName, context);
        if (routeUrl != null) {
            templateRoutes.put(templateName, routeUrl);
        }
        log.add("  + " + templateName + " <- " + functionName + "()"
                + (routeUrl != null ? " [route " + routeUrl + "]" : "")
                + " context=" + context.getVariables().keySet());
    }

    // ------------------------------------------------------------------
    // expression evaluation
    // ------------------------------------------------------------------

    /**
     * Fold a Python expression node into a concrete {@link TemplateContext.Value}.
     * Returns null when the expression depends on runtime state the compiler
     * cannot know (a form POST, a database, request data, ...).
     */
    private TemplateContext.Value evaluate(ASTNode node, Map<String, TemplateContext.Value> scope) {
        if (node == null) {
            return null;
        }

        if (node instanceof LiteralNode) {
            return TemplateContext.Value.of(stripQuotes(((LiteralNode) node).getValue()));
        }

        if (node instanceof IdentifierNode) {
            return scope.get(((IdentifierNode) node).getName());
        }

        if (node instanceof ListLiteralNode) {
            List<TemplateContext.Value> items = new ArrayList<>();
            for (ASTNode element : node.getChildren()) {
                TemplateContext.Value item = evaluate(element, scope);
                items.add(item != null ? item : TemplateContext.Value.of(""));
            }
            return TemplateContext.Value.ofList(items);
        }

        if (node instanceof DictLiteralNode) {
            Map<String, TemplateContext.Value> fields = new LinkedHashMap<>();
            for (ASTNode entry : node.getChildren()) {
                if (entry instanceof DictEntryNode) {
                    DictEntryNode dictEntry = (DictEntryNode) entry;
                    TemplateContext.Value value = evaluate(firstChild(dictEntry), scope);
                    fields.put(stripQuotes(dictEntry.getKey()),
                            value != null ? value : TemplateContext.Value.of(""));
                }
            }
            return TemplateContext.Value.ofDict(fields);
        }

        if (node instanceof FunctionCallNode) {
            return evaluateCall((FunctionCallNode) node, scope);
        }

        if (node instanceof BinaryOpNode) {
            return evaluateBinaryOp((BinaryOpNode) node, scope);
        }

        // Anything else (index access, request.form.get, ...) is not foldable.
        return null;
    }

    /** Only len() is constant-foldable; every other call needs a running app. */
    private TemplateContext.Value evaluateCall(FunctionCallNode call,
                                               Map<String, TemplateContext.Value> scope) {
        if (!"len".equals(call.getFunctionName())) {
            return null;
        }
        for (ASTNode arg : call.getChildren()) {
            ASTNode valueNode = (arg instanceof ArgumentNode) ? firstChild(arg) : arg;
            TemplateContext.Value value = evaluate(valueNode, scope);
            if (value != null && value.isList()) {
                TemplateContext.Value folded =
                        TemplateContext.Value.of(String.valueOf(value.getList().size()));
                // Remember that this literal is really a row count. Folding loses
                // that, and a live server has to recompute it after every add or
                // delete — otherwise the page shows a stale total.
                countValues.add(folded);
                return folded;
            }
        }
        return null;
    }

    /** Fold integer arithmetic, which is what route maths like id - 1 needs. */
    private TemplateContext.Value evaluateBinaryOp(BinaryOpNode op,
                                                   Map<String, TemplateContext.Value> scope) {
        if (op.getChildren().size() < 2) {
            return null;
        }
        TemplateContext.Value left = evaluate(op.getChildren().get(0), scope);
        TemplateContext.Value right = evaluate(op.getChildren().get(1), scope);

        // The parser models subscripting (products[index]) as a "[]" operation.
        if ("[]".equals(op.getOperator())) {
            return evaluateIndex(left, right);
        }

        if (left == null || right == null || !left.isScalar() || !right.isScalar()) {
            return null;
        }

        Integer a = asInt(left.getScalar());
        Integer b = asInt(right.getScalar());
        if (a == null || b == null) {
            // String concatenation is the only non-numeric case worth folding.
            if ("+".equals(op.getOperator())) {
                return TemplateContext.Value.of(left.getScalar() + right.getScalar());
            }
            return null;
        }

        switch (op.getOperator()) {
            case "+": return TemplateContext.Value.of(String.valueOf(a + b));
            case "-": return TemplateContext.Value.of(String.valueOf(a - b));
            case "*": return TemplateContext.Value.of(String.valueOf(a * b));
            case "/": return b == 0 ? null : TemplateContext.Value.of(String.valueOf(a / b));
            default:  return null;
        }
    }

    /**
     * Evaluate {@code collection[key]} — a list subscripted by an integer, or a
     * dict subscripted by a field name. Out-of-range indices yield null so the
     * expression is reported as unevaluable rather than silently wrong.
     */
    private TemplateContext.Value evaluateIndex(TemplateContext.Value collection,
                                                TemplateContext.Value key) {
        if (collection == null || key == null || !key.isScalar()) {
            return null;
        }

        if (collection.isList()) {
            Integer index = asInt(key.getScalar());
            if (index == null) {
                return null;
            }
            List<TemplateContext.Value> items = collection.getList();
            // Python's negative indexing counts back from the end.
            int resolved = index < 0 ? items.size() + index : index;
            if (resolved < 0 || resolved >= items.size()) {
                return null;
            }
            return items.get(resolved);
        }

        if (collection.isDict()) {
            return collection.field(key.getScalar());
        }

        return null;
    }

    // ------------------------------------------------------------------
    // helpers
    // ------------------------------------------------------------------

    private static Integer asInt(String s) {
        try {
            return Integer.valueOf(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static ASTNode firstChild(ASTNode node) {
        if (node == null || node.getChildren().isEmpty()) {
            return null;
        }
        return node.getChildren().get(0);
    }

    /** Pull the first '...' or "..." out of a decorator expression. */
    private static String firstQuotedString(String expr) {
        if (expr == null) {
            return null;
        }
        for (char quote : new char[]{'\'', '"'}) {
            int start = expr.indexOf(quote);
            if (start >= 0) {
                int end = expr.indexOf(quote, start + 1);
                if (end > start) {
                    return expr.substring(start + 1, end);
                }
            }
        }
        return null;
    }

    /** Strip a matching pair of surrounding quotes, if present. */
    public static String stripQuotes(String s) {
        if (s == null || s.length() < 2) {
            return s;
        }
        char first = s.charAt(0);
        char last = s.charAt(s.length() - 1);
        if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }
}
