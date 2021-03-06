<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text" version="1.0" encoding="UTF-8" indent="yes"/>

<xsl:template match="Instructions">

	package yams.processor;
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
			// TODO - implement MIPS exception and interrupt handling (an extension)
			
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
				 *  regs (RegisterManagerInterface)
				 *  mem (MemoryManagerInterface)
				 */

				// R TYPE INSTRUCTIONS FOLLOW
					 
				<xsl:apply-templates select="Instruction">
					<xsl:with-param name="inst_type" select="'R'"/>
				</xsl:apply-templates>

				// unsupported instruction
				throw new YAMSUnsupportedInstructionException("Instruction is R type with Op field: " + op + ", Func field: " + func);	
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
			
			/* 
			 * Available to the XML Java code programmer:
			 * 
			 *   For I types
			 *   -----------
			 *   rs (int)						first source register
			 *   rs_bitstr (MIPSBitstring)
			 * 
			 *   rt (int)						second source register
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
			 *   For both types
			 *   --------------
			 *   regs (RegisterManagerInterface)
			 *	 mem (MemoryManagerInterface)
			 *
			 */
	
			// I &amp; J TYPE INSTRUCTIONS FOLLOW
		
			<xsl:apply-templates select="Instruction">
				<xsl:with-param name="inst_type" select="'IJ'"/>
			</xsl:apply-templates>
			
			// unsupported instruction
			throw new YAMSUnsupportedInstructionException("Instruction is I/J type with Op field: " + op);
		}

		public void setCycleManager(CycleManagerInterface cycleManager) {
		}
	
		public void setVerbose(PrintStream verbose) {
			this.verbose = verbose;
		}
		
		private int toDecimal(String binarystr) {
			int total = 0;
			int weight = 1;
			for(int j = 0; j &lt; 6; j++) {
				if(binarystr.charAt(5 - j) == '1') {
					total += weight;
				}
				weight *= 2;
			}
			return total;
		}
		
	}
		
</xsl:template>


<xsl:template match="Instruction">
	<xsl:param name="inst_type"/>
	<xsl:variable name="regular" select='"Regular"'/>
	<xsl:variable name="extended" select='"Extended"'/>
	
	<xsl:if test="$inst_type = 'R'">
		<xsl:variable name="type" select="Type"/>
		<xsl:if test="$type = $regular">
			<xsl:apply-templates select="MachineCodeRepresentations">
				<xsl:with-param name="inst_type" select="$inst_type"/>
			</xsl:apply-templates>
		</xsl:if>
		<xsl:if test="$type = $extended">
			<xsl:variable name="coreMachineCode" select="CoreMachineCode"/>
			<xsl:variable name="allzeros" select="'000000'"/>
			<xsl:variable name="op" select="substring($coreMachineCode, 1, 6)"/>
			<xsl:variable name="funct" select="substring($coreMachineCode, 27, 6)"/>
			<xsl:if test="$op = $allzeros">
					if(func == toDecimal("<xsl:value-of select="$funct"/>")) {
						verbose.println("<xsl:value-of select="Name"/>");
						stats.instructionUsed("<xsl:value-of select="Name"/>");
						<xsl:value-of select="Javacode"/>
						return true;
					}
			</xsl:if>
		</xsl:if>
	</xsl:if>
	
	<xsl:if test="$inst_type = 'IJ'">
		<xsl:variable name="type" select="Type"/>
		<xsl:if test="$type = $regular">
			<xsl:apply-templates select="MachineCodeRepresentations">
				<xsl:with-param name="inst_type" select="$inst_type"/>
			</xsl:apply-templates>
		</xsl:if>
		<xsl:if test="$type = $extended">
			<xsl:variable name="coreMachineCode" select="CoreMachineCode"/>
			<xsl:variable name="allzeros" select="'000000'"/>
			<xsl:variable name="op" select="substring($coreMachineCode, 1, 6)"/>
			<xsl:if test="$op != $allzeros">
			if(op == toDecimal("<xsl:value-of select="$op"/>")) {
				verbose.println("<xsl:value-of select="Name"/>");
				stats.instructionUsed("<xsl:value-of select="Name"/>");
				<xsl:value-of select="Javacode"/>
				return true;
			}
			</xsl:if>
		</xsl:if>
			
	</xsl:if>

</xsl:template>


<xsl:template match="MachineCodeRepresentations">
	<xsl:param name="inst_type"/>
	<xsl:variable name="machineCode" select="Representation/MachineCode"/>
	<xsl:variable name="allzeros" select="'000000'"/>
	<xsl:variable name="syscall_funct" select="'001100'"/>
	<xsl:variable name="op" select="substring($machineCode, 1, 6)"/>
	<xsl:variable name="funct" select="substring($machineCode, 27, 6)"/>
	<xsl:if test="$inst_type = 'R'">
		<xsl:if test="$op = $allzeros">
			<xsl:choose>
				<xsl:when test="$funct = $syscall_funct">
					if(func == toDecimal("<xsl:value-of select="$funct"/>")) {
						// syscall placed in SyscallHandler	
						return false;
					}
				</xsl:when>
				<xsl:otherwise>
					if(func == toDecimal("<xsl:value-of select="$funct"/>")) {
						verbose.println("<xsl:value-of select="../Name"/>");
						stats.instructionUsed("<xsl:value-of select="../Name"/>");
						<xsl:value-of select="../Javacode"/>
						return true;
					}
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:if>
	<xsl:if test="$inst_type = 'IJ'">
		<xsl:if test="$op != $allzeros">
			if(op == toDecimal("<xsl:value-of select="$op"/>")) {
				verbose.println("<xsl:value-of select="../Name"/>");
				stats.instructionUsed("<xsl:value-of select="../Name"/>");
				<xsl:value-of select="../Javacode"/>
				return true;
			}
		</xsl:if>
	</xsl:if>
</xsl:template>


</xsl:stylesheet>

