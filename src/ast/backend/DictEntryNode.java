package ast.backend;

import ast.base.ASTNode;

/**
 * A single key/value pair inside a dict literal, e.g. "name": value.
 * The value is attached as the single child; the key is kept here so it is no
 * longer silently dropped during AST construction.
 */
public class DictEntryNode extends ASTNode {
    private final String key;

    public DictEntryNode(int lineNumber, String key) {
        super("DictEntry", lineNumber);
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s: %s", lineNumber, nodeName, key);
    }
}
