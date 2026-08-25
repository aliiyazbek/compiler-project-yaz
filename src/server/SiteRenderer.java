package server;

import ast.base.ASTNode;
import compiler.CompilationResult;
import compiler.Compiler;
import generator.ContextBuilder;
import generator.HtmlEmitter;
import generator.TemplateContext;
import output.OutputWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Regenerates the whole site from the <b>live</b> {@link DataStore}.
 *
 * <p>This is the piece that answers "who is responsible for regeneration". It
 * holds the compiled template ASTs and the compile-time context, and on every
 * call re-runs {@link HtmlEmitter} with the store's current rows substituted in
 * — so a product added a second ago appears in the freshly written HTML.
 *
 * <p>Two things can trigger it:
 * <ul>
 *   <li>a data change (add/delete) — {@link #render()}, driven by {@link DataStore};</li>
 *   <li>a source-file edit — {@link #reloadSources()}, driven by {@link SourceWatcher},
 *       which re-parses the Python and the templates first.</li>
 * </ul>
 *
 * <p>Renders are serialized on this object's monitor: two concurrent writes to the
 * same HTML file would otherwise interleave.
 */
public class SiteRenderer {

    private final String projectRoot;
    private final String backendSource;
    private final String templateDir;
    private final String staticDir;
    private final DataStore store;

    private final Compiler compiler = new Compiler();

    /** Template file name -> its parsed Jinja AST. */
    private Map<String, ASTNode> templates = new LinkedHashMap<>();

    /** Template file name -> the context the Python said to render it with. */
    private Map<String, TemplateContext> baseContexts = new LinkedHashMap<>();

    /** Template file name -> the route that serves it, for naming the output. */
    private Map<String, String> routes = new LinkedHashMap<>();

    /** Kept from the last reload so folded len() values can be recomputed. */
    private ContextBuilder contextBuilder;

    private int renderCount;
    private String lastSummary = "not yet rendered";

    public SiteRenderer(String projectRoot, String backendSource, String templateDir,
                        String staticDir, DataStore store) {
        this.projectRoot = projectRoot;
        this.backendSource = backendSource;
        this.templateDir = templateDir;
        this.staticDir = staticDir;
        this.store = store;
    }

    public int getRenderCount() {
        return renderCount;
    }

    public String getLastSummary() {
        return lastSummary;
    }

    // ------------------------------------------------------------------
    // compiling the sources
    // ------------------------------------------------------------------

    /**
     * Re-run the front half of the compiler: parse the Python and every template,
     * and rebuild the compile-time contexts. Called at startup and whenever the
     * watcher sees a source file change.
     *
     * @param seedStore when true, reload the store's rows from the Python source;
     *                  used at startup and after run_app.py itself is edited
     * @return the number of syntax errors found (0 means the reload succeeded)
     */
    public synchronized int reloadSources(boolean seedStore) {
        CompilationResult backend = compiler.compileBackend(backendSource);
        if (backend.getAst() == null) {
            lastSummary = "backend failed to parse; keeping the previous build";
            return Math.max(1, backend.getSyntaxErrors());
        }

        int errors = backend.getSyntaxErrors();

        ContextBuilder builder = new ContextBuilder();
        builder.build(backend.getAst());

        Map<String, ASTNode> freshTemplates = new LinkedHashMap<>();
        for (Path file : templateFiles()) {
            CompilationResult result = compiler.compileFrontend(file.toString());
            if (result.getAst() != null) {
                freshTemplates.put(fileName(file.toString()), result.getAst());
            } else {
                errors += Math.max(1, result.getSyntaxErrors());
            }
        }

        if (freshTemplates.isEmpty()) {
            lastSummary = "no templates parsed; keeping the previous build";
            return Math.max(1, errors);
        }

        this.templates = freshTemplates;
        this.baseContexts = new LinkedHashMap<>(builder.getContexts());
        this.routes = new LinkedHashMap<>(builder.getTemplateRoutes());
        this.contextBuilder = builder;

        if (seedStore) {
            store.seedFrom(builder.getGlobals().get(store.getCollectionName()));
        }
        return errors;
    }

    private List<Path> templateFiles() {
        List<Path> files = new ArrayList<>();
        Path dir = Paths.get(templateDir);
        if (!Files.isDirectory(dir)) {
            return files;
        }
        try (java.util.stream.Stream<Path> entries = Files.list(dir)) {
            entries.filter(p -> p.toString().endsWith(".html") || p.toString().endsWith(".jinja"))
                   .sorted()
                   .forEach(files::add);
        } catch (IOException e) {
            System.err.println("  Could not list templates: " + e.getMessage());
        }
        return files;
    }

    // ------------------------------------------------------------------
    // rendering
    // ------------------------------------------------------------------

    /**
     * Write every page, with the live store's rows substituted for the
     * compile-time data. Support files are copied on the first render only —
     * they never change between renders.
     */
    public synchronized List<String> render() throws IOException {
        OutputWriter writer = new OutputWriter(projectRoot);
        boolean firstRender = renderCount == 0;
        if (firstRender) {
            writer.prepare();
        }

        List<String> pages = new ArrayList<>();
        int resolved = 0;
        int unresolved = 0;

        for (Map.Entry<String, ASTNode> entry : templates.entrySet()) {
            String templateName = entry.getKey();
            TemplateContext context = baseContexts.get(templateName);
            if (context == null) {
                continue; // a layout no route renders
            }

            TemplateContext live = withLiveData(context);
            HtmlEmitter emitter = new HtmlEmitter();
            String html = emitter.emit(templateName, entry.getValue(), live, templates);

            String outputName = pageName(templateName);
            writer.writeHtml(outputName, html);
            pages.add(outputName);
            resolved += emitter.getExpressionsResolved();
            unresolved += emitter.getExpressionsUnresolved();
        }

        if (firstRender) {
            copySupportFiles(writer);
        }

        renderCount++;
        lastSummary = pages.size() + " page(s), " + store.size() + " "
                + store.getCollectionName() + " row(s), "
                + resolved + " expressions resolved"
                + (unresolved > 0 ? ", " + unresolved + " unresolved" : "");
        return pages;
    }

    /**
     * Render one template for a specific row, without writing to disk.
     *
     * A page behind a URL parameter ({@code /product/<int:id>}) cannot exist as a
     * single static file — the compiler bakes in one representative id. The server
     * calls this so {@code /product/2} shows row 2 rather than the baked page.
     *
     * @return the HTML, or null if that template is not loaded
     */
    public synchronized String renderForRow(String templateName, Map<String, String> row) {
        ASTNode tree = templates.get(templateName);
        TemplateContext compiled = baseContexts.get(templateName);
        if (tree == null || compiled == null) {
            return null;
        }

        TemplateContext context = withLiveData(compiled);

        // Point every single-row variable at the requested row.
        Map<String, TemplateContext.Value> fields = new LinkedHashMap<>();
        for (Map.Entry<String, String> field : row.entrySet()) {
            fields.put(field.getKey(), TemplateContext.Value.of(field.getValue()));
        }
        TemplateContext.Value rowValue = TemplateContext.Value.ofDict(fields);

        for (Map.Entry<String, TemplateContext.Value> entry : compiled.getVariables().entrySet()) {
            if (entry.getValue().isDict()) {
                context.put(entry.getKey(), rowValue);
            }
        }

        return new HtmlEmitter().emit(templateName, tree, context, templates);
    }

    private void copySupportFiles(OutputWriter writer) throws IOException {
        writer.copySupportFile(Paths.get(backendSource), "app.py");
        writer.copyDirectory(Paths.get(staticDir), "static");
    }

    /**
     * Replace the compile-time collection with the store's current rows.
     *
     * Anything derived from it is recomputed too: {@code total} came from
     * {@code len(products)}, so leaving the compiled 3 in place would show a stale
     * count the moment a product is added. A single-row variable (the
     * {@code product} on a detail page) is refreshed by id so it survives edits.
     */
    private TemplateContext withLiveData(TemplateContext compiled) {
        TemplateContext live = compiled.copy();
        TemplateContext.Value rows = store.asValue();

        for (Map.Entry<String, TemplateContext.Value> entry : compiled.getVariables().entrySet()) {
            String name = entry.getKey();
            TemplateContext.Value compiledValue = entry.getValue();

            if (compiledValue.isList()) {
                live.put(name, rows);
            } else if (compiledValue.isDict()) {
                live.put(name, refreshRow(compiledValue, rows));
            } else if (compiledValue.isScalar()
                    && contextBuilder != null && contextBuilder.isRowCount(compiledValue)) {
                live.put(name, TemplateContext.Value.of(String.valueOf(store.size())));
            }
        }
        return live;
    }

    /**
     * Re-look-up a single row by id against the live data, so a detail page shows
     * current values. If that row is gone, the compiled one is kept — the page
     * still renders rather than collapsing to blanks.
     */
    private TemplateContext.Value refreshRow(TemplateContext.Value compiledRow,
                                             TemplateContext.Value liveRows) {
        TemplateContext.Value id = compiledRow.field("id");
        if (id == null || !id.isScalar()) {
            return compiledRow;
        }
        for (TemplateContext.Value candidate : liveRows.getList()) {
            TemplateContext.Value candidateId = candidate.field("id");
            if (candidateId != null && id.getScalar().equals(candidateId.getScalar())) {
                return candidate;
            }
        }
        return compiledRow;
    }

    /** '/' is served as index.html; everything else keeps its template name. */
    private String pageName(String templateName) {
        if ("/".equals(routes.get(templateName))) {
            return "index.html";
        }
        return templateName.endsWith(".html") ? templateName : templateName + ".html";
    }

    private static String fileName(String path) {
        String normalised = path.replace('\\', '/');
        int slash = normalised.lastIndexOf('/');
        return slash >= 0 ? normalised.substring(slash + 1) : normalised;
    }
}
