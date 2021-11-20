/*
 * Created on 28-Oct-2003
 *
 */
package ic.doc.yams;

public class InstructionDecoder implements InstructionDecoderInterface {
	
	private RegisterManagerInterface registerManager;
	private MemoryManagerInterface memoryManager;
	private StatisticsManagerInterface statisticsManager;
	private ALUInterface alu;
	private InstructionHandlerInterface instructionHandler;
	private InstructionHandlerInterface syscallHandler;
			
	public InstructionDecoder(RegisterManagerInterface registerManager,
				MemoryManagerInterface memoryManager,
				StatisticsManagerInterface statisticsManager,
				ALUInterface alu,
				InstructionHandlerInterface instructionHandler,
				InstructionHandlerInterface syscallHandler) {
		this.registerManager = registerManager;
		this.memoryManager = memoryManager;
		this.statisticsManager = statisticsManager;
		this.alu = alu;
		this.instructionHandler = instructionHandler;
		this.syscallHandler = syscallHandler;
	}

	public boolean handle(MIPSWord instruction) {
		boolean success = false;
		// determine whether instruction is a syscall
		// if(syscall) {
		//   success = syscallHandler.execute(instruction);
		// }
		// else {
		//   success = instructionHandler.execute(instruction);
		// }
		return success;
	}

}
