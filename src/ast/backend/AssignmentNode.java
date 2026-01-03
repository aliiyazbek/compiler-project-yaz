package ast.backend;

import ast.base.ASTNode;

public class AssignmentNode extends ASTNode {
    private String variableName;

    public AssignmentNode(int lineNumber, String variableName) {
        super("Assignment", lineNumber);
        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s: %s = ...",
                lineNumber, nodeName, variableName);
    }
}
