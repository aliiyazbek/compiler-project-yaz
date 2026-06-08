package test;

import antlr.backend.PythonFlaskLexer;
import antlr.backend.PythonFlaskParser;
import ast.base.ASTNode;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import visitor.BackendASTVisitor;

import java.util.ArrayList;
import java.util.List;

/** Shared helpers for building ASTs from inline Python/Flask snippets. */
public final class TestSupport {

    private TestSupport() {}

    /**
     * Parse a backend snippet and return the visitor. After this call the
     * visitor exposes both the built AST (via the returned root passed back to
     * the caller) and the populated symbol table (visitor.getSymbolTable()).
     */
    public static BackendASTVisitor parseBackend(String source) {
        PythonFlaskLexer lexer = new PythonFlaskLexer(CharStreams.fromString(source));
        PythonFlaskParser parser = new PythonFlaskParser(new CommonTokenStream(lexer));
        BackendASTVisitor visitor = new BackendASTVisitor();
        visitor.visit(parser.program());
        return visitor;
    }

    /** Build just the AST root for a snippet. */
    public static ASTNode parseBackendAst(String source) {
        PythonFlaskLexer lexer = new PythonFlaskLexer(CharStreams.fromString(source));
        PythonFlaskParser parser = new PythonFlaskParser(new CommonTokenStream(lexer));
        return new BackendASTVisitor().visit(parser.program());
    }

    /** Depth-first collect all nodes of a given class within an AST. */
    public static <T extends ASTNode> List<T> findAll(ASTNode root, Class<T> type) {
        List<T> out = new ArrayList<>();
        collect(root, type, out);
        return out;
    }

    private static <T extends ASTNode> void collect(ASTNode node, Class<T> type, List<T> out) {
        if (node == null) return;
        if (type.isInstance(node)) {
            out.add(type.cast(node));
        }
        for (ASTNode child : node.getChildren()) {
            collect(child, type, out);
        }
    }
}
