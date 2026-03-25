package com.example.cpplexer;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LexerMain {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Uso: java com.example.cpplexer.LexerMain <archivoFuente>");
            System.exit(1);
        }

        String path = args[0];

        try {
            CharStream input = CharStreams.fromFileName(path);
            CppSubsetLexer lexer = new CppSubsetLexer(input);

            List<? extends Token> tokens = lexer.getAllTokens();
            List<TokenInfo> tokenList = new ArrayList<>();
            List<TokenInfo> errors = new ArrayList<>();

            for (Token t : tokens) {
                if (t.getType() == Token.EOF) continue;

                String typeName = CppSubsetLexer.VOCABULARY.getSymbolicName(t.getType());
                if (typeName == null) {
                    String lit = CppSubsetLexer.VOCABULARY.getLiteralName(t.getType());
                    typeName = lit != null ? lit : Integer.toString(t.getType());
                }

                TokenInfo info = new TokenInfo(typeName, t.getText(), t.getLine(), t.getCharPositionInLine());
                if ("INVALID".equals(typeName)) {
                    errors.add(info);
                } else {
                    tokenList.add(info);
                }
            }

            // Imprimir tabla de tokens
            printTable(tokenList);

            if (!errors.isEmpty()) {
                System.out.println();
                System.out.println(color(ANSI_RED, "ERRORES LÉXICOS DETECTADOS:"));
                for (TokenInfo e : errors) {
                    System.out.printf("%sLine %d, Col %d:%s unexpected lexeme -> %s%n",
                            ANSI_RED, e.getLine(), e.getCol(), ANSI_RESET, e.getLexeme());
                }
                System.out.printf("%sResumen: %d error(es) léxico(s)%s%n", ANSI_RED, errors.size(), ANSI_RESET);
                System.exit(2);
            } else {
                System.out.println();
                System.out.println(color(ANSI_GREEN, "Análisis léxico completado correctamente. Sin errores léxicos."));
            }

        } catch (IOException e) {
            System.err.println(color(ANSI_RED, "Error leyendo archivo: " + e.getMessage()));
            System.exit(1);
        }
    }

    private static void printTable(List<TokenInfo> tokens) {
        int wType = 20;
        int wLex = 30;
        System.out.println(String.format("%-" + wType + "s | %-" + wLex + "s | %4s | %3s", "TYPE", "LEXEMA", "LINE", "COL"));
        System.out.println(new String(new char[wType + wLex + 12]).replace('\0', '-'));

        for (TokenInfo t : tokens) {
            String type = t.getType();
            String lex = t.getLexeme().replace("\n", "\\n").replace("\r", "\\r");
            if (lex.length() > wLex) lex = lex.substring(0, wLex - 3) + "...";
            System.out.printf("%-" + wType + "s | %-" + wLex + "s | %4d | %3d%n", type, lex, t.getLine(), t.getCol());
        }
    }

    private static String color(String ansi, String text) {
        return ansi + text + ANSI_RESET;
    }
}
