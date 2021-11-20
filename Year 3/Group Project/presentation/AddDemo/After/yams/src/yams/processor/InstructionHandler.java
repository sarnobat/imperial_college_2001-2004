

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
					 
				
					if(func == toDecimal("100000")) {
						verbose.println("add");
						stats.instructionUsed("add");
						
		regs.setReg(rd, regs.getReg(rs) + regs.getReg(rt));
		
						return true;
					}
			
					if(func == toDecimal("100001")) {
						verbose.println("addu");
						stats.instructionUsed("addu");
						
		regs.setReg(rd, regs.getReg(rs) + regs.getReg(rt));
		
						return true;
					}
				
					if(func == toDecimal("101010")) {
						verbose.println("slt");
						stats.instructionUsed("slt");
						
		if(regs.getReg(rs) < regs.getReg(rt)) {
			regs.setReg(rd, 1);
		}
		else {
			regs.setReg(rd, 0);
		}
		
						return true;
					}
				
					if(func == toDecimal("100010")) {
						verbose.println("sub");
						stats.instructionUsed("sub");
						
		regs.setReg(rd, regs.getReg(rs) - regs.getReg(rt));
		
						return true;
					}
			
					if(func == toDecimal("011000")) {
						verbose.println("mult");
						stats.instructionUsed("mult");
						
		long left = regs.getReg(rs);
		long right = regs.getReg(rt);
		long product = left * right;
		int lo = (int) (product & 0xffffffff);
		int hi = (int) (product >>> 32);
		regs.setReg("LO", lo);
		regs.setReg("HI", hi);
		
						return true;
					}
				
					if(func == toDecimal("011010")) {
						verbose.println("div");
						stats.instructionUsed("div");
						
		int quotient;
		int remainder;
		int rscontents = regs.getReg(rs);
		int rtcontents = regs.getReg(rt);
		quotient = rscontents / rtcontents;
		remainder = rscontents % rtcontents;
		regs.setReg("LO", quotient);
		regs.setReg("HI", remainder);
		
						return true;
					}
			
					if(func == toDecimal("001100")) {
						// syscall placed in SyscallHandler	
						return false;
					}
				
					if(func == toDecimal("010010")) {
						verbose.println("mflo");
						stats.instructionUsed("mflo");
						
		regs.setReg(rd, regs.getReg("LO"));
		
						return true;
					}
				

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
	
			// I & J TYPE INSTRUCTIONS FOLLOW
		
			
			if(op == toDecimal("001000")) {
				verbose.println("addi");
				stats.instructionUsed("addi");
				
		regs.setReg(rt, regs.getReg(rs) + i);
		
				return true;
			}
		
			if(op == toDecimal("001111")) {
				verbose.println("lui");
				stats.instructionUsed("lui");
				
		MIPSBitstring zerohalfword;
		MIPSBitstring result;
		zerohalfword = MIPSBitstring.repeat(0, 16);
		result = MIPSBitstring.concatenate(i_bitstr, zerohalfword);
		regs.setReg(rt, result.toInt());
		
				return true;
			}
		
			if(op == toDecimal("000100")) {
				verbose.println("beq");
				stats.instructionUsed("beq");
				
		if(regs.getReg(rs) == regs.getReg(rt)) {
			int pc = 0;
			pc = regs.getReg("PC");
			pc += i * 4 - 4;
			regs.setReg("PC", pc);						
		}
		
				return true;
			}
			
			if(op == toDecimal("000001")) {
				verbose.println("bgez");
				stats.instructionUsed("bgez");
				
		if(regs.getReg(rs) >= 0) {
			int pc = 0;
			pc = regs.getReg("PC");
			pc += i * 4 - 4;
			regs.setReg("PC", pc);						
		}		
		
				return true;
			}
		
			if(op == toDecimal("000111")) {
				verbose.println("bgtz");
				stats.instructionUsed("bgtz");
				
		if(regs.getReg(rs) > 0) {
			int pc = 0;
			pc = regs.getReg("PC");
			pc += i * 4 - 4;
			regs.setReg("PC", pc);						
		}
		
				return true;
			}
		
			if(op == toDecimal("000110")) {
				verbose.println("blez");
				stats.instructionUsed("blez");
				
		if(regs.getReg(rs) <= 0) {
			int pc = 0;
			pc = regs.getReg("PC");
			pc += i * 4 - 4;
			regs.setReg("PC", pc);						
		}
		
				return true;
			}
		
			if(op == toDecimal("000001")) {
				verbose.println("bltz");
				stats.instructionUsed("bltz");
				
		if(regs.getReg(rs) < 0) {
			int pc = 0;
			pc = regs.getReg("PC");
			pc += i * 4 - 4;
			regs.setReg("PC", pc);						
		}
		
				return true;
			}
		
			if(op == toDecimal("000101")) {
				verbose.println("bne");
				stats.instructionUsed("bne");
				
		if(regs.getReg(rs) != regs.getReg(rt)) {
			int pc = 0;
			pc = regs.getReg("PC");
			pc += i * 4 - 4;
			regs.setReg("PC", pc);						
		}
		
				return true;
			}
		
			if(op == toDecimal("000010")) {
				verbose.println("j");
				stats.instructionUsed("j");
				
		int pc = 0;
		pc = regs.getReg("PC");				 
		MIPSWord pcword;
		MIPSBitstring higherpcbits;
		MIPSBitstring newpc;
		pcword = new MIPSWord(pc);
		higherpcbits = MIPSBitstring.extract(pcword, 28, 31);
		newpc = MIPSBitstring.concatenate(higherpcbits, addr_bitstr);
		newpc = MIPSBitstring.concatenate(newpc, MIPSBitstring.repeat(0, 2));
		regs.setReg("PC", pc);
		
				return true;
			}
		
			if(op == toDecimal("000011")) {
				verbose.println("jal");
				stats.instructionUsed("jal");
				
		int pc = 0;
		pc = regs.getReg("PC");
		regs.setReg(31, pc + 4);				 
		MIPSWord pcword;
		MIPSBitstring higherpcbits;
		MIPSBitstring newpc;
		pcword = new MIPSWord(pc);
		higherpcbits = MIPSBitstring.extract(pcword, 28, 31);
		newpc = MIPSBitstring.concatenate(higherpcbits, addr_bitstr);
		newpc = MIPSBitstring.concatenate(newpc, MIPSBitstring.repeat(0, 2));
		regs.setReg("PC", pc);
		
				return true;
			}
		
			if(op == toDecimal("100011")) {
				verbose.println("lw");
				stats.instructionUsed("lw");
				
		int memorylocation = regs.getReg(rs) + i;
		regs.setReg(rt, mem.getLocation(memorylocation));
		
				return true;
			}
			
			if(op == toDecimal("101011")) {
				verbose.println("sw");
				stats.instructionUsed("sw");
				
		int memorylocation = regs.getReg(rs) + i;
		mem.setLocation(memorylocation, regs.getReg(rt));
		
				return true;
			}
			
			if(op == toDecimal("001101")) {
				verbose.println("ori");
				stats.instructionUsed("ori");
				
		regs.setReg(rt, regs.getReg(rs) | i_bitstr.toInt());
		
				return true;
			}
		
			
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
			for(int j = 0; j < 6; j++) {
				if(binarystr.charAt(5 - j) == '1') {
					total += weight;
				}
				weight *= 2;
			}
			return total;
		}
		
	}
		
