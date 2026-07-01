package com.example.cpplexer.ast;

import com.example.cpplexer.CppSubsetParser;
import com.example.cpplexer.CppSubsetParserBaseVisitor;

public class AstBuilder extends CppSubsetParserBaseVisitor<AstNode> {
    @Override
    public AstNode visitProgram(CppSubsetParser.ProgramContext ctx) {
        AstNode root = new AstNode("Program");
        for (CppSubsetParser.GlobalItemContext item : ctx.globalItem()) {
            root.addChild(visit(item));
        }
        return root;
    }

    @Override
    public AstNode visitGlobalItem(CppSubsetParser.GlobalItemContext ctx) {
        if (ctx.variableDecl() != null) {
            return visit(ctx.variableDecl());
        }
        if (ctx.functionDecl() != null) {
            return visit(ctx.functionDecl());
        }
        if (ctx.functionDef() != null) {
            return visit(ctx.functionDef());
        }
        if (ctx.statement() != null) {
            return visit(ctx.statement());
        }
        return null;
    }

    @Override
    public AstNode visitFunctionDecl(CppSubsetParser.FunctionDeclContext ctx) {
        return signatureNode("FunctionDecl", ctx.type().getText(), ctx.IDENTIFIER().getText(), ctx.parameterList());
    }

    @Override
    public AstNode visitFunctionDef(CppSubsetParser.FunctionDefContext ctx) {
        AstNode node = signatureNode("FunctionDef", ctx.type().getText(), ctx.IDENTIFIER().getText(), ctx.parameterList());
        node.addChild(visit(ctx.block()));
        return node;
    }

    @Override
    public AstNode visitParameterList(CppSubsetParser.ParameterListContext ctx) {
        AstNode node = new AstNode("Parameters");
        for (CppSubsetParser.ParameterContext parameter : ctx.parameter()) {
            node.addChild(visit(parameter));
        }
        return node;
    }

    @Override
    public AstNode visitParameter(CppSubsetParser.ParameterContext ctx) {
        return new AstNode("Param " + ctx.IDENTIFIER().getText() + ": " + ctx.type().getText());
    }

    @Override
    public AstNode visitVariableDecl(CppSubsetParser.VariableDeclContext ctx) {
        return variableNode("VarDecl", ctx.type().getText(), ctx.IDENTIFIER().getText(), ctx.LBRACK() != null, ctx.INT_LITERAL() != null ? ctx.INT_LITERAL().getText() : null, ctx.ASSIGN() != null ? visit(ctx.expression()) : null);
    }

    @Override
    public AstNode visitVariableDeclNoSemi(CppSubsetParser.VariableDeclNoSemiContext ctx) {
        return variableNode("VarDecl", ctx.type().getText(), ctx.IDENTIFIER().getText(), ctx.LBRACK() != null, ctx.INT_LITERAL() != null ? ctx.INT_LITERAL().getText() : null, ctx.ASSIGN() != null ? visit(ctx.expression()) : null);
    }

    @Override
    public AstNode visitBlock(CppSubsetParser.BlockContext ctx) {
        AstNode node = new AstNode("Block");
        for (CppSubsetParser.StatementContext statement : ctx.statement()) {
            node.addChild(visit(statement));
        }
        return node;
    }

    @Override
    public AstNode visitAssign(CppSubsetParser.AssignContext ctx) {
        return new AstNode("Assign " + ctx.IDENTIFIER().getText()).addChild(visit(ctx.expression()));
    }

    @Override
    public AstNode visitArrayAssign(CppSubsetParser.ArrayAssignContext ctx) {
        AstNode node = new AstNode("ArrayAssign " + ctx.IDENTIFIER().getText());
        node.addChild(visit(ctx.expression(0)));
        node.addChild(visit(ctx.expression(1)));
        return node;
    }

    @Override
    public AstNode visitReturnExpr(CppSubsetParser.ReturnExprContext ctx) {
        return new AstNode("Return").addChild(visit(ctx.expression()));
    }

    @Override
    public AstNode visitReturnVoid(CppSubsetParser.ReturnVoidContext ctx) {
        return new AstNode("Return");
    }

    @Override
    public AstNode visitPrintExpr(CppSubsetParser.PrintExprContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public AstNode visitVarDeclStmt(CppSubsetParser.VarDeclStmtContext ctx) {
        return visit(ctx.variableDecl());
    }

    @Override
    public AstNode visitBlockStmt(CppSubsetParser.BlockStmtContext ctx) {
        return visit(ctx.block());
    }

    @Override
    public AstNode visitForStmt(CppSubsetParser.ForStmtContext ctx) {
        AstNode node = new AstNode("For");
        node.addChild(ctx.forInit() != null ? visit(ctx.forInit()) : new AstNode("Init"));
        node.addChild(ctx.expression() != null ? visit(ctx.expression()) : new AstNode("Condition"));
        node.addChild(ctx.forUpdate() != null ? visit(ctx.forUpdate()) : new AstNode("Update"));
        node.addChild(visit(ctx.statement()));
        return node;
    }

    @Override
    public AstNode visitForInit(CppSubsetParser.ForInitContext ctx) {
        if (ctx.variableDeclNoSemi() != null) {
            return visit(ctx.variableDeclNoSemi());
        }
        return visit(ctx.expression());
    }

    @Override
    public AstNode visitForUpdate(CppSubsetParser.ForUpdateContext ctx) {
        AstNode node = new AstNode("Update");
        for (CppSubsetParser.ExpressionContext expression : ctx.expression()) {
            node.addChild(visit(expression));
        }
        return node;
    }

    @Override
    public AstNode visitIfStmt(CppSubsetParser.IfStmtContext ctx) {
        return visit(ctx.ifStatement());
    }

    @Override
    public AstNode visitIfStatement(CppSubsetParser.IfStatementContext ctx) {
        AstNode node = new AstNode("If");
        node.addChild(visit(ctx.expression()));
        node.addChild(visit(ctx.statement(0)));
        if (ctx.statement().size() > 1) {
            node.addChild(visit(ctx.statement(1)));
        }
        return node;
    }

    @Override
    public AstNode visitWhileStmt(CppSubsetParser.WhileStmtContext ctx) {
        return visit(ctx.whileStatement());
    }

    @Override
    public AstNode visitWhileStatement(CppSubsetParser.WhileStatementContext ctx) {
        return new AstNode("While")
                .addChild(visit(ctx.expression()))
                .addChild(visit(ctx.statement()));
    }

    @Override
    public AstNode visitBreakStmt(CppSubsetParser.BreakStmtContext ctx) {
        return new AstNode("Break");
    }

    @Override
    public AstNode visitContinueStmt(CppSubsetParser.ContinueStmtContext ctx) {
        return new AstNode("Continue");
    }

    @Override
    public AstNode visitEmptyStmt(CppSubsetParser.EmptyStmtContext ctx) {
        return new AstNode("Empty");
    }

    @Override
    public AstNode visitMulDiv(CppSubsetParser.MulDivContext ctx) {
        return binary("BinOp " + ctx.op.getText(), visit(ctx.expression(0)), visit(ctx.expression(1)));
    }

    @Override
    public AstNode visitAddSub(CppSubsetParser.AddSubContext ctx) {
        return binary("BinOp " + ctx.op.getText(), visit(ctx.expression(0)), visit(ctx.expression(1)));
    }

    @Override
    public AstNode visitRelational(CppSubsetParser.RelationalContext ctx) {
        return binary("Compare " + ctx.op.getText(), visit(ctx.expression(0)), visit(ctx.expression(1)));
    }

    @Override
    public AstNode visitLogical(CppSubsetParser.LogicalContext ctx) {
        return binary("Logical " + ctx.op.getText(), visit(ctx.expression(0)), visit(ctx.expression(1)));
    }

    @Override
    public AstNode visitPostIncDec(CppSubsetParser.PostIncDecContext ctx) {
        return new AstNode("PostIncDec " + ctx.IDENTIFIER().getText() + (ctx.PLUSPLUS() != null ? "++" : "--"));
    }

    @Override
    public AstNode visitFuncCall(CppSubsetParser.FuncCallContext ctx) {
        AstNode node = new AstNode("Call " + ctx.IDENTIFIER().getText());
        if (ctx.argumentList() != null) {
            node.addChild(visit(ctx.argumentList()));
        }
        return node;
    }

    @Override
    public AstNode visitArgumentList(CppSubsetParser.ArgumentListContext ctx) {
        AstNode node = new AstNode("Args");
        for (CppSubsetParser.ExpressionContext expression : ctx.expression()) {
            node.addChild(visit(expression));
        }
        return node;
    }

    @Override
    public AstNode visitArrayAccess(CppSubsetParser.ArrayAccessContext ctx) {
        return new AstNode("ArrayAccess " + ctx.IDENTIFIER().getText()).addChild(visit(ctx.expression()));
    }

    @Override
    public AstNode visitId(CppSubsetParser.IdContext ctx) {
        return new AstNode("Id " + ctx.IDENTIFIER().getText());
    }

    @Override
    public AstNode visitInt(CppSubsetParser.IntContext ctx) {
        return new AstNode("Int " + ctx.INT_LITERAL().getText());
    }

    @Override
    public AstNode visitDouble(CppSubsetParser.DoubleContext ctx) {
        return new AstNode("Double " + ctx.DOUBLE_LITERAL().getText());
    }

    @Override
    public AstNode visitFloat(CppSubsetParser.FloatContext ctx) {
        return new AstNode("Float " + ctx.FLOAT_LITERAL().getText());
    }

    @Override
    public AstNode visitChar(CppSubsetParser.CharContext ctx) {
        return new AstNode("Char " + ctx.CHAR_LITERAL().getText());
    }

    @Override
    public AstNode visitBool(CppSubsetParser.BoolContext ctx) {
        return new AstNode("Bool " + ctx.BOOL_LITERAL().getText());
    }

    @Override
    public AstNode visitStringLit(CppSubsetParser.StringLitContext ctx) {
        return new AstNode("String " + ctx.STRING_LITERAL().getText());
    }

    @Override
    public AstNode visitDateLit(CppSubsetParser.DateLitContext ctx) {
        return new AstNode("Date " + ctx.DATE_LITERAL().getText());
    }

    @Override
    public AstNode visitParens(CppSubsetParser.ParensContext ctx) {
        return visit(ctx.expression());
    }

    private AstNode signatureNode(String kind, String returnType, String name, CppSubsetParser.ParameterListContext parameterList) {
        AstNode node = new AstNode(kind + " " + name + ": " + returnType);
        if (parameterList != null) {
            node.addChild(visit(parameterList));
        }
        return node;
    }

    private AstNode variableNode(String kind, String type, String name, boolean isArray, String size, AstNode initializer) {
        StringBuilder label = new StringBuilder(kind).append(' ').append(name).append(": ").append(type);
        if (isArray) {
            label.append('[').append(size).append(']');
        }
        AstNode node = new AstNode(label.toString());
        node.addChild(initializer);
        return node;
    }

    private AstNode binary(String label, AstNode left, AstNode right) {
        return new AstNode(label)
                .addChild(left)
                .addChild(right);
    }
}