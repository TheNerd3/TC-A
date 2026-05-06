package com.example.cpplexer;

import java.util.HashMap;
import java.util.Map;

public class SemanticVisitor extends CppSubsetParserBaseVisitor<Integer> {
    Map<String, Integer> memoria = new HashMap<>();

    @Override
    public Integer visitAssign(com.example.cpplexer.CppSubsetParser.AssignContext ctx) {
        String id = ctx.IDENTIFIER().getText();
        int value = visit(ctx.expression());
        memoria.put(id, value);
        System.out.println("Semántico -> Se asignó '" + id + "' con valor: " + value);
        return value;
    }

    @Override
    public Integer visitAddSub(com.example.cpplexer.CppSubsetParser.AddSubContext ctx) {
        int left = visit(ctx.expression(0));
        int right = visit(ctx.expression(1));

        if (ctx.op.getText().equals("+")){
            return left + right;
        } else {
            return left - right;
        }
    }

    @Override
    public Integer visitMulDiv(com.example.cpplexer.CppSubsetParser.MulDivContext ctx) {
        int left = visit(ctx.expression(0));
        int right = visit(ctx.expression(1));

        if (ctx.op.getText().equals("*")) {
            return left * right;
        } else {
            if (right == 0) {
                throw  new ArithmeticException("ERROR SEMANTICO: Division por cero detectada!");
            }
            return left / right;
        }
    }

    @Override
    public Integer visitParens(com.example.cpplexer.CppSubsetParser.ParensContext ctx) {
        return visit(ctx.expression());
    }
}
