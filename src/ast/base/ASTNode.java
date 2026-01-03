package ast.base;

import java.util.ArrayList;
import java.util.List;


public abstract class ASTNode {
    protected String nodeName;
    protected int lineNumber;
    protected List<ASTNode> children;

    public ASTNode(String nodeName, int lineNumber) {
        this.nodeName = nodeName;
        this.lineNumber = lineNumber;
        this.children = new ArrayList<>();
    }

    public String getNodeName() {
        return nodeName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public List<ASTNode> getChildren() {
        return children;
    }

    public void addChild(ASTNode child) {
        if (child != null) {
            children.add(child);
        }
    }

    public void addChildren(List<ASTNode> children) {
        if (children != null) {
            this.children.addAll(children);
        }
    }


    public abstract String getNodeInfo();


    public void printTree(int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("  ");
        }

        System.out.println(getNodeInfo());

        for (ASTNode child : children) {
            child.printTree(indent + 1);
        }
    }


    public void printTree() {
        printTree(0);
    }

    @Override
    public String toString() {
        return getNodeInfo();
    }
}
