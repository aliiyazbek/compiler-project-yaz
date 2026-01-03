package ast.backend;

public class ListLiteralNode extends ExpressionNode {

    public ListLiteralNode(int lineNumber) {
        super("ListLiteral", lineNumber);
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s (%d elements)",
                lineNumber, nodeName, children.size());
    }
}
