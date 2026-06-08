package compiler;

import antlr.backend.*;
import antlr.frontend.*;
import ast.base.ASTNode;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import visitor.BackendASTVisitor;
import visitor.FrontendASTVisitor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The reusable compilation pipeline: lexer -> parser -> AST visitor -> symbol
 * table. Previously this logic was copy-pasted (as static testBackend /
 * testFrontend / testCSS methods) into all four *Test driver classes; it now
 * lives in one place and returns a {@link CompilationResult} instead of mutating
 * shared static state.
 */
public class Compiler {

    /** Compile a Python/Flask backend file. */
    public CompilationResult compileBackend(String filePath) {
        CompilationResult result = new CompilationResult(filePath);
        try {
            String input = new String(Files.readAllBytes(Paths.get(filePath)));

            PythonFlaskLexer lexer = new PythonFlaskLexer(CharStreams.fromString(input));
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            PythonFlaskParser parser = new PythonFlaskParser(tokens);
            attachErrorListener(parser);

            ParseTree tree = parser.program();
            tokens.fill();
            result.setTokenCount(tokens.size());
            result.setParseTreeNodeCount(countParseTreeNodes(tree));
            result.setSyntaxErrors(parser.getNumberOfSyntaxErrors());

            if (parser.getNumberOfSyntaxErrors() == 0) {
                BackendASTVisitor visitor = new BackendASTVisitor();
                ASTNode ast = visitor.visit(tree);
                result.setAst(ast);
                result.setAstNodeCount(countAstNodes(ast));
                result.setSymbolTable(visitor.getSymbolTable());
                result.setSuccess(true);
            }
        } catch (IOException e) {
            System.err.println("  Failed to read file: " + e.getMessage());
            result.setSyntaxErrors(result.getSyntaxErrors() + 1);
        }
        return result;
    }

    /** Compile an HTML + Jinja2 frontend template. */
    public CompilationResult compileFrontend(String filePath) {
        return compileFrontendSource(filePath, readFile(filePath));
    }

    /** Compile a standalone CSS file by wrapping it in a &lt;style&gt; element. */
    public CompilationResult compileCss(String filePath) {
        String css = readFile(filePath);
        return compileFrontendSource(filePath, css == null ? null : "<style>" + css + "</style>");
    }

    private CompilationResult compileFrontendSource(String filePath, String input) {
        CompilationResult result = new CompilationResult(filePath);
        if (input == null) {
            result.setSyntaxErrors(1);
            return result;
        }

        FrontendLexer lexer = new FrontendLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        FrontendParser parser = new FrontendParser(tokens);
        attachErrorListener(parser);

        ParseTree tree = parser.document();
        tokens.fill();
        result.setTokenCount(tokens.size());
        result.setParseTreeNodeCount(countParseTreeNodes(tree));
        result.setSyntaxErrors(parser.getNumberOfSyntaxErrors());

        if (parser.getNumberOfSyntaxErrors() == 0) {
            FrontendASTVisitor visitor = new FrontendASTVisitor();
            ASTNode ast = visitor.visit(tree);
            result.setAst(ast);
            result.setAstNodeCount(countAstNodes(ast));
            result.setSuccess(true);
        }
        return result;
    }

    private String readFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            System.err.println("  Failed to read file: " + e.getMessage());
            return null;
        }
    }

    private void attachErrorListener(Parser parser) {
        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine, String msg,
                                    RecognitionException e) {
                System.err.println("    ERROR at line " + line + ":" + charPositionInLine + " - " + msg);
            }
        });
    }

    public static int countParseTreeNodes(ParseTree tree) {
        if (tree == null) return 0;
        int count = 1;
        for (int i = 0; i < tree.getChildCount(); i++) {
            count += countParseTreeNodes(tree.getChild(i));
        }
        return count;
    }

    public static int countAstNodes(ASTNode node) {
        if (node == null) return 0;
        int count = 1;
        for (ASTNode child : node.getChildren()) {
            count += countAstNodes(child);
        }
        return count;
    }
}
