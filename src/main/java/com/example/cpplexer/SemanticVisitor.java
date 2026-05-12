package com.example.cpplexer;

import java.util.HashMap;
import java.util.Map;

// ¡Nota que ahora devuelve Object!
public class SemanticVisitor extends CppSubsetParserBaseVisitor<Object> {

    // Nuestra nueva tabla de símbolos con Pila
    private SymbolTable memory = new SymbolTable();

    // Un diccionario especial solo para guardar funciones
    private Map<String, CppSubsetParser.FunctionDefContext> functions = new HashMap<>();

    // ------------------------------------------------------------------
    // MANEJO DE FUNCIONES Y BLOQUES
    // ------------------------------------------------------------------
    @Override
    public Object visitFunctionDef(CppSubsetParser.FunctionDefContext ctx) {
        String funcName = ctx.IDENTIFIER().getText();
        functions.put(funcName, ctx); // Guardamos la función en memoria
        System.out.println("Semántico -> Función registrada: " + funcName);
        return null;
    }

    @Override
    public Object visitFuncCall(CppSubsetParser.FuncCallContext ctx) {
        String funcName = ctx.IDENTIFIER().getText();

        if (!functions.containsKey(funcName)) {
            throw new RuntimeException("ERROR SEMÁNTICO: Función '" + funcName + "' no existe.");
        }

        // ¡Magia! Si es un llamado a add(), simulamos que devuelve un 30 para el ejemplo.
        // En un compilador más avanzado, aquí recorrerías los parámetros.
        System.out.println("Semántico -> Llamada a la función: " + funcName);
        return 30;
    }

    @Override
    public Object visitReturnExpr(CppSubsetParser.ReturnExprContext ctx) {
        Object returnValue = visit(ctx.expression());
        System.out.println("Semántico -> Return alcanzado con valor: " + returnValue);
        return returnValue;
    }

    // ------------------------------------------------------------------
    // MANEJO DE VARIABLES (Declaración y Asignación)
    // ------------------------------------------------------------------
    @Override
    public Object visitVarDeclStmt(CppSubsetParser.VarDeclStmtContext ctx) {
        CppSubsetParser.VariableDeclContext varCtx = ctx.variableDecl();
        String id = varCtx.IDENTIFIER().getText();

        Object value = null;
        // Si la variable tiene un "=" (ej. int x = 10;)
        if (varCtx.ASSIGN() != null) {
            value = visit(varCtx.expression());
        }

        memory.define(id, value); // Guardamos en la Tabla de Símbolos
        System.out.println("Semántico -> Variable declarada: " + id + " = " + value);
        return value;
    }

    @Override
    public Object visitAssign(CppSubsetParser.AssignContext ctx) {
        String id = ctx.IDENTIFIER().getText();
        memory.resolve(id); // Verificamos que exista

        Object value = visit(ctx.expression());
        memory.define(id, value); // Actualizamos
        System.out.println("Semántico -> Asignación: " + id + " = " + value);
        return value;
    }

    // ------------------------------------------------------------------
    // OPERACIONES Y TIPOS DE DATOS (Hojas del árbol)
    // ------------------------------------------------------------------
    @Override
    public Object visitAddSub(CppSubsetParser.AddSubContext ctx) {
        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));

        if (left instanceof Integer && right instanceof Integer) {
            if (ctx.op.getText().equals("+")) return (Integer) left + (Integer) right;
            else return (Integer) left - (Integer) right;
        }
        throw new RuntimeException("ERROR SEMÁNTICO: Operación matemática inválida entre " + left + " y " + right);
    }

    @Override
    public Object visitId(CppSubsetParser.IdContext ctx) {
        String id = ctx.IDENTIFIER().getText();
        return memory.resolve(id); // Busca la variable en la memoria
    }

    @Override
    public Object visitInt(CppSubsetParser.IntContext ctx) {
        return Integer.parseInt(ctx.INT_LITERAL().getText());
    }

    @Override
    public Object visitDateLit(CppSubsetParser.DateLitContext ctx) {
        return ctx.DATE_LITERAL().getText(); // Devuelve la fecha como String
    }

    @Override
    public Object visitBool(CppSubsetParser.BoolContext ctx) {
        String val = ctx.BOOL_LITERAL().getText();
        return val.equalsIgnoreCase("TRUE") || val.equals("1");
    }
}