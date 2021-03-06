/*
 * Created on 30-Oct-2003
 *
 */
package yams.p cessor;

/**
 * Interface for the instruction decoder
 * @author sw00
 */
public interface InstructionDecoderInterface {

	/**
	 * Decodes instruction, and passes it to the appropriate instruction handler to execute
	 * @param instruction
	 * @return success of execution
	 */
	public boolean handle(int instruction);
	// decodes instruction, then passes it to execute in the instruction handler
	//   or syscall handler as appropriate
	// returns true for success

}
