package com.example.cpplexer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;

public class SemanticVisitor extends CppSubsetParserBaseVisitor<String> {

    private static final String TYPE_INT = "int";
    private static final String TYPE_DOUBLE = "double";
    private static final String TYPE_CHAR = "char";
    private static final String TYPE_BOOL = "bool";
    private static final String TYPE_VOID = "void";
    private static final String TYPE_DATE = "date";
    private static final String TYPE_STRING = "string";
    private static final String TYPE_ERROR = "<error>";

    private static class FunctionSymbol {
        private final String name;
        private final String returnType;
        private final List<String> paramTypes;
        private boolean defined;

        FunctionSymbol(String name, String returnType, List<String> paramTypes, boolean defined) {
            this.name = name;
            this.returnType = returnType;
            this.paramTypes = paramTypes;
            this.defined = defined;
        }
    }

    private final SymbolTable symbolTable = new SymbolTable();
    private final Map<String, FunctionSymbol> functions = new LinkedHashMap<>();
    private final List<SymbolTable.Symbol> declaredSymbols = new ArrayList<>();
    private final List<String> errors = new ArrayList<>();
    private final List<String> warnings = new ArrayList<>();

    private String currentFunction = null;
    private String currentFunctionReturnType = null;
    private boolean currentFunctionHasReturn = false;
    private int loopDepth = 0;

    public List<String> getErrors() {
        return errors;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }

    @Override
    public String visitProgram(CppSubsetParser.ProgramContext ctx) {
        for (CppSubsetParser.GlobalItemContext item : ctx.globalItem()) {
            if (item.variableDecl() != null) {
                visit(item.variableDecl());
            } else if (item.statement() instanceof CppSubsetParser.VarDeclStmtContext varDeclStmt) {
                visit(varDeclStmt.variableDecl());
            } else if (item.functionDecl() != null) {
                registerFunction(
                        normalizeType(item.functionDecl().type().getText()),
                        item.functionDecl().IDENTIFIER().getText(),
                        extractParamTypes(item.functionDecl().parameterList()),
                        false,
                        item.functionDecl());
            } else if (item.functionDef() != null) {
                registerFunction(
                        normalizeType(item.functionDef().type().getText()),
                        item.functionDef().IDENTIFIER().getText(),
                        extractParamTypes(item.functionDef().parameterList()),
                        true,
                        item.functionDef());
            }
        }

        for (CppSubsetParser.GlobalItemContext item : ctx.globalItem()) {
            if (item.functionDef() != null) {
                visit(item.functionDef());
            } else if (item.statement() != null && !(item.statement() instanceof CppSubsetParser.VarDeclStmtContext)) {
                visit(item.statement());
            }
        }

        if (!functions.containsKey("main")) {
            warning(ctx, "no se encontró la función 'main'.");
        }

        for (FunctionSymbol function : functions.values()) {
            if (!function.defined) {
                warning(ctx, "función declarada pero no definida: '" + function.name + "'.");
            }
        }

        reportUnusedDeclarations();
        return null;
    }

    @Override
    public String visitFunctionDef(CppSubsetParser.FunctionDefContext ctx) {
        String functionName = ctx.IDENTIFIER().getText();
        FunctionSymbol function = functions.get(functionName);
        if (function == null) {
            return null;
        }

        String previousFunction = currentFunction;
        String previousReturnType = currentFunctionReturnType;
        boolean previousHasReturn = currentFunctionHasReturn;

        currentFunction = functionName;
        currentFunctionReturnType = function.returnType;
        currentFunctionHasReturn = false;

        symbolTable.enterScope();
        if (ctx.parameterList() != null) {
            for (CppSubsetParser.ParameterContext parameter : ctx.parameterList().parameter()) {
                String paramType = normalizeType(parameter.type().getText());
                String paramName = parameter.IDENTIFIER().getText();

                if (TYPE_VOID.equals(paramType)) {
                    error(parameter, "parámetro '" + paramName + "' no puede tener tipo void.");
                    continue;
                }

                if (symbolTable.existsInCurrentScope(paramName)) {
                    error(parameter, "parámetro duplicado: '" + paramName + "'.");
                    continue;
                }

                SymbolTable.Symbol symbol = symbolTable.defineVariable(paramName, paramType, true);
                declaredSymbols.add(symbol);
            }
        }

        visit(ctx.block());

        if (!TYPE_VOID.equals(function.returnType) && !currentFunctionHasReturn) {
            error(ctx, "la función '" + functionName + "' debe retornar un valor de tipo " + function.returnType + ".");
        }

        symbolTable.exitScope();

        currentFunction = previousFunction;
        currentFunctionReturnType = previousReturnType;
        currentFunctionHasReturn = previousHasReturn;
        return null;
    }

    @Override
    public String visitBlock(CppSubsetParser.BlockContext ctx) {
        symbolTable.enterScope();
        for (CppSubsetParser.StatementContext statement : ctx.statement()) {
            visit(statement);
        }
        symbolTable.exitScope();
        return null;
    }

    @Override
    public String visitVariableDecl(CppSubsetParser.VariableDeclContext ctx) {
        String declaredType = normalizeType(ctx.type().getText());
        String name = ctx.IDENTIFIER().getText();

        if (TYPE_VOID.equals(declaredType)) {
            error(ctx, "la variable '" + name + "' no puede ser de tipo void.");
            return TYPE_ERROR;
        }

        if (symbolTable.existsInCurrentScope(name)) {
            error(ctx, "identificador redeclarado en el mismo ámbito: '" + name + "'.");
            return TYPE_ERROR;
        }

        if (functions.containsKey(name)) {
            error(ctx, "'" + name + "' ya fue declarado como función y no puede reutilizarse como variable.");
            return TYPE_ERROR;
        }

        boolean hasInitializer = ctx.ASSIGN() != null;
        boolean isArray = ctx.LBRACK() != null;

        if (isArray) {
            int size = Integer.parseInt(ctx.INT_LITERAL().getText());
            if (size <= 0) {
                error(ctx, "el arreglo '" + name + "' debe tener tamaño mayor a 0.");
            }

            SymbolTable.Symbol symbol = symbolTable.defineArray(name, declaredType, size, hasInitializer);
            declaredSymbols.add(symbol);

            if (hasInitializer) {
                warning(ctx, "la inicialización directa de arreglos no está soportada en este subconjunto; use asignaciones por índice.");
            }
            return declaredType;
        }

        SymbolTable.Symbol symbol = symbolTable.defineVariable(name, declaredType, false);
        declaredSymbols.add(symbol);

        if (hasInitializer) {
            String expressionType = visit(ctx.expression());
            if (!isAssignable(declaredType, expressionType)) {
                error(ctx, "asignación incompatible para '" + name + "': se esperaba " + declaredType + " y se obtuvo " + expressionType + ".");
            } else {
                symbol.setInitialized(true);
            }
        }

        return declaredType;
    }

    @Override
    public String visitVarDeclStmt(CppSubsetParser.VarDeclStmtContext ctx) {
        return visit(ctx.variableDecl());
    }

    @Override
    public String visitAssign(CppSubsetParser.AssignContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        SymbolTable.Symbol symbol = symbolTable.resolve(name);

        if (symbol == null) {
            if (functions.containsKey(name)) {
                error(ctx, "'" + name + "' es una función y no puede usarse como variable.");
            } else {
                error(ctx, "variable no declarada: '" + name + "'.");
            }
            return TYPE_ERROR;
        }

        if (symbol.isArray()) {
            error(ctx, "asignación inválida: '" + name + "' es un arreglo y requiere índice.");
            return TYPE_ERROR;
        }

        String expressionType = visit(ctx.expression());
        if (!isAssignable(symbol.getType(), expressionType)) {
            error(ctx, "tipos incompatibles en asignación a '" + name + "': " + symbol.getType() + " <- " + expressionType + ".");
            return TYPE_ERROR;
        }

        symbol.setInitialized(true);
        return symbol.getType();
    }

    @Override
    public String visitArrayAssign(CppSubsetParser.ArrayAssignContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        SymbolTable.Symbol symbol = symbolTable.resolve(name);

        if (symbol == null) {
            error(ctx, "arreglo no declarado: '" + name + "'.");
            return TYPE_ERROR;
        }

        if (!symbol.isArray()) {
            error(ctx, "uso inválido de índice: '" + name + "' no es un arreglo.");
            return TYPE_ERROR;
        }

        String indexType = visit(ctx.expression(0));
        if (!TYPE_INT.equals(indexType)) {
            error(ctx, "el índice del arreglo '" + name + "' debe ser de tipo int.");
        }

        String valueType = visit(ctx.expression(1));
        if (!isAssignable(symbol.getType(), valueType)) {
            error(ctx, "tipos incompatibles en asignación de arreglo '" + name + "': " + symbol.getType() + " <- " + valueType + ".");
        } else {
            symbol.setInitialized(true);
        }

        return symbol.getType();
    }

    @Override
    public String visitReturnExpr(CppSubsetParser.ReturnExprContext ctx) {
        if (currentFunction == null) {
            error(ctx, "sentencia return fuera de una función.");
            return TYPE_ERROR;
        }

        String returnType = visit(ctx.expression());
        currentFunctionHasReturn = true;

        if (TYPE_VOID.equals(currentFunctionReturnType)) {
            error(ctx, "la función '" + currentFunction + "' es void y no debe retornar valor.");
            return TYPE_ERROR;
        }

        if (!isAssignable(currentFunctionReturnType, returnType)) {
            error(ctx, "tipo de retorno inválido en función '" + currentFunction + "': se esperaba "
                    + currentFunctionReturnType + " y se obtuvo " + returnType + ".");
        }

        return currentFunctionReturnType;
    }

    @Override
    public String visitReturnVoid(CppSubsetParser.ReturnVoidContext ctx) {
        if (currentFunction == null) {
            error(ctx, "sentencia return fuera de una función.");
            return TYPE_ERROR;
        }

        currentFunctionHasReturn = true;
        if (!TYPE_VOID.equals(currentFunctionReturnType)) {
            error(ctx, "la función '" + currentFunction + "' debe retornar " + currentFunctionReturnType + ".");
        }
        return TYPE_VOID;
    }

    @Override
    public String visitIfStatement(CppSubsetParser.IfStatementContext ctx) {
        if (currentFunction == null) {
            error(ctx, "sentencia if fuera de una función.");
        }

        String conditionType = visit(ctx.expression());
        if (!TYPE_BOOL.equals(conditionType) && !TYPE_ERROR.equals(conditionType)) {
            error(ctx, "la condición de if debe ser de tipo bool.");
        }

        visit(ctx.statement(0));
        if (ctx.statement().size() > 1) {
            visit(ctx.statement(1));
        }
        return null;
    }

    @Override
    public String visitWhileStatement(CppSubsetParser.WhileStatementContext ctx) {
        if (currentFunction == null) {
            error(ctx, "sentencia while fuera de una función.");
        }

        String conditionType = visit(ctx.expression());
        if (!TYPE_BOOL.equals(conditionType) && !TYPE_ERROR.equals(conditionType)) {
            error(ctx, "la condición de while debe ser de tipo bool.");
        }

        loopDepth++;
        visit(ctx.statement());
        loopDepth--;
        return null;
    }

    @Override
    public String visitForStmt(CppSubsetParser.ForStmtContext ctx) {
        if (currentFunction == null) {
            error(ctx, "sentencia for fuera de una función.");
        }

        symbolTable.enterScope();

        if (ctx.forInit() != null) {
            visit(ctx.forInit());
        }

        if (ctx.expression() != null) {
            String conditionType = visit(ctx.expression());
            if (!TYPE_BOOL.equals(conditionType) && !TYPE_ERROR.equals(conditionType)) {
                error(ctx, "la condición de for debe ser de tipo bool.");
            }
        }

        if (ctx.forUpdate() != null) {
            visit(ctx.forUpdate());
        }

        loopDepth++;
        visit(ctx.statement());
        loopDepth--;

        symbolTable.exitScope();
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
    public String visitVariableDeclNoSemi(CppSubsetParser.VariableDeclNoSemiContext ctx) {
        String declaredType = normalizeType(ctx.type().getText());
        String name = ctx.IDENTIFIER().getText();

        if (TYPE_VOID.equals(declaredType)) {
            error(ctx, "la variable '" + name + "' no puede ser de tipo void.");
            return TYPE_ERROR;
        }

        if (symbolTable.existsInCurrentScope(name)) {
            error(ctx, "identificador redeclarado en el mismo ámbito: '" + name + "'.");
            return TYPE_ERROR;
        }

        if (functions.containsKey(name)) {
            error(ctx, "'" + name + "' ya fue declarado como función y no puede reutilizarse como variable.");
            return TYPE_ERROR;
        }

        boolean hasInitializer = ctx.ASSIGN() != null;
        boolean isArray = ctx.LBRACK() != null;

        if (isArray) {
            int size = Integer.parseInt(ctx.INT_LITERAL().getText());
            SymbolTable.Symbol symbol = symbolTable.defineArray(name, declaredType, size, hasInitializer);
            declaredSymbols.add(symbol);

            if (hasInitializer) {
                warning(ctx, "la inicialización directa de arreglos no está soportada en este subconjunto; use asignaciones por índice.");
            }
            return declaredType;
        }

        SymbolTable.Symbol symbol = symbolTable.defineVariable(name, declaredType, false);
        declaredSymbols.add(symbol);

        if (hasInitializer) {
            String expressionType = visit(ctx.expression());
            if (!isAssignable(declaredType, expressionType)) {
                error(ctx, "asignación incompatible para '" + name + "': se esperaba " + declaredType + " y se obtuvo " + expressionType + ".");
            } else {
                symbol.setInitialized(true);
            }
        }

        return declaredType;
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
        if (loopDepth == 0) {
            error(ctx, "sentencia break fuera de un bucle.");
        }
        return null;
    }

    @Override
    public String visitContinueStatement(CppSubsetParser.ContinueStatementContext ctx) {
        if (loopDepth == 0) {
            error(ctx, "sentencia continue fuera de un bucle.");
        }
        return null;
    }

    @Override
    public String visitPrintExpr(CppSubsetParser.PrintExprContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public String visitMulDiv(CppSubsetParser.MulDivContext ctx) {
        String left = visit(ctx.expression(0));
        String right = visit(ctx.expression(1));

        if (!isNumeric(left) || !isNumeric(right)) {
            error(ctx, "operación aritmética inválida entre " + left + " y " + right + ".");
            return TYPE_ERROR;
        }

        if ("%".equals(ctx.op.getText()) && (!isIntegral(left) || !isIntegral(right))) {
            error(ctx, "el operador % solo acepta operandos enteros.");
            return TYPE_ERROR;
        }

        return promotedNumericType(left, right);
    }

    @Override
    public String visitAddSub(CppSubsetParser.AddSubContext ctx) {
        String left = visit(ctx.expression(0));
        String right = visit(ctx.expression(1));

        if (!isNumeric(left) || !isNumeric(right)) {
            error(ctx, "operación aritmética inválida entre " + left + " y " + right + ".");
            return TYPE_ERROR;
        }

        return promotedNumericType(left, right);
    }

    @Override
    public String visitRelational(CppSubsetParser.RelationalContext ctx) {
        String left = visit(ctx.expression(0));
        String right = visit(ctx.expression(1));
        String op = ctx.op.getText();

        if ("==".equals(op) || "!=".equals(op)) {
            if (!isAssignable(left, right) && !isAssignable(right, left)) {
                error(ctx, "comparación inválida entre " + left + " y " + right + ".");
            }
            return TYPE_BOOL;
        }

        if (!isNumeric(left) || !isNumeric(right)) {
            error(ctx, "operación relacional inválida entre " + left + " y " + right + ".");
            return TYPE_ERROR;
        }

        return TYPE_BOOL;
    }

    @Override
    public String visitLogical(CppSubsetParser.LogicalContext ctx) {
        String left = visit(ctx.expression(0));
        String right = visit(ctx.expression(1));

        if (!TYPE_BOOL.equals(left) || !TYPE_BOOL.equals(right)) {
            error(ctx, "operación lógica inválida entre " + left + " y " + right + ".");
            return TYPE_ERROR;
        }

        return TYPE_BOOL;
    }

    @Override
    public String visitPostIncDec(CppSubsetParser.PostIncDecContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        SymbolTable.Symbol symbol = symbolTable.resolve(name);

        if (symbol == null) {
            error(ctx, "variable no declarada: '" + name + "'.");
            return TYPE_ERROR;
        }

        if (symbol.isArray()) {
            error(ctx, "operación inválida: '" + name + "' es un arreglo.");
            return TYPE_ERROR;
        }

        if (!isNumeric(symbol.getType())) {
            error(ctx, "operación " + ctx.getText() + " solo permitida sobre tipos numéricos.");
            return TYPE_ERROR;
        }

        symbol.markUsed();
        symbol.setInitialized(true);
        return symbol.getType();
    }

    @Override
    public String visitFuncCall(CppSubsetParser.FuncCallContext ctx) {
        String functionName = ctx.IDENTIFIER().getText();
        FunctionSymbol function = functions.get(functionName);
        if (function == null) {
            error(ctx, "llamada a función no declarada: '" + functionName + "'.");
            return TYPE_ERROR;
        }

        List<CppSubsetParser.ExpressionContext> args = new ArrayList<>();
        if (ctx.argumentList() != null) {
            args.addAll(ctx.argumentList().expression());
        }

        if (args.size() != function.paramTypes.size()) {
            error(ctx, "cantidad de argumentos inválida en llamada a '" + functionName + "': se esperaba "
                    + function.paramTypes.size() + " y se recibió " + args.size() + ".");
        }

        int checkedCount = Math.min(args.size(), function.paramTypes.size());
        for (int i = 0; i < checkedCount; i++) {
            String argType = visit(args.get(i));
            String expected = function.paramTypes.get(i);
            if (!isAssignable(expected, argType)) {
                error(args.get(i), "tipo de argumento inválido en posición " + (i + 1) + " para '"
                        + functionName + "': se esperaba " + expected + " y se obtuvo " + argType + ".");
            }
        }

        return function.returnType;
    }

    @Override
    public String visitArrayAccess(CppSubsetParser.ArrayAccessContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        SymbolTable.Symbol symbol = symbolTable.resolve(name);

        if (symbol == null) {
            error(ctx, "arreglo no declarado: '" + name + "'.");
            return TYPE_ERROR;
        }

        if (!symbol.isArray()) {
            error(ctx, "uso inválido de índice: '" + name + "' no es un arreglo.");
            return TYPE_ERROR;
        }

        String indexType = visit(ctx.expression());
        if (!TYPE_INT.equals(indexType)) {
            error(ctx, "el índice del arreglo '" + name + "' debe ser de tipo int.");
        }

        symbol.markUsed();
        if (!symbol.isInitialized()) {
            warning(ctx, "uso de arreglo potencialmente no inicializado: '" + name + "'.");
        }

        return symbol.getType();
    }

    @Override
    public String visitId(CppSubsetParser.IdContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        SymbolTable.Symbol symbol = symbolTable.resolve(name);

        if (symbol == null) {
            if (functions.containsKey(name)) {
                error(ctx, "'" + name + "' es una función y no puede usarse como variable.");
            } else {
                error(ctx, "variable no declarada: '" + name + "'.");
            }
            return TYPE_ERROR;
        }

        if (symbol.isArray()) {
            error(ctx, "uso inválido: '" + name + "' es un arreglo; use índice.");
            return TYPE_ERROR;
        }

        symbol.markUsed();
        if (!symbol.isInitialized()) {
            warning(ctx, "uso de variable potencialmente no inicializada: '" + name + "'.");
        }

        return symbol.getType();
    }

    @Override
    public String visitInt(CppSubsetParser.IntContext ctx) {
        return TYPE_INT;
    }

    @Override
    public String visitDouble(CppSubsetParser.DoubleContext ctx) {
        return TYPE_DOUBLE;
    }

    @Override
    public String visitFloat(CppSubsetParser.FloatContext ctx) {
        return TYPE_DOUBLE;
    }

    @Override
    public String visitChar(CppSubsetParser.CharContext ctx) {
        return TYPE_CHAR;
    }

    @Override
    public String visitBool(CppSubsetParser.BoolContext ctx) {
        return TYPE_BOOL;
    }

    @Override
    public String visitStringLit(CppSubsetParser.StringLitContext ctx) {
        return TYPE_STRING;
    }

    @Override
    public String visitDateLit(CppSubsetParser.DateLitContext ctx) {
        return TYPE_DATE;
    }

    @Override
    public String visitParens(CppSubsetParser.ParensContext ctx) {
        return visit(ctx.expression());
    }

    private List<String> extractParamTypes(CppSubsetParser.ParameterListContext parameterList) {
        List<String> types = new ArrayList<>();
        if (parameterList == null) {
            return types;
        }

        for (CppSubsetParser.ParameterContext parameter : parameterList.parameter()) {
            types.add(normalizeType(parameter.type().getText()));
        }
        return types;
    }

    private void registerFunction(String returnType, String functionName, List<String> paramTypes, boolean definition,
                                  ParserRuleContext ctx) {
        if (functions.containsKey(functionName)) {
            FunctionSymbol existing = functions.get(functionName);
            if (!existing.returnType.equals(returnType) || !sameParams(existing.paramTypes, paramTypes)) {
                error(ctx, "firma incompatible para función '" + functionName + "'.");
                return;
            }

            if (definition && existing.defined) {
                error(ctx, "redefinición de función: '" + functionName + "'.");
                return;
            }

            if (definition) {
                existing.defined = true;
            }
            return;
        }

        if (symbolTable.resolve(functionName) != null) {
            error(ctx, "'" + functionName + "' ya fue declarado como variable y no puede reutilizarse como función.");
            return;
        }

        functions.put(functionName, new FunctionSymbol(functionName, returnType, paramTypes, definition));
    }

    private boolean sameParams(List<String> a, List<String> b) {
        if (a.size() != b.size()) {
            return false;
        }

        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i))) {
                return false;
            }
        }

        return true;
    }

    private String normalizeType(String type) {
        if ("float".equals(type)) {
            return TYPE_DOUBLE;
        }
        if ("string".equals(type)) {
            return TYPE_STRING;
        }
        return type;
    }

    private boolean isAssignable(String target, String source) {
        if (TYPE_ERROR.equals(target) || TYPE_ERROR.equals(source)) {
            return true;
        }

        if (target.equals(source)) {
            return true;
        }

        if (TYPE_DOUBLE.equals(target) && (TYPE_INT.equals(source) || TYPE_CHAR.equals(source))) {
            return true;
        }

        if (TYPE_INT.equals(target) && TYPE_CHAR.equals(source)) {
            return true;
        }

        return false;
    }

    private boolean isNumeric(String type) {
        return TYPE_INT.equals(type) || TYPE_DOUBLE.equals(type) || TYPE_CHAR.equals(type);
    }

    private boolean isIntegral(String type) {
        return TYPE_INT.equals(type) || TYPE_CHAR.equals(type);
    }

    private String promotedNumericType(String left, String right) {
        if (TYPE_DOUBLE.equals(left) || TYPE_DOUBLE.equals(right)) {
            return TYPE_DOUBLE;
        }
        return TYPE_INT;
    }

    private void reportUnusedDeclarations() {
        for (SymbolTable.Symbol symbol : declaredSymbols) {
            if (symbol.getUsages() == 0) {
                warning(null, "identificador declarado pero no usado: '" + symbol.getName() + "'.");
            }
        }
    }

    private void error(ParserRuleContext ctx, String message) {
        if (ctx == null) {
            errors.add(message);
            return;
        }

        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        errors.add("error(" + line + ":" + col + "): " + message);
    }

    private void warning(ParserRuleContext ctx, String message) {
        if (ctx == null) {
            warnings.add("warning: " + message);
            return;
        }

        int line = ctx.getStart().getLine();
        int col = ctx.getStart().getCharPositionInLine();
        warnings.add("warning(" + line + ":" + col + "): " + message);
    }
}
