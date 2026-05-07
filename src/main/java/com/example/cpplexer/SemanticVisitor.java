package com.example.cpplexer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Visitor semántico con tabla de símbolos por ámbitos (scope stack).
 * Devuelve String con el tipo resultante de cada expresión/nodo.
 */
public class SemanticVisitor extends CppSubsetParserBaseVisitor<String> {

    // Pila de ámbitos: tope = ámbito actual, base = ámbito global
    protected Deque<Map<String, SymbolTable.Symbol>> scopeStack = new ArrayDeque<>();

    public SemanticVisitor() {
        // Ámbito global inicial
        pushScope();
    }

    // ─── Gestión de ámbitos ───────────────────────────────────────────────────

    protected void pushScope() {
        scopeStack.push(new HashMap<>());
        System.out.println("  [SCOPE] Entrar ámbito (nivel " + scopeStack.size() + ")");
    }

    protected void popScope() {
        System.out.println("  [SCOPE] Salir ámbito (nivel " + scopeStack.size() + ")");
        scopeStack.pop();
    }

    /** Busca un símbolo desde el ámbito más interno hacia el global. */
    protected SymbolTable.Symbol lookup(String name) {
        for (Map<String, SymbolTable.Symbol> scope : scopeStack) {
            if (scope.containsKey(name)) return scope.get(name);
        }
        return null;
    }

    /** Busca un símbolo únicamente en el ámbito actual (tope de la pila). */
    protected SymbolTable.Symbol lookupCurrentScope(String name) {
        return scopeStack.peek().get(name);
    }

    /** Registra un símbolo en el ámbito actual. */
    protected void defineInCurrentScope(String name, SymbolTable.Symbol sym) {
        scopeStack.peek().put(name, sym);
        System.out.println("  [TS] " + sym);
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    protected void semanticError(String msg) {
        throw new RuntimeException("ERROR SEMANTICO: " + msg);
    }

    protected String resolveType(CppSubsetParser.TypeContext ctx) {
        return ctx.getText();
    }

    protected List<String> collectParamTypes(CppSubsetParser.ParameterListContext ctx) {
        List<String> types = new ArrayList<>();
        if (ctx != null) {
            for (CppSubsetParser.ParameterContext p : ctx.parameter()) {
                types.add(resolveType(p.type()));
            }
        }
        return types;
    }

    // ─── Program ─────────────────────────────────────────────────────────────

    @Override
    public String visitProgram(CppSubsetParser.ProgramContext ctx) {
        System.out.println("--- Iniciando análisis semántico ---");
        visitChildren(ctx);
        System.out.println("--- Análisis semántico completado ---");
        return null;
    }

    // ─── Declaraciones globales ───────────────────────────────────────────────

    @Override
    public String visitVariableDecl(CppSubsetParser.VariableDeclContext ctx) {
        String type = resolveType(ctx.type());
        String name = ctx.IDENTIFIER().getText();

        SymbolTable.Symbol sym;
        if (ctx.INT_LITERAL() != null) {
            int size = Integer.parseInt(ctx.INT_LITERAL().getText());
            sym = new SymbolTable.Symbol(name, type, size);
        } else {
            sym = new SymbolTable.Symbol(name, type);
        }
        defineInCurrentScope(name, sym);
        return type;
    }

    @Override
    public String visitFunctionDecl(CppSubsetParser.FunctionDeclContext ctx) {
        String returnType = resolveType(ctx.type());
        String name = ctx.IDENTIFIER().getText();
        List<String> paramTypes = collectParamTypes(ctx.parameterList());
        SymbolTable.Symbol sym = new SymbolTable.Symbol(name, returnType, paramTypes);
        defineInCurrentScope(name, sym);
        return returnType;
    }

    @Override
    public String visitFunctionDef(CppSubsetParser.FunctionDefContext ctx) {
        String returnType = resolveType(ctx.type());
        String name = ctx.IDENTIFIER().getText();
        List<String> paramTypes = collectParamTypes(ctx.parameterList());

        // Registrar la función en el ámbito actual ANTES de abrir el bloque
        SymbolTable.Symbol sym = new SymbolTable.Symbol(name, returnType, paramTypes);
        defineInCurrentScope(name, sym);

        // Abrir ámbito del cuerpo de la función y registrar parámetros
        pushScope();
        if (ctx.parameterList() != null) {
            for (CppSubsetParser.ParameterContext p : ctx.parameterList().parameter()) {
                String pType = resolveType(p.type());
                String pName = p.IDENTIFIER().getText();
                defineInCurrentScope(pName, new SymbolTable.Symbol(pName, pType));
            }
        }

        // Visitamos los statements del bloque directamente (sin que visitBlock
        // abra otro ámbito, porque ya abrimos uno para los parámetros)
        for (CppSubsetParser.StatementContext stmt : ctx.block().statement()) {
            visit(stmt);
        }

        popScope();
        return returnType;
    }

    // ─── Bloque ───────────────────────────────────────────────────────────────
    // Un bloque { ... } que NO es el cuerpo directo de una función abre su
    // propio ámbito. Los cuerpos de función ya son manejados en visitFunctionDef.

    @Override
    public String visitBlock(CppSubsetParser.BlockContext ctx) {
        pushScope();
        visitChildren(ctx);
        popScope();
        return null;
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
        SymbolTable.Symbol sym = lookup(name);
        if (sym == null) {
            semanticError("Variable '" + name + "' no declarada.");
        }
        return sym.type;
    }

    @Override
    public String visitParens(CppSubsetParser.ParensContext ctx) {
        return visit(ctx.expression());
    }
}
