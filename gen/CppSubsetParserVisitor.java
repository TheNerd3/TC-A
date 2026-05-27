// Generated from C:/Users/fgome/IdeaProjects/TC-A/src/main/antlr4/CppSubsetParser.g4 by ANTLR 4.13.2

    package com.example.cpplexer;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CppSubsetParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CppSubsetParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CppSubsetParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(CppSubsetParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link CppSubsetParser#globalItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobalItem(CppSubsetParser.GlobalItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link CppSubsetParser#functionDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDecl(CppSubsetParser.FunctionDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link CppSubsetParser#functionDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDef(CppSubsetParser.FunctionDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link CppSubsetParser#parameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterList(CppSubsetParser.ParameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link CppSubsetParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(CppSubsetParser.ParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link CppSubsetParser#variableDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDecl(CppSubsetParser.VariableDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link CppSubsetParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(CppSubsetParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link CppSubsetParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(CppSubsetParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link CppSubsetParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(CppSubsetParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CppSubsetParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(CppSubsetParser.ExpressionContext ctx);
}