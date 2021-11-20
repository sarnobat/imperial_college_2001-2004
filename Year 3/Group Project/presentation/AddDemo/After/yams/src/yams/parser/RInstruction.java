/*
 * Created on 06-Nov-2003
 */
package yams.parser;

import java.util.Vector;

/**
 * Models a instruction of type R.
 * 
 * @author qq01
 */
public class RInstruction extends Instruction {
	
	/**
	 * Constructs a new instruction of type R.
	 * 
	 * @param name name of the instruction.
	 * @param lexer lexer
	 * @param operands expected operands.
	 * @throws LexerException if the lexer finds an error.
	 * @throws ParseException if the parser finds an error.
	 */
	public RInstruction(String name, Lexer lexer, int[] operands)
		throws LexerException, ParseException {
		this.name = name;
		this.operands = new Vector();

		parseOperands(lexer, operands);
	}

	/* (non-Javadoc)
	 * @see yams.parser.Instruction#getType()
	 */
	public int getType() {
		return TYPE_R;
	}
}
