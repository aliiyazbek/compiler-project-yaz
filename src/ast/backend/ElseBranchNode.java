package ast.backend;

import ast.base.ASTNode;

/**
 * An "else" branch of an if-statement. Its single child is a BlockNode holding
 * the else body.
 */
public class ElseBranchNode extends ASTNode {

    public ElseBranchNode(int lineNumber) {
        super("ElseBranch", lineNumber);
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s", lineNumber, nodeName);
    }
}
