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
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class GuiMap {
	
	private TreeMap table;
	
	// this class will hold addresses of memory locations assigned to a label
	
	public GuiMap() {
		table = new TreeMap();
	}
	public void resetGuiMap()
	{
		table.clear();
	}
	
	public boolean containsLine(int line) {
		return table.containsValue(new Integer(line));
	}
	
	
	public boolean containsAddress(int address) {
		return table.containsKey(new Integer(address));
	}
	
	
	public int returnLine(int address) {
		return ((Integer)table.get(new Integer(address))).intValue();
	}
	
	public int returnLastLine()
	{
		return ((Integer)table.get((Integer)table.lastKey())).intValue();
	}
	
	
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