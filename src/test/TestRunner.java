package test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * A tiny zero-dependency test runner. It reflectively invokes every public,
 * no-arg method whose name starts with "test" on each registered test class,
 * counts passes/failures, prints a summary, and exits non-zero if anything
 * failed (so build scripts / CI can detect regressions).
 */
public class TestRunner {

    private static final Class<?>[] TEST_CLASSES = {
            SymbolTableTest.class,
            BackendAstTest.class,
            TypeInferenceTest.class,
            CompilerPipelineTest.class,
    };

    public static void main(String[] args) {
        int passed = 0;
        List<String> failures = new ArrayList<>();

        for (Class<?> testClass : TEST_CLASSES) {
            System.out.println("\n# " + testClass.getSimpleName());
            for (Method m : testClass.getDeclaredMethods()) {
                if (!m.getName().startsWith("test") || m.getParameterCount() != 0) {
                    continue;
                }
                String label = testClass.getSimpleName() + "." + m.getName();
                try {
                    Object instance = testClass.getDeclaredConstructor().newInstance();
                    m.invoke(instance);
                    System.out.println("  [PASS] " + m.getName());
                    passed++;
                } catch (Throwable t) {
                    Throwable cause = t.getCause() != null ? t.getCause() : t;
                    System.out.println("  [FAIL] " + m.getName() + " -> " + cause);
                    failures.add(label + ": " + cause.getMessage());
                }
            }
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("Tests run: " + (passed + failures.size())
                + ", Passed: " + passed + ", Failed: " + failures.size());
        if (!failures.isEmpty()) {
            System.out.println("\nFailures:");
            for (String f : failures) {
                System.out.println("  - " + f);
            }
            System.out.println("=".repeat(60));
            System.exit(1);
        }
        System.out.println("ALL TESTS PASSED");
        System.out.println("=".repeat(60));
    }
}
