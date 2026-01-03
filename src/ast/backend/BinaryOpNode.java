package ast.backend;

public class BinaryOpNode extends ExpressionNode {
    private String operator;

    public BinaryOpNode(int lineNumber, String operator) {
        super("BinaryOperation", lineNumber);
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s: %s",
                lineNumber, nodeName, operator);
    }
}
