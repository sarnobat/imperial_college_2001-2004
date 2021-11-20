/*
 * Created on 26-Oct-2003
 *
 */
package yams.processor;

public interface RegisterManagerInterface {

	/**
	 * Sets the value of the register
	 * @param regid String value of the register
	 * @param val Value to be stored
	 * @return success
	 */
	public boolean setReg(String regid, int val);
	
	/**
	 * Sets the value of the register
	 * @param regid Int value of the register
	 * @param val Value to be sotred
	 * @return success
	 */
	public boolean setReg(int regid, int val);

	/**
	 * Returns the value of the register
	 * @param regid 
	 * @return register value
	 */
	public int getReg(String regid);
	public int getReg(int regid);
	
	/**
	 * Returns the register value without incrementing the count in the statistics manager
	 * @param regid
	 * @return register value
	 */
	public int getRegOnly(String regid); 
	public int getRegOnly(int regid);
	
	/**
	 * Resets the registers to 0
	 */
	public void reset();

	/**
	 * Gets name corresponding to register id, for example 31 maps to $ra 
	 * @return name of register
	 */
	public String getRegName(int id) throws Exception;
	
	/**
	 * Gets the total number of registers present in the processor
	 * @return number of registers
	 */
	public int getTotalNumberOfRegisters();
}
