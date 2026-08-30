package ast.backend;

import ast.base.ASTNode;

/**
 * A function together with the decorators applied to it, such as a Flask view
 * behind {@code @app.route('/')}.
 *
 * The decorators and the function are separate children rather than fields, so
 * each keeps its own line number and prints as the node it is.
 */
public class DecoratedFunctionNode extends ASTNode {

    public DecoratedFunctionNode(int lineNumber) {
        super("DecoratedFunction", lineNumber);
    }

    /** The decorators applied to the function, in source order. */
    public int getDecoratorCount() {
        int count = 0;
        for (ASTNode child : children) {
            if (child instanceof DecoratorNode) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String getNodeInfo() {
        int decorators = getDecoratorCount();
        return String.format("[Line %d] %s (%d decorator%s)",
                lineNumber, nodeName, decorators, decorators == 1 ? "" : "s");
    }
}
