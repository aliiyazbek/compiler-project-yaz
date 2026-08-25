package output;

/**
 * A tiny, dependency-free JSON builder.
 *
 * The project deliberately ships with only the ANTLR jar on the classpath, so
 * rather than pulling in Jackson/Gson for the two AST dumps we need, this class
 * writes well-formed, indented JSON directly into a {@link StringBuilder}.
 *
 * It is intentionally minimal: objects, arrays, strings, numbers and booleans —
 * exactly what {@link AstJsonSerializer} emits. Callers are responsible for
 * balancing their begin/end calls; there is no validation beyond the indent
 * bookkeeping.
 */
public class JsonWriter {

    private final StringBuilder sb = new StringBuilder();
    private int indent;
    /** True right after a begin* call, so we know not to prepend a comma. */
    private boolean firstEntry = true;

    /** Escape a Java string into a JSON string literal (without quotes). */
    public static String escape(String raw) {
        if (raw == null) {
            return "";
        }
        StringBuilder out = new StringBuilder(raw.length() + 8);
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            switch (c) {
                case '"':  out.append("\\\""); break;
                case '\\': out.append("\\\\"); break;
                case '\n': out.append("\\n");  break;
                case '\r': out.append("\\r");  break;
                case '\t': out.append("\\t");  break;
                case '\b': out.append("\\b");  break;
                case '\f': out.append("\\f");  break;
                default:
                    // Escape the remaining control characters as \ u00XX.
                    if (c < 0x20) {
                        out.append(String.format("\\u%04x", (int) c));
                    } else {
                        out.append(c);
                    }
            }
        }
        return out.toString();
    }

    // ------------------------------------------------------------------
    // structure
    // ------------------------------------------------------------------

    public JsonWriter beginObject() {
        separate();
        sb.append('{');
        indent++;
        firstEntry = true;
        return this;
    }

    public JsonWriter endObject() {
        indent--;
        if (!firstEntry) {
            newLine();
        }
        sb.append('}');
        firstEntry = false;
        return this;
    }

    public JsonWriter beginArray(String name) {
        separate();
        writeName(name);
        sb.append('[');
        indent++;
        firstEntry = true;
        return this;
    }

    public JsonWriter endArray() {
        indent--;
        if (!firstEntry) {
            newLine();
        }
        sb.append(']');
        firstEntry = false;
        return this;
    }

    /** Open an object as an array element (no property name). */
    public JsonWriter beginArrayElement() {
        return beginObject();
    }

    // ------------------------------------------------------------------
    // values
    // ------------------------------------------------------------------

    public JsonWriter field(String name, String value) {
        separate();
        writeName(name);
        sb.append('"').append(escape(value)).append('"');
        return this;
    }

    public JsonWriter field(String name, int value) {
        separate();
        writeName(name);
        sb.append(value);
        return this;
    }

    public JsonWriter field(String name, boolean value) {
        separate();
        writeName(name);
        sb.append(value);
        return this;
    }

    /** A string element inside an array (no property name). */
    public JsonWriter value(String value) {
        separate();
        sb.append('"').append(escape(value)).append('"');
        return this;
    }

    // ------------------------------------------------------------------
    // internals
    // ------------------------------------------------------------------

    /** Emit the comma + newline that precede every entry except the first. */
    private void separate() {
        if (!firstEntry) {
            sb.append(',');
        }
        if (sb.length() > 0) {
            newLine();
        }
        firstEntry = false;
    }

    private void writeName(String name) {
        if (name != null) {
            sb.append('"').append(escape(name)).append("\": ");
        }
    }

    private void newLine() {
        sb.append('\n');
        for (int i = 0; i < indent; i++) {
            sb.append("  ");
        }
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}
