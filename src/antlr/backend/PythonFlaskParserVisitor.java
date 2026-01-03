// Generated from src/antlr/backend/PythonFlaskParser.g4 by ANTLR 4.13.2
package antlr.backend;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PythonFlaskParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PythonFlaskParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PythonFlaskParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(PythonFlaskParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonFlaskParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(PythonFlaskParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonFlaskParser#importStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportStatement(PythonFlaskParser.ImportStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonFlaskParser#importList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportList(PythonFlaskParser.ImportListContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonFlaskParser#assignmentStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentStatement(PythonFlaskParser.AssignmentStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonFlaskParser#functionDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDefinition(PythonFlaskParser.FunctionDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonFlaskParser#parameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterList(PythonFlaskParser.ParameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonFlaskParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(PythonFlaskParser.ParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonFlaskParser#functionBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionBody(PythonFlaskParser.FunctionBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonFlaskParser#decoratedFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecoratedFunction(PythonFlaskParser.DecoratedFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonFlaskParser#decorator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecorator(PythonFlaskParser.DecoratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonFlaskParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(PythonFlaskParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonFlaskParser#elifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElifStatement(PythonFlaskParser.ElifStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonFlaskParser#elseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseStatement(PythonFlaskParser.ElseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonFlaskParser#forStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatement(PythonFlaskParser.ForStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonFlaskParser#returnStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStatement(PythonFlaskParser.ReturnStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonFlaskParser#expressionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionStatement(PythonFlaskParser.ExpressionStatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AdditiveExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpression(PythonFlaskParser.AdditiveExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ComparisonExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonExpression(PythonFlaskParser.ComparisonExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MemberAccessExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberAccessExpression(PythonFlaskParser.MemberAccessExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpression(PythonFlaskParser.AndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrimaryExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpression(PythonFlaskParser.PrimaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpression(PythonFlaskParser.NotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IndexAccessExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexAccessExpression(PythonFlaskParser.IndexAccessExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EqualityExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityExpression(PythonFlaskParser.EqualityExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpression(PythonFlaskParser.OrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MultiplicativeExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeExpression(PythonFlaskParser.MultiplicativeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FunctionCallExpression}
	 * labeled alternative in {@link PythonFlaskParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCallExpression(PythonFlaskParser.FunctionCallExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IdentifierPrimary}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierPrimary(PythonFlaskParser.IdentifierPrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(PythonFlaskParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntegerLiteral}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerLiteral(PythonFlaskParser.IntegerLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FloatLiteral}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatLiteral(PythonFlaskParser.FloatLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BooleanLiteral}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanLiteral(PythonFlaskParser.BooleanLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NoneLiteral}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoneLiteral(PythonFlaskParser.NoneLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ListPrimary}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListPrimary(PythonFlaskParser.ListPrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DictPrimary}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictPrimary(PythonFlaskParser.DictPrimaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenthesizedExpression}
	 * labeled alternative in {@link PythonFlaskParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesizedExpression(PythonFlaskParser.ParenthesizedExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonFlaskParser#listLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListLiteral(PythonFlaskParser.ListLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonFlaskParser#dictLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDictLiteral(PythonFlaskParser.DictLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonFlaskParser#keyValuePair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyValuePair(PythonFlaskParser.KeyValuePairContext ctx);
	/**
	 * Visit a parse tree produced by {@link PythonFlaskParser#argumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentList(PythonFlaskParser.ArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code KeywordArgument}
	 * labeled alternative in {@link PythonFlaskParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeywordArgument(PythonFlaskParser.KeywordArgumentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PositionalArgument}
	 * labeled alternative in {@link PythonFlaskParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPositionalArgument(PythonFlaskParser.PositionalArgumentContext ctx);
}