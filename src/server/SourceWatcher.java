package server;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Watches the compiler's <em>inputs</em> and rebuilds the site when they change.
 *
 * <p>This is the "Java listens and regenerates" half of the requirement, and it
 * complements {@link DataStore}'s change events:
 *
 * <ul>
 *   <li>{@code DataStore} fires when the <b>data</b> changes — a visitor added or
 *       deleted a product through the running server.</li>
 *   <li>{@code SourceWatcher} fires when the <b>source</b> changes — someone edited
 *       {@code run_app.py} or a template in the editor.</li>
 * </ul>
 *
 * Both end at the same place: {@link SiteRenderer} rewrites {@code output/}.
 *
 * <p>Editing the Python re-seeds the data store (its {@code products = [...]} literal
 * is the source of truth for the baseline), whereas editing a template only
 * re-parses the templates and leaves any rows added at runtime alone.
 *
 * <p>Runs on its own daemon thread, so it never keeps the JVM alive by itself.
 * Editors typically write a file in several steps, so events are debounced: a
 * burst of changes produces one rebuild, not five.
 */
public class SourceWatcher implements Runnable {

    /** Ignore repeat events for the same file within this window. */
    private static final long DEBOUNCE_MILLIS = 300;

    private final Path backendFile;
    private final Path templateDir;
    private final SiteRenderer renderer;

    private final Map<String, Long> lastSeen = new HashMap<>();

    private volatile boolean running;
    private Thread thread;
    private WatchService watchService;

    public SourceWatcher(String backendSource, String templateDir, SiteRenderer renderer) {
        this.backendFile = Paths.get(backendSource).toAbsolutePath().normalize();
        this.templateDir = Paths.get(templateDir).toAbsolutePath().normalize();
        this.renderer = renderer;
    }

    /** Begin watching on a background daemon thread. */
    public void start() throws IOException {
        watchService = FileSystems.getDefault().newWatchService();

        // Directories are what a WatchService can register, not single files, so
        // the backend's parent directory is watched and events filtered by name.
        register(backendFile.getParent());
        register(templateDir);

        running = true;
        thread = new Thread(this, "source-watcher");
        thread.setDaemon(true);
        thread.start();
    }

    private void register(Path dir) throws IOException {
        if (dir != null && Files.isDirectory(dir)) {
            dir.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
        }
    }

    public void stop() {
        running = false;
        if (thread != null) {
            thread.interrupt();
        }
        try {
            if (watchService != null) {
                watchService.close();
            }
        } catch (IOException ignored) {
            // Shutting down; a failure to close the watch service is not actionable.
        }
    }

    @Override
    public void run() {
        while (running) {
            WatchKey key;
            try {
                key = watchService.take();
            } catch (InterruptedException | ClosedWatchServiceException e) {
                return; // stop() was called
            }

            boolean backendChanged = false;
            boolean templateChanged = false;

            for (WatchEvent<?> event : key.pollEvents()) {
                if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }
                Path dir = (Path) key.watchable();
                Path changed = dir.resolve((Path) event.context()).toAbsolutePath().normalize();

                if (!isInteresting(changed) || isDuplicate(changed)) {
                    continue;
                }

                if (changed.equals(backendFile)) {
                    backendChanged = true;
                } else if (changed.startsWith(templateDir)) {
                    templateChanged = true;
                }
            }

            key.reset();

            if (backendChanged || templateChanged) {
                rebuild(backendChanged);
            }
        }
    }

    /** Only the Python backend and template files matter here. */
    private boolean isInteresting(Path file) {
        if (file.equals(backendFile)) {
            return true;
        }
        String name = file.getFileName().toString();
        return file.startsWith(templateDir)
                && (name.endsWith(".html") || name.endsWith(".jinja"));
    }

    /** Collapse the burst of events an editor emits for a single save. */
    private boolean isDuplicate(Path file) {
        long now = System.currentTimeMillis();
        String key = file.toString();
        Long previous = lastSeen.get(key);
        lastSeen.put(key, now);
        return previous != null && now - previous < DEBOUNCE_MILLIS;
    }

    private void rebuild(boolean backendChanged) {
        String reason = backendChanged ? "run_app.py" : "a template";
        System.out.println("  [watcher] " + reason + " changed — recompiling...");
        try {
            // Let the editor finish writing before re-reading the file.
            Thread.sleep(DEBOUNCE_MILLIS);

            int errors = renderer.reloadSources(backendChanged);
            if (errors > 0) {
                System.out.println("  [watcher] " + errors
                        + " error(s); the previous build is still being served.");
                return;
            }
            renderer.render();
            System.out.println("  [watcher] regenerated: " + renderer.getLastSummary());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("  [watcher] rebuild failed: " + e.getMessage());
        }
    }
}
