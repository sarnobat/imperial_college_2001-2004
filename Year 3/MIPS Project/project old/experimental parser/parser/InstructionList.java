package parser;

public interface InstructionList {

	//Map m = new TreeMap(); 	public Instruction[] getInstructionList();
	//should be public Line[] getInstructionList();	//return the map
	public Instruction getInstruction(int lineNumber);	//public Line getLine(int lineNumber);
}