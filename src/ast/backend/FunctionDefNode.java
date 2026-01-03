package ast.backend;

import ast.base.ASTNode;

import java.util.List;

public class FunctionDefNode extends ASTNode {
    private final String functionName;
    private final List<String> parameters;

    public FunctionDefNode(int lineNumber, String functionName, List<String> parameters) {
        super("FunctionDefinition", lineNumber);
        this.functionName = functionName;
        this.parameters = parameters;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<String> getParameters() {
        return parameters;
    }

    @Override
    public String getNodeInfo() {
        String params = parameters.isEmpty() ? "" : String.join(", ", parameters);
        return String.format("[Line %d] %s: %s(%s)",
                lineNumber, nodeName, functionName, params);
    }
}
