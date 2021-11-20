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
 * The InstructionTable is designed to store the data read from the “Instruction_file.xml? by the AssemblerXMLHandler in a predefined object structure which can be easily read by the Assembler.
 * NB also contains a further mapping of machine code values to instruction names, used for debugging.
 *
 * @author jkm01
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
	
	/**
	 * Method returns a string, but calls all the substructures it contains to build up a string representation of all
	 * the information it contains as a result of being built from the XML file.
	 * 
	 * @return returns a String containing all the information in the table
	 */
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
	
	/**
	 * 
	 * Method resets the tables that store the instruction name -> InstructionTableRow and machine code -> instruction name.
	 * 
	 */
	public void resetInstructionTable2()
	{
		instructionMap.clear();
		machineCodeMap.clear();
	}
	
	/**
	 *
	 * Adds a new instruction to the InstructionTable, requiring the new instruction name as well as a precreated InstructionTableRow structure.
	 * 
	 * @param instructionName
	 * @param r
	 * @throws AssemblerException
	 */
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
	
	/**
	 * Returns true if the instruction name given is in the InstructionTable.
	 * 
	 * @param instructionName
	 * @return returns true if the instruction is present
	 */
	public boolean isInstruction(String instructionName)
	{
		return instructionMap.containsKey(instructionName);
	}
	
	/**
	 * Returns the InstructionTableTow entry within the InstructionTable for the give instruction name string.
	 * 
	 * @param instructionName
	 * @param line
	 * @param currentInstruction
	 * @return Returns the row associated with the instruction name
	 * @throws AssemblerException
	 */
	public InstructionTableRow getInstruction(String instructionName, int line, Instruction currentInstruction) throws AssemblerException
	{
		if (isInstruction(instructionName))	return (InstructionTableRow)instructionMap.get(instructionName);
		else								throw new AssemblerException("[Error A17] Instruction "+instructionName+" doesnt exist in instructionfile",line,currentInstruction);
	}
	
	/**
	 * 
	 * Retrieves the corresponding instruction name of a given machine code representation. It must therefore check various parts of the machine code to achieve this. NB this method is only used for debugging.
	 * 
	 * @param machineCode
	 * @param line
	 * @param currentInstruction
	 * @return returns the name of a given machine code representation
	 * @throws AssemblerException
	 */
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