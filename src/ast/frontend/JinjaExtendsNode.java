package ast.frontend;

import ast.base.ASTNode;


public class JinjaExtendsNode extends ASTNode {
    private final String templateName;

    public JinjaExtendsNode(int lineNumber, String templateName) {
        super("JinjaExtends", lineNumber);
        this.templateName = templateName;
    }

    public String getTemplateName() {
        return templateName;
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s: extends %s",
                lineNumber, nodeName, templateName);
    }
}
