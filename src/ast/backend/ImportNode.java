package ast.backend;

import ast.base.ASTNode;

import java.util.List;

public class ImportNode extends ASTNode {
    private String moduleName;
    private List<String> importedItems;

    public ImportNode(int lineNumber, String moduleName, List<String> importedItems) {
        super("ImportStatement", lineNumber);
        this.moduleName = moduleName;
        this.importedItems = importedItems;
    }

    public String getModuleName() {
        return moduleName;
    }

    public List<String> getImportedItems() {
        return importedItems;
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s: from %s import %s",
                lineNumber, nodeName, moduleName, String.join(", ", importedItems));
    }
}
