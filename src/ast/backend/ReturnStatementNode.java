package ast.backend;

import ast.base.ASTNode;

public class ReturnStatementNode extends ASTNode {

    public ReturnStatementNode(int lineNumber) {
        super("ReturnStatement", lineNumber);
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s",
                lineNumber, nodeName);
    }
}
