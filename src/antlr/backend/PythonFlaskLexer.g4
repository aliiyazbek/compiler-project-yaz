lexer grammar PythonFlaskLexer;

FROM: 'from';
IMPORT: 'import';
DEF: 'def';
RETURN: 'return';
IF: 'if';
ELIF: 'elif';
ELSE: 'else';
FOR: 'for';
IN: 'in';
AND: 'and';
OR: 'or';
NOT: 'not';
TRUE: 'True';
FALSE: 'False';
NONE: 'None';

EQ: '==';
NEQ: '!=';
LTE: '<=';
GTE: '>=';
ASSIGN: '=';
PLUS: '+';
MINUS: '-';
STAR: '*';
SLASH: '/';
LT: '<';
GT: '>';

LPAREN: '(' -> pushMode(INSIDE_BRACKETS);
RPAREN: ')';
LBRACKET: '[' -> pushMode(INSIDE_BRACKETS);
RBRACKET: ']';
LBRACE: '{' -> pushMode(INSIDE_BRACKETS);
RBRACE: '}';
COMMA: ',';
COLON: ':';
DOT: '.';
AT: '@';

STRING: '"' (~["\r\n\\] | '\\' .)* '"'
      | '\'' (~['\r\n\\] | '\\' .)* '\'';

FLOAT: [0-9]+ '.' [0-9]+;
INTEGER: '-'? [0-9]+;

IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_]*;

NEWLINE: '\r'? '\n';
WS: [ \t]+ -> skip;
COMMENT: '#' ~[\r\n]* -> skip;

mode INSIDE_BRACKETS;

IB_LPAREN: '(' -> type(LPAREN), pushMode(INSIDE_BRACKETS);
IB_RPAREN: ')' -> type(RPAREN), popMode;
IB_LBRACKET: '[' -> type(LBRACKET), pushMode(INSIDE_BRACKETS);
IB_RBRACKET: ']' -> type(RBRACKET), popMode;
IB_LBRACE: '{' -> type(LBRACE), pushMode(INSIDE_BRACKETS);
IB_RBRACE: '}' -> type(RBRACE), popMode;

IB_COMMA: ',' -> type(COMMA);
IB_COLON: ':' -> type(COLON);
IB_DOT: '.' -> type(DOT);

IB_EQ: '==' -> type(EQ);
IB_NEQ: '!=' -> type(NEQ);
IB_LTE: '<=' -> type(LTE);
IB_GTE: '>=' -> type(GTE);
IB_ASSIGN: '=' -> type(ASSIGN);
IB_PLUS: '+' -> type(PLUS);
IB_MINUS: '-' -> type(MINUS);
IB_STAR: '*' -> type(STAR);
IB_SLASH: '/' -> type(SLASH);
IB_LT: '<' -> type(LT);
IB_GT: '>' -> type(GT);

IB_STRING: '"' (~["\r\n\\] | '\\' .)* '"' -> type(STRING);
IB_STRING2: '\'' (~['\r\n\\] | '\\' .)* '\'' -> type(STRING);
IB_FLOAT: [0-9]+ '.' [0-9]+ -> type(FLOAT);
IB_INTEGER: '-'? [0-9]+ -> type(INTEGER);
IB_TRUE: 'True' -> type(TRUE);
IB_FALSE: 'False' -> type(FALSE);
IB_NONE: 'None' -> type(NONE);
IB_IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_]* -> type(IDENTIFIER);

IB_WS: [ \t\r\n]+ -> skip;
IB_COMMENT: '#' ~[\r\n]* -> skip;
