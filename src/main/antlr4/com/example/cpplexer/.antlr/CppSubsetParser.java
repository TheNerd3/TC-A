// Generated from c:/Users/marti/OneDrive/Desktop/FACU/QUINTO/TC/FINAL/TC-A/src/main/antlr4/com/example/cpplexer/CppSubsetParser.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class CppSubsetParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		INT=1, CHAR=2, DOUBLE=3, VOID=4, DATE=5, BOOL=6, IF=7, ELSE=8, FOR=9, 
		WHILE=10, BREAK=11, CONTINUE=12, RETURN=13, EQEQ=14, NEQ=15, LE=16, GE=17, 
		PE=18, ME=19, MULE=20, DIVE=21, ANDAND=22, OROR=23, PLUSPLUS=24, MINUSMINUS=25, 
		PLUS=26, MINUS=27, STAR=28, DIV=29, MOD=30, ASSIGN=31, LT=32, GT=33, NOT=34, 
		SEMI=35, COMMA=36, LPAREN=37, RPAREN=38, LBRACE=39, RBRACE=40, LBRACK=41, 
		RBRACK=42, SQUOTE=43, DQUOTE=44, FLOAT_LITERAL=45, INT_LITERAL=46, CHAR_LITERAL=47, 
		STRING_LITERAL=48, DATE_LITERAL=49, DOUBLE_LITERAL=50, BOOL_LITERAL=51, 
		IDENTIFIER=52, LINE_COMMENT=53, BLOCK_COMMENT=54, WS=55, INVALID=56;
	public static final int
		RULE_program = 0, RULE_globalItem = 1, RULE_functionDecl = 2, RULE_functionDef = 3, 
		RULE_parameterList = 4, RULE_parameter = 5, RULE_variableDecl = 6, RULE_type = 7, 
		RULE_block = 8, RULE_statement = 9, RULE_expression = 10, RULE_argumentList = 11;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "globalItem", "functionDecl", "functionDef", "parameterList", 
			"parameter", "variableDecl", "type", "block", "statement", "expression", 
			"argumentList"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'int'", "'char'", "'double'", "'void'", "'date'", "'bool'", "'if'", 
			"'else'", "'for'", "'while'", "'break'", "'continue'", "'return'", "'=='", 
			"'!='", "'<='", "'>='", "'+='", "'-='", "'*='", "'/='", "'&&'", "'||'", 
			"'++'", "'--'", "'+'", "'-'", "'*'", "'/'", "'%'", "'='", "'<'", "'>'", 
			"'!'", "';'", "','", "'('", "')'", "'{'", "'}'", "'['", "']'", "'''", 
			"'\"'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "INT", "CHAR", "DOUBLE", "VOID", "DATE", "BOOL", "IF", "ELSE", 
			"FOR", "WHILE", "BREAK", "CONTINUE", "RETURN", "EQEQ", "NEQ", "LE", "GE", 
			"PE", "ME", "MULE", "DIVE", "ANDAND", "OROR", "PLUSPLUS", "MINUSMINUS", 
			"PLUS", "MINUS", "STAR", "DIV", "MOD", "ASSIGN", "LT", "GT", "NOT", "SEMI", 
			"COMMA", "LPAREN", "RPAREN", "LBRACE", "RBRACE", "LBRACK", "RBRACK", 
			"SQUOTE", "DQUOTE", "FLOAT_LITERAL", "INT_LITERAL", "CHAR_LITERAL", "STRING_LITERAL", 
			"DATE_LITERAL", "DOUBLE_LITERAL", "BOOL_LITERAL", "IDENTIFIER", "LINE_COMMENT", 
			"BLOCK_COMMENT", "WS", "INVALID"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "CppSubsetParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CppSubsetParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(CppSubsetParser.EOF, 0); }
		public List<GlobalItemContext> globalItem() {
			return getRuleContexts(GlobalItemContext.class);
		}
		public GlobalItemContext globalItem(int i) {
			return getRuleContext(GlobalItemContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(27);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 94L) != 0)) {
				{
				{
				setState(24);
				globalItem();
				}
				}
				setState(29);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(30);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GlobalItemContext extends ParserRuleContext {
		public VariableDeclContext variableDecl() {
			return getRuleContext(VariableDeclContext.class,0);
		}
		public FunctionDeclContext functionDecl() {
			return getRuleContext(FunctionDeclContext.class,0);
		}
		public FunctionDefContext functionDef() {
			return getRuleContext(FunctionDefContext.class,0);
		}
		public GlobalItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_globalItem; }
	}

	public final GlobalItemContext globalItem() throws RecognitionException {
		GlobalItemContext _localctx = new GlobalItemContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_globalItem);
		try {
			setState(35);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(32);
				variableDecl();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(33);
				functionDecl();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(34);
				functionDef();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionDeclContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(CppSubsetParser.IDENTIFIER, 0); }
		public TerminalNode LPAREN() { return getToken(CppSubsetParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(CppSubsetParser.RPAREN, 0); }
		public TerminalNode SEMI() { return getToken(CppSubsetParser.SEMI, 0); }
		public ParameterListContext parameterList() {
			return getRuleContext(ParameterListContext.class,0);
		}
		public FunctionDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionDecl; }
	}

	public final FunctionDeclContext functionDecl() throws RecognitionException {
		FunctionDeclContext _localctx = new FunctionDeclContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_functionDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(37);
			type();
			setState(38);
			match(IDENTIFIER);
			setState(39);
			match(LPAREN);
			setState(41);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 94L) != 0)) {
				{
				setState(40);
				parameterList();
				}
			}

			setState(43);
			match(RPAREN);
			setState(44);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionDefContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(CppSubsetParser.IDENTIFIER, 0); }
		public TerminalNode LPAREN() { return getToken(CppSubsetParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(CppSubsetParser.RPAREN, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ParameterListContext parameterList() {
			return getRuleContext(ParameterListContext.class,0);
		}
		public FunctionDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionDef; }
	}

	public final FunctionDefContext functionDef() throws RecognitionException {
		FunctionDefContext _localctx = new FunctionDefContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_functionDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(46);
			type();
			setState(47);
			match(IDENTIFIER);
			setState(48);
			match(LPAREN);
			setState(50);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 94L) != 0)) {
				{
				setState(49);
				parameterList();
				}
			}

			setState(52);
			match(RPAREN);
			setState(53);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParameterListContext extends ParserRuleContext {
		public List<ParameterContext> parameter() {
			return getRuleContexts(ParameterContext.class);
		}
		public ParameterContext parameter(int i) {
			return getRuleContext(ParameterContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(CppSubsetParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(CppSubsetParser.COMMA, i);
		}
		public ParameterListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterList; }
	}

	public final ParameterListContext parameterList() throws RecognitionException {
		ParameterListContext _localctx = new ParameterListContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_parameterList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55);
			parameter();
			setState(60);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(56);
				match(COMMA);
				setState(57);
				parameter();
				}
				}
				setState(62);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParameterContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(CppSubsetParser.IDENTIFIER, 0); }
		public ParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameter; }
	}

	public final ParameterContext parameter() throws RecognitionException {
		ParameterContext _localctx = new ParameterContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_parameter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(63);
			type();
			setState(64);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableDeclContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(CppSubsetParser.IDENTIFIER, 0); }
		public TerminalNode SEMI() { return getToken(CppSubsetParser.SEMI, 0); }
		public TerminalNode LBRACK() { return getToken(CppSubsetParser.LBRACK, 0); }
		public TerminalNode INT_LITERAL() { return getToken(CppSubsetParser.INT_LITERAL, 0); }
		public TerminalNode RBRACK() { return getToken(CppSubsetParser.RBRACK, 0); }
		public VariableDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDecl; }
	}

	public final VariableDeclContext variableDecl() throws RecognitionException {
		VariableDeclContext _localctx = new VariableDeclContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_variableDecl);
		try {
			setState(77);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(66);
				type();
				setState(67);
				match(IDENTIFIER);
				setState(68);
				match(SEMI);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(70);
				type();
				setState(71);
				match(IDENTIFIER);
				setState(72);
				match(LBRACK);
				setState(73);
				match(INT_LITERAL);
				setState(74);
				match(RBRACK);
				setState(75);
				match(SEMI);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(CppSubsetParser.INT, 0); }
		public TerminalNode DOUBLE() { return getToken(CppSubsetParser.DOUBLE, 0); }
		public TerminalNode CHAR() { return getToken(CppSubsetParser.CHAR, 0); }
		public TerminalNode BOOL() { return getToken(CppSubsetParser.BOOL, 0); }
		public TerminalNode VOID() { return getToken(CppSubsetParser.VOID, 0); }
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_type);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(79);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 94L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(CppSubsetParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(CppSubsetParser.RBRACE, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			match(LBRACE);
			setState(85);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8374017996103680L) != 0)) {
				{
				{
				setState(82);
				statement();
				}
				}
				setState(87);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(88);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
	 
		public StatementContext() { }
		public void copyFrom(StatementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ReturnVoidContext extends StatementContext {
		public TerminalNode RETURN() { return getToken(CppSubsetParser.RETURN, 0); }
		public TerminalNode SEMI() { return getToken(CppSubsetParser.SEMI, 0); }
		public ReturnVoidContext(StatementContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AssignContext extends StatementContext {
		public TerminalNode IDENTIFIER() { return getToken(CppSubsetParser.IDENTIFIER, 0); }
		public TerminalNode ASSIGN() { return getToken(CppSubsetParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(CppSubsetParser.SEMI, 0); }
		public AssignContext(StatementContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ReturnExprContext extends StatementContext {
		public TerminalNode RETURN() { return getToken(CppSubsetParser.RETURN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(CppSubsetParser.SEMI, 0); }
		public ReturnExprContext(StatementContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrintExprContext extends StatementContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(CppSubsetParser.SEMI, 0); }
		public PrintExprContext(StatementContext ctx) { copyFrom(ctx); }
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_statement);
		try {
			setState(104);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				_localctx = new AssignContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(90);
				match(IDENTIFIER);
				setState(91);
				match(ASSIGN);
				setState(92);
				expression(0);
				setState(93);
				match(SEMI);
				}
				break;
			case 2:
				_localctx = new ReturnExprContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(95);
				match(RETURN);
				setState(96);
				expression(0);
				setState(97);
				match(SEMI);
				}
				break;
			case 3:
				_localctx = new ReturnVoidContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(99);
				match(RETURN);
				setState(100);
				match(SEMI);
				}
				break;
			case 4:
				_localctx = new PrintExprContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(101);
				expression(0);
				setState(102);
				match(SEMI);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FuncCallContext extends ExpressionContext {
		public TerminalNode IDENTIFIER() { return getToken(CppSubsetParser.IDENTIFIER, 0); }
		public TerminalNode LPAREN() { return getToken(CppSubsetParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(CppSubsetParser.RPAREN, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public FuncCallContext(ExpressionContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BoolContext extends ExpressionContext {
		public TerminalNode BOOL_LITERAL() { return getToken(CppSubsetParser.BOOL_LITERAL, 0); }
		public BoolContext(ExpressionContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MulDivContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode STAR() { return getToken(CppSubsetParser.STAR, 0); }
		public TerminalNode DIV() { return getToken(CppSubsetParser.DIV, 0); }
		public MulDivContext(ExpressionContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AddSubContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode PLUS() { return getToken(CppSubsetParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(CppSubsetParser.MINUS, 0); }
		public AddSubContext(ExpressionContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CharContext extends ExpressionContext {
		public TerminalNode CHAR_LITERAL() { return getToken(CppSubsetParser.CHAR_LITERAL, 0); }
		public CharContext(ExpressionContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParensContext extends ExpressionContext {
		public TerminalNode LPAREN() { return getToken(CppSubsetParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(CppSubsetParser.RPAREN, 0); }
		public ParensContext(ExpressionContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IdContext extends ExpressionContext {
		public TerminalNode IDENTIFIER() { return getToken(CppSubsetParser.IDENTIFIER, 0); }
		public IdContext(ExpressionContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DoubleContext extends ExpressionContext {
		public TerminalNode DOUBLE_LITERAL() { return getToken(CppSubsetParser.DOUBLE_LITERAL, 0); }
		public DoubleContext(ExpressionContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IntContext extends ExpressionContext {
		public TerminalNode INT_LITERAL() { return getToken(CppSubsetParser.INT_LITERAL, 0); }
		public IntContext(ExpressionContext ctx) { copyFrom(ctx); }
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StringLitContext extends ExpressionContext {
		public TerminalNode STRING_LITERAL() { return getToken(CppSubsetParser.STRING_LITERAL, 0); }
		public StringLitContext(ExpressionContext ctx) { copyFrom(ctx); }
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 20;
		enterRecursionRule(_localctx, 20, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(123);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				{
				_localctx = new FuncCallContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(107);
				match(IDENTIFIER);
				setState(108);
				match(LPAREN);
				setState(110);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8374017996095488L) != 0)) {
					{
					setState(109);
					argumentList();
					}
				}

				setState(112);
				match(RPAREN);
				}
				break;
			case 2:
				{
				_localctx = new IntContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(113);
				match(INT_LITERAL);
				}
				break;
			case 3:
				{
				_localctx = new DoubleContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(114);
				match(DOUBLE_LITERAL);
				}
				break;
			case 4:
				{
				_localctx = new CharContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(115);
				match(CHAR_LITERAL);
				}
				break;
			case 5:
				{
				_localctx = new BoolContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(116);
				match(BOOL_LITERAL);
				}
				break;
			case 6:
				{
				_localctx = new StringLitContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(117);
				match(STRING_LITERAL);
				}
				break;
			case 7:
				{
				_localctx = new IdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(118);
				match(IDENTIFIER);
				}
				break;
			case 8:
				{
				_localctx = new ParensContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(119);
				match(LPAREN);
				setState(120);
				expression(0);
				setState(121);
				match(RPAREN);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(133);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(131);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
					case 1:
						{
						_localctx = new MulDivContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(125);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(126);
						((MulDivContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==STAR || _la==DIV) ) {
							((MulDivContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(127);
						expression(11);
						}
						break;
					case 2:
						{
						_localctx = new AddSubContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(128);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(129);
						((AddSubContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==PLUS || _la==MINUS) ) {
							((AddSubContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(130);
						expression(10);
						}
						break;
					}
					} 
				}
				setState(135);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentListContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(CppSubsetParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(CppSubsetParser.COMMA, i);
		}
		public ArgumentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentList; }
	}

	public final ArgumentListContext argumentList() throws RecognitionException {
		ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(136);
			expression(0);
			setState(141);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(137);
				match(COMMA);
				setState(138);
				expression(0);
				}
				}
				setState(143);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 10:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 10);
		case 1:
			return precpred(_ctx, 9);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u00018\u0091\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0001"+
		"\u0000\u0005\u0000\u001a\b\u0000\n\u0000\f\u0000\u001d\t\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001$\b\u0001"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002*\b\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0003\u00033\b\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0005\u0004;\b\u0004\n\u0004\f\u0004"+
		">\t\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0003\u0006N\b\u0006\u0001\u0007"+
		"\u0001\u0007\u0001\b\u0001\b\u0005\bT\b\b\n\b\f\bW\t\b\u0001\b\u0001\b"+
		"\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0003\ti\b\t\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0003\no\b\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0003\n|\b\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0005\n\u0084\b\n\n\n\f\n\u0087"+
		"\t\n\u0001\u000b\u0001\u000b\u0001\u000b\u0005\u000b\u008c\b\u000b\n\u000b"+
		"\f\u000b\u008f\t\u000b\u0001\u000b\u0000\u0001\u0014\f\u0000\u0002\u0004"+
		"\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0000\u0003\u0002\u0000\u0001"+
		"\u0004\u0006\u0006\u0001\u0000\u001c\u001d\u0001\u0000\u001a\u001b\u009a"+
		"\u0000\u001b\u0001\u0000\u0000\u0000\u0002#\u0001\u0000\u0000\u0000\u0004"+
		"%\u0001\u0000\u0000\u0000\u0006.\u0001\u0000\u0000\u0000\b7\u0001\u0000"+
		"\u0000\u0000\n?\u0001\u0000\u0000\u0000\fM\u0001\u0000\u0000\u0000\u000e"+
		"O\u0001\u0000\u0000\u0000\u0010Q\u0001\u0000\u0000\u0000\u0012h\u0001"+
		"\u0000\u0000\u0000\u0014{\u0001\u0000\u0000\u0000\u0016\u0088\u0001\u0000"+
		"\u0000\u0000\u0018\u001a\u0003\u0002\u0001\u0000\u0019\u0018\u0001\u0000"+
		"\u0000\u0000\u001a\u001d\u0001\u0000\u0000\u0000\u001b\u0019\u0001\u0000"+
		"\u0000\u0000\u001b\u001c\u0001\u0000\u0000\u0000\u001c\u001e\u0001\u0000"+
		"\u0000\u0000\u001d\u001b\u0001\u0000\u0000\u0000\u001e\u001f\u0005\u0000"+
		"\u0000\u0001\u001f\u0001\u0001\u0000\u0000\u0000 $\u0003\f\u0006\u0000"+
		"!$\u0003\u0004\u0002\u0000\"$\u0003\u0006\u0003\u0000# \u0001\u0000\u0000"+
		"\u0000#!\u0001\u0000\u0000\u0000#\"\u0001\u0000\u0000\u0000$\u0003\u0001"+
		"\u0000\u0000\u0000%&\u0003\u000e\u0007\u0000&\'\u00054\u0000\u0000\')"+
		"\u0005%\u0000\u0000(*\u0003\b\u0004\u0000)(\u0001\u0000\u0000\u0000)*"+
		"\u0001\u0000\u0000\u0000*+\u0001\u0000\u0000\u0000+,\u0005&\u0000\u0000"+
		",-\u0005#\u0000\u0000-\u0005\u0001\u0000\u0000\u0000./\u0003\u000e\u0007"+
		"\u0000/0\u00054\u0000\u000002\u0005%\u0000\u000013\u0003\b\u0004\u0000"+
		"21\u0001\u0000\u0000\u000023\u0001\u0000\u0000\u000034\u0001\u0000\u0000"+
		"\u000045\u0005&\u0000\u000056\u0003\u0010\b\u00006\u0007\u0001\u0000\u0000"+
		"\u00007<\u0003\n\u0005\u000089\u0005$\u0000\u00009;\u0003\n\u0005\u0000"+
		":8\u0001\u0000\u0000\u0000;>\u0001\u0000\u0000\u0000<:\u0001\u0000\u0000"+
		"\u0000<=\u0001\u0000\u0000\u0000=\t\u0001\u0000\u0000\u0000><\u0001\u0000"+
		"\u0000\u0000?@\u0003\u000e\u0007\u0000@A\u00054\u0000\u0000A\u000b\u0001"+
		"\u0000\u0000\u0000BC\u0003\u000e\u0007\u0000CD\u00054\u0000\u0000DE\u0005"+
		"#\u0000\u0000EN\u0001\u0000\u0000\u0000FG\u0003\u000e\u0007\u0000GH\u0005"+
		"4\u0000\u0000HI\u0005)\u0000\u0000IJ\u0005.\u0000\u0000JK\u0005*\u0000"+
		"\u0000KL\u0005#\u0000\u0000LN\u0001\u0000\u0000\u0000MB\u0001\u0000\u0000"+
		"\u0000MF\u0001\u0000\u0000\u0000N\r\u0001\u0000\u0000\u0000OP\u0007\u0000"+
		"\u0000\u0000P\u000f\u0001\u0000\u0000\u0000QU\u0005\'\u0000\u0000RT\u0003"+
		"\u0012\t\u0000SR\u0001\u0000\u0000\u0000TW\u0001\u0000\u0000\u0000US\u0001"+
		"\u0000\u0000\u0000UV\u0001\u0000\u0000\u0000VX\u0001\u0000\u0000\u0000"+
		"WU\u0001\u0000\u0000\u0000XY\u0005(\u0000\u0000Y\u0011\u0001\u0000\u0000"+
		"\u0000Z[\u00054\u0000\u0000[\\\u0005\u001f\u0000\u0000\\]\u0003\u0014"+
		"\n\u0000]^\u0005#\u0000\u0000^i\u0001\u0000\u0000\u0000_`\u0005\r\u0000"+
		"\u0000`a\u0003\u0014\n\u0000ab\u0005#\u0000\u0000bi\u0001\u0000\u0000"+
		"\u0000cd\u0005\r\u0000\u0000di\u0005#\u0000\u0000ef\u0003\u0014\n\u0000"+
		"fg\u0005#\u0000\u0000gi\u0001\u0000\u0000\u0000hZ\u0001\u0000\u0000\u0000"+
		"h_\u0001\u0000\u0000\u0000hc\u0001\u0000\u0000\u0000he\u0001\u0000\u0000"+
		"\u0000i\u0013\u0001\u0000\u0000\u0000jk\u0006\n\uffff\uffff\u0000kl\u0005"+
		"4\u0000\u0000ln\u0005%\u0000\u0000mo\u0003\u0016\u000b\u0000nm\u0001\u0000"+
		"\u0000\u0000no\u0001\u0000\u0000\u0000op\u0001\u0000\u0000\u0000p|\u0005"+
		"&\u0000\u0000q|\u0005.\u0000\u0000r|\u00052\u0000\u0000s|\u0005/\u0000"+
		"\u0000t|\u00053\u0000\u0000u|\u00050\u0000\u0000v|\u00054\u0000\u0000"+
		"wx\u0005%\u0000\u0000xy\u0003\u0014\n\u0000yz\u0005&\u0000\u0000z|\u0001"+
		"\u0000\u0000\u0000{j\u0001\u0000\u0000\u0000{q\u0001\u0000\u0000\u0000"+
		"{r\u0001\u0000\u0000\u0000{s\u0001\u0000\u0000\u0000{t\u0001\u0000\u0000"+
		"\u0000{u\u0001\u0000\u0000\u0000{v\u0001\u0000\u0000\u0000{w\u0001\u0000"+
		"\u0000\u0000|\u0085\u0001\u0000\u0000\u0000}~\n\n\u0000\u0000~\u007f\u0007"+
		"\u0001\u0000\u0000\u007f\u0084\u0003\u0014\n\u000b\u0080\u0081\n\t\u0000"+
		"\u0000\u0081\u0082\u0007\u0002\u0000\u0000\u0082\u0084\u0003\u0014\n\n"+
		"\u0083}\u0001\u0000\u0000\u0000\u0083\u0080\u0001\u0000\u0000\u0000\u0084"+
		"\u0087\u0001\u0000\u0000\u0000\u0085\u0083\u0001\u0000\u0000\u0000\u0085"+
		"\u0086\u0001\u0000\u0000\u0000\u0086\u0015\u0001\u0000\u0000\u0000\u0087"+
		"\u0085\u0001\u0000\u0000\u0000\u0088\u008d\u0003\u0014\n\u0000\u0089\u008a"+
		"\u0005$\u0000\u0000\u008a\u008c\u0003\u0014\n\u0000\u008b\u0089\u0001"+
		"\u0000\u0000\u0000\u008c\u008f\u0001\u0000\u0000\u0000\u008d\u008b\u0001"+
		"\u0000\u0000\u0000\u008d\u008e\u0001\u0000\u0000\u0000\u008e\u0017\u0001"+
		"\u0000\u0000\u0000\u008f\u008d\u0001\u0000\u0000\u0000\r\u001b#)2<MUh"+
		"n{\u0083\u0085\u008d";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}