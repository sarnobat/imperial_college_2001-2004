package parser;

import java.util.*;

public class InstructionListImpl 

	Map m = new TreeMap();
	//crucially, TreeMap is a sorted Map

	/**
	 * Returned array is sorted by line number
	 */
	public Instruction[] getInstructionList() {
		//Instruction[] instructionListArr =  new Instruction[m.values().size()];
		return (Instruction[]) m.values().toArray();
	}

	/**
	 *
	 */
	public Instruction getInstruction(int lineNumber) {
 		return (Instruction) m.get(new Integer(lineNumber));
	}

}
