// Generated from src/antlr/frontend/FrontendParser.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class FrontendParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		DOCTYPE=1, JINJA_EXPR_START=2, JINJA_EXPR_END=3, JINJA_STMT_START=4, JINJA_STMT_END=5, 
		JINJA_COMMENT=6, JINJA_IF=7, JINJA_ELIF=8, JINJA_ELSE=9, JINJA_ENDIF=10, 
		JINJA_FOR=11, JINJA_IN=12, JINJA_ENDFOR=13, JINJA_EXTENDS=14, JINJA_BLOCK=15, 
		JINJA_ENDBLOCK=16, TAG_OPEN=17, TAG_CLOSE=18, TAG_SLASH=19, STYLE_TAG=20, 
		JINJA_EQ=21, JINJA_NEQ=22, JINJA_LTE=23, JINJA_GTE=24, JINJA_LT=25, JINJA_GT=26, 
		JINJA_AND=27, JINJA_OR=28, JINJA_NOT=29, CSS_LBRACE=30, CSS_RBRACE=31, 
		CSS_COLON=32, CSS_SEMICOLON=33, EQUALS=34, DOT=35, LBRACKET=36, RBRACKET=37, 
		COMMA=38, HASH=39, STRING=40, NUMBER=41, CSS_UNIT=42, CSS_IDENT=43, IDENTIFIER=44, 
		TEXT=45, WS=46, HTML_COMMENT=47, CSS_COMMENT=48;
	public static final int
		RULE_document = 0, RULE_content = 1, RULE_contentItem = 2, RULE_htmlElement = 3, 
		RULE_tagName = 4, RULE_htmlAttribute = 5, RULE_attrName = 6, RULE_attrValue = 7, 
		RULE_styleElement = 8, RULE_cssRule = 9, RULE_cssSelector = 10, RULE_cssSelectorPart = 11, 
		RULE_cssSelectorItem = 12, RULE_cssDeclaration = 13, RULE_cssPropertyName = 14, 
		RULE_cssValue = 15, RULE_jinjaExpression = 16, RULE_jinjaExpr = 17, RULE_jinjaStatement = 18, 
		RULE_jinjaIfStatement = 19, RULE_jinjaElifStatement = 20, RULE_jinjaElseStatement = 21, 
		RULE_jinjaForStatement = 22, RULE_jinjaExtendsStatement = 23, RULE_jinjaBlockStatement = 24, 
		RULE_jinjaCondition = 25, RULE_jinjaStmtExpr = 26, RULE_text = 27;
	private static String[] makeRuleNames() {
		return new String[] {
			"document", "content", "contentItem", "htmlElement", "tagName", "htmlAttribute", 
			"attrName", "attrValue", "styleElement", "cssRule", "cssSelector", "cssSelectorPart", 
			"cssSelectorItem", "cssDeclaration", "cssPropertyName", "cssValue", "jinjaExpression", 
			"jinjaExpr", "jinjaStatement", "jinjaIfStatement", "jinjaElifStatement", 
			"jinjaElseStatement", "jinjaForStatement", "jinjaExtendsStatement", "jinjaBlockStatement", 
			"jinjaCondition", "jinjaStmtExpr", "text"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, "'{{'", "'}}'", "'{%'", "'%}'", null, "'if'", "'elif'", "'else'", 
			"'endif'", "'for'", "'in'", "'endfor'", "'extends'", "'block'", "'endblock'", 
			null, null, "'/'", "'style'", "'=='", "'!='", "'<='", "'>='", null, null, 
			"'and'", "'or'", "'not'", "'{'", "'}'", "':'", "';'", "'='", "'.'", "'['", 
			"']'", "','", "'#'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "DOCTYPE", "JINJA_EXPR_START", "JINJA_EXPR_END", "JINJA_STMT_START", 
			"JINJA_STMT_END", "JINJA_COMMENT", "JINJA_IF", "JINJA_ELIF", "JINJA_ELSE", 
			"JINJA_ENDIF", "JINJA_FOR", "JINJA_IN", "JINJA_ENDFOR", "JINJA_EXTENDS", 
			"JINJA_BLOCK", "JINJA_ENDBLOCK", "TAG_OPEN", "TAG_CLOSE", "TAG_SLASH", 
			"STYLE_TAG", "JINJA_EQ", "JINJA_NEQ", "JINJA_LTE", "JINJA_GTE", "JINJA_LT", 
			"JINJA_GT", "JINJA_AND", "JINJA_OR", "JINJA_NOT", "CSS_LBRACE", "CSS_RBRACE", 
			"CSS_COLON", "CSS_SEMICOLON", "EQUALS", "DOT", "LBRACKET", "RBRACKET", 
			"COMMA", "HASH", "STRING", "NUMBER", "CSS_UNIT", "CSS_IDENT", "IDENTIFIER", 
			"TEXT", "WS", "HTML_COMMENT", "CSS_COMMENT"
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
	public String getGrammarFileName() { return "FrontendParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public FrontendParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DocumentContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(FrontendParser.EOF, 0); }
		public JinjaExtendsStatementContext jinjaExtendsStatement() {
			return getRuleContext(JinjaExtendsStatementContext.class,0);
		}
		public TerminalNode DOCTYPE() { return getToken(FrontendParser.DOCTYPE, 0); }
		public ContentContext content() {
			return getRuleContext(ContentContext.class,0);
		}
		public DocumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_document; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterDocument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitDocument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitDocument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DocumentContext document() throws RecognitionException {
		DocumentContext _localctx = new DocumentContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_document);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(57);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(56);
				jinjaExtendsStatement();
				}
				break;
			}
			setState(60);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DOCTYPE) {
				{
				setState(59);
				match(DOCTYPE);
				}
			}

			setState(63);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				{
				setState(62);
				content();
				}
				break;
			}
			setState(65);
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
	public static class ContentContext extends ParserRuleContext {
		public List<ContentItemContext> contentItem() {
			return getRuleContexts(ContentItemContext.class);
		}
		public ContentItemContext contentItem(int i) {
			return getRuleContext(ContentItemContext.class,i);
		}
		public ContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_content; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitContent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitContent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContentContext content() throws RecognitionException {
		ContentContext _localctx = new ContentContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_content);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(70);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(67);
					contentItem();
					}
					} 
				}
				setState(72);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
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
	public static class ContentItemContext extends ParserRuleContext {
		public HtmlElementContext htmlElement() {
			return getRuleContext(HtmlElementContext.class,0);
		}
		public JinjaExpressionContext jinjaExpression() {
			return getRuleContext(JinjaExpressionContext.class,0);
		}
		public JinjaStatementContext jinjaStatement() {
			return getRuleContext(JinjaStatementContext.class,0);
		}
		public TextContext text() {
			return getRuleContext(TextContext.class,0);
		}
		public ContentItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_contentItem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterContentItem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitContentItem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitContentItem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContentItemContext contentItem() throws RecognitionException {
		ContentItemContext _localctx = new ContentItemContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_contentItem);
		try {
			setState(77);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TAG_OPEN:
				enterOuterAlt(_localctx, 1);
				{
				setState(73);
				htmlElement();
				}
				break;
			case JINJA_EXPR_START:
				enterOuterAlt(_localctx, 2);
				{
				setState(74);
				jinjaExpression();
				}
				break;
			case JINJA_STMT_START:
				enterOuterAlt(_localctx, 3);
				{
				setState(75);
				jinjaStatement();
				}
				break;
			case NUMBER:
			case CSS_IDENT:
			case IDENTIFIER:
			case TEXT:
				enterOuterAlt(_localctx, 4);
				{
				setState(76);
				text();
				}
				break;
			default:
				throw new NoViableAltException(this);
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
	public static class HtmlElementContext extends ParserRuleContext {
		public HtmlElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_htmlElement; }
	 
		public HtmlElementContext() { }
		public void copyFrom(HtmlElementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RegularElementContext extends HtmlElementContext {
		public List<TerminalNode> TAG_OPEN() { return getTokens(FrontendParser.TAG_OPEN); }
		public TerminalNode TAG_OPEN(int i) {
			return getToken(FrontendParser.TAG_OPEN, i);
		}
		public List<TagNameContext> tagName() {
			return getRuleContexts(TagNameContext.class);
		}
		public TagNameContext tagName(int i) {
			return getRuleContext(TagNameContext.class,i);
		}
		public List<TerminalNode> TAG_CLOSE() { return getTokens(FrontendParser.TAG_CLOSE); }
		public TerminalNode TAG_CLOSE(int i) {
			return getToken(FrontendParser.TAG_CLOSE, i);
		}
		public ContentContext content() {
			return getRuleContext(ContentContext.class,0);
		}
		public TerminalNode TAG_SLASH() { return getToken(FrontendParser.TAG_SLASH, 0); }
		public List<HtmlAttributeContext> htmlAttribute() {
			return getRuleContexts(HtmlAttributeContext.class);
		}
		public HtmlAttributeContext htmlAttribute(int i) {
			return getRuleContext(HtmlAttributeContext.class,i);
		}
		public RegularElementContext(HtmlElementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterRegularElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitRegularElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitRegularElement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StyleContext extends HtmlElementContext {
		public StyleElementContext styleElement() {
			return getRuleContext(StyleElementContext.class,0);
		}
		public StyleContext(HtmlElementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterStyle(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitStyle(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitStyle(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SelfClosingElementContext extends HtmlElementContext {
		public TerminalNode TAG_OPEN() { return getToken(FrontendParser.TAG_OPEN, 0); }
		public TagNameContext tagName() {
			return getRuleContext(TagNameContext.class,0);
		}
		public TerminalNode TAG_SLASH() { return getToken(FrontendParser.TAG_SLASH, 0); }
		public TerminalNode TAG_CLOSE() { return getToken(FrontendParser.TAG_CLOSE, 0); }
		public List<HtmlAttributeContext> htmlAttribute() {
			return getRuleContexts(HtmlAttributeContext.class);
		}
		public HtmlAttributeContext htmlAttribute(int i) {
			return getRuleContext(HtmlAttributeContext.class,i);
		}
		public SelfClosingElementContext(HtmlElementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterSelfClosingElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitSelfClosingElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitSelfClosingElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HtmlElementContext htmlElement() throws RecognitionException {
		HtmlElementContext _localctx = new HtmlElementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_htmlElement);
		int _la;
		try {
			setState(106);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				_localctx = new RegularElementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(79);
				match(TAG_OPEN);
				setState(80);
				tagName();
				setState(84);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==CSS_IDENT || _la==IDENTIFIER) {
					{
					{
					setState(81);
					htmlAttribute();
					}
					}
					setState(86);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(87);
				match(TAG_CLOSE);
				setState(88);
				content();
				setState(89);
				match(TAG_OPEN);
				setState(90);
				match(TAG_SLASH);
				setState(91);
				tagName();
				setState(92);
				match(TAG_CLOSE);
				}
				break;
			case 2:
				_localctx = new SelfClosingElementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(94);
				match(TAG_OPEN);
				setState(95);
				tagName();
				setState(99);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==CSS_IDENT || _la==IDENTIFIER) {
					{
					{
					setState(96);
					htmlAttribute();
					}
					}
					setState(101);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(102);
				match(TAG_SLASH);
				setState(103);
				match(TAG_CLOSE);
				}
				break;
			case 3:
				_localctx = new StyleContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(105);
				styleElement();
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
	public static class TagNameContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(FrontendParser.IDENTIFIER, 0); }
		public TerminalNode CSS_IDENT() { return getToken(FrontendParser.CSS_IDENT, 0); }
		public TagNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tagName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterTagName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitTagName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitTagName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TagNameContext tagName() throws RecognitionException {
		TagNameContext _localctx = new TagNameContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_tagName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(108);
			_la = _input.LA(1);
			if ( !(_la==CSS_IDENT || _la==IDENTIFIER) ) {
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
	public static class HtmlAttributeContext extends ParserRuleContext {
		public AttrNameContext attrName() {
			return getRuleContext(AttrNameContext.class,0);
		}
		public TerminalNode EQUALS() { return getToken(FrontendParser.EQUALS, 0); }
		public AttrValueContext attrValue() {
			return getRuleContext(AttrValueContext.class,0);
		}
		public HtmlAttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_htmlAttribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterHtmlAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitHtmlAttribute(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitHtmlAttribute(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HtmlAttributeContext htmlAttribute() throws RecognitionException {
		HtmlAttributeContext _localctx = new HtmlAttributeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_htmlAttribute);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
			attrName();
			setState(113);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EQUALS) {
				{
				setState(111);
				match(EQUALS);
				setState(112);
				attrValue();
				}
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
	public static class AttrNameContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(FrontendParser.IDENTIFIER, 0); }
		public TerminalNode CSS_IDENT() { return getToken(FrontendParser.CSS_IDENT, 0); }
		public AttrNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attrName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterAttrName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitAttrName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitAttrName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttrNameContext attrName() throws RecognitionException {
		AttrNameContext _localctx = new AttrNameContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_attrName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(115);
			_la = _input.LA(1);
			if ( !(_la==CSS_IDENT || _la==IDENTIFIER) ) {
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
	public static class AttrValueContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(FrontendParser.STRING, 0); }
		public TerminalNode IDENTIFIER() { return getToken(FrontendParser.IDENTIFIER, 0); }
		public TerminalNode CSS_IDENT() { return getToken(FrontendParser.CSS_IDENT, 0); }
		public AttrValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attrValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterAttrValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitAttrValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitAttrValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttrValueContext attrValue() throws RecognitionException {
		AttrValueContext _localctx = new AttrValueContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_attrValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 27487790694400L) != 0)) ) {
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
	public static class StyleElementContext extends ParserRuleContext {
		public List<TerminalNode> TAG_OPEN() { return getTokens(FrontendParser.TAG_OPEN); }
		public TerminalNode TAG_OPEN(int i) {
			return getToken(FrontendParser.TAG_OPEN, i);
		}
		public List<TerminalNode> STYLE_TAG() { return getTokens(FrontendParser.STYLE_TAG); }
		public TerminalNode STYLE_TAG(int i) {
			return getToken(FrontendParser.STYLE_TAG, i);
		}
		public List<TerminalNode> TAG_CLOSE() { return getTokens(FrontendParser.TAG_CLOSE); }
		public TerminalNode TAG_CLOSE(int i) {
			return getToken(FrontendParser.TAG_CLOSE, i);
		}
		public TerminalNode TAG_SLASH() { return getToken(FrontendParser.TAG_SLASH, 0); }
		public List<CssRuleContext> cssRule() {
			return getRuleContexts(CssRuleContext.class);
		}
		public CssRuleContext cssRule(int i) {
			return getRuleContext(CssRuleContext.class,i);
		}
		public StyleElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_styleElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterStyleElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitStyleElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitStyleElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StyleElementContext styleElement() throws RecognitionException {
		StyleElementContext _localctx = new StyleElementContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_styleElement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(119);
			match(TAG_OPEN);
			setState(120);
			match(STYLE_TAG);
			setState(121);
			match(TAG_CLOSE);
			setState(125);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 26972394618880L) != 0)) {
				{
				{
				setState(122);
				cssRule();
				}
				}
				setState(127);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(128);
			match(TAG_OPEN);
			setState(129);
			match(TAG_SLASH);
			setState(130);
			match(STYLE_TAG);
			setState(131);
			match(TAG_CLOSE);
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
	public static class CssRuleContext extends ParserRuleContext {
		public CssSelectorContext cssSelector() {
			return getRuleContext(CssSelectorContext.class,0);
		}
		public TerminalNode CSS_LBRACE() { return getToken(FrontendParser.CSS_LBRACE, 0); }
		public TerminalNode CSS_RBRACE() { return getToken(FrontendParser.CSS_RBRACE, 0); }
		public List<CssDeclarationContext> cssDeclaration() {
			return getRuleContexts(CssDeclarationContext.class);
		}
		public CssDeclarationContext cssDeclaration(int i) {
			return getRuleContext(CssDeclarationContext.class,i);
		}
		public CssRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cssRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterCssRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitCssRule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitCssRule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CssRuleContext cssRule() throws RecognitionException {
		CssRuleContext _localctx = new CssRuleContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_cssRule);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(133);
			cssSelector();
			setState(134);
			match(CSS_LBRACE);
			setState(138);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CSS_IDENT || _la==IDENTIFIER) {
				{
				{
				setState(135);
				cssDeclaration();
				}
				}
				setState(140);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(141);
			match(CSS_RBRACE);
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
	public static class CssSelectorContext extends ParserRuleContext {
		public List<CssSelectorPartContext> cssSelectorPart() {
			return getRuleContexts(CssSelectorPartContext.class);
		}
		public CssSelectorPartContext cssSelectorPart(int i) {
			return getRuleContext(CssSelectorPartContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(FrontendParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(FrontendParser.COMMA, i);
		}
		public CssSelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cssSelector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterCssSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitCssSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitCssSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CssSelectorContext cssSelector() throws RecognitionException {
		CssSelectorContext _localctx = new CssSelectorContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_cssSelector);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(143);
			cssSelectorPart();
			setState(148);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(144);
				match(COMMA);
				setState(145);
				cssSelectorPart();
				}
				}
				setState(150);
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
	public static class CssSelectorPartContext extends ParserRuleContext {
		public List<CssSelectorItemContext> cssSelectorItem() {
			return getRuleContexts(CssSelectorItemContext.class);
		}
		public CssSelectorItemContext cssSelectorItem(int i) {
			return getRuleContext(CssSelectorItemContext.class,i);
		}
		public CssSelectorPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cssSelectorPart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterCssSelectorPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitCssSelectorPart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitCssSelectorPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CssSelectorPartContext cssSelectorPart() throws RecognitionException {
		CssSelectorPartContext _localctx = new CssSelectorPartContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_cssSelectorPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(152); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(151);
				cssSelectorItem();
				}
				}
				setState(154); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 26972394618880L) != 0) );
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
	public static class CssSelectorItemContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(FrontendParser.IDENTIFIER, 0); }
		public TerminalNode CSS_IDENT() { return getToken(FrontendParser.CSS_IDENT, 0); }
		public TerminalNode DOT() { return getToken(FrontendParser.DOT, 0); }
		public TerminalNode HASH() { return getToken(FrontendParser.HASH, 0); }
		public CssSelectorItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cssSelectorItem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterCssSelectorItem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitCssSelectorItem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitCssSelectorItem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CssSelectorItemContext cssSelectorItem() throws RecognitionException {
		CssSelectorItemContext _localctx = new CssSelectorItemContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_cssSelectorItem);
		try {
			setState(166);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(156);
				match(IDENTIFIER);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(157);
				match(CSS_IDENT);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(158);
				match(DOT);
				setState(159);
				match(IDENTIFIER);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(160);
				match(DOT);
				setState(161);
				match(CSS_IDENT);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(162);
				match(HASH);
				setState(163);
				match(IDENTIFIER);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(164);
				match(HASH);
				setState(165);
				match(CSS_IDENT);
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
	public static class CssDeclarationContext extends ParserRuleContext {
		public CssPropertyNameContext cssPropertyName() {
			return getRuleContext(CssPropertyNameContext.class,0);
		}
		public TerminalNode CSS_COLON() { return getToken(FrontendParser.CSS_COLON, 0); }
		public TerminalNode CSS_SEMICOLON() { return getToken(FrontendParser.CSS_SEMICOLON, 0); }
		public List<CssValueContext> cssValue() {
			return getRuleContexts(CssValueContext.class);
		}
		public CssValueContext cssValue(int i) {
			return getRuleContext(CssValueContext.class,i);
		}
		public CssDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cssDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterCssDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitCssDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitCssDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CssDeclarationContext cssDeclaration() throws RecognitionException {
		CssDeclarationContext _localctx = new CssDeclarationContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_cssDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(168);
			cssPropertyName();
			setState(169);
			match(CSS_COLON);
			setState(171); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(170);
				cssValue();
				}
				}
				setState(173); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 30236569763840L) != 0) );
			setState(175);
			match(CSS_SEMICOLON);
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
	public static class CssPropertyNameContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(FrontendParser.IDENTIFIER, 0); }
		public TerminalNode CSS_IDENT() { return getToken(FrontendParser.CSS_IDENT, 0); }
		public CssPropertyNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cssPropertyName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterCssPropertyName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitCssPropertyName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitCssPropertyName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CssPropertyNameContext cssPropertyName() throws RecognitionException {
		CssPropertyNameContext _localctx = new CssPropertyNameContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_cssPropertyName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(177);
			_la = _input.LA(1);
			if ( !(_la==CSS_IDENT || _la==IDENTIFIER) ) {
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
	public static class CssValueContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(FrontendParser.IDENTIFIER, 0); }
		public TerminalNode CSS_IDENT() { return getToken(FrontendParser.CSS_IDENT, 0); }
		public TerminalNode NUMBER() { return getToken(FrontendParser.NUMBER, 0); }
		public TerminalNode CSS_UNIT() { return getToken(FrontendParser.CSS_UNIT, 0); }
		public TerminalNode HASH() { return getToken(FrontendParser.HASH, 0); }
		public TerminalNode STRING() { return getToken(FrontendParser.STRING, 0); }
		public CssValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cssValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterCssValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitCssValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitCssValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CssValueContext cssValue() throws RecognitionException {
		CssValueContext _localctx = new CssValueContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_cssValue);
		int _la;
		try {
			setState(192);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(179);
				match(IDENTIFIER);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(180);
				match(CSS_IDENT);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(181);
				match(NUMBER);
				setState(183);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==CSS_UNIT) {
					{
					setState(182);
					match(CSS_UNIT);
					}
				}

				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(185);
				match(HASH);
				setState(186);
				match(IDENTIFIER);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(187);
				match(HASH);
				setState(188);
				match(CSS_IDENT);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(189);
				match(HASH);
				setState(190);
				match(NUMBER);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(191);
				match(STRING);
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
	public static class JinjaExpressionContext extends ParserRuleContext {
		public TerminalNode JINJA_EXPR_START() { return getToken(FrontendParser.JINJA_EXPR_START, 0); }
		public JinjaExprContext jinjaExpr() {
			return getRuleContext(JinjaExprContext.class,0);
		}
		public TerminalNode JINJA_EXPR_END() { return getToken(FrontendParser.JINJA_EXPR_END, 0); }
		public JinjaExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterJinjaExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitJinjaExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitJinjaExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaExpressionContext jinjaExpression() throws RecognitionException {
		JinjaExpressionContext _localctx = new JinjaExpressionContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_jinjaExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(194);
			match(JINJA_EXPR_START);
			setState(195);
			jinjaExpr(0);
			setState(196);
			match(JINJA_EXPR_END);
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
	public static class JinjaExprContext extends ParserRuleContext {
		public JinjaExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaExpr; }
	 
		public JinjaExprContext() { }
		public void copyFrom(JinjaExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaNumberLiteralContext extends JinjaExprContext {
		public TerminalNode NUMBER() { return getToken(FrontendParser.NUMBER, 0); }
		public JinjaNumberLiteralContext(JinjaExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterJinjaNumberLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitJinjaNumberLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitJinjaNumberLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaIndexAccessContext extends JinjaExprContext {
		public List<JinjaExprContext> jinjaExpr() {
			return getRuleContexts(JinjaExprContext.class);
		}
		public JinjaExprContext jinjaExpr(int i) {
			return getRuleContext(JinjaExprContext.class,i);
		}
		public TerminalNode LBRACKET() { return getToken(FrontendParser.LBRACKET, 0); }
		public TerminalNode RBRACKET() { return getToken(FrontendParser.RBRACKET, 0); }
		public JinjaIndexAccessContext(JinjaExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterJinjaIndexAccess(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitJinjaIndexAccess(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitJinjaIndexAccess(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaStringLiteralContext extends JinjaExprContext {
		public TerminalNode STRING() { return getToken(FrontendParser.STRING, 0); }
		public JinjaStringLiteralContext(JinjaExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterJinjaStringLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitJinjaStringLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitJinjaStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaIdentifierContext extends JinjaExprContext {
		public TerminalNode IDENTIFIER() { return getToken(FrontendParser.IDENTIFIER, 0); }
		public JinjaIdentifierContext(JinjaExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterJinjaIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitJinjaIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitJinjaIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaMemberAccessContext extends JinjaExprContext {
		public JinjaExprContext jinjaExpr() {
			return getRuleContext(JinjaExprContext.class,0);
		}
		public TerminalNode DOT() { return getToken(FrontendParser.DOT, 0); }
		public TerminalNode IDENTIFIER() { return getToken(FrontendParser.IDENTIFIER, 0); }
		public JinjaMemberAccessContext(JinjaExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterJinjaMemberAccess(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitJinjaMemberAccess(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitJinjaMemberAccess(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaExprContext jinjaExpr() throws RecognitionException {
		return jinjaExpr(0);
	}

	private JinjaExprContext jinjaExpr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		JinjaExprContext _localctx = new JinjaExprContext(_ctx, _parentState);
		JinjaExprContext _prevctx = _localctx;
		int _startState = 34;
		enterRecursionRule(_localctx, 34, RULE_jinjaExpr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(202);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				_localctx = new JinjaIdentifierContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(199);
				match(IDENTIFIER);
				}
				break;
			case STRING:
				{
				_localctx = new JinjaStringLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(200);
				match(STRING);
				}
				break;
			case NUMBER:
				{
				_localctx = new JinjaNumberLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(201);
				match(NUMBER);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(214);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(212);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
					case 1:
						{
						_localctx = new JinjaMemberAccessContext(new JinjaExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_jinjaExpr);
						setState(204);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(205);
						match(DOT);
						setState(206);
						match(IDENTIFIER);
						}
						break;
					case 2:
						{
						_localctx = new JinjaIndexAccessContext(new JinjaExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_jinjaExpr);
						setState(207);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(208);
						match(LBRACKET);
						setState(209);
						jinjaExpr(0);
						setState(210);
						match(RBRACKET);
						}
						break;
					}
					} 
				}
				setState(216);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
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
	public static class JinjaStatementContext extends ParserRuleContext {
		public JinjaIfStatementContext jinjaIfStatement() {
			return getRuleContext(JinjaIfStatementContext.class,0);
		}
		public JinjaForStatementContext jinjaForStatement() {
			return getRuleContext(JinjaForStatementContext.class,0);
		}
		public JinjaBlockStatementContext jinjaBlockStatement() {
			return getRuleContext(JinjaBlockStatementContext.class,0);
		}
		public JinjaStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterJinjaStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitJinjaStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitJinjaStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaStatementContext jinjaStatement() throws RecognitionException {
		JinjaStatementContext _localctx = new JinjaStatementContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_jinjaStatement);
		try {
			setState(220);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(217);
				jinjaIfStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(218);
				jinjaForStatement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(219);
				jinjaBlockStatement();
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
	public static class JinjaIfStatementContext extends ParserRuleContext {
		public List<TerminalNode> JINJA_STMT_START() { return getTokens(FrontendParser.JINJA_STMT_START); }
		public TerminalNode JINJA_STMT_START(int i) {
			return getToken(FrontendParser.JINJA_STMT_START, i);
		}
		public TerminalNode JINJA_IF() { return getToken(FrontendParser.JINJA_IF, 0); }
		public JinjaConditionContext jinjaCondition() {
			return getRuleContext(JinjaConditionContext.class,0);
		}
		public List<TerminalNode> JINJA_STMT_END() { return getTokens(FrontendParser.JINJA_STMT_END); }
		public TerminalNode JINJA_STMT_END(int i) {
			return getToken(FrontendParser.JINJA_STMT_END, i);
		}
		public ContentContext content() {
			return getRuleContext(ContentContext.class,0);
		}
		public TerminalNode JINJA_ENDIF() { return getToken(FrontendParser.JINJA_ENDIF, 0); }
		public List<JinjaElifStatementContext> jinjaElifStatement() {
			return getRuleContexts(JinjaElifStatementContext.class);
		}
		public JinjaElifStatementContext jinjaElifStatement(int i) {
			return getRuleContext(JinjaElifStatementContext.class,i);
		}
		public JinjaElseStatementContext jinjaElseStatement() {
			return getRuleContext(JinjaElseStatementContext.class,0);
		}
		public JinjaIfStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaIfStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterJinjaIfStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitJinjaIfStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitJinjaIfStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaIfStatementContext jinjaIfStatement() throws RecognitionException {
		JinjaIfStatementContext _localctx = new JinjaIfStatementContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_jinjaIfStatement);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(222);
			match(JINJA_STMT_START);
			setState(223);
			match(JINJA_IF);
			setState(224);
			jinjaCondition(0);
			setState(225);
			match(JINJA_STMT_END);
			setState(226);
			content();
			setState(230);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(227);
					jinjaElifStatement();
					}
					} 
				}
				setState(232);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
			}
			setState(234);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				{
				setState(233);
				jinjaElseStatement();
				}
				break;
			}
			setState(236);
			match(JINJA_STMT_START);
			setState(237);
			match(JINJA_ENDIF);
			setState(238);
			match(JINJA_STMT_END);
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
	public static class JinjaElifStatementContext extends ParserRuleContext {
		public TerminalNode JINJA_STMT_START() { return getToken(FrontendParser.JINJA_STMT_START, 0); }
		public TerminalNode JINJA_ELIF() { return getToken(FrontendParser.JINJA_ELIF, 0); }
		public JinjaConditionContext jinjaCondition() {
			return getRuleContext(JinjaConditionContext.class,0);
		}
		public TerminalNode JINJA_STMT_END() { return getToken(FrontendParser.JINJA_STMT_END, 0); }
		public ContentContext content() {
			return getRuleContext(ContentContext.class,0);
		}
		public JinjaElifStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaElifStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterJinjaElifStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitJinjaElifStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitJinjaElifStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaElifStatementContext jinjaElifStatement() throws RecognitionException {
		JinjaElifStatementContext _localctx = new JinjaElifStatementContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_jinjaElifStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(240);
			match(JINJA_STMT_START);
			setState(241);
			match(JINJA_ELIF);
			setState(242);
			jinjaCondition(0);
			setState(243);
			match(JINJA_STMT_END);
			setState(244);
			content();
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
	public static class JinjaElseStatementContext extends ParserRuleContext {
		public TerminalNode JINJA_STMT_START() { return getToken(FrontendParser.JINJA_STMT_START, 0); }
		public TerminalNode JINJA_ELSE() { return getToken(FrontendParser.JINJA_ELSE, 0); }
		public TerminalNode JINJA_STMT_END() { return getToken(FrontendParser.JINJA_STMT_END, 0); }
		public ContentContext content() {
			return getRuleContext(ContentContext.class,0);
		}
		public JinjaElseStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaElseStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterJinjaElseStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitJinjaElseStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitJinjaElseStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaElseStatementContext jinjaElseStatement() throws RecognitionException {
		JinjaElseStatementContext _localctx = new JinjaElseStatementContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_jinjaElseStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(246);
			match(JINJA_STMT_START);
			setState(247);
			match(JINJA_ELSE);
			setState(248);
			match(JINJA_STMT_END);
			setState(249);
			content();
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
	public static class JinjaForStatementContext extends ParserRuleContext {
		public List<TerminalNode> JINJA_STMT_START() { return getTokens(FrontendParser.JINJA_STMT_START); }
		public TerminalNode JINJA_STMT_START(int i) {
			return getToken(FrontendParser.JINJA_STMT_START, i);
		}
		public TerminalNode JINJA_FOR() { return getToken(FrontendParser.JINJA_FOR, 0); }
		public TerminalNode IDENTIFIER() { return getToken(FrontendParser.IDENTIFIER, 0); }
		public TerminalNode JINJA_IN() { return getToken(FrontendParser.JINJA_IN, 0); }
		public JinjaStmtExprContext jinjaStmtExpr() {
			return getRuleContext(JinjaStmtExprContext.class,0);
		}
		public List<TerminalNode> JINJA_STMT_END() { return getTokens(FrontendParser.JINJA_STMT_END); }
		public TerminalNode JINJA_STMT_END(int i) {
			return getToken(FrontendParser.JINJA_STMT_END, i);
		}
		public ContentContext content() {
			return getRuleContext(ContentContext.class,0);
		}
		public TerminalNode JINJA_ENDFOR() { return getToken(FrontendParser.JINJA_ENDFOR, 0); }
		public JinjaForStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaForStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterJinjaForStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitJinjaForStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitJinjaForStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaForStatementContext jinjaForStatement() throws RecognitionException {
		JinjaForStatementContext _localctx = new JinjaForStatementContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_jinjaForStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(251);
			match(JINJA_STMT_START);
			setState(252);
			match(JINJA_FOR);
			setState(253);
			match(IDENTIFIER);
			setState(254);
			match(JINJA_IN);
			setState(255);
			jinjaStmtExpr(0);
			setState(256);
			match(JINJA_STMT_END);
			setState(257);
			content();
			setState(258);
			match(JINJA_STMT_START);
			setState(259);
			match(JINJA_ENDFOR);
			setState(260);
			match(JINJA_STMT_END);
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
	public static class JinjaExtendsStatementContext extends ParserRuleContext {
		public TerminalNode JINJA_STMT_START() { return getToken(FrontendParser.JINJA_STMT_START, 0); }
		public TerminalNode JINJA_EXTENDS() { return getToken(FrontendParser.JINJA_EXTENDS, 0); }
		public TerminalNode STRING() { return getToken(FrontendParser.STRING, 0); }
		public TerminalNode JINJA_STMT_END() { return getToken(FrontendParser.JINJA_STMT_END, 0); }
		public JinjaExtendsStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaExtendsStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterJinjaExtendsStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitJinjaExtendsStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitJinjaExtendsStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaExtendsStatementContext jinjaExtendsStatement() throws RecognitionException {
		JinjaExtendsStatementContext _localctx = new JinjaExtendsStatementContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_jinjaExtendsStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(262);
			match(JINJA_STMT_START);
			setState(263);
			match(JINJA_EXTENDS);
			setState(264);
			match(STRING);
			setState(265);
			match(JINJA_STMT_END);
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
	public static class JinjaBlockStatementContext extends ParserRuleContext {
		public List<TerminalNode> JINJA_STMT_START() { return getTokens(FrontendParser.JINJA_STMT_START); }
		public TerminalNode JINJA_STMT_START(int i) {
			return getToken(FrontendParser.JINJA_STMT_START, i);
		}
		public TerminalNode JINJA_BLOCK() { return getToken(FrontendParser.JINJA_BLOCK, 0); }
		public TerminalNode IDENTIFIER() { return getToken(FrontendParser.IDENTIFIER, 0); }
		public List<TerminalNode> JINJA_STMT_END() { return getTokens(FrontendParser.JINJA_STMT_END); }
		public TerminalNode JINJA_STMT_END(int i) {
			return getToken(FrontendParser.JINJA_STMT_END, i);
		}
		public ContentContext content() {
			return getRuleContext(ContentContext.class,0);
		}
		public TerminalNode JINJA_ENDBLOCK() { return getToken(FrontendParser.JINJA_ENDBLOCK, 0); }
		public JinjaBlockStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaBlockStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterJinjaBlockStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitJinjaBlockStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitJinjaBlockStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaBlockStatementContext jinjaBlockStatement() throws RecognitionException {
		JinjaBlockStatementContext _localctx = new JinjaBlockStatementContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_jinjaBlockStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(267);
			match(JINJA_STMT_START);
			setState(268);
			match(JINJA_BLOCK);
			setState(269);
			match(IDENTIFIER);
			setState(270);
			match(JINJA_STMT_END);
			setState(271);
			content();
			setState(272);
			match(JINJA_STMT_START);
			setState(273);
			match(JINJA_ENDBLOCK);
			setState(274);
			match(JINJA_STMT_END);
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
	public static class JinjaConditionContext extends ParserRuleContext {
		public List<JinjaStmtExprContext> jinjaStmtExpr() {
			return getRuleContexts(JinjaStmtExprContext.class);
		}
		public JinjaStmtExprContext jinjaStmtExpr(int i) {
			return getRuleContext(JinjaStmtExprContext.class,i);
		}
		public TerminalNode JINJA_EQ() { return getToken(FrontendParser.JINJA_EQ, 0); }
		public TerminalNode JINJA_NEQ() { return getToken(FrontendParser.JINJA_NEQ, 0); }
		public TerminalNode JINJA_LT() { return getToken(FrontendParser.JINJA_LT, 0); }
		public TerminalNode JINJA_GT() { return getToken(FrontendParser.JINJA_GT, 0); }
		public TerminalNode JINJA_LTE() { return getToken(FrontendParser.JINJA_LTE, 0); }
		public TerminalNode JINJA_GTE() { return getToken(FrontendParser.JINJA_GTE, 0); }
		public TerminalNode JINJA_NOT() { return getToken(FrontendParser.JINJA_NOT, 0); }
		public List<JinjaConditionContext> jinjaCondition() {
			return getRuleContexts(JinjaConditionContext.class);
		}
		public JinjaConditionContext jinjaCondition(int i) {
			return getRuleContext(JinjaConditionContext.class,i);
		}
		public TerminalNode JINJA_AND() { return getToken(FrontendParser.JINJA_AND, 0); }
		public TerminalNode JINJA_OR() { return getToken(FrontendParser.JINJA_OR, 0); }
		public JinjaConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaCondition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterJinjaCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitJinjaCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitJinjaCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaConditionContext jinjaCondition() throws RecognitionException {
		return jinjaCondition(0);
	}

	private JinjaConditionContext jinjaCondition(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		JinjaConditionContext _localctx = new JinjaConditionContext(_ctx, _parentState);
		JinjaConditionContext _prevctx = _localctx;
		int _startState = 50;
		enterRecursionRule(_localctx, 50, RULE_jinjaCondition, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(284);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING:
			case NUMBER:
			case IDENTIFIER:
				{
				setState(277);
				jinjaStmtExpr(0);
				setState(280);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
				case 1:
					{
					setState(278);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 132120576L) != 0)) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(279);
					jinjaStmtExpr(0);
					}
					break;
				}
				}
				break;
			case JINJA_NOT:
				{
				setState(282);
				match(JINJA_NOT);
				setState(283);
				jinjaCondition(1);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(294);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(292);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
					case 1:
						{
						_localctx = new JinjaConditionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_jinjaCondition);
						setState(286);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(287);
						match(JINJA_AND);
						setState(288);
						jinjaCondition(4);
						}
						break;
					case 2:
						{
						_localctx = new JinjaConditionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_jinjaCondition);
						setState(289);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(290);
						match(JINJA_OR);
						setState(291);
						jinjaCondition(3);
						}
						break;
					}
					} 
				}
				setState(296);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
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
	public static class JinjaStmtExprContext extends ParserRuleContext {
		public JinjaStmtExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jinjaStmtExpr; }
	 
		public JinjaStmtExprContext() { }
		public void copyFrom(JinjaStmtExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaStmtStringLiteralContext extends JinjaStmtExprContext {
		public TerminalNode STRING() { return getToken(FrontendParser.STRING, 0); }
		public JinjaStmtStringLiteralContext(JinjaStmtExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterJinjaStmtStringLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitJinjaStmtStringLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitJinjaStmtStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaStmtIdentifierContext extends JinjaStmtExprContext {
		public TerminalNode IDENTIFIER() { return getToken(FrontendParser.IDENTIFIER, 0); }
		public JinjaStmtIdentifierContext(JinjaStmtExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterJinjaStmtIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitJinjaStmtIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitJinjaStmtIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaStmtIndexAccessContext extends JinjaStmtExprContext {
		public List<JinjaStmtExprContext> jinjaStmtExpr() {
			return getRuleContexts(JinjaStmtExprContext.class);
		}
		public JinjaStmtExprContext jinjaStmtExpr(int i) {
			return getRuleContext(JinjaStmtExprContext.class,i);
		}
		public TerminalNode LBRACKET() { return getToken(FrontendParser.LBRACKET, 0); }
		public TerminalNode RBRACKET() { return getToken(FrontendParser.RBRACKET, 0); }
		public JinjaStmtIndexAccessContext(JinjaStmtExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterJinjaStmtIndexAccess(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitJinjaStmtIndexAccess(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitJinjaStmtIndexAccess(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaStmtNumberLiteralContext extends JinjaStmtExprContext {
		public TerminalNode NUMBER() { return getToken(FrontendParser.NUMBER, 0); }
		public JinjaStmtNumberLiteralContext(JinjaStmtExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterJinjaStmtNumberLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitJinjaStmtNumberLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitJinjaStmtNumberLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class JinjaStmtMemberAccessContext extends JinjaStmtExprContext {
		public JinjaStmtExprContext jinjaStmtExpr() {
			return getRuleContext(JinjaStmtExprContext.class,0);
		}
		public TerminalNode DOT() { return getToken(FrontendParser.DOT, 0); }
		public TerminalNode IDENTIFIER() { return getToken(FrontendParser.IDENTIFIER, 0); }
		public JinjaStmtMemberAccessContext(JinjaStmtExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterJinjaStmtMemberAccess(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitJinjaStmtMemberAccess(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitJinjaStmtMemberAccess(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JinjaStmtExprContext jinjaStmtExpr() throws RecognitionException {
		return jinjaStmtExpr(0);
	}

	private JinjaStmtExprContext jinjaStmtExpr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		JinjaStmtExprContext _localctx = new JinjaStmtExprContext(_ctx, _parentState);
		JinjaStmtExprContext _prevctx = _localctx;
		int _startState = 52;
		enterRecursionRule(_localctx, 52, RULE_jinjaStmtExpr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(301);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				_localctx = new JinjaStmtIdentifierContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(298);
				match(IDENTIFIER);
				}
				break;
			case STRING:
				{
				_localctx = new JinjaStmtStringLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(299);
				match(STRING);
				}
				break;
			case NUMBER:
				{
				_localctx = new JinjaStmtNumberLiteralContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(300);
				match(NUMBER);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(313);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(311);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
					case 1:
						{
						_localctx = new JinjaStmtMemberAccessContext(new JinjaStmtExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_jinjaStmtExpr);
						setState(303);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(304);
						match(DOT);
						setState(305);
						match(IDENTIFIER);
						}
						break;
					case 2:
						{
						_localctx = new JinjaStmtIndexAccessContext(new JinjaStmtExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_jinjaStmtExpr);
						setState(306);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(307);
						match(LBRACKET);
						setState(308);
						jinjaStmtExpr(0);
						setState(309);
						match(RBRACKET);
						}
						break;
					}
					} 
				}
				setState(315);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
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
	public static class TextContext extends ParserRuleContext {
		public TerminalNode TEXT() { return getToken(FrontendParser.TEXT, 0); }
		public TerminalNode NUMBER() { return getToken(FrontendParser.NUMBER, 0); }
		public TerminalNode IDENTIFIER() { return getToken(FrontendParser.IDENTIFIER, 0); }
		public TerminalNode CSS_IDENT() { return getToken(FrontendParser.CSS_IDENT, 0); }
		public TextContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_text; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).enterText(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof FrontendParserListener ) ((FrontendParserListener)listener).exitText(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof FrontendParserVisitor ) return ((FrontendParserVisitor<? extends T>)visitor).visitText(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TextContext text() throws RecognitionException {
		TextContext _localctx = new TextContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_text);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(316);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 63771674411008L) != 0)) ) {
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 17:
			return jinjaExpr_sempred((JinjaExprContext)_localctx, predIndex);
		case 25:
			return jinjaCondition_sempred((JinjaConditionContext)_localctx, predIndex);
		case 26:
			return jinjaStmtExpr_sempred((JinjaStmtExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean jinjaExpr_sempred(JinjaExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 4);
		case 1:
			return precpred(_ctx, 3);
		}
		return true;
	}
	private boolean jinjaCondition_sempred(JinjaConditionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return precpred(_ctx, 3);
		case 3:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean jinjaStmtExpr_sempred(JinjaStmtExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 4:
			return precpred(_ctx, 4);
		case 5:
			return precpred(_ctx, 3);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u00010\u013f\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0001\u0000\u0003\u0000:\b\u0000\u0001\u0000\u0003\u0000=\b\u0000\u0001"+
		"\u0000\u0003\u0000@\b\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0005"+
		"\u0001E\b\u0001\n\u0001\f\u0001H\t\u0001\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0003\u0002N\b\u0002\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0005\u0003S\b\u0003\n\u0003\f\u0003V\t\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0005\u0003b\b\u0003\n\u0003\f\u0003e\t"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003k\b"+
		"\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0003"+
		"\u0005r\b\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0005\b|\b\b\n\b\f\b\u007f\t\b\u0001\b\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0005\t\u0089\b\t\n"+
		"\t\f\t\u008c\t\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0005\n\u0093"+
		"\b\n\n\n\f\n\u0096\t\n\u0001\u000b\u0004\u000b\u0099\b\u000b\u000b\u000b"+
		"\f\u000b\u009a\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f"+
		"\u0001\f\u0001\f\u0001\f\u0003\f\u00a7\b\f\u0001\r\u0001\r\u0001\r\u0004"+
		"\r\u00ac\b\r\u000b\r\f\r\u00ad\u0001\r\u0001\r\u0001\u000e\u0001\u000e"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u00b8\b\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0003\u000f\u00c1\b\u000f\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0003\u0011"+
		"\u00cb\b\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0001\u0011\u0005\u0011\u00d5\b\u0011\n\u0011"+
		"\f\u0011\u00d8\t\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012"+
		"\u00dd\b\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0005\u0013\u00e5\b\u0013\n\u0013\f\u0013\u00e8\t\u0013\u0001"+
		"\u0013\u0003\u0013\u00eb\b\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u0119"+
		"\b\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u011d\b\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0005\u0019"+
		"\u0125\b\u0019\n\u0019\f\u0019\u0128\t\u0019\u0001\u001a\u0001\u001a\u0001"+
		"\u001a\u0001\u001a\u0003\u001a\u012e\b\u001a\u0001\u001a\u0001\u001a\u0001"+
		"\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0005"+
		"\u001a\u0138\b\u001a\n\u001a\f\u001a\u013b\t\u001a\u0001\u001b\u0001\u001b"+
		"\u0001\u001b\u0000\u0003\"24\u001c\u0000\u0002\u0004\u0006\b\n\f\u000e"+
		"\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.0246\u0000\u0004"+
		"\u0001\u0000+,\u0002\u0000((+,\u0001\u0000\u0015\u001a\u0002\u0000))+"+
		"-\u014f\u00009\u0001\u0000\u0000\u0000\u0002F\u0001\u0000\u0000\u0000"+
		"\u0004M\u0001\u0000\u0000\u0000\u0006j\u0001\u0000\u0000\u0000\bl\u0001"+
		"\u0000\u0000\u0000\nn\u0001\u0000\u0000\u0000\fs\u0001\u0000\u0000\u0000"+
		"\u000eu\u0001\u0000\u0000\u0000\u0010w\u0001\u0000\u0000\u0000\u0012\u0085"+
		"\u0001\u0000\u0000\u0000\u0014\u008f\u0001\u0000\u0000\u0000\u0016\u0098"+
		"\u0001\u0000\u0000\u0000\u0018\u00a6\u0001\u0000\u0000\u0000\u001a\u00a8"+
		"\u0001\u0000\u0000\u0000\u001c\u00b1\u0001\u0000\u0000\u0000\u001e\u00c0"+
		"\u0001\u0000\u0000\u0000 \u00c2\u0001\u0000\u0000\u0000\"\u00ca\u0001"+
		"\u0000\u0000\u0000$\u00dc\u0001\u0000\u0000\u0000&\u00de\u0001\u0000\u0000"+
		"\u0000(\u00f0\u0001\u0000\u0000\u0000*\u00f6\u0001\u0000\u0000\u0000,"+
		"\u00fb\u0001\u0000\u0000\u0000.\u0106\u0001\u0000\u0000\u00000\u010b\u0001"+
		"\u0000\u0000\u00002\u011c\u0001\u0000\u0000\u00004\u012d\u0001\u0000\u0000"+
		"\u00006\u013c\u0001\u0000\u0000\u00008:\u0003.\u0017\u000098\u0001\u0000"+
		"\u0000\u00009:\u0001\u0000\u0000\u0000:<\u0001\u0000\u0000\u0000;=\u0005"+
		"\u0001\u0000\u0000<;\u0001\u0000\u0000\u0000<=\u0001\u0000\u0000\u0000"+
		"=?\u0001\u0000\u0000\u0000>@\u0003\u0002\u0001\u0000?>\u0001\u0000\u0000"+
		"\u0000?@\u0001\u0000\u0000\u0000@A\u0001\u0000\u0000\u0000AB\u0005\u0000"+
		"\u0000\u0001B\u0001\u0001\u0000\u0000\u0000CE\u0003\u0004\u0002\u0000"+
		"DC\u0001\u0000\u0000\u0000EH\u0001\u0000\u0000\u0000FD\u0001\u0000\u0000"+
		"\u0000FG\u0001\u0000\u0000\u0000G\u0003\u0001\u0000\u0000\u0000HF\u0001"+
		"\u0000\u0000\u0000IN\u0003\u0006\u0003\u0000JN\u0003 \u0010\u0000KN\u0003"+
		"$\u0012\u0000LN\u00036\u001b\u0000MI\u0001\u0000\u0000\u0000MJ\u0001\u0000"+
		"\u0000\u0000MK\u0001\u0000\u0000\u0000ML\u0001\u0000\u0000\u0000N\u0005"+
		"\u0001\u0000\u0000\u0000OP\u0005\u0011\u0000\u0000PT\u0003\b\u0004\u0000"+
		"QS\u0003\n\u0005\u0000RQ\u0001\u0000\u0000\u0000SV\u0001\u0000\u0000\u0000"+
		"TR\u0001\u0000\u0000\u0000TU\u0001\u0000\u0000\u0000UW\u0001\u0000\u0000"+
		"\u0000VT\u0001\u0000\u0000\u0000WX\u0005\u0012\u0000\u0000XY\u0003\u0002"+
		"\u0001\u0000YZ\u0005\u0011\u0000\u0000Z[\u0005\u0013\u0000\u0000[\\\u0003"+
		"\b\u0004\u0000\\]\u0005\u0012\u0000\u0000]k\u0001\u0000\u0000\u0000^_"+
		"\u0005\u0011\u0000\u0000_c\u0003\b\u0004\u0000`b\u0003\n\u0005\u0000a"+
		"`\u0001\u0000\u0000\u0000be\u0001\u0000\u0000\u0000ca\u0001\u0000\u0000"+
		"\u0000cd\u0001\u0000\u0000\u0000df\u0001\u0000\u0000\u0000ec\u0001\u0000"+
		"\u0000\u0000fg\u0005\u0013\u0000\u0000gh\u0005\u0012\u0000\u0000hk\u0001"+
		"\u0000\u0000\u0000ik\u0003\u0010\b\u0000jO\u0001\u0000\u0000\u0000j^\u0001"+
		"\u0000\u0000\u0000ji\u0001\u0000\u0000\u0000k\u0007\u0001\u0000\u0000"+
		"\u0000lm\u0007\u0000\u0000\u0000m\t\u0001\u0000\u0000\u0000nq\u0003\f"+
		"\u0006\u0000op\u0005\"\u0000\u0000pr\u0003\u000e\u0007\u0000qo\u0001\u0000"+
		"\u0000\u0000qr\u0001\u0000\u0000\u0000r\u000b\u0001\u0000\u0000\u0000"+
		"st\u0007\u0000\u0000\u0000t\r\u0001\u0000\u0000\u0000uv\u0007\u0001\u0000"+
		"\u0000v\u000f\u0001\u0000\u0000\u0000wx\u0005\u0011\u0000\u0000xy\u0005"+
		"\u0014\u0000\u0000y}\u0005\u0012\u0000\u0000z|\u0003\u0012\t\u0000{z\u0001"+
		"\u0000\u0000\u0000|\u007f\u0001\u0000\u0000\u0000}{\u0001\u0000\u0000"+
		"\u0000}~\u0001\u0000\u0000\u0000~\u0080\u0001\u0000\u0000\u0000\u007f"+
		"}\u0001\u0000\u0000\u0000\u0080\u0081\u0005\u0011\u0000\u0000\u0081\u0082"+
		"\u0005\u0013\u0000\u0000\u0082\u0083\u0005\u0014\u0000\u0000\u0083\u0084"+
		"\u0005\u0012\u0000\u0000\u0084\u0011\u0001\u0000\u0000\u0000\u0085\u0086"+
		"\u0003\u0014\n\u0000\u0086\u008a\u0005\u001e\u0000\u0000\u0087\u0089\u0003"+
		"\u001a\r\u0000\u0088\u0087\u0001\u0000\u0000\u0000\u0089\u008c\u0001\u0000"+
		"\u0000\u0000\u008a\u0088\u0001\u0000\u0000\u0000\u008a\u008b\u0001\u0000"+
		"\u0000\u0000\u008b\u008d\u0001\u0000\u0000\u0000\u008c\u008a\u0001\u0000"+
		"\u0000\u0000\u008d\u008e\u0005\u001f\u0000\u0000\u008e\u0013\u0001\u0000"+
		"\u0000\u0000\u008f\u0094\u0003\u0016\u000b\u0000\u0090\u0091\u0005&\u0000"+
		"\u0000\u0091\u0093\u0003\u0016\u000b\u0000\u0092\u0090\u0001\u0000\u0000"+
		"\u0000\u0093\u0096\u0001\u0000\u0000\u0000\u0094\u0092\u0001\u0000\u0000"+
		"\u0000\u0094\u0095\u0001\u0000\u0000\u0000\u0095\u0015\u0001\u0000\u0000"+
		"\u0000\u0096\u0094\u0001\u0000\u0000\u0000\u0097\u0099\u0003\u0018\f\u0000"+
		"\u0098\u0097\u0001\u0000\u0000\u0000\u0099\u009a\u0001\u0000\u0000\u0000"+
		"\u009a\u0098\u0001\u0000\u0000\u0000\u009a\u009b\u0001\u0000\u0000\u0000"+
		"\u009b\u0017\u0001\u0000\u0000\u0000\u009c\u00a7\u0005,\u0000\u0000\u009d"+
		"\u00a7\u0005+\u0000\u0000\u009e\u009f\u0005#\u0000\u0000\u009f\u00a7\u0005"+
		",\u0000\u0000\u00a0\u00a1\u0005#\u0000\u0000\u00a1\u00a7\u0005+\u0000"+
		"\u0000\u00a2\u00a3\u0005\'\u0000\u0000\u00a3\u00a7\u0005,\u0000\u0000"+
		"\u00a4\u00a5\u0005\'\u0000\u0000\u00a5\u00a7\u0005+\u0000\u0000\u00a6"+
		"\u009c\u0001\u0000\u0000\u0000\u00a6\u009d\u0001\u0000\u0000\u0000\u00a6"+
		"\u009e\u0001\u0000\u0000\u0000\u00a6\u00a0\u0001\u0000\u0000\u0000\u00a6"+
		"\u00a2\u0001\u0000\u0000\u0000\u00a6\u00a4\u0001\u0000\u0000\u0000\u00a7"+
		"\u0019\u0001\u0000\u0000\u0000\u00a8\u00a9\u0003\u001c\u000e\u0000\u00a9"+
		"\u00ab\u0005 \u0000\u0000\u00aa\u00ac\u0003\u001e\u000f\u0000\u00ab\u00aa"+
		"\u0001\u0000\u0000\u0000\u00ac\u00ad\u0001\u0000\u0000\u0000\u00ad\u00ab"+
		"\u0001\u0000\u0000\u0000\u00ad\u00ae\u0001\u0000\u0000\u0000\u00ae\u00af"+
		"\u0001\u0000\u0000\u0000\u00af\u00b0\u0005!\u0000\u0000\u00b0\u001b\u0001"+
		"\u0000\u0000\u0000\u00b1\u00b2\u0007\u0000\u0000\u0000\u00b2\u001d\u0001"+
		"\u0000\u0000\u0000\u00b3\u00c1\u0005,\u0000\u0000\u00b4\u00c1\u0005+\u0000"+
		"\u0000\u00b5\u00b7\u0005)\u0000\u0000\u00b6\u00b8\u0005*\u0000\u0000\u00b7"+
		"\u00b6\u0001\u0000\u0000\u0000\u00b7\u00b8\u0001\u0000\u0000\u0000\u00b8"+
		"\u00c1\u0001\u0000\u0000\u0000\u00b9\u00ba\u0005\'\u0000\u0000\u00ba\u00c1"+
		"\u0005,\u0000\u0000\u00bb\u00bc\u0005\'\u0000\u0000\u00bc\u00c1\u0005"+
		"+\u0000\u0000\u00bd\u00be\u0005\'\u0000\u0000\u00be\u00c1\u0005)\u0000"+
		"\u0000\u00bf\u00c1\u0005(\u0000\u0000\u00c0\u00b3\u0001\u0000\u0000\u0000"+
		"\u00c0\u00b4\u0001\u0000\u0000\u0000\u00c0\u00b5\u0001\u0000\u0000\u0000"+
		"\u00c0\u00b9\u0001\u0000\u0000\u0000\u00c0\u00bb\u0001\u0000\u0000\u0000"+
		"\u00c0\u00bd\u0001\u0000\u0000\u0000\u00c0\u00bf\u0001\u0000\u0000\u0000"+
		"\u00c1\u001f\u0001\u0000\u0000\u0000\u00c2\u00c3\u0005\u0002\u0000\u0000"+
		"\u00c3\u00c4\u0003\"\u0011\u0000\u00c4\u00c5\u0005\u0003\u0000\u0000\u00c5"+
		"!\u0001\u0000\u0000\u0000\u00c6\u00c7\u0006\u0011\uffff\uffff\u0000\u00c7"+
		"\u00cb\u0005,\u0000\u0000\u00c8\u00cb\u0005(\u0000\u0000\u00c9\u00cb\u0005"+
		")\u0000\u0000\u00ca\u00c6\u0001\u0000\u0000\u0000\u00ca\u00c8\u0001\u0000"+
		"\u0000\u0000\u00ca\u00c9\u0001\u0000\u0000\u0000\u00cb\u00d6\u0001\u0000"+
		"\u0000\u0000\u00cc\u00cd\n\u0004\u0000\u0000\u00cd\u00ce\u0005#\u0000"+
		"\u0000\u00ce\u00d5\u0005,\u0000\u0000\u00cf\u00d0\n\u0003\u0000\u0000"+
		"\u00d0\u00d1\u0005$\u0000\u0000\u00d1\u00d2\u0003\"\u0011\u0000\u00d2"+
		"\u00d3\u0005%\u0000\u0000\u00d3\u00d5\u0001\u0000\u0000\u0000\u00d4\u00cc"+
		"\u0001\u0000\u0000\u0000\u00d4\u00cf\u0001\u0000\u0000\u0000\u00d5\u00d8"+
		"\u0001\u0000\u0000\u0000\u00d6\u00d4\u0001\u0000\u0000\u0000\u00d6\u00d7"+
		"\u0001\u0000\u0000\u0000\u00d7#\u0001\u0000\u0000\u0000\u00d8\u00d6\u0001"+
		"\u0000\u0000\u0000\u00d9\u00dd\u0003&\u0013\u0000\u00da\u00dd\u0003,\u0016"+
		"\u0000\u00db\u00dd\u00030\u0018\u0000\u00dc\u00d9\u0001\u0000\u0000\u0000"+
		"\u00dc\u00da\u0001\u0000\u0000\u0000\u00dc\u00db\u0001\u0000\u0000\u0000"+
		"\u00dd%\u0001\u0000\u0000\u0000\u00de\u00df\u0005\u0004\u0000\u0000\u00df"+
		"\u00e0\u0005\u0007\u0000\u0000\u00e0\u00e1\u00032\u0019\u0000\u00e1\u00e2"+
		"\u0005\u0005\u0000\u0000\u00e2\u00e6\u0003\u0002\u0001\u0000\u00e3\u00e5"+
		"\u0003(\u0014\u0000\u00e4\u00e3\u0001\u0000\u0000\u0000\u00e5\u00e8\u0001"+
		"\u0000\u0000\u0000\u00e6\u00e4\u0001\u0000\u0000\u0000\u00e6\u00e7\u0001"+
		"\u0000\u0000\u0000\u00e7\u00ea\u0001\u0000\u0000\u0000\u00e8\u00e6\u0001"+
		"\u0000\u0000\u0000\u00e9\u00eb\u0003*\u0015\u0000\u00ea\u00e9\u0001\u0000"+
		"\u0000\u0000\u00ea\u00eb\u0001\u0000\u0000\u0000\u00eb\u00ec\u0001\u0000"+
		"\u0000\u0000\u00ec\u00ed\u0005\u0004\u0000\u0000\u00ed\u00ee\u0005\n\u0000"+
		"\u0000\u00ee\u00ef\u0005\u0005\u0000\u0000\u00ef\'\u0001\u0000\u0000\u0000"+
		"\u00f0\u00f1\u0005\u0004\u0000\u0000\u00f1\u00f2\u0005\b\u0000\u0000\u00f2"+
		"\u00f3\u00032\u0019\u0000\u00f3\u00f4\u0005\u0005\u0000\u0000\u00f4\u00f5"+
		"\u0003\u0002\u0001\u0000\u00f5)\u0001\u0000\u0000\u0000\u00f6\u00f7\u0005"+
		"\u0004\u0000\u0000\u00f7\u00f8\u0005\t\u0000\u0000\u00f8\u00f9\u0005\u0005"+
		"\u0000\u0000\u00f9\u00fa\u0003\u0002\u0001\u0000\u00fa+\u0001\u0000\u0000"+
		"\u0000\u00fb\u00fc\u0005\u0004\u0000\u0000\u00fc\u00fd\u0005\u000b\u0000"+
		"\u0000\u00fd\u00fe\u0005,\u0000\u0000\u00fe\u00ff\u0005\f\u0000\u0000"+
		"\u00ff\u0100\u00034\u001a\u0000\u0100\u0101\u0005\u0005\u0000\u0000\u0101"+
		"\u0102\u0003\u0002\u0001\u0000\u0102\u0103\u0005\u0004\u0000\u0000\u0103"+
		"\u0104\u0005\r\u0000\u0000\u0104\u0105\u0005\u0005\u0000\u0000\u0105-"+
		"\u0001\u0000\u0000\u0000\u0106\u0107\u0005\u0004\u0000\u0000\u0107\u0108"+
		"\u0005\u000e\u0000\u0000\u0108\u0109\u0005(\u0000\u0000\u0109\u010a\u0005"+
		"\u0005\u0000\u0000\u010a/\u0001\u0000\u0000\u0000\u010b\u010c\u0005\u0004"+
		"\u0000\u0000\u010c\u010d\u0005\u000f\u0000\u0000\u010d\u010e\u0005,\u0000"+
		"\u0000\u010e\u010f\u0005\u0005\u0000\u0000\u010f\u0110\u0003\u0002\u0001"+
		"\u0000\u0110\u0111\u0005\u0004\u0000\u0000\u0111\u0112\u0005\u0010\u0000"+
		"\u0000\u0112\u0113\u0005\u0005\u0000\u0000\u01131\u0001\u0000\u0000\u0000"+
		"\u0114\u0115\u0006\u0019\uffff\uffff\u0000\u0115\u0118\u00034\u001a\u0000"+
		"\u0116\u0117\u0007\u0002\u0000\u0000\u0117\u0119\u00034\u001a\u0000\u0118"+
		"\u0116\u0001\u0000\u0000\u0000\u0118\u0119\u0001\u0000\u0000\u0000\u0119"+
		"\u011d\u0001\u0000\u0000\u0000\u011a\u011b\u0005\u001d\u0000\u0000\u011b"+
		"\u011d\u00032\u0019\u0001\u011c\u0114\u0001\u0000\u0000\u0000\u011c\u011a"+
		"\u0001\u0000\u0000\u0000\u011d\u0126\u0001\u0000\u0000\u0000\u011e\u011f"+
		"\n\u0003\u0000\u0000\u011f\u0120\u0005\u001b\u0000\u0000\u0120\u0125\u0003"+
		"2\u0019\u0004\u0121\u0122\n\u0002\u0000\u0000\u0122\u0123\u0005\u001c"+
		"\u0000\u0000\u0123\u0125\u00032\u0019\u0003\u0124\u011e\u0001\u0000\u0000"+
		"\u0000\u0124\u0121\u0001\u0000\u0000\u0000\u0125\u0128\u0001\u0000\u0000"+
		"\u0000\u0126\u0124\u0001\u0000\u0000\u0000\u0126\u0127\u0001\u0000\u0000"+
		"\u0000\u01273\u0001\u0000\u0000\u0000\u0128\u0126\u0001\u0000\u0000\u0000"+
		"\u0129\u012a\u0006\u001a\uffff\uffff\u0000\u012a\u012e\u0005,\u0000\u0000"+
		"\u012b\u012e\u0005(\u0000\u0000\u012c\u012e\u0005)\u0000\u0000\u012d\u0129"+
		"\u0001\u0000\u0000\u0000\u012d\u012b\u0001\u0000\u0000\u0000\u012d\u012c"+
		"\u0001\u0000\u0000\u0000\u012e\u0139\u0001\u0000\u0000\u0000\u012f\u0130"+
		"\n\u0004\u0000\u0000\u0130\u0131\u0005#\u0000\u0000\u0131\u0138\u0005"+
		",\u0000\u0000\u0132\u0133\n\u0003\u0000\u0000\u0133\u0134\u0005$\u0000"+
		"\u0000\u0134\u0135\u00034\u001a\u0000\u0135\u0136\u0005%\u0000\u0000\u0136"+
		"\u0138\u0001\u0000\u0000\u0000\u0137\u012f\u0001\u0000\u0000\u0000\u0137"+
		"\u0132\u0001\u0000\u0000\u0000\u0138\u013b\u0001\u0000\u0000\u0000\u0139"+
		"\u0137\u0001\u0000\u0000\u0000\u0139\u013a\u0001\u0000\u0000\u0000\u013a"+
		"5\u0001\u0000\u0000\u0000\u013b\u0139\u0001\u0000\u0000\u0000\u013c\u013d"+
		"\u0007\u0003\u0000\u0000\u013d7\u0001\u0000\u0000\u0000\u001e9<?FMTcj"+
		"q}\u008a\u0094\u009a\u00a6\u00ad\u00b7\u00c0\u00ca\u00d4\u00d6\u00dc\u00e6"+
		"\u00ea\u0118\u011c\u0124\u0126\u012d\u0137\u0139";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}