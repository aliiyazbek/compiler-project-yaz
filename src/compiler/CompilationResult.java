package compiler;

import ast.base.ASTNode;
import symboltable.SymbolTable;

/**
 * The outcome of compiling one source file: how many tokens / parse-tree nodes
 * / AST nodes were produced, the root AST node, any symbol table built, and the
 * syntax-error count. Replaces the static int totalErrors + ad-hoc prints that
 * were copy-pasted across the four driver classes.
 */
public class CompilationResult {

    private final String filePath;
    private boolean success;
    private int tokenCount;
    private int parseTreeNodeCount;
    private int astNodeCount;
    private int syntaxErrors;
    private ASTNode ast;
    private SymbolTable symbolTable;

    public CompilationResult(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTokenCount() {
        return tokenCount;
    }

    public void setTokenCount(int tokenCount) {
        this.tokenCount = tokenCount;
    }

    public int getParseTreeNodeCount() {
        return parseTreeNodeCount;
    }

    public void setParseTreeNodeCount(int parseTreeNodeCount) {
        this.parseTreeNodeCount = parseTreeNodeCount;
    }

    public int getAstNodeCount() {
        return astNodeCount;
    }

    public void setAstNodeCount(int astNodeCount) {
        this.astNodeCount = astNodeCount;
    }

    public int getSyntaxErrors() {
        return syntaxErrors;
    }

    public void setSyntaxErrors(int syntaxErrors) {
        this.syntaxErrors = syntaxErrors;
    }

    public ASTNode getAst() {
        return ast;
    }

    public void setAst(ASTNode ast) {
        this.ast = ast;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }
}
