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

// Reserved words
IF: 'if';
ELSE: 'else';
FOR: 'for';
WHILE: 'while';
BREAK: 'break';
CONTINUE: 'continue';
RETURN: 'return';

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

// Literals 
FLOAT_LITERAL
    : [0-9]+ '.' [0-9]* ([eE] [+-]? [0-9]+)?
    | [0] '.' [0-9]+ ([eE] [+-]? [0-9]+)?
    | [0-9]+ [eE] [+-]? [0-9]+
    ;
INT_LITERAL: [0-9]+;

CHAR_LITERAL
    : '\'' ( ~[\\'\r\n] | '\\' . ) '\''
    ;

STRING_LITERAL
    : '"' ( ~[\\"\r\n] | '\\' . )* '"'
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
