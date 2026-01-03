package ast.frontend;

import ast.base.ASTNode;


public class JinjaExpressionNode extends ASTNode {
    private final String expressionText;

    public JinjaExpressionNode(int lineNumber, String expressionText) {
        super("JinjaExpression", lineNumber);
        this.expressionText = expressionText;
    }

    public String getExpressionText() {
        return expressionText;
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s: {{ %s }}",
                lineNumber, nodeName, expressionText);
    }
}
