package ast.frontend;

import ast.base.ASTNode;


public class JinjaBlockNode extends ASTNode {
    private final String blockName;

    public JinjaBlockNode(int lineNumber, String blockName) {
        super("JinjaBlock", lineNumber);
        this.blockName = blockName;
    }

    public String getBlockName() {
        return blockName;
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s: block %s",
                lineNumber, nodeName, blockName);
    }
}
