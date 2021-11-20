/*
 * Created on 26-Oct-2003
 *
 */
package yams.processor;

import yams.assembler.GuiMap;
import yams.parser.LineList;

public interface StatisticsManagerInterface {

	/**
	 * Increments the usage count for the specified register
	 * @param regid The name of the register
	 */
	public void regUsed(String regid);
	
	/**
	 * Prints the statistics held in the statistics manager
	 * @param lines The line list for the original code
	 */
	public void printStats(LineList lines);
	
	/**
	 * Adds an instruction name to the instruction count list
	 * @param instrName The name of the instruction
	 */
	public void addInstruction(String instrName);
	
	/**
	 * Increments the count when an instruction is used
	 * @param instr The name of the instruction
	 */
	public void instructionUsed(String instr);
	
	/**
	 * Resets the variables in the statistics manager ready for the next program to run
	 */
	public void reset();
	
	/**
	 * Increments the CPU Cycle count for the program being executed
	 */
	public void cycle();
	
	/**
	 * Sets the line map object in the statistics manager
	 * @param gm The line map - maps memory addresses to line numbers in the program
	 */
	public void setLineMap(GuiMap gm);
	
	/**
	 * Increments the count for the amount of times the line has been executed
	 * @param address The address where the line is stored
	 */
	public void lineUsed(int address);
	
	/**
	 * Returns the number of CPU cycles used by the program so far
	 * @return Number of CPU cycles
	 */
	public int getCycles();
	
	/**
	 * Returns the total number of CPU cycles used by all of the files run so far
	 * @return Total number of CPU cycles
	 */
	public int getTotalCycles();
	
	/**
	 * Returns the total memory used by the program
	 * @return Memory used
	 */
	public int getMemoryUsed();
	
	/**
	 * Returns the usage count for the specified register
	 * @param regid The name of the register
	 * @return Usage count for the specified register
	 */
	public int getRegCount(String regid);
	
	/**
	 * Returns usage count for the specified register
	 * @param regid Number of the register
	 * @return Usage count for the specified register
	 */
	public int getRegCount(int regid);
	
	/**
	 * Returns the usage count for the specified line of the program
	 * @param line Number of the line
	 * @return Usage count for the line specified
	 */
	public int getLineCount(int line);
	
	/**
	 * Returns an array of all the instructions used in the program
	 * @return Array of instructions
	 */
	public String[] getInstr();
	
	/**
	 * Array of usage counts for the instructions used in the program (used with getInstr() )
	 * @return Array of usage counts
	 */
	public Integer[] getInstrCount();
	
	
}
