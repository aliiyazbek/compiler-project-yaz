package test;

import static test.Assert.*;

import ast.backend.*;
import ast.base.ASTNode;

import java.util.List;

/** Verifies the AST data-loss bugs are fixed: dict keys, kwarg names, if/for structure. */
public class BackendAstTest {

    public void testDictKeysArePreserved() {
        ASTNode ast = TestSupport.parseBackendAst("d = {\"id\": 1, \"name\": \"x\"}\n\n");
        List<DictEntryNode> entries = TestSupport.findAll(ast, DictEntryNode.class);
        assertEquals("two dict entries", 2, entries.size());
        assertEquals("first key kept", "\"id\"", entries.get(0).getKey());
        assertEquals("second key kept", "\"name\"", entries.get(1).getKey());
        // Each entry must carry its value as a child.
        assertEquals("entry has its value child", 1, entries.get(0).getChildren().size());
    }

    public void testKeywordArgumentNamesArePreserved() {
        ASTNode ast = TestSupport.parseBackendAst("render_template(\"p.html\", products=products, total=total)\n\n");
        List<ArgumentNode> args = TestSupport.findAll(ast, ArgumentNode.class);
        assertEquals("three arguments", 3, args.size());
        assertFalse("first arg positional", args.get(0).isKeyword());
        assertTrue("second arg keyword", args.get(1).isKeyword());
        assertEquals("keyword name kept", "products", args.get(1).getName());
        assertEquals("third keyword name kept", "total", args.get(2).getName());
    }

    public void testIfStatementHasStructuredBranches() {
        String src = "if a:\n    x = 1\nelif b:\n    x = 2\nelse:\n    x = 3\n\n";
        ASTNode ast = TestSupport.parseBackendAst(src);

        List<IfStatementNode> ifs = TestSupport.findAll(ast, IfStatementNode.class);
        assertEquals("one if statement", 1, ifs.size());

        // Condition / then-block / elif / else must each be represented distinctly.
        // The if has a direct ConditionNode child (the elif's condition is nested
        // under the ElifBranch, so it is not a direct child of the if).
        assertEquals("if has one direct condition", 1, countDirect(ifs.get(0), ConditionNode.class));
        assertEquals("one elif branch", 1, TestSupport.findAll(ast, ElifBranchNode.class).size());
        assertEquals("one else branch", 1, TestSupport.findAll(ast, ElseBranchNode.class).size());
        // then + elif body + else body = 3 blocks
        assertEquals("three blocks (then/elif/else)", 3, TestSupport.findAll(ast, BlockNode.class).size());
    }

    public void testForStatementBodyIsBlock() {
        ASTNode ast = TestSupport.parseBackendAst("for p in products:\n    x = p\n\n");
        List<ForStatementNode> fors = TestSupport.findAll(ast, ForStatementNode.class);
        assertEquals("one for statement", 1, fors.size());
        assertEquals("iterator variable captured", "p", fors.get(0).getIteratorVariable());
    }

    private static int countDirect(ASTNode parent, Class<?> type) {
        int n = 0;
        for (ASTNode c : parent.getChildren()) {
            if (type.isInstance(c)) n++;
        }
        return n;
    }
}
