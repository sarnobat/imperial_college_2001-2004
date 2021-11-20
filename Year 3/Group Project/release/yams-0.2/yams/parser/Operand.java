/*
 * Created on 06-Nov-2003
 */
package yams.parser;

/**
 * Operand of instructions. This class has subclasses that model different types of operands.
 * 
 * @author qq01
 */
public abstract class Operand {
	
	public static final int TYPE_ADDRESSING = 0;

	/**
	 * Indicates the type of the operand being <b>immediate</b>.
	 */
	public static final int TYPE_IMMEDIATE = 1;
	
	/**
	 * Indicates the type of the operand being <b>label</b>
	 */
	public static final int TYPE_LABEL = 2;

	/**
	 * Indicates the type of the operand being <b>register</b>.
	 */
	public static final int TYPE_REGISTER = 3;
	
	public static final int TYPE_STRING = 4;

	/**
	 * Identifies the type of the operand.
	 * 
	 * @return type of the operand
	 */
	public abstract int getType();
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString();

}
