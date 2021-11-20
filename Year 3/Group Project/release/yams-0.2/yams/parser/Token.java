/*
 * Created on 2003-11-1
 */
package yams.parser;

/**
 * Lexical token class.
 * 
 * @author Qian Qiao (qq01)
 */
public class Token {
	private int type;
	private String value;

	/**
	 * Initialises a token with it's type.
	 * 
	 * @param type type of the token.
	 * @see yams.parser.MIPSLexer
	 */
	public Token(int type) {
		this(type, "");
	}

	/**
	 * Initialises a token with a type and a value.
	 * 
	 * @param type type of the token
	 * @param value value of the token
	 * @see yams.parser.MIPSLexer
	 */
	public Token(int type, String value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Gives the type of the token.
	 * 
	 * @return the type of the token
	 * @see yams.parser.MIPSLexer
	 */
	public int getType() {
		return type;
	}

	/**
	 * Gives the value of the token. Note that this may be <code>null</code>.
	 * 
	 * @return the value of the token
	 */
	public String getValue() {
		return value;
	}

}
