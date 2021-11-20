/*
 * Created on 20-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package yams.assembler;

import yams.parser.Instruction;;

/**
 * @author jkm01
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class AssemblerException extends Exception {

private int line;
private Instruction instruction;

public AssemblerException(String s, int lineNumber, Instruction thisInstruction)
{
	super(s);
	line=lineNumber;
	instruction=thisInstruction;
}

public int getLine() {return line;}
public Instruction getInstruction() {return instruction;}

}
