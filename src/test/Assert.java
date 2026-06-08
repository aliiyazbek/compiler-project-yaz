package test;

/**
 * Minimal assertion helpers so the project's tests have no external dependency
 * (the vendored ANTLR jar is the only third-party artifact). Each failed
 * assertion throws AssertionError, which TestRunner catches and reports.
 */
public final class Assert {

    private Assert() {}

    public static void assertTrue(String message, boolean condition) {
        if (!condition) {
            throw new AssertionError(message + " (expected true)");
        }
    }

    public static void assertFalse(String message, boolean condition) {
        if (condition) {
            throw new AssertionError(message + " (expected false)");
        }
    }

    public static void assertEquals(String message, Object expected, Object actual) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(message + " — expected <" + expected + "> but was <" + actual + ">");
        }
    }

    public static void assertNotNull(String message, Object value) {
        if (value == null) {
            throw new AssertionError(message + " (expected non-null)");
        }
    }

    public static void assertNull(String message, Object value) {
        if (value != null) {
            throw new AssertionError(message + " (expected null but was <" + value + ">)");
        }
    }
}
