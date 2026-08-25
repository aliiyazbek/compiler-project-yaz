package output;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Writes everything the compiler produces to disk, in the two-directory layout
 * the project requires:
 *
 * <pre>
 *   output/            the runnable app
 *     index.html       generated from templates/products.html
 *     ...              one file per rendered template
 *     app.py           copied unchanged
 *     static/          copied unchanged (style.css, script.js, images)
 *
 *   compiler_output/   the compiler's own artefacts
 *     ast_python.json
 *     ast_jinja.json
 *     semantic_report.txt
 *     generation_log.txt
 * </pre>
 *
 * Support files (app.py, style.css, script.js, images) are <em>copied verbatim</em>:
 * they are inputs to the running app, not subjects of translation, so the
 * compiler must not rewrite them.
 */
public class OutputWriter {

    private final Path outputDir;
    private final Path compilerOutputDir;
    private final List<String> written = new ArrayList<>();

    public OutputWriter(String projectRoot) {
        Path root = Paths.get(projectRoot == null ? "." : projectRoot);
        this.outputDir = root.resolve("output");
        this.compilerOutputDir = root.resolve("compiler_output");
    }

    public Path getOutputDir() {
        return outputDir;
    }

    public Path getCompilerOutputDir() {
        return compilerOutputDir;
    }

    public List<String> getWrittenFiles() {
        return written;
    }

    /**
     * Create both target directories, clearing any files left by a previous run
     * so stale output cannot be mistaken for fresh output.
     */
    public void prepare() throws IOException {
        resetDirectory(outputDir);
        resetDirectory(compilerOutputDir);
    }

    private void resetDirectory(Path dir) throws IOException {
        if (Files.exists(dir)) {
            deleteRecursively(dir);
        }
        Files.createDirectories(dir);
    }

    private void deleteRecursively(Path dir) throws IOException {
        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path d, IOException e) throws IOException {
                if (e != null) {
                    throw e;
                }
                Files.delete(d);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    // ------------------------------------------------------------------
    // output/ — the generated app
    // ------------------------------------------------------------------

    /** Write one generated HTML page into output/. */
    public void writeHtml(String fileName, String html) throws IOException {
        Path target = outputDir.resolve(fileName);
        Files.write(target, html.getBytes(StandardCharsets.UTF_8));
        written.add("output/" + fileName);
    }

    /**
     * Copy a support file into output/ unchanged.
     *
     * @param source     the file to copy
     * @param targetName its name inside output/ (app.py, style.css, ...)
     * @return true if the file existed and was copied
     */
    public boolean copySupportFile(Path source, String targetName) throws IOException {
        if (source == null || !Files.exists(source)) {
            return false;
        }
        Path target = outputDir.resolve(targetName);
        Files.createDirectories(target.getParent());
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        written.add("output/" + targetName.replace('\\', '/'));
        return true;
    }

    /** Recursively copy a directory (e.g. static/) into output/, unchanged. */
    public int copyDirectory(Path sourceDir, String targetPrefix) throws IOException {
        if (sourceDir == null || !Files.isDirectory(sourceDir)) {
            return 0;
        }
        final int[] count = {0};
        Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String relative = sourceDir.relativize(file).toString().replace('\\', '/');
                Path target = outputDir.resolve(targetPrefix + "/" + relative);
                Files.createDirectories(target.getParent());
                Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
                written.add("output/" + targetPrefix + "/" + relative);
                count[0]++;
                return FileVisitResult.CONTINUE;
            }
        });
        return count[0];
    }

    // ------------------------------------------------------------------
    // compiler_output/ — analysis artefacts
    // ------------------------------------------------------------------

    public void writeCompilerArtifact(String fileName, String content) throws IOException {
        Path target = compilerOutputDir.resolve(fileName);
        Files.write(target, content.getBytes(StandardCharsets.UTF_8));
        written.add("compiler_output/" + fileName);
    }
}
