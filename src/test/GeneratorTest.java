package test;

import static test.Assert.*;

import ast.base.ASTNode;
import ast.frontend.JinjaBoundDataNode;
import compiler.CompilationResult;
import compiler.Compiler;
import generator.Generator;

import java.util.List;

/**
 * Tests requirement #2's generator: the data from the Python data array must be
 * carried into the second (Jinja) tree. We compile the real sample backend and
 * the products.html template, run the generator, and assert that concrete values
 * from the Python {@code products = [...]} list now appear as bound-data nodes in
 * the template AST.
 */
public class GeneratorTest {

    private final Compiler compiler = new Compiler();

    public void testDataArrayIsDiscovered() {
        Generator gen = new Generator();
        gen.collectFromBackend(backendAst());

        assertTrue("found the 'products' data array",
                gen.getDataArrays().containsKey("products"));
        assertEquals("products has 3 rows", 3,
                gen.getDataArrays().get("products").rows.size());
    }

    public void testValuesAreBoundIntoJinjaTree() {
        Generator gen = new Generator();
        gen.collectFromBackend(backendAst());

        ASTNode jinja = templateAst("test_programs/templates/products.html");
        int bound = gen.generateInto("products.html", jinja);

        assertTrue("at least one value was bound", bound > 0);

        List<JinjaBoundDataNode> boundNodes =
                TestSupport.findAll(jinja, JinjaBoundDataNode.class);
        assertEquals("bound node count matches return value", bound, boundNodes.size());

        // The concrete name from the Python data array must be present in the tree.
        assertTrue("'Ali Yazbek' was carried from Python into the Jinja tree",
                boundNodes.stream().anyMatch(n -> "Ali Yazbek".equals(n.getResolvedValue())));

        // One iteration marker per Python row (3) should exist on the for-loop.
        long iterationMarkers = boundNodes.stream()
                .filter(n -> n.getBinding().startsWith("product["))
                .count();
        assertEquals("one iteration binding per Python row", 3L, iterationMarkers);
    }

    public void testUnrelatedTemplateGetsNoBindings() {
        Generator gen = new Generator();
        gen.collectFromBackend(backendAst());

        // base.html is not fed by a render_template(...) data array in the sample.
        ASTNode jinja = templateAst("test_programs/templates/base.html");
        int bound = gen.generateInto("base.html", jinja);
        assertEquals("no data bound into a template with no context", 0, bound);
    }

    private ASTNode backendAst() {
        CompilationResult r = compiler.compileBackend("test_programs/run_app.py");
        assertNotNull("backend AST built", r.getAst());
        return r.getAst();
    }

    private ASTNode templateAst(String path) {
        CompilationResult r = compiler.compileFrontend(path);
        assertNotNull("template AST built: " + path, r.getAst());
        return r.getAst();
    }
}
