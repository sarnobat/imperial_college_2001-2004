/*
 * Created on 30-Oct-2003
 *
 */
package yams.processor;
import java.io.PrintStream;

/**
 * Interface for the cycle manager
 * @author sw00
 *
 */
public interface CycleManagerInterface {
	
	/**
	 * Start the processor
	 */
	public void start();
 
	/**
	 * Stop the processor after the current cycle finishes executing
	 */
	public void stop();

	/**
	 * When stopped, execute the next instruction 
	 */
	public void advance();
	
	/**
	 * When stopped, skip the next instruction
	 */
	public void skip();
	
	/**
	 * When stopped, jump to instruction at address
	 * @param address
	 */
	public void jump(int address);
	
	/**
	 * Set breakpoint on instruction
	 * @param address of instruction
	 */
	public void setBreakpoint(int address);
		
	/**
	 * Clear breakpoint on instruction
	 * @param address of instruction
	 */
	public void clearBreakpoint(int address);
	
	/**
	 * Returns true is a syscall exit has been executed 
	 */
	public boolean hasFinished();
	
	/**
	 * Call when a syscall exit is been executed 
	 */
	public void finish();
	
	/**
	 * Resets the finished executing flag
	 */
	public void resetFinished();
	
	/**
	 * Reset the cycle manager flags
	 */
	public void reset();
	
	/* Sets the verbose PrintStream */
	public void setVerbose(PrintStream verbose);
	
}
