package ast.backend;

import ast.base.ASTNode;

public class DecoratorNode extends ASTNode {
    private String decoratorExpression;

    public DecoratorNode(int lineNumber, String decoratorExpression) {
        super("Decorator", lineNumber);
        this.decoratorExpression = decoratorExpression;
    }

    public String getDecoratorExpression() {
        return decoratorExpression;
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s: @%s",
                lineNumber, nodeName, decoratorExpression);
    }
}
