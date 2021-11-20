/*
 * Created on 09-Dec-2003
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
public class Representation {

	private String operandsEncoding;
	private String machineCode;
	private TreeMap operandDetails;
	
	public Representation()
	{
		operandDetails = new TreeMap();
	}
	public void setOperandsEncoding(String opse)
	{
		operandsEncoding = opse;
	}
	public void setMachineCode(String mc)
	{
		machineCode = mc;
	}
	public String printRepresentation()
	{
		String outputString="Encoding: "+operandsEncoding+" machineCode: "+machineCode+" OPERANDS: \n";
		Set instructionSet = operandDetails.keySet();
		Iterator instructionSetIterator = instructionSet.iterator();
		while (instructionSetIterator.hasNext())
		{
			int currentKey = ((Integer)instructionSetIterator.next()).intValue();
			OperandDetails currentOperand = (OperandDetails)operandDetails.get(new Integer(currentKey));
			outputString += currentOperand.printOperand();
		}
		return outputString;
	}
/*
	public void addOperand(String optype, OperandDetails o)
	{
		operandDetails.put(optype,o);
	}
	public boolean isOperand(String optype)
	{
		return operandDetails.containsKey(optype);
	}
	public OperandDetails getOperand(String optype)
	{
		if (isOperand(optype))  return (OperandDetails)operandDetails.get(optype);
		else					return null;
	}
*/	
	// index operand by number
	public void addOperand(int number,OperandDetails o)
	{
		operandDetails.put(new Integer(number),o);
	}
	public OperandDetails getOperand(int number,String type)
	{
		if (isOperand(number,type)) return (OperandDetails)operandDetails.get(new Integer(number));
		else return null;
	}
	public boolean isOperand(int number,String type)
	{
		return (operandDetails.containsKey(new Integer(number)) && ((OperandDetails)operandDetails.get(new Integer(number))).getType().equals(type));
	}
	//index oeprand by number	
		
		
	public String getEncoding()
	{
		return operandsEncoding;
	}
	public String getMachineCode()
	{
		return machineCode;
	}
}
