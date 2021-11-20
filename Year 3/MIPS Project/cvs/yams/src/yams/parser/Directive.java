/*
 * Created on 2003-11-28
 */
package yams.parser;

import java.util.Vector;

/**
 * Models the assembler directives. They are treated the same as instructions by the parser, but gets
 * translated in to several instructions by the assembler.
 * 
 * @author Qian Qiao(qq01)
 */
public class Directive extends Instruction {

	/**
	 * Constructs a new assembler directive.
	 * 
	 * @param name name of the directive
	 * @param lexer lexer
	 * @param operands operands of the directive
	 * @throws LexerException if the lexer finds an error
	 * @throws ParseException if the parser finds an error
	 */
	public Directive(String name, Lexer lexer, int[] operands)
		throws LexerException, ParseException {

		this.name = name;
		this.operands = new Vector();
		parseOperands(lexer, operands);
	}

	/* (non-Javadoc)
	 * @see yams.parser.Instruction#getType()
	 */
	public int getType() {
		return TYPE_DIRECTIVE;
	}

}
