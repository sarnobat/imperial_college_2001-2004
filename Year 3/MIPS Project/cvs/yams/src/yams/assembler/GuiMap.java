/*
 * Created on 10-Nov-2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package yams.assembler;

import java.util.TreeMap;
import yams.parser.Instruction;

/**
 * The GUIMap component is a table whose existence is pertinent from the GUI point of view. It maps the line numbers of the original MIPS file to their assembled locations within memory, and can be used in the GUI to keep track of which line is being executed during a run of the program. 
 *
 * @author jkm01
 */
public class GuiMap {
	
	private TreeMap table;
	
	/**
	 * Constructor creates a new table into which Key,Value pairs can be added.
	 */
	// this class will hold addresses of memory locations assigned to a label
	public GuiMap() {
		table = new TreeMap();
	}
	
	/**
	 * Method to clear all the information within the GUIMap table TreeMap in this class
	 */
	public void resetGuiMap()
	{
		table.clear();
	}
	
	/**
	 * 
	 * Returns true if the line number is contained within the stored TreeMap (ie has an associated address)
	 * 
	 * @param line Requested Line Number
	 * @return true if contains requested line number -> address mapping
	 */
	public boolean containsLine(int line) {
		return table.containsValue(new Integer(line));
	}
	
	
	/**

	 * Returns true if the address is contained within the stored TreeMap (ie has an associated line number)
	 * @param address
	 * @return true if contains requested address
	 */
	public boolean containsAddress(int address) {
		return table.containsKey(new Integer(address));
	}
	
	
	/**
	 * 
	 * Returns the line number within the MIPS code of the given memory address.
	 * 
	 * @param address
	 * @return returns line number of the given memory address
	 */
	public int returnLine(int address) {
		return ((Integer)table.get(new Integer(address))).intValue();
	}
	
	/**
	 * 
	 * Returns the very last line number that has been assembled to a location
	 * 
	 * @return returns the integer number of the last line assembled
	 */
	public int returnLastLine()
	{
		return ((Integer)table.get((Integer)table.lastKey())).intValue();
	}
	
	
	/**
	 * 
	 * Allows a line number within the MIPS code to be added to the GUIMap with a given address, and thus instantiates
	 * the mapping within the TreeMap table.
	 * 
	 * @param line
	 * @param address
	 * @param currentInstruction
	 * @throws AssemblerException
	 */
	public void add(int line, int address, Instruction currentInstruction) throws AssemblerException
	{
		if (!containsLine(line))
			{
				if (!containsAddress(address))
					{
						table.put(new Integer(address), new Integer(line));
					}
			}
		else throw new AssemblerException("[Error A16] Line already used within assembler",line,currentInstruction);		
	}
}