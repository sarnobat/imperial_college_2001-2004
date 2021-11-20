/*
 * Created on 26-Oct-2003
 *
 */
package ic.doc.yams;

public class CycleManager implements CycleManagerInterface {

	private ControllerInterface controller;
	private RegisterManagerInterface registerManager;
	private MemoryManagerInterface memoryManager;
	private InstructionDecoderInterface instructionDecoder;
	private StatisticsManagerInterface statisticsManager;
		
	private boolean running;

	public CycleManager(ControllerInterface controller,
						RegisterManagerInterface registerManager,
						MemoryManagerInterface memoryManager,
						InstructionDecoderInterface instructionDecoder,
						StatisticsManagerInterface statisticsManager) {
		this.controller = controller;
		this.registerManager = registerManager;
		this.memoryManager = memoryManager;
		this.instructionDecoder = instructionDecoder;
		this.statisticsManager = statisticsManager;
		running = false;
		// controller should next issue jump() to start of code, and start()
	}

	public void start() {
		running = true;
		while(running) {
			executeInstruction();
		}
	}

	public void stop() {
		running = false;
	}

	public void terminate() {
		stop();
		// controller.terminateSimulation();
	}

	public void advance() {
		if(!running) {
			executeInstruction();
		}
	}

	public void skip() {
		if(!running) {
			incrementPC();
		}
	}

	public void jump(MIPSWord address) {
		if(!running) {
			// registermanager.PC.set(address);
		}
	}

	private boolean executeInstruction() {
		boolean executionSuccessful = false;
		MIPSWord instruction;
		// instruction = memorymanager.retrieve(registermanager.PC.get());
		// executionSuccessful = instructiondecoder.handle(instruction); 
		incrementPC();
		return executionSuccessful;
	}

	private void incrementPC() {
		// registermanager.PC.set(registermanager.PC.get() + 4);
	}
}
