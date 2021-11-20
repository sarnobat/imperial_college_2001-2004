/*
 * Created on 20-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package yams.assembler;

import yams.parser.Instruction;;

/**
 * Provides an Exception class that contains the line number and associated instruction which has caused 
 * the exception to be thrown.	
 * 
 * @author jkm01
 */
public class AssemblerException extends Exception {

private int line;
private Instruction instruction;

/**
 * When provided with an error message (String s) and a line number and associated instruction, it sets up this
 * new exception with the appropriate variables.
 * 
 * @param s Error Message provided with throw of exception
 * @param lineNumber
 * @param thisInstruction
 */
public AssemblerException(String s, int lineNumber, Instruction thisInstruction)
{
	super(s);
	line=lineNumber;
	instruction=thisInstruction;
}

/**
 * Returns the MIPS line number where this exception was thrown.
 * @return Returns the integer line number where exception was thrown
 */

public int getLine() {return line;}
/**
 * Returns the MIPS instruction iteself which contained some error causing an exception to be thrown.
 * @return MIPS Instruction that is in error
 */
public Instruction getInstruction() {return instruction;}

}
