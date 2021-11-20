class Decm2Lexer extends Lexer ;
// The definition of a Lexer for decm2
options { k = 4;
	charVocabulary = '\3'..'\377';
	exportVocab = Decm2Lexer;
}

tokens {
// Key words
 AND   = "AND" ;
 BEGIN = "BEGIN" ;
 CALL = "CALL" ;
 CONST = "CONST" ;
 DIV   = "DIV" ;
 DO =  "DO" ;
 ELSE = "ELSE" ;
 END = "END" ;
 IF = "IF" ;
 INTEGER = "INTEGER" ;
 MODULE = "MODULE" ;
 NOT = "NOT"  ;
 OR  = "OR"   ;
 THEN = "THEN" ;
 VAR =   "VAR" ;
 WHILE = "WHILE" ;
 REPEAT = "REPEAT";
 UNTIL = "UNTIL";
}
// special symbols
 SEMICOLON : ";" ;
 COMMA : "," ;
 ASSIGN : ":=" ;
 COLON : ":" ;
 STOP : "." ;
 LPAREN : "(" ;
 RPAREN : ")" ;
// Operators
 ADDOP : ( "+" | "-") ;
 MULTOP : ( "*" | "/");
 BOOLOP : ( "#"| "<=" | "<" | ">=" | ">"| "=" ) ;

// Identifiers
ID options {testLiterals=true;}
        : ('a'..'z'|'A'..'Z') ('a'..'z'|'0'..'9'|'A'..'Z')*
        ;

NUMBER  : ('0'..'9')('0'..'9')* ;

// Whitespace -- ignored
WS      : ( ' '
          | '\t'
          | '\f'
// handle newlines
          |   (  "\r\n"  // DOS
              |  '\r'    // Macintosh
              |  '\n'    // Unix
              )
             { newline(); }
          )
          { _ttype = Token.SKIP; };

 COMMENT_1 : "(*"
               ( options { generateAmbigWarnings=false; }
               :    { LA(2) != ')' }? '*'
                   |    '\r' '\n'               {newline();}
                   |    '\r'                    {newline();}
                   |    '\n'                    {newline();}
                   |   ~('*' | '\n' | '\r')
                   )*
                "*)"
                {$setType(Token.SKIP);};

class Decm2Parser extends Parser ;

options { k = 2;
          buildAST = true;
	      importVocab = Decm2Lexer;
        }
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

}

module : MODULE ^ name : ID  SEMICOLON !
          constdec! vardec!    block  end :  ID!
      {
			if (!(name.getText()).equals(end.getText()))
		    reportError("The module name '"+name.getText()+
					"' is different from the name on the end statement: '"+end.getText()+"'");

	  };

/*
 constant definitions are not put in the tree,
 the information is put into the symbol table
*/
constdec ! :   CONST constlist
           | /* empty */ ;

constlist ! : ( constdef  SEMICOLON!  ) * ;

constdef :
		// to avoid nondeterminism with  BOOLOP and =
        // use BOOLOP and a predicate
        id : ID op : BOOLOP !  { op.getText().equals("=")}? num : NUMBER
		{
		// check the constant is not declared
  		 String name = #id.getText();
			if (symbolTable.isDeclared(name)) {
			   reportError(name+ " is already declared on"+LT(1).getLine());
			}
			else {
				int value = Integer.parseInt(#num.getText());
				symbolTable.declareConstant(name,value);
			}
		};

vardec ! :  VAR   ( onevar  SEMICOLON )+
       |  /* empty */ ;


onevar! :   idlist COLON  type  ;

idlist :  id : ID
		{
		 declare( #id.getText());
		}

		( COMMA ! id1 : ID
		{
		  declare( #id1.getText());
		}
      )*  ;

type :   INTEGER ;


block  :   BEGIN ^ statement_list END ! ;

statements! : s : statement_list
              {
			  // put a root node on a list of statement
			  // so it can be treated as a block
			   #statements = #([BEGIN,"BEGIN"],  #s);
		      } ;

statement_list : statement ( SEMICOLON ! statement  ) *  ;


statement :
      id : ID   ASSIGN ^
		{
		// Check the variable is declared
		 String name = #id.getText();
		 if (!symbolTable.isVariable(name)) {
			throw new SemanticException(name+ " is not declared as a variable",
					getFilename(),LT(1).getLine());
			}

		}
          expr
      | proc : ID  head : LPAREN ^
		{
		// Check the procedure is declared
		 String name = #proc.getText();
         if (! symbolTable.isProcedure(name)){
			throw new SemanticException(name+
		       " is not declared as a procedure",getFilename(),LT(1).getLine());
		 }
		 // change the root of this tree from (  to CALL
		 #head.setType(CALL);
		 #head.setText("CALL");
		}
		( expr )? RPAREN!
      | IF ^ ifcond : bool_expr
		{
			if ((#ifcond.getType() != Decm2LexerTokenTypes.AND) &&
                (#ifcond.getType() != Decm2LexerTokenTypes.OR) &&
               (#ifcond.getType() != Decm2LexerTokenTypes.NOT) &&
				(#ifcond.getType() != Decm2LexerTokenTypes.BOOLOP) )
				throw new SemanticException("non boolean in IF",
					getFilename(),LT(1).getLine());
		}
         THEN ! statements ( ELSE ! statements ) ? END !
      | WHILE ^ whilecond:bool_expr
		{
			if ((#whilecond.getType() != Decm2LexerTokenTypes.AND) &&
                (#whilecond.getType() != Decm2LexerTokenTypes.OR) &&
               (#whilecond.getType() != Decm2LexerTokenTypes.NOT) &&
				(#whilecond.getType() != Decm2LexerTokenTypes.BOOLOP) )
				throw new SemanticException("non boolean in REPEAT",
					getFilename(),LT(1).getLine());
		}
		DO! statements END !
      | REPEAT ^ statements UNTIL ! repeatcond:bool_expr
      				    {
			if ((#repeatcond.getType() != Decm2LexerTokenTypes.AND) &&
                (#repeatcond.getType() != Decm2LexerTokenTypes.OR) &&
               (#repeatcond.getType() != Decm2LexerTokenTypes.NOT) &&
				(#repeatcond.getType() != Decm2LexerTokenTypes.BOOLOP) )
				throw new SemanticException("non boolean in REPEAT",
					getFilename(),LT(1).getLine());
		}
      | /*empty*/ ;


bool_expr   : bool_term ( OR ^ bool_term )* ;
bool_term   : bool_factor ( AND ^ bool_factor ) * ;
bool_factor : NOT ^ bool_factor
            | expr  ( BOOLOP ^ expr )? ;
expr : ADDOP ^ term
     |  term   ( ADDOP ^ term )* ;
term : factor ( MULTOP ^ factor)* ;

factor :
         num : NUMBER
		{
			MAST thenumber = (MAST) #num;
			thenumber.setValue(Integer.parseInt(#num.getText()));
		}
       | LPAREN !  bool_expr  RPAREN !
       | id : ID
		{
		// Check the identifier is declared
		  String name = #id.getText();
		  if (symbolTable.isConstant(name)) {
				((MAST)#id).setValue(symbolTable.getValue(name));
			}
		  else if ( !(symbolTable.isVariable(name)))
			throw new SemanticException(name+
		         " is not declared as a constant or variable",
					getFilename(), LT(1).getLine());
		} ;


