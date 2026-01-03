package ast.frontend;

import ast.base.ASTNode;


public class DocumentNode extends ASTNode {

    public DocumentNode(int lineNumber) {
        super("Document", lineNumber);
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s (%d children)",
                lineNumber, nodeName, children.size());
    }
}
