package output;

import ast.backend.*;
import ast.base.ASTNode;
import ast.frontend.*;

/**
 * Serialises any AST produced by this compiler into JSON.
 *
 * Every node contributes the three fields it has by virtue of being an
 * {@link ASTNode} — {@code node}, {@code line} and (when non-empty)
 * {@code children}. On top of that, each concrete node type contributes the
 * attributes that actually carry its meaning (a {@code HtmlElement}'s tag name,
 * a {@code JinjaFor}'s iterator and collection, an {@code Assignment}'s target
 * variable, and so on) so the dumps are readable rather than a shapeless tree of
 * node names.
 *
 * Used to produce {@code compiler_output/ast_python.json} and
 * {@code compiler_output/ast_jinja.json}.
 */
public class AstJsonSerializer {

    /** Serialise a single tree, wrapped in a small envelope naming its source. */
    public static String serialize(String sourceFile, ASTNode root) {
        JsonWriter w = new JsonWriter();
        w.beginObject();
        w.field("source", sourceFile == null ? "" : sourceFile.replace('\\', '/'));
        w.field("nodeCount", count(root));
        if (root == null) {
            w.field("ast", "");
        } else {
            w.beginArray("ast");
            writeNode(w, root);
            w.endArray();
        }
        w.endObject();
        return w.toString();
    }

    /**
     * Serialise several trees under one root object — used for the Jinja dump,
     * where every template in {@code templates/} goes into a single file.
     */
    public static String serializeAll(String[] sourceFiles, ASTNode[] roots) {
        JsonWriter w = new JsonWriter();
        w.beginObject();
        w.field("templateCount", roots.length);
        w.beginArray("templates");
        for (int i = 0; i < roots.length; i++) {
            w.beginArrayElement();
            String src = sourceFiles[i] == null ? "" : sourceFiles[i].replace('\\', '/');
            w.field("source", src);
            w.field("nodeCount", count(roots[i]));
            if (roots[i] != null) {
                w.beginArray("ast");
                writeNode(w, roots[i]);
                w.endArray();
            }
            w.endObject();
        }
        w.endArray();
        w.endObject();
        return w.toString();
    }

    private static int count(ASTNode node) {
        if (node == null) {
            return 0;
        }
        int total = 1;
        for (ASTNode child : node.getChildren()) {
            total += count(child);
        }
        return total;
    }

    private static void writeNode(JsonWriter w, ASTNode node) {
        w.beginArrayElement();
        w.field("node", node.getNodeName());
        w.field("line", node.getLineNumber());
        writeAttributes(w, node);

        if (!node.getChildren().isEmpty()) {
            w.beginArray("children");
            for (ASTNode child : node.getChildren()) {
                writeNode(w, child);
            }
            w.endArray();
        }
        w.endObject();
    }

    /** Emit the fields specific to this node's concrete type. */
    private static void writeAttributes(JsonWriter w, ASTNode node) {
        // ---- frontend (HTML / Jinja / CSS) ----
        if (node instanceof HtmlElementNode) {
            HtmlElementNode n = (HtmlElementNode) node;
            w.field("tag", n.getTagName());
            w.field("selfClosing", n.isSelfClosing());

        } else if (node instanceof HtmlAttributeNode) {
            HtmlAttributeNode n = (HtmlAttributeNode) node;
            w.field("attribute", n.getAttributeName());
            if (n.getAttributeValue() != null) {
                w.field("value", n.getAttributeValue());
            }

        } else if (node instanceof TextNode) {
            w.field("text", ((TextNode) node).getText());

        } else if (node instanceof JinjaExpressionNode) {
            w.field("expression", ((JinjaExpressionNode) node).getExpressionText());

        } else if (node instanceof JinjaForNode) {
            JinjaForNode n = (JinjaForNode) node;
            w.field("iterator", n.getIteratorVariable());
            w.field("collection", n.getCollectionExpression());

        } else if (node instanceof JinjaIfNode) {
            w.field("condition", ((JinjaIfNode) node).getCondition());

        } else if (node instanceof JinjaBlockNode) {
            w.field("block", ((JinjaBlockNode) node).getBlockName());

        } else if (node instanceof JinjaExtendsNode) {
            w.field("extends", ((JinjaExtendsNode) node).getTemplateName());

        } else if (node instanceof JinjaBoundDataNode) {
            JinjaBoundDataNode n = (JinjaBoundDataNode) node;
            w.field("binding", n.getBinding());
            w.field("resolvedValue", n.getResolvedValue());

        } else if (node instanceof CssSelectorNode) {
            w.field("selector", ((CssSelectorNode) node).getSelector());

        } else if (node instanceof CssDeclarationNode) {
            CssDeclarationNode n = (CssDeclarationNode) node;
            w.field("property", n.getProperty());
            w.field("value", n.getValue());

        // ---- backend (Python / Flask) ----
        } else if (node instanceof AssignmentNode) {
            w.field("variable", ((AssignmentNode) node).getVariableName());

        } else if (node instanceof FunctionDefNode) {
            FunctionDefNode n = (FunctionDefNode) node;
            w.field("function", n.getFunctionName());
            w.beginArray("parameters");
            for (String p : n.getParameters()) {
                w.value(p);
            }
            w.endArray();

        } else if (node instanceof FunctionCallNode) {
            w.field("call", ((FunctionCallNode) node).getFunctionName());

        } else if (node instanceof ArgumentNode) {
            ArgumentNode n = (ArgumentNode) node;
            if (n.getName() != null) {
                w.field("name", n.getName());
            }
            w.field("keyword", n.isKeyword());

        } else if (node instanceof DecoratorNode) {
            w.field("decorator", ((DecoratorNode) node).getDecoratorExpression());

        } else if (node instanceof LiteralNode) {
            LiteralNode n = (LiteralNode) node;
            w.field("value", n.getValue());
            w.field("literalType", n.getType());

        } else if (node instanceof IdentifierNode) {
            w.field("identifier", ((IdentifierNode) node).getName());

        } else if (node instanceof BinaryOpNode) {
            w.field("operator", ((BinaryOpNode) node).getOperator());

        } else if (node instanceof DictEntryNode) {
            w.field("key", ((DictEntryNode) node).getKey());

        } else if (node instanceof ImportNode) {
            ImportNode n = (ImportNode) node;
            w.field("module", n.getModuleName());
            w.beginArray("imports");
            for (String item : n.getImportedItems()) {
                w.value(item);
            }
            w.endArray();
        }
    }
}
