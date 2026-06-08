# Yazbek Compiler Project

An academic two-language **compiler front-end** for a Flask web application. It
performs the classic analysis phases — **lexical analysis → parsing → AST
construction → symbol table** — over two source languages:

- **Backend:** Python / Flask (`app.py`-style server code)
- **Frontend:** HTML + CSS + Jinja2 templates

It is a front-end only: there is no code generation. The output is the set of
intermediate compiler artifacts (token counts, parse-tree sizes, ASTs, and a
typed symbol table), printed so each phase is visible.

## Requirements

- JDK 17 or newer (developed on JDK 25)
- The ANTLR runtime is **vendored** in `dependencies/antlr-4.13.2-complete.jar`
  — no internet access is needed to build.

## Build & run

### Option A — build scripts (JDK only, recommended)

Windows (PowerShell):

```powershell
.\build.ps1          # compile + run the full pipeline on the sample project
.\build.ps1 -Test    # compile + run the unit tests
.\build.ps1 -NoRun   # compile only
```

Linux / macOS / Git Bash:

```bash
./build.sh           # compile + run
./build.sh --test    # compile + run tests
./build.sh --no-run  # compile only
```

You can also compile a specific file:

```bash
java -cp out:dependencies/antlr-4.13.2-complete.jar app.Main path/to/file.py
```

### Option B — Gradle

```bash
gradle run     # run app.Main
gradle test    # run unit tests
```

> Gradle pulls JUnit from Maven Central, so it needs network access the first
> time. The build scripts above have **zero** external dependencies and are the
> reliable path for offline grading.

## Project layout

```
src/
  antlr/        ANTLR grammars (.g4) + generated lexers/parsers
    backend/    Python/Flask grammar
    frontend/   HTML/CSS/Jinja2 grammar
  ast/          AST node classes (base + backend + frontend)
  visitor/      Parse-tree -> AST visitors (also populate the symbol table)
  symboltable/  Scope-tree-based symbol table (Scope, Symbol, SymbolTable)
  compiler/     Reusable compilation pipeline (Compiler, CompilationResult)
  app/          Main.java — single entry point / driver
  test/         Zero-dependency unit tests + tiny test runner
test_programs/  The sample Flask app, templates, and stylesheet being compiled
dependencies/   Vendored ANTLR jar
gen/            STALE generated copy — NOT compiled (duplicate of src/antlr)
```

> **Note:** `gen/` is an old duplicate of `src/antlr/frontend` left over from an
> earlier setup. The build scripts and Gradle config compile **only `src/`** to
> avoid duplicate-class errors. It can safely be deleted.

## What this build improved over the original

- **Real nested scopes.** The symbol table is now a tree of `Scope`s with proper
  lexical name resolution, instead of a flat `name -> list` map tagged with a
  scope string.
- **No more AST data loss.** Dict keys, keyword-argument names, and if/elif/else
  + for-loop structure are now preserved as dedicated AST nodes.
- **Type inference.** The `dataType` column is populated (list / dict / integer /
  string / boolean / inferred-from-builtins) instead of always `unknown`.
- **One reusable pipeline.** The four near-identical `*Test` driver classes were
  replaced by `compiler.Compiler` + a single `app.Main`.
- **Reproducible builds** via scripts and Gradle.
- **Real unit tests** (19 assertions) covering scoping, AST fidelity, type
  inference, and end-to-end compilation.

## Tests

`build.ps1 -Test` / `./build.sh --test` runs `test.TestRunner`, a tiny
reflection-based runner (no JUnit needed). Test classes live in `src/test/`:

- `SymbolTableTest` — nested scope separation and outward resolution
- `BackendAstTest` — dict keys, kwarg names, if/for structure
- `TypeInferenceTest` — inferred data types
- `CompilerPipelineTest` — the sample project compiles end-to-end

## Next step (not yet implemented)

**Semantic analysis** — undeclared-variable and duplicate-definition checks, and
richer type checking — is the planned next phase, building on the scoped symbol
table introduced here.
