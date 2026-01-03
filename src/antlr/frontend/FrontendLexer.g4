lexer grammar FrontendLexer;

DOCTYPE: '<!DOCTYPE' [ ]+ 'html>';

JINJA_EXPR_START: '{{';
JINJA_EXPR_END: '}}';
JINJA_STMT_START: '{%';
JINJA_STMT_END: '%}';
JINJA_COMMENT: '{#' .*? '#}' -> skip;

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

TAG_OPEN: '<';
TAG_CLOSE: '>';
TAG_SLASH: '/';
STYLE_TAG: 'style';

JINJA_EQ: '==';
JINJA_NEQ: '!=';
JINJA_LTE: '<=';
JINJA_GTE: '>=';
JINJA_LT: '<';
JINJA_GT: '>';
JINJA_AND: 'and';
JINJA_OR: 'or';
JINJA_NOT: 'not';

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
