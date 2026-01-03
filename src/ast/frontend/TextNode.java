package ast.frontend;

import ast.base.ASTNode;


public class TextNode extends ASTNode {
    private final String text;

    public TextNode(int lineNumber, String text) {
        super("Text", lineNumber);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String getNodeInfo() {
        String displayText = text.length() > 30 ? text.substring(0, 30) + "..." : text;
        return String.format("[Line %d] %s: \"%s\"",
                lineNumber, nodeName, displayText);
    }
}
