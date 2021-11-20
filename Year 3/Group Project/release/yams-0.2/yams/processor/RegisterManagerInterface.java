/*
 * Created on 26-Oct-2003
 *
 */
package yams.processor;

public interface RegisterManagerInterface {

	/**
	 * Sets the value of the register
	 * @param regid
	 * @param val
	 * @return
	 */
	public boolean setReg(String regid, int val); //throws UnknownRegisterException
	public boolean setReg(int regid, int val);

	/**
	 * Returns the value of the register
	 * @param regid
	 * @return
	 */
	public int getReg(String regid); //throws UnkownRegisterException
	public int getReg(int regid);
	
	/**
	 * Returns the register value without incrementing the count in the statistics manager
	 * @param regid
	 * @return
	 */
	public int getRegOnly(String regid); //throws UnkownRegisterException
	public int getRegOnly(int regid);
	
	/**
	 * Resets the registers to 0
	 */
	public void reset();

	/**
	 * Gets name corresponding to regsiter id (e.g. 31 maps to $ra) 
	 * @author ss401
	 */
	public String getRegName(int id) throws Exception;
	
	/**
	 * Gets the total number of registers present in the processor
	 * @return
	 */
	public int getTotalNumberOfRegisters();
}
