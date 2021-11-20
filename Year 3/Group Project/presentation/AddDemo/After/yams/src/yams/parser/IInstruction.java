/*
 * Created on 06-Nov-2003
 */
package yams.parser;

import java.util.Vector;

/**
 * Models instructions of type I.
 * 
 * @author qq01
 */
public class IInstruction extends Instruction {
	/**
	 * Constructs an <code>Instruction</code> of type I.
	 * 
	 * @param name name of the instruction
	 * @param lexer the lexer that's tokenizes the program
	 * @param operands operands
	 * @throws LexerException if an error occur
	 * @throws ParseException if an error occur
	 */
	public IInstruction(String name, Lexer lexer, int[] operands)
		throws LexerException, ParseException {

		this.name = name;

		this.operands = new Vector();

		parseOperands(lexer, operands);
	}

	/* (non-Javadoc)
	 * @see yams.parser.Instruction#getType()
	 */
	public int getType() {
		return TYPE_I;
	}
}
