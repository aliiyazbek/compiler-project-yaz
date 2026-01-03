package ast.backend;

import ast.base.ASTNode;

public abstract class ExpressionNode extends ASTNode {

    public ExpressionNode(String nodeName, int lineNumber) {
        super(nodeName, lineNumber);
    }
}
