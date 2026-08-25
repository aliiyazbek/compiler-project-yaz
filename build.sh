#!/usr/bin/env bash
# Build & run the Yazbek compiler project with only a JDK (no Gradle needed).
#
# Usage:
#   ./build.sh           # compile, then run app.Main on the sample project
#   ./build.sh --no-run  # compile only
#   ./build.sh --test    # compile main + tests and run the test runner
#   ./build.sh --serve   # compile, then run the live server (app.Serve)
#   ./build.sh --serve --port 9090
#
# Compiles ONLY src/ (gen/ is a stale duplicate of src/antlr and is excluded).
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JAR="$ROOT/dependencies/antlr-4.13.2-complete.jar"
OUT="$ROOT/out"

# On Windows shells (Git Bash/MSYS/Cygwin) the JDK is a native Windows binary,
# so the classpath must use ';' as the separator AND native Windows paths
# (e.g. C:\...), not MSYS paths (/c/...). winpath() converts when needed.
case "$(uname -s)" in
    MINGW*|MSYS*|CYGWIN*)
        SEP=';'
        winpath() { cygpath -w "$1"; }
        ;;
    *)
        SEP=':'
        winpath() { printf '%s' "$1"; }
        ;;
esac

[ -f "$JAR" ] || { echo "Missing ANTLR jar: $JAR" >&2; exit 1; }

# Classpath-form (native) versions of the output dir and jar.
CP_OUT="$(winpath "$OUT")"
CP_JAR="$(winpath "$JAR")"

RUN=1
TEST=0
SERVE=0
PORT=8080
expect_port=0
for arg in "$@"; do
    if [ "$expect_port" -eq 1 ]; then
        PORT="$arg"
        expect_port=0
        continue
    fi
    case "$arg" in
        --no-run) RUN=0 ;;
        --test)   TEST=1 ;;
        --run)    RUN=1 ;;
        --serve)  SERVE=1 ;;
        --port)   expect_port=1 ;;
        *) echo "Unknown option: $arg" >&2; exit 2 ;;
    esac
done

rm -rf "$OUT"
mkdir -p "$OUT"

# Main sources: everything under src/ except the unit tests and the IDE's
# .antlr/ preview cache (stale duplicate copies of the generated parsers).
# Paths are NUL-delimited and passed via xargs so directories containing spaces
# (e.g. "C:\Users\Haytham Mohammad\...") are handled correctly.
find "$ROOT/src" -name '*.java' -not -path '*/src/test/*' -not -path '*/.antlr/*' -print0 > "$OUT/main.sources"
echo "Compiling $(tr -dc '\0' < "$OUT/main.sources" | wc -c | tr -d ' ') source files..."
xargs -0 javac -cp "$CP_JAR" -d "$OUT" < "$OUT/main.sources"
echo "Build OK -> $OUT"

if [ "$TEST" -eq 1 ]; then
    if find "$ROOT/src/test" -name '*.java' -print0 > "$OUT/test.sources" 2>/dev/null && [ -s "$OUT/test.sources" ]; then
        echo "Compiling tests..."
        xargs -0 javac -cp "$CP_OUT$SEP$CP_JAR" -d "$OUT" < "$OUT/test.sources"
    fi
    echo "Running tests..."
    java -Dfile.encoding=UTF-8 -cp "$CP_OUT$SEP$CP_JAR" test.TestRunner
    exit $?
fi

if [ "$SERVE" -eq 1 ]; then
    echo "Starting the live server on port $PORT..."
    java -Dfile.encoding=UTF-8 -cp "$CP_OUT$SEP$CP_JAR" app.Serve "$PORT"
    exit $?
fi

if [ "$RUN" -eq 1 ]; then
    echo "Running app.Main..."
    java -Dfile.encoding=UTF-8 -cp "$CP_OUT$SEP$CP_JAR" app.Main
fi
