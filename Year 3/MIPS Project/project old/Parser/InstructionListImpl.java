package parser;

import java.util.*;

public class InstructionListImpl 

	Map m = new TreeMap();
	//crucially, TreeMap is a sorted Map
	public Map getInstructionList() {
		//Instruction[] instructionListArr =  new Instruction[m.values().size()];
		return m;
	}

}
