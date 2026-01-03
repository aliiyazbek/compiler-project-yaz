package ast.backend;

import ast.base.ASTNode;

public class IfStatementNode extends ASTNode {

    public IfStatementNode(int lineNumber) {
        super("IfStatement", lineNumber);
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s",
                lineNumber, nodeName);
    }
}
