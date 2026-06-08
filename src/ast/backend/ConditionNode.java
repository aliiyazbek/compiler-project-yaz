package ast.backend;

import ast.base.ASTNode;

/**
 * Wraps the condition expression of an if/elif branch. The condition expression
 * is attached as the single child, so it is clearly distinguished from the
 * branch body rather than being a bare sibling of the body statements.
 */
public class ConditionNode extends ASTNode {

    public ConditionNode(int lineNumber) {
        super("Condition", lineNumber);
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s", lineNumber, nodeName);
    }
}
