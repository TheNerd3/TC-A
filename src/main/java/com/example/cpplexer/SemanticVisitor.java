package com.example.cpplexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Visitor semántico. Devuelve String con el tipo resultante de cada nodo.
 */
public class SemanticVisitor extends CppSubsetParserBaseVisitor<String> {

    // Tabla de símbolos global: nombre -> Symbol
    protected Map<String, SymbolTable.Symbol> symbolTable = new HashMap<>();

    // ─── Helpers ─────────────────────────────────────────────────────────────

    protected void semanticError(String msg) {
        throw new RuntimeException("ERROR SEMANTICO: " + msg);
    }

    protected String resolveType(CppSubsetParser.TypeContext ctx) {
        return ctx.getText();
    }

    protected void registerVariable(String name, String type) {
        symbolTable.put(name, new SymbolTable.Symbol(name, type));
        System.out.println("  [TS] Registrada variable: " + type + " " + name);
    }

    protected void registerArray(String name, String type, int size) {
        symbolTable.put(name, new SymbolTable.Symbol(name, type, size));
        System.out.println("  [TS] Registrado array: " + type + " " + name + "[" + size + "]");
    }

    protected void registerFunction(String name, String returnType, List<String> paramTypes) {
        symbolTable.put(name, new SymbolTable.Symbol(name, returnType, paramTypes));
        System.out.println("  [TS] Registrada función: " + returnType + " " + name +
                "(" + String.join(", ", paramTypes) + ")");
    }

    // ─── Program ─────────────────────────────────────────────────────────────

    @Override
    public String visitProgram(CppSubsetParser.ProgramContext ctx) {
        System.out.println("--- Tabla de Símbolos ---");
        visitChildren(ctx);
        System.out.println("--- Símbolos registrados: " + symbolTable.size() + " ---");
        symbolTable.values().forEach(s -> System.out.println("  " + s));
        return null;
    }

    // ─── Declaraciones globales ───────────────────────────────────────────────

    @Override
    public String visitVariableDecl(CppSubsetParser.VariableDeclContext ctx) {
        String type = resolveType(ctx.type());
        String name = ctx.IDENTIFIER().getText();

        if (ctx.INT_LITERAL() != null) {
            // Declaración de array
            int size = Integer.parseInt(ctx.INT_LITERAL().getText());
            registerArray(name, type, size);
        } else {
            registerVariable(name, type);
        }
        return type;
    }

    @Override
    public String visitFunctionDecl(CppSubsetParser.FunctionDeclContext ctx) {
        String returnType = resolveType(ctx.type());
        String name = ctx.IDENTIFIER().getText();
        List<String> paramTypes = collectParamTypes(ctx.parameterList());
        registerFunction(name, returnType, paramTypes);
        return returnType;
    }

    @Override
    public String visitFunctionDef(CppSubsetParser.FunctionDefContext ctx) {
        String returnType = resolveType(ctx.type());
        String name = ctx.IDENTIFIER().getText();
        List<String> paramTypes = collectParamTypes(ctx.parameterList());
        registerFunction(name, returnType, paramTypes);
        visitChildren(ctx);
        return returnType;
    }

    // ─── Statements ──────────────────────────────────────────────────────────

    @Override
    public String visitAssign(CppSubsetParser.AssignContext ctx) {
        String id = ctx.IDENTIFIER().getText();
        String exprType = visit(ctx.expression());
        System.out.println("  Asignación: " + id + " = <" + exprType + ">");
        return exprType;
    }

    @Override
    public String visitPrintExpr(CppSubsetParser.PrintExprContext ctx) {
        String type = visit(ctx.expression());
        System.out.println("  Expresión de tipo: <" + type + ">");
        return type;
    }

    // ─── Expresiones ─────────────────────────────────────────────────────────

    @Override
    public String visitMulDiv(CppSubsetParser.MulDivContext ctx) {
        String left = visit(ctx.expression(0));
        String right = visit(ctx.expression(1));
        return left != null ? left : right;
    }

    @Override
    public String visitAddSub(CppSubsetParser.AddSubContext ctx) {
        String left = visit(ctx.expression(0));
        String right = visit(ctx.expression(1));
        return left != null ? left : right;
    }

    @Override
    public String visitInt(CppSubsetParser.IntContext ctx) {
        return "int";
    }

    @Override
    public String visitId(CppSubsetParser.IdContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        SymbolTable.Symbol sym = symbolTable.get(name);
        if (sym == null) {
            semanticError("Variable '" + name + "' no declarada.");
        }
        return sym.type;
    }

    @Override
    public String visitParens(CppSubsetParser.ParensContext ctx) {
        return visit(ctx.expression());
    }

    // ─── Utilidades ──────────────────────────────────────────────────────────

    protected List<String> collectParamTypes(CppSubsetParser.ParameterListContext ctx) {
        List<String> types = new ArrayList<>();
        if (ctx != null) {
            for (CppSubsetParser.ParameterContext p : ctx.parameter()) {
                types.add(resolveType(p.type()));
            }
        }
        return types;
    }
}
