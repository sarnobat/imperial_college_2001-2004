/*
 * Created on 04-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package yams.assembler;

import java.util.TreeMap;
import yams.parser.Instruction;


/**
 *The assembler will be reading and assembling the provided user code “line by line,? therefore there 
 *will be occasions where an instruction cannot be immediately translated into machine code. For example,
 * if we encounter a ‘j _location? (JUMP to _location) MIPS instruction and _location does not appear till 
 * some later line in the program, the assembler will not be able to work out the offset to assign the 
 * corresponding target jump instruction in binary. Therefore the translation of this instruction must be left 
 * till a later time, and the ToBeDone table contains a list of these instructions that need implementing 
 * along with the addresses to which they need assembling in memory.
 * 
 * @author jkm01
 */

public class ToBeDoneTable 
{
	//table contains all the entries that need to be updated in the second phase of assembling
	
	private TreeMap table;
		
	public ToBeDoneTable()
	{
		table = new TreeMap();
	}
	
	/**
	 * Resets the entries in the ToBeDoneTable by clearing the TreeMap of all entries.
	 */
	public void resetToBeDoneTable()
	{
		table.clear();
	}
	
	/**
	 * Allows an instruction with a destination address to be added to the ToBeDoneTable.
	 * <BR><BR>
	 * @param a		Address for future assembly
	 * @param i		Instruction to be added
	 * @param lineNumber Start Address of where to begin reencoding the instruction from.
	 */
	public void add(int a, Instruction i, int lineNumber) {table.put(new Integer(table.size()),new ToBeDoneRow(a,i,lineNumber));}
	
	/**
	 * 
	 * Returns the number of required actions that need to be taken care of in the ToBeDoneTable.
	 * 
	 * @return returns size of table.
	 */
	public int requiredActions() {return table.size();}
	
	/**
	 * Returns a ToBeDoneRow object (see ToBeDoneRow structure) when a given index line is given. The instruction to be handled
	 * can be retrieved from this object.
	 * 
	 * @param i Index Number to retrieve from.
	 * @return returns the Row object containing the things that need to be done
	 * @throws AssemblerException
	 */
	public ToBeDoneRow get(int i) throws AssemblerException
	{
		if (i>=0)
			{
				if (i<table.size())
					{
						return (ToBeDoneRow)table.get(new Integer(i));
					}
				else
					{
						throw new AssemblerException("Error A38 - This row number ("+i+") already exists within the symbol table, addition aborted",-1,null);
					}
			}
		else
			{
				throw new AssemblerException("Error A39 - This row number is 0, addition to symbol table aborted",-1,null);
			}
	}
	
	/**
	 * 
	 * Returns the size of the ToBeDoneTable.
	 * 
	 * @return returns size of table
	 */
	public int size() {return table.size();}

	/**
	 *
	 * This class encapulates all the information needed for a specific instruction to be encoded on the second assembler pass.
	 *
	 * @author Administrator
	 * 
	 */
	public class ToBeDoneRow
	{
		private int next_text_address;
		private Instruction instruction;
		private int lineNumber;
		
		/**
		 * 
		 * Creates a new row, containing an address for future assembly, an associated instruction to be handled on the second pass, and the line number in the original MIPS code that is the source of this instruction.
		 * The methods within this class are simply get / set methods for retrieving information stored within it.
		 * 
		 * @param a
		 * @param i
		 * @param lN
		 */
		public ToBeDoneRow(int a, Instruction i, int lN)
		{
			next_text_address=a; instruction = i; lineNumber = lN;
		}
		public int getAddress() {return next_text_address;}
		public Instruction getInstruction() {return instruction;}
		public int getLineNumber() {return lineNumber;}
	}

}