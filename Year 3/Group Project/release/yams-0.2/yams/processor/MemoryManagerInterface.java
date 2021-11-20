/*
 * Created on 26-Oct-2003
 *
 */
package yams.processor;
import java.io.PrintStream;

public interface MemoryManagerInterface {

	/* Sets a given address to given value */
	public boolean setLocation(int addr, int val);

	/* Returns to value of a given address */
	public int getLocation(int addr);

	/* Sets the byte at a given address to given value */
	public boolean setByte(int addr, int val);

	/* Returns the byte at a given address */
	public int getByte(int addr);
	
	/* Resets the memory to 0 */
	public void reset();
	
	/* Returns the number of words in memory */
	public int memoryUsed();
	
	/* Sets the verbose PrintStream */
	public void setVerbose(PrintStream verbose);

}
