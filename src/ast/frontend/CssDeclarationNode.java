package ast.frontend;

import ast.base.ASTNode;


public class CssDeclarationNode extends ASTNode {
    private final String property;
    private final String value;

    public CssDeclarationNode(int lineNumber, String property, String value) {
        super("CssDeclaration", lineNumber);
        this.property = property;
        this.value = value;
    }

    public String getProperty() {
        return property;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s: %s: %s",
                lineNumber, nodeName, property, value);
    }
}
