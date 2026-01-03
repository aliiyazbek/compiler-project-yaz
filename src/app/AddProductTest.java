package app;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import antlr.backend.*;
import antlr.frontend.*;
import ast.base.ASTNode;
import visitor.BackendASTVisitor;
import visitor.FrontendASTVisitor;
import symboltable.SymbolTable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Test Program 2: Add Product
 * Tests the compilation pipeline for the Add Product feature
 * Demonstrates: Form handling, POST method, if statements, request.form
 */
public class AddProductTest {

    private static int totalErrors = 0;
    private static SymbolTable symbolTable = null;

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║     TEST PROGRAM 2: ADD PRODUCT - COMPILATION TEST               ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();

        // Test 1: Backend - Flask Application with Add Product route
        System.out.println("┌──────────────────────────────────────────────────────────────────┐");
        System.out.println("│ PHASE 1: BACKEND COMPILATION (Python/Flask with Form Handling)   │");
        System.out.println("└──────────────────────────────────────────────────────────────────┘");
        ASTNode backendAST = testBackend("test_programs/run_app.py");
        System.out.println();

        // Test 2: Frontend - Add Product Template
        System.out.println("┌──────────────────────────────────────────────────────────────────┐");
        System.out.println("│ PHASE 2: ADD PRODUCT TEMPLATE (HTML + Jinja2 + Form)             │");
        System.out.println("└──────────────────────────────────────────────────────────────────┘");
        ASTNode addProductAST = testFrontend("test_programs/templates/add_product.html");
        System.out.println();

        // Print Symbol Table
        System.out.println("┌──────────────────────────────────────────────────────────────────┐");
        System.out.println("│ SYMBOL TABLE (populated via Visitor)                              │");
        System.out.println("└──────────────────────────────────────────────────────────────────┘");
        if (symbolTable != null) {
            symbolTable.print();
        }
        System.out.println();

        // Print AST Trees
        if (backendAST != null) {
            System.out.println("┌──────────────────────────────────────────────────────────────────┐");
            System.out.println("│ BACKEND AST (app.py with add_product route)                      │");
            System.out.println("└──────────────────────────────────────────────────────────────────┘");
            backendAST.printTree();
            System.out.println();
        }

        if (addProductAST != null) {
            System.out.println("┌──────────────────────────────────────────────────────────────────┐");
            System.out.println("│ FRONTEND AST (add_product.html)                                  │");
            System.out.println("└──────────────────────────────────────────────────────────────────┘");
            addProductAST.printTree();
            System.out.println();
        }

        // Final Summary
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        if (totalErrors == 0) {
            System.out.println("║  ✓ ALL TESTS PASSED - Add Product compiled successfully!        ║");
        } else {
            System.out.println("║  ✗ TESTS FAILED - " + totalErrors + " error(s) found                              ║");
        }
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
    }

    private static ASTNode testBackend(String filePath) {
        System.out.println("  File: " + filePath);
        try {
            String input = new String(Files.readAllBytes(Paths.get(filePath)));

            // Lexer
            CharStream charStream = CharStreams.fromString(input);
            PythonFlaskLexer lexer = new PythonFlaskLexer(charStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            // Parser
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

                // Build AST and Symbol Table (via Visitor)
                BackendASTVisitor visitor = new BackendASTVisitor();
                ASTNode ast = visitor.visit(tree);
                System.out.println("  ✓ AST Construction: " + countASTNodes(ast) + " AST nodes");

                // Get Symbol Table from visitor
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

            // Lexer
            CharStream charStream = CharStreams.fromString(input);
            FrontendLexer lexer = new FrontendLexer(charStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            // Parser
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

                // Build AST
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
