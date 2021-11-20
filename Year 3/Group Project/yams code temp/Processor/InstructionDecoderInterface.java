/*
 * Created on 26-Oct-2003
 *
 */
package ic.doc ams;

public interface InstructionDecoderInterface {

	public boolean handle(MIPSWord instruction);
	// decodes instruction, then passes it to execute in the instruction handler
	//  or syscall handler as appropriate
	// returns true for success
}
