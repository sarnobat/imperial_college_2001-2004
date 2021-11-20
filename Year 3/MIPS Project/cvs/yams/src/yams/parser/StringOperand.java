/*
 * Created on 2003-11-28
 */
package yams.parser;

/**
 * Models an operand of an instruction that's a string.
 * 
 * @author Qian Qiao(qq01)
 */
public class StringOperand extends Operand {
	
	private String string;
	
	/**
	 * Constructs a new <code>StringOperand</code>.
	 * 
	 * @param string value of the <code>String</code>
	 */
	public StringOperand(String string){
		this.string = string;
	}

	/* (non-Javadoc)
	 * @see yams.parser.Operand#getType()
	 */
	public int getType() {
		return TYPE_STRING;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return string;
	}
	
	/**
	 * @return the actual value of the <code>String</code>
	 */
	public String getString(){
		return string;
	}
}
