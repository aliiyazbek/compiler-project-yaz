package test;

import static test.Assert.*;

import symboltable.Symbol;
import symboltable.SymbolTable;
import visitor.BackendASTVisitor;

/** Verifies the type-inference pass fills the dataType column instead of "unknown". */
public class TypeInferenceTest {

    private String typeOf(String source, String varName) {
        BackendASTVisitor v = TestSupport.parseBackend(source);
        SymbolTable st = v.getSymbolTable();
        for (Symbol s : st.getAllSymbols()) {
            if (s.getName().equals(varName)) {
                return s.getDataType();
            }
        }
        return null;
    }

    public void testListLiteralInferred() {
        assertEquals("list literal -> list", "list", typeOf("xs = [1, 2, 3]\n\n", "xs"));
    }

    public void testDictLiteralInferred() {
        assertEquals("dict literal -> dict", "dict", typeOf("d = {\"a\": 1}\n\n", "d"));
    }

    public void testIntegerLiteralInferred() {
        assertEquals("int literal -> integer", "integer", typeOf("n = 42\n\n", "n"));
    }

    public void testStringLiteralInferred() {
        assertEquals("string literal -> string", "string", typeOf("s = \"hi\"\n\n", "s"));
    }

    public void testLenCallInferred() {
        assertEquals("len(...) -> integer", "integer", typeOf("xs = [1]\nc = len(xs)\n\n", "c"));
    }

    public void testComparisonInferredBoolean() {
        assertEquals("a > b -> boolean", "boolean", typeOf("r = 1 > 2\n\n", "r"));
    }

    public void testIdentifierCopyPropagatesType() {
        // y copies x; x is a list, so y should be inferred as list too.
        assertEquals("copied identifier keeps type", "list", typeOf("x = [1]\ny = x\n\n", "y"));
    }
}
