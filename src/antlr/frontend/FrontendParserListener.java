// Generated from src/antlr/frontend/FrontendParser.g4 by ANTLR 4.13.2
package antlr.frontend;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link FrontendParser}.
 */
public interface FrontendParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link FrontendParser#document}.
	 * @param ctx the parse tree
	 */
	void enterDocument(FrontendParser.DocumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#document}.
	 * @param ctx the parse tree
	 */
	void exitDocument(FrontendParser.DocumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#content}.
	 * @param ctx the parse tree
	 */
	void enterContent(FrontendParser.ContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#content}.
	 * @param ctx the parse tree
	 */
	void exitContent(FrontendParser.ContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#contentItem}.
	 * @param ctx the parse tree
	 */
	void enterContentItem(FrontendParser.ContentItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#contentItem}.
	 * @param ctx the parse tree
	 */
	void exitContentItem(FrontendParser.ContentItemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RegularElement}
	 * labeled alternative in {@link FrontendParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void enterRegularElement(FrontendParser.RegularElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RegularElement}
	 * labeled alternative in {@link FrontendParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void exitRegularElement(FrontendParser.RegularElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SelfClosingElement}
	 * labeled alternative in {@link FrontendParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void enterSelfClosingElement(FrontendParser.SelfClosingElementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SelfClosingElement}
	 * labeled alternative in {@link FrontendParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void exitSelfClosingElement(FrontendParser.SelfClosingElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Style}
	 * labeled alternative in {@link FrontendParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void enterStyle(FrontendParser.StyleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Style}
	 * labeled alternative in {@link FrontendParser#htmlElement}.
	 * @param ctx the parse tree
	 */
	void exitStyle(FrontendParser.StyleContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#tagName}.
	 * @param ctx the parse tree
	 */
	void enterTagName(FrontendParser.TagNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#tagName}.
	 * @param ctx the parse tree
	 */
	void exitTagName(FrontendParser.TagNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#htmlAttribute}.
	 * @param ctx the parse tree
	 */
	void enterHtmlAttribute(FrontendParser.HtmlAttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#htmlAttribute}.
	 * @param ctx the parse tree
	 */
	void exitHtmlAttribute(FrontendParser.HtmlAttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#attrName}.
	 * @param ctx the parse tree
	 */
	void enterAttrName(FrontendParser.AttrNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#attrName}.
	 * @param ctx the parse tree
	 */
	void exitAttrName(FrontendParser.AttrNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#attrValue}.
	 * @param ctx the parse tree
	 */
	void enterAttrValue(FrontendParser.AttrValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#attrValue}.
	 * @param ctx the parse tree
	 */
	void exitAttrValue(FrontendParser.AttrValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#styleElement}.
	 * @param ctx the parse tree
	 */
	void enterStyleElement(FrontendParser.StyleElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#styleElement}.
	 * @param ctx the parse tree
	 */
	void exitStyleElement(FrontendParser.StyleElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#cssRule}.
	 * @param ctx the parse tree
	 */
	void enterCssRule(FrontendParser.CssRuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#cssRule}.
	 * @param ctx the parse tree
	 */
	void exitCssRule(FrontendParser.CssRuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#cssSelector}.
	 * @param ctx the parse tree
	 */
	void enterCssSelector(FrontendParser.CssSelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#cssSelector}.
	 * @param ctx the parse tree
	 */
	void exitCssSelector(FrontendParser.CssSelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#cssSelectorPart}.
	 * @param ctx the parse tree
	 */
	void enterCssSelectorPart(FrontendParser.CssSelectorPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#cssSelectorPart}.
	 * @param ctx the parse tree
	 */
	void exitCssSelectorPart(FrontendParser.CssSelectorPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#cssSelectorItem}.
	 * @param ctx the parse tree
	 */
	void enterCssSelectorItem(FrontendParser.CssSelectorItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#cssSelectorItem}.
	 * @param ctx the parse tree
	 */
	void exitCssSelectorItem(FrontendParser.CssSelectorItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#cssDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterCssDeclaration(FrontendParser.CssDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#cssDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitCssDeclaration(FrontendParser.CssDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#cssPropertyName}.
	 * @param ctx the parse tree
	 */
	void enterCssPropertyName(FrontendParser.CssPropertyNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#cssPropertyName}.
	 * @param ctx the parse tree
	 */
	void exitCssPropertyName(FrontendParser.CssPropertyNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#cssValue}.
	 * @param ctx the parse tree
	 */
	void enterCssValue(FrontendParser.CssValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#cssValue}.
	 * @param ctx the parse tree
	 */
	void exitCssValue(FrontendParser.CssValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#jinjaExpression}.
	 * @param ctx the parse tree
	 */
	void enterJinjaExpression(FrontendParser.JinjaExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#jinjaExpression}.
	 * @param ctx the parse tree
	 */
	void exitJinjaExpression(FrontendParser.JinjaExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaNumberLiteral}
	 * labeled alternative in {@link FrontendParser#jinjaExpr}.
	 * @param ctx the parse tree
	 */
	void enterJinjaNumberLiteral(FrontendParser.JinjaNumberLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaNumberLiteral}
	 * labeled alternative in {@link FrontendParser#jinjaExpr}.
	 * @param ctx the parse tree
	 */
	void exitJinjaNumberLiteral(FrontendParser.JinjaNumberLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaIndexAccess}
	 * labeled alternative in {@link FrontendParser#jinjaExpr}.
	 * @param ctx the parse tree
	 */
	void enterJinjaIndexAccess(FrontendParser.JinjaIndexAccessContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaIndexAccess}
	 * labeled alternative in {@link FrontendParser#jinjaExpr}.
	 * @param ctx the parse tree
	 */
	void exitJinjaIndexAccess(FrontendParser.JinjaIndexAccessContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaStringLiteral}
	 * labeled alternative in {@link FrontendParser#jinjaExpr}.
	 * @param ctx the parse tree
	 */
	void enterJinjaStringLiteral(FrontendParser.JinjaStringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaStringLiteral}
	 * labeled alternative in {@link FrontendParser#jinjaExpr}.
	 * @param ctx the parse tree
	 */
	void exitJinjaStringLiteral(FrontendParser.JinjaStringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaIdentifier}
	 * labeled alternative in {@link FrontendParser#jinjaExpr}.
	 * @param ctx the parse tree
	 */
	void enterJinjaIdentifier(FrontendParser.JinjaIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaIdentifier}
	 * labeled alternative in {@link FrontendParser#jinjaExpr}.
	 * @param ctx the parse tree
	 */
	void exitJinjaIdentifier(FrontendParser.JinjaIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaMemberAccess}
	 * labeled alternative in {@link FrontendParser#jinjaExpr}.
	 * @param ctx the parse tree
	 */
	void enterJinjaMemberAccess(FrontendParser.JinjaMemberAccessContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaMemberAccess}
	 * labeled alternative in {@link FrontendParser#jinjaExpr}.
	 * @param ctx the parse tree
	 */
	void exitJinjaMemberAccess(FrontendParser.JinjaMemberAccessContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#jinjaStatement}.
	 * @param ctx the parse tree
	 */
	void enterJinjaStatement(FrontendParser.JinjaStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#jinjaStatement}.
	 * @param ctx the parse tree
	 */
	void exitJinjaStatement(FrontendParser.JinjaStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#jinjaIfStatement}.
	 * @param ctx the parse tree
	 */
	void enterJinjaIfStatement(FrontendParser.JinjaIfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#jinjaIfStatement}.
	 * @param ctx the parse tree
	 */
	void exitJinjaIfStatement(FrontendParser.JinjaIfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#jinjaElifStatement}.
	 * @param ctx the parse tree
	 */
	void enterJinjaElifStatement(FrontendParser.JinjaElifStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#jinjaElifStatement}.
	 * @param ctx the parse tree
	 */
	void exitJinjaElifStatement(FrontendParser.JinjaElifStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#jinjaElseStatement}.
	 * @param ctx the parse tree
	 */
	void enterJinjaElseStatement(FrontendParser.JinjaElseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#jinjaElseStatement}.
	 * @param ctx the parse tree
	 */
	void exitJinjaElseStatement(FrontendParser.JinjaElseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#jinjaForStatement}.
	 * @param ctx the parse tree
	 */
	void enterJinjaForStatement(FrontendParser.JinjaForStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#jinjaForStatement}.
	 * @param ctx the parse tree
	 */
	void exitJinjaForStatement(FrontendParser.JinjaForStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#jinjaExtendsStatement}.
	 * @param ctx the parse tree
	 */
	void enterJinjaExtendsStatement(FrontendParser.JinjaExtendsStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#jinjaExtendsStatement}.
	 * @param ctx the parse tree
	 */
	void exitJinjaExtendsStatement(FrontendParser.JinjaExtendsStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#jinjaBlockStatement}.
	 * @param ctx the parse tree
	 */
	void enterJinjaBlockStatement(FrontendParser.JinjaBlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#jinjaBlockStatement}.
	 * @param ctx the parse tree
	 */
	void exitJinjaBlockStatement(FrontendParser.JinjaBlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#jinjaCondition}.
	 * @param ctx the parse tree
	 */
	void enterJinjaCondition(FrontendParser.JinjaConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#jinjaCondition}.
	 * @param ctx the parse tree
	 */
	void exitJinjaCondition(FrontendParser.JinjaConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaStmtStringLiteral}
	 * labeled alternative in {@link FrontendParser#jinjaStmtExpr}.
	 * @param ctx the parse tree
	 */
	void enterJinjaStmtStringLiteral(FrontendParser.JinjaStmtStringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaStmtStringLiteral}
	 * labeled alternative in {@link FrontendParser#jinjaStmtExpr}.
	 * @param ctx the parse tree
	 */
	void exitJinjaStmtStringLiteral(FrontendParser.JinjaStmtStringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaStmtIdentifier}
	 * labeled alternative in {@link FrontendParser#jinjaStmtExpr}.
	 * @param ctx the parse tree
	 */
	void enterJinjaStmtIdentifier(FrontendParser.JinjaStmtIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaStmtIdentifier}
	 * labeled alternative in {@link FrontendParser#jinjaStmtExpr}.
	 * @param ctx the parse tree
	 */
	void exitJinjaStmtIdentifier(FrontendParser.JinjaStmtIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaStmtIndexAccess}
	 * labeled alternative in {@link FrontendParser#jinjaStmtExpr}.
	 * @param ctx the parse tree
	 */
	void enterJinjaStmtIndexAccess(FrontendParser.JinjaStmtIndexAccessContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaStmtIndexAccess}
	 * labeled alternative in {@link FrontendParser#jinjaStmtExpr}.
	 * @param ctx the parse tree
	 */
	void exitJinjaStmtIndexAccess(FrontendParser.JinjaStmtIndexAccessContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaStmtNumberLiteral}
	 * labeled alternative in {@link FrontendParser#jinjaStmtExpr}.
	 * @param ctx the parse tree
	 */
	void enterJinjaStmtNumberLiteral(FrontendParser.JinjaStmtNumberLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaStmtNumberLiteral}
	 * labeled alternative in {@link FrontendParser#jinjaStmtExpr}.
	 * @param ctx the parse tree
	 */
	void exitJinjaStmtNumberLiteral(FrontendParser.JinjaStmtNumberLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JinjaStmtMemberAccess}
	 * labeled alternative in {@link FrontendParser#jinjaStmtExpr}.
	 * @param ctx the parse tree
	 */
	void enterJinjaStmtMemberAccess(FrontendParser.JinjaStmtMemberAccessContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JinjaStmtMemberAccess}
	 * labeled alternative in {@link FrontendParser#jinjaStmtExpr}.
	 * @param ctx the parse tree
	 */
	void exitJinjaStmtMemberAccess(FrontendParser.JinjaStmtMemberAccessContext ctx);
	/**
	 * Enter a parse tree produced by {@link FrontendParser#text}.
	 * @param ctx the parse tree
	 */
	void enterText(FrontendParser.TextContext ctx);
	/**
	 * Exit a parse tree produced by {@link FrontendParser#text}.
	 * @param ctx the parse tree
	 */
	void exitText(FrontendParser.TextContext ctx);
}