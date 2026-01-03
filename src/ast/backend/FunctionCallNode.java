package ast.backend;

public class FunctionCallNode extends ExpressionNode {
    private final String functionName;

    public FunctionCallNode(int lineNumber, String functionName) {
        super("FunctionCall", lineNumber);
        this.functionName = functionName;
    }

    public String getFunctionName() {
        return functionName;
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s: %s(...)",
                lineNumber, nodeName, functionName);
    }
}
