// Generated from src/antlr/backend/PythonFlaskParser.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PythonFlaskParser}.
 */
public interface PythonFlaskParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PythonFlaskParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(PythonFlaskParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonFlaskParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(PythonFlaskParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonFlaskParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(PythonFlaskParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonFlaskParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(PythonFlaskParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonFlaskParser#importStatement}.
	 * @param ctx the parse tree
	 */
	void enterImportStatement(PythonFlaskParser.ImportStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonFlaskParser#importStatement}.
	 * @param ctx the parse tree
	 */
	void exitImportStatement(PythonFlaskParser.ImportStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonFlaskParser#importList}.
	 * @param ctx the parse tree
	 */
	void enterImportList(PythonFlaskParser.ImportListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonFlaskParser#importList}.
	 * @param ctx the parse tree
	 */
	void exitImportList(PythonFlaskParser.ImportListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonFlaskParser#assignmentStatement}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentStatement(PythonFlaskParser.AssignmentStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonFlaskParser#assignmentStatement}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentStatement(PythonFlaskParser.AssignmentStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonFlaskParser#functionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDefinition(PythonFlaskParser.FunctionDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonFlaskParser#functionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDefinition(PythonFlaskParser.FunctionDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonFlaskParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void enterParameterList(PythonFlaskParser.ParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonFlaskParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void exitParameterList(PythonFlaskParser.ParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonFlaskParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(PythonFlaskParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonFlaskParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(PythonFlaskParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonFlaskParser#functionBody}.
	 * @param ctx the parse tree
	 */
	void enterFunctionBody(PythonFlaskParser.FunctionBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonFlaskParser#functionBody}.
	 * @param ctx the parse tree
	 */
	void exitFunctionBody(PythonFlaskParser.FunctionBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonFlaskParser#decoratedFunction}.
	 * @param ctx the parse tree
	 */
	void enterDecoratedFunction(PythonFlaskParser.DecoratedFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonFlaskParser#decoratedFunction}.
	 * @param ctx the parse tree
	 */
	void exitDecoratedFunction(PythonFlaskParser.DecoratedFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonFlaskParser#decorator}.
	 * @param ctx the parse tree
	 */
	void enterDecorator(PythonFlaskParser.DecoratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonFlaskParser#decorator}.
	 * @param ctx the parse tree
	 */
	void exitDecorator(PythonFlaskParser.DecoratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonFlaskParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(PythonFlaskParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonFlaskParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(PythonFlaskParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonFlaskParser#elifStatement}.
	 * @param ctx the parse tree
	 */
	void enterElifStatement(PythonFlaskParser.ElifStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonFlaskParser#elifStatement}.
	 * @param ctx the parse tree
	 */
	void exitElifStatement(PythonFlaskParser.ElifStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonFlaskParser#elseStatement}.
	 * @param ctx the parse tree
	 */
	void enterElseStatement(PythonFlaskParser.ElseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonFlaskParser#elseStatement}.
	 * @param ctx the parse tree
	 */
	void exitElseStatement(PythonFlaskParser.ElseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonFlaskParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void enterForStatement(PythonFlaskParser.ForStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonFlaskParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void exitForStatement(PythonFlaskParser.ForStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonFlaskParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(PythonFlaskParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonFlaskParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(PythonFlaskParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonFlaskParser#expressionStatement}.
	 * @param ctx the parse tree
	 */
	void enterExpressionStatement(PythonFlaskParser.ExpressionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonFlaskParser#expressionStatement}.
	 * @param ctx the parse tree
	 */
	void exitExpressionStatement(PythonFlaskParser.ExpressionStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AdditiveExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpression(PythonFlaskParser.AdditiveExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AdditiveExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpression(PythonFlaskParser.AdditiveExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ComparisonExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterComparisonExpression(PythonFlaskParser.ComparisonExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ComparisonExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitComparisonExpression(PythonFlaskParser.ComparisonExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MemberAccessExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMemberAccessExpression(PythonFlaskParser.MemberAccessExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MemberAccessExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMemberAccessExpression(PythonFlaskParser.MemberAccessExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(PythonFlaskParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(PythonFlaskParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PrimaryExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpression(PythonFlaskParser.PrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PrimaryExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpression(PythonFlaskParser.PrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotExpression(PythonFlaskParser.NotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotExpression(PythonFlaskParser.NotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IndexAccessExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIndexAccessExpression(PythonFlaskParser.IndexAccessExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IndexAccessExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIndexAccessExpression(PythonFlaskParser.IndexAccessExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EqualityExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpression(PythonFlaskParser.EqualityExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EqualityExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpression(PythonFlaskParser.EqualityExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterOrExpression(PythonFlaskParser.OrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitOrExpression(PythonFlaskParser.OrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MultiplicativeExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeExpression(PythonFlaskParser.MultiplicativeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MultiplicativeExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeExpression(PythonFlaskParser.MultiplicativeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FunctionCallExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCallExpression(PythonFlaskParser.FunctionCallExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FunctionCallExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCallExpression(PythonFlaskParser.FunctionCallExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IdentifierPrimary}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierPrimary(PythonFlaskParser.IdentifierPrimaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IdentifierPrimary}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierPrimary(PythonFlaskParser.IdentifierPrimaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(PythonFlaskParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(PythonFlaskParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntegerLiteral}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterIntegerLiteral(PythonFlaskParser.IntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntegerLiteral}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitIntegerLiteral(PythonFlaskParser.IntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FloatLiteral}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterFloatLiteral(PythonFlaskParser.FloatLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FloatLiteral}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitFloatLiteral(PythonFlaskParser.FloatLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BooleanLiteral}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLiteral(PythonFlaskParser.BooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BooleanLiteral}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLiteral(PythonFlaskParser.BooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NoneLiteral}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterNoneLiteral(PythonFlaskParser.NoneLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NoneLiteral}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitNoneLiteral(PythonFlaskParser.NoneLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ListPrimary}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterListPrimary(PythonFlaskParser.ListPrimaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ListPrimary}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitListPrimary(PythonFlaskParser.ListPrimaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DictPrimary}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterDictPrimary(PythonFlaskParser.DictPrimaryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DictPrimary}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitDictPrimary(PythonFlaskParser.DictPrimaryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenthesizedExpression}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterParenthesizedExpression(PythonFlaskParser.ParenthesizedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenthesizedExpression}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitParenthesizedExpression(PythonFlaskParser.ParenthesizedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonFlaskParser#listLiteral}.
	 * @param ctx the parse tree
	 */
	void enterListLiteral(PythonFlaskParser.ListLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonFlaskParser#listLiteral}.
	 * @param ctx the parse tree
	 */
	void exitListLiteral(PythonFlaskParser.ListLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonFlaskParser#dictLiteral}.
	 * @param ctx the parse tree
	 */
	void enterDictLiteral(PythonFlaskParser.DictLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonFlaskParser#dictLiteral}.
	 * @param ctx the parse tree
	 */
	void exitDictLiteral(PythonFlaskParser.DictLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonFlaskParser#keyValuePair}.
	 * @param ctx the parse tree
	 */
	void enterKeyValuePair(PythonFlaskParser.KeyValuePairContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonFlaskParser#keyValuePair}.
	 * @param ctx the parse tree
	 */
	void exitKeyValuePair(PythonFlaskParser.KeyValuePairContext ctx);
	/**
	 * Enter a parse tree produced by {@link PythonFlaskParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void enterArgumentList(PythonFlaskParser.ArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PythonFlaskParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void exitArgumentList(PythonFlaskParser.ArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code KeywordArgument}
	 * labeled alternative in {@link PythonFlaskParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterKeywordArgument(PythonFlaskParser.KeywordArgumentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code KeywordArgument}
	 * labeled alternative in {@link PythonFlaskParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitKeywordArgument(PythonFlaskParser.KeywordArgumentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PositionalArgument}
	 * labeled alternative in {@link PythonFlaskParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterPositionalArgument(PythonFlaskParser.PositionalArgumentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PositionalArgument}
	 * labeled alternative in {@link PythonFlaskParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitPositionalArgument(PythonFlaskParser.PositionalArgumentContext ctx);
}