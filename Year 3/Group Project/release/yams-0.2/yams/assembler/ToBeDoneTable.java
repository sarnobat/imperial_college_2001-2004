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
 * @author jkm01
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

public class ToBeDoneTable 
{
	//table contains all the entries that need to be updated in the second phase of assembling
	
	private TreeMap table;
		
	public ToBeDoneTable()
	{
		table = new TreeMap();
	}
	public void resetToBeDoneTable()
	{
		table.clear();
	}
	public void add(int a, Instruction i, int lineNumber) {table.put(new Integer(table.size()),new ToBeDoneRow(a,i,lineNumber));}
	public int requiredActions() {return table.size();}
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
	public int size() {return table.size();}

	public class ToBeDoneRow
	{
		private int next_text_address;
		private Instruction instruction;
		private int lineNumber;
		
		public ToBeDoneRow(int a, Instruction i, int lN)
		{
			next_text_address=a; instruction = i; lineNumber = lN;
		}
		public int getAddress() {return next_text_address;}
		public Instruction getInstruction() {return instruction;}
		public int getLineNumber() {return lineNumber;}
	}

}