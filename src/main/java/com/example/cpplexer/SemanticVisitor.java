package com.example.cpplexer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Visitor semántico con tabla de símbolos por ámbitos (scope stack).
 * Devuelve String con el tipo resultante de cada expresión/nodo.
 */
public class SemanticVisitor extends CppSubsetParserBaseVisitor<String> {

    // Tipos numéricos sobre los que se permiten operaciones aritméticas
    private static final Set<String> NUMERIC = Set.of("int", "double");

    // Pila de ámbitos: tope = ámbito actual, base = ámbito global
    protected Deque<Map<String, SymbolTable.Symbol>> scopeStack = new ArrayDeque<>();

    public SemanticVisitor() {
        // Ámbito global inicial — inline para evitar override call en constructor
        scopeStack.push(new HashMap<>());
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

    protected SymbolTable.Symbol lookup(String name) {
        for (Map<String, SymbolTable.Symbol> scope : scopeStack) {
            if (scope.containsKey(name)) return scope.get(name);
        }
        return null;
    }

    protected SymbolTable.Symbol lookupCurrentScope(String name) {
        return scopeStack.peek().get(name);
    }

    protected void defineInCurrentScope(String name, SymbolTable.Symbol sym) {
        if (lookupCurrentScope(name) != null) {
            semanticError("Redefinición de '" + name + "' en el mismo ámbito.");
        }
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

    /**
     * Reglas de compatibilidad de asignación (C++ implícito).
     * int  <- int      OK
     * int  <- double   ERROR
     * double <- int    OK  (widening)
     * double <- double OK
     * char <- char     OK
     * bool <- bool     OK
     * void <- *        ERROR siempre
     */
    private boolean isAssignCompatible(String declared, String expr) {
        if (declared.equals(expr)) return true;
        // widening numérico: double puede recibir int
        if (declared.equals("double") && expr.equals("int")) return true;
        return false;
    }

    /**
     * Tipo resultado de una operación aritmética entre dos operandos.
     * Retorna null si la combinación no está permitida.
     */
    private String arithmeticResultType(String left, String right) {
        if (!NUMERIC.contains(left) || !NUMERIC.contains(right)) return null;
        // Si alguno es double el resultado es double
        if (left.equals("double") || right.equals("double")) return "double";
        return "int";
    }

    // ─── Program ─────────────────────────────────────────────────────────────

    @Override
    public String visitProgram(CppSubsetParser.ProgramContext ctx) {
        System.out.println("--- Iniciando análisis semántico ---");
        visitChildren(ctx);
        System.out.println("--- Análisis semántico completado ---");
        return null;
    }

    // ─── Declaraciones ───────────────────────────────────────────────────────

    @Override
    public String visitVariableDecl(CppSubsetParser.VariableDeclContext ctx) {
        String type = resolveType(ctx.type());
        String name = ctx.IDENTIFIER().getText();

        SymbolTable.Symbol sym;
        if (ctx.INT_LITERAL() != null) {
            // Declaración de array: validar tamaño > 0
            int size = Integer.parseInt(ctx.INT_LITERAL().getText());
            if (size <= 0) {
                semanticError("El tamaño del array '" + name + "' debe ser mayor a cero (es " + size + ").");
            }
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
        defineInCurrentScope(name, new SymbolTable.Symbol(name, returnType, paramTypes));
        return returnType;
    }

    @Override
    public String visitFunctionDef(CppSubsetParser.FunctionDefContext ctx) {
        String returnType = resolveType(ctx.type());
        String name = ctx.IDENTIFIER().getText();
        List<String> paramTypes = collectParamTypes(ctx.parameterList());

        defineInCurrentScope(name, new SymbolTable.Symbol(name, returnType, paramTypes));

        pushScope();
        if (ctx.parameterList() != null) {
            for (CppSubsetParser.ParameterContext p : ctx.parameterList().parameter()) {
                String pType = resolveType(p.type());
                String pName = p.IDENTIFIER().getText();
                defineInCurrentScope(pName, new SymbolTable.Symbol(pName, pType));
            }
        }

        for (CppSubsetParser.StatementContext stmt : ctx.block().statement()) {
            visit(stmt);
        }

        popScope();
        return returnType;
    }

    // ─── Bloque ───────────────────────────────────────────────────────────────

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

        SymbolTable.Symbol sym = lookup(id);
        if (sym == null) {
            semanticError("Variable '" + id + "' usada sin declarar.");
            return null;
        }

        String exprType = visit(ctx.expression());

        // Compatibilidad de asignación
        if (exprType != null && !isAssignCompatible(sym.type, exprType)) {
            semanticError("Incompatibilidad de tipos en asignación a '" + id +
                    "': se esperaba <" + sym.type + ">, se obtuvo <" + exprType + ">.");
        }

        System.out.println("  Asignación OK: " + sym.type + " " + id + " = <" + exprType + ">");
        return sym.type;
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
        String result = arithmeticResultType(left, right);
        if (result == null) {
            semanticError("Operador '" + ctx.op.getText() + "' no aplicable entre <" +
                    left + "> y <" + right + ">.");
        }
        return result;
    }

    @Override
    public String visitAddSub(CppSubsetParser.AddSubContext ctx) {
        String left = visit(ctx.expression(0));
        String right = visit(ctx.expression(1));
        String result = arithmeticResultType(left, right);
        if (result == null) {
            semanticError("Operador '" + ctx.op.getText() + "' no aplicable entre <" +
                    left + "> y <" + right + ">.");
        }
        return result;
    }

    @Override
    public String visitInt(CppSubsetParser.IntContext ctx) {
        return "int";
    }

    @Override
    public String visitDouble(CppSubsetParser.DoubleContext ctx) {
        return "double";
    }

    @Override
    public String visitChar(CppSubsetParser.CharContext ctx) {
        return "char";
    }

    @Override
    public String visitBool(CppSubsetParser.BoolContext ctx) {
        return "bool";
    }

    @Override
    public String visitStringLit(CppSubsetParser.StringLitContext ctx) {
        return "string";
    }

    @Override
    public String visitId(CppSubsetParser.IdContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        SymbolTable.Symbol sym = lookup(name);
        if (sym == null) {
            semanticError("Variable '" + name + "' no declarada.");
            return null;
        }
        return sym.type;
    }

    @Override
    public String visitParens(CppSubsetParser.ParensContext ctx) {
        return visit(ctx.expression());
    }
}
