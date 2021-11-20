/*
 * Created on 2003-11-1
 */
package yams.parser;

/**
 * Exception to be thrown the parser finds an error in the users file.
 * 
 * @author Qian Qiao <a href="mailto:qq01@doc.ic.ac.uk">qq01@doc.ic.ac.uk</a>
 */
public class ParseException extends Exception {
	private int line;

	public ParseException(String message, int line) {
		super(message);
		this.line = line;
	}

	public ParseException(Token actual, int expected, int line) {
		this(
			getString(expected) + " expected, not '" + actual.getValue() + "'",
			line);
	}

	private static String getString(int expected) {
		switch (expected) {
			case Lexer.IDENTIFIER :
				{
					return "identifier";
				}
			case Lexer.INTEGER :
				{
					return "integer";
				}
			case Lexer.COMMA :
				{
					return "','";
				}
			case Lexer.DOLLAR :
				{
					return "'$'";
				}
			case Lexer.COLON :
				{
					return "':'";
				}
			case Lexer.LEFT_PAR :
				{
					return "'('";
				}
			case Lexer.RIGHT_PAR :
				{
					return "')'";
				}
			case Lexer.NEWLINE :
				{
					return "\\n";
				}
			case Lexer.MINUS :
				{
					return "-";
				}
			case Lexer.EOF :
				{
					return "EOF";
				}
			case Lexer.DOT :
				{
					return ".";
				}
			case Lexer.PLUS :
				{
					return "+";
				}
			case Lexer.QUOTE :
				{
					return "\"";
				}
			case Lexer.ESCAPED_CHAR :
				{
					return "escaped character";
				}
		}
		throw new RuntimeException(
			"Internal error: MIPSParseException::getString() : Unknown token type:"
				+ expected);
	}

	public ParseException(int line) {
		super();
		this.line = line;
	}

	public int getLine() {
		return line;
	}

	public String toString() {
		String message = getMessage() == null ? "" : getMessage();
		return "Parse error at line: " + line + "\n" + message;
	}
}
