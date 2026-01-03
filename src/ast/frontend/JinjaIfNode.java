package ast.frontend;

import ast.base.ASTNode;


public class JinjaIfNode extends ASTNode {
    private final String condition;

    public JinjaIfNode(int lineNumber, String condition) {
        super("JinjaIf", lineNumber);
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s: if %s",
                lineNumber, nodeName, condition);
    }
}
