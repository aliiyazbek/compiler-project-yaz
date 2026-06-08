package app;

import compiler.CompilationResult;
import compiler.Compiler;

/**
 * Single entry point for the compiler. Drives the full pipeline over the sample
 * Flask app plus its templates and stylesheet, prints each phase's metrics, the
 * symbol table (with inferred types and a scope tree), and the resulting ASTs.
 *
 * Replaces the four near-identical *Test driver classes, which each re-declared
 * the same lex/parse/visit logic and a private static totalErrors counter.
 *
 * Usage:
 *   java app.Main                      # compile the default sample project
 *   java app.Main path/to/file.py ...  # compile the given files (by extension)
 */
public class Main {

    public static void main(String[] args) {
        Compiler compiler = new Compiler();

        printBanner("YAZBEK COMPILER - FULL PIPELINE");

        if (args.length > 0) {
            int errors = 0;
            for (String file : args) {
                errors += compileByExtension(compiler, file);
            }
            printSummary(errors);
            return;
        }

        int errors = 0;

        section("PHASE 1: BACKEND COMPILATION (Python / Flask)");
        CompilationResult backend = compiler.compileBackend("test_programs/run_app.py");
        errors += report(backend);

        section("PHASE 2: TEMPLATE COMPILATION (HTML + Jinja2)");
        String[] templates = {
                "test_programs/templates/base.html",
                "test_programs/templates/products.html",
                "test_programs/templates/add_product.html",
                "test_programs/templates/product_detail.html",
                "test_programs/templates/delete_confirm.html",
        };
        for (String tpl : templates) {
            errors += report(compiler.compileFrontend(tpl));
        }

        section("PHASE 3: STYLESHEET COMPILATION (CSS)");
        errors += report(compiler.compileCss("test_programs/static/style.css"));

        if (backend.getSymbolTable() != null) {
            section("SYMBOL TABLE (with inferred data types)");
            backend.getSymbolTable().print();
            backend.getSymbolTable().printScopeTree();
        }

        if (backend.getAst() != null) {
            section("BACKEND AST (run_app.py)");
            backend.getAst().printTree();
        }

        printSummary(errors);
    }

    private static int compileByExtension(Compiler compiler, String file) {
        CompilationResult result;
        if (file.endsWith(".py")) {
            result = compiler.compileBackend(file);
        } else if (file.endsWith(".css")) {
            result = compiler.compileCss(file);
        } else {
            result = compiler.compileFrontend(file);
        }
        int errors = report(result);
        if (result.getSymbolTable() != null) {
            result.getSymbolTable().print();
        }
        if (result.getAst() != null) {
            result.getAst().printTree();
        }
        return errors;
    }

    /** Print one file's compilation metrics; return its syntax-error count. */
    private static int report(CompilationResult r) {
        System.out.println("  File: " + r.getFilePath());
        if (r.isSuccess()) {
            System.out.println("    Lexical Analysis : " + r.getTokenCount() + " tokens");
            System.out.println("    Syntax Analysis  : " + r.getParseTreeNodeCount() + " parse tree nodes");
            if (r.getAstNodeCount() > 0) {
                System.out.println("    AST Construction : " + r.getAstNodeCount() + " AST nodes");
            }
            if (r.getSymbolTable() != null) {
                System.out.println("    Symbol Table     : "
                        + r.getSymbolTable().getAllSymbols().size() + " symbols");
            }
        } else {
            System.out.println("    FAILED with " + r.getSyntaxErrors() + " syntax error(s)");
        }
        System.out.println();
        return r.getSyntaxErrors();
    }

    private static void printBanner(String title) {
        String bar = "=".repeat(70);
        System.out.println(bar);
        System.out.println("  " + title);
        System.out.println(bar);
        System.out.println();
    }

    private static void section(String title) {
        System.out.println("---- " + title + " " + "-".repeat(Math.max(0, 60 - title.length())));
    }

    private static void printSummary(int errors) {
        System.out.println("=".repeat(70));
        if (errors == 0) {
            System.out.println("  ALL PHASES PASSED - compilation succeeded with 0 errors.");
        } else {
            System.out.println("  COMPILATION FAILED - " + errors + " error(s) found.");
        }
        System.out.println("=".repeat(70));
    }
}
