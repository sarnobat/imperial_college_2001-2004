/*
 * Created on 03-Dec-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

package yams.assembler;
import java.util.TreeMap;
import java.util.Set;
import java.util.Iterator;
import yams.parser.Instruction;
/**
 * @author jkm01
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

public class InstructionTable
{
	TreeMap instructionMap;	
	TreeMap machineCodeMap;

	public InstructionTable()
	{
		instructionMap = new TreeMap();
		machineCodeMap = new TreeMap();
	}
	public String printTable()
	{
		String outputString="";
		Set instructionSet = instructionMap.keySet();
		Iterator instructionSetIterator = instructionSet.iterator();
		while (instructionSetIterator.hasNext())
		{
			String currentKey = (String)instructionSetIterator.next();
			InstructionTableRow currentRow= (InstructionTableRow)instructionMap.get(currentKey);
			outputString+= currentRow.printRow();
		}
		return outputString;
	}
	public void resetInstructionTable2()
	{
		instructionMap.clear();
		machineCodeMap.clear();
	}
	public void addInstruction(String instructionName, InstructionTableRow r) throws AssemblerException
	{
		instructionMap.put(instructionName, r);
		
		// for debugging add mapping from code to monatomic instructions
		String machineCode;
		if (r.getType().equals("Regular"))
			{
				Representation thisRepresentation;
				Iterator i = r.getRepresentationsIterator();
				while (i.hasNext())
					{
						String key = (String)i.next();
						thisRepresentation = r.getRepresentation(key);	
						machineCode=thisRepresentation.getMachineCode();
						if (machineCode.length()==32)
							{
								if (machineCode.substring(0,6).equals("000000"))
									{
										machineCodeMap.put(machineCode.substring(0,6)+machineCode.substring(26,32),instructionName);														
									}
								else
									{
										machineCodeMap.put(machineCode.substring(0,6), instructionName);														
									}
							}
					}
			}
		else if (r.getType().equals("Extended"))
			{
				machineCode = r.getCoreMachineCode();
				if (machineCode.substring(0,6).equals("000000"))
					{
						machineCodeMap.put(machineCode.substring(0,6)+machineCode.substring(26,32),instructionName);														
					}
				else
					{
						machineCodeMap.put(machineCode.substring(0,6), instructionName);														
					}
			}
	}
	public boolean isInstruction(String instructionName)
	{
		return instructionMap.containsKey(instructionName);
	}
	public InstructionTableRow getInstruction(String instructionName, int line, Instruction currentInstruction) throws AssemblerException
	{
		if (isInstruction(instructionName))	return (InstructionTableRow)instructionMap.get(instructionName);
		else								throw new AssemblerException("[Error A17] Instruction "+instructionName+" doesnt exist in instructionfile",line,currentInstruction);
	}
	public String getMonatomicInstruction(String machineCode, int line, Instruction currentInstruction) throws AssemblerException
	{
		String name="";
		if (machineCode.length()==32)
			{
				if (machineCode.substring(0,6).equals("000000"))
					{
						name = (String)machineCodeMap.get(machineCode.substring(0,6)+machineCode.substring(26,32));														
					}
				else
					{
						name = (String)machineCodeMap.get(machineCode.substring(0,6));														
					}				
			}
		if (name=="") throw new AssemblerException("[Error A18] Instruction "+machineCode+" doesnt exist as monatomic instruction in instructionfile",line, currentInstruction);
		else return name;
	}	
	
}