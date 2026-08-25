package server;

import generator.TemplateContext;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The live data the generated site is rendered from.
 *
 * <p>This is the piece that makes regeneration meaningful. {@code ContextBuilder}
 * reads {@code products = [...]} out of the Python AST, which is fixed at compile
 * time — re-rendering from the AST would always produce identical pages no matter
 * what a visitor did. So the store is <b>seeded</b> from the AST once and from
 * then on owns the truth: adding or deleting a product mutates the store, and the
 * store tells its listeners, which is what triggers a fresh render.
 *
 * <pre>
 *   AST (once, at startup) ──▶ DataStore ──▶ listeners ──▶ SiteRenderer ──▶ output/*.html
 *                                  ▲
 *                            add / delete
 * </pre>
 *
 * <p>Every mutation is synchronized and listeners are held in a
 * {@link CopyOnWriteArrayList}, because the HTTP server calls in from its own
 * worker threads while the file watcher may be reloading on another.
 */
public class DataStore {

    /** Notified after any mutation, so the site can be re-rendered. */
    public interface ChangeListener {
        void onDataChanged(String description);
    }

    /** The collection name as it appears in the Python source, e.g. "products". */
    private final String collectionName;

    /** Rows, each an ordered field map — the same shape a Python dict has. */
    private final List<Map<String, String>> rows = new ArrayList<>();

    /** Field order, taken from the first seeded row, so new rows match it. */
    private final List<String> fieldOrder = new ArrayList<>();

    private final List<ChangeListener> listeners = new CopyOnWriteArrayList<>();

    /** Tracks the highest id handed out, so new rows never collide. */
    private int highestId;

    public DataStore(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void addListener(ChangeListener listener) {
        listeners.add(listener);
    }

    // ------------------------------------------------------------------
    // seeding from the compiled Python data
    // ------------------------------------------------------------------

    /**
     * Load the initial rows from the value the compiler extracted from the AST.
     * Replaces anything already held, and does <em>not</em> fire listeners: this
     * is the baseline, not a change to it.
     */
    public synchronized void seedFrom(TemplateContext.Value listValue) {
        rows.clear();
        fieldOrder.clear();
        highestId = 0;

        if (listValue == null || !listValue.isList()) {
            return;
        }

        for (TemplateContext.Value element : listValue.getList()) {
            if (!element.isDict()) {
                continue;
            }
            Map<String, String> row = new LinkedHashMap<>();
            for (Map.Entry<String, TemplateContext.Value> field : element.getDict().entrySet()) {
                TemplateContext.Value fieldValue = field.getValue();
                row.put(field.getKey(), fieldValue.isScalar() ? fieldValue.getScalar() : "");
                if (!fieldOrder.contains(field.getKey())) {
                    fieldOrder.add(field.getKey());
                }
            }
            rows.add(row);
            trackId(row.get("id"));
        }
    }

    private void trackId(String rawId) {
        try {
            highestId = Math.max(highestId, Integer.parseInt(rawId.trim()));
        } catch (RuntimeException ignored) {
            // Non-numeric or absent ids simply do not advance the counter.
        }
    }

    // ------------------------------------------------------------------
    // reading
    // ------------------------------------------------------------------

    public synchronized int size() {
        return rows.size();
    }

    /** A defensive copy, so callers cannot mutate the store behind its back. */
    public synchronized List<Map<String, String>> snapshot() {
        List<Map<String, String>> copy = new ArrayList<>(rows.size());
        for (Map<String, String> row : rows) {
            copy.add(new LinkedHashMap<>(row));
        }
        return copy;
    }

    /** The row whose "id" field matches, or null. */
    public synchronized Map<String, String> findById(String id) {
        for (Map<String, String> row : rows) {
            if (id != null && id.equals(row.get("id"))) {
                return new LinkedHashMap<>(row);
            }
        }
        return null;
    }

    /**
     * Render the live rows back into the value model the emitter consumes, so a
     * re-render reads from the store rather than from the compile-time AST.
     */
    public synchronized TemplateContext.Value asValue() {
        List<TemplateContext.Value> items = new ArrayList<>(rows.size());
        for (Map<String, String> row : rows) {
            Map<String, TemplateContext.Value> fields = new LinkedHashMap<>();
            for (Map.Entry<String, String> field : row.entrySet()) {
                fields.put(field.getKey(), TemplateContext.Value.of(field.getValue()));
            }
            items.add(TemplateContext.Value.ofDict(fields));
        }
        return TemplateContext.Value.ofList(items);
    }

    // ------------------------------------------------------------------
    // mutation
    // ------------------------------------------------------------------

    /**
     * Append a row, assigning it the next free id.
     *
     * @param submitted the user-supplied fields (an id here is ignored)
     * @return the id assigned to the new row
     */
    public String add(Map<String, String> submitted) {
        String assignedId;
        synchronized (this) {
            Map<String, String> row = new LinkedHashMap<>();
            assignedId = String.valueOf(++highestId);
            row.put("id", assignedId);

            // Follow the seeded field order so every row has the same shape.
            for (String field : fieldOrder) {
                if ("id".equals(field)) {
                    continue;
                }
                String value = submitted.get(field);
                row.put(field, value == null ? "" : value);
            }
            // Keep any extra field the form supplied that the seed did not have.
            for (Map.Entry<String, String> entry : submitted.entrySet()) {
                row.putIfAbsent(entry.getKey(), entry.getValue());
            }
            rows.add(row);
        }
        fireChanged("added " + collectionName + " row id=" + assignedId);
        return assignedId;
    }

    /** Remove the row with this id. Returns true if a row was actually removed. */
    public boolean deleteById(String id) {
        boolean removed;
        synchronized (this) {
            removed = rows.removeIf(row -> id != null && id.equals(row.get("id")));
        }
        if (removed) {
            fireChanged("deleted " + collectionName + " row id=" + id);
        }
        return removed;
    }

    private void fireChanged(String description) {
        // Fired outside the lock: a listener re-renders the site, which is slow
        // and must not block other requests from reading the store.
        for (ChangeListener listener : listeners) {
            listener.onDataChanged(description);
        }
    }
}
