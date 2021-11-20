/*
 * Created on 30-Oct-2003
 *
 */
package yams.processor;
import java.io.PrintStream;

/**
 * Interface for the instruction and syscall handlers to implement.
 * @author sw00
 *
 */
public interface InstructionHandlerInterface {

	/**
	 * Executes the instruction
	 * @param instruction
	 * @return success of execution
	 */
	public boolean execute(int instruction);
	
	/**
	 * Sets the cycle manager
	 * @param cycleManager
	 */
	public void setCycleManager(CycleManagerInterface cycleManager);
	

	/**
	 * Sets the verbose PrintStream
	 * @param verbose
	 */
	public void setVerbose(PrintStream verbose);

}
