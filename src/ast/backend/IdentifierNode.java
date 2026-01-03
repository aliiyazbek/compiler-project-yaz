package ast.backend;

public class IdentifierNode extends ExpressionNode {
    private final String name;

    public IdentifierNode(int lineNumber, String name) {
        super("Identifier", lineNumber);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s: %s",
                lineNumber, nodeName, name);
    }
}
