package test;

import static test.Assert.*;

import ast.base.ASTNode;
import compiler.CompilationResult;
import compiler.Compiler;
import generator.ContextBuilder;
import generator.HtmlEmitter;
import generator.TemplateContext;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Tests the code-generation stage: the Python context must reach the templates,
 * and the emitter must turn a Jinja tree plus that context into real HTML.
 *
 * These assertions are about the <em>emitted text</em>, which is what the
 * requirement's output/ directory actually contains — distinct from
 * {@link GeneratorTest}, which checks the data binding is visible in the tree.
 */
public class CodeGenerationTest {

    private final Compiler compiler = new Compiler();

    // ------------------------------------------------------------------
    // context building
    // ------------------------------------------------------------------

    public void testGlobalDataArrayBecomesAList() {
        ContextBuilder builder = buildContexts();
        TemplateContext.Value products = builder.getGlobals().get("products");

        assertNotNull("products is a global value", products);
        assertTrue("products is a list", products.isList());
        assertEquals("products has 3 rows", 3, products.getList().size());

        TemplateContext.Value first = products.getList().get(0);
        assertTrue("each row is a dict", first.isDict());
        assertEquals("first row's name field", "Ali Yazbek",
                first.field("name").getScalar());
    }

    public void testLenCallIsFoldedIntoTheContext() {
        ContextBuilder builder = buildContexts();
        TemplateContext ctx = builder.contextFor("products.html");

        assertNotNull("products.html has a context", ctx);
        TemplateContext.Value total = ctx.get("total");
        assertNotNull("total reached the template", total);
        assertEquals("len(products) folded to 3", "3", total.getScalar());
    }

    public void testRouteUrlIsRecordedForTemplate() {
        ContextBuilder builder = buildContexts();
        assertEquals("products.html is served by the root route",
                "/", builder.getTemplateRoutes().get("products.html"));
    }

    public void testIndexAccessResolvesThroughRouteParameter() {
        ContextBuilder builder = buildContexts();
        TemplateContext ctx = builder.contextFor("product_detail.html");

        assertNotNull("product_detail.html has a context", ctx);
        TemplateContext.Value product = ctx.get("product");
        assertNotNull("products[index] was resolved", product);
        assertTrue("the resolved product is a dict", product.isDict());
        assertEquals("route id 1 selects the first row", "Ali Yazbek",
                product.field("name").getScalar());
    }

    // ------------------------------------------------------------------
    // emitting
    // ------------------------------------------------------------------

    public void testLoopBodyIsRepeatedPerRow() {
        String html = emit("products.html");

        // Every member of the Python data array must appear in the output.
        assertTrue("first member rendered", html.contains("Ali Yazbek"));
        assertTrue("second member rendered", html.contains("Ali Suliman"));
        assertTrue("third member rendered", html.contains("Sara Nabhan"));

        assertEquals("the card markup is emitted once per row", 3,
                countOccurrences(html, "class=\"productcard\""));
    }

    public void testExpressionOutsideLoopIsSubstituted() {
        String html = emit("products.html");
        assertTrue("{{ total }} became the folded value 3",
                html.contains("<span class=\"countvalue\">3</span>"));
        assertFalse("no raw Jinja expression survives", html.contains("{{"));
    }

    public void testExpressionInsideAttributeIsInterpolated() {
        String html = emit("products.html");
        // href="/product/{{ product.id }}" must resolve per row.
        assertTrue("first row's link", html.contains("href=\"/product/1\""));
        assertTrue("second row's link", html.contains("href=\"/product/2\""));
        assertTrue("third row's link", html.contains("href=\"/product/3\""));
    }

    public void testFalseConditionIsNotEmitted() {
        // add_product.html guards a block with {% if success %}; nothing supplies
        // 'success', so the block must be absent from the output.
        String html = emit("add_product.html");
        assertFalse("the success branch was pruned",
                html.contains("Member added successfully"));
        assertTrue("the rest of the page still rendered",
                html.contains("Add New Member"));
    }

    public void testEmittedPageIsWellFormedHtml() {
        String html = emit("products.html");
        assertTrue("starts with a doctype", html.startsWith("<!DOCTYPE html>"));
        assertTrue("closes the document", html.trim().endsWith("</html>"));
        assertEquals("one opening body tag", 1, countOccurrences(html, "<body>"));
        assertEquals("one closing body tag", 1, countOccurrences(html, "</body>"));
        assertFalse("no binding markers leaked into the HTML",
                html.contains("JinjaBoundData"));
    }

    public void testMissingContextRendersEmptyRatherThanRawSyntax() {
        // Emit a template with a deliberately empty context: expressions must
        // disappear rather than leak {{ ... }} into the page.
        ASTNode tree = templateAst("test_programs/templates/product_detail.html");
        HtmlEmitter emitter = new HtmlEmitter();
        String html = emitter.emit("product_detail.html", tree,
                new TemplateContext(), new LinkedHashMap<>());

        assertFalse("no raw expression syntax in the output", html.contains("{{"));
        assertTrue("the unresolved expressions were counted",
                emitter.getExpressionsUnresolved() > 0);
    }

    // ------------------------------------------------------------------
    // helpers
    // ------------------------------------------------------------------

    private ContextBuilder buildContexts() {
        CompilationResult backend = compiler.compileBackend("test_programs/run_app.py");
        assertNotNull("backend AST built", backend.getAst());

        ContextBuilder builder = new ContextBuilder();
        builder.build(backend.getAst());
        return builder;
    }

    /** Compile the sample project and emit one template to HTML. */
    private String emit(String templateName) {
        ContextBuilder builder = buildContexts();

        Map<String, ASTNode> templates = new LinkedHashMap<>();
        for (String name : new String[]{"base.html", "products.html", "add_product.html",
                                        "product_detail.html", "delete_confirm.html"}) {
            templates.put(name, templateAst("test_programs/templates/" + name));
        }

        String html = new HtmlEmitter().emit(templateName, templates.get(templateName),
                builder.contextFor(templateName), templates);
        assertTrue("emitted non-empty HTML for " + templateName, html.length() > 0);
        return html;
    }

    private ASTNode templateAst(String path) {
        CompilationResult r = compiler.compileFrontend(path);
        assertNotNull("template AST built: " + path, r.getAst());
        return r.getAst();
    }

    private static int countOccurrences(String haystack, String needle) {
        int count = 0;
        int at = haystack.indexOf(needle);
        while (at >= 0) {
            count++;
            at = haystack.indexOf(needle, at + needle.length());
        }
        return count;
    }
}
