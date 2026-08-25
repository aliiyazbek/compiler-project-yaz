package output;

import ast.base.ASTNode;
import compiler.CompilationResult;
import generator.ContextBuilder;
import generator.HtmlEmitter;
import generator.TemplateContext;
import semantic.Diagnostic;
import semantic.SemanticAnalyzer;
import symboltable.Symbol;
import symboltable.SymbolTable;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Drives the code-generation stage end to end and writes both output trees.
 *
 * <p>Given the already-compiled backend and template results, it:
 * <ol>
 *   <li>builds the per-template {@link TemplateContext} from the Python AST,</li>
 *   <li>renders each template that a route actually renders, via {@link HtmlEmitter},</li>
 *   <li>names each page after its route ({@code '/'} becomes {@code index.html}),</li>
 *   <li>copies the untouched support files, and</li>
 *   <li>writes the four {@code compiler_output/} reports.</li>
 * </ol>
 *
 * Layout templates (those only ever extended, never rendered by a route) are used
 * during rendering but produce no page of their own.
 */
public class BuildDriver {

    private final String projectRoot;
    private final List<String> generationLog = new ArrayList<>();

    private int pagesGenerated;
    private int filesCopied;

    /** The semantic phase's findings, kept for the report and the exit status. */
    private SemanticAnalyzer analyzer;

    public SemanticAnalyzer getAnalyzer() {
        return analyzer;
    }

    public BuildDriver(String projectRoot) {
        this.projectRoot = projectRoot;
    }

    public int getPagesGenerated() {
        return pagesGenerated;
    }

    public int getFilesCopied() {
        return filesCopied;
    }

    public List<String> getGenerationLog() {
        return generationLog;
    }

    /**
     * @param backend       the compiled app.py / run_app.py
     * @param templates     the compiled templates, in the order they were compiled
     * @param backendSource path of the Python file, so it can be copied to output/
     * @param staticDir     path of the static/ directory, copied verbatim
     */
    public void run(CompilationResult backend,
                    List<CompilationResult> templates,
                    String backendSource,
                    String staticDir) throws IOException {

        OutputWriter writer = new OutputWriter(projectRoot);
        writer.prepare();

        // ---- 1. context data from the Python side -------------------------
        ContextBuilder contexts = new ContextBuilder();
        contexts.build(backend == null ? null : backend.getAst());
        generationLog.add("== Context data ==");
        generationLog.addAll(contexts.getLog());
        generationLog.add("");

        // ---- 2. index the parsed templates by file name -------------------
        Map<String, ASTNode> templatesByName = new LinkedHashMap<>();
        Map<String, String> pathsByName = new LinkedHashMap<>();
        for (CompilationResult tpl : templates) {
            if (tpl.getAst() == null) {
                generationLog.add("! " + tpl.getFilePath()
                        + " failed to parse; it cannot be rendered.");
                continue;
            }
            String name = baseName(tpl.getFilePath());
            templatesByName.put(name, tpl.getAst());
            pathsByName.put(name, tpl.getFilePath());
        }

        // ---- 3. semantic analysis -----------------------------------------
        // Run before generation so problems are reported against the source the
        // author wrote, rather than being silently swallowed into empty output.
        analyzer = new SemanticAnalyzer();
        if (backend != null) {
            analyzer.analyzeBackend(backend.getFilePath(), backend.getAst(),
                    templatesByName.keySet());
        }
        for (Map.Entry<String, ASTNode> entry : templatesByName.entrySet()) {
            String templateName = entry.getKey();
            TemplateContext context = contexts.contextFor(templateName);
            analyzer.analyzeTemplate(pathsByName.get(templateName), templateName,
                    entry.getValue(), context, templatesByName.keySet(), context != null);
        }

        generationLog.add("== Semantic analysis ==");
        if (analyzer.getDiagnostics().isEmpty()) {
            generationLog.add("  No problems found.");
        } else {
            for (Diagnostic d : analyzer.getDiagnostics()) {
                generationLog.add("  " + d);
            }
            generationLog.add("  " + analyzer.errorCount() + " error(s), "
                    + analyzer.warningCount() + " warning(s).");
        }
        generationLog.add("");

        // ---- 4. render every template a route renders ---------------------
        generationLog.add("== Generation ==");
        for (Map.Entry<String, ASTNode> entry : templatesByName.entrySet()) {
            String templateName = entry.getKey();
            TemplateContext context = contexts.contextFor(templateName);

            if (context == null) {
                generationLog.add("- " + templateName
                        + ": not rendered by any route (layout/partial); skipped.");
                continue;
            }

            HtmlEmitter emitter = new HtmlEmitter();
            String html = emitter.emit(templateName, entry.getValue(), context, templatesByName);

            String outputName = outputFileName(templateName,
                    contexts.getTemplateRoutes().get(templateName));
            writer.writeHtml(outputName, html);
            pagesGenerated++;

            generationLog.add("+ " + templateName + " -> output/" + outputName
                    + "  (" + emitter.getExpressionsResolved() + " expressions resolved, "
                    + emitter.getExpressionsUnresolved() + " unresolved, "
                    + emitter.getLoopIterations() + " loop iterations, "
                    + emitter.getConditionsTaken() + " conditions taken, "
                    + emitter.getConditionsSkipped() + " skipped)");
            for (String line : emitter.getLog()) {
                generationLog.add("    " + line);
            }
        }
        generationLog.add("");

        // ---- 4. copy the untouched support files --------------------------
        generationLog.add("== Support files (copied unchanged) ==");
        if (backendSource != null) {
            // The requirement names the backend app.py in the output tree.
            if (writer.copySupportFile(Paths.get(backendSource), "app.py")) {
                filesCopied++;
                generationLog.add("  " + backendSource + " -> output/app.py");
            }
        }
        if (staticDir != null) {
            int copied = writer.copyDirectory(Paths.get(staticDir), "static");
            filesCopied += copied;
            generationLog.add("  " + staticDir + "/** -> output/static/** ("
                    + copied + " file(s))");
        }
        generationLog.add("");

        // ---- 5. compiler_output/ artefacts --------------------------------
        writeArtifacts(writer, backend, templates, pathsByName);

        generationLog.add("== Summary ==");
        generationLog.add("Pages generated : " + pagesGenerated);
        generationLog.add("Files copied    : " + filesCopied);

        // generation_log.txt is written last so it records everything above.
        writer.writeCompilerArtifact("generation_log.txt",
                String.join(System.lineSeparator(), generationLog) + System.lineSeparator());
    }

    private void writeArtifacts(OutputWriter writer,
                                CompilationResult backend,
                                List<CompilationResult> templates,
                                Map<String, String> pathsByName) throws IOException {

        // ast_python.json
        String pythonJson = AstJsonSerializer.serialize(
                backend == null ? null : backend.getFilePath(),
                backend == null ? null : backend.getAst());
        writer.writeCompilerArtifact("ast_python.json", pythonJson);

        // ast_jinja.json — every template in one document
        List<String> names = new ArrayList<>();
        List<ASTNode> roots = new ArrayList<>();
        for (CompilationResult tpl : templates) {
            if (tpl.getAst() != null) {
                names.add(tpl.getFilePath());
                roots.add(tpl.getAst());
            }
        }
        String jinjaJson = AstJsonSerializer.serializeAll(
                names.toArray(new String[0]), roots.toArray(new ASTNode[0]));
        writer.writeCompilerArtifact("ast_jinja.json", jinjaJson);

        // semantic_report.txt
        writer.writeCompilerArtifact("semantic_report.txt",
                buildSemanticReport(backend, templates));
    }

    /**
     * Render the semantic-analysis results as text: per-file metrics, the symbol
     * table with inferred data types, and the scope tree.
     */
    private String buildSemanticReport(CompilationResult backend,
                                       List<CompilationResult> templates) {
        StringBuilder sb = new StringBuilder();
        String nl = System.lineSeparator();

        sb.append("SEMANTIC ANALYSIS REPORT").append(nl);
        sb.append("=".repeat(78)).append(nl).append(nl);

        // The findings come first: they are the point of the phase.
        sb.append("--- Diagnostics ---").append(nl);
        if (analyzer == null || analyzer.getDiagnostics().isEmpty()) {
            sb.append("  No semantic problems found.").append(nl);
        } else {
            for (Diagnostic d : analyzer.getDiagnostics()) {
                sb.append("  ").append(d).append(nl);
            }
            sb.append(nl).append("  ")
              .append(analyzer.errorCount()).append(" error(s), ")
              .append(analyzer.warningCount()).append(" warning(s).").append(nl);
        }
        sb.append(nl);

        sb.append("--- Compilation metrics ---").append(nl);
        appendMetrics(sb, backend, nl);
        for (CompilationResult tpl : templates) {
            appendMetrics(sb, tpl, nl);
        }
        sb.append(nl);

        SymbolTable table = backend == null ? null : backend.getSymbolTable();
        if (table == null) {
            sb.append("No symbol table was produced.").append(nl);
            return sb.toString();
        }

        List<Symbol> symbols = new ArrayList<>(table.getAllSymbols());
        symbols.sort((a, b) -> {
            int byScope = a.getScope().compareTo(b.getScope());
            return byScope != 0 ? byScope : Integer.compare(a.getLineNumber(), b.getLineNumber());
        });

        sb.append("--- Symbol table (").append(symbols.size()).append(" symbols) ---").append(nl);
        sb.append(String.format("%-22s %-16s %-16s %-8s %s",
                "Name", "Kind", "Data Type", "Line", "Scope")).append(nl);
        sb.append("-".repeat(78)).append(nl);
        for (Symbol s : symbols) {
            sb.append(String.format("%-22s %-16s %-16s %-8d %s",
                    s.getName(), s.getType(), s.getDataType(),
                    s.getLineNumber(), s.getScope())).append(nl);
        }
        sb.append(nl);

        sb.append("--- Scope tree ---").append(nl);
        appendScope(sb, table.getGlobalScope(), 0, nl);

        return sb.toString();
    }

    private void appendMetrics(StringBuilder sb, CompilationResult r, String nl) {
        if (r == null) {
            return;
        }
        sb.append("  ").append(r.getFilePath()).append(nl);
        if (r.isSuccess()) {
            sb.append("      tokens=").append(r.getTokenCount())
              .append("  parseTreeNodes=").append(r.getParseTreeNodeCount())
              .append("  astNodes=").append(r.getAstNodeCount());
            if (r.getSymbolTable() != null) {
                sb.append("  symbols=").append(r.getSymbolTable().getAllSymbols().size());
            }
            sb.append(nl);
        } else {
            sb.append("      FAILED: ").append(r.getSyntaxErrors())
              .append(" syntax error(s)").append(nl);
        }
    }

    private void appendScope(StringBuilder sb, symboltable.Scope scope, int depth, String nl) {
        String pad = "  ".repeat(depth);
        sb.append(pad).append("Scope: ").append(scope.getName()).append(nl);
        for (Symbol s : scope.getSymbols()) {
            sb.append(pad).append("  - ").append(s.getName())
              .append(" (").append(s.getType()).append(", ")
              .append(s.getDataType()).append(")").append(nl);
        }
        for (symboltable.Scope child : scope.getChildren()) {
            appendScope(sb, child, depth + 1, nl);
        }
    }

    // ------------------------------------------------------------------
    // helpers
    // ------------------------------------------------------------------

    /**
     * Name the generated page after the route that serves it, so the site has a
     * real entry point: the '/' route becomes index.html, '/add' becomes add.html
     * only if that reads better than the template's own name — otherwise the
     * template name is kept, which is what the requirement's example shows.
     */
    static String outputFileName(String templateName, String routeUrl) {
        if ("/".equals(routeUrl)) {
            return "index.html";
        }
        return templateName.endsWith(".html") ? templateName : templateName + ".html";
    }

    private static String baseName(String path) {
        if (path == null) {
            return null;
        }
        String normalised = path.replace('\\', '/');
        int slash = normalised.lastIndexOf('/');
        return slash >= 0 ? normalised.substring(slash + 1) : normalised;
    }
}
