package com.example.cpplexer;

public class TokenInfo {
    private final String type;
    private final String lexeme;
    private final int line;
    private final int col;

    public TokenInfo(String type, String lexeme, int line, int col) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.col = col;
    }

    public String getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }
}
