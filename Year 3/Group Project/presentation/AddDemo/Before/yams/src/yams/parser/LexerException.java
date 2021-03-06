/*
 * Created on 2003-11-4
 */
package yams.parser;

/**
 * Exception to be thrown if  e lexer finds an illegal symbol.
 * 
 * @author Qian Qiao <a href="mailto:qq01@doc.ic.ac.uk">qq01@doc.ic.ac.uk</a>
 */
public class LexerException extends Exception {
	/**
	 * Constructs a new LexerException.
	 * 
	 * @param symbol the incorrect symbol
	 * @param lineNumber which line the error occur
	 */
	public LexerException(char symbol, int lineNumber) {
		super("Illegal symbol:'" + symbol + "' at line " + lineNumber);
	}
}
