
package yams.GUI;

import yams.*;
import yams.processor.*;


public class ProcessorHandler extends Thread {


	private YAMSGui controller;
	private Processor processor;
	
	private boolean running = false;
	
	private static int PH_FOREVER = -1;
	private static int PH_ONCE = 1;
	private static int PH_ONCEDONE = 0;
	
	private boolean runOneLine = false;
	private boolean runSkip = false;
	
	private boolean alive = true;
	
	// Default delay to half a second
	private int delay = 3;
	
	public ProcessorHandler(YAMSGui g, Processor p) {
		controller = g;
		processor = p;
		setDelay(controller.getProcessorSpeedFromGUI());
	}


	public void run() {
		// Infinite loop until this handler is destroyed, in which case
		//   exit this run method
		while(alive) {
			
			if (running) {
				if (runOneLine) {
					// execute instructions until the next line is reached
					controller.updateProcessorStatus("Processor Running (one line)...");

					int startLine = controller.getCurrentLine();
					// execute while not finished AND [currentLine != -1 OR currentLine == startLine]
					while(running && (!processor.cycleManager.hasFinished()) && ((controller.getCurrentLine() == -1) || controller.getCurrentLine() == startLine) ) {
						processor.cycleManager.advance();
						try { sleep(delay); } catch (InterruptedException e) {}
					}
					
					// stop the processor
					running = false;
					runOneLine = false;
					
					// if finished, disable buttons
					if (processor.cycleManager.hasFinished()) {
						controller.disableProgramCodeButtons();
					}
					
					// update currently highlighted line
					controller.updateBreakPointPanel();
					
				} else if (runSkip) {
					// skip the next line
					controller.updateProcessorStatus("Processor Running (skipping a line)...");

					int startLine = controller.getCurrentLine();
					// execute while not finished AND [currentLine != -1 OR currentLine == startLine]
					while(running && (!processor.cycleManager.hasFinished()) && ((controller.getCurrentLine() == -1) || controller.getCurrentLine() == startLine) ) {
						processor.cycleManager.skip();
						try { sleep(delay); } catch (InterruptedException e) {}
					}
					
					// stop the processor
					running = false;
					runSkip = false;
					
					// if finished, disable buttons
					if (processor.cycleManager.hasFinished()) {
						controller.disableProgramCodeButtons();
					}
					
					// update currently highlighted line
					controller.updateBreakPointPanel();
					
				} else {
					// run next instruction normally
					controller.updateProcessorStatus("Processor Running...");

					processor.cycleManager.advance();
					
					// if finished, stop processor and disable buttons
					if (processor.cycleManager.hasFinished()) {
						running = false;
						controller.disableProgramCodeButtons();
					}
					
					// update currently highlighted line
					controller.updateBreakPointPanel();
					
					// if the current line is breakpointed, stop the processor
					if (controller.currentLineHasBreakPoint()) {
						running = false;
					}
					try {sleep(delay);}	catch (InterruptedException e) {}
				}
				
			} else {
				// update processor status label
				if (processor.cycleManager.hasFinished()) {
					controller.updateProcessorStatus("Program Finished");
				} else {
					controller.updateProcessorStatus("Processor Stopped");
				}

				// sleep for half a second
				try {sleep(500);}
				catch (InterruptedException e) {}
			}
		}
	}
	
	
	/**
	 * Causes the processor to run until it reaches the next line and then stop
	 */
	public void runOne() {
		runOneLine = true;
		running = true;
	}
	
	/**
	 * Causes the processor to skip the next line
	 */
	public void runSkip() {
		runSkip = true;
		running = true;
	}
	
	/**
	 * Starts the processor running normally
	 *
	 */
	public void processorStart() {
		running = true;
	}
	
	/**
	 * Stops the processor from running
	 */
	public void processorStop() {
		running = false;
		runOneLine = false;
		runSkip = false;
	}
	
	/**
	 * Causes the run() method to exit
	 */
	public void destroy() {
		alive = false;
	}
	
	/**
	 * Returns the status of the processor
	 * @return returns true if the processor is in the running state, false otherwise
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * Sets the delay between instructions being executed in milliseconds
	 * @param d delay in milliseconds
	 */
	public void setDelay(int d) {
		delay = d;
	}
	
	/**
	 * Returns the delay between instuctions being executed in milliseconds
	 * @return delay in milliseconds
	 */
	public int getDelay() {
		return delay;
	}

}
