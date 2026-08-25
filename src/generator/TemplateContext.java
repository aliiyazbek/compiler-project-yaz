package generator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The value model the generator passes to the template emitter — the compiler's
 * stand-in for the keyword arguments a real {@code render_template(...)} call
 * would receive.
 *
 * The original {@link Generator} could only carry {@code Map<String, String>},
 * which is enough to label a node in a tree dump but not enough to actually
 * render: {@code {% for product in products %}} needs a genuine <em>list</em> to
 * iterate, and {@code {{ product.name }}} needs a genuine <em>dict</em> to look a
 * field up in. {@link Value} models exactly those three shapes — scalar, list and
 * dict — which is all the sample Flask app uses.
 */
public class TemplateContext {

    /** A scalar, a list or a dict; exactly one of the three fields is set. */
    public static class Value {
        private final String scalar;
        private final List<Value> list;
        private final Map<String, Value> dict;

        private Value(String scalar, List<Value> list, Map<String, Value> dict) {
            this.scalar = scalar;
            this.list = list;
            this.dict = dict;
        }

        public static Value of(String scalar) {
            return new Value(scalar == null ? "" : scalar, null, null);
        }

        public static Value ofList(List<Value> items) {
            return new Value(null, items == null ? new ArrayList<>() : items, null);
        }

        public static Value ofDict(Map<String, Value> fields) {
            return new Value(null, null, fields == null ? new LinkedHashMap<>() : fields);
        }

        public boolean isScalar() {
            return scalar != null;
        }

        public boolean isList() {
            return list != null;
        }

        public boolean isDict() {
            return dict != null;
        }

        public String getScalar() {
            return scalar;
        }

        public List<Value> getList() {
            return list;
        }

        public Map<String, Value> getDict() {
            return dict;
        }

        /** Look a field up on a dict value; null for non-dicts or missing keys. */
        public Value field(String name) {
            return dict == null ? null : dict.get(name);
        }

        /**
         * Jinja's notion of truthiness, used by {@code {% if %}}: empty strings,
         * empty collections, {@code 0} and {@code False} are falsy.
         */
        public boolean isTruthy() {
            if (list != null) {
                return !list.isEmpty();
            }
            if (dict != null) {
                return !dict.isEmpty();
            }
            if (scalar == null || scalar.isEmpty()) {
                return false;
            }
            return !"0".equals(scalar) && !"False".equals(scalar) && !"None".equals(scalar);
        }

        /** How the value appears once substituted into the HTML. */
        public String render() {
            if (scalar != null) {
                return scalar;
            }
            if (list != null) {
                return "[" + list.size() + " items]";
            }
            return String.valueOf(dict);
        }

        @Override
        public String toString() {
            return render();
        }
    }

    /** Top-level template variables, e.g. "products" and "total". */
    private final Map<String, Value> variables = new LinkedHashMap<>();

    public void put(String name, Value value) {
        variables.put(name, value);
    }

    public Value get(String name) {
        return variables.get(name);
    }

    public boolean has(String name) {
        return variables.containsKey(name);
    }

    public Map<String, Value> getVariables() {
        return variables;
    }

    /** A shallow copy — the emitter forks the context to bind loop iterators. */
    public TemplateContext copy() {
        TemplateContext clone = new TemplateContext();
        clone.variables.putAll(this.variables);
        return clone;
    }

    /**
     * Resolve a dotted path such as {@code product.name} against this context.
     * Returns null when any segment is missing, which the emitter renders as an
     * empty string (matching Jinja's default undefined behaviour).
     */
    public Value resolve(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        String[] parts = path.trim().split("\\.");
        Value current = variables.get(parts[0]);
        for (int i = 1; i < parts.length && current != null; i++) {
            current = current.field(parts[i]);
        }
        return current;
    }

    @Override
    public String toString() {
        return variables.toString();
    }
}
