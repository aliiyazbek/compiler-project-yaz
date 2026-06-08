package test;

import static test.Assert.*;

import compiler.CompilationResult;
import compiler.Compiler;

/** End-to-end checks that the unified Compiler compiles the sample project cleanly. */
public class CompilerPipelineTest {

    private final Compiler compiler = new Compiler();

    public void testBackendSampleCompiles() {
        CompilationResult r = compiler.compileBackend("test_programs/run_app.py");
        assertTrue("backend compiles without syntax errors", r.isSuccess());
        assertEquals("no syntax errors", 0, r.getSyntaxErrors());
        assertNotNull("backend produced an AST", r.getAst());
        assertNotNull("backend produced a symbol table", r.getSymbolTable());
        assertTrue("symbol table is non-empty", r.getSymbolTable().getAllSymbols().size() > 0);
    }

    public void testAllTemplatesCompile() {
        String[] templates = {
                "test_programs/templates/base.html",
                "test_programs/templates/products.html",
                "test_programs/templates/add_product.html",
                "test_programs/templates/product_detail.html",
                "test_programs/templates/delete_confirm.html",
        };
        for (String tpl : templates) {
            CompilationResult r = compiler.compileFrontend(tpl);
            assertTrue("template compiles: " + tpl, r.isSuccess());
            assertEquals("no syntax errors in " + tpl, 0, r.getSyntaxErrors());
        }
    }

    public void testStylesheetCompiles() {
        CompilationResult r = compiler.compileCss("test_programs/static/style.css");
        assertTrue("stylesheet compiles", r.isSuccess());
        assertEquals("no CSS syntax errors", 0, r.getSyntaxErrors());
    }

    public void testMissingFileReportsError() {
        CompilationResult r = compiler.compileBackend("test_programs/does_not_exist.py");
        assertFalse("missing file is not a success", r.isSuccess());
        assertTrue("missing file reports >=1 error", r.getSyntaxErrors() >= 1);
    }
}
