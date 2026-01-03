package visitor;

import antlr.frontend.*;
import ast.base.ASTNode;
import ast.frontend.*;

public class FrontendASTVisitor extends FrontendParserBaseVisitor<ASTNode> {

    @Override
    public ASTNode visitDocument(FrontendParser.DocumentContext ctx) {
        int line = ctx.start != null ? ctx.start.getLine() : 1;
        DocumentNode docNode = new DocumentNode(line);

        if (ctx.jinjaExtendsStatement() != null) {
            ASTNode extendsNode = visit(ctx.jinjaExtendsStatement());
            if (extendsNode != null) {
                docNode.addChild(extendsNode);
            }
        }

        if (ctx.content() != null) {
            ASTNode contentNode = visit(ctx.content());
            if (contentNode != null && contentNode.getChildren() != null) {
                docNode.addChildren(contentNode.getChildren());
            }
        }

        return docNode;
    }

    @Override
    public ASTNode visitContent(FrontendParser.ContentContext ctx) {
        int line = ctx.start != null ? ctx.start.getLine() : 1;
        DocumentNode containerNode = new DocumentNode(line);

        for (FrontendParser.ContentItemContext itemCtx : ctx.contentItem()) {
            ASTNode itemNode = visit(itemCtx);
            if (itemNode != null) {
                containerNode.addChild(itemNode);
            }
        }

        return containerNode;
    }

    @Override
    public ASTNode visitContentItem(FrontendParser.ContentItemContext ctx) {
        if (ctx.htmlElement() != null) {
            return visit(ctx.htmlElement());
        } else if (ctx.jinjaExpression() != null) {
            return visit(ctx.jinjaExpression());
        } else if (ctx.jinjaStatement() != null) {
            return visit(ctx.jinjaStatement());
        } else if (ctx.text() != null) {
            return visit(ctx.text());
        }
        return null;
    }

    @Override
    public ASTNode visitRegularElement(FrontendParser.RegularElementContext ctx) {
        int line = ctx.start.getLine();
        String tagName = ctx.tagName(0).getText();
        HtmlElementNode elementNode = new HtmlElementNode(line, tagName, false);

        for (FrontendParser.HtmlAttributeContext attrCtx : ctx.htmlAttribute()) {
            ASTNode attrNode = visit(attrCtx);
            if (attrNode != null) {
                elementNode.addChild(attrNode);
            }
        }

        if (ctx.content() != null) {
            ASTNode contentNode = visit(ctx.content());
            if (contentNode != null && contentNode.getChildren() != null) {
                elementNode.addChildren(contentNode.getChildren());
            }
        }

        return elementNode;
    }

    @Override
    public ASTNode visitSelfClosingElement(FrontendParser.SelfClosingElementContext ctx) {
        int line = ctx.start.getLine();
        String tagName = ctx.tagName().getText();
        HtmlElementNode elementNode = new HtmlElementNode(line, tagName, true);

        for (FrontendParser.HtmlAttributeContext attrCtx : ctx.htmlAttribute()) {
            ASTNode attrNode = visit(attrCtx);
            if (attrNode != null) {
                elementNode.addChild(attrNode);
            }
        }

        return elementNode;
    }

    @Override
    public ASTNode visitStyle(FrontendParser.StyleContext ctx) {
        return visit(ctx.styleElement());
    }

    @Override
    public ASTNode visitHtmlAttribute(FrontendParser.HtmlAttributeContext ctx) {
        int line = ctx.start.getLine();
        String attrName = ctx.attrName().getText();
        String attrValue = null;
        if (ctx.attrValue() != null) {
            attrValue = ctx.attrValue().getText();
        }
        return new HtmlAttributeNode(line, attrName, attrValue);
    }

    @Override
    public ASTNode visitText(FrontendParser.TextContext ctx) {
        int line = ctx.start.getLine();
        String text = ctx.getText();
        return new TextNode(line, text);
    }

    @Override
    public ASTNode visitJinjaExpression(FrontendParser.JinjaExpressionContext ctx) {
        int line = ctx.start.getLine();
        String exprText = ctx.jinjaExpr().getText();
        return new JinjaExpressionNode(line, exprText);
    }

    @Override
    public ASTNode visitJinjaForStatement(FrontendParser.JinjaForStatementContext ctx) {
        int line = ctx.start.getLine();
        String iterVar = ctx.IDENTIFIER().getText();
        String collection = ctx.jinjaStmtExpr().getText();

        JinjaForNode forNode = new JinjaForNode(line, iterVar, collection);

        if (ctx.content() != null) {
            ASTNode contentNode = visit(ctx.content());
            if (contentNode != null && contentNode.getChildren() != null) {
                forNode.addChildren(contentNode.getChildren());
            }
        }

        return forNode;
    }

    @Override
    public ASTNode visitJinjaIfStatement(FrontendParser.JinjaIfStatementContext ctx) {
        int line = ctx.start.getLine();
        String condition = ctx.jinjaCondition().getText();

        JinjaIfNode ifNode = new JinjaIfNode(line, condition);

        if (ctx.content() != null) {
            ASTNode contentNode = visit(ctx.content());
            if (contentNode != null && contentNode.getChildren() != null) {
                ifNode.addChildren(contentNode.getChildren());
            }
        }

        for (FrontendParser.JinjaElifStatementContext elifCtx : ctx.jinjaElifStatement()) {
            if (elifCtx.content() != null) {
                ASTNode elifContent = visit(elifCtx.content());
                if (elifContent != null && elifContent.getChildren() != null) {
                    ifNode.addChildren(elifContent.getChildren());
                }
            }
        }

        if (ctx.jinjaElseStatement() != null && ctx.jinjaElseStatement().content() != null) {
            ASTNode elseContent = visit(ctx.jinjaElseStatement().content());
            if (elseContent != null && elseContent.getChildren() != null) {
                ifNode.addChildren(elseContent.getChildren());
            }
        }

        return ifNode;
    }

    @Override
    public ASTNode visitJinjaBlockStatement(FrontendParser.JinjaBlockStatementContext ctx) {
        int line = ctx.start.getLine();
        String blockName = ctx.IDENTIFIER().getText();

        JinjaBlockNode blockNode = new JinjaBlockNode(line, blockName);

        if (ctx.content() != null) {
            ASTNode contentNode = visit(ctx.content());
            if (contentNode != null && contentNode.getChildren() != null) {
                blockNode.addChildren(contentNode.getChildren());
            }
        }

        return blockNode;
    }

    @Override
    public ASTNode visitJinjaExtendsStatement(FrontendParser.JinjaExtendsStatementContext ctx) {
        int line = ctx.start.getLine();
        String templateName = ctx.STRING().getText();
        return new JinjaExtendsNode(line, templateName);
    }

    @Override
    public ASTNode visitStyleElement(FrontendParser.StyleElementContext ctx) {
        int line = ctx.start.getLine();
        HtmlElementNode styleNode = new HtmlElementNode(line, "style", false);

        for (FrontendParser.CssRuleContext ruleCtx : ctx.cssRule()) {
            ASTNode ruleNode = visit(ruleCtx);
            if (ruleNode != null) {
                styleNode.addChild(ruleNode);
            }
        }

        return styleNode;
    }

    @Override
    public ASTNode visitCssRule(FrontendParser.CssRuleContext ctx) {
        int line = ctx.start.getLine();
        CssRuleNode ruleNode = new CssRuleNode(line);

        ASTNode selectorNode = visit(ctx.cssSelector());
        if (selectorNode != null) {
            ruleNode.addChild(selectorNode);
        }

        for (FrontendParser.CssDeclarationContext declCtx : ctx.cssDeclaration()) {
            ASTNode declNode = visit(declCtx);
            if (declNode != null) {
                ruleNode.addChild(declNode);
            }
        }

        return ruleNode;
    }

    @Override
    public ASTNode visitCssSelector(FrontendParser.CssSelectorContext ctx) {
        int line = ctx.start.getLine();
        String selector = ctx.getText();
        return new CssSelectorNode(line, selector);
    }

    @Override
    public ASTNode visitCssDeclaration(FrontendParser.CssDeclarationContext ctx) {
        int line = ctx.start.getLine();
        String property = ctx.cssPropertyName().getText();

        StringBuilder valueBuilder = new StringBuilder();
        for (FrontendParser.CssValueContext valueCtx : ctx.cssValue()) {
            if (valueBuilder.length() > 0) {
                valueBuilder.append(" ");
            }
            valueBuilder.append(valueCtx.getText());
        }
        String value = valueBuilder.toString();

        return new CssDeclarationNode(line, property, value);
    }
}
