package ast.backend;

public class DictLiteralNode extends ExpressionNode {

    public DictLiteralNode(int lineNumber) {
        super("DictLiteral", lineNumber);
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s (%d pairs)",
                lineNumber, nodeName, children.size());
    }
}
