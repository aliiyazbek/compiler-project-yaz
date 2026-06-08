package ast.backend;

import ast.base.ASTNode;

/**
 * An "elif" branch of an if-statement. Children: a ConditionNode followed by a
 * BlockNode for the branch body.
 */
public class ElifBranchNode extends ASTNode {

    public ElifBranchNode(int lineNumber) {
        super("ElifBranch", lineNumber);
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s", lineNumber, nodeName);
    }
}
