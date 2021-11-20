/*
 * Created on 26-Oct-2003
 *
 */
package ic.doc ams;

public interface InstructionHandlerInterface {

	// the instruction and syscall handlers should implement this interface

	public boolean execute(MIPSWord instruction);
	// executes instruction, returns true for success
}
