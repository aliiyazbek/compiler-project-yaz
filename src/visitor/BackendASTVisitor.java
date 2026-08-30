package visitor;

import antlr.backend.*;
import ast.backend.*;
import ast.base.ASTNode;
import org.antlr.v4.runtime.tree.ParseTree;
import symboltable.Symbol;
import symboltable.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class BackendASTVisitor extends PythonFlaskParserBaseVisitor<ASTNode> {

    private SymbolTable symbolTable = new SymbolTable();

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    @Override
    public ASTNode visitProgram(PythonFlaskParser.ProgramContext ctx) {
        int line = ctx.start != null ? ctx.start.getLine() : 1;
        ProgramNode programNode = new ProgramNode(line);

        for (PythonFlaskParser.StatementContext stmtCtx : ctx.statement()) {
            ASTNode stmtNode = visit(stmtCtx);
            if (stmtNode != null) {
                programNode.addChild(stmtNode);
            }
        }

        return programNode;
    }

    @Override
    public ASTNode visitImportStatement(PythonFlaskParser.ImportStatementContext ctx) {
        int line = ctx.start.getLine();
        String moduleName = ctx.IDENTIFIER().getText();

        List<String> importedItems = new ArrayList<>();
        PythonFlaskParser.ImportListContext importListCtx = ctx.importList();
        for (int i = 0; i < importListCtx.IDENTIFIER().size(); i++) {
            String importedItem = importListCtx.IDENTIFIER(i).getText();
            importedItems.add(importedItem);

            symbolTable.addSymbol(importedItem, "import", "module", line);
        }

        return new ImportNode(line, moduleName, importedItems);
    }

    @Override
    public ASTNode visitAssignmentStatement(PythonFlaskParser.AssignmentStatementContext ctx) {
        int line = ctx.start.getLine();
        String varName = ctx.IDENTIFIER().getText();

        AssignmentNode assignNode = new AssignmentNode(line, varName);

        ASTNode exprNode = visit(ctx.expression());
        if (exprNode != null) {
            assignNode.addChild(exprNode);
        }

        // Infer the variable's data type from its initializer expression.
        symbolTable.addSymbol(varName, "variable", inferType(exprNode), line);

        return assignNode;
    }

    @Override
    public ASTNode visitFunctionDefinition(PythonFlaskParser.FunctionDefinitionContext ctx) {
        int line = ctx.start.getLine();
        String funcName = ctx.IDENTIFIER().getText();

        symbolTable.addSymbol(funcName, "function", "function", line);

        symbolTable.enterScope(funcName);

        List<String> parameters = new ArrayList<>();
        if (ctx.parameterList() != null) {
            for (PythonFlaskParser.ParameterContext paramCtx : ctx.parameterList().parameter()) {
                String paramName = paramCtx.IDENTIFIER().getText();
                parameters.add(paramName);

                symbolTable.addSymbol(paramName, "parameter", "unknown", line);
            }
        }

        FunctionDefNode funcNode = new FunctionDefNode(line, funcName, parameters);

        if (ctx.functionBody() != null) {
            for (PythonFlaskParser.StatementContext stmtCtx : ctx.functionBody().statement()) {
                ASTNode stmtNode = visit(stmtCtx);
                if (stmtNode != null) {
                    funcNode.addChild(stmtNode);
                }
            }
        }

        symbolTable.exitScope();

        return funcNode;
    }

    @Override
    public ASTNode visitDecoratedFunction(PythonFlaskParser.DecoratedFunctionContext ctx) {
        int line = ctx.start.getLine();

        ASTNode funcNode = visit(ctx.functionDefinition());

        DecoratedFunctionNode wrapper = new DecoratedFunctionNode(line);

        for (PythonFlaskParser.DecoratorContext decCtx : ctx.decorator()) {
            String decoratorText = decCtx.getText().substring(1);
            DecoratorNode decNode = new DecoratorNode(decCtx.start.getLine(), decoratorText);
            wrapper.addChild(decNode);
        }

        if (funcNode != null) {
            wrapper.addChild(funcNode);
        }

        return wrapper;
    }

    @Override
    public ASTNode visitIfStatement(PythonFlaskParser.IfStatementContext ctx) {
        int line = ctx.start.getLine();
        IfStatementNode ifNode = new IfStatementNode(line);

        // Condition wrapped so it is distinct from the body.
        ConditionNode condNode = new ConditionNode(line);
        ASTNode condExpr = visit(ctx.expression());
        if (condExpr != null) {
            condNode.addChild(condExpr);
        }
        ifNode.addChild(condNode);

        // "then" body.
        ifNode.addChild(buildBlock(line, "then", ctx.statement()));

        // elif branches, each with its own condition + body.
        for (PythonFlaskParser.ElifStatementContext elifCtx : ctx.elifStatement()) {
            ASTNode elifNode = visit(elifCtx);
            if (elifNode != null) {
                ifNode.addChild(elifNode);
            }
        }

        // optional else branch.
        if (ctx.elseStatement() != null) {
            ASTNode elseNode = visit(ctx.elseStatement());
            if (elseNode != null) {
                ifNode.addChild(elseNode);
            }
        }

        return ifNode;
    }

    @Override
    public ASTNode visitElifStatement(PythonFlaskParser.ElifStatementContext ctx) {
        int line = ctx.start.getLine();
        ElifBranchNode elifNode = new ElifBranchNode(line);

        ConditionNode condNode = new ConditionNode(line);
        ASTNode condExpr = visit(ctx.expression());
        if (condExpr != null) {
            condNode.addChild(condExpr);
        }
        elifNode.addChild(condNode);
        elifNode.addChild(buildBlock(line, "body", ctx.statement()));

        return elifNode;
    }

    @Override
    public ASTNode visitElseStatement(PythonFlaskParser.ElseStatementContext ctx) {
        int line = ctx.start.getLine();
        ElseBranchNode elseNode = new ElseBranchNode(line);
        elseNode.addChild(buildBlock(line, "else", ctx.statement()));
        return elseNode;
    }

    /** Visit a list of statements and wrap the results in a labelled BlockNode. */
    private BlockNode buildBlock(int line, String label,
                                 List<PythonFlaskParser.StatementContext> statements) {
        BlockNode block = new BlockNode(line, label);
        for (PythonFlaskParser.StatementContext stmtCtx : statements) {
            ASTNode stmtNode = visit(stmtCtx);
            if (stmtNode != null) {
                block.addChild(stmtNode);
            }
        }
        return block;
    }

    @Override
    public ASTNode visitForStatement(PythonFlaskParser.ForStatementContext ctx) {
        int line = ctx.start.getLine();
        String iterVar = ctx.IDENTIFIER().getText();

        symbolTable.addSymbol(iterVar, "variable", "iterator", line);

        ForStatementNode forNode = new ForStatementNode(line, iterVar);

        ASTNode iterableExpr = visit(ctx.expression());
        if (iterableExpr != null) {
            forNode.addChild(iterableExpr);
        }

        forNode.addChild(buildBlock(line, "body", ctx.statement()));

        return forNode;
    }

    @Override
    public ASTNode visitReturnStatement(PythonFlaskParser.ReturnStatementContext ctx) {
        int line = ctx.start.getLine();
        ReturnStatementNode returnNode = new ReturnStatementNode(line);

        if (ctx.expression() != null) {
            ASTNode exprNode = visit(ctx.expression());
            if (exprNode != null) {
                returnNode.addChild(exprNode);
            }
        }

        return returnNode;
    }

    @Override
    public ASTNode visitExpressionStatement(PythonFlaskParser.ExpressionStatementContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public ASTNode visitIdentifierPrimary(PythonFlaskParser.IdentifierPrimaryContext ctx) {
        int line = ctx.start.getLine();
        String name = ctx.IDENTIFIER().getText();
        return new IdentifierNode(line, name);
    }

    @Override
    public ASTNode visitStringLiteral(PythonFlaskParser.StringLiteralContext ctx) {
        int line = ctx.start.getLine();
        String value = ctx.STRING().getText();
        return new LiteralNode(line, value, "string");
    }

    @Override
    public ASTNode visitIntegerLiteral(PythonFlaskParser.IntegerLiteralContext ctx) {
        int line = ctx.start.getLine();
        String value = ctx.INTEGER().getText();
        return new LiteralNode(line, value, "integer");
    }

    @Override
    public ASTNode visitFloatLiteral(PythonFlaskParser.FloatLiteralContext ctx) {
        int line = ctx.start.getLine();
        String value = ctx.FLOAT().getText();
        return new LiteralNode(line, value, "float");
    }

    @Override
    public ASTNode visitBooleanLiteral(PythonFlaskParser.BooleanLiteralContext ctx) {
        int line = ctx.start.getLine();
        String value = ctx.getText();
        return new LiteralNode(line, value, "boolean");
    }

    @Override
    public ASTNode visitNoneLiteral(PythonFlaskParser.NoneLiteralContext ctx) {
        int line = ctx.start.getLine();
        return new LiteralNode(line, "None", "none");
    }

    @Override
    public ASTNode visitListPrimary(PythonFlaskParser.ListPrimaryContext ctx) {
        int line = ctx.start.getLine();
        ListLiteralNode listNode = new ListLiteralNode(line);

        ASTNode listLiteral = visit(ctx.listLiteral());
        if (listLiteral != null && listLiteral.getChildren() != null) {
            listNode.addChildren(listLiteral.getChildren());
        }

        return listNode;
    }

    @Override
    public ASTNode visitListLiteral(PythonFlaskParser.ListLiteralContext ctx) {
        int line = ctx.start.getLine();
        ListLiteralNode listNode = new ListLiteralNode(line);

        for (PythonFlaskParser.ExpressionContext exprCtx : ctx.expression()) {
            ASTNode exprNode = visit(exprCtx);
            if (exprNode != null) {
                listNode.addChild(exprNode);
            }
        }

        return listNode;
    }

    @Override
    public ASTNode visitDictPrimary(PythonFlaskParser.DictPrimaryContext ctx) {
        int line = ctx.start.getLine();
        return visit(ctx.dictLiteral());
    }

    @Override
    public ASTNode visitDictLiteral(PythonFlaskParser.DictLiteralContext ctx) {
        int line = ctx.start.getLine();
        DictLiteralNode dictNode = new DictLiteralNode(line);

        for (PythonFlaskParser.KeyValuePairContext kvCtx : ctx.keyValuePair()) {
            // The key is either a STRING or an IDENTIFIER token; keep it instead
            // of dropping it. getChild(0) is the key, then COLON, then expression.
            String key = kvCtx.getChild(0).getText();
            DictEntryNode entry = new DictEntryNode(kvCtx.start.getLine(), key);

            ASTNode valueNode = visit(kvCtx.expression());
            if (valueNode != null) {
                entry.addChild(valueNode);
            }
            dictNode.addChild(entry);
        }

        return dictNode;
    }

    @Override
    public ASTNode visitFunctionCallExpression(PythonFlaskParser.FunctionCallExpressionContext ctx) {
        int line = ctx.start.getLine();

        String funcName = ctx.expression().getText();
        FunctionCallNode callNode = new FunctionCallNode(line, funcName);

        if (ctx.argumentList() != null) {
            for (PythonFlaskParser.ArgumentContext argCtx : ctx.argumentList().argument()) {
                ASTNode argNode = visit(argCtx);
                if (argNode != null) {
                    callNode.addChild(argNode);
                }
            }
        }

        return callNode;
    }

    @Override
    public ASTNode visitPositionalArgument(PythonFlaskParser.PositionalArgumentContext ctx) {
        ArgumentNode argNode = new ArgumentNode(ctx.start.getLine(), null);
        ASTNode valueNode = visit(ctx.expression());
        if (valueNode != null) {
            argNode.addChild(valueNode);
        }
        return argNode;
    }

    @Override
    public ASTNode visitKeywordArgument(PythonFlaskParser.KeywordArgumentContext ctx) {
        // Preserve the keyword name (e.g. products=...) instead of discarding it.
        String name = ctx.IDENTIFIER().getText();
        ArgumentNode argNode = new ArgumentNode(ctx.start.getLine(), name);
        ASTNode valueNode = visit(ctx.expression());
        if (valueNode != null) {
            argNode.addChild(valueNode);
        }
        return argNode;
    }

    @Override
    public ASTNode visitAdditiveExpression(PythonFlaskParser.AdditiveExpressionContext ctx) {
        return visitBinaryOp(ctx, ctx.op.getText(), ctx.expression(0), ctx.expression(1));
    }

    @Override
    public ASTNode visitMultiplicativeExpression(PythonFlaskParser.MultiplicativeExpressionContext ctx) {
        return visitBinaryOp(ctx, ctx.op.getText(), ctx.expression(0), ctx.expression(1));
    }

    @Override
    public ASTNode visitComparisonExpression(PythonFlaskParser.ComparisonExpressionContext ctx) {
        return visitBinaryOp(ctx, ctx.op.getText(), ctx.expression(0), ctx.expression(1));
    }

    @Override
    public ASTNode visitEqualityExpression(PythonFlaskParser.EqualityExpressionContext ctx) {
        return visitBinaryOp(ctx, ctx.op.getText(), ctx.expression(0), ctx.expression(1));
    }

    @Override
    public ASTNode visitAndExpression(PythonFlaskParser.AndExpressionContext ctx) {
        return visitBinaryOp(ctx, "and", ctx.expression(0), ctx.expression(1));
    }

    @Override
    public ASTNode visitOrExpression(PythonFlaskParser.OrExpressionContext ctx) {
        return visitBinaryOp(ctx, "or", ctx.expression(0), ctx.expression(1));
    }

    private ASTNode visitBinaryOp(ParseTree ctx, String operator, PythonFlaskParser.ExpressionContext left, PythonFlaskParser.ExpressionContext right) {
        int line = ((PythonFlaskParser.ExpressionContext) ctx).start.getLine();
        BinaryOpNode opNode = new BinaryOpNode(line, operator);

        ASTNode leftNode = visit(left);
        ASTNode rightNode = visit(right);

        if (leftNode != null) {
            opNode.addChild(leftNode);
        }
        if (rightNode != null) {
            opNode.addChild(rightNode);
        }

        return opNode;
    }

    @Override
    public ASTNode visitMemberAccessExpression(PythonFlaskParser.MemberAccessExpressionContext ctx) {
        int line = ctx.start.getLine();
        String memberName = ctx.IDENTIFIER().getText();
        BinaryOpNode accessNode = new BinaryOpNode(line, ".");

        ASTNode objNode = visit(ctx.expression());
        IdentifierNode memberNode = new IdentifierNode(line, memberName);

        if (objNode != null) {
            accessNode.addChild(objNode);
        }
        accessNode.addChild(memberNode);

        return accessNode;
    }

    @Override
    public ASTNode visitIndexAccessExpression(PythonFlaskParser.IndexAccessExpressionContext ctx) {
        int line = ctx.start.getLine();
        BinaryOpNode indexNode = new BinaryOpNode(line, "[]");

        ASTNode objNode = visit(ctx.expression(0));
        ASTNode indexExpr = visit(ctx.expression(1));

        if (objNode != null) {
            indexNode.addChild(objNode);
        }
        if (indexExpr != null) {
            indexNode.addChild(indexExpr);
        }

        return indexNode;
    }

    @Override
    public ASTNode visitParenthesizedExpression(PythonFlaskParser.ParenthesizedExpressionContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public ASTNode visitPrimaryExpression(PythonFlaskParser.PrimaryExpressionContext ctx) {
        return visit(ctx.primary());
    }

    /**
     * Infer a data type for an expression's resulting AST node. This is a light,
     * syntax-directed inference (not a full type system): it covers literals,
     * list/dict literals, common builtins, and resolves identifiers against the
     * symbol table so a variable copied from another takes on its type.
     */
    private String inferType(ASTNode node) {
        if (node == null) {
            return "unknown";
        }

        if (node instanceof LiteralNode) {
            return ((LiteralNode) node).getType(); // string, integer, float, boolean, none
        }
        if (node instanceof ListLiteralNode) {
            return "list";
        }
        if (node instanceof DictLiteralNode) {
            return "dict";
        }
        if (node instanceof FunctionCallNode) {
            return inferCallType(((FunctionCallNode) node).getFunctionName());
        }
        if (node instanceof IdentifierNode) {
            Symbol resolved = symbolTable.resolve(((IdentifierNode) node).getName());
            return resolved != null ? resolved.getDataType() : "unknown";
        }
        if (node instanceof BinaryOpNode) {
            return inferBinaryType((BinaryOpNode) node);
        }
        return "unknown";
    }

    /** Infer the return type of a few well-known builtins, else unknown. */
    private String inferCallType(String funcName) {
        switch (funcName) {
            case "len":
                return "integer";
            case "str":
                return "string";
            case "int":
                return "integer";
            case "float":
                return "float";
            case "bool":
                return "boolean";
            case "list":
                return "list";
            case "dict":
                return "dict";
            default:
                return "unknown";
        }
    }

    /** Comparison/logical operators yield booleans; otherwise fall back. */
    private String inferBinaryType(BinaryOpNode op) {
        switch (op.getOperator()) {
            case "==": case "!=": case "<": case ">": case "<=": case ">=":
            case "and": case "or":
                return "boolean";
            default:
                return "unknown";
        }
    }
}
