package com.example.cpplexer;

import java.util.HashMap;
import java.util.Map;

public class SemanticVisitor extends CppSubsetParserBaseVisitor<Integer> {

    // Memoria para guardar variables
    Map<String, Integer> memoria = new HashMap<>();

    @Override
    public Integer visitAssign(CppSubsetParser.AssignContext ctx) {
        String id = ctx.IDENTIFIER().getText();
        Integer value = visit(ctx.expression());

        if (value != null) {
            memoria.put(id, value);
            System.out.println("Semántico -> Se asignó '" + id + "' con valor: " + value);
        }
        return value;
    }

    @Override
    public Integer visitPrintExpr(CppSubsetParser.PrintExprContext ctx) {
        Integer value = visit(ctx.expression());
        System.out.println("Semántico -> Resultado de la expresión: " + value);
        return value;
    }

    @Override
    public Integer visitAddSub(CppSubsetParser.AddSubContext ctx) {
        // Usamos Integer en vez de int por seguridad
        Integer left = visit(ctx.expression(0));
        Integer right = visit(ctx.expression(1));

        if (left == null || right == null) return null; // Protección contra errores de sintaxis

        if (ctx.op.getText().equals("+")){
            return left + right;
        } else {
            return left - right;
        }
    }

    @Override
    public Integer visitMulDiv(CppSubsetParser.MulDivContext ctx) {
        Integer left = visit(ctx.expression(0));
        Integer right = visit(ctx.expression(1));

        if (left == null || right == null) return null; // Protección

        if (ctx.op.getText().equals("*")) {
            return left * right;
        } else {
            if (right == 0) {
                throw new ArithmeticException("ERROR SEMANTICO: ¡Division por cero detectada!");
            }
            return left / right;
        }
    }

    @Override
    public Integer visitInt(CppSubsetParser.IntContext ctx) {
        return Integer.valueOf(ctx.INT_LITERAL().getText());
    }

    @Override
    public Integer visitId(CppSubsetParser.IdContext ctx) {
        String id = ctx.IDENTIFIER().getText();
        if (memoria.containsKey(id)) {
            return memoria.get(id);
        }
        throw new RuntimeException("ERROR SEMANTICO: Variable '" + id + "' no inicializada.");
    }

    @Override
    public Integer visitParens(CppSubsetParser.ParensContext ctx) {
        return visit(ctx.expression());
    }
}