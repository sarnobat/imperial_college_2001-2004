/*
 * Created on 06-Nov-2003
 */
package yams.parser;

/**
 * Models a label. A label is not an instruction, and cannot take arguments, therefore
 * it is not a subclass of <code>Instruction</code>.
 * 
 * @author qq01
 */
public class Label {
	private String label;
	
	/**
	 * constructs a label with the given name
	 * 
	 * @param label name of the label
	 */
	public Label(String label){
		this.label = label;
	}
	
	/**
	 * returns the name of the label
	 * 
	 * @return name of the label
	 */
	public String getLabel() {
		return label;
	}
	
	
	/**
	 * Returns a string representation of the object.
	 * 
	 * @return a string representation of the object.
	 */
	public String toString(){
		return label;
	}
}
