package com.example.cpplexer;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class LexerErrorListener extends BaseErrorListener {
    public static class LexError {
        public final int line;
        public final int charPositionInLine;
        public final String msg;

        public LexError(int line, int charPositionInLine, String msg) {
            this.line = line;
            this.charPositionInLine = charPositionInLine;
            this.msg = msg;
        }
    }

    private final List<LexError> errors = new ArrayList<>();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        errors.add(new LexError(line, charPositionInLine, msg));
    }

    public List<LexError> getErrors() {
        return errors;
    }
}
