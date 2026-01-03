parser grammar FrontendParser;

options {
    tokenVocab = FrontendLexer;
}

document
    : jinjaExtendsStatement? DOCTYPE? content? EOF
    ;

content
    : contentItem*
    ;

contentItem
    : htmlElement
    | jinjaExpression
    | jinjaStatement
    | text
    ;

htmlElement
    : TAG_OPEN tagName htmlAttribute* TAG_CLOSE content TAG_OPEN TAG_SLASH tagName TAG_CLOSE    # RegularElement
    | TAG_OPEN tagName htmlAttribute* TAG_SLASH TAG_CLOSE                                        # SelfClosingElement
    | styleElement                                                                               # Style
    ;

tagName
    : IDENTIFIER
    | CSS_IDENT
    ;

htmlAttribute
    : attrName (EQUALS attrValue)?
    ;

attrName
    : IDENTIFIER
    | CSS_IDENT
    ;

attrValue
    : STRING
    | IDENTIFIER
    | CSS_IDENT
    ;

styleElement
    : TAG_OPEN STYLE_TAG TAG_CLOSE cssRule* TAG_OPEN TAG_SLASH STYLE_TAG TAG_CLOSE
    ;

cssRule
    : cssSelector CSS_LBRACE cssDeclaration* CSS_RBRACE
    ;

cssSelector
    : cssSelectorPart (COMMA cssSelectorPart)*
    ;

cssSelectorPart
    : cssSelectorItem+
    ;

cssSelectorItem
    : IDENTIFIER
    | CSS_IDENT
    | DOT IDENTIFIER
    | DOT CSS_IDENT
    | HASH IDENTIFIER
    | HASH CSS_IDENT
    ;

cssDeclaration
    : cssPropertyName CSS_COLON cssValue+ CSS_SEMICOLON
    ;

cssPropertyName
    : IDENTIFIER
    | CSS_IDENT
    ;

cssValue
    : IDENTIFIER
    | CSS_IDENT
    | NUMBER CSS_UNIT?
    | HASH IDENTIFIER
    | HASH CSS_IDENT
    | HASH NUMBER
    | STRING
    ;

jinjaExpression
    : JINJA_EXPR_START jinjaExpr JINJA_EXPR_END
    ;

jinjaExpr
    : IDENTIFIER                                            # JinjaIdentifier
    | jinjaExpr DOT IDENTIFIER                              # JinjaMemberAccess
    | jinjaExpr LBRACKET jinjaExpr RBRACKET                 # JinjaIndexAccess
    | STRING                                                # JinjaStringLiteral
    | NUMBER                                                # JinjaNumberLiteral
    ;

jinjaStatement
    : jinjaIfStatement
    | jinjaForStatement
    | jinjaBlockStatement
    ;

jinjaIfStatement
    : JINJA_STMT_START JINJA_IF jinjaCondition JINJA_STMT_END
      content
      jinjaElifStatement*
      jinjaElseStatement?
      JINJA_STMT_START JINJA_ENDIF JINJA_STMT_END
    ;

jinjaElifStatement
    : JINJA_STMT_START JINJA_ELIF jinjaCondition JINJA_STMT_END content
    ;

jinjaElseStatement
    : JINJA_STMT_START JINJA_ELSE JINJA_STMT_END content
    ;

jinjaForStatement
    : JINJA_STMT_START JINJA_FOR IDENTIFIER JINJA_IN jinjaStmtExpr JINJA_STMT_END
      content
      JINJA_STMT_START JINJA_ENDFOR JINJA_STMT_END
    ;

jinjaExtendsStatement
    : JINJA_STMT_START JINJA_EXTENDS STRING JINJA_STMT_END
    ;

jinjaBlockStatement
    : JINJA_STMT_START JINJA_BLOCK IDENTIFIER JINJA_STMT_END
      content
      JINJA_STMT_START JINJA_ENDBLOCK JINJA_STMT_END
    ;

jinjaCondition
    : jinjaStmtExpr ((JINJA_EQ | JINJA_NEQ | JINJA_LT | JINJA_GT | JINJA_LTE | JINJA_GTE) jinjaStmtExpr)?
    | jinjaCondition JINJA_AND jinjaCondition
    | jinjaCondition JINJA_OR jinjaCondition
    | JINJA_NOT jinjaCondition
    ;

jinjaStmtExpr
    : IDENTIFIER                                            # JinjaStmtIdentifier
    | jinjaStmtExpr DOT IDENTIFIER                          # JinjaStmtMemberAccess
    | jinjaStmtExpr LBRACKET jinjaStmtExpr RBRACKET         # JinjaStmtIndexAccess
    | STRING                                                # JinjaStmtStringLiteral
    | NUMBER                                                # JinjaStmtNumberLiteral
    ;

text
    : TEXT
    | NUMBER
    | IDENTIFIER
    | CSS_IDENT
    ;
