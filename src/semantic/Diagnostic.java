package semantic;

/**
 * One problem found by the semantic analyser.
 *
 * <p>Separate from a syntax error: the file parsed, but it does not <em>mean</em>
 * anything sensible — a template reads a variable nobody passed it, a function
 * uses a name before it exists, a layout extends a file that is not there.
 *
 * <p>Severity decides whether the build fails. An {@link Severity#ERROR} is
 * something that will visibly break the generated site; a
 * {@link Severity#WARNING} is suspicious but still renders.
 */
public class Diagnostic {

    public enum Severity {
        ERROR,
        WARNING
    }

    private final Severity severity;
    private final String file;
    private final int line;
    private final String code;
    private final String message;
    private final String hint;

    public Diagnostic(Severity severity, String file, int line,
                      String code, String message, String hint) {
        this.severity = severity;
        this.file = file;
        this.line = line;
        this.code = code;
        this.message = message;
        this.hint = hint;
    }

    public static Diagnostic error(String file, int line, String code,
                                   String message, String hint) {
        return new Diagnostic(Severity.ERROR, file, line, code, message, hint);
    }

    public static Diagnostic warning(String file, int line, String code,
                                     String message, String hint) {
        return new Diagnostic(Severity.WARNING, file, line, code, message, hint);
    }

    public Severity getSeverity() {
        return severity;
    }

    public boolean isError() {
        return severity == Severity.ERROR;
    }

    public String getFile() {
        return file;
    }

    public int getLine() {
        return line;
    }

    /** A short stable identifier, e.g. "undefined-template-variable". */
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /** An optional suggestion, e.g. the closest name that does exist. */
    public String getHint() {
        return hint;
    }

    /** "ERROR  templates/products.html:31  message (hint)" */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(severity == Severity.ERROR ? "ERROR  " : "WARN   ");
        sb.append(shortFile()).append(':').append(line).append("  ");
        sb.append(message);
        if (hint != null && !hint.isEmpty()) {
            sb.append(" (").append(hint).append(')');
        }
        return sb.toString();
    }

    private String shortFile() {
        if (file == null) {
            return "<unknown>";
        }
        String normalised = file.replace('\\', '/');
        int slash = normalised.lastIndexOf('/');
        return slash >= 0 ? normalised.substring(slash + 1) : normalised;
    }
}
