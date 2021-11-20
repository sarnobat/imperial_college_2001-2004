/*
 * Created on 08-Dec-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package yams.assembler;
import java.util.*;

/**
 * @author jkm01
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

public class InstructionTableRow
{
	private String instructionName;
	private String instructionType;
	private String coreCode;
	private TreeMap representations;
	
	public InstructionTableRow()
	{
		representations = new TreeMap();
	}
	public Iterator getRepresentationsIterator()
	{
		return representations.keySet().iterator();
	}
	public void setInstructionName(String in)
	{
		instructionName=in;
	}
	public void setInstructionType(String type)
	{
		instructionType=type;
	}
	public String getType()
	{
		return instructionType;
	}
	public void setCoreMachineCode(String machineCode)
	{
		coreCode = machineCode;
	}
	public String getCoreMachineCode()
	{
		return coreCode;
	}
	public String printRow()
	{
		String outputString="name: "+instructionName+" REPRESENTATIONS: \n";
		Set instructionSet = representations.keySet();
		Iterator instructionSetIterator = instructionSet.iterator();
		while (instructionSetIterator.hasNext())
		{
			String currentKey = (String)instructionSetIterator.next();
			Representation currentRepresentation = (Representation)representations.get(currentKey);
			outputString += currentRepresentation.printRepresentation();
		}
		return outputString;
	}
	public void addRepresentation(String operandEncoding, Representation r)
	{
		representations.put(operandEncoding, r);		
	}
	public boolean isRepresentation(String operandEncoding)
	{
		return representations.containsKey(operandEncoding);
	}
	public Representation getRepresentation(String operandEncoding) throws AssemblerException
	{
		if (isRepresentation(operandEncoding))  return (Representation)representations.get(operandEncoding);
		else									throw new AssemblerException("[Error A19] representation "+operandEncoding+" not found for instruction "+instructionName,-1,null);
	}
	public String getName()
	{
		return instructionName;
	}
}