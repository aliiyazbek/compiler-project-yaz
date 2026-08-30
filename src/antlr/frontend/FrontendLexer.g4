lexer grammar FrontendLexer;

DOCTYPE: '<!DOCTYPE' [ ]+ 'html>';

JINJA_EXPR_START: '{{' -> pushMode(JINJA);
JINJA_STMT_START: '{%' -> pushMode(JINJA);
JINJA_COMMENT: '{#' .*? '#}' -> skip;

TAG_OPEN: '<';
TAG_CLOSE: '>';
TAG_SLASH: '/';
STYLE_TAG: 'style';

CSS_LBRACE: '{';
CSS_RBRACE: '}';
CSS_COLON: ':';
CSS_SEMICOLON: ';';

EQUALS: '=';
DOT: '.';
LBRACKET: '[';
RBRACKET: ']';
COMMA: ',';
HASH: '#';

STRING: '"' (~["\r\n\\] | '\\' .)* '"'
      | '\'' (~['\r\n\\] | '\\' .)* '\'';

NUMBER: [0-9]+ ('.' [0-9]+)?;

CSS_UNIT: 'px' | '%' | 'em' | 'rem';

CSS_IDENT: [a-zA-Z] [a-zA-Z0-9]* ('-' [a-zA-Z0-9]+)+;

IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_]*;

TEXT: [,!?;:@]+;

WS: [ \t\r\n]+ -> skip;

HTML_COMMENT: '<!--' .*? '-->' -> skip;
CSS_COMMENT: '/*' .*? '*/' -> skip;


mode JINJA;

JINJA_EXPR_END: '}}' -> popMode;
JINJA_STMT_END: '%}' -> popMode;

JINJA_IF: 'if';
JINJA_ELIF: 'elif';
JINJA_ELSE: 'else';
JINJA_ENDIF: 'endif';
JINJA_FOR: 'for';
JINJA_IN: 'in';
JINJA_ENDFOR: 'endfor';
JINJA_EXTENDS: 'extends';
JINJA_BLOCK: 'block';
JINJA_ENDBLOCK: 'endblock';

JINJA_EQ: '==';
JINJA_NEQ: '!=';
JINJA_LTE: '<=';
JINJA_GTE: '>=';
JINJA_LT: '<';
JINJA_GT: '>';
JINJA_AND: 'and';
JINJA_OR: 'or';
JINJA_NOT: 'not';

J_DOT: '.' -> type(DOT);
J_LBRACKET: '[' -> type(LBRACKET);
J_RBRACKET: ']' -> type(RBRACKET);
J_COMMA: ',' -> type(COMMA);

J_STRING: '"' (~["\r\n\\] | '\\' .)* '"' -> type(STRING);
J_STRING_SQ: '\'' (~['\r\n\\] | '\\' .)* '\'' -> type(STRING);

J_NUMBER: [0-9]+ ('.' [0-9]+)? -> type(NUMBER);

J_IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_]* -> type(IDENTIFIER);

J_WS: [ \t\r\n]+ -> skip;

J_JINJA_COMMENT: '{#' .*? '#}' -> skip;
J_HTML_COMMENT: '<!--' .*? '-->' -> skip;
J_CSS_COMMENT: '/*' .*? '*/' -> skip;
