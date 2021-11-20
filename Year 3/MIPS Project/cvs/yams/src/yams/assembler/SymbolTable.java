package yams.assembler;

import java.util.TreeMap;
/*
 * Created on 04-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

/**
 * The SymbolTable is designed to store maps of symbols (e.g. a label within the Text segment OR a variable name in the Data segment) to their absolute addresses within memory.
 * 
 * @author jkm01
 */
public class SymbolTable {
	
	private TreeMap table;
	
	// this class will hold addresses of memory locations assigned to a label
	public SymbolTable()
	{
		table = new TreeMap();
	}
	
	/**
	 * Clears all entries of labels -> addresses within the symbol table
	 */
	public void resetSymbolTable()
	{
		table.clear();
	}
	
	/**
	 * 
	 * Returns true if the given symbol / label is contained within the SymbolTable as a key, false if not.
	 * 
	 * @param label
	 * @return true if symbol table contains the label
	 */
	public boolean containsLabel(String label) {return table.containsKey(label);}
	
	/**
	 * 
	 * Returns true if the SymbolTable contains the given memory address.
	 * 
	 * @param address
	 * @return returns true if the symbol table contains required address
	 */
	public boolean containsAddress(int address) {return table.containsValue(new Integer(address));}
	
	/**
	 * Returns the integer address value allocated to the provided label, by examining the SymbolTable.
	 * 
	 * @param label
	 * @return returns the integer value absolute address of give label
	 */
	public int returnAddress(String label) {return ((Integer)table.get(label)).intValue();}
	
	/**
	 * Adds a given label, to a given address (ie adds a mapping of label -> address within the TreeMap, throwing an
	 * exception when that symbol / address is already existing within the SysmbolTable.
	 * 
	 * @param label
	 * @param address
	 * @param lineNumber
	 * @throws AssemblerException
	 */
	public void add(String label, int address, int lineNumber) throws AssemblerException
	{
		if (!containsLabel(label))
			{
				table.put(label,new Integer(address));
			}
		else
			{
				throw new AssemblerException("[Error A37] Current label ("+label+") already used within assembler",lineNumber,null);
			}			
	}
}