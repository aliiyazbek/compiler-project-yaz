# Yazbek Compiler Project

An academic two-language **compiler** for a Flask web application. It performs
the classic analysis phases — **lexical analysis → parsing → AST construction →
symbol table** — over two source languages, plus a **generator** step that
carries data between the two trees:

- **Backend:** Python / Flask (`app.py`-style server code)
- **Frontend:** HTML + CSS + Jinja2 templates

It then **generates** a working static site from those trees, and can **serve it
live**, regenerating the HTML whenever the data or the sources change.

Two entry points:

| | |
| --- | --- |
| `app.Main` | compile once — print every phase, write `output/` and `compiler_output/` |
| `app.Serve` | stay up — serve the site and regenerate it on every change |

## The generator (data passing between the two trees)

Requirement #2 asks that a generator pass the data from the Python data array
into the second (Jinja) tree. `generator.Generator` does exactly this:

1. It reads the **first tree** (Python AST) to find global list-of-dict data
   arrays (e.g. `products = [...]`) and to read each
   `render_template('x.html', products=products)` call, learning which template
   variable is fed by which Python variable.
2. It walks the **second tree** (HTML + Jinja2 AST) and injects
   `JinjaBoundDataNode`s: one per Python row under each `{% for %}` loop, and the
   resolved concrete value under each `{{ item.field }}` expression.

The bound values become real children of the Jinja AST, so they appear in the
existing `printTree()` output (see "PHASE 4: GENERATOR" when you run the app).

## Code generation (the emitted website)

Phase 5 turns the analysed trees into actual files. Where `Generator` *annotates*
the tree so the binding is visible, the emitter **evaluates** it and writes HTML:
a `{% for %}` over three rows emits its body three times with different values,
and a false `{% if %}` emits nothing at all.

```
run_app.py ──▶ ContextBuilder ──▶ TemplateContext (products=[…], total=3)
                                        │
templates/*.html ──▶ Jinja AST ──▶ HtmlEmitter ──▶ output/*.html
```

- **`generator.ContextBuilder`** reads the Python AST and works out which values
  reach which template — folding `len(products)` to `3`, resolving
  `products[index]`, and binding route parameters such as `<int:id>`.
- **`generator.TemplateContext`** models those values as real scalars, lists and
  dicts (a flat `Map<String,String>` cannot back a `{% for %}` loop).
- **`generator.HtmlEmitter`** walks the Jinja AST with that context and emits
  HTML — including `{% extends %}`/`{% block %}` inheritance and `{{ … }}`
  interpolation *inside attribute values*, which the grammar hands over as one
  opaque `STRING` token.

### Output layout

```
output/                  the runnable site
  index.html             generated (products.html, served by the '/' route)
  add_product.html       generated
  product_detail.html    generated
  delete_confirm.html    generated
  app.py                 copied unchanged
  static/                copied unchanged (style.css, images/)

compiler_output/         the compiler's own artifacts
  ast_python.json        the Python AST
  ast_jinja.json         every template's Jinja AST
  semantic_report.txt    metrics, symbol table, scope tree
  generation_log.txt     what was resolved, skipped, and why
```

Support files (`app.py`, `style.css`, images) are **copied verbatim** — they are
inputs to the running app, not subjects of translation, so the compiler does not
rewrite them. A template that no route renders (`base.html`) is used as a layout
but produces no page of its own.

Pages are named after the route that serves them, so `'/'` becomes `index.html`.
A route with a URL parameter (`/product/<int:id>`) has no single compile-time
value, so the page is generated for the first id and this is recorded in
`generation_log.txt`.

## Semantic analysis (error checking)

This phase used to only *gather* information — a symbol table and inferred types
— and never rejected anything, so a typo like `{{ prodcut.name }}` silently
rendered as empty. `semantic.SemanticAnalyzer` turns it into a real phase that
reports problems with a file, a line, and a suggestion.

**Python side**

| Check | Example |
| --- | --- |
| `undefined-name` | using a variable that was never assigned |
| `undefined-function` | calling a function that does not exist |
| `missing-template` | `render_template('nope.html')` |
| `route-returns-nothing` | a view with no `return` (warning) |

**Template side**

| Check | Example |
| --- | --- |
| `undefined-template-variable` | `{{ x }}` where no route passes `x` |
| `unknown-field` | `{{ product.titel }}` — the row has no such field |
| `not-iterable` | `{% for x in total %}` where `total` is a number |
| `missing-layout` | `{% extends %}` naming a file that is not there |
| `unrendered-template` | a template no route renders (warning) |

Name checks are **scope-aware** — the analyser keeps its own scope stack, since
the visitor's symbol table has already collapsed back to global scope by the time
construction finishes. Function names are hoisted first, so one route may call
another defined further down the file.

Misspellings get a suggestion measured by **Damerau-Levenshtein** distance, which
counts a transposition (`titel` → `title`) as the single edit it actually is;
plain Levenshtein scores it 2 and would push the real name out of range.

Findings appear in the console as `PHASE 6`, and in full in
`compiler_output/semantic_report.txt`.

## Regeneration — the live server

`app.Main` compiles once and exits. `app.Serve` stays up, serves the generated
site, and **keeps the HTML in step with reality**. Two independent triggers feed
the same renderer:

```
DataStore     ──(data changed: add / delete)──┐
                                              ├──▶ SiteRenderer ──▶ output/*.html
SourceWatcher ──(run_app.py or a template edited)──┘

AppServer ──▶ serves output/ and performs the add / delete mutations
```

- **`server.DataStore`** holds the live rows. It is *seeded* from the Python
  `products = [...]` literal once, then owns the truth — and fires a change event
  on every add or delete.
- **`server.SiteRenderer`** re-runs the emitter with the store's current rows in
  place of the compile-time data, and rewrites the pages.
- **`server.AppServer`** is a JDK `com.sun.net.httpserver` server standing in for
  Flask at runtime: it answers the routes the Python declared and performs the
  mutations.
- **`server.SourceWatcher`** is a `WatchService` on `run_app.py` and `templates/`;
  editing either recompiles and re-renders without a restart.

### Why the store exists

`ContextBuilder` reads `products` out of the AST, which is fixed at compile time.
Re-rendering straight from the AST would produce byte-identical pages no matter
what a visitor did — so regeneration only becomes meaningful once a mutable store
owns the data. Values *derived* from it are re-derived too: `total` came from
`len(products)`, so it is recomputed after every mutation rather than keeping the
number that was true when the compiler ran.

### Running it

```powershell
.\build.ps1 -Serve              # http://localhost:8080
.\build.ps1 -Serve -Port 9090
```

```bash
./build.sh --serve
./build.sh --serve --port 9090
```

| Route | Behaviour |
| --- | --- |
| `GET /` | `output/index.html` |
| `GET /product/{id}` | rendered on demand for that id |
| `GET /delete/{id}` | delete-confirmation page for that id |
| `POST /add` | adds a row, re-renders, redirects to `/` |
| `POST /confirm-delete/{id}` | deletes a row, re-renders, redirects to `/` |
| `GET /static/**` | assets copied into `output/static` |

The id-parameterised GETs render per request because the compiler can only bake
one representative id into a static file — `/product/2` must not serve the page
built for id 1.

Editing a **template** re-parses the templates and keeps rows added at runtime;
editing **`run_app.py`** re-seeds the store from source, since its `products`
literal is the baseline.

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
  generator/    Generator (tree binding) + ContextBuilder/TemplateContext/HtmlEmitter
  output/       Writes output/ and compiler_output/ (BuildDriver, JSON, reports)
  semantic/     Error checking (SemanticAnalyzer, Diagnostic)
  server/       Live runtime: DataStore, SiteRenderer, AppServer, SourceWatcher
  compiler/     Reusable compilation pipeline (Compiler, CompilationResult)
  app/          Main.java (compile once) and Serve.java (live server)
  test/         Zero-dependency unit tests + tiny test runner
test_programs/  The sample Flask app, templates, and stylesheet being compiled
dependencies/   Vendored ANTLR jar
gen/            STALE generated copy — NOT compiled (duplicate of src/antlr)

output/          generated site      (created by a run; safe to delete)
compiler_output/ analysis artifacts  (created by a run; safe to delete)
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
- **Actual code generation.** The compiler now writes files: rendered HTML pages
  in `output/` and analysis artifacts in `compiler_output/`, rather than only
  printing trees to the console.
- **A live runtime.** `app.Serve` regenerates the site when the data changes
  (add/delete over HTTP) or when a source file is edited.
- **Semantic analysis that actually rejects.** Undefined names, unpassed template
  variables, misspelled fields, and missing templates are reported with a line
  number and a suggestion, instead of silently rendering as empty.
- **Real unit tests** (64 assertions) covering scoping, AST fidelity, type
  inference, end-to-end compilation, HTML emission, file output, semantic checks,
  and the server.

## Tests

`build.ps1 -Test` / `./build.sh --test` runs `test.TestRunner`, a tiny
reflection-based runner (no JUnit needed). Test classes live in `src/test/`:

- `SymbolTableTest` — nested scope separation and outward resolution
- `BackendAstTest` — dict keys, kwarg names, if/for structure
- `TypeInferenceTest` — inferred data types
- `CompilerPipelineTest` — the sample project compiles end-to-end
- `GeneratorTest` — the Python data array is bound into the Jinja tree
- `CodeGenerationTest` — context building and HTML emission (loops unrolled,
  attributes interpolated, false conditions pruned, no raw `{{ }}` left)
- `OutputWriterTest` — both output trees are written, `app.py` is copied
  byte-identical, and the JSON artifacts are well formed
- `SemanticAnalyzerTest` — each check fires on a deliberately broken project, and
  (just as importantly) the real sample project stays clean, which is what guards
  against false positives
- `ServerTest` — the data store, regeneration after add/delete (including the
  recomputed row count), per-id detail rendering, and the HTTP server end-to-end
  on an ephemeral port

## Grammar limitations to be aware of

Two quirks of the current grammar affect what you can write in the sample app:

- **Blank lines between decorated functions are required.** Two `@app.route`
  functions written back to back without a blank line between them will not parse.
- **English words that are Jinja keywords break template text.** The lexer has no
  separate mode for text, so a sentence containing a bare `for`, `if`, `in`, or
  `block` outside `{% %}` is tokenised as the keyword. Write "details of X"
  rather than "details for X".

Neither is introduced by the generation or semantic phases; both live in
`src/antlr/`.

## Next step (not yet implemented)

**Type checking across the two languages** — verifying that a template uses a
value the way its inferred Python type allows (iterating only lists, comparing
compatible types) — is the natural next phase. The analyser currently checks that
names and fields *exist*; it does not yet check that every *use* of them is
type-correct.
