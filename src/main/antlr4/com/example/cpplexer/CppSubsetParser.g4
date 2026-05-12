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
    : type IDENTIFIER (ASSIGN expression)? SEMI
    | type IDENTIFIER LBRACK INT_LITERAL RBRACK (ASSIGN expression)? SEMI
    ;

// variant without trailing semicolon (useful for for-loop init)
variableDeclNoSemi
    : type IDENTIFIER (ASSIGN expression)?
    | type IDENTIFIER LBRACK INT_LITERAL RBRACK (ASSIGN expression)?
    ;

type
    : INT
    | DOUBLE
    | CHAR
    | BOOL
    | VOID
    | DATE
    ;

block
    : LBRACE statement* RBRACE
    ;

statement
// Statements
statement
    : variableDecl
    | block
    | SEMI
    | RETURN expression? SEMI
    | ifStatement
    | whileStatement
    | forStatement
    | breakStatement
    | continueStatement
    | expressionStatement
    ;

ifStatement
    : IF LPAREN expression RPAREN statement (ELSE statement)?
    ;

whileStatement
    : WHILE LPAREN expression RPAREN statement
    ;

forStatement
    : FOR LPAREN forInit? SEMI expression? SEMI forUpdate? RPAREN statement
    ;

forInit
    : variableDeclNoSemi
    | expression
    ;

forUpdate
    : expression (COMMA expression)*
    ;

breakStatement
    : BREAK SEMI
    ;

continueStatement
    : CONTINUE SEMI
    ;

expressionStatement
    : expression? SEMI
    ;
    ;

expression
// Expressions with precedence
expression
    : assignmentExpression
    ;

assignmentExpression
    : logicalOrExpression (ASSIGN assignmentExpression)?
    ;

logicalOrExpression
    : logicalAndExpression ( OROR logicalAndExpression )*
    ;

logicalAndExpression
    : equalityExpression ( ANDAND equalityExpression )*
    ;

equalityExpression
    : relationalExpression ( (EQEQ | NEQ) relationalExpression )*
    ;

relationalExpression
    : additiveExpression ( (LT | LE | GT | GE) additiveExpression )*
    ;

additiveExpression
    : multiplicativeExpression ( (PLUS | MINUS) multiplicativeExpression )*
    ;

multiplicativeExpression
    : unaryExpression ( (STAR | DIV | MOD) unaryExpression )*
    ;

unaryExpression
    : (PLUS | MINUS | NOT)? postfixExpression
    ;

postfixExpression
    : primary ( PLUSPLUS | MINUSMINUS )?
    ;

primary
    : IDENTIFIER
    | INT_LITERAL
    | DOUBLE_LITERAL
    | FLOAT_LITERAL
    | STRING_LITERAL
    | CHAR_LITERAL
    | DATE_LITERAL
    | BOOL_LITERAL
    | LPAREN expression RPAREN
    ;
    ;

argumentList
    : expression (COMMA expression)*
    ;