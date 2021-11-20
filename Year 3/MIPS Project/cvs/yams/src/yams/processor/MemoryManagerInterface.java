/*
 * Created on 26-Oct-2003
 *
 */
package yams.processor;
import java.io.PrintStream;

public interface MemoryManagerInterface {

	/**
	 * Sets a given address to given value
	 * @param addr Memory word address
	 * @param val Value to be set
	 * @return success
	 */
	public boolean setLocation(int addr, int val);

	/**
	 * Returns to value of a given address
	 * @param addr Memory word address to get
	 * @return Value at the memory address
	 */
	public int getLocation(int addr);

	/**
	 * Sets the byte at a given address to given value
	 * @param addr Address of memory byte
	 * @param val Value to store
	 * @return success
	 */
	public boolean setByte(int addr, int val);

	/**
	 * Returns the byte at a given address
	 * @param addr Memory byte requested
	 * @return Value at that memory address
	 */
	public int getByte(int addr);
	
	/**
	 * Resets the memory to 0
	 */
	public void reset();
	
	/**
	 * Returns the number of words in memory
	 * @return number of words in memory
	 */
	public int memoryUsed();
	
	/**
	 * Sets the verbose PrintStream
	 * @param verbose Address of printstream for verbose mode
	 */
	public void setVerbose(PrintStream verbose);

}
