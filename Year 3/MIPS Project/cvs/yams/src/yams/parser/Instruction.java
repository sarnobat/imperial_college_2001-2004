/*
 * Created on 06-Nov-2003
 */
package yams.parser;

import java.util.Iterator;
import java.util.List;

/**
 * Models a single instruction, all instructions should subclass this abstract class.
 * 
 * @author qq01
 */
public abstract class Instruction {

	/**
	 * Indicates the instructio is a directive.
	 */
	public static final int TYPE_DIRECTIVE = 0;

	/**
	 * Indicates the instruction is a I type instruction.
	 */
	public static final int TYPE_I = 1;

	/**
	 * Indicates the instruction is a J type instruction.
	 */
	public static final int TYPE_J = 2;

	/**
	 * Indicates the instruction is a R type instruction.
	 */
	public static final int TYPE_R = 3;

	protected String name;

	// Operands must be a sorted Collection
	protected List operands;

	/**
	 * Returns the name of the instruction.
	 * 
	 * @return the name of the instruction
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the list of oprands.
	 * 
	 * @return a list of the oprands of the instruction
	 */
	public List getOperands() {
		return operands;
	}

	/**
	 * Returns the type of the instruction.
	 * 
	 * @return the type of the instruction.
	 */
	public abstract int getType();

	private void match(Lexer lexer, int expected)
		throws LexerException, ParseException {
		if (lexer.currentToken().getType() == expected) {
			lexer.nextToken();
		} else {
			throw new ParseException(
				lexer.currentToken(),
				expected,
				lexer.getLineNumber());
		}
	}

	private AddressingOperand parseAddressingOperand(Lexer lexer)
		throws LexerException, ParseException {

		Token token = lexer.nextToken();

		if (token.getType() == Lexer.LEFT_PAR) { // ($register) format
			lexer.pushTokenBack();
			return new AddressingOperand(
				AddressingOperand.REGISTER,
				parseParRegister(lexer));
		} else if (token.getType() == Lexer.IDENTIFIER) { // detects a label
			String label = token.getValue();
			token = lexer.nextToken();

			if ((token.getType() == Lexer.PLUS)
				|| (token.getType() == Lexer.MINUS)) { //label +/- format

				String sign = token.getValue();

				//token = lexer.nextToken();

				int immediate = parseImmediateValue(lexer, sign);

				lexer.nextToken();
				token = lexer.nextToken();

				if (token.getType() == Lexer.LEFT_PAR) {
					lexer.pushTokenBack();

					String register = parseParRegister(lexer);

					return new AddressingOperand(
						AddressingOperand.LABEL_PLUS_IMMEDIATE_REGISTER,
						label,
						immediate,
						register);

				} else {
					lexer.pushTokenBack();
					return new AddressingOperand(
						AddressingOperand.LABEL_PLUS_IMMEDIATE,
						label,
						immediate);
				}

			} else {
				lexer.pushTokenBack();
				return new AddressingOperand(AddressingOperand.LABEL, label);
			}
		} else {
			String sign;
			if (token.getType() == Lexer.PLUS
				|| token.getType() == Lexer.MINUS) {
				sign = token.getValue();
			} else {
				sign = "+";
				lexer.pushTokenBack();
			}

			int immediate = parseImmediateValue(lexer, sign);

			lexer.nextToken();
			token = lexer.nextToken();

			if (token.getType() == Lexer.LEFT_PAR) {
				lexer.pushTokenBack();
				String register = parseParRegister(lexer);
				return new AddressingOperand(
					AddressingOperand.IMMEDIATE_REGISTER,
					immediate,
					register);
			} else {
				lexer.pushTokenBack();
				return new AddressingOperand(
					AddressingOperand.IMMEDIATE,
					immediate);
			}
		}
	}

	private Operand parseImmediateOperand(Lexer lexer)
		throws LexerException, ParseException {

		String sign;
		if (lexer.currentToken().getType() == Lexer.PLUS
			|| lexer.currentToken().getType() == Lexer.MINUS) {
			sign = lexer.currentToken().getValue();
			lexer.nextToken();
		} else {
			sign = "+";
		}

		ImmediateOperand io =
			new ImmediateOperand(parseImmediateValue(lexer, sign));
		lexer.nextToken();
		return io;
	}

	private int parseImmediateValue(Lexer lexer, String sign)
		throws LexerException, ParseException {

		int intvalue;

		if (lexer.currentToken().getValue().equals("0")) {
			lexer.mark();
			lexer.nextToken();
			if (lexer.currentToken().getType() == Lexer.IDENTIFIER) {
				intvalue =
					Integer.parseInt(
						lexer.currentToken().getValue().substring(1),
						16);
			} else {
				lexer.rewind();
				intvalue = 0;
			}
		} else {
			if (!lexer.currentToken().getValue().startsWith("0")) {
				intvalue = Integer.parseInt(lexer.currentToken().getValue());
			} else {
				intvalue =
					Integer.parseInt(
						lexer.currentToken().getValue().substring(1),
						8);

			}
		}
		
		if (sign.equals("-")) {
			return 0 - intvalue;
		} else {
			return intvalue;
		}
	}

	private LabelOperand parseLabelOperand(Lexer lexer)
		throws LexerException, ParseException {
		Token token = lexer.nextToken();

		if (token.getType() == Lexer.IDENTIFIER) {
			return new LabelOperand(token.getValue());
		} else {
			throw new ParseException(
				token,
				Lexer.IDENTIFIER,
				lexer.getLineNumber());
		}
	}

	/**
	 * Parses oprands needed by an instruction.
	 * 
	 * @param lexer lexer that tokenizes user's programs
	 * @param expected array of types of oprands expected by an instruction
	 * @throws LexerException if the lexer finds any error
	 * @throws ParseException if the parser finds any error
	 */
	protected void parseOperands(Lexer lexer, int[] expected)
		throws LexerException, ParseException {

		Token token;

		for (int i = 0; i < expected.length; i++) {
			switch (expected[i]) {
				case Operand.TYPE_REGISTER :
					{
						operands.add(parseRegisterOperand(lexer));
						break;
					}
				case Operand.TYPE_IMMEDIATE :
					{
						operands.add(parseImmediateOperand(lexer));
						break;
					}
				case Operand.TYPE_ADDRESSING :
					{
						operands.add(parseAddressingOperand(lexer));
						break;
					}
				case Operand.TYPE_STRING :
					{
						operands.add(parseStringOperand(lexer));
						break;
					}
				case Operand.TYPE_LABEL :
					{
						operands.add(parseLabelOperand(lexer));
						break;
					}
				default :
					{
						throw new ParseException(
							"Unexpected token:"
								+ lexer.currentToken().getValue(),
							lexer.getLineNumber());
					}
			}
			if (i != expected.length - 1) {
				token = lexer.nextToken();
				if (token.getType() != Lexer.COMMA) {
					throw new ParseException(
						"Expected ',' - not '" + token.getValue() + "'",
						lexer.getLineNumber());
				}
			}
		}
	}

	private String parseParRegister(Lexer lexer)
		throws LexerException, ParseException {

		match(lexer, Lexer.LEFT_PAR);
		match(lexer, Lexer.DOLLAR);
		String register = lexer.currentToken().getValue();
		match(lexer, Lexer.IDENTIFIER);
		match(lexer, Lexer.RIGHT_PAR);

		return register;
	}

	private RegisterOperand parseRegisterOperand(Lexer lexer)
		throws LexerException, ParseException {
		Token token = lexer.nextToken();
		if (token.getType() == Lexer.DOLLAR) {
			token = lexer.nextToken();
			return new RegisterOperand(token.getValue());
		} else {
			throw new ParseException(
				"Expected '$', not '" + token.getValue() + "'"+" name "+name,
				lexer.getLineNumber());
		}
	}

	private StringOperand parseStringOperand(Lexer lexer)
		throws LexerException, ParseException {
		match(lexer, Lexer.QUOTE);
		StringBuffer s = new StringBuffer();
		while (lexer.currentToken().getType() != Lexer.QUOTE) {
			s.append(lexer.currentToken().getValue());
			s.append(' ');
			lexer.nextToken();
		}
		lexer.nextToken();
		return new StringOperand(s.toString());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer("Instruction:" + name + " ");

		Operand op;

		Iterator it = operands.iterator();

		while (it.hasNext()) {
			op = (Operand) it.next();
			buffer.append(op.toString());
		}

		return buffer.toString();
	}
}
