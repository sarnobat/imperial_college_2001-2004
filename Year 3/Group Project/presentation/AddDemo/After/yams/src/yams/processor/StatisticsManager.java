	/*
 * Created on 26-Oct-2003
 *
 */
package yams.processor;

import yams.assembler.GuiMap;
import java.io.PrintStream;
import java.util.*;
import yams.parser.LineList;

public class StatisticsManager implements StatisticsManagerInterface {

	private TreeMap regStatMap = new TreeMap();
	private TreeMap instrStatMap = new TreeMap();
	private TreeMap lineStatMap = new TreeMap();
	private PrintStream out;
	private int cycleCount;
	private int totalCycleCount;
	private Processor processor;
	private Integer zero_value = new Integer(0);
	private GuiMap lineMap;

  	public StatisticsManager(PrintStream out, Processor p)
  	{
  	this.out = out;
  	processor = p;
  	//initialise the register statistics map
	String regname;
	for (int i=0; i<32; i++)
		{
		regname = "$" + i;
		regStatMap.put(regname, zero_value);
		}
	//initialise counts
	cycleCount = 0;
  	}
  	
  	public void regUsed(String regid) //throws UnknownRegisterNameException;
	{
	//get the current regcount of the register
	Integer value_object;
	int count;
	if (regStatMap.containsKey(regid))
	{   
		count = ((Integer) regStatMap.get(regid)).intValue();
		count++;
		value_object= new Integer(count);
		regStatMap.remove(regid);
		regStatMap.put(regid, value_object);
		}
		else
		{
	    
		}
	}
	
  	public void cycle()
  	{
  		cycleCount++;
  		totalCycleCount++;
  	}
  	
	public void addInstruction(String instrName)
		{
		//receive the instruction
		String instruction = instrName.toUpperCase();
		//add the name and count
		instrStatMap.put(instruction, zero_value);
		}
		
  	
	public void instructionUsed(String instr)
		{
		
		int count;
		instr = instr.toUpperCase();
		//get the current count
		Integer value_object;
	
		//get the name of the instruction
			
		if (instrStatMap.containsKey(instr))
			{   
			count = ((Integer) instrStatMap.get(instr)).intValue();
			count++;
			value_object= new Integer(count);
			instrStatMap.remove(instr);
			instrStatMap.put(instr, value_object);
			}
			else
			{
			//throw new UnknownInstructionException
			}
		
		}

		
	public void reset()
	{
		//reset registermap
		regStatMap = new TreeMap();
		String regname;
		for (int i=0; i<32; i++)
			{
			regname = "$" + i;
			regStatMap.put(regname, zero_value);
			}
			
		//reset instructionmap
		String instrName;
		String instr[] = new String[instrStatMap.size()];
		instrStatMap.keySet().toArray(instr);
		
		for (int i = 0; i<instr.length; i++)
				{ 
				instrName = instr[i];
				instrStatMap.remove(instrName);
				instrStatMap.put(instrName, zero_value);
				}
				
		//reset lineMap
		lineStatMap.clear();
		
		//reset instructions executed
		cycleCount = 0;
	}
	
	
	public void setLineMap(GuiMap gm)
	{ lineMap = gm; }
	
		
	public void lineUsed(int address)
	{		
		Integer line;
		Integer one = new Integer(1);
		int count;
	if (lineMap.containsAddress(address))
		{
		//get line number from lineMap
		line = new Integer(lineMap.returnLine(address));

		//update the lineStatMap 
		if (!lineStatMap.containsKey(line))
			 {
			   // doesnt contain the addr value, so add to map 
			   lineStatMap.put(line, one);
			 }
			 else
			 {
			   // does contain addr, so must replace 
			   count = ((Integer)lineStatMap.get(line)).intValue();
			   count++;
			   lineStatMap.remove(line);
			   lineStatMap.put(line, new Integer(count));
			 }
		}
	}
	
	
  	
  	
	public void printStats(LineList lineList)
	{
	int count;
	String line;
	int z;
	out.println("");
    out.println("==============");
	out.println(" STATISTICS");
    out.println("==============");
	out.println("");
	//lines used 
	out.println("------------LINES EXECUTED------------");
	out.println("");
	out.println("Line                                  Count");
	int lastLine = lineMap.returnLastLine();
	int startLine = lineMap.returnLine(0x400000);
	
	for (int i=startLine; i<=lastLine; i++)
		{
			count = 0;
			line = lineList.getLine(i).getOriginal();
			if (lineList.getLine(i).containsInstruction())
			{
			
				if (lineStatMap.containsKey(new Integer(i)))
					{
					count = ((Integer)lineStatMap.get(new Integer(i))).intValue();
					}
			
				//add extra space if less than 10 to format nicely.
				if (i < 10)
				  z=1;
				else
				  z=0;
				 
				out.println(i + "   " + removeTabs(line) + ":" + spacer(38 + z, line.length() + 4) + count);
			}
			else
			{
				out.println(i + "   " + removeTabs(line));
			}
				
		}
		
	//instructions used
	out.println("");
	out.println("-----------INSTRUCTION USED-----------");
	out.println("");
	out.println("Instr     Count");
	String instrName;
	Integer instrCount;
	for(Iterator instrNames = instrStatMap.keySet().iterator(); instrNames.hasNext();)
	  {
	  instrName = (String)instrNames.next();
	  instrCount = (Integer)instrStatMap.get(instrName);
	  if (instrCount.intValue() != 0)
		  {
			  out.println(instrName + ":" + spacer(11,instrName.length()) + instrCount.toString());  
		  }
	   
	  }

		
	out.println("");
	//registers used
	out.println("------------REGISTERS USED------------");
	out.println("");
	out.println("Reg       Count");
	String regname;
	count = 0;
	for (int i=0; i<32; i++)
	{
		regname = "$" + i;
		count = ( (Integer) regStatMap.get(regname)).intValue();
		if (count > 0)
		{
			out.println(regname + ":" + spacer(11, regname.length()) + count);
		}
	}

		out.println("");
		//Processor cycles
		out.println("CPU CYCLES: " + cycleCount);
		out.println("");
 
	 //memory used
	 out.println("MEMORY USED: " + processor.memoryManager.memoryUsed() + " WORDS");
	 out.println("");
	  	  
	}
	
	
	public int getCycles(){
		return cycleCount;
	}
	
	public int getMemoryUsed() {
		return processor.memoryManager.memoryUsed();
	}
	
	public int getTotalCycles() {
		return totalCycleCount;
	}
	
	public int getRegCount(String regid) {
		return ((Integer)regStatMap.get(regid)).intValue();
	}
	
	public int getRegCount(int regid) {
		return getRegCount("$" + regid);
	}

	public int getLineCount(int line) {
		if (lineStatMap.containsKey(new Integer(line)))
		  return ((Integer)lineStatMap.get(new Integer(line))).intValue();
		else
		  return 0;
	}
	
	public String[] getInstr()
	{
	String instr[] = new String[instrStatMap.size()];
	instrStatMap.keySet().toArray(instr);
	return instr;
	}
	
	public Integer[] getInstrCount()
	{
	Integer instr[] = new Integer[instrStatMap.size()];
	instrStatMap.values().toArray(instr);
	return instr;
	}
	
	
	/**
	 * Used to format the output nicely. Generates a space of the required length
	 * @param totalLength - available space
	 * @param usedLength - used space
	 * @return String of spaces of the required length
	 */
	private String spacer(int totalLength, int usedLength)
	{
		String result = "";
		int length = totalLength - usedLength;
		for (int i=0; i<length; i++)
		{
			result = result + " ";
		}
		
		return result;
		}
		
	/**
	 * Used to format the output nicely by removing tabs.
	 * @param input String with tabs
	 * @return String with the tabs removed
	 */
	private String removeTabs(String input)
	{
		String result = "";
		for (int i=0; i<input.length(); i++)
		{
		if (input.charAt(i) == '\t')
			result = result + " ";
		else
			result = result + input.charAt(i);			
		}		
		return result;
	}
	
	
	
}
