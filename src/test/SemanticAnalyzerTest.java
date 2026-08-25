package test;

import static test.Assert.*;

import ast.base.ASTNode;
import compiler.CompilationResult;
import compiler.Compiler;
import generator.ContextBuilder;
import generator.TemplateContext;
import semantic.Diagnostic;
import semantic.SemanticAnalyzer;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Tests the semantic phase — the checks that make it more than a symbol-table
 * dump.
 *
 * Each test writes a small project into a temporary directory, analyses it, and
 * asserts on the diagnostic codes produced. Asserting on the code rather than the
 * message keeps the tests stable when wording changes.
 *
 * Two properties matter equally: that a real mistake IS reported, and that
 * correct code is NOT — a checker that cries wolf is worse than none.
 */
public class SemanticAnalyzerTest {

    // ------------------------------------------------------------------
    // the real sample project
    // ------------------------------------------------------------------

    /**
     * The sample project is correct, so the analyser must stay silent on it.
     *
     * This is the false-positive guard: every check added here is run against a
     * real, working project, and anything it flags is a bug in the checker rather
     * than in the sample.
     */
    public void testSampleProjectIsClean() {
        SemanticAnalyzer analyzer = analyzeSampleProject();
        assertEquals("the sample project has no semantic problems: "
                        + analyzer.getDiagnostics(),
                0, analyzer.getDiagnostics().size());
    }

    public void testValidCodeProducesNoFalsePositives() throws Exception {
        String python =
                "from flask import Flask, render_template\n"
              + "app = Flask(__name__)\n"
              + "rows = [{\"id\": 1, \"name\": \"A\"}]\n"
              + "@app.route('/')\n"
              + "def home():\n"
              + "    total = len(rows)\n"
              + "    return render_template('page.html', rows=rows, total=total)\n";
        String template =
                "<!DOCTYPE html>\n<html><body>\n"
              + "<p>{{ total }}</p>\n"
              + "{% for row in rows %}\n"
              + "<h2>{{ row.name }}</h2>\n"
              + "<a href=\"/x/{{ row.id }}\">go</a>\n"
              + "{% endfor %}\n"
              + "</body></html>\n";

        List<Diagnostic> diagnostics = analyze(python, Map.of("page.html", template));
        assertEquals("correct code produces no diagnostics: " + diagnostics,
                0, diagnostics.size());
    }

    // ------------------------------------------------------------------
    // Python checks
    // ------------------------------------------------------------------

    public void testUndefinedNameIsReported() throws Exception {
        String python =
                "from flask import Flask, render_template\n"
              + "app = Flask(__name__)\n"
              + "@app.route('/')\n"
              + "def home():\n"
              + "    return render_template('page.html', value=mystery)\n";

        List<Diagnostic> diagnostics = analyze(python, Map.of("page.html", minimalTemplate()));
        assertTrue("the undefined name is reported",
                hasCode(diagnostics, "undefined-name"));
    }

    public void testMisspelledNameSuggestsTheRealOne() throws Exception {
        String python =
                "from flask import Flask, render_template\n"
              + "app = Flask(__name__)\n"
              + "products = [{\"id\": 1}]\n"
              + "@app.route('/')\n"
              + "def home():\n"
              + "    return render_template('page.html', value=prodcts)\n";

        List<Diagnostic> diagnostics = analyze(python, Map.of("page.html", minimalTemplate()));
        Diagnostic found = firstWithCode(diagnostics, "undefined-name");
        assertNotNull("the typo is reported", found);
        assertNotNull("a suggestion is offered", found.getHint());
        assertTrue("it suggests the real name: " + found.getHint(),
                found.getHint().contains("products"));
    }

    public void testRenderTemplateOfMissingFileIsReported() throws Exception {
        String python =
                "from flask import Flask, render_template\n"
              + "app = Flask(__name__)\n"
              + "@app.route('/')\n"
              + "def home():\n"
              + "    return render_template('nope.html')\n";

        List<Diagnostic> diagnostics = analyze(python, Map.of("page.html", minimalTemplate()));
        assertTrue("the missing template is reported",
                hasCode(diagnostics, "missing-template"));
    }

    public void testRouteWithNoReturnIsWarned() throws Exception {
        String python =
                "from flask import Flask, render_template\n"
              + "app = Flask(__name__)\n"
              + "@app.route('/')\n"
              + "def home():\n"
              + "    x = 1\n";

        List<Diagnostic> diagnostics = analyze(python, Map.of("page.html", minimalTemplate()));
        Diagnostic found = firstWithCode(diagnostics, "route-returns-nothing");
        assertNotNull("the empty route is reported", found);
        assertFalse("it is a warning, not an error", found.isError());
    }

    /**
     * Attribute access must not be mistaken for a variable use: in request.method
     * only 'request' is a name, and 'method' is an attribute of it.
     */
    public void testAttributeAccessIsNotTreatedAsAVariable() throws Exception {
        String python =
                "from flask import Flask, render_template, request\n"
              + "app = Flask(__name__)\n"
              + "@app.route('/')\n"
              + "def home():\n"
              + "    method = request.method\n"
              + "    return render_template('page.html', method=method)\n";

        List<Diagnostic> diagnostics = analyze(python, Map.of("page.html", minimalTemplate()));
        assertFalse("request.method is not an undefined name: " + diagnostics,
                hasCode(diagnostics, "undefined-name"));
    }

    /**
     * A route may call another route defined further down the file, so function
     * names are hoisted before the body walk rather than resolved in order.
     */
    public void testForwardFunctionReferenceIsAllowed() throws Exception {
        // The grammar needs a blank line between two decorated functions.
        String python =
                "from flask import Flask, render_template\n"
              + "app = Flask(__name__)\n"
              + "\n"
              + "@app.route('/')\n"
              + "def home():\n"
              + "    return later()\n"
              + "\n"
              + "@app.route('/later')\n"
              + "def later():\n"
              + "    return render_template('page.html')\n";

        List<Diagnostic> diagnostics = analyze(python, Map.of("page.html", minimalTemplate()));
        assertFalse("a later definition still resolves: " + diagnostics,
                hasCode(diagnostics, "undefined-function"));
    }

    // ------------------------------------------------------------------
    // template checks
    // ------------------------------------------------------------------

    public void testTemplateVariableNotInContextIsReported() throws Exception {
        String python =
                "from flask import Flask, render_template\n"
              + "app = Flask(__name__)\n"
              + "@app.route('/')\n"
              + "def home():\n"
              + "    return render_template('page.html')\n";
        String template = "<!DOCTYPE html>\n<html><body><p>{{ missing }}</p></body></html>\n";

        List<Diagnostic> diagnostics = analyze(python, Map.of("page.html", template));
        assertTrue("the unpassed variable is reported",
                hasCode(diagnostics, "undefined-template-variable"));
    }

    /** The typo case the phase exists for: {{ product.titel }}. */
    public void testMisspelledFieldIsReportedWithSuggestion() throws Exception {
        String python =
                "from flask import Flask, render_template\n"
              + "app = Flask(__name__)\n"
              + "rows = [{\"id\": 1, \"title\": \"A\"}]\n"
              + "@app.route('/')\n"
              + "def home():\n"
              + "    return render_template('page.html', rows=rows)\n";
        String template =
                "<!DOCTYPE html>\n<html><body>\n"
              + "{% for row in rows %}<h2>{{ row.titel }}</h2>{% endfor %}\n"
              + "</body></html>\n";

        List<Diagnostic> diagnostics = analyze(python, Map.of("page.html", template));
        Diagnostic found = firstWithCode(diagnostics, "unknown-field");
        assertNotNull("the misspelled field is reported", found);
        assertNotNull("a suggestion is offered", found.getHint());
        assertTrue("a transposition still suggests the real field: " + found.getHint(),
                found.getHint().contains("title"));
    }

    public void testCorrectFieldOnLoopIteratorIsAccepted() throws Exception {
        String python =
                "from flask import Flask, render_template\n"
              + "app = Flask(__name__)\n"
              + "rows = [{\"id\": 1, \"title\": \"A\"}]\n"
              + "@app.route('/')\n"
              + "def home():\n"
              + "    return render_template('page.html', rows=rows)\n";
        String template =
                "<!DOCTYPE html>\n<html><body>\n"
              + "{% for row in rows %}<h2>{{ row.title }}</h2>{% endfor %}\n"
              + "</body></html>\n";

        List<Diagnostic> diagnostics = analyze(python, Map.of("page.html", template));
        assertFalse("a field that exists is not reported: " + diagnostics,
                hasCode(diagnostics, "unknown-field"));
    }

    public void testLoopingOverANonListIsReported() throws Exception {
        String python =
                "from flask import Flask, render_template\n"
              + "app = Flask(__name__)\n"
              + "rows = [{\"id\": 1}]\n"
              + "@app.route('/')\n"
              + "def home():\n"
              + "    total = len(rows)\n"
              + "    return render_template('page.html', total=total)\n";
        String template =
                "<!DOCTYPE html>\n<html><body>\n"
              + "{% for x in total %}<p>{{ x }}</p>{% endfor %}\n"
              + "</body></html>\n";

        List<Diagnostic> diagnostics = analyze(python, Map.of("page.html", template));
        assertTrue("iterating a scalar is reported", hasCode(diagnostics, "not-iterable"));
    }

    /** An interpolation inside an attribute is unparsed text, so it needs its own check. */
    public void testUndefinedVariableInsideAnAttributeIsReported() throws Exception {
        String python =
                "from flask import Flask, render_template\n"
              + "app = Flask(__name__)\n"
              + "@app.route('/')\n"
              + "def home():\n"
              + "    return render_template('page.html')\n";
        String template =
                "<!DOCTYPE html>\n<html><body>\n"
              + "<a href=\"/item/{{ mystery }}\">go</a>\n"
              + "</body></html>\n";

        List<Diagnostic> diagnostics = analyze(python, Map.of("page.html", template));
        assertTrue("an attribute interpolation is checked too",
                hasCode(diagnostics, "undefined-template-variable"));
    }

    public void testMissingLayoutIsReported() throws Exception {
        String python =
                "from flask import Flask, render_template\n"
              + "app = Flask(__name__)\n"
              + "@app.route('/')\n"
              + "def home():\n"
              + "    return render_template('page.html')\n";
        String template =
                "{% extends 'no_such_layout.html' %}\n"
              + "<!DOCTYPE html>\n<html><body><p>hi</p></body></html>\n";

        List<Diagnostic> diagnostics = analyze(python, Map.of("page.html", template));
        assertTrue("the missing layout is reported", hasCode(diagnostics, "missing-layout"));
    }

    public void testTemplateNoRouteRendersIsWarned() throws Exception {
        String python =
                "from flask import Flask, render_template\n"
              + "app = Flask(__name__)\n"
              + "@app.route('/')\n"
              + "def home():\n"
              + "    return render_template('page.html')\n";

        Map<String, String> templates = new LinkedHashMap<>();
        templates.put("page.html", minimalTemplate());
        templates.put("orphan.html", minimalTemplate());

        List<Diagnostic> diagnostics = analyze(python, templates);
        Diagnostic found = firstWithCode(diagnostics, "unrendered-template");
        assertNotNull("the orphan template is reported", found);
        assertFalse("it is a warning, not an error", found.isError());
        assertTrue("it names the orphan", found.getMessage().contains("orphan.html"));
    }

    /** A layout is extended, not rendered, so it must not be called dead. */
    public void testLayoutIsNotReportedAsUnrendered() throws Exception {
        String python =
                "from flask import Flask, render_template\n"
              + "app = Flask(__name__)\n"
              + "@app.route('/')\n"
              + "def home():\n"
              + "    return render_template('page.html')\n";

        Map<String, String> templates = new LinkedHashMap<>();
        templates.put("layout.html", "<!DOCTYPE html>\n<html><body>"
                + "{% block content %}{% endblock %}</body></html>\n");
        templates.put("page.html", "{% extends 'layout.html' %}\n"
                + "{% block content %}<p>hi</p>{% endblock %}\n");

        List<Diagnostic> diagnostics = analyze(python, templates);
        assertFalse("a layout is not flagged as unrendered: " + diagnostics,
                hasCode(diagnostics, "unrendered-template"));
    }

    // ------------------------------------------------------------------
    // the distance metric
    // ------------------------------------------------------------------

    public void testTranspositionCountsAsOneEdit() {
        assertEquals("a swap of two letters is one edit", 1,
                SemanticAnalyzer.editDistance("titel", "title"));
        assertEquals("identical strings are distance 0", 0,
                SemanticAnalyzer.editDistance("name", "name"));
        assertEquals("one substitution", 1,
                SemanticAnalyzer.editDistance("nome", "name"));
    }

    // ------------------------------------------------------------------
    // helpers
    // ------------------------------------------------------------------

    private SemanticAnalyzer analyzeSampleProject() {
        Compiler compiler = new Compiler();
        CompilationResult backend = compiler.compileBackend("test_programs/run_app.py");

        // Read the directory rather than a fixed list, so adding a template to
        // the sample project cannot silently leave it unchecked here.
        Map<String, ASTNode> templates = new LinkedHashMap<>();
        Map<String, String> paths = new LinkedHashMap<>();
        Path templateDir = Path.of("test_programs/templates");
        try (Stream<Path> files = Files.list(templateDir)) {
            for (Path file : files.sorted().toList()) {
                if (!file.toString().endsWith(".html")) {
                    continue;
                }
                String name = file.getFileName().toString();
                CompilationResult result = compiler.compileFrontend(file.toString());
                assertNotNull("template parsed: " + name, result.getAst());
                templates.put(name, result.getAst());
                paths.put(name, file.toString());
            }
        } catch (Exception e) {
            throw new AssertionError("could not read the sample templates: " + e);
        }

        ContextBuilder builder = new ContextBuilder();
        builder.build(backend.getAst());

        SemanticAnalyzer analyzer = new SemanticAnalyzer();
        analyzer.analyzeBackend(backend.getFilePath(), backend.getAst(), templates.keySet());
        for (Map.Entry<String, ASTNode> entry : templates.entrySet()) {
            TemplateContext context = builder.contextFor(entry.getKey());
            analyzer.analyzeTemplate(paths.get(entry.getKey()), entry.getKey(),
                    entry.getValue(), context, templates.keySet(), context != null);
        }
        return analyzer;
    }

    /** Write a throwaway project to disk, analyse it, and return its findings. */
    private List<Diagnostic> analyze(String python, Map<String, String> templateSources)
            throws Exception {
        Path sandbox = Files.createTempDirectory("yazbek-semantic-test");
        try {
            Path backendFile = sandbox.resolve("app.py");
            Files.write(backendFile, python.getBytes(StandardCharsets.UTF_8));

            Path templateDir = sandbox.resolve("templates");
            Files.createDirectories(templateDir);

            Compiler compiler = new Compiler();
            Map<String, ASTNode> templates = new LinkedHashMap<>();
            Map<String, String> paths = new LinkedHashMap<>();
            Set<String> names = new LinkedHashSet<>(templateSources.keySet());

            for (Map.Entry<String, String> entry : templateSources.entrySet()) {
                Path file = templateDir.resolve(entry.getKey());
                Files.write(file, entry.getValue().getBytes(StandardCharsets.UTF_8));
                CompilationResult result = compiler.compileFrontend(file.toString());
                assertNotNull("test template parsed: " + entry.getKey(), result.getAst());
                templates.put(entry.getKey(), result.getAst());
                paths.put(entry.getKey(), file.toString());
            }

            CompilationResult backend = compiler.compileBackend(backendFile.toString());
            assertNotNull("test backend parsed", backend.getAst());

            ContextBuilder builder = new ContextBuilder();
            builder.build(backend.getAst());

            SemanticAnalyzer analyzer = new SemanticAnalyzer();
            analyzer.analyzeBackend(backendFile.toString(), backend.getAst(), names);
            for (Map.Entry<String, ASTNode> entry : templates.entrySet()) {
                TemplateContext context = builder.contextFor(entry.getKey());
                analyzer.analyzeTemplate(paths.get(entry.getKey()), entry.getKey(),
                        entry.getValue(), context, names, context != null);
            }
            return analyzer.getDiagnostics();
        } finally {
            deleteRecursively(sandbox);
        }
    }

    private static String minimalTemplate() {
        return "<!DOCTYPE html>\n<html><body><p>hello</p></body></html>\n";
    }

    private static boolean hasCode(List<Diagnostic> diagnostics, String code) {
        return firstWithCode(diagnostics, code) != null;
    }

    private static Diagnostic firstWithCode(List<Diagnostic> diagnostics, String code) {
        for (Diagnostic d : diagnostics) {
            if (code.equals(d.getCode())) {
                return d;
            }
        }
        return null;
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
