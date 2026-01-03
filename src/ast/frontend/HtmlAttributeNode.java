package ast.frontend;

import ast.base.ASTNode;


public class HtmlAttributeNode extends ASTNode {
    private final String attributeName;
    private final String attributeValue;

    public HtmlAttributeNode(int lineNumber, String attributeName, String attributeValue) {
        super("HtmlAttribute", lineNumber);
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    @Override
    public String getNodeInfo() {
        if (attributeValue != null) {
            return String.format("[Line %d] %s: %s=%s",
                    lineNumber, nodeName, attributeName, attributeValue);
        } else {
            return String.format("[Line %d] %s: %s",
                    lineNumber, nodeName, attributeName);
        }
    }
}
