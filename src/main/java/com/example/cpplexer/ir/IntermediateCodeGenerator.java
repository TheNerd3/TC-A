package com.example.cpplexer.ir;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.example.cpplexer.CppSubsetParser;
import com.example.cpplexer.CppSubsetParserBaseVisitor;

public class IntermediateCodeGenerator extends CppSubsetParserBaseVisitor<String> {
    private static class LoopLabels {
        private final String continueLabel;
        private final String breakLabel;

        LoopLabels(String continueLabel, String breakLabel) {
            this.continueLabel = continueLabel;
            this.breakLabel = breakLabel;
        }
    }

    private final IntermediateCode code = new IntermediateCode();
    private final Deque<LoopLabels> loopStack = new ArrayDeque<>();
    private int tempCounter = 0;
    private int labelCounter = 0;

    public IntermediateCode getCode() {
        return code;
    }

    @Override
    public String visitProgram(CppSubsetParser.ProgramContext ctx) {
        for (CppSubsetParser.GlobalItemContext item : ctx.globalItem()) {
            visit(item);
        }
        return null;
    }

    @Override
    public String visitFunctionDecl(CppSubsetParser.FunctionDeclContext ctx) {
        code.add("declare " + ctx.type().getText() + " " + ctx.IDENTIFIER().getText() + "(" + parameterNames(ctx.parameterList()) + ")");
        return null;
    }

    @Override
    public String visitFunctionDef(CppSubsetParser.FunctionDefContext ctx) {
        String functionName = ctx.IDENTIFIER().getText();
        code.add("");
        code.add("func " + functionName + ":");
        if (ctx.parameterList() != null) {
            for (CppSubsetParser.ParameterContext parameter : ctx.parameterList().parameter()) {
                code.add("param " + parameter.IDENTIFIER().getText());
            }
        }
        visit(ctx.block());
        code.add("endfunc " + functionName);
        return null;
    }

    @Override
    public String visitVariableDecl(CppSubsetParser.VariableDeclContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        if (ctx.LBRACK() != null) {
            code.add("declare " + ctx.type().getText() + " " + name + "[" + ctx.INT_LITERAL().getText() + "]");
        } else {
            code.add("declare " + ctx.type().getText() + " " + name);
        }

        if (ctx.ASSIGN() != null) {
            String value = visit(ctx.expression());
            code.add(name + " = " + value);
        }
        return null;
    }

    @Override
    public String visitVariableDeclNoSemi(CppSubsetParser.VariableDeclNoSemiContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        if (ctx.LBRACK() != null) {
            code.add("declare " + ctx.type().getText() + " " + name + "[" + ctx.INT_LITERAL().getText() + "]");
        } else {
            code.add("declare " + ctx.type().getText() + " " + name);
        }

        if (ctx.ASSIGN() != null) {
            String value = visit(ctx.expression());
            code.add(name + " = " + value);
        }
        return null;
    }

    @Override
    public String visitAssign(CppSubsetParser.AssignContext ctx) {
        String value = visit(ctx.expression());
        code.add(ctx.IDENTIFIER().getText() + " = " + value);
        return null;
    }

    @Override
    public String visitArrayAssign(CppSubsetParser.ArrayAssignContext ctx) {
        String index = visit(ctx.expression(0));
        String value = visit(ctx.expression(1));
        code.add(ctx.IDENTIFIER().getText() + "[" + index + "] = " + value);
        return null;
    }

    @Override
    public String visitReturnExpr(CppSubsetParser.ReturnExprContext ctx) {
        code.add("return " + visit(ctx.expression()));
        return null;
    }

    @Override
    public String visitReturnVoid(CppSubsetParser.ReturnVoidContext ctx) {
        code.add("return");
        return null;
    }

    @Override
    public String visitPrintExpr(CppSubsetParser.PrintExprContext ctx) {
        visit(ctx.expression());
        return null;
    }

    @Override
    public String visitIfStatement(CppSubsetParser.IfStatementContext ctx) {
        String elseLabel = newLabel();
        String endLabel = newLabel();
        String condition = visit(ctx.expression());

        code.add("ifFalse " + condition + " goto " + elseLabel);
        visit(ctx.statement(0));

        if (ctx.statement().size() > 1) {
            code.add("goto " + endLabel);
            code.add(elseLabel + ":");
            visit(ctx.statement(1));
            code.add(endLabel + ":");
        } else {
            code.add(elseLabel + ":");
        }
        return null;
    }

    @Override
    public String visitWhileStatement(CppSubsetParser.WhileStatementContext ctx) {
        String startLabel = newLabel();
        String endLabel = newLabel();

        code.add(startLabel + ":");
        String condition = visit(ctx.expression());
        code.add("ifFalse " + condition + " goto " + endLabel);

        loopStack.push(new LoopLabels(startLabel, endLabel));
        visit(ctx.statement());
        loopStack.pop();

        code.add("goto " + startLabel);
        code.add(endLabel + ":");
        return null;
    }

    @Override
    public String visitForStmt(CppSubsetParser.ForStmtContext ctx) {
        String startLabel = newLabel();
        String updateLabel = newLabel();
        String endLabel = newLabel();

        if (ctx.forInit() != null) {
            visit(ctx.forInit());
        }

        code.add(startLabel + ":");
        if (ctx.expression() != null) {
            String condition = visit(ctx.expression());
            code.add("ifFalse " + condition + " goto " + endLabel);
        }

        loopStack.push(new LoopLabels(updateLabel, endLabel));
        visit(ctx.statement());
        loopStack.pop();

        code.add(updateLabel + ":");
        if (ctx.forUpdate() != null) {
            visit(ctx.forUpdate());
        }
        code.add("goto " + startLabel);
        code.add(endLabel + ":");
        return null;
    }

    @Override
    public String visitForInit(CppSubsetParser.ForInitContext ctx) {
        if (ctx.variableDeclNoSemi() != null) {
            return visit(ctx.variableDeclNoSemi());
        }
        return visit(ctx.expression());
    }

    @Override
    public String visitForUpdate(CppSubsetParser.ForUpdateContext ctx) {
        for (CppSubsetParser.ExpressionContext expression : ctx.expression()) {
            visit(expression);
        }
        return null;
    }

    @Override
    public String visitBreakStatement(CppSubsetParser.BreakStatementContext ctx) {
        if (!loopStack.isEmpty()) {
            code.add("goto " + loopStack.peek().breakLabel);
        }
        return null;
    }

    @Override
    public String visitContinueStatement(CppSubsetParser.ContinueStatementContext ctx) {
        if (!loopStack.isEmpty()) {
            code.add("goto " + loopStack.peek().continueLabel);
        }
        return null;
    }

    @Override
    public String visitMulDiv(CppSubsetParser.MulDivContext ctx) {
        return binary(ctx.expression(0), ctx.op.getText(), ctx.expression(1));
    }

    @Override
    public String visitAddSub(CppSubsetParser.AddSubContext ctx) {
        return binary(ctx.expression(0), ctx.op.getText(), ctx.expression(1));
    }

    @Override
    public String visitRelational(CppSubsetParser.RelationalContext ctx) {
        return binary(ctx.expression(0), ctx.op.getText(), ctx.expression(1));
    }

    @Override
    public String visitLogical(CppSubsetParser.LogicalContext ctx) {
        return binary(ctx.expression(0), ctx.op.getText(), ctx.expression(1));
    }

    @Override
    public String visitPostIncDec(CppSubsetParser.PostIncDecContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        String op = ctx.PLUSPLUS() != null ? "+" : "-";
        code.add(name + " = " + name + " " + op + " 1");
        return name;
    }

    @Override
    public String visitFuncCall(CppSubsetParser.FuncCallContext ctx) {
        List<String> args = new ArrayList<>();
        if (ctx.argumentList() != null) {
            for (CppSubsetParser.ExpressionContext expression : ctx.argumentList().expression()) {
                args.add(visit(expression));
            }
        }

        for (String arg : args) {
            code.add("arg " + arg);
        }

        String result = newTemp();
        code.add(result + " = call " + ctx.IDENTIFIER().getText() + ", " + args.size());
        return result;
    }

    @Override
    public String visitArrayAccess(CppSubsetParser.ArrayAccessContext ctx) {
        String index = visit(ctx.expression());
        String result = newTemp();
        code.add(result + " = " + ctx.IDENTIFIER().getText() + "[" + index + "]");
        return result;
    }

    @Override
    public String visitId(CppSubsetParser.IdContext ctx) {
        return ctx.IDENTIFIER().getText();
    }

    @Override
    public String visitInt(CppSubsetParser.IntContext ctx) {
        return ctx.INT_LITERAL().getText();
    }

    @Override
    public String visitDouble(CppSubsetParser.DoubleContext ctx) {
        return ctx.DOUBLE_LITERAL().getText();
    }

    @Override
    public String visitFloat(CppSubsetParser.FloatContext ctx) {
        return ctx.FLOAT_LITERAL().getText();
    }

    @Override
    public String visitChar(CppSubsetParser.CharContext ctx) {
        return ctx.CHAR_LITERAL().getText();
    }

    @Override
    public String visitBool(CppSubsetParser.BoolContext ctx) {
        return ctx.BOOL_LITERAL().getText();
    }

    @Override
    public String visitStringLit(CppSubsetParser.StringLitContext ctx) {
        return ctx.STRING_LITERAL().getText();
    }

    @Override
    public String visitDateLit(CppSubsetParser.DateLitContext ctx) {
        return ctx.DATE_LITERAL().getText();
    }

    @Override
    public String visitParens(CppSubsetParser.ParensContext ctx) {
        return visit(ctx.expression());
    }

    private String binary(CppSubsetParser.ExpressionContext leftCtx, String operator, CppSubsetParser.ExpressionContext rightCtx) {
        String left = visit(leftCtx);
        String right = visit(rightCtx);
        String result = newTemp();
        code.add(result + " = " + left + " " + operator + " " + right);
        return result;
    }

    private String parameterNames(CppSubsetParser.ParameterListContext parameterList) {
        if (parameterList == null) {
            return "";
        }

        List<String> names = new ArrayList<>();
        for (CppSubsetParser.ParameterContext parameter : parameterList.parameter()) {
            names.add(parameter.IDENTIFIER().getText());
        }
        return String.join(", ", names);
    }

    private String newTemp() {
        tempCounter++;
        return "t" + tempCounter;
    }

    private String newLabel() {
        labelCounter++;
        return "L" + labelCounter;
    }
}
