package ast.frontend;

import ast.base.ASTNode;


public class CssSelectorNode extends ASTNode {
    private final String selector;

    public CssSelectorNode(int lineNumber, String selector) {
        super("CssSelector", lineNumber);
        this.selector = selector;
    }

    public String getSelector() {
        return selector;
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s: %s",
                lineNumber, nodeName, selector);
    }
}
