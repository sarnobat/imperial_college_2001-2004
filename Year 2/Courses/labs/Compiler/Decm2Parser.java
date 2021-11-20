// $ANTLR 2.7.1: "decm2.g" -> "Decm2Parser.java"$

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.collections.AST;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

public class Decm2Parser extends antlr.LLkParser
       implements Decm2ParserTokenTypes
 {

     // create the symbol table
    public MSymbolTable symbolTable = new MSymbolTable();
	public int errorcount = 0;
	public void reportError(String s) {
		errorcount++;
		super.reportError(s);
	}
	public void reportError(RecognitionException ex) {
		errorcount++;
		super.reportError(ex);
	}

	void declare( String name) throws antlr.TokenStreamException {
		// Check a variable is not declared
		// and add to the symbol table
		if ( symbolTable.isDeclared(name)) {
			reportError(name+ " is already declared on line "+LT(1).getLine());
		}
		else {
			symbolTable.declareVariable(name);
		}
	}


protected Decm2Parser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public Decm2Parser(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected Decm2Parser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public Decm2Parser(TokenStream lexer) {
  this(lexer,2);
}

public Decm2Parser(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
}

	public final void module() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST module_AST = null;
		Token  name = null;
		AST name_AST = null;
		Token  end = null;
		AST end_AST = null;
		
		try {      // for error handling
			AST tmp1_AST = null;
			tmp1_AST = (AST)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp1_AST);
			match(MODULE);
			name = LT(1);
			name_AST = (AST)astFactory.create(name);
			astFactory.addASTChild(currentAST, name_AST);
			match(ID);
			AST tmp2_AST = null;
			tmp2_AST = (AST)astFactory.create(LT(1));
			match(SEMICOLON);
			constdec();
			vardec();
			block();
			astFactory.addASTChild(currentAST, returnAST);
			end = LT(1);
			end_AST = (AST)astFactory.create(end);
			match(ID);
			
						if (!(name.getText()).equals(end.getText()))
					    reportError("The module name '"+name.getText()+
								"' is different from the name on the end statement: '"+end.getText()+"'");
			
				
			module_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		returnAST = module_AST;
	}
	
	public final void constdec() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constdec_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case CONST:
			{
				AST tmp3_AST = null;
				tmp3_AST = (AST)astFactory.create(LT(1));
				match(CONST);
				constlist();
				break;
			}
			case BEGIN:
			case VAR:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_1);
		}
		returnAST = constdec_AST;
	}
	
	public final void vardec() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST vardec_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case VAR:
			{
				AST tmp4_AST = null;
				tmp4_AST = (AST)astFactory.create(LT(1));
				match(VAR);
				{
				int _cnt37=0;
				_loop37:
				do {
					if ((LA(1)==ID)) {
						onevar();
						AST tmp5_AST = null;
						tmp5_AST = (AST)astFactory.create(LT(1));
						match(SEMICOLON);
					}
					else {
						if ( _cnt37>=1 ) { break _loop37; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt37++;
				} while (true);
				}
				break;
			}
			case BEGIN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = vardec_AST;
	}
	
	public final void block() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST block_AST = null;
		
		try {      // for error handling
			AST tmp6_AST = null;
			tmp6_AST = (AST)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp6_AST);
			match(BEGIN);
			statement_list();
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp7_AST = null;
			tmp7_AST = (AST)astFactory.create(LT(1));
			match(END);
			block_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_3);
		}
		returnAST = block_AST;
	}
	
	public final void constlist() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constlist_AST = null;
		
		try {      // for error handling
			{
			_loop33:
			do {
				if ((LA(1)==ID)) {
					constdef();
					AST tmp8_AST = null;
					tmp8_AST = (AST)astFactory.create(LT(1));
					match(SEMICOLON);
				}
				else {
					break _loop33;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_1);
		}
		returnAST = constlist_AST;
	}
	
	public final void constdef() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constdef_AST = null;
		Token  id = null;
		AST id_AST = null;
		Token  op = null;
		AST op_AST = null;
		Token  num = null;
		AST num_AST = null;
		
		try {      // for error handling
			id = LT(1);
			id_AST = (AST)astFactory.create(id);
			astFactory.addASTChild(currentAST, id_AST);
			match(ID);
			op = LT(1);
			op_AST = (AST)astFactory.create(op);
			match(BOOLOP);
			if (!( op.getText().equals("=")))
			  throw new SemanticException(" op.getText().equals(\"=\")");
			num = LT(1);
			num_AST = (AST)astFactory.create(num);
			astFactory.addASTChild(currentAST, num_AST);
			match(NUMBER);
			
					// check the constant is not declared
					 String name = id_AST.getText();
						if (symbolTable.isDeclared(name)) {
						   reportError(name+ " is already declared on"+LT(1).getLine());
						}
						else {
							int value = Integer.parseInt(num_AST.getText());
							symbolTable.declareConstant(name,value);
						}
					
			constdef_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_4);
		}
		returnAST = constdef_AST;
	}
	
	public final void onevar() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST onevar_AST = null;
		
		try {      // for error handling
			idlist();
			AST tmp9_AST = null;
			tmp9_AST = (AST)astFactory.create(LT(1));
			match(COLON);
			type();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_4);
		}
		returnAST = onevar_AST;
	}
	
	public final void idlist() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST idlist_AST = null;
		Token  id = null;
		AST id_AST = null;
		Token  id1 = null;
		AST id1_AST = null;
		
		try {      // for error handling
			id = LT(1);
			id_AST = (AST)astFactory.create(id);
			astFactory.addASTChild(currentAST, id_AST);
			match(ID);
			
					 declare( id_AST.getText());
					
			{
			_loop41:
			do {
				if ((LA(1)==COMMA)) {
					AST tmp10_AST = null;
					tmp10_AST = (AST)astFactory.create(LT(1));
					match(COMMA);
					id1 = LT(1);
					id1_AST = (AST)astFactory.create(id1);
					astFactory.addASTChild(currentAST, id1_AST);
					match(ID);
					
							  declare( id1_AST.getText());
							
				}
				else {
					break _loop41;
				}
				
			} while (true);
			}
			idlist_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_5);
		}
		returnAST = idlist_AST;
	}
	
	public final void type() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST type_AST = null;
		
		try {      // for error handling
			AST tmp11_AST = null;
			tmp11_AST = (AST)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp11_AST);
			match(INTEGER);
			type_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_4);
		}
		returnAST = type_AST;
	}
	
	public final void statement_list() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST statement_list_AST = null;
		
		try {      // for error handling
			statement();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop47:
			do {
				if ((LA(1)==SEMICOLON)) {
					AST tmp12_AST = null;
					tmp12_AST = (AST)astFactory.create(LT(1));
					match(SEMICOLON);
					statement();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop47;
				}
				
			} while (true);
			}
			statement_list_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_6);
		}
		returnAST = statement_list_AST;
	}
	
	public final void statements() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST statements_AST = null;
		AST s_AST = null;
		
		try {      // for error handling
			statement_list();
			s_AST = (AST)returnAST;
			statements_AST = (AST)currentAST.root;
			
						  // put a root node on a list of statement
						  // so it can be treated as a block
						   statements_AST = (AST)astFactory.make( (new ASTArray(2)).add((AST)astFactory.create(BEGIN,"BEGIN")).add(s_AST));
					
			currentAST.root = statements_AST;
			currentAST.child = statements_AST!=null &&statements_AST.getFirstChild()!=null ?
				statements_AST.getFirstChild() : statements_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_6);
		}
		returnAST = statements_AST;
	}
	
	public final void statement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST statement_AST = null;
		Token  id = null;
		AST id_AST = null;
		Token  proc = null;
		AST proc_AST = null;
		Token  head = null;
		AST head_AST = null;
		AST ifcond_AST = null;
		AST whilecond_AST = null;
		AST repeatcond_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case IF:
			{
				AST tmp13_AST = null;
				tmp13_AST = (AST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp13_AST);
				match(IF);
				bool_expr();
				ifcond_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				
							if ((ifcond_AST.getType() != Decm2LexerTokenTypes.AND) &&
				(ifcond_AST.getType() != Decm2LexerTokenTypes.OR) &&
				(ifcond_AST.getType() != Decm2LexerTokenTypes.NOT) &&
								(ifcond_AST.getType() != Decm2LexerTokenTypes.BOOLOP) )
								throw new SemanticException("non boolean in IF",
									getFilename(),LT(1).getLine());
						
				AST tmp14_AST = null;
				tmp14_AST = (AST)astFactory.create(LT(1));
				match(THEN);
				statements();
				astFactory.addASTChild(currentAST, returnAST);
				{
				switch ( LA(1)) {
				case ELSE:
				{
					AST tmp15_AST = null;
					tmp15_AST = (AST)astFactory.create(LT(1));
					match(ELSE);
					statements();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case END:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				AST tmp16_AST = null;
				tmp16_AST = (AST)astFactory.create(LT(1));
				match(END);
				statement_AST = (AST)currentAST.root;
				break;
			}
			case WHILE:
			{
				AST tmp17_AST = null;
				tmp17_AST = (AST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp17_AST);
				match(WHILE);
				bool_expr();
				whilecond_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				
							if ((whilecond_AST.getType() != Decm2LexerTokenTypes.AND) &&
				(whilecond_AST.getType() != Decm2LexerTokenTypes.OR) &&
				(whilecond_AST.getType() != Decm2LexerTokenTypes.NOT) &&
								(whilecond_AST.getType() != Decm2LexerTokenTypes.BOOLOP) )
								throw new SemanticException("non boolean in REPEAT",
									getFilename(),LT(1).getLine());
						
				AST tmp18_AST = null;
				tmp18_AST = (AST)astFactory.create(LT(1));
				match(DO);
				statements();
				astFactory.addASTChild(currentAST, returnAST);
				AST tmp19_AST = null;
				tmp19_AST = (AST)astFactory.create(LT(1));
				match(END);
				statement_AST = (AST)currentAST.root;
				break;
			}
			case REPEAT:
			{
				AST tmp20_AST = null;
				tmp20_AST = (AST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp20_AST);
				match(REPEAT);
				statements();
				astFactory.addASTChild(currentAST, returnAST);
				AST tmp21_AST = null;
				tmp21_AST = (AST)astFactory.create(LT(1));
				match(UNTIL);
				bool_expr();
				repeatcond_AST = (AST)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				
							if ((repeatcond_AST.getType() != Decm2LexerTokenTypes.AND) &&
				(repeatcond_AST.getType() != Decm2LexerTokenTypes.OR) &&
				(repeatcond_AST.getType() != Decm2LexerTokenTypes.NOT) &&
								(repeatcond_AST.getType() != Decm2LexerTokenTypes.BOOLOP) )
								throw new SemanticException("non boolean in REPEAT",
									getFilename(),LT(1).getLine());
						
				statement_AST = (AST)currentAST.root;
				break;
			}
			case ELSE:
			case END:
			case UNTIL:
			case SEMICOLON:
			{
				statement_AST = (AST)currentAST.root;
				break;
			}
			default:
				if ((LA(1)==ID) && (LA(2)==ASSIGN)) {
					id = LT(1);
					id_AST = (AST)astFactory.create(id);
					astFactory.addASTChild(currentAST, id_AST);
					match(ID);
					AST tmp22_AST = null;
					tmp22_AST = (AST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp22_AST);
					match(ASSIGN);
					
							// Check the variable is declared
							 String name = id_AST.getText();
							 if (!symbolTable.isVariable(name)) {
								throw new SemanticException(name+ " is not declared as a variable",
										getFilename(),LT(1).getLine());
								}
					
							
					expr();
					astFactory.addASTChild(currentAST, returnAST);
					statement_AST = (AST)currentAST.root;
				}
				else if ((LA(1)==ID) && (LA(2)==LPAREN)) {
					proc = LT(1);
					proc_AST = (AST)astFactory.create(proc);
					astFactory.addASTChild(currentAST, proc_AST);
					match(ID);
					head = LT(1);
					head_AST = (AST)astFactory.create(head);
					astFactory.makeASTRoot(currentAST, head_AST);
					match(LPAREN);
					
							// Check the procedure is declared
							 String name = proc_AST.getText();
					if (! symbolTable.isProcedure(name)){
								throw new SemanticException(name+
							       " is not declared as a procedure",getFilename(),LT(1).getLine());
							 }
							 // change the root of this tree from (  to CALL
							 head_AST.setType(CALL);
							 head_AST.setText("CALL");
							
					{
					switch ( LA(1)) {
					case LPAREN:
					case ADDOP:
					case ID:
					case NUMBER:
					{
						expr();
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					case RPAREN:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					AST tmp23_AST = null;
					tmp23_AST = (AST)astFactory.create(LT(1));
					match(RPAREN);
					statement_AST = (AST)currentAST.root;
				}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_7);
		}
		returnAST = statement_AST;
	}
	
	public final void expr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST expr_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case ADDOP:
			{
				AST tmp24_AST = null;
				tmp24_AST = (AST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp24_AST);
				match(ADDOP);
				term();
				astFactory.addASTChild(currentAST, returnAST);
				expr_AST = (AST)currentAST.root;
				break;
			}
			case LPAREN:
			case ID:
			case NUMBER:
			{
				term();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop61:
				do {
					if ((LA(1)==ADDOP)) {
						AST tmp25_AST = null;
						tmp25_AST = (AST)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp25_AST);
						match(ADDOP);
						term();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop61;
					}
					
				} while (true);
				}
				expr_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_8);
		}
		returnAST = expr_AST;
	}
	
	public final void bool_expr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST bool_expr_AST = null;
		
		try {      // for error handling
			bool_term();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop53:
			do {
				if ((LA(1)==OR)) {
					AST tmp26_AST = null;
					tmp26_AST = (AST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp26_AST);
					match(OR);
					bool_term();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop53;
				}
				
			} while (true);
			}
			bool_expr_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_9);
		}
		returnAST = bool_expr_AST;
	}
	
	public final void bool_term() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST bool_term_AST = null;
		
		try {      // for error handling
			bool_factor();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop56:
			do {
				if ((LA(1)==AND)) {
					AST tmp27_AST = null;
					tmp27_AST = (AST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp27_AST);
					match(AND);
					bool_factor();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop56;
				}
				
			} while (true);
			}
			bool_term_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_10);
		}
		returnAST = bool_term_AST;
	}
	
	public final void bool_factor() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST bool_factor_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case NOT:
			{
				AST tmp28_AST = null;
				tmp28_AST = (AST)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp28_AST);
				match(NOT);
				bool_factor();
				astFactory.addASTChild(currentAST, returnAST);
				bool_factor_AST = (AST)currentAST.root;
				break;
			}
			case LPAREN:
			case ADDOP:
			case ID:
			case NUMBER:
			{
				expr();
				astFactory.addASTChild(currentAST, returnAST);
				{
				switch ( LA(1)) {
				case BOOLOP:
				{
					AST tmp29_AST = null;
					tmp29_AST = (AST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp29_AST);
					match(BOOLOP);
					expr();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case AND:
				case DO:
				case ELSE:
				case END:
				case OR:
				case THEN:
				case UNTIL:
				case SEMICOLON:
				case RPAREN:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				bool_factor_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_11);
		}
		returnAST = bool_factor_AST;
	}
	
	public final void term() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST term_AST = null;
		
		try {      // for error handling
			factor();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop64:
			do {
				if ((LA(1)==MULTOP)) {
					AST tmp30_AST = null;
					tmp30_AST = (AST)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp30_AST);
					match(MULTOP);
					factor();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop64;
				}
				
			} while (true);
			}
			term_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_12);
		}
		returnAST = term_AST;
	}
	
	public final void factor() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST factor_AST = null;
		Token  num = null;
		AST num_AST = null;
		Token  id = null;
		AST id_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case NUMBER:
			{
				num = LT(1);
				num_AST = (AST)astFactory.create(num);
				astFactory.addASTChild(currentAST, num_AST);
				match(NUMBER);
				
							MAST thenumber = (MAST) num_AST;
							thenumber.setValue(Integer.parseInt(num_AST.getText()));
						
				factor_AST = (AST)currentAST.root;
				break;
			}
			case LPAREN:
			{
				AST tmp31_AST = null;
				tmp31_AST = (AST)astFactory.create(LT(1));
				match(LPAREN);
				bool_expr();
				astFactory.addASTChild(currentAST, returnAST);
				AST tmp32_AST = null;
				tmp32_AST = (AST)astFactory.create(LT(1));
				match(RPAREN);
				factor_AST = (AST)currentAST.root;
				break;
			}
			case ID:
			{
				id = LT(1);
				id_AST = (AST)astFactory.create(id);
				astFactory.addASTChild(currentAST, id_AST);
				match(ID);
				
						// Check the identifier is declared
						  String name = id_AST.getText();
						  if (symbolTable.isConstant(name)) {
								((MAST)id_AST).setValue(symbolTable.getValue(name));
							}
						  else if ( !(symbolTable.isVariable(name)))
							throw new SemanticException(name+
						         " is not declared as a constant or variable",
									getFilename(), LT(1).getLine());
						
				factor_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_13);
		}
		returnAST = factor_AST;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"AND\"",
		"\"BEGIN\"",
		"\"CALL\"",
		"\"CONST\"",
		"\"DIV\"",
		"\"DO\"",
		"\"ELSE\"",
		"\"END\"",
		"\"IF\"",
		"\"INTEGER\"",
		"\"MODULE\"",
		"\"NOT\"",
		"\"OR\"",
		"\"THEN\"",
		"\"VAR\"",
		"\"WHILE\"",
		"\"REPEAT\"",
		"\"UNTIL\"",
		"SEMICOLON",
		"COMMA",
		"ASSIGN",
		"COLON",
		"STOP",
		"LPAREN",
		"RPAREN",
		"ADDOP",
		"MULTOP",
		"BOOLOP",
		"ID",
		"NUMBER",
		"WS",
		"COMMENT_1"
	};
	
	private static final long _tokenSet_0_data_[] = { 2L, 0L };
	public static final BitSet _tokenSet_0 = new BitSet(_tokenSet_0_data_);
	private static final long _tokenSet_1_data_[] = { 262176L, 0L };
	public static final BitSet _tokenSet_1 = new BitSet(_tokenSet_1_data_);
	private static final long _tokenSet_2_data_[] = { 32L, 0L };
	public static final BitSet _tokenSet_2 = new BitSet(_tokenSet_2_data_);
	private static final long _tokenSet_3_data_[] = { 4294967296L, 0L };
	public static final BitSet _tokenSet_3 = new BitSet(_tokenSet_3_data_);
	private static final long _tokenSet_4_data_[] = { 4194304L, 0L };
	public static final BitSet _tokenSet_4 = new BitSet(_tokenSet_4_data_);
	private static final long _tokenSet_5_data_[] = { 33554432L, 0L };
	public static final BitSet _tokenSet_5 = new BitSet(_tokenSet_5_data_);
	private static final long _tokenSet_6_data_[] = { 2100224L, 0L };
	public static final BitSet _tokenSet_6 = new BitSet(_tokenSet_6_data_);
	private static final long _tokenSet_7_data_[] = { 6294528L, 0L };
	public static final BitSet _tokenSet_7 = new BitSet(_tokenSet_7_data_);
	private static final long _tokenSet_8_data_[] = { 2422410768L, 0L };
	public static final BitSet _tokenSet_8 = new BitSet(_tokenSet_8_data_);
	private static final long _tokenSet_9_data_[] = { 274861568L, 0L };
	public static final BitSet _tokenSet_9 = new BitSet(_tokenSet_9_data_);
	private static final long _tokenSet_10_data_[] = { 274927104L, 0L };
	public static final BitSet _tokenSet_10 = new BitSet(_tokenSet_10_data_);
	private static final long _tokenSet_11_data_[] = { 274927120L, 0L };
	public static final BitSet _tokenSet_11 = new BitSet(_tokenSet_11_data_);
	private static final long _tokenSet_12_data_[] = { 2959281680L, 0L };
	public static final BitSet _tokenSet_12 = new BitSet(_tokenSet_12_data_);
	private static final long _tokenSet_13_data_[] = { 4033023504L, 0L };
	public static final BitSet _tokenSet_13 = new BitSet(_tokenSet_13_data_);
	
	}
