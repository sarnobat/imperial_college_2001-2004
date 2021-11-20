<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text" version="1.0" encoding="UTF-8" indent="yes"/>

<xsl:template match="Instructions">

	package yams.processor;
	import java.io.BufferedReader;
	import java.io.InputStream;
	import java.io.InputStreamReader;
	import java.io.IOException;
	import java.io.PrintStream;
	import yams.exceptions.YAMSRuntimeException;
	import yams.exceptions.YAMSUnsupportedSyscallException;

	/**
	 * Auto generated from the XML instruction file.
	 */
	public class SyscallHandler implements InstructionHandlerInterface {
	
		private RegisterManagerInterface regs;
		private MemoryManagerInterface mem;
		private StatisticsManagerInterface stats;
		private CycleManagerInterface cycleManager;
		private InputStream in;
		private PrintStream out;
		private PrintStream verbose;
		
		/**
		 * Create a new syscall handler.
		 * @param registerManager
		 * @param memoryManager
		 * @param cycleManager
		 */
		public SyscallHandler(RegisterManagerInterface registerManager,
									MemoryManagerInterface memoryManager,
									StatisticsManagerInterface statisticsManager,
									InputStream in,
									PrintStream out,
									PrintStream verbose) {
			regs = registerManager;
			mem = memoryManager;
			stats = statisticsManager;
			this.in = in;
			this.out = out;
			this.verbose = verbose;
		}
	
		public boolean execute(int instruction) {
			// register $v0 holds the system call code
			
			/*
			Service 		Code		Arguments 						Result
			
			print_int 			01 		$a0 = integer
			print_float 		02 		$f12 = float
			print_double 	03 		$f12 = double
			print_string 		04 		$a0 = string
			read_int 			05 											integer (in $v0)
			read_float 		06 											float (in $f0)
			read_double 	07 											double (in $f0)
			read_string 		08		$a0 = buffer, $a1 = length
			sbrk 				09		$a0 = amount 				address (in $v0)
			exit 					10
			*/		
			
			stats.addInstruction("SYSCALL");

			// SYSCALL BODY FOLLOWS
					 
				<xsl:apply-templates select="Instruction"/>

			return true;
		}

		public void setCycleManager(CycleManagerInterface cycleManager) {
			this.cycleManager = cycleManager;
		}
	
		public void setVerbose(PrintStream verbose) {
			this.verbose = verbose;
		}
	
	}



		
</xsl:template>


<xsl:template match="Instruction">
	<xsl:variable name="regular" select='"Regular"'/>
	<xsl:variable name="type" select="Type"/>
	<xsl:if test="$type = $regular">
		<xsl:apply-templates select="MachineCodeRepresentations"/>
	</xsl:if>
</xsl:template>


<xsl:template match="MachineCodeRepresentations">
	<xsl:variable name="machineCode" select="Representation/MachineCode"/>
	<xsl:variable name="allzeros" select="'000000'"/>
	<xsl:variable name="syscall_funct" select="'001100'"/>
	<xsl:variable name="op" select="substring($machineCode, 1, 6)"/>
	<xsl:variable name="funct" select="substring($machineCode, 27, 6)"/>
	<xsl:if test="$op = $allzeros">
		<xsl:if test="$funct = $syscall_funct">
				<xsl:value-of select="../Javacode"/>
		</xsl:if>
	</xsl:if>
</xsl:template>


</xsl:stylesheet>

