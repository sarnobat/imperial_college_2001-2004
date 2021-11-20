/*
 * Created on 2003-11-28
 */
package yams.parser;

/**
 * Models an operand of an instruction that's a label.
 * 
 * @author Qian Qiao(qq01)
 */
public class LabelOperand extends Operand {
	
	private String label;

	/**
	 * Constructs a new <code>LabelOperand</code> by the given label.
	 * 
	 * @param label the value of the label.
	 */
	public LabelOperand(String label) {
		this.label = label;
	}

	/* (non-Javadoc)
	 * @see yams.parser.Operand#getType()
	 */
	public int getType() {
		return TYPE_LABEL;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Label: " + label;
	}

	/**
	 * Returns the value of the label.
	 * 
	 * @return the value of the label.
	 */
	public String getLabel() {
		return label;
	}
}
