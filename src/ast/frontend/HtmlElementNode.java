package ast.frontend;

import ast.base.ASTNode;


public class HtmlElementNode extends ASTNode {
    private final String tagName;
    private final boolean isSelfClosing;

    public HtmlElementNode(int lineNumber, String tagName, boolean isSelfClosing) {
        super("HtmlElement", lineNumber);
        this.tagName = tagName;
        this.isSelfClosing = isSelfClosing;
    }

    public String getTagName() {
        return tagName;
    }

    public boolean isSelfClosing() {
        return isSelfClosing;
    }

    @Override
    public String getNodeInfo() {
        if (isSelfClosing) {
            return String.format("[Line %d] %s: <%s />",
                    lineNumber, nodeName, tagName);
        } else {
            return String.format("[Line %d] %s: <%s>...<%s>",
                    lineNumber, nodeName, tagName, tagName);
        }
    }
}
