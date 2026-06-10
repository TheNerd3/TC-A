package com.example.cpplexer;

import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;

public class ErrorReporter {
    public static String format(ParserRuleContext ctx, List<String> sourceLines, String message, String suggestion) {
        if (ctx == null) {
            String base = message;
            if (suggestion != null) base += "\nSuggestion: " + suggestion;
            return base;
        }

        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();

        String snippet = "";
        if (sourceLines != null && line >= 1 && line <= sourceLines.size()) {
            snippet = sourceLines.get(line - 1).replace("\t", "    ");
        }

        String pointer = "";
        if (col >= 0) {
            pointer = String.join("", Collections.nCopies(col, " ")) + "^";
        }

        StringBuilder out = new StringBuilder();
        out.append("error(").append(line).append(":").append(col).append("): ").append(message).append(System.lineSeparator());
        if (!snippet.isEmpty()) {
            out.append(snippet).append(System.lineSeparator());
            out.append(pointer).append(System.lineSeparator());
        }
        if (suggestion != null) {
            out.append("Suggestion: ").append(suggestion);
        }

        return out.toString();
    }
}
