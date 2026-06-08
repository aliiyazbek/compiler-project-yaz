package ast.backend;

import ast.base.ASTNode;

/**
 * An argument in a function call. For a keyword argument (name=value) the name
 * is preserved here; for a positional argument the name is null. Previously the
 * keyword name was discarded, making render_template('x', products=products)
 * indistinguishable from a positional call.
 */
public class ArgumentNode extends ASTNode {
    private final String name; // null for positional arguments

    public ArgumentNode(int lineNumber, String name) {
        super(name == null ? "PositionalArg" : "KeywordArg", lineNumber);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isKeyword() {
        return name != null;
    }

    @Override
    public String getNodeInfo() {
        if (isKeyword()) {
            return String.format("[Line %d] %s: %s=", lineNumber, nodeName, name);
        }
        return String.format("[Line %d] %s", lineNumber, nodeName);
    }
}
