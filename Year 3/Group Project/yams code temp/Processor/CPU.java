/*
 * Created on 26-Oct-2003
 *
 */
package uk.ac.ic.doc.yams;

public class CPU {

	public CycleManag Interface cycleManager;
	public InstructionDecoderInterface instructionDecoder;
	public ALUInterface alu;
	public RegisterManagerInterface registerManager;
	public MemoryManagerInterface memoryManager;
	public StatisticsManagerInterface statisticsManager;

}
