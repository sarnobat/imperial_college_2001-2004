/*
 * Created on 03-Nov-2003
 *
 */
package yams.processor;
import java.io.PrintStream;
import yams.exceptions.YAMSUnsupportedInstructionException;

/**
 * A template instruction handler.
 * The instructions will be added to this file by the instruction handler
 * generator, taking the XML file as input.
 * N.B. R type instructions should be added as cases to the switch on 'func', and
 * I & J type instructions should be added as cases to the switch on 'op'.
 *  
 * @author sw00
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
					
				case 0x00: {
					if((rs == 0) && (rt == 0) && (rd == 0) && (shamt == 0)) {
						verbose.println("NOP");
						stats.instructionUsed("NOP");
						// no operation
					}
					else {
						verbose.println("SLL");
						stats.instructionUsed("SLL");
						// shift left logical
						int shifted;
						shifted = regs.getReg(rt) << shamt;
						regs.setReg(rd, shifted);
					}
					break;
				}
				
				case 0x02: {
					verbose.println("SRL");
					stats.instructionUsed("SRL");
					// shift right logical 
					MIPSBitstring higherbits;
					MIPSBitstring shiftedbits;
					MIPSBitstring output;
					if(shamt == 0) {
						output = rt_bitstr;	
					}
					else {
						higherbits = MIPSBitstring.repeat(0, shamt);
						shiftedbits = rt_bitstr.extract(shamt, 31);
						output = MIPSBitstring.concatenate(higherbits, shiftedbits);
					}
					regs.setReg(rd, output.toInt());
					break;
				}
				
				case 0x03: {
					verbose.println("SRA");
					stats.instructionUsed("SRA");
					// shift right arithmetic 
					MIPSBitstring higherbits;
					MIPSBitstring shiftedbits;
					MIPSBitstring output;
					if(shamt == 0) {
						output = rt_bitstr;	
					}
					else {
						higherbits = MIPSBitstring.repeat(rt_bitstr.getBit(31), shamt);
						shiftedbits = rt_bitstr.extract(shamt, 31);
						output = MIPSBitstring.concatenate(higherbits, shiftedbits);
					}
					regs.setReg(rd, output.toInt());
					break;
				}
								
				case 0x04: {
					verbose.println("SLLV");
					stats.instructionUsed("SLLV");
					// shift left logical variable
					MIPSBitstring rs_shamt;
					int shiftamount;
					int shifted;
					shiftamount = rs_bitstr.extract(0, 4).toInt();
					shifted = regs.getReg(rt) << shiftamount;
					regs.setReg(rd, shifted);
					break;
				}
								
				case 0x06: {
					verbose.println("SRLV");
					stats.instructionUsed("SRLV");
					// shift right logical variable
					MIPSBitstring higherbits;
					MIPSBitstring shiftedbits;
					MIPSBitstring output;
					int shiftamount;
					shiftamount = rs_bitstr.extract(0, 4).toInt();
					if(shiftamount == 0) {
						output = rt_bitstr;	
					}
					else {
						higherbits = MIPSBitstring.repeat(0, shiftamount);
						shiftedbits = rt_bitstr.extract(shiftamount, 31);
						output = MIPSBitstring.concatenate(higherbits, shiftedbits);
					}
					regs.setReg(rd, output.toInt());
					break;
				}
								
				case 0x07: {
					verbose.println("SRAV");
					stats.instructionUsed("SRAV");
					// shift right arithmetic variable
					MIPSBitstring higherbits;
					MIPSBitstring shiftedbits;
					MIPSBitstring output;
					int shiftamount;
					shiftamount = rs_bitstr.extract(0, 4).toInt();
					if(shiftamount == 0) {
						output = rt_bitstr;	
					}
					else {
						higherbits = MIPSBitstring.repeat(rt_bitstr.getBit(31), shiftamount);
						shiftedbits = rt_bitstr.extract(shiftamount, 31);
						output = MIPSBitstring.concatenate(higherbits, shiftedbits);
					}
					regs.setReg(rd, output.toInt());
					break;
				}
								
				case 0x08: {
					verbose.println("JR");
					stats.instructionUsed("JR");
					// jump register
					regs.setReg("PC", regs.getReg(rs)); 
					break;
				}
								
				case 0x09: {
					verbose.println("JALR");
					stats.instructionUsed("JALR");
					// jump and link register
					regs.setReg(31, regs.getReg("PC") + 4);
					regs.setReg("PC", regs.getReg(rs)); 
					break;
				}
								
				case 0x0c: {
					verbose.println("SYSCALL");
					stats.instructionUsed("SYSCALL");
					// system call
					// we shouldn't ever be passed a syscall instruction
					// (that's for the syscall handler)
					// the instruction decoder has done its job wrong
					throw new YAMSUnsupportedInstructionException();
				}
								
				case 0x0d: {
					verbose.println("BREAK");
					stats.instructionUsed("BREAK");
					// TODO - BREAK (an extension)
					break;
				}
								
				case 0x10: {
					verbose.println("MFHI");
					stats.instructionUsed("MFHI");
					// move from HI
					regs.setReg(rd, regs.getReg("HI"));
					break;
				}
								
				case 0x11: {
					verbose.println("MTHI");
					stats.instructionUsed("MTHI");
					// move to HI
					regs.setReg("HI", regs.getReg(rs));
					break;
				}
								
				case 0x12: {
					verbose.println("MFLO");
					stats.instructionUsed("MFLO");
					// move from LO
					regs.setReg(rd, regs.getReg("LO"));
					break;
				}
				
				case 0x13: {
					verbose.println("MTLO");
					stats.instructionUsed("MTLO");
					// move to LO
					regs.setReg("LO", regs.getReg(rs));
					break;
				}
				
				case 0x18: {
					verbose.println("MULT");
					stats.instructionUsed("MULT");
					// multiply
					long left = regs.getReg(rs);
					long right = regs.getReg(rt);
					long product = left * right;
					int lo = (int) (product & 0xffffffff);
					int hi = (int) (product >>> 32);
					regs.setReg("LO", lo);
					regs.setReg("HI", hi);
					break;
				}
				
				case 0x19: {
					verbose.println("MULTU");
					stats.instructionUsed("MULTU");
					// multiply unsigned
					long left = regs.getReg(rs);
					long right = regs.getReg(rt);
					long product = left * right;
					int lo = (int) (product & 0xffffffff);
					int hi = (int) (product >>> 32);
					regs.setReg("LO", lo);
					regs.setReg("HI", hi);
					break;
				}
				
				case 0x1a: {
					verbose.println("DIV");
					stats.instructionUsed("DIV");
					// divide
					int quotient;
					int remainder;
					int rscontents = regs.getReg(rs);
					int rtcontents = regs.getReg(rt);
					quotient = rscontents / rtcontents;
					remainder = rscontents % rtcontents;
					regs.setReg("LO", quotient);
					regs.setReg("HI", remainder);
					break;
				}
				
				case 0x1b: {
					verbose.println("DIVU");
					stats.instructionUsed("DIVU");
					// divide unsigned
					int quotient;
					int remainder;
					int rscontents = regs.getReg(rs);
					int rtcontents = regs.getReg(rt);
					quotient = rscontents / rtcontents;
					remainder = rscontents % rtcontents;
					regs.setReg("LO", quotient);
					regs.setReg("HI", remainder);
					break;
				}
				
				case 0x20: {
					verbose.println("ADD");
					stats.instructionUsed("ADD");
					// add
					regs.setReg(rd, regs.getReg(rs) + regs.getReg(rt));
					break;
				}

				case 0x21: {
					verbose.println("ADDU");
					stats.instructionUsed("ADDU");
					// addu
					regs.setReg(rd, regs.getReg(rs) + regs.getReg(rt));
					break;
				}
				
				case 0x22: {
					verbose.println("SUB");
					stats.instructionUsed("SUB");
					// sub
					regs.setReg(rd, regs.getReg(rs) - regs.getReg(rt));
					break;
				}

				case 0x23: {
					verbose.println("SUBU");
					stats.instructionUsed("SUBU");
					// subu
					regs.setReg(rd, regs.getReg(rs) - regs.getReg(rt));
					break;
				}
				
				case 0x24: {
					verbose.println("AND");
					stats.instructionUsed("AND");
					// and
					regs.setReg(rd, regs.getReg(rs) & regs.getReg(rt));
					break;
				}
				
				case 0x25: {
					verbose.println("OR");
					stats.instructionUsed("OR");
					// or
					regs.setReg(rd, regs.getReg(rs) | regs.getReg(rt));
					break;
				}
				
				case 0x26: {
					verbose.println("XOR");
					stats.instructionUsed("XOR");
					// xor
					regs.setReg(rd, regs.getReg(rs) ^ regs.getReg(rt));
					break;
				}
				
				case 0x27: {
					verbose.println("NOR");
					stats.instructionUsed("NOR");
					// nor
					regs.setReg(rd, ~(regs.getReg(rs) | regs.getReg(rt)));
					break;
				}
				
				case 0x2a: {
					verbose.println("SLT");
					stats.instructionUsed("SLT");
					// set less than
					if(regs.getReg(rs) < regs.getReg(rt)) {
						regs.setReg(rd, 1);
					}
					else {
						regs.setReg(rd, 0);
					}
					break;
				}
				
				case 0x2b: {
					verbose.println("SLTU");
					stats.instructionUsed("SLTU");
					// set less than unsigned
					if(regs.getReg(rs) < regs.getReg(rt)) {
						regs.setReg(rd, 1);
					}
					else {
						regs.setReg(rd, 0);
					}
					break;
				}
								
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
				
			// I type instructions
			
			case 0x01: {
				if(rt == 0x00) {
					verbose.println("BLTZ");
					stats.instructionUsed("BLTZ");
					// branch on less than zero [rs, offset]
					if(regs.getReg(rs) < 0) {
						int pc = 0;
						pc = regs.getReg("PC");
						pc += i * 4 - 4;
						regs.setReg("PC", pc);						
					}
				}
				else if(rt == 0x01) {
					verbose.println("BGEZ");
					stats.instructionUsed("BGEZ");	
					// branch on greater than equal zero [rs, offset]
					if(regs.getReg(rs) >= 0) {
						int pc = 0;
						pc = regs.getReg("PC");
						pc += i * 4 - 4;
						regs.setReg("PC", pc);						
					}		
				}
				else if(rt == 0x10) {
					verbose.println("BLTZAL");
					stats.instructionUsed("BLTZAL");
					// branch on less than zero and link
					if(regs.getReg(rs) < 0) {
						int pc = 0;
						pc = regs.getReg("PC");
						regs.setReg(31, pc + 4);	
						pc += i * 4 - 4;
						regs.setReg("PC", pc);
					}
				}
				else if(rt == 0x11) {
					verbose.println("BGEZAL");
					stats.instructionUsed("BGEZAL");
					// branch on greater than equal zero and link
					if(regs.getReg(rs) >= 0) {
						int pc = 0;
						pc = regs.getReg("PC");
						regs.setReg(31, pc + 4);	
						pc += i * 4 - 4;
						regs.setReg("PC", pc);	
					}
				}
				else {
					throw new YAMSUnsupportedInstructionException();
				}
				break;
			}
			
			case 0x04: {
				verbose.println("BEQ");
				stats.instructionUsed("BEQ");
				//branch on equal
				if(regs.getReg(rs) == regs.getReg(rt)) {
					 int pc = 0;
					pc = regs.getReg("PC");
					 pc += i * 4 - 4;
					regs.setReg("PC", pc);						
				}
				break;
			}
			
			case 0x05: {
				verbose.println("BNE");
				stats.instructionUsed("BNE");
				//branch on not equal
				if(regs.getReg(rs) != regs.getReg(rt)) {
					 int pc = 0;
					pc = regs.getReg("PC");
					 pc += i * 4 - 4;
					regs.setReg("PC", pc);						
				}
				break;
			}
			
			case 0x06: {
				verbose.println("BLEZ");
				stats.instructionUsed("BLEZ");
				// branch on less than equal zero [rs, offset]
				if(regs.getReg(rs) <= 0) {
					int pc = 0;
					pc = regs.getReg("PC");
					pc += i * 4 - 4;
					regs.setReg("PC", pc);						
				}
				break;
			}
			
			case 0x07: {
				verbose.println("BGTZ");
				stats.instructionUsed("BGTZ");
				// branch on greater than zero [rs, offset]
				if(regs.getReg(rs) > 0) {
						int pc = 0;
						pc = regs.getReg("PC");
						pc += i * 4 - 4;
						regs.setReg("PC", pc);						
					}
				break;
			}
			
			case 0x08: {
				verbose.println("ADDI");
				stats.instructionUsed("ADDI");
				// add immediate
				regs.setReg(rt, regs.getReg(rs) + i);
				break;
			}
			
			case 0x09: {
				verbose.println("ADDIU");
				stats.instructionUsed("ADDIU");
				// add immediate unsigned
				regs.setReg(rt, regs.getReg(rs) + i);
				break;
			}
			
			case 0x0a: {
				verbose.println("SLTI");
				stats.instructionUsed("SLTI");
				// set less than immediate
				if(regs.getReg(rs) < i) {
					regs.setReg(rt, 1);
				}
				else {
					regs.setReg(rt, 0);
				}
				break;
			}
			
			case 0x0b: {
				verbose.println("SLTIU");
				stats.instructionUsed("SLTIU");
				// set less than immediate
				if(regs.getReg(rs) < i) {
					regs.setReg(rt, 1);
				}
				else {
					regs.setReg(rt, 0);
				}
				break;
			}
			
			case 0x0c: {
				verbose.println("ANDI");
				stats.instructionUsed("ANDI");
				// and immediate
				regs.setReg(rt, regs.getReg(rs) & i);
				break;
			}				
						
			case 0x0d: {
				verbose.println("ORI");
				stats.instructionUsed("ORI");
				// or immediate
				regs.setReg(rt, regs.getReg(rs) | i_bitstr.toInt());
				break;
			}
			
			case 0x0e: {
				verbose.println("XORI");
				stats.instructionUsed("XORI");
				// xor immediate
				regs.setReg(rt, regs.getReg(rs) ^ i);
				break;
			}
			
			case 0x0f: {
				verbose.println("LUI");
				stats.instructionUsed("LUI");
				// load upper immediate
				MIPSBitstring zerohalfword;
				MIPSBitstring result;
				zerohalfword = MIPSBitstring.repeat(0, 16);
				result = MIPSBitstring.concatenate(i_bitstr, zerohalfword);
				regs.setReg(rt, result.toInt());
				break;
			}

			case 0x20: {
				verbose.println("LB");
				stats.instructionUsed("LB");
				// load byte
				int bytelocation = regs.getReg(rs) + i;
				int bytevalue = mem.getByte(bytelocation);
				// now sign extend the byte to a word
				MIPSWord result = new MIPSWord(bytevalue);
				int signbit = result.getBit(7);
				for(int j = 8; j < 32; j++) {
					result.setBit(j, signbit);
				}
				regs.setReg(rt, result.get());
				break;
			}
			
			case 0x21: {
				verbose.println("LH");
				stats.instructionUsed("LH");
				// load halfword
				int hwlocation = regs.getReg(rs) + i;
				int hwvalue = (mem.getByte(hwlocation) << 8) + mem.getByte(hwlocation + 1);
				// now sign extend the halfword to a word
				MIPSWord result = new MIPSWord(hwvalue);
				int signbit = result.getBit(15);
				for(int j = 16; j < 32; j++) {
					result.setBit(j, signbit);
				}
				regs.setReg(rt, result.get());
				break;
			}
			
			case 0x22: {
				verbose.println("LWL");
				stats.instructionUsed("LWL");
				// load word left
				/*
				Sign-extend 16-bit offset and add to contents of register base to form 
				address. 
				Shift addressed word left so that addressed byte is leftmost 
				byte of a word. Merge bytes from memory with contents of register rt 
				and load result into register rt.  

				M is byte at given address
				Register			A			B			C			D
				Memory			W		X			Y			Z
				
				Addr(1..0)=0	M			M+1		M+2		M+3		(WXYZ)
				Addr(1..0)=1	M			M+1		M+2		D			(XYZD)
				Addr(1..0)=2	M			M+1		C			D			(YZCD)
				Addr(1..0)=3	M			B			C			D			(ZBCD)
				*/
				
				int memoryaddress = regs.getReg(rs) + i;
				MIPSWord register = new MIPSWord(regs.getReg(rt));
				MIPSBitstring result;
				MIPSBitstring byte3;
				MIPSBitstring byte2;
				MIPSBitstring byte1;
				MIPSBitstring byte0;
							
				switch(memoryaddress & 3) {
					case 0 : regs.setReg(rt, mem.getLocation(memoryaddress));
							break;
					case 1 : byte3 = new MIPSByte(mem.getByte(memoryaddress)).getBitstring();
							byte2 = new MIPSByte(mem.getByte(memoryaddress+1)).getBitstring();
							byte1 = new MIPSByte(mem.getByte(memoryaddress+2)).getBitstring();
							byte0 = MIPSBitstring.extract(register, 0, 7);
							result = MIPSBitstring.concatenate(
										MIPSBitstring.concatenate(byte3, byte2),
										MIPSBitstring.concatenate(byte1, byte0));
							regs.setReg(rt, result.toInt());
							break;
					case 2 : byte3 = new MIPSByte(mem.getByte(memoryaddress)).getBitstring();
							byte2 = new MIPSByte(mem.getByte(memoryaddress+1)).getBitstring();
							byte1 = MIPSBitstring.extract(register, 8, 15);
							byte0 = MIPSBitstring.extract(register, 0, 7);
							result = MIPSBitstring.concatenate(
										MIPSBitstring.concatenate(byte3, byte2),
										MIPSBitstring.concatenate(byte1, byte0));
							regs.setReg(rt, result.toInt());
							break;
					case 3 : byte3 = new MIPSByte(mem.getByte(memoryaddress)).getBitstring();
							byte2 = MIPSBitstring.extract(register, 16, 23);
							byte1 = MIPSBitstring.extract(register, 8, 15);
							byte0 = MIPSBitstring.extract(register, 0, 7);
							result = MIPSBitstring.concatenate(
										MIPSBitstring.concatenate(byte3, byte2),
										MIPSBitstring.concatenate(byte1, byte0));
							regs.setReg(rt, result.toInt());
							break;				
				}
				break;
			}
			
			case 0x23: {
				verbose.println("LW");
				stats.instructionUsed("LW");
				// load word
				int memorylocation = regs.getReg(rs) + i;
				regs.setReg(rt, mem.getLocation(memorylocation));
				break;
			}
			
			case 0x24: {
				verbose.println("LBU");
				stats.instructionUsed("LBU");
				// load byte unsigned
				int bytelocation = regs.getReg(rs) + i;
				int bytevalue = mem.getByte(bytelocation);
				regs.setReg(rt, bytevalue);
				break;
			}
			
			case 0x25: {
				verbose.println("LHU");
				stats.instructionUsed("LHU");
				// load halfword unsigned
				int hwlocation = regs.getReg(rs) + i;
				int hwvalue = (mem.getByte(hwlocation) << 8) + mem.getByte(hwlocation + 1);
				regs.setReg(rt, hwvalue);
				break;
			}
			
			case 0x26: {
				verbose.println("LWR");
				stats.instructionUsed("LWR");
				// load word right
				/*
				Sign-extend 16-bit offset and add to contents of register base to form
				address. 
				Shift addressed word right so that addressed byte is rightmost
				byte of a word. Merge bytes from memory with contents of register rt
				and load result into register rt. 
				
				M is byte at Addr
				Register			A			B			C			D
				Memory			W		X			Y			Z
				
				Addr(1..0)=0	A			B			C			M		(ABCW)
				Addr(1..0)=1	A			B			M-1		M		(ABWX)
				Addr(1..0)=2	A			M-2		M-1		M		(AWXY)
				Addr(1..0)=3	M-3		M-2		M-1		M		(WXYZ)
				*/
				
				int memoryaddress = regs.getReg(rs) + i;
				MIPSWord register = new MIPSWord(regs.getReg(rt));
				MIPSBitstring result;
				MIPSBitstring byte3;
				MIPSBitstring byte2;
				MIPSBitstring byte1;
				MIPSBitstring byte0;
							
				switch(memoryaddress & 3) {
					case 0 : byte3 = MIPSBitstring.extract(register, 24, 31);
							byte2 = MIPSBitstring.extract(register, 16, 23);
							byte1 = MIPSBitstring.extract(register, 8, 15);
							byte0 = new MIPSByte(mem.getByte(memoryaddress)).getBitstring();
							result = MIPSBitstring.concatenate(
										MIPSBitstring.concatenate(byte3, byte2),
										MIPSBitstring.concatenate(byte1, byte0));
							regs.setReg(rt, result.toInt());
							break;
					case 1 : byte3 = MIPSBitstring.extract(register, 24, 31);
							byte2 = MIPSBitstring.extract(register, 16, 23);
							byte1 = new MIPSByte(mem.getByte(memoryaddress-1)).getBitstring();
							byte0 = new MIPSByte(mem.getByte(memoryaddress)).getBitstring();
							result = MIPSBitstring.concatenate(
										MIPSBitstring.concatenate(byte3, byte2),
										MIPSBitstring.concatenate(byte1, byte0));
							regs.setReg(rt, result.toInt());
							break;
					case 2 : byte3 = MIPSBitstring.extract(register, 24, 31);
							byte2 = new MIPSByte(mem.getByte(memoryaddress-2)).getBitstring();
							byte1 = new MIPSByte(mem.getByte(memoryaddress-1)).getBitstring();
							byte0 = new MIPSByte(mem.getByte(memoryaddress)).getBitstring();
							result = MIPSBitstring.concatenate(
										MIPSBitstring.concatenate(byte3, byte2),
										MIPSBitstring.concatenate(byte1, byte0));
							regs.setReg(rt, result.toInt());
							break;
					case 3 : regs.setReg(rt, mem.getLocation(memoryaddress - 3));
							break;				
				}
				break;
			}

			case 0x28: {
				verbose.println("SB");
				stats.instructionUsed("SB");
				// store byte
				int memorylocation = regs.getReg(rs) + i;
				MIPSWord rtword = new MIPSWord(regs.getReg(rt));
				int thebyte = MIPSBitstring.extract(rtword, 0, 7).toInt();
				mem.setByte(memorylocation, thebyte);
				break;
			}
			
			case 0x29: {
				verbose.println("SH");
				stats.instructionUsed("SH");
				// store halfword
				int memorylocation = regs.getReg(rs) + i;
				MIPSWord rtword = new MIPSWord(regs.getReg(rt));
				int thebyte = MIPSBitstring.extract(rtword, 8, 15).toInt();
				mem.setByte(memorylocation, thebyte);
				thebyte = MIPSBitstring.extract(rtword, 0, 7).toInt();
				mem.setByte(memorylocation + 1, thebyte);
				break;
			}
			
			case 0x2a: {
				verbose.println("SWL");
				stats.instructionUsed("SWL");
				// store word left
				/*
				 Sign-extend 16-bit offset and add to contents of register base to form
				 address. 
				 Shift contents of register rt right so that leftmost byte of the word 
				 is in position of addressed byte. Store bytes containing original data
				 into corresponding bytes at addressed byte.
				 
				 M is byte at Addr
				 Register			A		B		C		D
				 Memory			W	X		Y		Z
				 
				 Addr(1..0)=0	A		B		C		D		(ABCD)
				 Addr(1..0)=1	-		A		B		C		(-ABC)
				 Addr(1..0)=2	-		-		A		B		(--AB)
				 Addr(1..0)=3	-		-		-		A		(---A)
				 */
				
				int memoryaddress = regs.getReg(rs) + i;
				int rtcontents = regs.getReg(rt);
				MIPSWord register = new MIPSWord(rtcontents);
				int byte3 = MIPSBitstring.extract(register, 24, 31).toInt(); 
				int byte2 = MIPSBitstring.extract(register, 16, 23).toInt();
				int byte1 = MIPSBitstring.extract(register, 8, 15).toInt();
							
				switch(memoryaddress & 3) {
					case 0 : mem.setLocation(memoryaddress, rtcontents);
								break;
					case 1 : mem.setByte(memoryaddress, byte3);
								mem.setByte(memoryaddress + 1, byte2); 
								mem.setByte(memoryaddress + 2, byte1); 
								break;
					case 2 : mem.setByte(memoryaddress, byte3);
								mem.setByte(memoryaddress + 1, byte2); 
								break;
					case 3 : mem.setByte(memoryaddress, byte3);
								break;				
				}
				break;
			}
			
			case 0x2b: {
				verbose.println("SW");
				stats.instructionUsed("SW");
				// store word
				int memorylocation = regs.getReg(rs) + i;
				mem.setLocation(memorylocation, regs.getReg(rt));
				break;
			}
			
			case 0x2e: {
				verbose.println("SWR");
				stats.instructionUsed("SWR");
				// store word right
				/*
				 Sign-extend 16-bit offset and add to contents of register base to form 
				 address. 
				 Shift contents of register rt left so that rightmost byte of the word 
				 is in position of addressed byte. Store bytes containing original data
				 into corresponding bytes at addressed byte. 
				 
				 M is byte at Addr
				 Register			A		B		C		D
				 Memory			W	X		Y		Z
				 
				 Addr(1..0)=0	D		-		-		-		(D---)
				 Addr(1..0)=1	C		D		-		-		(CD--)
				 Addr(1..0)=2	B		C		D		-		(BCD-)
				 Addr(1..0)=3	A		B		C		D		(ABCD)
				 */
				 
				int memoryaddress = regs.getReg(rs) + i;
				int rtcontents = regs.getReg(rt);
				MIPSWord register = new MIPSWord(rtcontents);
				int byte3 = MIPSBitstring.extract(register, 24, 31).toInt(); 
				int byte2 = MIPSBitstring.extract(register, 16, 23).toInt();
				int byte1 = MIPSBitstring.extract(register, 8, 15).toInt();
				int byte0 = MIPSBitstring.extract(register, 0, 7).toInt();
							
				switch(memoryaddress & 3) {
					case 0 : mem.setByte(memoryaddress, byte0);
								break;
					case 1 : mem.setByte(memoryaddress - 1, byte1);
								mem.setByte(memoryaddress, byte0); 
								break;
					case 2 : mem.setByte(memoryaddress - 2, byte2); 
								mem.setByte(memoryaddress - 1, byte1);
								mem.setByte(memoryaddress, byte0); 
								break;
					case 3 : mem.setLocation(memoryaddress - 3, rtcontents);
								break;				
				}
				break;
			}
						
			
			// J type instructions
			
			case 0x02: {
				verbose.println("J");
				stats.instructionUsed("J");
				// jump
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
				break;
			}
			
			case 0x03: {
				verbose.println("JAL");
				stats.instructionUsed("JAL");
				// jump and link
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
				break;
			}
			
			default: {
				// unsupported instruction
				throw new YAMSUnsupportedInstructionException();
			}		
		}		
		return true;
	}

	public void setCycleManager(CycleManagerInterface cycleManager) {
	}
	
	public void setVerbose(PrintStream verbose) {
		this.verbose = verbose;
	}

}

