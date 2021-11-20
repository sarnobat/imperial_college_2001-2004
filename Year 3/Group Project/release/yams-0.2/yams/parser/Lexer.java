/*
 * Created on 2003-11-1
 */
package yams.parser;

import java.util.Vector;

/**
 * Breaks users program into lexical tokens for the parser to do further check.
 * 
 * @author Qian Qiao <a href="mailto:qq01@doc.ic.ac.uk">qq01@doc.ic.ac.uk</a>
 */
public class Lexer {
	/**
	 * Colon.
	 */
	public static final int COLON = 0;

	/**
	 * Comma.
	 */
	public static final int COMMA = 1;

	/**
	 * Dollar.
	 */
	public static final int DOLLAR = 2;

	/**
	 * Dot.
	 */
	public static final int DOT = 3;

	/**
	 * End of file.
	 */
	public static final int EOF = 4;
	/**
	 * Identifier.
	 */
	public static final int IDENTIFIER = 5;

	/**
	 * Integer.
	 */
	public static final int INTEGER = 6;

	/**
	 * Left bracket.
	 */
	public static final int LEFT_PAR = 7;

	/**
	 * Minus.
	 */
	public static final int MINUS = 8;

	/**
	 * New line.
	 */
	public static final int NEWLINE = 9;

	/**
	 * Plus.
	 */
	public static final int PLUS = 10;

	/**
	 * Double quote.
	 */
	public static final int QUOTE = 12;

	/**
	 * Right bracket.
	 */
	public static final int RIGHT_PAR = 11;

	public static final int ESCAPED_CHAR = 13;

	private int currentCharIndex = -1;

	private int currentTokenIndex = 0;

	private int currentOriginalIndex = 0;

	private int mark = 0;

	private int lineNumber = 1;

	private StringBuffer program;

	private Vector tokens;

	/**
	 * Creates an instance of the lexer class.
	 * 
	 * @param program user's program input.
	 * @throws LexerException if the lexer is unable to tokenize the program.
	 */
	public Lexer(StringBuffer program) throws LexerException {
		this.program = program;

		if (program.charAt(program.length() - 1) != '\n') {
			program.append('\n');
		}

		tokens = new Vector();

		tokenize();
	}

	/**
	 * Returns the current token.
	 * 
	 * @return the current token.
	 */
	public Token currentToken() {
		return (Token) tokens.elementAt(currentTokenIndex);
	}

	/**
	 * Returns the current line number.
	 * 
	 * @return the current line number.
	 */
	public int getLineNumber() {
		return lineNumber;
	}

	/**
	 * Returns the next token.
	 */
	private Token getNextToken() throws LexerException {
		// test for End Of File
		if (currentCharIndex >= program.length() - 1) {
			return new Token(EOF, "EOF");
		} else { // if not EOF
			char symbol;

			// ignore whitespaces
			while (isWhiteSpace(symbol = nextSymbol())) {}

			// ignores comments
			if (symbol == ';') {
				while ((symbol = nextSymbol()) != '\n') {}
			}

			if (symbol == '#') {
				while ((symbol = nextSymbol()) != '\n') {}
			}

			StringBuffer buffer = new StringBuffer();

			if (symbol == '\\') {
				buffer.append(symbol);
				buffer.append(symbol = nextSymbol());

				return new Token(ESCAPED_CHAR, buffer.toString());
			}

			if (isAlpha(symbol)) {

				buffer.append(symbol);

				while ((symbol = nextSymbol()) != (char) - 1
					&& isAlpha(symbol)
					|| isNumber(symbol)) {
					buffer.append(symbol);
				}

				pushCharBack();

				return new Token(IDENTIFIER, buffer.toString());

			} else if (isNumber(symbol)) {

				buffer.append(symbol);

				while (isNumber(symbol = nextSymbol())) {
					buffer.append(symbol);
				}

				pushCharBack();

				return new Token(INTEGER, buffer.toString());

			} else if (symbol == '$') {
				return new Token(DOLLAR, "$");
			} else if (symbol == ',') {
				return new Token(COMMA, ",");
			} else if (symbol == '-') {
				return new Token(MINUS, "-");
			} else if (symbol == '+') {
				return new Token(PLUS, "+");
			} else if (symbol == ':') {
				return new Token(COLON, ":");
			} else if (symbol == '(') {
				return new Token(LEFT_PAR, "(");
			} else if (symbol == ')') {
				return new Token(RIGHT_PAR, ")");
			} else if (symbol == '\n') {
				return new Token(NEWLINE, "\\n");
			} else if (symbol == '\"') {
				return new Token(QUOTE, "\"");
			} else if (symbol == '.') {
				return new Token(DOT, ".");
			} else {
				throw new LexerException(symbol, lineNumber);
			}
		}
	}

	private boolean isAlpha(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c == '_');
	}

	private boolean isNumber(char c) {
		return c >= '0' && c <= '9';
	}

	private boolean isWhiteSpace(char c) {
		switch (c) {
			case ' ' :
			case '\r' :
			case '\t' :
				{
					return true;
				}
		}
		return false;
	}

	private char nextSymbol() {
		if (currentCharIndex >= program.length() - 1) {
			return (char) - 1;
		}
		return program.charAt(++currentCharIndex);
	}

	/**
	 * Returns the next token.
	 * 
	 * @return the next token.
	 */
	public Token nextToken() {
		Token currentToken = (Token) tokens.elementAt(currentTokenIndex++);

		if (currentToken.getType() == NEWLINE) {
			lineNumber++;
		}

		return currentToken;
	}

	private void pushCharBack() {
		if (currentCharIndex != program.length()) {
			currentCharIndex--;
		}
	}

	/**
	 * Pushes the current token back onto the list of tokens.
	 */
	public void pushTokenBack() {
		Token currentToken = (Token) tokens.elementAt(currentTokenIndex);

		if (currentToken.getType() == NEWLINE) {
			lineNumber--;
		}

		currentTokenIndex--;
	}

	private void tokenize() throws LexerException {

		Token t = getNextToken();

		while (t.getType() != EOF) {
			tokens.add(t);
			t = getNextToken();
		}

		tokens.add(new Token(EOF, "EOF"));
	}

	protected void mark() {
		mark = currentTokenIndex;
	}

	protected void rewind() {

		while (currentTokenIndex > mark) {
			pushTokenBack();
		}
	}

	protected String getOriginalLine() {
		StringBuffer buffer = new StringBuffer();

		char ch = '\n';
		if (currentOriginalIndex < program.length()) {
			ch = program.charAt(currentOriginalIndex);
		}

		while (ch != '\n' && currentOriginalIndex < program.length()) {
			buffer.append(ch);
			currentOriginalIndex++;
			ch = program.charAt(currentOriginalIndex); 
		}

		if (currentOriginalIndex < program.length()) {
			currentOriginalIndex++;
		}

		return buffer.toString();
	}
}
