package ast.frontend;

import ast.base.ASTNode;

/**
 * One arm of a {@code {% if %}} chain: the {@code if}, an {@code elif}, or the
 * {@code else}.
 *
 * Each arm owns its own content, so the branches stay distinguishable in the
 * tree and the emitter can take exactly one of them. Holding all three arms'
 * children in a single list would make them indistinguishable, and every arm
 * would be emitted together or not at all.
 */
public class JinjaBranchNode extends ASTNode {

    private final String keyword;
    private final String condition;

    public JinjaBranchNode(int lineNumber, String keyword, String condition) {
        super("JinjaBranch", lineNumber);
        this.keyword = keyword;
        this.condition = condition;
    }

    public String getKeyword() {
        return keyword;
    }

    /** The branch's test, or null for {@code else}, which always applies. */
    public String getCondition() {
        return condition;
    }

    public boolean isElse() {
        return condition == null;
    }

    @Override
    public String getNodeInfo() {
        if (condition == null) {
            return String.format("[Line %d] %s: %s", lineNumber, nodeName, keyword);
        }
        return String.format("[Line %d] %s: %s %s",
                lineNumber, nodeName, keyword, condition);
    }
}
