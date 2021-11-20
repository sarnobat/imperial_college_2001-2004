
// import java packages

package yams;

import java.io.*;
import java.util.*;

import yams.parser.*;
import yams.assembler.*;
import yams.processor.*;


/**
 * @author ajb101
 *
 */
public class YAMSConsole extends YAMSController {


	boolean statsMode = false;
	boolean verboseMode = false;
	
	PrintWriter statsOutputFile = null;
	
	public YAMSConsole() {
		// constructor
	}
	
	public void start(String args[]) {

		// variable declarations (include default options)
		LinkedList files = new LinkedList();
		String instructionFile = "Instruction_file.xml";

		// parse command line arguments
		for (int i = 1; i < args.length; i++) {

			if (args[i].equalsIgnoreCase("-s")
					|| args[i].equalsIgnoreCase("--stats")) {
				statsMode = true;
			
			} else if (args[i].equalsIgnoreCase("-o")
					|| args[i].equalsIgnoreCase("--output")) {
				// next argument should be the name of an output file
				if (i == args.length - 1) {
					// no more options
					System.err.println("-o/--output needs an argument");
					continue;
				} else {
					i++;
					// create an output stream
					try {
						statsOutputFile = new PrintWriter(new BufferedWriter(new FileWriter(args[i])));
					}
					catch (IOException e) {
						// error opening output file
						System.err.println("Error opening stats output file: " + args[i]);
						statsOutputFile = null;
					}
				}

			} else if (args[i].equalsIgnoreCase("-v")
					|| args[i].equalsIgnoreCase("--verbose")) {
				verboseMode = true;

			} else if (args[i].equalsIgnoreCase("-if")) {
				// next argument is an input file containing a list of files
				if (i == args.length - 1) {
					// but no more options
					System.err.println("-if needs an argument");
					continue;
				} else {
					i++;
					// open file and read filenames
					FileReader file;
					BufferedReader input = null;
					// get the path of the file we were given
					File fileName = new File(args[i]);
					String path = (fileName.getPath()).substring(0, fileName.getPath().length() - fileName.getName().length());
					try {
						file = new FileReader(args[i]);
						input = new BufferedReader(file);
					} catch (FileNotFoundException e) {
						System.err.println("Unable to find input file: " + args[i]);
						continue;
					}
					try {
						String line;
						while ((line = input.readLine()) != null) {
							files.add(new File(path + line));
						}
					} catch (IOException e) {
						// some other error
						e.printStackTrace();
					}

				}

			} else {
				// assume it's a file
				files.add(new File(args[i]));

			}

		}
		
		
		// if in stats mode, create processor with a blank out stream
		Processor processor = null;
		Assembler asm;
		if (verboseMode == true) {
			processor = new Processor(this, System.in, System.out, System.out);
			asm = new Assembler(instructionFile, processor.memoryManager, processor.statisticsManager, new PrintStream(new NullStream()));
		} else {
			processor = new Processor(this, System.in, System.out, new PrintStream(new NullStream()));
			asm = new Assembler(instructionFile, processor.memoryManager, processor.statisticsManager, new PrintStream(new NullStream()));
		}
		

		// loop through our list of files
		Iterator it = files.iterator();
		File currentFile;
		
		while( it.hasNext() ) {
			currentFile = (File)it.next();
			System.out.println("");
			System.out.println("=====STARTING NEW FILE=====");
			System.out.println("Input File: " + currentFile.getName());
			
			// reset processor (memoryManager, statistics manager, register manager and cycle manager)
			processor.reset();
			
			// reset assembler
			asm.resetAssembler();

			StringBuffer programCode;
			Parser parser = new Parser();
			LineList instrList = null;

			try {
				// load code into string buffer
				programCode = loadCode(currentFile);
				// parse it
				instrList = parser.parse(programCode);
//				System.out.println(instrList.toString());
				// assemble it
				asm.assembleCode(instrList);
			}
			catch (FileNotFoundException e) {
				System.out.println("File Not Found, skipping");
				continue;
			}
			catch (LexerException e) {
				System.out.println("Lexer Error:" + e.toString());
				continue;
			}
			catch (ParseException e) {
				System.out.println("Parser Error: LINE("+e.getLine()+ ") - " +e.toString());
				continue;
			}
			catch (AssemblerException e) {
				int line = e.getLine();
				Instruction i = e.getInstruction();				
//				System.out.println("Assembler Error: LINE("+e.getLine()+") INSTURCTION("+e.getInstruction().toString()+" - " + e.toString());
				System.out.println("Assembler Error: LINE("+e.getLine() + e.toString());
				continue;

			}
			
			try {
				// start processor
				processor.cycleManager.jump(0x400000);
				processor.cycleManager.start();
			}
			catch (Exception e) {
				System.out.println("Processor error");
				continue;
			}
			
			
			// if in stats mode, display stats
			if (statsMode == true) {
				processor.statisticsManager.printStats(instrList);
			}
			
			// if an output file was specified, output some stats to it
			if (statsOutputFile != null) {
				statsOutputFile.println(currentFile.getName() + "," + processor.statisticsManager.getCycles());
			}
			
		}
		
		if (statsOutputFile != null) {
			statsOutputFile.println("Total Cycles," + processor.statisticsManager.getTotalCycles());
			statsOutputFile.flush();
		}

	}
	
	public void regChanged(String regID) {}
	public void memoryChanged(int address) {}
	public void updateStatistics() {}

	class NullStream extends OutputStream {
		NullStream() {}
		public void write(int b) {}
	} 

}
