package ast.frontend;

import ast.base.ASTNode;


public class CssRuleNode extends ASTNode {

    public CssRuleNode(int lineNumber) {
        super("CssRule", lineNumber);
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s",
                lineNumber, nodeName);
    }
}
