/*
 * Created on 26-Oct-2003
 * 
 */
package ic.doc.yams;

public interface CycleManagerInterface {

	public void start();
	// start the processor
 
	public void stop();
	// stop the processor after the current cycle finishes executing

	public void terminate();
	// terminate the simulation after the current cycle finishes
	//  executing

	public void advance();
	// when stopped, execute the next instruction
	
	public void skip();
	// when stopped, skip the next instruction
	
	public void jump(MIPSWord address);
	// when stopped, jump to address
}

