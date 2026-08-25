package test;

import static test.Assert.*;

import compiler.CompilationResult;
import compiler.Compiler;
import output.AstJsonSerializer;
import output.BuildDriver;
import output.JsonWriter;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Tests the file-writing stage: running the driver must produce the two required
 * directories with the required files in them, and the JSON artefacts must be
 * well formed.
 *
 * Everything is written under a temporary directory so the test never disturbs
 * the project's real output/ tree.
 */
public class OutputWriterTest {

    private final Compiler compiler = new Compiler();

    public void testJsonEscapingProducesValidLiterals() {
        assertEquals("quotes are escaped", "say \\\"hi\\\"",
                JsonWriter.escape("say \"hi\""));
        assertEquals("backslashes are escaped", "a\\\\b",
                JsonWriter.escape("a\\b"));
        assertEquals("newlines are escaped", "line\\nbreak",
                JsonWriter.escape("line\nbreak"));
    }

    public void testPythonAstSerializesWithNodeDetail() {
        CompilationResult backend = compiler.compileBackend("test_programs/run_app.py");
        String json = AstJsonSerializer.serialize(backend.getFilePath(), backend.getAst());

        assertTrue("is a JSON object", json.trim().startsWith("{"));
        assertTrue("records the source file", json.contains("run_app.py"));
        assertTrue("carries the products assignment", json.contains("\"variable\": \"products\""));
        assertTrue("carries route decorators", json.contains("\"decorator\""));
        assertTrue("braces balance", balanced(json, '{', '}'));
        assertTrue("brackets balance", balanced(json, '[', ']'));
    }

    public void testJinjaAstSerializesEveryTemplate() {
        String[] names = {
                "test_programs/templates/products.html",
                "test_programs/templates/base.html",
        };
        ast.base.ASTNode[] roots = new ast.base.ASTNode[names.length];
        for (int i = 0; i < names.length; i++) {
            roots[i] = compiler.compileFrontend(names[i]).getAst();
        }

        String json = AstJsonSerializer.serializeAll(names, roots);
        assertTrue("counts both templates", json.contains("\"templateCount\": 2"));
        assertTrue("records the for-loop", json.contains("\"iterator\": \"product\""));
        assertTrue("records the block", json.contains("\"block\": \"content\""));
        assertTrue("braces balance", balanced(json, '{', '}'));
    }

    public void testDriverWritesBothOutputTrees() throws Exception {
        Path sandbox = Files.createTempDirectory("yazbek-output-test");
        try {
            CompilationResult backend = compiler.compileBackend("test_programs/run_app.py");
            List<CompilationResult> templates = new ArrayList<>();
            for (String name : new String[]{"base.html", "products.html", "add_product.html",
                                            "product_detail.html", "delete_confirm.html"}) {
                templates.add(compiler.compileFrontend("test_programs/templates/" + name));
            }

            BuildDriver driver = new BuildDriver(sandbox.toString());
            driver.run(backend, templates,
                    "test_programs/run_app.py", "test_programs/static");

            // output/ — the runnable app
            assertTrue("index.html generated", exists(sandbox, "output/index.html"));
            assertTrue("add_product.html generated", exists(sandbox, "output/add_product.html"));
            assertTrue("product_detail.html generated", exists(sandbox, "output/product_detail.html"));
            assertTrue("delete_confirm.html generated", exists(sandbox, "output/delete_confirm.html"));
            assertTrue("app.py copied", exists(sandbox, "output/app.py"));
            assertTrue("stylesheet copied", exists(sandbox, "output/static/style.css"));

            // A layout that no route renders must not become a page.
            assertFalse("base.html is not emitted as a page",
                    exists(sandbox, "output/base.html"));

            // compiler_output/ — the analysis artefacts
            assertTrue("ast_python.json written", exists(sandbox, "compiler_output/ast_python.json"));
            assertTrue("ast_jinja.json written", exists(sandbox, "compiler_output/ast_jinja.json"));
            assertTrue("semantic_report.txt written", exists(sandbox, "compiler_output/semantic_report.txt"));
            assertTrue("generation_log.txt written", exists(sandbox, "compiler_output/generation_log.txt"));

            assertEquals("four pages generated", 4, driver.getPagesGenerated());

            // The copied backend must be byte-identical: it is not translated.
            byte[] original = Files.readAllBytes(Paths.get("test_programs/run_app.py"));
            byte[] copied = Files.readAllBytes(sandbox.resolve("output/app.py"));
            assertEquals("app.py is copied unchanged",
                    new String(original, StandardCharsets.UTF_8),
                    new String(copied, StandardCharsets.UTF_8));

            String report = read(sandbox, "compiler_output/semantic_report.txt");
            assertTrue("the report lists the symbol table", report.contains("Symbol table"));
            assertTrue("the report shows the scope tree", report.contains("Scope tree"));
            assertTrue("the report records inferred types", report.contains("products"));
        } finally {
            deleteRecursively(sandbox);
        }
    }

    public void testRerunClearsStaleOutput() throws Exception {
        Path sandbox = Files.createTempDirectory("yazbek-stale-test");
        try {
            Path outputDir = sandbox.resolve("output");
            Files.createDirectories(outputDir);
            Path stale = outputDir.resolve("stale_page.html");
            Files.write(stale, "old".getBytes(StandardCharsets.UTF_8));

            CompilationResult backend = compiler.compileBackend("test_programs/run_app.py");
            List<CompilationResult> templates = new ArrayList<>();
            templates.add(compiler.compileFrontend("test_programs/templates/products.html"));

            new BuildDriver(sandbox.toString())
                    .run(backend, templates, "test_programs/run_app.py", "test_programs/static");

            assertFalse("a file from a previous run is removed", Files.exists(stale));
            assertTrue("the fresh page is present", exists(sandbox, "output/index.html"));
        } finally {
            deleteRecursively(sandbox);
        }
    }

    // ------------------------------------------------------------------
    // helpers
    // ------------------------------------------------------------------

    private static boolean exists(Path root, String relative) {
        return Files.exists(root.resolve(relative));
    }

    private static String read(Path root, String relative) throws Exception {
        return new String(Files.readAllBytes(root.resolve(relative)), StandardCharsets.UTF_8);
    }

    /** Cheap structural check that a generated document's delimiters balance. */
    private static boolean balanced(String text, char open, char close) {
        int depth = 0;
        boolean inString = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '"' && (i == 0 || text.charAt(i - 1) != '\\')) {
                inString = !inString;
            } else if (!inString) {
                if (c == open) {
                    depth++;
                } else if (c == close) {
                    depth--;
                    if (depth < 0) {
                        return false;
                    }
                }
            }
        }
        return depth == 0;
    }

    private static void deleteRecursively(Path dir) throws Exception {
        if (!Files.exists(dir)) {
            return;
        }
        try (Stream<Path> paths = Files.walk(dir)) {
            paths.sorted(Comparator.reverseOrder()).forEach(p -> {
                try {
                    Files.deleteIfExists(p);
                } catch (Exception ignored) {
                    // A leftover temp file is not worth failing the test over.
                }
            });
        }
    }
}
