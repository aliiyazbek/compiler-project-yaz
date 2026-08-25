package generator;

import ast.backend.*;
import ast.base.ASTNode;
import ast.frontend.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The Generator — requirement #2's data-passing step.
 *
 * It takes the <b>first tree</b> (the Python/Flask AST) and the <b>second tree</b>
 * (the HTML + Jinja2 AST) and carries the data from the Python data array into
 * the Jinja tree. Concretely:
 *
 * <ol>
 *   <li>It scans the Python AST for global list-of-dict assignments (the
 *       {@code products = [...]} data array) and records them as {@link DataRecord}s.</li>
 *   <li>It scans each route function's {@code render_template('x.html', products=products)}
 *       call to learn, for a given template, which template variable is fed by which
 *       Python data array (the "context").</li>
 *   <li>It walks the Jinja tree and, wherever a {@code {% for item in products %}}
 *       loop or a {@code {{ item.field }}} expression refers to that context variable,
 *       it injects {@link JinjaBoundDataNode}s holding the concrete values pulled from
 *       the Python data — so the second tree now contains the actual data, not just
 *       the variable names that were written in the template.</li>
 * </ol>
 *
 * This is intentionally a syntax-directed, best-effort binding (it covers the
 * shapes the sample Flask app uses: a global list of string/number dicts, passed
 * by keyword to render_template, iterated and member-accessed in the template).
 * Anything it cannot resolve is left untouched.
 */
public class Generator {

    /** One global data array found in the Python code: name -> list of row maps. */
    public static class DataRecord {
        public final String variableName;
        // Each row is an ordered map of field name -> literal value.
        public final List<Map<String, String>> rows = new ArrayList<>();

        DataRecord(String variableName) {
            this.variableName = variableName;
        }
    }

    /** Global data arrays discovered in the Python AST, keyed by variable name. */
    private final Map<String, DataRecord> dataArrays = new HashMap<>();

    /**
     * Per-template context: template file name -> (template var -> python var).
     * Built from render_template(...) keyword arguments.
     */
    private final Map<String, Map<String, String>> templateContexts = new HashMap<>();

    private int bindingsInjected;

    public int getBindingsInjected() {
        return bindingsInjected;
    }

    public Map<String, DataRecord> getDataArrays() {
        return dataArrays;
    }

    // ------------------------------------------------------------------
    // Phase 1: read the Python (first) tree.
    // ------------------------------------------------------------------

    /** Scan the Python AST for global data arrays and render_template contexts. */
    public void collectFromBackend(ASTNode pythonRoot) {
        if (pythonRoot == null) {
            return;
        }
        collectDataArrays(pythonRoot);
        collectRenderContexts(pythonRoot);
    }

    private void collectDataArrays(ASTNode node) {
        if (node instanceof AssignmentNode) {
            AssignmentNode assign = (AssignmentNode) node;
            ASTNode value = firstChild(assign);
            if (value instanceof ListLiteralNode) {
                DataRecord record = readListOfDicts(assign.getVariableName(), (ListLiteralNode) value);
                if (!record.rows.isEmpty()) {
                    dataArrays.put(record.variableName, record);
                }
            }
        }
        for (ASTNode child : node.getChildren()) {
            collectDataArrays(child);
        }
    }

    /** Turn a Python list-of-dicts AST node into plain ordered row maps. */
    private DataRecord readListOfDicts(String varName, ListLiteralNode list) {
        DataRecord record = new DataRecord(varName);
        for (ASTNode element : list.getChildren()) {
            if (element instanceof DictLiteralNode) {
                Map<String, String> row = new LinkedHashMap<>();
                for (ASTNode entry : element.getChildren()) {
                    if (entry instanceof DictEntryNode) {
                        DictEntryNode dictEntry = (DictEntryNode) entry;
                        ASTNode val = firstChild(dictEntry);
                        row.put(stripQuotes(dictEntry.getKey()), literalText(val));
                    }
                }
                if (!row.isEmpty()) {
                    record.rows.add(row);
                }
            }
        }
        return record;
    }

    private void collectRenderContexts(ASTNode node) {
        if (node instanceof FunctionCallNode) {
            FunctionCallNode call = (FunctionCallNode) node;
            if ("render_template".equals(call.getFunctionName())) {
                readRenderTemplate(call);
            }
        }
        for (ASTNode child : node.getChildren()) {
            collectRenderContexts(child);
        }
    }

    private void readRenderTemplate(FunctionCallNode call) {
        String templateName = null;
        Map<String, String> context = new HashMap<>();

        for (ASTNode arg : call.getChildren()) {
            if (!(arg instanceof ArgumentNode)) {
                continue;
            }
            ArgumentNode argument = (ArgumentNode) arg;
            ASTNode value = firstChild(argument);
            if (!argument.isKeyword()) {
                // First positional argument is the template file name string.
                if (templateName == null && value instanceof LiteralNode) {
                    templateName = stripQuotes(((LiteralNode) value).getValue());
                }
            } else if (value instanceof IdentifierNode) {
                // products=products  ->  templateVar "products" fed by python var "products"
                context.put(argument.getName(), ((IdentifierNode) value).getName());
            }
        }

        if (templateName != null && !context.isEmpty()) {
            templateContexts.put(templateName, context);
        }
    }

    // ------------------------------------------------------------------
    // Phase 2: bind into the Jinja (second) tree.
    // ------------------------------------------------------------------

    /**
     * Walk the Jinja AST for the given template file and inject the Python data
     * into it. Returns the number of binding nodes added.
     */
    public int generateInto(String templateFileName, ASTNode jinjaRoot) {
        bindingsInjected = 0;
        if (jinjaRoot == null) {
            return 0;
        }
        Map<String, String> context = templateContexts.get(baseName(templateFileName));
        if (context == null) {
            return 0; // no render_template fed this template; nothing to bind
        }
        // Start with an empty binding environment: template var -> resolved scalar.
        bindNode(jinjaRoot, context, new HashMap<>());
        return bindingsInjected;
    }

    /**
     * @param env maps an in-scope simple template variable name to a concrete
     *            scalar string (used to resolve {{ x }} where x is a string field).
     */
    private void bindNode(ASTNode node, Map<String, String> context, Map<String, String> env) {
        if (node instanceof JinjaForNode) {
            bindForLoop((JinjaForNode) node, context, env);
            return; // bindForLoop recurses with the per-row environment itself
        }

        if (node instanceof JinjaExpressionNode) {
            bindExpression((JinjaExpressionNode) node, env);
        }

        for (ASTNode child : new ArrayList<>(node.getChildren())) {
            bindNode(child, context, env);
        }
    }

    /** {% for product in products %} -> attach one binding row per Python element. */
    private void bindForLoop(JinjaForNode forNode, Map<String, String> context, Map<String, String> env) {
        String collection = forNode.getCollectionExpression(); // e.g. "products"
        String pythonVar = context.get(collection);
        DataRecord data = pythonVar != null ? dataArrays.get(pythonVar) : null;

        if (data == null) {
            // Unknown collection: still recurse so nested loops/expressions are visited.
            for (ASTNode child : new ArrayList<>(forNode.getChildren())) {
                bindNode(child, context, env);
            }
            return;
        }

        String iterVar = forNode.getIteratorVariable(); // e.g. "product"
        int index = 0;
        for (Map<String, String> row : data.rows) {
            // Record which Python row this iteration corresponds to.
            JinjaBoundDataNode iterationMarker = new JinjaBoundDataNode(
                    forNode.getLineNumber(),
                    iterVar + "[" + index + "]",
                    "{" + joinRow(row) + "}");
            forNode.addChild(iterationMarker);
            bindingsInjected++;

            // Build the environment for member accesses on the iterator variable,
            // e.g. product.name -> "Ali Yazbek".
            Map<String, String> rowEnv = new HashMap<>(env);
            for (Map.Entry<String, String> field : row.entrySet()) {
                rowEnv.put(iterVar + "." + field.getKey(), field.getValue());
            }

            // Re-bind the loop body once per row, resolving expressions concretely.
            for (ASTNode child : new ArrayList<>(forNode.getChildren())) {
                if (child instanceof JinjaBoundDataNode) {
                    continue; // skip markers we just added
                }
                bindNode(child, context, rowEnv);
            }
            index++;
        }
    }

    /** {{ product.name }} / {{ total }} -> attach the concrete value if known. */
    private void bindExpression(JinjaExpressionNode expr, Map<String, String> env) {
        String key = expr.getExpressionText().trim();
        String resolved = env.get(key);
        if (resolved != null) {
            expr.addChild(new JinjaBoundDataNode(expr.getLineNumber(), key, resolved));
            bindingsInjected++;
        }
    }

    // ------------------------------------------------------------------
    // helpers
    // ------------------------------------------------------------------

    private static ASTNode firstChild(ASTNode node) {
        if (node == null || node.getChildren().isEmpty()) {
            return null;
        }
        return node.getChildren().get(0);
    }

    /** Render an expression AST node back to a short literal/text form. */
    private static String literalText(ASTNode node) {
        if (node instanceof LiteralNode) {
            return stripQuotes(((LiteralNode) node).getValue());
        }
        if (node instanceof IdentifierNode) {
            return ((IdentifierNode) node).getName();
        }
        if (node == null) {
            return "";
        }
        return node.getNodeInfo();
    }

    private static String stripQuotes(String s) {
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

    private static String joinRow(Map<String, String> row) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> e : row.entrySet()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(e.getKey()).append(": ").append(e.getValue());
        }
        return sb.toString();
    }

    /** Reduce "templates/products.html" or "products.html" to "products.html". */
    private static String baseName(String path) {
        if (path == null) {
            return null;
        }
        String norm = path.replace('\\', '/');
        int slash = norm.lastIndexOf('/');
        return slash >= 0 ? norm.substring(slash + 1) : norm;
    }
}
