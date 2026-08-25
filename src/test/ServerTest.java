package test;

import static test.Assert.*;

import generator.TemplateContext;
import server.AppServer;
import server.DataStore;
import server.SiteRenderer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Tests the live half of the project: the data store, the regeneration it
 * triggers, and the HTTP server that drives both.
 *
 * The server is bound to an ephemeral port and everything is written under a
 * temporary directory, so these tests never collide with a running instance or
 * disturb the project's real output/ tree.
 */
public class ServerTest {

    private static final String BACKEND = "test_programs/run_app.py";
    private static final String TEMPLATES = "test_programs/templates";
    private static final String STATIC = "test_programs/static";

    // ------------------------------------------------------------------
    // the data store
    // ------------------------------------------------------------------

    public void testSeedingReadsRowsFromCompiledValue() {
        DataStore store = seededStore();
        assertEquals("three rows seeded from the Python list", 3, store.size());
        assertNotNull("row 1 is addressable by id", store.findById("1"));
        assertEquals("the row keeps its field values", "Ali Yazbek",
                store.findById("1").get("name"));
    }

    public void testAddAssignsNextIdAndNotifies() {
        DataStore store = seededStore();
        List<String> events = new ArrayList<>();
        store.addListener(events::add);

        Map<String, String> submitted = new LinkedHashMap<>();
        submitted.put("name", "New Member");
        submitted.put("details", "Tester");
        String id = store.add(submitted);

        assertEquals("the id follows the highest seeded one", "4", id);
        assertEquals("the row was appended", 4, store.size());
        assertEquals("one change event fired", 1, events.size());
        assertEquals("the new row is retrievable", "New Member",
                store.findById("4").get("name"));
    }

    public void testDeleteRemovesRowAndNotifies() {
        DataStore store = seededStore();
        List<String> events = new ArrayList<>();
        store.addListener(events::add);

        assertTrue("an existing row is deleted", store.deleteById("2"));
        assertEquals("the row is gone", 2, store.size());
        assertNull("it can no longer be found", store.findById("2"));
        assertEquals("one change event fired", 1, events.size());

        assertFalse("deleting a missing row reports false", store.deleteById("99"));
        assertEquals("and fires no event", 1, events.size());
    }

    public void testSnapshotIsDefensive() {
        DataStore store = seededStore();
        List<Map<String, String>> snapshot = store.snapshot();
        snapshot.get(0).put("name", "mutated");
        snapshot.clear();

        assertEquals("the store is unaffected by changes to a snapshot",
                3, store.size());
        assertEquals("row values are unchanged", "Ali Yazbek",
                store.findById("1").get("name"));
    }

    public void testFormParsingDecodesFields() {
        Map<String, String> form = AppServer.parseForm(
                "name=Ali+Yazbek&details=Computer%20Science&image=");

        assertEquals("plus signs become spaces", "Ali Yazbek", form.get("name"));
        assertEquals("percent escapes are decoded", "Computer Science", form.get("details"));
        assertEquals("an empty field stays empty", "", form.get("image"));
    }

    // ------------------------------------------------------------------
    // regeneration
    // ------------------------------------------------------------------

    public void testAddingDataRegeneratesTheSite() throws Exception {
        Path sandbox = Files.createTempDirectory("yazbek-regen-test");
        try {
            DataStore store = new DataStore("products");
            SiteRenderer renderer = newRenderer(sandbox, store);
            renderer.reloadSources(true);
            renderer.render();

            String before = read(sandbox.resolve("output/index.html"));
            assertTrue("the seeded members are present", before.contains("Ali Yazbek"));
            assertFalse("the new member is not there yet", before.contains("Zara Testing"));

            // Wire regeneration the way Serve does, then mutate.
            store.addListener(d -> {
                try {
                    renderer.render();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            Map<String, String> submitted = new LinkedHashMap<>();
            submitted.put("name", "Zara Testing");
            submitted.put("details", "Added at runtime");
            store.add(submitted);

            String after = read(sandbox.resolve("output/index.html"));
            assertTrue("the new member reached the HTML", after.contains("Zara Testing"));
            assertTrue("the earlier members are still there", after.contains("Ali Yazbek"));
            assertTrue("more than one render happened", renderer.getRenderCount() > 1);
        } finally {
            deleteRecursively(sandbox);
        }
    }

    public void testDeletingDataRegeneratesTheSite() throws Exception {
        Path sandbox = Files.createTempDirectory("yazbek-regen-del-test");
        try {
            DataStore store = new DataStore("products");
            SiteRenderer renderer = newRenderer(sandbox, store);
            renderer.reloadSources(true);
            renderer.render();
            store.addListener(d -> {
                try {
                    renderer.render();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            store.deleteById("2");

            String after = read(sandbox.resolve("output/index.html"));
            assertFalse("the deleted member is gone from the HTML",
                    after.contains("Ali Suliman"));
            assertTrue("the others remain", after.contains("Ali Yazbek"));
        } finally {
            deleteRecursively(sandbox);
        }
    }

    /**
     * The count came from a folded len(products), so it must be recomputed —
     * otherwise the page keeps showing the number that was true at compile time.
     */
    public void testRowCountIsRecomputedAfterMutation() throws Exception {
        Path sandbox = Files.createTempDirectory("yazbek-count-test");
        try {
            DataStore store = new DataStore("products");
            SiteRenderer renderer = newRenderer(sandbox, store);
            renderer.reloadSources(true);
            renderer.render();

            assertTrue("the seeded count is rendered",
                    read(sandbox.resolve("output/index.html"))
                            .contains("<span class=\"countvalue\">3</span>"));

            store.addListener(d -> {
                try {
                    renderer.render();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            Map<String, String> submitted = new LinkedHashMap<>();
            submitted.put("name", "Fourth Member");
            store.add(submitted);

            assertTrue("the count follows the live data",
                    read(sandbox.resolve("output/index.html"))
                            .contains("<span class=\"countvalue\">4</span>"));
        } finally {
            deleteRecursively(sandbox);
        }
    }

    public void testDetailPageRendersForTheRequestedRow() throws Exception {
        Path sandbox = Files.createTempDirectory("yazbek-detail-test");
        try {
            DataStore store = new DataStore("products");
            SiteRenderer renderer = newRenderer(sandbox, store);
            renderer.reloadSources(true);
            renderer.render();

            // The static page is baked for one id; asking for another must differ.
            String second = renderer.renderForRow("product_detail.html", store.findById("2"));
            assertNotNull("a page was rendered", second);
            assertTrue("it shows the requested member", second.contains("Ali Suliman"));
            assertFalse("not the baked-in one", second.contains("Ali Yazbek"));
        } finally {
            deleteRecursively(sandbox);
        }
    }

    // ------------------------------------------------------------------
    // the HTTP server
    // ------------------------------------------------------------------

    public void testServerServesAndMutates() throws Exception {
        Path sandbox = Files.createTempDirectory("yazbek-http-test");
        DataStore store = new DataStore("products");
        SiteRenderer renderer = newRenderer(sandbox, store);
        renderer.reloadSources(true);
        renderer.render();
        store.addListener(d -> {
            try {
                renderer.render();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        int port = freePort();
        AppServer server = new AppServer(port, sandbox.toString(), store, renderer);
        server.start();
        try {
            String home = get(port, "/");
            assertTrue("the home page is served", home.contains("Ali Yazbek"));
            assertTrue("the seeded count is shown",
                    home.contains("<span class=\"countvalue\">3</span>"));

            // A detail page must reflect the id in the URL, not the baked one.
            assertTrue("/product/2 shows the second member",
                    get(port, "/product/2").contains("Ali Suliman"));
            assertEquals("an unknown id is a 404", 404, status(port, "/product/99"));

            // Adding through the server must regenerate and be visible immediately.
            assertEquals("POST /add redirects", 303,
                    post(port, "/add", "name=Http+Member&details=Added+over+HTTP"));
            assertEquals("the row reached the store", 4, store.size());

            String afterAdd = get(port, "/");
            assertTrue("the new member is served", afterAdd.contains("Http Member"));
            assertTrue("the count was recomputed",
                    afterAdd.contains("<span class=\"countvalue\">4</span>"));

            assertEquals("POST confirm-delete redirects", 303,
                    post(port, "/confirm-delete/1", ""));
            assertFalse("the deleted member is gone",
                    get(port, "/").contains("Ali Yazbek"));

            // A path that climbs out of output/ must not be served.
            assertEquals("directory traversal is refused", 404,
                    status(port, "/../build.gradle"));
        } finally {
            server.stop();
            deleteRecursively(sandbox);
        }
    }

    // ------------------------------------------------------------------
    // helpers
    // ------------------------------------------------------------------

    private DataStore seededStore() {
        compiler.Compiler compiler = new compiler.Compiler();
        generator.ContextBuilder builder = new generator.ContextBuilder();
        builder.build(compiler.compileBackend(BACKEND).getAst());

        TemplateContext.Value products = builder.getGlobals().get("products");
        assertNotNull("the compiler found the products list", products);

        DataStore store = new DataStore("products");
        store.seedFrom(products);
        return store;
    }

    private SiteRenderer newRenderer(Path sandbox, DataStore store) {
        return new SiteRenderer(sandbox.toString(), BACKEND, TEMPLATES, STATIC, store);
    }

    private static int freePort() throws IOException {
        try (java.net.ServerSocket socket = new java.net.ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    private static String get(int port, String path) throws IOException {
        HttpURLConnection connection = open(port, path);
        connection.setRequestMethod("GET");
        try (java.io.InputStream in = connection.getInputStream()) {
            return new String(readAll(in), StandardCharsets.UTF_8);
        } finally {
            connection.disconnect();
        }
    }

    private static int status(int port, String path) throws IOException {
        HttpURLConnection connection = open(port, path);
        connection.setRequestMethod("GET");
        try {
            return connection.getResponseCode();
        } finally {
            connection.disconnect();
        }
    }

    private static int post(int port, String path, String body) throws IOException {
        HttpURLConnection connection = open(port, path);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        // Follow-redirects would turn the 303 we want to assert on into a 200.
        connection.setInstanceFollowRedirects(false);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        try (OutputStream out = connection.getOutputStream()) {
            out.write(body.getBytes(StandardCharsets.UTF_8));
        }
        try {
            return connection.getResponseCode();
        } finally {
            connection.disconnect();
        }
    }

    private static HttpURLConnection open(int port, String path) throws IOException {
        URL url;
        try {
            url = java.net.URI.create("http://localhost:" + port + path).toURL();
        } catch (IllegalArgumentException e) {
            // A deliberately malformed path (the traversal check) still needs a
            // connection object, so fall back to the literal form.
            url = new URL("http://localhost:" + port + path);
        }
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        return connection;
    }

    private static byte[] readAll(java.io.InputStream in) throws IOException {
        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
        byte[] chunk = new byte[8192];
        int read;
        while ((read = in.read(chunk)) > 0) {
            buffer.write(chunk, 0, read);
        }
        return buffer.toByteArray();
    }

    private static String read(Path file) throws IOException {
        return new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
    }

    private static void deleteRecursively(Path dir) throws IOException {
        if (!Files.exists(dir)) {
            return;
        }
        try (Stream<Path> paths = Files.walk(dir)) {
            paths.sorted(Comparator.reverseOrder()).forEach(p -> {
                try {
                    Files.deleteIfExists(p);
                } catch (IOException ignored) {
                    // A leftover temp file is not worth failing the test over.
                }
            });
        }
    }
}
