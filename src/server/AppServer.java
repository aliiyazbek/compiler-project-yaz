package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Serves the generated site and handles the mutations that drive regeneration.
 *
 * <p>This is the "Java works like a server" half of the requirement. It replaces
 * Flask at runtime: the routes the Python declared are answered here, over the
 * {@link DataStore}, and every mutation re-renders the site through
 * {@link SiteRenderer} before replying.
 *
 * <pre>
 *   GET  /                       -> output/index.html
 *   GET  /product/{id}           -> a detail page rendered for that id
 *   GET  /delete/{id}            -> the delete-confirmation page for that id
 *   GET  /add                    -> output/add_product.html
 *   POST /add                    -> add a row, re-render, redirect to /
 *   POST /confirm-delete/{id}    -> delete a row, re-render, redirect to /
 *   GET  /static/**              -> files copied into output/static
 * </pre>
 *
 * <p>Built on the JDK's own {@code com.sun.net.httpserver}, so the project keeps
 * its "vendored ANTLR jar is the only dependency" property.
 *
 * <p>Note the id-parameterised GETs: the compiler can only bake one representative
 * id into a static page, so those routes render on demand for the id asked for
 * rather than serving the one file on disk.
 */
public class AppServer {

    private static final int STOP_DELAY_SECONDS = 0;

    private final int port;
    private final Path outputDir;
    private final DataStore store;
    private final SiteRenderer renderer;

    private HttpServer server;
    private java.util.concurrent.ExecutorService executor;

    public AppServer(int port, String projectRoot, DataStore store, SiteRenderer renderer) {
        this.port = port;
        this.outputDir = Paths.get(projectRoot == null ? "." : projectRoot).resolve("output");
        this.store = store;
        this.renderer = renderer;
    }

    public int getPort() {
        return port;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", this::handle);

        // A small pool: requests that trigger a re-render must not block each
        // other. The threads are daemons so a stopped server never keeps the JVM
        // alive — HttpServer.stop() shuts down the listener but not the executor.
        executor = Executors.newFixedThreadPool(4, runnable -> {
            Thread worker = new Thread(runnable, "http-worker");
            worker.setDaemon(true);
            return worker;
        });
        server.setExecutor(executor);
        server.start();
    }

    public void stop() {
        if (server != null) {
            server.stop(STOP_DELAY_SECONDS);
        }
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    // ------------------------------------------------------------------
    // routing
    // ------------------------------------------------------------------

    private void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if ("POST".equalsIgnoreCase(method)) {
                handlePost(exchange, path);
            } else {
                handleGet(exchange, path);
            }
        } catch (Exception e) {
            // Never let a handler throw: the client would see a dropped connection.
            System.err.println("  Request failed: " + e);
            sendText(exchange, 500, "Internal error: " + e.getMessage());
        } finally {
            exchange.close();
        }
    }

    private void handleGet(HttpExchange exchange, String path) throws IOException {
        if ("/".equals(path)) {
            sendFile(exchange, outputDir.resolve("index.html"));
            return;
        }

        String detailId = idAfter(path, "/product/");
        if (detailId != null) {
            sendRenderedForId(exchange, "product_detail.html", detailId);
            return;
        }

        String editId = idAfter(path, "/edit/");
        if (editId != null) {
            sendRenderedForId(exchange, "edit_product.html", editId);
            return;
        }

        String deleteId = idAfter(path, "/delete/");
        if (deleteId != null) {
            sendRenderedForId(exchange, "delete_confirm.html", deleteId);
            return;
        }

        if ("/add".equals(path)) {
            sendFile(exchange, outputDir.resolve("add_product.html"));
            return;
        }

        // Anything else is a file in output/ (static assets, generated pages).
        Path candidate = resolveSafely(path);
        if (candidate != null && Files.isRegularFile(candidate)) {
            sendFile(exchange, candidate);
            return;
        }

        sendText(exchange, 404, "Not found: " + path);
    }

    private void handlePost(HttpExchange exchange, String path) throws IOException {
        if ("/add".equals(path)) {
            Map<String, String> form = parseForm(readBody(exchange));
            String id = store.add(form);
            System.out.println("  [server] added row id=" + id + "; site re-rendered.");
            redirect(exchange, "/");
            return;
        }

        String editId = idAfter(path, "/edit/");
        if (editId != null) {
            Map<String, String> form = parseForm(readBody(exchange));
            boolean updated = store.update(editId, form);
            System.out.println("  [server] edit id=" + editId
                    + (updated ? "; site re-rendered." : " - no such row."));
            redirect(exchange, updated ? "/product/" + editId : "/");
            return;
        }

        String id = idAfter(path, "/confirm-delete/");
        if (id != null) {
            boolean removed = store.deleteById(id);
            System.out.println("  [server] delete id=" + id
                    + (removed ? "; site re-rendered." : " — no such row."));
            redirect(exchange, "/");
            return;
        }

        sendText(exchange, 404, "No POST route for " + path);
    }

    /**
     * Render one template for a specific id.
     *
     * The static file on disk was baked for a single representative id, so
     * requesting /product/2 must not serve the /product/1 page. The renderer is
     * asked for that row's page directly.
     */
    private void sendRenderedForId(HttpExchange exchange, String template, String id)
            throws IOException {
        Map<String, String> row = store.findById(id);
        if (row == null) {
            sendText(exchange, 404, "No " + store.getCollectionName() + " with id " + id);
            return;
        }
        String html = renderer.renderForRow(template, row);
        if (html == null) {
            sendText(exchange, 404, "Template not available: " + template);
            return;
        }
        sendHtml(exchange, 200, html);
    }

    // ------------------------------------------------------------------
    // request/response plumbing
    // ------------------------------------------------------------------

    /** The id segment after a prefix, or null when the path does not match. */
    private static String idAfter(String path, String prefix) {
        if (!path.startsWith(prefix)) {
            return null;
        }
        String rest = path.substring(prefix.length());
        if (rest.isEmpty() || rest.contains("/")) {
            return null;
        }
        return rest;
    }

    /**
     * Map a URL path to a file inside output/, refusing anything that escapes it.
     * Without the containment check, "/../../etc/passwd" would be served.
     */
    private Path resolveSafely(String urlPath) {
        String relative = urlPath.startsWith("/") ? urlPath.substring(1) : urlPath;
        Path base = outputDir.toAbsolutePath().normalize();
        Path resolved = base.resolve(relative).normalize();
        return resolved.startsWith(base) ? resolved : null;
    }

    private String readBody(HttpExchange exchange) throws IOException {
        byte[] raw = readAll(exchange);
        return new String(raw, StandardCharsets.UTF_8);
    }

    private static byte[] readAll(HttpExchange exchange) throws IOException {
        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
        byte[] chunk = new byte[8192];
        int read;
        while ((read = exchange.getRequestBody().read(chunk)) > 0) {
            buffer.write(chunk, 0, read);
        }
        return buffer.toByteArray();
    }

    /** Parse an application/x-www-form-urlencoded body into field values. */
    public static Map<String, String> parseForm(String body) {
        Map<String, String> fields = new LinkedHashMap<>();
        if (body == null || body.isEmpty()) {
            return fields;
        }
        for (String pair : body.split("&")) {
            if (pair.isEmpty()) {
                continue;
            }
            int equals = pair.indexOf('=');
            String name = equals < 0 ? pair : pair.substring(0, equals);
            String value = equals < 0 ? "" : pair.substring(equals + 1);
            fields.put(urlDecode(name), urlDecode(value));
        }
        return fields;
    }

    private static String urlDecode(String raw) {
        try {
            return URLDecoder.decode(raw, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return raw;
        }
    }

    private void redirect(HttpExchange exchange, String location) throws IOException {
        exchange.getResponseHeaders().add("Location", location);
        exchange.sendResponseHeaders(303, -1);
    }

    private void sendFile(HttpExchange exchange, Path file) throws IOException {
        if (!Files.isRegularFile(file)) {
            sendText(exchange, 404, "Not found: " + file.getFileName());
            return;
        }
        byte[] body = Files.readAllBytes(file);
        exchange.getResponseHeaders().add("Content-Type", contentType(file.toString()));
        // The site is re-rendered in place, so a cached copy would hide updates.
        exchange.getResponseHeaders().add("Cache-Control", "no-store");
        exchange.sendResponseHeaders(200, body.length);
        write(exchange, body);
    }

    private void sendHtml(HttpExchange exchange, int status, String html) throws IOException {
        byte[] body = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
        exchange.getResponseHeaders().add("Cache-Control", "no-store");
        exchange.sendResponseHeaders(status, body.length);
        write(exchange, body);
    }

    private void sendText(HttpExchange exchange, int status, String message) throws IOException {
        byte[] body = message.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
        exchange.sendResponseHeaders(status, body.length);
        write(exchange, body);
    }

    private void write(HttpExchange exchange, byte[] body) throws IOException {
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(body);
        }
    }

    private static String contentType(String fileName) {
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".html")) return "text/html; charset=utf-8";
        if (lower.endsWith(".css"))  return "text/css; charset=utf-8";
        if (lower.endsWith(".js"))   return "application/javascript; charset=utf-8";
        if (lower.endsWith(".png"))  return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".gif"))  return "image/gif";
        if (lower.endsWith(".svg"))  return "image/svg+xml";
        if (lower.endsWith(".py"))   return "text/plain; charset=utf-8";
        return "application/octet-stream";
    }
}
