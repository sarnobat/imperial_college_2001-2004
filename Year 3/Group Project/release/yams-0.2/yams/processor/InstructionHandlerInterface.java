/*
 * Created on 30-Oct-2003
 *
 */
package yams.p cessor;
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
	
	public void setCycleManager(CycleManagerInterface cycleManager);
	
	/* Sets the verbose PrintStream */
	public void setVerbose(PrintStream verbose);

}
