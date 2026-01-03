package ast.backend;

import ast.base.ASTNode;

public class ForStatementNode extends ASTNode {
    private final String iteratorVariable;

    public ForStatementNode(int lineNumber, String iteratorVariable) {
        super("ForStatement", lineNumber);
        this.iteratorVariable = iteratorVariable;
    }

    public String getIteratorVariable() {
        return iteratorVariable;
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s: for %s in ...",
                lineNumber, nodeName, iteratorVariable);
    }
}
