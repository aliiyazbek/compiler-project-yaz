// Generated from src/antlr/frontend/FrontendParser.g4 by ANTLR 4.13.2
package antlr.frontend;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link FrontendParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface FrontendParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link FrontendParser#document}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDocument(FrontendParser.DocumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#content}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContent(FrontendParser.ContentContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#contentItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContentItem(FrontendParser.ContentItemContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RegularElement}
	 * labeled alternative in {@link FrontendParser#htmlElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegularElement(FrontendParser.RegularElementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SelfClosingElement}
	 * labeled alternative in {@link FrontendParser#htmlElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelfClosingElement(FrontendParser.SelfClosingElementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Style}
	 * labeled alternative in {@link FrontendParser#htmlElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStyle(FrontendParser.StyleContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#tagName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTagName(FrontendParser.TagNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#htmlAttribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHtmlAttribute(FrontendParser.HtmlAttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#attrName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttrName(FrontendParser.AttrNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#attrValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttrValue(FrontendParser.AttrValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#styleElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStyleElement(FrontendParser.StyleElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#cssRule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCssRule(FrontendParser.CssRuleContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#cssSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCssSelector(FrontendParser.CssSelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#cssSelectorPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCssSelectorPart(FrontendParser.CssSelectorPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#cssSelectorItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCssSelectorItem(FrontendParser.CssSelectorItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#cssDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCssDeclaration(FrontendParser.CssDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#cssPropertyName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCssPropertyName(FrontendParser.CssPropertyNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#cssValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCssValue(FrontendParser.CssValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#jinjaExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaExpression(FrontendParser.JinjaExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaNumberLiteral}
	 * labeled alternative in {@link FrontendParser#jinjaExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaNumberLiteral(FrontendParser.JinjaNumberLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaIndexAccess}
	 * labeled alternative in {@link FrontendParser#jinjaExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaIndexAccess(FrontendParser.JinjaIndexAccessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaStringLiteral}
	 * labeled alternative in {@link FrontendParser#jinjaExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaStringLiteral(FrontendParser.JinjaStringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaIdentifier}
	 * labeled alternative in {@link FrontendParser#jinjaExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaIdentifier(FrontendParser.JinjaIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaMemberAccess}
	 * labeled alternative in {@link FrontendParser#jinjaExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaMemberAccess(FrontendParser.JinjaMemberAccessContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#jinjaStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaStatement(FrontendParser.JinjaStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#jinjaIfStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaIfStatement(FrontendParser.JinjaIfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#jinjaElifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaElifStatement(FrontendParser.JinjaElifStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#jinjaElseStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaElseStatement(FrontendParser.JinjaElseStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#jinjaForStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaForStatement(FrontendParser.JinjaForStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#jinjaExtendsStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaExtendsStatement(FrontendParser.JinjaExtendsStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#jinjaBlockStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaBlockStatement(FrontendParser.JinjaBlockStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#jinjaCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaCondition(FrontendParser.JinjaConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaStmtStringLiteral}
	 * labeled alternative in {@link FrontendParser#jinjaStmtExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaStmtStringLiteral(FrontendParser.JinjaStmtStringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaStmtIdentifier}
	 * labeled alternative in {@link FrontendParser#jinjaStmtExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaStmtIdentifier(FrontendParser.JinjaStmtIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaStmtIndexAccess}
	 * labeled alternative in {@link FrontendParser#jinjaStmtExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaStmtIndexAccess(FrontendParser.JinjaStmtIndexAccessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaStmtNumberLiteral}
	 * labeled alternative in {@link FrontendParser#jinjaStmtExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaStmtNumberLiteral(FrontendParser.JinjaStmtNumberLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JinjaStmtMemberAccess}
	 * labeled alternative in {@link FrontendParser#jinjaStmtExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJinjaStmtMemberAccess(FrontendParser.JinjaStmtMemberAccessContext ctx);
	/**
	 * Visit a parse tree produced by {@link FrontendParser#text}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitText(FrontendParser.TextContext ctx);
}