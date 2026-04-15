lexer grammar CppSubsetLexer;

@header {
    package com.example.cpplexer;
}

// Keywords (placed before IDENTIFIER to match keywords first)
// Data types
INT: 'int';
CHAR: 'char';
DOUBLE: 'double';
VOID: 'void';
DATE: 'date';
BOOL: 'bool';

// Reserved words
IF: 'if';
ELSE: 'else';
FOR: 'for';
WHILE: 'while';
BREAK: 'break';
CONTINUE: 'continue';
RETURN: 'return';

fragment YEAR : [0-9];
fragment MONTH
    : '0' [1-9]
    | '1' [0-2];
fragment DAY
    : '0' [1-9]
    | [12] [0-9]
    | '3' [01]
    ;

// Multi-character operators
EQEQ: '==';
NEQ: '!=';
LE: '<=';
GE: '>=';
PE: '+=';
ME: '-=';
MULE: '*=';
DIVE: '/=';
ANDAND: '&&';
OROR: '||';
PLUSPLUS: '++';
MINUSMINUS: '--';

// Single-character operators
PLUS: '+';
MINUS: '-';
STAR: '*';
DIV: '/';
MOD: '%';
ASSIGN: '=';
LT: '<';
GT: '>';
NOT: '!';

// Separators
SEMI: ';';
COMMA: ',';
LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';
LBRACK: '[';
RBRACK: ']';
SQUOTE: '\'';
DQUOTE: '"';

// Literals 
FLOAT_LITERAL
    : [+-]?[0-9]+ '.' [0-9]* ([eE] [+-]? [0-9]+)?
    | [+-]?[0] '.' [0-9]+ ([eE] [+-]? [0-9]+)?
    | [+-]?[0-9]+ [eE] [+-]? [0-9]+
    ;
INT_LITERAL: [+-]?[0-9]+;

CHAR_LITERAL
    : '\'' ( ~[\\'\r\n] | '\\' . ) '\''
    ;

STRING_LITERAL
    : '"' ( ~[\\"\r\n] | '\\' . )* '"'
    ;

DATE_LITERAL
    : '\'' YEAR '-' MONTH '-' DAY '\'' // formato ISO 8601
    | '\'' DAY '-' MONTH '-' YEAR '\'' // formato arg
    ;   
DOUBLE_LITERAL
    : [+-]?[0-9]+ '.' [0-9]* ([eE] [+-]? [0-9]+)?
    | [+-]?[0] '.' [0-9]+ ([eE] [+-]? [0-9]+)?
    | [+-]?[0-9]+ [eE] [+-]? [0-9]+
    ;

BOOL_LITERAL
    : 'TRUE'
    | 'FALSE'
    | [01]
    ;

// Identifier
IDENTIFIER: [a-zA-Z_] [a-zA-Z0-9_]*;

// Comments
LINE_COMMENT: '//' ~[\r\n]* -> skip;
BLOCK_COMMENT: '/*' .*? '*/' -> skip;

// Whitespace
WS: [ \t\r\n]+ -> skip;

// Any unrecognized character
INVALID: . ;
