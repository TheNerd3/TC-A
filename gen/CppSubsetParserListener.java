// Generated from C:/Users/fgome/IdeaProjects/TC-A/src/main/antlr4/CppSubsetParser.g4 by ANTLR 4.13.2

    package com.example.cpplexer;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CppSubsetParser}.
 */
public interface CppSubsetParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CppSubsetParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(CppSubsetParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link CppSubsetParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(CppSubsetParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link CppSubsetParser#globalItem}.
	 * @param ctx the parse tree
	 */
	void enterGlobalItem(CppSubsetParser.GlobalItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link CppSubsetParser#globalItem}.
	 * @param ctx the parse tree
	 */
	void exitGlobalItem(CppSubsetParser.GlobalItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link CppSubsetParser#functionDecl}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDecl(CppSubsetParser.FunctionDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link CppSubsetParser#functionDecl}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDecl(CppSubsetParser.FunctionDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link CppSubsetParser#functionDef}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDef(CppSubsetParser.FunctionDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link CppSubsetParser#functionDef}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDef(CppSubsetParser.FunctionDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link CppSubsetParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void enterParameterList(CppSubsetParser.ParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CppSubsetParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void exitParameterList(CppSubsetParser.ParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link CppSubsetParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(CppSubsetParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link CppSubsetParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(CppSubsetParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link CppSubsetParser#variableDecl}.
	 * @param ctx the parse tree
	 */
	void enterVariableDecl(CppSubsetParser.VariableDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link CppSubsetParser#variableDecl}.
	 * @param ctx the parse tree
	 */
	void exitVariableDecl(CppSubsetParser.VariableDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link CppSubsetParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(CppSubsetParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link CppSubsetParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(CppSubsetParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link CppSubsetParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(CppSubsetParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link CppSubsetParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(CppSubsetParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link CppSubsetParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(CppSubsetParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link CppSubsetParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(CppSubsetParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link CppSubsetParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(CppSubsetParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CppSubsetParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(CppSubsetParser.ExpressionContext ctx);
}