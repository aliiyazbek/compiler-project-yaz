parser grammar PythonFlaskParser;

options {
    tokenVocab = PythonFlaskLexer;
}

program: NEWLINE* (statement NEWLINE+)* statement? NEWLINE* EOF;

    statement
        : importStatement
        | assignmentStatement
        | decoratedFunction
        | functionDefinition
        | ifStatement
        | forStatement
        | returnStatement
        | expressionStatement
        ;

importStatement
    : FROM IDENTIFIER IMPORT importList
    ;

importList
    : IDENTIFIER (COMMA IDENTIFIER)*
    ;

assignmentStatement
    : IDENTIFIER ASSIGN expression
    ;

functionDefinition
    : DEF IDENTIFIER LPAREN parameterList? RPAREN COLON NEWLINE functionBody
    ;

parameterList
    : parameter (COMMA parameter)*
    ;

parameter
    : IDENTIFIER
    ;

functionBody
    : (statement NEWLINE+)+
    ;

decoratedFunction
    : (decorator NEWLINE+)+ functionDefinition
    ;

decorator
    : AT expression
    ;

ifStatement
    : IF expression COLON NEWLINE (statement NEWLINE+)+
      elifStatement*
      elseStatement?
    ;

elifStatement
    : ELIF expression COLON NEWLINE (statement NEWLINE+)+
    ;

elseStatement
    : ELSE COLON NEWLINE (statement NEWLINE+)+
    ;

forStatement
    : FOR IDENTIFIER IN expression COLON NEWLINE (statement NEWLINE+)+
    ;

returnStatement
    : RETURN expression?
    ;

expressionStatement
    : expression
    ;

expression
    : primary                                           # PrimaryExpression
    | expression DOT IDENTIFIER                         # MemberAccessExpression
    | expression LBRACKET expression RBRACKET           # IndexAccessExpression
    | expression LPAREN argumentList? RPAREN            # FunctionCallExpression
    | NOT expression                                    # NotExpression
    | expression op=(STAR | SLASH) expression           # MultiplicativeExpression
    | expression op=(PLUS | MINUS) expression           # AdditiveExpression
    | expression op=(LT | GT | LTE | GTE) expression    # ComparisonExpression
    | expression op=(EQ | NEQ) expression               # EqualityExpression
    | expression AND expression                         # AndExpression
    | expression OR expression                          # OrExpression
    ;

primary
    : IDENTIFIER                                        # IdentifierPrimary
    | STRING                                            # StringLiteral
    | INTEGER                                           # IntegerLiteral
    | FLOAT                                             # FloatLiteral
    | TRUE                                              # BooleanLiteral
    | FALSE                                             # BooleanLiteral
    | NONE                                              # NoneLiteral
    | listLiteral                                       # ListPrimary
    | dictLiteral                                       # DictPrimary
    | LPAREN expression RPAREN                          # ParenthesizedExpression
    ;

listLiteral
    : LBRACKET (expression (COMMA expression)*)? RBRACKET
    ;

dictLiteral
    : LBRACE (keyValuePair (COMMA keyValuePair)*)? RBRACE
    ;

keyValuePair
    : (STRING | IDENTIFIER) COLON expression
    ;

argumentList
    : argument (COMMA argument)*
    ;

argument
    : IDENTIFIER ASSIGN expression  # KeywordArgument
    | expression                    # PositionalArgument
    ;
