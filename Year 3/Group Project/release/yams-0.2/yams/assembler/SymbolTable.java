package yams.assembler;

import java.util.TreeMap;
/*
 * Created on 04-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

/**
 * @author jkm01
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SymbolTable {
	
	private TreeMap table;
	
	// this class will hold addresses of memory locations assigned to a label
	
	public SymbolTable()
	{
		table = new TreeMap();
	}
	public void resetSymbolTable()
	{
		table.clear();
	}
	public boolean containsLabel(String label) {return table.containsKey(label);}
	public boolean containsAddress(int address) {return table.containsValue(new Integer(address));}
	public int returnAddress(String label) {return ((Integer)table.get(label)).intValue();}
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