package ast.frontend;

import ast.base.ASTNode;


public class JinjaForNode extends ASTNode {
    private final String iteratorVariable;
    private final String collectionExpression;

    public JinjaForNode(int lineNumber, String iteratorVariable, String collectionExpression) {
        super("JinjaFor", lineNumber);
        this.iteratorVariable = iteratorVariable;
        this.collectionExpression = collectionExpression;
    }

    public String getIteratorVariable() {
        return iteratorVariable;
    }

    public String getCollectionExpression() {
        return collectionExpression;
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s: for %s in %s",
                lineNumber, nodeName, iteratorVariable, collectionExpression);
    }
}
