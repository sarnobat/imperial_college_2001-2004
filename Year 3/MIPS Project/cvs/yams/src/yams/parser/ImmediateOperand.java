/*
 * Created on 11-Nov-2003
 */
package yams.parser;

/**
 * Models an operand that's a immediate value.
 * 
 * @author ss401
 */
public class ImmediateOperand extends Operand {
	private int value;
	
	/**
	 * Constructs an new <code>ImmediateOperand</code> by the given <code>value</code>
	 * 
	 * @param value value of the operand.
	 */
	public ImmediateOperand(int value){
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see yams.parser.Operand#getType()
	 */
	public int getType() {
		return TYPE_IMMEDIATE;
		
	}
	public int getValue(){
		return value;
	}
	
	/* (non-Javadoc)
	 * @see yams.parser.Operand#toString()
	 */
	public String toString() {
		return Integer.toString(value);
	}
}
