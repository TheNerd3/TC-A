parser grammar CppSubsetParser;

options { tokenVocab=CppSubsetLexer; }

@header {
    package com.example.cpplexer;
}

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

// Minimal statement set so parser rules referring to statement compileable
statement
    : variableDecl
    | block
    | SEMI
    | RETURN expression? SEMI
    ;

expression
    : IDENTIFIER
    | INT_LITERAL
    | STRING_LITERAL
    | CHAR_LITERAL
    ;
