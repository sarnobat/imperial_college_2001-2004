/*
 * Created on 06-Nov-2003
 */

package yams.parser;

/**
 * Models a register as an oprand of an instruction.
 * 
 * @author qq01
 */
public class RegisterOperand extends Operand {
	private String value;
	
	/**
	 * Constructs a new <code>RegisterOperand</code>.
	 * 
	 * @param value name of the register.
	 */
	public RegisterOperand(String value){
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see yams.parser.Operand#getType()
	 */
	public int getType() {
		return TYPE_REGISTER;
	}

	/**
	 * Returns the value of the oprand.
	 * 
	 * @return the value of the oprand.
	 */
	public String getValue(){
		return "$" + value;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "Register: " + getValue() + " ";
	}
}
