package app;

import compiler.CompilationResult;
import compiler.Compiler;
import generator.Generator;
import output.BuildDriver;
import semantic.Diagnostic;
import semantic.SemanticAnalyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private static final String BACKEND_SOURCE = "test_programs/run_app.py";
    private static final String STATIC_DIR = "test_programs/static";
    private static final String TEMPLATE_DIR = "test_programs/templates/";

    private static final String[] TEMPLATES = {
            TEMPLATE_DIR + "base.html",
            TEMPLATE_DIR + "products.html",
            TEMPLATE_DIR + "add_product.html",
            TEMPLATE_DIR + "edit_product.html",
            TEMPLATE_DIR + "product_detail.html",
            TEMPLATE_DIR + "delete_confirm.html",
    };

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
        CompilationResult backend = compiler.compileBackend(BACKEND_SOURCE);
        errors += report(backend);

        section("PHASE 2: TEMPLATE COMPILATION (HTML + Jinja2)");
        List<CompilationResult> templateResults = new ArrayList<>();
        for (String tpl : TEMPLATES) {
            CompilationResult tplResult = compiler.compileFrontend(tpl);
            templateResults.add(tplResult);
            errors += report(tplResult);
        }

        section("PHASE 3: STYLESHEET COMPILATION (CSS)");
        errors += report(compiler.compileCss(STATIC_DIR + "/style.css"));

        section("PHASE 4: GENERATOR (Python data array -> Jinja tree)");
        runGenerator(backend, templateResults);

        section("PHASE 5: CODE GENERATION (HTML output + compiler artefacts)");
        errors += runCodeGeneration(backend, templateResults);

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

    /**
     * Run the generator: carry the Python data array into each template's Jinja
     * tree, then print the bound trees so the injected data is visible.
     */
    private static void runGenerator(CompilationResult backend, List<CompilationResult> templates) {
        if (backend == null || backend.getAst() == null) {
            System.out.println("  Skipped: no backend AST to read data from.\n");
            return;
        }

        Generator generator = new Generator();
        generator.collectFromBackend(backend.getAst());

        System.out.println("  Python data arrays discovered: "
                + generator.getDataArrays().keySet());
        for (Generator.DataRecord rec : generator.getDataArrays().values()) {
            System.out.println("    - " + rec.variableName + " (" + rec.rows.size() + " rows)");
        }
        System.out.println();

        int totalBound = 0;
        for (CompilationResult tpl : templates) {
            if (tpl.getAst() == null) {
                continue;
            }
            int bound = generator.generateInto(tpl.getFilePath(), tpl.getAst());
            if (bound > 0) {
                totalBound += bound;
                System.out.println("  " + tpl.getFilePath() + ": injected " + bound
                        + " bound-data node(s).");
                section("BOUND JINJA TREE (" + tpl.getFilePath() + ")");
                tpl.getAst().printTree();
                System.out.println();
            }
        }

        if (totalBound == 0) {
            System.out.println("  No template variables resolved against the Python data.");
        } else {
            System.out.println("  Generator bound " + totalBound + " value(s) total.");
        }
        System.out.println();
    }

    /**
     * Run the real code-generation stage: render each template to HTML, copy the
     * support files, and write the compiler's own artefacts.
     *
     * Unlike {@link #runGenerator}, which annotates the template tree so the data
     * binding is visible in a dump, this stage evaluates the tree and writes files
     * to disk — output/ for the runnable app, compiler_output/ for the analysis.
     *
     * @return 1 if writing failed, 0 otherwise
     */
    private static int runCodeGeneration(CompilationResult backend,
                                         List<CompilationResult> templates) {
        BuildDriver driver = new BuildDriver(".");
        try {
            driver.run(backend, templates, BACKEND_SOURCE, STATIC_DIR);
        } catch (IOException e) {
            System.err.println("  Code generation failed: " + e.getMessage());
            return 1;
        }

        System.out.println("  Pages generated : " + driver.getPagesGenerated());
        System.out.println("  Files copied    : " + driver.getFilesCopied());
        System.out.println();
        System.out.println("  output/            -> generated pages + support files");
        System.out.println("  compiler_output/   -> ast_python.json, ast_jinja.json,");
        System.out.println("                        semantic_report.txt, generation_log.txt");
        System.out.println();

        for (String line : driver.getGenerationLog()) {
            System.out.println("  " + line);
        }
        System.out.println();

        reportSemantics(driver.getAnalyzer());
        return 0;
    }

    /**
     * Print the semantic phase's findings as their own section.
     *
     * These are not syntax errors — every file parsed. They are the checks that
     * make the phase more than a symbol-table dump: a template reading a variable
     * nobody passed it, a name used before it exists, a missing layout.
     */
    private static void reportSemantics(SemanticAnalyzer analyzer) {
        section("PHASE 6: SEMANTIC ANALYSIS (error checking)");

        if (analyzer == null) {
            System.out.println("  Skipped: nothing to analyse.\n");
            return;
        }

        List<Diagnostic> diagnostics = analyzer.getDiagnostics();
        if (diagnostics.isEmpty()) {
            System.out.println("  No semantic problems found.\n");
            return;
        }

        for (Diagnostic d : diagnostics) {
            System.out.println("  " + d);
        }
        System.out.println();
        System.out.println("  " + analyzer.errorCount() + " error(s), "
                + analyzer.warningCount() + " warning(s).");
        System.out.println("  Full report: compiler_output/semantic_report.txt");
        System.out.println();
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
