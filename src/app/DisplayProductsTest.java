package app;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import antlr.backend.*;
import antlr.frontend.*;
import ast.base.ASTNode;
import ast.backend.*;
import ast.frontend.*;
import visitor.BackendASTVisitor;
import visitor.FrontendASTVisitor;
import symboltable.SymbolTable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DisplayProductsTest {

    private static int totalErrors = 0;
    private static SymbolTable symbolTable = null;

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║     TEST PROGRAM 1: DISPLAY PRODUCTS - COMPILATION TEST          ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.println("┌──────────────────────────────────────────────────────────────────┐");
        System.out.println("│ PHASE 1: BACKEND COMPILATION (Python/Flask)                      │");
        System.out.println("└──────────────────────────────────────────────────────────────────┘");
        ASTNode backendAST = testBackend("test_programs/run_app.py");
        System.out.println();

        System.out.println("┌──────────────────────────────────────────────────────────────────┐");
        System.out.println("│ PHASE 2: BASE TEMPLATE COMPILATION (HTML + Jinja2)               │");
        System.out.println("└──────────────────────────────────────────────────────────────────┘");
        ASTNode baseAST = testFrontend("test_programs/templates/base.html");
        System.out.println();

        System.out.println("┌──────────────────────────────────────────────────────────────────┐");
        System.out.println("│ PHASE 3: PRODUCTS TEMPLATE COMPILATION (HTML + Jinja2)           │");
        System.out.println("└──────────────────────────────────────────────────────────────────┘");
        ASTNode productsAST = testFrontend("test_programs/templates/products.html");
        System.out.println();

        System.out.println("┌──────────────────────────────────────────────────────────────────┐");
        System.out.println("│ PHASE 4: STYLESHEET COMPILATION (CSS)                            │");
        System.out.println("└──────────────────────────────────────────────────────────────────┘");
        testCSS("test_programs/static/style.css");
        System.out.println();

        System.out.println("┌──────────────────────────────────────────────────────────────────┐");
        System.out.println("│ SYMBOL TABLE (populated via Visitor)                              │");
        System.out.println("└──────────────────────────────────────────────────────────────────┘");
        if (symbolTable != null) {
            symbolTable.print();
        }
        System.out.println();

        if (backendAST != null) {
            System.out.println("┌──────────────────────────────────────────────────────────────────┐");
            System.out.println("│ BACKEND AST (app.py)                                             │");
            System.out.println("└──────────────────────────────────────────────────────────────────┘");
            backendAST.printTree();
            System.out.println();
        }

        if (productsAST != null) {
            System.out.println("┌──────────────────────────────────────────────────────────────────┐");
            System.out.println("│ FRONTEND AST (products.html)                                     │");
            System.out.println("└──────────────────────────────────────────────────────────────────┘");
            productsAST.printTree();
            System.out.println();
        }

        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        if (totalErrors == 0) {
            System.out.println("║  ✓ ALL TESTS PASSED - Display Products compiled successfully!   ║");
        } else {
            System.out.println("║  ✗ TESTS FAILED - " + totalErrors + " error(s) found                              ║");
        }
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
    }

    private static ASTNode testBackend(String filePath) {
        System.out.println("  File: " + filePath);
        try {
            String input = new String(Files.readAllBytes(Paths.get(filePath)));

            CharStream charStream = CharStreams.fromString(input);
            PythonFlaskLexer lexer = new PythonFlaskLexer(charStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            PythonFlaskParser parser = new PythonFlaskParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                        int line, int charPositionInLine, String msg,
                                        RecognitionException e) {
                    System.err.println("    ERROR at line " + line + ":" + charPositionInLine + " - " + msg);
                    totalErrors++;
                }
            });

            ParseTree tree = parser.program();

            if (parser.getNumberOfSyntaxErrors() == 0) {
                System.out.println("  ✓ Lexical Analysis: " + tokens.size() + " tokens");
                System.out.println("  ✓ Syntax Analysis: " + countNodes(tree) + " parse tree nodes");

                BackendASTVisitor visitor = new BackendASTVisitor();
                ASTNode ast = visitor.visit(tree);
                System.out.println("  ✓ AST Construction: " + countASTNodes(ast) + " AST nodes");

                symbolTable = visitor.getSymbolTable();
                System.out.println("  ✓ Symbol Table: " + symbolTable.getAllSymbols().size() + " symbols");

                return ast;
            } else {
                System.out.println("  ✗ Parsing failed with " + parser.getNumberOfSyntaxErrors() + " error(s)");
                return null;
            }

        } catch (IOException e) {
            System.err.println("  ✗ Failed to read file: " + e.getMessage());
            totalErrors++;
            return null;
        }
    }

    private static ASTNode testFrontend(String filePath) {
        System.out.println("  File: " + filePath);
        try {
            String input = new String(Files.readAllBytes(Paths.get(filePath)));

            CharStream charStream = CharStreams.fromString(input);
            FrontendLexer lexer = new FrontendLexer(charStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            FrontendParser parser = new FrontendParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                        int line, int charPositionInLine, String msg,
                                        RecognitionException e) {
                    System.err.println("    ERROR at line " + line + ":" + charPositionInLine + " - " + msg);
                    totalErrors++;
                }
            });

            ParseTree tree = parser.document();

            if (parser.getNumberOfSyntaxErrors() == 0) {
                System.out.println("  ✓ Lexical Analysis: " + tokens.size() + " tokens");
                System.out.println("  ✓ Syntax Analysis: " + countNodes(tree) + " parse tree nodes");

                FrontendASTVisitor visitor = new FrontendASTVisitor();
                ASTNode ast = visitor.visit(tree);
                System.out.println("  ✓ AST Construction: " + countASTNodes(ast) + " AST nodes");

                return ast;
            } else {
                System.out.println("  ✗ Parsing failed with " + parser.getNumberOfSyntaxErrors() + " error(s)");
                return null;
            }

        } catch (IOException e) {
            System.err.println("  ✗ Failed to read file: " + e.getMessage());
            totalErrors++;
            return null;
        }
    }

    private static void testCSS(String filePath) {
        System.out.println("  File: " + filePath);
        try {
            String input = new String(Files.readAllBytes(Paths.get(filePath)));

            String wrappedCSS = "<style>" + input + "</style>";

            CharStream charStream = CharStreams.fromString(wrappedCSS);
            FrontendLexer lexer = new FrontendLexer(charStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            FrontendParser parser = new FrontendParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                        int line, int charPositionInLine, String msg,
                                        RecognitionException e) {
                    System.err.println("    ERROR at line " + line + ":" + charPositionInLine + " - " + msg);
                    totalErrors++;
                }
            });

            ParseTree tree = parser.document();

            if (parser.getNumberOfSyntaxErrors() == 0) {
                System.out.println("  ✓ Lexical Analysis: " + tokens.size() + " tokens");
                System.out.println("  ✓ Syntax Analysis: " + countNodes(tree) + " parse tree nodes");
                System.out.println("  ✓ CSS Validation: Passed");
            } else {
                System.out.println("  ✗ Parsing failed with " + parser.getNumberOfSyntaxErrors() + " error(s)");
            }

        } catch (IOException e) {
            System.err.println("  ✗ Failed to read file: " + e.getMessage());
            totalErrors++;
        }
    }

    private static int countNodes(ParseTree tree) {
        if (tree == null) return 0;
        int count = 1;
        for (int i = 0; i < tree.getChildCount(); i++) {
            count += countNodes(tree.getChild(i));
        }
        return count;
    }

    private static int countASTNodes(ASTNode node) {
        if (node == null) return 0;
        int count = 1;
        for (ASTNode child : node.getChildren()) {
            count += countASTNodes(child);
        }
        return count;
    }
}
