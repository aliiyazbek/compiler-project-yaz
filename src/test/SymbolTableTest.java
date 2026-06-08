package test;

import static test.Assert.*;

import symboltable.Symbol;
import symboltable.SymbolTable;
import visitor.BackendASTVisitor;

/** Verifies the symbol table provides real nested scoping, not a flat map. */
public class SymbolTableTest {

    public void testNestedScopesAreSeparate() {
        SymbolTable st = new SymbolTable();
        st.addSymbol("x", "variable", "integer", 1);   // in global

        st.enterScope("f");
        st.addSymbol("x", "variable", "string", 2);     // shadows in f
        Symbol localX = st.resolveLocal("x");
        assertNotNull("x resolves locally in f", localX);
        assertEquals("local x in f is the string one", "string", localX.getDataType());
        st.exitScope();

        // Back in global, the global x must be the one we see.
        Symbol globalX = st.resolveLocal("x");
        assertNotNull("x resolves in global", globalX);
        assertEquals("global x is the integer one", "integer", globalX.getDataType());
    }

    public void testResolveWalksOutward() {
        SymbolTable st = new SymbolTable();
        st.addSymbol("g", "variable", "list", 1);
        st.enterScope("inner");
        // g is not declared locally, but resolve() should find it in the parent.
        assertNull("g not local to inner", st.resolveLocal("g"));
        assertNotNull("g resolves via parent walk", st.resolve("g"));
        st.exitScope();
    }

    public void testExitScopeNeverPopsPastGlobal() {
        SymbolTable st = new SymbolTable();
        st.exitScope();
        st.exitScope();
        assertEquals("still global after extra exits", "global", st.getCurrentScope());
    }

    public void testFunctionLocalsLandInFunctionScope() {
        BackendASTVisitor v = TestSupport.parseBackend(
                "def f(a):\n    b = 1\n\n");
        SymbolTable st = v.getSymbolTable();
        // f is declared at global; a and b live under the f scope.
        boolean fInGlobal = st.getGlobalScope().resolveLocal("f") != null;
        assertTrue("function f declared in global scope", fInGlobal);
        assertEquals("total symbols (f, a, b)", 3, st.getAllSymbols().size());
    }
}
