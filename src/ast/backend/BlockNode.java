package ast.backend;

import ast.base.ASTNode;

/**
 * A block of statements (a suite). Used as the body of an if/elif/else branch
 * and the body of a for loop, so condition, body, and else-branch are no longer
 * flattened into one undifferentiated list of siblings.
 */
public class BlockNode extends ASTNode {
    private final String label; // e.g. "then", "else", "body"

    public BlockNode(int lineNumber, String label) {
        super("Block", lineNumber);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s (%s)", lineNumber, nodeName, label);
    }
}
