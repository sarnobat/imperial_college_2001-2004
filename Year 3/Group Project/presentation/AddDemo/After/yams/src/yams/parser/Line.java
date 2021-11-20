/*
 * Created on 06-Nov-2003
 */
package yams.parser;

/**
 * Models a line of code in a MIPS program that is input to the program. 
 * A line consists of a line number, an optional Label and an optional Instruction.
 * The latter two are optional because the line could be blank by the user's decision
 * 
 * @author qq01
 */
public class Line {
	private String original;
	private Instruction instruction;
	private Label label;
	private int lineNumber;

	/**
	 * Creates a Line with no label or instruction
	 * 
	 * @param lineNumber line number of the line
	 */
	public Line(int lineNumber, String original) {
		this.lineNumber = lineNumber;
		this.original = original;
	}

	/**
	 * Creates a line with an instruction only
	 * 
	 * @param lineNumber line number of the line
	 * @param instruction the instruction
	 */
	public Line(int lineNumber, String original, Instruction instruction) {
		this.lineNumber = lineNumber;
		this.original = original;
		this.instruction = instruction;
	}

	/**
	 * Creates a Line with a label only
	 * 
	 * @param lineNumber line number of the line
	 * @param label the label
	 */
	public Line(int lineNumber, String original, Label label) {
		this.lineNumber = lineNumber;
		this.original = original;
		this.label = label;
	}

	/**
	 * creates a Line with a label AND an instruction.
	 * 
	 * @param lineNumber the line number
	 * @param label the label
	 * @param instruction the instruction
	 */
	public Line(int lineNumber,String original, Label label, Instruction instruction) {
		this.lineNumber = lineNumber;
		this.original = original;
		this.label = label;
		this.instruction = instruction;
	}

	/**
	 * Whether this line contains an instruction.
	 * 
	 * @return <code>true</code> it contains an instruction, <code>false</code> if it doesn't
	 */
	public boolean containsInstruction() {
		return instruction != null;
	}

	/**
	 * Whether this line contains a label
	 * 
	 * @return <code>true</code> if contails a label, <code>false</code> if not
	 */
	public boolean containsLabel() {
		return label != null;
	}

	/** 
	 * Returns the instruction in this line.
	 * 
	 * @return the instruction in this line
	 */
	public Instruction getInstruction() {
		return instruction;
	}

	/**
	 * Returns the label object. Note that a <code>null</code> is returned if no label is present
	 * 
	 * @return the label
	 */
	public Label getLabel() {
		return label;
	}

	/**
	 * Returns the line number of the line.
	 * 
	 * @return the line number of the line
	 */
	public int getLineNumber() {
		return lineNumber;
	}

	/**
	 * Whether the line is a blank line.
	 * 
	 * @return true if the line is a blank line
	 */
	public boolean isBlank() {
		return (label == null && instruction == null);
	}
	
	public String getOriginal(){
		return original;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer("Line(" + lineNumber + "): " + original + "\n");

		if (label != null) {
			buffer.append("Label: " + label.toString() + ", ");
		}

		if (instruction != null) {
			buffer.append(instruction.toString());
		}
		
		buffer.append('\n');

		return buffer.toString();
	}
}
