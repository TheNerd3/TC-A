parser grammar CppSubsetParser;

options { tokenVocab=CppSubsetLexer; }

program
    : globalItem* EOF
    ;

globalItem
    : variableDecl
    | functionDecl
    | functionDef
    ;

functionDecl
    : type IDENTIFIER LPAREN parameterList? RPAREN SEMI
    ;

functionDef
    : type IDENTIFIER LPAREN parameterList? RPAREN block
    ;

parameterList
    : parameter (COMMA parameter)*
    ;

parameter
    : type IDENTIFIER
    ;

variableDecl
    : type IDENTIFIER SEMI
    | type IDENTIFIER LBRACK INT_LITERAL RBRACK SEMI
    ;

type
    : 'int'
    | 'double'
    | 'char'
    | 'bool'
    | 'void'
    ;

block
    : LBRACE statement* RBRACE
    ;

statement
    : IDENTIFIER '=' expression SEMI   # Assign
    | expression SEMI                  # PrintExpr
    ;

expression
    : expression op=('*'|'/') expression # MulDiv
    | expression op=('+'|'-') expression # AddSub
    | INT_LITERAL                        # Int
    | IDENTIFIER                         # Id
    | LPAREN expression RPAREN           # Parens
    ;