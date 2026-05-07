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
    : IDENTIFIER '=' expression SEMI               # Assign
    | RETURN expression SEMI                       # ReturnExpr
    | RETURN SEMI                                  # ReturnVoid
    | expression SEMI                              # PrintExpr
    ;

expression
    : expression op=('*'|'/') expression                        # MulDiv
    | expression op=('+'|'-') expression                        # AddSub
    | IDENTIFIER LPAREN argumentList? RPAREN                    # FuncCall
    | INT_LITERAL                                               # Int
    | DOUBLE_LITERAL                                            # Double
    | CHAR_LITERAL                                              # Char
    | BOOL_LITERAL                                              # Bool
    | STRING_LITERAL                                            # StringLit
    | IDENTIFIER                                                # Id
    | LPAREN expression RPAREN                                  # Parens
    ;

argumentList
    : expression (COMMA expression)*
    ;