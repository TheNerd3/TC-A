parser grammar CppSubsetParser;

options { tokenVocab=CppSubsetLexer; }

program
    : globalItem* EOF
    ;

globalItem
    : variableDecl
    | functionDecl
    | functionDef
    | statement
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
    | FLOAT
    | DOUBLE
    | CHAR
    | BOOL
    | VOID
    | STRING
    | DATE
    ;

block
    : LBRACE statement* RBRACE
    ;

// Statements
statement
    : IDENTIFIER ASSIGN expression SEMI                                     # Assign
    | IDENTIFIER LBRACK expression RBRACK ASSIGN expression SEMI            # ArrayAssign
    | RETURN expression SEMI                                                # ReturnExpr
    | RETURN SEMI                                                           # ReturnVoid
    | expression SEMI                                                       # PrintExpr
    | variableDecl                                                          # VarDeclStmt
    | block                                                                 # BlockStmt
    | FOR LPAREN forInit? SEMI expression? SEMI forUpdate? RPAREN statement # ForStmt
    | SEMI                                                                  # EmptyStmt
    | ifStatement                                                           # IfStmt
    | whileStatement                                                        # WhileStmt
    | breakStatement                                                        # BreakStmt
    | continueStatement                                                     # ContinueStmt
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

// Expressions with precedence
expression
    : expression op=(STAR|DIV|MOD) expression                   # MulDiv
    | expression op=(PLUS|MINUS) expression                     # AddSub
    | expression op=(LT|LE|GT|GE|EQEQ|NEQ) expression           # Relational
    | expression op=(ANDAND|OROR) expression                    # Logical
    | IDENTIFIER (PLUSPLUS | MINUSMINUS)                        # PostIncDec
    | IDENTIFIER LPAREN argumentList? RPAREN                    # FuncCall
    | INT_LITERAL                                               # Int
    | DOUBLE_LITERAL                                            # Double
    | FLOAT_LITERAL                                             # Float
    | CHAR_LITERAL                                              # Char
    | BOOL_LITERAL                                              # Bool
    | STRING_LITERAL                                            # StringLit
    | DATE_LITERAL                                              # DateLit
    | IDENTIFIER LBRACK expression RBRACK                       # ArrayAccess
    | IDENTIFIER                                                # Id
    | LPAREN expression RPAREN                                  # Parens
    ;

argumentList
    : expression (COMMA expression)*
    ;