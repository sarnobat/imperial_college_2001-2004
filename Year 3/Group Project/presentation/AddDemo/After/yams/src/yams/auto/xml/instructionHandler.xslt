<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text" version="1.0" encoding="UTF-8" indent="yes"/>

<xsl:template match="Root">
	package yams.auto;
	import java.io.PrintStream;
	import yams.exceptions.YAMSUnsupportedInstructionException;

	/**
	 * Auto generated from the XML instruction file.
	 */
	public class InstructionHandler implements InstructionHandlerInterface {
	
		private RegisterManagerInterface regs;
		private MemoryManagerInterface mem;
		private StatisticsManagerInterface stats;
		private PrintStream out;
		private PrintStream verbose;
		
		/**
		 * Create a new instruction handler.
		 * @param registerManager
		 * @param memoryManager
		 */
		public InstructionHandler(RegisterManagerInterface registerManager,
									MemoryManagerInterface memoryManager,
									StatisticsManagerInterface statisticsManager,
									PrintStream out,
									PrintStream verbose) {
			regs = registerManager;
			mem = memoryManager;
			stats = statisticsManager;
			this.out = out;
			this.verbose = verbose;
		}
	
		public boolean execute(int instruction) {
			MIPSWord inst = new MIPSWord(instruction);
			verbose.println("Next: " + inst.toString());
			// get 'op' field to determine instruction
			MIPSBitstring op_bitstr = MIPSBitstring.extract(inst, 26, 31);
			int op = op_bitstr.toInt();
			
			// TODO - add coprocessor and floating point instructions (an extension)
			// TODO - detect MIPS exceptions e.g. arithmetic overflow (an extension)
			// TODO - implement MIPS exception & interrupt handling (an extension)
			
			if(op == 0) {
				// we're executing an R type instruction
				
				// extract R type instruction operands
				MIPSBitstring rs_bitstr = MIPSBitstring.extract(inst, 21, 25);
				MIPSBitstring rt_bitstr = MIPSBitstring.extract(inst, 16, 20);
				MIPSBitstring rd_bitstr = MIPSBitstring.extract(inst, 11, 15);
				MIPSBitstring shamt_bitstr = MIPSBitstring.extract(inst, 6, 10);
				MIPSBitstring func_bitstr = MIPSBitstring.extract(inst, 0, 5);
				int rs = rs_bitstr.toInt();
				int rt = rt_bitstr.toInt();
				int rd = rd_bitstr.toInt();
				int shamt = shamt_bitstr.toInt();
				int func = func_bitstr.toInt();
				
				switch(func) {
					/* 
					 * Available to the XML Java code programmer:
					 * 
					 *   For R types
					 *   -----------
					 *   rs (int)						first source register
					 *   rs_bitstr (MIPSBitstring)
					 * 
					 *   rt (int)						second source register
					 *   rt_bitstr (MIPSBitstring)
					 * 
					 *   rd (int)						destination register
					 *   rd_bitstr (MIPSBitstring)
					 * 
					 *   shamt (int)					shift amount
					 *   shamt_bitstr (MIPSBitstring)
					 *  
					 *	 regs (RegisterManagerInterface)
					 *	 mem (MemoryManagerInterface)
					 */

					// R TYPES HERE	
					<xsl:apply-templates select="R_Instructions"/>
																		
					default: {
						// unsupported instruction
						throw new YAMSUnsupportedInstructionException();	
					}
				}
				return true;
			}
	
			// extract I type instruction operands
			MIPSBitstring rs_bitstr = MIPSBitstring.extract(inst, 21, 25);
			MIPSBitstring rt_bitstr = MIPSBitstring.extract(inst, 16, 20);
			MIPSBitstring i_bitstr = MIPSBitstring.extract(inst, 0, 15);
			int rs = rs_bitstr.toInt();
			int rt = rt_bitstr.toInt();
			int i = i_bitstr.signExtend();
			
			// extract J type instruction operand
			MIPSBitstring addr_bitstr = MIPSBitstring.extract(inst, 0, 25);
			int addr = addr_bitstr.signExtend();
	
			switch(op) { 
				/* 
				* Available to the XML Java code programmer:
				 * 
				 *   For I types
				 *   -----------
				 *   rs (int)						first source register
				 *   rs_bitstr (MIPSBitstring)
				 * 
				 * 	 rt (int)						second source register
				 *   rt_bitstr (MIPSBitstring)
				 * 
				 *   i (int)						immediate value
				 *   i_bitstr (MIPSBitstring)
				 * 
				 *   For J types
				 *   -----------
				 *   addr (int)						address
				 *   addr_bitstr (MIPSBitstring)
				 *  
				 * 	 For both types
				 *   --------------
				 *	 regs (RegisterManagerInterface)
				 *	 mem (MemoryManagerInterface)
				 *
				 */
					
				// I & J type instructions
				
				// I & J TYPES HERE		
				<xsl:apply-templates select="IJ_Instructions"/>
				
				default: {
					// unsupported instruction
					throw new YAMSUnsupportedInstructionException();
				}		
			}		
			return true;
		}
	
		public void setCycleManager(CycleManagerInterface cycleManager) {
		}
	
	}
</xsl:template>


<xsl:template match="Instructions">
	in Instructions
	<xsl:apply-templates select="Instruction"/>
</xsl:template>

<xsl:template match="Instruction">
<xsl:variable name="actualType" select="Type"/>
<xsl:variable name="regular" select="'Regular'"/>
<xsl:variable name="extended" select="'Extended'"/>
<xsl:variable name="machineCode" select="MachineCode"/>
<xsl:variable name="coreMachineCode" select="CoreMachineCode"/>
	<xsl:if test="${Type} = ${regular}">
	
		<xsl:value-of select="substring(${machineCode}, 1, 6)">
	</xsl:if>



/*
// first 2 ifs should be in R_Instructions

if((Type == Regular) && (substring(MachineCode, 1, 6) == "000000"))

	case number(substring(MachineCode, 27, 6) : {
		verbose.println(Name);
		stats.instructionUsed(Name);
		Javacode
		break;
	}

if((Type == Extended) && (substring(CoreMachineCode, 1, 6) == "000000"))

	case number(substring(CoreMachineCode, 27, 6) : {
		verbose.println(Name);
		stats.instructionUsed(Name);
		Javacode
		break;
	}

// these 2 ifs should be in IJ_Instructions

if((Type == Regular) && (substring(MachineCode, 1, 6) != "000000"))

	case number(substring(MachineCode, 1, 6) : {
		verbose.println(Name);
		stats.instructionUsed(Name);
		Javacode
		break;
	}

if((Type == Extended) && (substring(CoreMachineCode, 1, 6) != "000000"))

	case number(substring(CoreMachineCode, 1, 6) : {
		verbose.println(Name);
		stats.instructionUsed(Name);
		Javacode
		break;
	}
*/

</xsl:template>

</xsl:stylesheet>
