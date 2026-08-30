package ast.base;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public abstract class ASTNode {

    private static final AtomicInteger NODE_COUNTER = new AtomicInteger();

    protected final int nodeId;
    protected String nodeName;
    protected int lineNumber;
    protected List<ASTNode> children;

    public ASTNode(String nodeName, int lineNumber) {
        this.nodeId = NODE_COUNTER.incrementAndGet();
        this.nodeName = nodeName;
        this.lineNumber = lineNumber;
        this.children = new ArrayList<>();
    }

    public int getNodeId() {
        return nodeId;
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


    public String describe() {
        return String.format("#%-4d %s", nodeId, getNodeInfo());
    }


    public void printTree(int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("  ");
        }

        System.out.println(describe());

        for (ASTNode child : children) {
            child.printTree(indent + 1);
        }
    }


    public void printTree() {
        printTree(0);
    }

    @Override
    public String toString() {
        return describe();
    }
}
