package ast.backend;

public class LiteralNode extends ExpressionNode {
    private String value;
    private String type; // string, integer, float, boolean, none

    public LiteralNode(int lineNumber, String value, String type) {
        super("Literal", lineNumber);
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s (%s): %s",
                lineNumber, nodeName, type, value);
    }
}
