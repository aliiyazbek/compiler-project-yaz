package ast.backend;

import ast.base.ASTNode;

public class ProgramNode extends ASTNode {

    public ProgramNode(int lineNumber) {
        super("Program", lineNumber);
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s (%d statements)",
                lineNumber, nodeName, children.size());
    }
}
