/*
 * Created on 08-Dec-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package yams.assembler;

/**
 * Contained as objects within a TreeMap in the Representation object, this class stores information on specific operands e.g. immediates etc.
 * 
 * @author jkm01
 */
public class OperandDetails
{
	private int operandNumber;
	private String operandType;
	/*
	* 0 Immediate   	mask,num
	* 1 Register		
	* 2 Label				mask,num,offset
	* 3 Addressing	offset
	* 4 Addressing Immediate
	* 5 Addressing Immediate(register)
	* 6 Addressing Label
	* 7 Addressing Label immediate
	* 8 Addressing Label Immediate Register
	* 9 Addressing Register
	*/
	private String mask;
	private int encodeBits;
	private int outputBits;
	private int offsetMode;

	public OperandDetails(int num, String t, String m, int e, int out, int off)
	{
		operandNumber = num; operandType = t; mask = m; encodeBits = e; outputBits = out; offsetMode = off;
	}
	public String printOperand()
	{
		return "       number: "+operandNumber+" type: "+operandType+" mask: "+mask+" encodeBits: "+encodeBits+" outputBits: "+outputBits+" offsetMode: "+offsetMode+"\n";
	}
	public String getType() {return operandType;}
	public String getMask() {return mask;}
	public int getEncodeBits() {return encodeBits;}
	public int getOutputBits() {return outputBits;}
	public int getOffset() {return offsetMode;}
	public int getNumber() {return operandNumber;}
}
