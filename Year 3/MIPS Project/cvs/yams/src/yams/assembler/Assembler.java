package yams.assembler;

import yams.parser.*;
import yams.processor.*;
import java.util.*;
import java.io.PrintStream;

/**
 * The Assembler class is a core component of the assembler package, 
 * which provides an intermediate assembly stage between the parser 
 * (reading in the MIPS instruction file) and the processor (executing
 * the assembled machine code).
 * <BR><BR>
 * The Assembler class coordinates the activities of the other components
 * to which it maintains references. It is created by the YAMSConsole /
 * YAMSGui objects during creation of the YAMS software. During normal
 * runtime execution, the public methods it provides will 
 * allow one entire program to be assembled into machine code, using the 
 * other components which it creates. It is the only class within the package
 * to have such a connection to external components within the package
 * <BR><BR>
 * It maintains references to the following classes:<BR><BR>
 * 	SymbolTable<BR>
 * 	GUIMap<BR>
 * 	ToBeDoneTable<BR>
 * 	OperandHandler<BR>
 * 	InstructionTable<BR>
 * 	AssemblerXMLHandler<BR>
 * 	Memory Manager<BR>
 * 
 * <BR><BR>
 * It goes through the following execution stages:<BR>
 * 0	Assembler Instantiation<BR>
 * 1	Table Initialisation<BR>
 * 2	1st Pass Assembly<BR>
 * 3	2nd Pass Assembly<BR>
 * 
 * @author jkm01
 */
public class Assembler {

	// Assembler Directives
	private static String ALIGN = ".align";
	private static String ASCII = ".ascii";
	private static String ASCIIZ = ".asciiz";
	private static String BYTE = ".byte";
	private static String DATA = ".data";
	private static String DOUBLE = ".double";
	private static String EXTERN = ".extern";
	private static String FLOAT = ".float";
	private static String GLOBL = ".globl";
	private static String HALF = ".half";
	private static String KDATA = ".kdata";
	private static String KTEXT = ".ktext";
	private static String SET = ".set";
	private static String SPACE = ".space";
	private static String TEXT = ".text";
	private static String WORD = ".word";

	// Internal Tables
	private SymbolTable symbolTable;
	// holds all the symbol -> address mappings
	private ToBeDoneTable toBeDoneTable;
	// holds all the information on items that need to be reassembled in the second phase
	private GuiMap guiMap; // maps physical addresses to program line numbers 
	private InstructionTable instructionTable;
	// holds information on the instructions

	// External Operand Handler
	private OperandHandler operandHandler;

	//Assembler read_modes & current line numbers
	private boolean data_mode;
	private boolean data_declaration_mode;
	private int thisLine;
	private int maxLine;
	private int NEXT_TEXT_ADDRESS = 0x400000;
	private int NEXT_DATA_ADDRESS = 0x10000000;
	private int $AT_REGISTER_VALUE = 0x10008000;

	// memory manager reference
	private MemoryManager memoryManager;

	// printstream reference
	private PrintStream verboseStream;

	/**
	 * The Assembler constructor sets up the Assembler so that it is ready to receive a MIPS program and
	 * assemble it into machine code. 
	 * <br><br>
	 * It goes through these stages to do so:
	 * <br><br>
	 * 1) TABLE CREATION<br><br>
	 *		Must create tables it will need during assembly: InstructionTable,SymbolTable,ToBeDoneTable,GUIMap<br><br>
	 * 2)	OPERAND HANDLER CREATION<br><br>
	 * 3)	SET INTERNAL MEMORY MANAGER REFERENCE<br><br>
	 * 		The Assembler creation call will also contain an external reference to an already existing MemoryManager. Since the writeToLocation() method contained within this class will be used frequently throughout various stages of the Assembler, it is important to set a global reference to this object.<br><br> 
	 * 4) 	XML FILE READING<br><br>
	 * 		It is necessary to fill the InstructionTable with information on each of the instructions within the “Instruction_file.xml? repository, since this will give us our supported instruction set. It would be possible to do this every time we reset the assembler, and capture any changes to the XML file (say if an instruction is added). However any changes to the instruction set require recompilation of YAMS to autogenerate the Parser and Processor handlers, so it is only necessary to read the XML once and copy the data to our tables.<br><br>
	 * 		This is achieved by utilising the LoadTableFromXML(?) method within the AssemblerXMLHandler, which when provided with the XML File Path and the empty instruction table (just created in phase I), will fill the table. 
	 * 
	 * @param xmlFilePath	The file path location of the Instruction_file.xml (Instruction Repository)
	 * @param mm			Provide a reference to the memory manager within the processor
	 * @param sm			Reference to the statistics manager, to create the entries in the instruction count table
	 * @param verbose		Outputstream for Assembler verbose mode (for debugging)
	 */
	public Assembler(
		String xmlFilePath,
		MemoryManagerInterface mm,
		StatisticsManagerInterface sm,
		PrintStream verbose) {
		// Create required tables
		symbolTable = new SymbolTable(); // create the new symbol table
		toBeDoneTable = new ToBeDoneTable();
		guiMap = new GuiMap();
		instructionTable = new InstructionTable();

		// Create the operand handler for operand encoding
		operandHandler =
			new OperandHandler(symbolTable, toBeDoneTable, instructionTable);

		// Pass the statistics manager a reference to the GUIMap
		sm.setLineMap(guiMap);

		// Initialise assembler modes (data mode and in the midst of declaring data)
		data_mode = false;
		data_declaration_mode = false;

		// Memory manager reference
		memoryManager = (MemoryManager) mm;

		// Set verbose output stream
		verboseStream = verbose;

		// Load the instruction data from the XML to the instruction table for assembler reference
		AssemblerXMLHandler.LoadTableFromXML(xmlFilePath, instructionTable, sm);
		verboseStream.println(instructionTable.printTable());
	}

	/**
	 * 
	 * The resetAssembler() method resets the assembler so that it is ready to receive a new 
	 * MIPS file to be assembled into machine code. It resets the SymbolTable, ToBeDoneTable,
	 * the GUIMap table and the values of NEXT_TEXT and NEXT_DATA addresses, to effectively wipe
	 * the effects of any previous assembly.
	 * 
	 */
	public void resetAssembler() {
		// reset symboltable
		symbolTable.resetSymbolTable();
		// reset tobedonetable
		toBeDoneTable.resetToBeDoneTable();
		// reset guimap
		guiMap.resetGuiMap();
		// reset static values
		NEXT_TEXT_ADDRESS = 0x400000;
		NEXT_DATA_ADDRESS = 0x10000000;
		$AT_REGISTER_VALUE = 0x10008000;
	}

	/**
	 * The assembleCode() method receives a LineList (AST) from the parser containing the MIPS code for
	 * the required file. It then proceeds to execute the required flow the assemble this code into machine
	 * code and place the words within memory.
	 * <BR><BR>
	 * Two phases:
	 * <BR><BR>
	 * Pass 1: Go through entire program (LineList) and translate the instructions one at a time, placing 
	 * instructions that have symbol table errors into the ToBeDoneTable.
	 * <BR><BR>
	 * Pass 2: Iterate through the ToBeDoneTable and translate the instructions with the noew complete symbol table
	 * <BR><BR>
	 * 
	 * @param code The MIPS code in a LineList AST
	 * @throws AssemblerException Throws exceptions if there are errors during assembly.
	 */
	public void assembleCode(LineList code) throws AssemblerException {
		// FIRST PHASE OF ASSEMBLY
		Line currentLine;
		thisLine = 1;
		maxLine = code.totalLines();
		while (thisLine <= maxLine) {
			currentLine = code.getLine(thisLine);
			verboseStream.println(
				"[Assembler] Encountered new line "
					+ thisLine
					+ " : "
					+ currentLine.toString());
			if (currentLine.containsLabel()) {
				handleLabel(
					currentLine.getLabel(),
					currentLine.getLineNumber());
				if (currentLine.containsInstruction())
					handleInstruction(
						currentLine.getInstruction(),
						currentLine.getLineNumber());
			} else if (currentLine.containsInstruction()) {
				handleInstruction(
					currentLine.getInstruction(),
					currentLine.getLineNumber());
			}
			thisLine++;
		}

		// SECOND PHASE OF ASSEMBLY
		int address;
		Instruction instruction;
		int lineNumber;
		for (int count = 0; count < toBeDoneTable.size(); count++) {
			address = toBeDoneTable.get(count).getAddress();
			instruction = toBeDoneTable.get(count).getInstruction();
			lineNumber = toBeDoneTable.get(count).getLineNumber();
			handleToBeDoneInstruction(address, instruction, lineNumber);
		}
	}

	/**
	 * 
	 * Handles any label encountered within a line, whether it is found within a DATA segment
	 * or within a TEXT segment, adding to SymbolTable with respective addresses ready for later
	 * reference.
	 * 
	 * @param currentLabel Label being examined
	 * @param lineNumber Current line number
	 * @throws AssemblerException
	 */
	private void handleLabel(Label currentLabel, int lineNumber)
		throws AssemblerException {
		if (data_mode) {
			symbolTable.add(
				currentLabel.getLabel(),
				NEXT_DATA_ADDRESS,
				lineNumber);
			verboseStream.println(
				"[Assembler] "
					+ currentLabel.getLabel()
					+ " given address "
					+ NEXT_DATA_ADDRESS);
			data_declaration_mode = true;
		} else {
			symbolTable.add(
				currentLabel.getLabel(),
				NEXT_TEXT_ADDRESS,
				lineNumber);
			verboseStream.println(
				"[Assembler] "
					+ currentLabel.getLabel()
					+ " given address "
					+ NEXT_TEXT_ADDRESS);
		}
	}

	/**
	 * 
	 * Handles any instruction found within a single line, but essentially checks whether it is a Directive
	 * or a General Instruction, passing the currentInstruction variable to other handlers to deal with those two
	 * cases. Will also throw an error if found to not fit into either of these categories. NB uses InstructionTable
	 * to determine whether the current instruction is valid.
	 * 
	 * @param currentInstruction	Current Instruction being considered
	 * @param lineNumber	Line number of current instruction
	 * @throws AssemblerException	
	 */
	private void handleInstruction(
		Instruction currentInstruction,
		int lineNumber)
		throws AssemblerException {
		if (currentInstruction.getName().charAt(0) == '.') {
			handleDirective(currentInstruction, lineNumber);
		} else if (
			instructionTable.isInstruction(currentInstruction.getName())) {
			if (!data_mode) {
				guiMap.add(lineNumber, NEXT_TEXT_ADDRESS, currentInstruction);
				handleGenericInstruction(currentInstruction, lineNumber);
			} else
				throw new AssemblerException(
					"[Error A01] Instruction should not be within data section",
					lineNumber,
					currentInstruction);
		} else
			throw new AssemblerException(
				"[Error A02] Encountered keyword is not an instruction and has been identified by parser incorrectly",
				lineNumber,
				currentInstruction);
	}

	/**
	 * This handles a normal instruction (not a directive), by working out what Representation it has in machine code
	 * and its individual operand combination. It then takes one machine word at a time from the code and invokes the
	 * operand handler to translate the operands and substitute them into that word. This done, all of the machine
	 * code is written to memory. If there is a symbol table miss, then it is not written to memory at the 
	 * NEXT_TEXT_ADDRESS.
	 * 
	 * @param currentInstruction
	 * @param lineNumber
	 * @throws AssemblerException
	 */
	private void handleGenericInstruction(
		Instruction currentInstruction,
		int lineNumber)
		throws AssemblerException {
		// variables
		String fullCodedMachineCode = "";
		String currentUncodedWord = "";
		String currentCodedWord = "";
		String currentSubWord = "";
		int CURRENT_ADDRESS;

		// need instruction name and operand code to obtain the machine code representation
		operandHandler.resetHandler();
		String instructionName = currentInstruction.getName();
		String operandCode =
			operandHandler.encodeOperandList(currentInstruction.getOperands());
		Representation instructionRepresentation =
			instructionTable
				.getInstruction(instructionName, lineNumber, currentInstruction)
				.getRepresentation(operandCode);
		String fullUncodedMachineCode =
			instructionRepresentation.getMachineCode();
		CURRENT_ADDRESS = NEXT_TEXT_ADDRESS;
		for (int wordNumber = 1;
			wordNumber
				<= numberOfWordsWithinWordString(
					fullUncodedMachineCode,
					lineNumber,
					currentInstruction);
			wordNumber++) {
			//iterate through the subwords in the fullUncodedMachineWord
			currentUncodedWord =
				extractWord(
					wordNumber,
					fullUncodedMachineCode,
					lineNumber,
					currentInstruction);
			currentCodedWord =
				operandHandler.encodeWord(
					currentUncodedWord,
					CURRENT_ADDRESS,
					instructionRepresentation,
					currentInstruction.getOperands(),
					operandCode,
					lineNumber,
					currentInstruction);
			fullCodedMachineCode += currentCodedWord;
			CURRENT_ADDRESS += (currentCodedWord.length() / 32 * 4);
		}
		if (operandHandler.isSymbolTableError()) {
			// if there is an symbol table error, then add the whole instruction
			toBeDoneTable.add(
				NEXT_TEXT_ADDRESS,
				currentInstruction,
				lineNumber);
			verboseStream.println(
				"[Assembler] "
					+ " added  "
					+ currentInstruction
					+ " line "
					+ lineNumber
					+ " at "
					+ NEXT_TEXT_ADDRESS
					+ " to2bdonetable");
			NEXT_TEXT_ADDRESS =
				NEXT_TEXT_ADDRESS + (fullCodedMachineCode.length() / 32) * 4;
		} else {
			for (int subWordNumber = 1;
				subWordNumber
					<= numberOfWordsWithinWordString(
						fullCodedMachineCode,
						lineNumber,
						currentInstruction);
				subWordNumber++) {
				currentSubWord =
					extractWord(
						subWordNumber,
						fullCodedMachineCode,
						lineNumber,
						currentInstruction);
				memoryManager.setLocation(
					NEXT_TEXT_ADDRESS,
					operandHandler.binaryStringToInteger(currentSubWord));
				verboseStream.println(
					"[Assembler] "
						+ "Assembled "
						+ currentInstruction
						+ " ( "
						+ currentSubWord
						+ " = "
						+ operandHandler.binaryStringToInteger(currentSubWord)
						+ " = "
						+ instructionTable.getMonatomicInstruction(
							currentSubWord,
							lineNumber,
							currentInstruction)
						+ " ) + to location "
						+ NEXT_TEXT_ADDRESS);
				NEXT_TEXT_ADDRESS = NEXT_TEXT_ADDRESS + 4;
			}
		}
	}

	/**
	 * 
	 * See implementation for handleGenericInstruction() for details of how this metho fundementally works. The only
	 * real difference is that if there is a symbol table error during this second phase of passing, then an error
	 * is thrown to indicate an invalid program. Additionally, the extra operand provides the specific addresses to
	 * write the instructions to on a instruction-by-instruction basis.
	 * 
	 * @param address Address to start writing the translated machine code for this particular instruction.
	 * @param currentInstruction
	 * @param lineNumber
	 * @throws AssemblerException
	 */
	private void handleToBeDoneInstruction(
		int address,
		Instruction currentInstruction,
		int lineNumber)
		throws AssemblerException {
		//variables
		String fullCodedMachineCode = "";
		String currentUncodedWord = "";
		String currentCodedWord = "";
		String currentSubWord = "";
		int CURRENT_ADDRESS;

		// need instruction name and operand code to obtain the machine code representation
		operandHandler.resetHandler();
		String instructionName = currentInstruction.getName();
		String operandCode =
			operandHandler.encodeOperandList(currentInstruction.getOperands());
		Representation instructionRepresentation =
			instructionTable
				.getInstruction(instructionName, lineNumber, currentInstruction)
				.getRepresentation(operandCode);
		String fullUncodedMachineCode =
			instructionRepresentation.getMachineCode();
		CURRENT_ADDRESS = address;
		for (int wordNumber = 1;
			wordNumber
				<= numberOfWordsWithinWordString(
					fullUncodedMachineCode,
					lineNumber,
					currentInstruction);
			wordNumber++) {
			//iterate through the subwords in the fullUncodedMachineWord
			currentUncodedWord =
				extractWord(
					wordNumber,
					fullUncodedMachineCode,
					lineNumber,
					currentInstruction);
			currentCodedWord =
				operandHandler.encodeWord(
					currentUncodedWord,
					CURRENT_ADDRESS,
					instructionRepresentation,
					currentInstruction.getOperands(),
					operandCode,
					lineNumber,
					currentInstruction);
			fullCodedMachineCode += currentCodedWord;
			CURRENT_ADDRESS += (currentCodedWord.length() / 32 * 4);
		}
		CURRENT_ADDRESS = address;
		if (operandHandler.isSymbolTableError()) {
			throw new AssemblerException(
				"[Error A03] SymbolTable Error: no such label on second pass through code",
				lineNumber,
				currentInstruction);
		} else {
			for (int subWordNumber = 1;
				subWordNumber
					<= numberOfWordsWithinWordString(
						fullCodedMachineCode,
						lineNumber,
						currentInstruction);
				subWordNumber++) {
				currentSubWord =
					extractWord(
						subWordNumber,
						fullCodedMachineCode,
						lineNumber,
						currentInstruction);
				memoryManager.setLocation(
					CURRENT_ADDRESS,
					operandHandler.binaryStringToInteger(currentSubWord));
				verboseStream.println(
					"[Assembler] "
						+ "Assembled "
						+ currentInstruction
						+ " ( "
						+ currentSubWord
						+ " = "
						+ operandHandler.binaryStringToInteger(currentSubWord)
						+ " = "
						+ instructionTable.getMonatomicInstruction(
							currentSubWord,
							lineNumber,
							currentInstruction)
						+ " ) + to location "
						+ CURRENT_ADDRESS);
				CURRENT_ADDRESS = CURRENT_ADDRESS + 4;
			}
		}
	}

	/**
	 * 
	 * Returns the number of 32 bit words within a given word string, throwing an exception if the string is not
	 * in multiples of 32 bits (including line / instruction reference).
	 * 
	 * @param wordString
	 * @param lineNumber
	 * @param currentInstruction
	 * @return integer number of 32 bit words within given word string
	 * @throws AssemblerException
	 */
	public int numberOfWordsWithinWordString(
		String wordString,
		int lineNumber,
		Instruction currentInstruction)
		throws AssemblerException {
		if (wordString.length() % 32 == 0) {
			return wordString.length() / 32;
		} else
			throw new AssemblerException(
				"[Error 04] XML Representation of instruction invalid, given word string ("
					+ wordString
					+ ") doesnt have length of multiples of 32",
				lineNumber,
				currentInstruction);
	}

	/**
	 * 
	 * Extracts a specific 32 bit from a string, whose length is a multiple of 32 bits.
	 * 
	 * @param wordNumber		Word number to extract, index 1 is the first and so on
	 * @param wordString		Provided machine code
	 * @param lineNumber	
	 * @param currentInstruction
	 * @return returns requested 32 bit word from string
	 * @throws AssemblerException
	 */
	public String extractWord(
		int wordNumber,
		String wordString,
		int lineNumber,
		Instruction currentInstruction)
		throws AssemblerException {
		if (wordNumber * 32 <= wordString.length() && wordNumber >= 1) {
			return wordString.substring((wordNumber - 1) * 32, wordNumber * 32);
		} else
			throw new AssemblerException(
				"[Error A05] Requested word ("
					+ wordNumber
					+ ") out of bounds for current instruction representation("
					+ wordString
					+ ")",
				lineNumber,
				currentInstruction);
	}

	/**
	 * Sets the data mode of the Assembler (boolean indicating whether we are in the DATA / TEXT segment)
	 * to the boolean "value"
	 * @param value Required data_mode value
	 */
	private void setDataMode(boolean value) {
		data_mode = value;
	}
	private void setDataDeclarationMode(boolean value) {
		data_declaration_mode = value;
	}
	/**
	 * 
	 * Returns a reference to the GUIMap in the assembler (for use when GUI / Statistics wants to get mapping
	 * information of lines -> addresses).
	 * 
	 * @return returns a reference to the GUIMap object in the Assembler
	 */
	public GuiMap getGUIMap() {
		return guiMap;
	}

	/**
	 * 
	 * Distinguishes between different types of directives, passing implementation down to the correct handlers, 
	 * and throwing errors if an invalid directive is encountered.
	 * 
	 * @param currentDirective
	 * @param lineNumber
	 * @throws AssemblerException
	 */
	private void handleDirective(Instruction currentDirective, int lineNumber)
		throws AssemblerException {
		if (currentDirective.getName().equals(ALIGN)) {
			handleAlign(currentDirective, lineNumber);
		} else if (currentDirective.getName().equals(ASCII)) {
			handleAscii(currentDirective, lineNumber);
		} else if (currentDirective.getName().equals(ASCIIZ)) {
			handleAsciiz(currentDirective, lineNumber);
		} else if (currentDirective.getName().equals(BYTE)) {
			handleByte(currentDirective, lineNumber);
		} else if (currentDirective.getName().equals(DATA)) {
			handleData(currentDirective, lineNumber);
		} else if (currentDirective.getName().equals(DOUBLE)) {
			handleDouble(currentDirective, lineNumber);
		} else if (currentDirective.getName().equals(EXTERN)) {
			handleExtern(currentDirective, lineNumber);
		} else if (currentDirective.getName().equals(FLOAT)) {
			handleFloat(currentDirective, lineNumber);
		} else if (currentDirective.getName().equals(GLOBL)) {
			handleGlobl(currentDirective, lineNumber);
		} else if (currentDirective.getName().equals(HALF)) {
			handleHalf(currentDirective, lineNumber);
		} else if (currentDirective.getName().equals(KDATA)) {
			handleKdata(currentDirective, lineNumber);
		} else if (currentDirective.getName().equals(KTEXT)) {
			handleKtext(currentDirective, lineNumber);
		} else if (currentDirective.getName().equals(SET)) {
			handleSet(currentDirective, lineNumber);
		} else if (currentDirective.getName().equals(SPACE)) {
			handleSpace(currentDirective, lineNumber);
		} else if (currentDirective.getName().equals(TEXT)) {
			handleText(currentDirective, lineNumber);
		} else if (currentDirective.getName().equals(WORD)) {
			handleWord(currentDirective, lineNumber);
		} else
			throw new AssemblerException(
				"[Error A06] Encountered keyword is not an assembler directive and has been identified by parser incorrectly",
				lineNumber,
				currentDirective);
	}

	/**
	 * Convert a given Ascii string (contained within Directive itelf) into ascii bytes and write to memory within
	 * the DATA / TEXT segment as indicated by the code.
	 * 
	 * @param currentDirective
	 * @param lineNumber
	 * @throws AssemblerException
	 */
	private void handleAsciiz(Instruction currentDirective, int lineNumber)
		throws AssemblerException {
		List ops = currentDirective.getOperands();
		if (ops.size() >= 1) {
			Operand op = (Operand) ops.get(0);
			if (op instanceof StringOperand) {
				String str = ((StringOperand) op).toString();

				int currentChar = 0;
				int currentCharValue = 0;
				while (currentChar < str.length()) {
					if (str.charAt(currentChar) == 92) {
						verboseStream.println("[Assembler] Picked up escape ");
						currentChar++;
						if (currentChar < str.length()) {
							verboseStream.println(
								"[Assembler] Assigning escape ");

							if (str.charAt(currentChar) == 'n')
								currentCharValue = 10;
							//										else if 						other escape characters definitions
						} else {
							currentChar--;
							currentCharValue = (int) str.charAt(currentChar);
						}
					} else
						currentCharValue = (int) str.charAt(currentChar);
					if (data_mode) {
						memoryManager.setByte(
							NEXT_DATA_ADDRESS,
							currentCharValue);
						verboseStream.println(
							"[Assembler] Assembled "
								+ currentDirective
								+ " ( "
								+ currentCharValue
								+ " ) + to location "
								+ NEXT_DATA_ADDRESS);
						NEXT_DATA_ADDRESS = NEXT_DATA_ADDRESS + 1;
					} else {
						memoryManager.setByte(
							NEXT_TEXT_ADDRESS,
							currentCharValue);
						verboseStream.println(
							"[Assembler] Assembled "
								+ currentDirective
								+ " ( "
								+ currentCharValue
								+ " ) + to location "
								+ NEXT_TEXT_ADDRESS);
						NEXT_TEXT_ADDRESS = NEXT_TEXT_ADDRESS + 1;
					}
					currentChar++;
				}
				if (data_mode) {
					while (NEXT_DATA_ADDRESS % 4 != 0) {
						memoryManager.setByte(NEXT_DATA_ADDRESS, 0);
						verboseStream.println(
							"[Assembler] Assembled "
								+ currentDirective
								+ " ( 0 ) + to location "
								+ NEXT_DATA_ADDRESS);
						NEXT_DATA_ADDRESS++;
					}
				} else {
					while (NEXT_TEXT_ADDRESS % 4 != 0) {
						memoryManager.setByte(NEXT_TEXT_ADDRESS, 0);
						verboseStream.println(
							"[Assembler] Assembled "
								+ currentDirective
								+ " (0) + to location "
								+ NEXT_TEXT_ADDRESS);
						NEXT_TEXT_ADDRESS++;
					}
				}
			} else
				throw new AssemblerException(
					"[Error A07] invalid argument for .asciiz",
					lineNumber,
					currentDirective);
		}
	}

	/**
	 * 
	 * When the data tag is encountered as a directive in the code, changes the data_mode value so that the assembler
	 * will expect data declarations / variable declarations.
	 * 
	 * @param currentDirective
	 * @param lineNumber
	 * @throws AssemblerException
	 */
	private void handleData(Instruction currentDirective, int lineNumber)
		throws AssemblerException {
		verboseStream.println("[Assembler] Encountered DATA tag");
		// data directive, so set data mode to true
		setDataMode(true);
		// if has operand then set next data address to immediate value
		List ops = currentDirective.getOperands();
		if (ops.size() >= 1) {
			Operand op = (Operand) ops.get(0);
			if (op instanceof ImmediateOperand) {
				NEXT_DATA_ADDRESS = ((ImmediateOperand) op).getValue();
				verboseStream.println(
					"[Assembler] Changed NEXT_DATA_ADDRESS to "
						+ NEXT_DATA_ADDRESS);
			} else
				throw new AssemblerException(
					"[Error A08] Invalid argument for .data",
					lineNumber,
					currentDirective);
		}
	}

	/**
	 * 
	 * Allocates a specified number of bytes within the DATA segment to a specified label in the symbol table,
	 * effectively incrementing the NEXT_DATA_ADDRESS so that variables can be stored there in the future.
	 * 
	 * @param currentDirective
	 * @param lineNumber
	 * @throws AssemblerException
	 */
	private void handleSpace(Instruction currentDirective, int lineNumber)
		throws AssemblerException {
		int n;
		List ops = currentDirective.getOperands();
		if (ops.size() >= 1) {
			Operand op = (Operand) ops.get(0);
			if (op instanceof ImmediateOperand) {
				n = ((ImmediateOperand) op).getValue();
			} else
				throw new AssemblerException(
					"[Error A09] Invalid argument for .space",
					lineNumber,
					currentDirective);
		} else
			throw new AssemblerException(
				"[Error A10] Argument missing for .space",
				lineNumber,
				currentDirective);
		if (data_mode) {
			NEXT_DATA_ADDRESS += n;
			while (NEXT_DATA_ADDRESS % 4 != 0)
				NEXT_DATA_ADDRESS++;
			verboseStream.println(
				"[Assembler] Assigned space in memory for " + n + " bytes");
		} else {
			NEXT_TEXT_ADDRESS += n;
			while (NEXT_TEXT_ADDRESS % 4 != 0)
				NEXT_TEXT_ADDRESS++;
			verboseStream.println(
				"[Assembler] Assigned space in memory for " + n + " bytes");
		}
	}

	/**
	 * When the text tag is encountered as a directive in the code, changes the data_mode value so that the assembler
	 * will expect instructions to be required to be translated in subsequent lines.
	 * 
	 * 
	 * @param currentDirective
	 * @param lineNumber
	 * @throws AssemblerException
	 */
	private void handleText(Instruction currentDirective, int lineNumber)
		throws AssemblerException {
		verboseStream.println("[Assembler] Encountered TEXT directive");
		// set data mode off
		setDataMode(false);
		// if has operand then set next text address to immediate value
		List ops = currentDirective.getOperands();
		if (ops.size() >= 1) {
			Operand op = (Operand) ops.get(0);
			if (op instanceof ImmediateOperand) {
				NEXT_TEXT_ADDRESS = ((ImmediateOperand) op).getValue();
			} else
				throw new AssemblerException(
					"[Error A11] Invalid argument for .text",
					lineNumber,
					currentDirective);
		}
	}

	/**
	 * Convert a given word value (contained within Directive itelf) into binary and write to memory within
	 * the DATA / TEXT segment as indicated by the code.
	 * 
	 * @param currentDirective
	 * @param lineNumber
	 * @throws AssemblerException
	 */
	private void handleWord(Instruction currentDirective, int lineNumber)
		throws AssemblerException {
		verboseStream.println(
			"[Assembler] Encountered WORD directive " + currentDirective);
		int n;
		List ops = currentDirective.getOperands();
		for (int count = 0; count < ops.size(); count++) {
			Operand op = (Operand) ops.get(count);
			if (op instanceof ImmediateOperand) {
				n = ((ImmediateOperand) op).getValue();
				if (data_mode) {
					memoryManager.setLocation(NEXT_DATA_ADDRESS, n);
					verboseStream.println(
						"[Assembler] Assembled "
							+ currentDirective
							+ " ( "
							+ n
							+ " ) + to location "
							+ NEXT_DATA_ADDRESS);
					NEXT_DATA_ADDRESS = NEXT_DATA_ADDRESS + 4;
				} else {
					memoryManager.setLocation(NEXT_TEXT_ADDRESS, n);
					verboseStream.println(
						"[Assembler] Assembled "
							+ currentDirective
							+ " ( "
							+ n
							+ " ) + to location "
							+ NEXT_TEXT_ADDRESS);
					NEXT_TEXT_ADDRESS = NEXT_TEXT_ADDRESS + 4;
				}
			} else
				throw new AssemblerException(
					"[Error A12] Invalid argument for .word",
					lineNumber,
					currentDirective);
		}
	}

	private void handleAscii(Instruction currentDirective, int lineNumber)
		throws AssemblerException {
		// NOT REQUIRED FOR COMPILER LAB
		String translatedOperand;
		// data section, datadeclaration mode 
		if (data_mode) {
		} else {
			// ascii defined not in data mode (i.e. after a .text)
			throw new AssemblerException(
				"[Error A13] .ascii found not in .data section",
				lineNumber,
				currentDirective);
		}
	}

	private void handleByte(Instruction currentDirective, int lineNumber)
		throws AssemblerException {
		// NOT REQUIRED FOR COMPILER LAB
		if (data_mode) {
			//store successive operands in memory @ next_data_address, increment appropriate amount, already stored label address in symbol table & null terminate it if has name
			// once finished turn off data declaration mode

		} else {
			throw new AssemblerException(
				"[Error A14] .byte found not in .data section",
				lineNumber,
				currentDirective);
		}
	}

	private void handleHalf(Instruction currentDirective, int lineNumber)
		throws AssemblerException {
		// NOT REQUIRED FOR COMPILER LAB
		if (data_mode) {
			//store successive 16 bit nums in memory halfwords @ next_data_address, increment appropriate amount, already stored label address in symbol table & null terminate it if has name
			// once finished turn off data declaration mode
		} else {
			throw new AssemblerException(
				"[Error A15] .half found not in .data section",
				lineNumber,
				currentDirective);
		}
	}

	private void handleDouble(
		Instruction currentDirective,
		int lineNumber) { /* NOWAY */
	}
	private void handleFloat(
		Instruction currentDirective,
		int lineNumber) { /* NOWAY */
	}
	private void handleAlign(
		Instruction currentDirective,
		int lineNumber) { /*NOWAY*/
	}
	private void handleExtern(
		Instruction currentDirective,
		int lineNumber) { /*NOWAY*/
	}
	private void handleGlobl(
		Instruction currentDirective,
		int lineNumber) { /*NOWAY*/
	}
	private void handleKdata(
		Instruction currentDirective,
		int lineNumber) { /*NOWAY*/
	}
	private void handleKtext(
		Instruction currentDirective,
		int lineNumber) { /*NOWAY*/
	}
	private void handleSet(
		Instruction currentDirective,
		int lineNumber) { /*NOWAY*/
	}
}
