/*
 * Created on 03-Nov-2003
 *
 */
package yams.processor;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintStream;
import yams.exceptions.YAMSRuntimeException;
import yams.exceptions.YAMSUnsupportedSyscallException;

/**
 * A template syscall handler.
 * The syscall handling will be added to this file by the instruction handler
 * generator, taking the XML file as input.
 *  
 * @author sw00
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
	 * @param registerManager
	 * @param memoryManager
	 * @param statisticsManager
	 * @param in
	 * @param out
	 * @param verbose
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
		
		switch(regs.getReg(2)) {
			case 1: {
				verbose.println("SYSCALL : print_int");
				// print_int syscall
				out.print(regs.getReg(4));
				break;
			}
			
			case 2: {
				verbose.println("SYSCALL : print_float");
				// TODO - SYSCALL print_float (an extension)
				break;
			}
			
			case 3: {
				verbose.println("SYSCALL : print_double");
				// TODO - SYSCALL print_double  (an extension)
				break;
			}
						
			case 4: {
				verbose.println("SYSCALL : print_string");
				// print_string syscall
				int address = regs.getReg(4);
				int character = mem.getByte(address);
				char c;
				while(character != 0) {
					c = (char) character;
					out.print(c);
					address++;
					character = mem.getByte(address);
				}
				break;
			}
						
			case 5: {
				verbose.println("SYSCALL : read_int");
				// read_int syscall
				try {
					regs.setReg(2,in.read());
				}
				catch(IOException e){
					throw new YAMSRuntimeException("Syscall read_int failed");
				}
				break;
			}
						
			case 6: {
				verbose.println("SYSCALL : read_float");
				// TODO - SYSCALL read_float  (an extension)
				break;
			}
						
			case 7: {
				verbose.println("SYSCALL : read_double");
				// TODO - SYSCALL read_double  (an extension)
				break;
			}
						
			case 8: {
				verbose.println("SYSCALL : read_string");
				// read_string syscall
				int maxlength = regs.getReg(5);
				int address = regs.getReg(4);
				BufferedReader reader = new BufferedReader(
											new InputStreamReader(in));
				try {
					// read a line from stdin
					String str = reader.readLine();
					char c;
					int charbyte;
					// for each char, write it to the memory
					for(int i = 0; i < str.length(); i++) {
						c = str.charAt(i);
						charbyte = (int) c;
						mem.setByte(address, c);
						address++;
						// if we have reached the max length that we've been told to read
						if(i == maxlength - 1) { break; }
					}
					// write a null terminator
					mem.setByte(address, 0);
				}
				catch(IOException e){
					throw new YAMSRuntimeException("Syscall read_string failed");
				}
				break;
			}
						
			case 9: {
				verbose.println("SYSCALL : sbrk");
				// TODO - SYSCALL sbrk  (an extension)
				break;
			}			
						
			case 10: {
				// 'exit' syscall
				verbose.println("SYSCALL : exit");
				cycleManager.finish();
				break;
			}
			
			default: {
				// unsupported syscall
				throw new YAMSUnsupportedSyscallException();	
			}
			
		}
		return true;
	}

	public void setCycleManager(CycleManagerInterface cycleManager) {
		this.cycleManager = cycleManager;
	}
	
	public void setVerbose(PrintStream verbose) {
		this.verbose = verbose;
	}
	
}
