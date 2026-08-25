package app;

import server.AppServer;
import server.DataStore;
import server.SiteRenderer;
import server.SourceWatcher;

/**
 * Runs the compiled site as a live application.
 *
 * <p>Where {@link Main} compiles once and exits, this entry point stays up and
 * keeps the generated HTML in step with reality. It wires the three pieces that
 * make regeneration happen:
 *
 * <pre>
 *   DataStore  ──(data changed: add/delete)──┐
 *                                            ├──▶ SiteRenderer ──▶ output/*.html
 *   SourceWatcher ──(source file edited)─────┘
 *
 *   AppServer ──▶ serves output/ and performs the add/delete mutations
 * </pre>
 *
 * <p>Usage:
 * <pre>
 *   java app.Serve            # http://localhost:8080
 *   java app.Serve 9090       # a different port
 * </pre>
 */
public class Serve {

    private static final int DEFAULT_PORT = 8080;
    private static final String PROJECT_ROOT = ".";
    private static final String BACKEND_SOURCE = "test_programs/run_app.py";
    private static final String TEMPLATE_DIR = "test_programs/templates";
    private static final String STATIC_DIR = "test_programs/static";

    /** The Python list the site is driven by. */
    private static final String COLLECTION = "products";

    public static void main(String[] args) throws Exception {
        int port = args.length > 0 ? parsePort(args[0]) : DEFAULT_PORT;

        System.out.println("=".repeat(70));
        System.out.println("  YAZBEK COMPILER - LIVE SERVER");
        System.out.println("=".repeat(70));
        System.out.println();

        DataStore store = new DataStore(COLLECTION);
        SiteRenderer renderer = new SiteRenderer(
                PROJECT_ROOT, BACKEND_SOURCE, TEMPLATE_DIR, STATIC_DIR, store);

        // 1. Compile the sources and seed the store from the Python data array.
        System.out.println("  Compiling sources...");
        int errors = renderer.reloadSources(true);
        if (errors > 0) {
            System.err.println("  Compilation failed with " + errors + " error(s).");
            System.exit(1);
        }
        System.out.println("  Seeded " + store.size() + " " + COLLECTION + " row(s).");

        // 2. Regenerate whenever the data changes. This is the answer to
        //    "who is responsible for regeneration": the store fires, we re-render.
        store.addListener(description -> {
            try {
                renderer.render();
                System.out.println("  [regen] " + description
                        + " -> " + renderer.getLastSummary());
            } catch (Exception e) {
                System.err.println("  [regen] failed: " + e.getMessage());
            }
        });

        // 3. First full build.
        renderer.render();
        System.out.println("  Initial build: " + renderer.getLastSummary());

        // 4. Watch the sources so editing them rebuilds the site too.
        SourceWatcher watcher = new SourceWatcher(BACKEND_SOURCE, TEMPLATE_DIR, renderer);
        watcher.start();
        System.out.println("  Watching " + BACKEND_SOURCE + " and " + TEMPLATE_DIR + "/");

        // 5. Serve.
        AppServer server = new AppServer(port, PROJECT_ROOT, store, renderer);
        server.start();

        System.out.println();
        System.out.println("  Serving http://localhost:" + port + "/");
        System.out.println("  Add or delete a member in the browser and the HTML is rewritten.");
        System.out.println("  Press Ctrl+C to stop.");
        System.out.println();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n  Shutting down...");
            watcher.stop();
            server.stop();
        }));

        // Park the main thread; the server and watcher run on their own threads.
        Thread.currentThread().join();
    }

    private static int parsePort(String raw) {
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            System.err.println("  Invalid port '" + raw + "'; using " + DEFAULT_PORT + ".");
            return DEFAULT_PORT;
        }
    }
}
