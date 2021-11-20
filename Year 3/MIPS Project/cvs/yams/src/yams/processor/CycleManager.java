/*
 * Created on 30-Oct-2003
 *
 */
package yams.processor;
import java.io.PrintStream;
import yams.*;

/**
 * Controls processor execution.
 * Single-stepping or running to completion are supported.
 * 
 * @author sw00
 */
public class CycleManager implements CycleManagerInterface {

	private YAMSController controller;
	private RegisterManagerInterface registerManager;
	private MemoryManagerInterface memoryManager;
	private StatisticsManagerInterface statisticsManager;
	private InstructionDecoderInterface instructionDecoder;
	private BreakpointManager breakpointManager;
	private PrintStream out;
	private PrintStream verbose;
		
	private boolean running;
	private boolean finished;
	
	/**
	 * Creates a new cycle manager
	 * @param controller
	 * @param registerManager
	 * @param memoryManager
	 * @param statisticsManager
	 * @param instructionDecoder
	 * @param out
	 * @param verbose
	 */
	public CycleManager(YAMSController controller,
						RegisterManagerInterface registerManager,
						MemoryManagerInterface memoryManager,
						StatisticsManagerInterface statisticsManager,
						InstructionDecoderInterface instructionDecoder,
						PrintStream out,
						PrintStream verbose) {
							
		this.controller = controller;
		this.registerManager = registerManager;
		this.memoryManager = memoryManager;
		this.statisticsManager = statisticsManager;
		this.instructionDecoder = instructionDecoder;
		breakpointManager = new BreakpointManager();
		this.out = out;
		this.verbose = verbose;
	
		running = false;
		finished = false;
		
		// controller should now issue a jump() to start of code, and a start()
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

	public void jump(int address) {
		if(!running) {
			registerManager.setReg("PC", address);
		}
	}

	public void setBreakpoint(int address) {
		breakpointManager.setBreakpoint(address);
	}

	public void clearBreakpoint(int address) {
		breakpointManager.clearBreakpoint(address);
	}

	public boolean hasFinished() {
		return finished;
	}
		
	public void finish() {
		running = false;
		finished = true;
	}
	
	public void resetFinished() {
		finished = false;
	}
	
	public void reset() {
		running = false;
		finished = false;
	}

	/**
	 * Execute the next instruction in memory
	 * @return success
	 */
	private boolean executeInstruction() {
		boolean executionSuccessful = false;
		int instruction;
		int pc = 0;
		pc = registerManager.getReg("PC"); 
		instruction = memoryManager.getLocation(pc);
		if(breakpointManager.isBreakpoint(instruction)) {
			reachedBreakpoint();
			return false;
		}
		executionSuccessful = instructionDecoder.handle(instruction);
		statisticsManager.cycle();
		statisticsManager.lineUsed(pc);
		incrementPC();
		
		controller.updateStatistics();
		
		return executionSuccessful;
	}

	/**
	 * Increments the program counter to point to the next instruction
	 */
	private void incrementPC() {
		int pc = 0;
		pc = registerManager.getReg("PC"); 
		pc += 4;
		registerManager.setReg("PC", pc);
	}

	/**
	 * Stops the processor, since breakpoint has been reached
	 */
	private void reachedBreakpoint() {
		verbose.println("Reached Breakpoint");
		running = false;
	}
	
	public void setVerbose(PrintStream verbose) {
		this.verbose = verbose;
	}

}
