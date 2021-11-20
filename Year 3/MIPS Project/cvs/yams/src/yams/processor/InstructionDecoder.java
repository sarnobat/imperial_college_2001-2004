/*
 * Created on 02-Nov-2003
 *
 */
package yams.processor;

/**
 * Instruction decoder class. Decodes instructions and passes them to either the instruction
 * or syscall handler for execution.
 * @author sw00
 */
public class InstructionDecoder implements InstructionDecoderInterface {

	private InstructionHandlerInterface instructionHandler;
	private InstructionHandlerInterface syscallHandler;
	
	// TODO - auto generate this class with another XSLT transformation (an extension)
	
	/**
	 * Creates an instruction decoder, given the handlers
	 * @param instructionHandler
	 * @param syscallHandler
	 */
	public InstructionDecoder(InstructionHandlerInterface instructionHandler,
								InstructionHandlerInterface syscallHandler) {
		this.instructionHandler = instructionHandler;
		this.syscallHandler = syscallHandler;
	}

	public boolean handle(int instruction) {
		InstructionHandlerInterface handler = instructionHandler;
		MIPSWord inst = new MIPSWord(instruction);
		MIPSBitstring op = MIPSBitstring.extract(inst, 26, 31);
		
		if(op.toInt() == 0) {
			// an R type instruction
			MIPSBitstring func = MIPSBitstring.extract(inst, 0, 5);
			if(func.toInt() == 0xC) {
				// a syscall instruction
				handler = syscallHandler;
			}
		}
		
		return handler.execute(instruction);		
	}

}
