package yams.assembler;

import yams.parser.*;
import yams.processor.*;
import java.util.*;
import java.io.PrintStream;


/*
 * 
 * Created on 04-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

/**
 * @author jkm01
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Assembler {
		
	// Assembler Directives
	private static String ALIGN= ".align";
	private static String ASCII= ".ascii";
	private static String ASCIIZ= ".asciiz";
	private static String BYTE= ".byte";
	private static String DATA= ".data";
	private static String DOUBLE= ".double";
	private static String EXTERN= ".extern";
	private static String FLOAT= ".float";
	private static String GLOBL= ".globl";
	private static String HALF= ".half";
	private static String KDATA= ".kdata";
	private static String KTEXT= ".ktext";
	private static String SET= ".set";
	private static String SPACE= ".space";
	private static String TEXT= ".text";
	private static String WORD = ".word";
	
	// Internal Tables
	private SymbolTable symbolTable; 			// holds all the symbol -> address mappings
	private ToBeDoneTable  toBeDoneTable; 	// holds all the information on items that need to be reassembled in the second phase
	private GuiMap guiMap;								// maps physical addresses to program line numbers 
	private InstructionTable instructionTable;	// holds information on the instructions

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

	public Assembler(String xmlFilePath, MemoryManagerInterface mm, StatisticsManagerInterface sm, PrintStream verbose)
	{
		// Create required tables
		symbolTable = new SymbolTable(); // create the new symbol table
		toBeDoneTable = new ToBeDoneTable();
		guiMap = new GuiMap();
		instructionTable = new InstructionTable();
		
		// Create the operand handler for operand encoding
		operandHandler = new OperandHandler(symbolTable,toBeDoneTable,instructionTable);

		// Pass the statistics manager a reference to the GUIMap
		sm.setLineMap(guiMap);
	
		// Initialise assembler modes (data mode and in the midst of declaring data)
		data_mode = false;
		data_declaration_mode=false;
		
		// Memory manager reference
		memoryManager = (MemoryManager)mm;
		
		// Set verbose output stream
		verboseStream = verbose;

		// Load the instruction data from the XML to the instruction table for assembler reference
		AssemblerXMLHandler.LoadTableFromXML(xmlFilePath,instructionTable,sm);
		verboseStream.println(instructionTable.printTable());
	}

	public void resetAssembler()
	{
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

	public void assembleCode(LineList code) throws AssemblerException
	{
		// FIRST PHASE OF ASSEMBLY
		Line currentLine;
		thisLine = 1;
		maxLine = code.totalLines();		
		while (thisLine<=maxLine)
		{
			currentLine = code.getLine(thisLine);
			verboseStream.println("[Assembler] Encountered new line "+thisLine+" : "+currentLine.toString());
			if (currentLine.containsLabel())
				{
					handleLabel(currentLine.getLabel(), currentLine.getLineNumber());
					if (currentLine.containsInstruction()) handleInstruction(currentLine.getInstruction(), currentLine.getLineNumber());
				}
			else if (currentLine.containsInstruction())
				{
					handleInstruction(currentLine.getInstruction(), currentLine.getLineNumber());
				}
			thisLine++;
		}

		// SECOND PHASE OF ASSEMBLY
		int address;
		Instruction instruction;
		int lineNumber;
		for (int count = 0; count <toBeDoneTable.size(); count++)
			{
				address = toBeDoneTable.get(count).getAddress();
				instruction = toBeDoneTable.get(count).getInstruction();
				lineNumber = toBeDoneTable.get(count).getLineNumber();
				handleToBeDoneInstruction(address,instruction,lineNumber);
			}
	}

	
	private void handleLabel(Label currentLabel,int lineNumber) throws AssemblerException
	{
		if (data_mode) {
			symbolTable.add(currentLabel.getLabel(),NEXT_DATA_ADDRESS, lineNumber);
			verboseStream.println("[Assembler] "+currentLabel.getLabel()+" given address "+NEXT_DATA_ADDRESS);		
			data_declaration_mode=true;			
		} else {
			symbolTable.add(currentLabel.getLabel(),NEXT_TEXT_ADDRESS, lineNumber);
			verboseStream.println("[Assembler] "+currentLabel.getLabel()+" given address "+NEXT_TEXT_ADDRESS);		
		}
	}
	
	
	 
	private void handleInstruction(Instruction currentInstruction, int lineNumber) throws AssemblerException 
	{
		if (currentInstruction.getName().charAt(0)=='.')
			{
				handleDirective(currentInstruction,lineNumber);
			} 
		else if (instructionTable.isInstruction(currentInstruction.getName())) 
			{
				if (!data_mode)
					{
						guiMap.add(lineNumber, NEXT_TEXT_ADDRESS,currentInstruction);
						handleGenericInstruction(currentInstruction,lineNumber);
					} 
				else throw new AssemblerException("[Error A01] Instruction should not be within data section",lineNumber,currentInstruction);			
		} else throw new AssemblerException("[Error A02] Encountered keyword is not an instruction and has been identified by parser incorrectly",lineNumber,currentInstruction);
	}
	
	private void handleGenericInstruction(Instruction currentInstruction, int lineNumber) throws AssemblerException
	{
		// variables
		String fullCodedMachineCode = "";
		String currentUncodedWord = "";
		String currentCodedWord = "";
		String currentSubWord="";
		int CURRENT_ADDRESS;
		
		// need instruction name and operand code to obtain the machine code representation
		operandHandler.resetHandler();
		String instructionName = currentInstruction.getName();
		String operandCode = operandHandler.encodeOperandList(currentInstruction.getOperands());
		Representation instructionRepresentation = instructionTable.getInstruction(instructionName,lineNumber,currentInstruction).getRepresentation(operandCode);
		String fullUncodedMachineCode = instructionRepresentation.getMachineCode();
		CURRENT_ADDRESS = NEXT_TEXT_ADDRESS; 
		for (int wordNumber=1; wordNumber<=numberOfWordsWithinWordString(fullUncodedMachineCode, lineNumber, currentInstruction); wordNumber++)
			{
				//iterate through the subwords in the fullUncodedMachineWord
				currentUncodedWord = extractWord(wordNumber,fullUncodedMachineCode,lineNumber,currentInstruction);
				currentCodedWord = operandHandler.encodeWord(currentUncodedWord,CURRENT_ADDRESS, instructionRepresentation, currentInstruction.getOperands(), operandCode, lineNumber, currentInstruction);
				fullCodedMachineCode+=currentCodedWord;
				CURRENT_ADDRESS+=(currentCodedWord.length()/32*4);
			}
		if (operandHandler.isSymbolTableError())
			{
				// if there is an symbol table error, then add the whole instruction
				toBeDoneTable.add(NEXT_TEXT_ADDRESS,currentInstruction,lineNumber);
				verboseStream.println("[Assembler] "+" added  "+currentInstruction+" line "+lineNumber+" at "+NEXT_TEXT_ADDRESS+" to2bdonetable");
				NEXT_TEXT_ADDRESS = NEXT_TEXT_ADDRESS + (fullCodedMachineCode.length()/32)*4;
			}
		else
			{
				for (int subWordNumber = 1; subWordNumber<=numberOfWordsWithinWordString(fullCodedMachineCode, lineNumber, currentInstruction); subWordNumber++)
					{
						currentSubWord = extractWord(subWordNumber,fullCodedMachineCode, lineNumber, currentInstruction);
						memoryManager.setLocation(NEXT_TEXT_ADDRESS,operandHandler.binaryStringToInteger(currentSubWord));
						verboseStream.println("[Assembler] "+"Assembled "+currentInstruction+" ( "+currentSubWord+" = "+operandHandler.binaryStringToInteger(currentSubWord)+" = "+instructionTable.getMonatomicInstruction(currentSubWord,lineNumber,currentInstruction)+" ) + to location "+NEXT_TEXT_ADDRESS);
						NEXT_TEXT_ADDRESS = NEXT_TEXT_ADDRESS+4;									
					}
			}
	}
	
	private void handleToBeDoneInstruction(int address, Instruction currentInstruction, int lineNumber) throws AssemblerException
	{
		//variables
		String fullCodedMachineCode = "";
		String currentUncodedWord = "";
		String currentCodedWord = "";
		String currentSubWord="";
		int CURRENT_ADDRESS;
		
		// need instruction name and operand code to obtain the machine code representation
		operandHandler.resetHandler();
		String instructionName = currentInstruction.getName();
		String operandCode = operandHandler.encodeOperandList(currentInstruction.getOperands());
		Representation instructionRepresentation = instructionTable.getInstruction(instructionName,lineNumber,currentInstruction).getRepresentation(operandCode);
		String fullUncodedMachineCode = instructionRepresentation.getMachineCode();
		CURRENT_ADDRESS = address; 
		for (int wordNumber=1; wordNumber<=numberOfWordsWithinWordString(fullUncodedMachineCode, lineNumber, currentInstruction); wordNumber++)
			{
				//iterate through the subwords in the fullUncodedMachineWord
				currentUncodedWord = extractWord(wordNumber,fullUncodedMachineCode,lineNumber,currentInstruction);
				currentCodedWord = operandHandler.encodeWord(currentUncodedWord,CURRENT_ADDRESS, instructionRepresentation, currentInstruction.getOperands(), operandCode, lineNumber, currentInstruction);
				fullCodedMachineCode+=currentCodedWord;
				CURRENT_ADDRESS+=(currentCodedWord.length()/32*4);
			}
		CURRENT_ADDRESS=address;
		if (operandHandler.isSymbolTableError())
			{
				throw new AssemblerException("[Error A03] SymbolTable Error: no such label on second pass through code",lineNumber,currentInstruction);
			}
		else
			{
				for (int subWordNumber = 1; subWordNumber<=numberOfWordsWithinWordString(fullCodedMachineCode, lineNumber, currentInstruction); subWordNumber++)
					{
						currentSubWord = extractWord(subWordNumber,fullCodedMachineCode, lineNumber, currentInstruction);
						memoryManager.setLocation(CURRENT_ADDRESS,operandHandler.binaryStringToInteger(currentSubWord));
						verboseStream.println("[Assembler] "+"Assembled "+currentInstruction+" ( "+currentSubWord+" = "+operandHandler.binaryStringToInteger(currentSubWord)+" = "+instructionTable.getMonatomicInstruction(currentSubWord,lineNumber,currentInstruction)+" ) + to location "+CURRENT_ADDRESS);
						CURRENT_ADDRESS= CURRENT_ADDRESS+4;									
					}
			}
	}

	public int numberOfWordsWithinWordString(String wordString, int lineNumber, Instruction currentInstruction) throws AssemblerException
	{
		if (wordString.length()%32==0)
			{
				return wordString.length()/32;
			}
		else	throw new AssemblerException("[Error 04] XML Representation of instruction invalid, given word string ("+wordString+") doesnt have length of multiples of 32",lineNumber,currentInstruction);
	}	
	
	public String extractWord(int wordNumber, String wordString, int lineNumber, Instruction currentInstruction) throws AssemblerException
	{
		if (wordNumber*32<=wordString.length() && wordNumber>=1)
			{
				return wordString.substring((wordNumber-1)*32,wordNumber*32);
			}
		else throw new AssemblerException("[Error A05] Requested word ("+wordNumber+") out of bounds for current instruction representation("+wordString+")",lineNumber,currentInstruction);
	}


	private void setDataMode(boolean value) {data_mode=value;}
	private void setDataDeclarationMode(boolean value) {data_declaration_mode=value;}
	public GuiMap getGUIMap() {return guiMap;}


	private void handleDirective(Instruction currentDirective, int lineNumber) throws AssemblerException
	{
		if (currentDirective.getName().equals(ALIGN)) 				{handleAlign(currentDirective,lineNumber);}
		else if (currentDirective.getName().equals(ASCII))			{handleAscii(currentDirective,lineNumber);}
		else if (currentDirective.getName().equals(ASCIIZ))		{handleAsciiz(currentDirective,lineNumber);}
		else if (currentDirective.getName().equals(BYTE))			{handleByte(currentDirective,lineNumber);}
		else if (currentDirective.getName().equals(DATA))			{handleData(currentDirective,lineNumber);}
		else if (currentDirective.getName().equals(DOUBLE))	{handleDouble(currentDirective,lineNumber);}
		else if (currentDirective.getName().equals(EXTERN))	{handleExtern(currentDirective,lineNumber);}
		else if (currentDirective.getName().equals(FLOAT))		{handleFloat(currentDirective,lineNumber);}
		else if (currentDirective.getName().equals(GLOBL))		{handleGlobl(currentDirective,lineNumber);}
		else if (currentDirective.getName().equals(HALF))			{handleHalf(currentDirective,lineNumber);}
		else if (currentDirective.getName().equals(KDATA))		{handleKdata(currentDirective,lineNumber);}
		else if (currentDirective.getName().equals(KTEXT))		{handleKtext(currentDirective,lineNumber);}
		else if (currentDirective.getName().equals(SET))			{handleSet(currentDirective,lineNumber);}
		else if (currentDirective.getName().equals(SPACE))		{handleSpace(currentDirective,lineNumber);}
		else if (currentDirective.getName().equals(TEXT))			{handleText(currentDirective,lineNumber);}
		else if (currentDirective.getName().equals(WORD))		{handleWord(currentDirective,lineNumber);}
		else	throw new AssemblerException("[Error A06] Encountered keyword is not an assembler directive and has been identified by parser incorrectly",lineNumber,currentDirective);
	}


	private void handleAsciiz(Instruction currentDirective, int lineNumber) throws AssemblerException
	{
		List ops = currentDirective.getOperands();
		if (ops.size()>= 1) 
		{
			Operand op = (Operand)ops.get(0);
			if (op instanceof StringOperand) 
			{
				String str = ((StringOperand)op).toString();

				int currentChar=0;
				int currentCharValue=0;
				while (currentChar<str.length())
					{
						if (str.charAt(currentChar)==92)
							{
								verboseStream.println("[Assembler] Picked up escape ");
								currentChar++;
								if (currentChar<str.length())
									{
										verboseStream.println("[Assembler] Assigning escape ");

										if (str.charAt(currentChar)=='n') currentCharValue=10;
//										else if 						other escape characters definitions
									}
								else 
									{
										currentChar--;
										currentCharValue= (int)str.charAt(currentChar);
									}
							}
						else	currentCharValue = (int)str.charAt(currentChar);
						if (data_mode)
							{
								memoryManager.setByte(NEXT_DATA_ADDRESS,currentCharValue);
								verboseStream.println("[Assembler] Assembled "+currentDirective+" ( "+currentCharValue+" ) + to location "+NEXT_DATA_ADDRESS);							
								NEXT_DATA_ADDRESS=NEXT_DATA_ADDRESS+1;		
							}
						else
							{
								memoryManager.setByte(NEXT_TEXT_ADDRESS,currentCharValue);
								verboseStream.println("[Assembler] Assembled "+currentDirective+" ( "+currentCharValue+" ) + to location "+NEXT_TEXT_ADDRESS);							
								NEXT_TEXT_ADDRESS= NEXT_TEXT_ADDRESS+1;
							}
						currentChar++;
					}
				if (data_mode)
					{
						while (NEXT_DATA_ADDRESS%4!=0)
							{
								memoryManager.setByte(NEXT_DATA_ADDRESS,0);
								verboseStream.println("[Assembler] Assembled "+currentDirective+" ( 0 ) + to location "+NEXT_DATA_ADDRESS);	
								NEXT_DATA_ADDRESS++;
							}
					}
				else
					{
						while (NEXT_TEXT_ADDRESS%4!=0)
							{
								memoryManager.setByte(NEXT_TEXT_ADDRESS,0);
								verboseStream.println("[Assembler] Assembled "+currentDirective+" (0) + to location "+NEXT_TEXT_ADDRESS);	
								NEXT_TEXT_ADDRESS++;						
							}
					}
			}
			else throw new AssemblerException("[Error A07] invalid argument for .asciiz",lineNumber,currentDirective);
		}
	}	
	
	
	private void handleData(Instruction currentDirective, int lineNumber) throws AssemblerException
	{
		verboseStream.println("[Assembler] Encountered DATA tag");
		// data directive, so set data mode to true
		setDataMode(true);
		// if has operand then set next data address to immediate value
		List ops = currentDirective.getOperands();
		if (ops.size()>= 1) 
		{
			Operand op = (Operand)ops.get(0);
			if (op instanceof ImmediateOperand) 
			{
				NEXT_DATA_ADDRESS = ((ImmediateOperand)op).getValue();
				verboseStream.println("[Assembler] Changed NEXT_DATA_ADDRESS to "+NEXT_DATA_ADDRESS);											
			}
			else throw new AssemblerException("[Error A08] Invalid argument for .data",lineNumber,currentDirective);
		}
	}
	
	private void handleSpace(Instruction currentDirective, int lineNumber) throws AssemblerException
	{
		int n;
		List ops = currentDirective.getOperands();
		if (ops.size() >= 1)
			{
				Operand op = (Operand)ops.get(0);
				if (op instanceof ImmediateOperand) 
					{
						n = ((ImmediateOperand)op).getValue();
					}
				else throw new AssemblerException("[Error A09] Invalid argument for .space",lineNumber,currentDirective);
			}
		else throw new AssemblerException("[Error A10] Argument missing for .space",lineNumber,currentDirective);
		if (data_mode) 
			{
				NEXT_DATA_ADDRESS += n;
				while (NEXT_DATA_ADDRESS%4!=0) NEXT_DATA_ADDRESS++;
				verboseStream.println("[Assembler] Assigned space in memory for "+n+" bytes");
			}
		else
			{
				NEXT_TEXT_ADDRESS += n;
				while (NEXT_TEXT_ADDRESS%4!=0) NEXT_TEXT_ADDRESS++;
				verboseStream.println("[Assembler] Assigned space in memory for "+n+" bytes");
			}
	}
	
	private void handleText(Instruction currentDirective, int lineNumber) throws AssemblerException
	{
		verboseStream.println("[Assembler] Encountered TEXT directive");
		// set data mode off
		setDataMode(false);
		// if has operand then set next text address to immediate value
		List ops = currentDirective.getOperands();
		if (ops.size() >= 1) 
			{
				Operand op = (Operand)ops.get(0);
				if (op instanceof ImmediateOperand) 
					{
						NEXT_TEXT_ADDRESS = ((ImmediateOperand)op).getValue();
					} 
				else throw new AssemblerException("[Error A11] Invalid argument for .text",lineNumber,currentDirective);
			}
	}
	
	private void handleWord(Instruction currentDirective, int lineNumber) throws AssemblerException
	{
		verboseStream.println("[Assembler] Encountered WORD directive "+currentDirective);
		int n;
		List ops = currentDirective.getOperands();
		for (int count = 0; count < ops.size(); count++)
			{
				Operand op = (Operand)ops.get(count);
				if (op instanceof ImmediateOperand) 
					{
						n = ((ImmediateOperand)op).getValue();
						if (data_mode)
							{
								memoryManager.setLocation(NEXT_DATA_ADDRESS,n);
								verboseStream.println("[Assembler] Assembled "+currentDirective+" ( "+n+" ) + to location "+NEXT_DATA_ADDRESS);							
								NEXT_DATA_ADDRESS=NEXT_DATA_ADDRESS+4;
							}
						else
							{
								memoryManager.setLocation(NEXT_TEXT_ADDRESS,n);
								verboseStream.println("[Assembler] Assembled "+currentDirective+" ( "+n+" ) + to location "+NEXT_TEXT_ADDRESS);							
								NEXT_TEXT_ADDRESS= NEXT_TEXT_ADDRESS+4;
							}
					}
				else throw new AssemblerException("[Error A12] Invalid argument for .word",lineNumber,currentDirective);
			}
	}

	private void handleAscii(Instruction currentDirective, int lineNumber) throws AssemblerException
	{
	// NOT REQUIRED FOR COMPILER LAB
		String translatedOperand;
		// data section, datadeclaration mode 
		if (data_mode) {
		} else {
			// ascii defined not in data mode (i.e. after a .text)
			throw new AssemblerException("[Error A13] .ascii found not in .data section",lineNumber,currentDirective);
		}
	}
	
	private void handleByte(Instruction currentDirective, int lineNumber) throws AssemblerException
	{
	// NOT REQUIRED FOR COMPILER LAB
		if (data_mode) {
			//store successive operands in memory @ next_data_address, increment appropriate amount, already stored label address in symbol table & null terminate it if has name
			// once finished turn off data declaration mode
			
		} else {
			throw new AssemblerException("[Error A14] .byte found not in .data section",lineNumber,currentDirective);
		}
	}

	
	private void handleHalf(Instruction currentDirective, int lineNumber) throws AssemblerException
	{
	// NOT REQUIRED FOR COMPILER LAB
		if (data_mode) {
			//store successive 16 bit nums in memory halfwords @ next_data_address, increment appropriate amount, already stored label address in symbol table & null terminate it if has name
			// once finished turn off data declaration mode
		} else {
			throw new AssemblerException("[Error A15] .half found not in .data section",lineNumber,currentDirective);
		}
	}
	

	private void handleDouble(Instruction currentDirective, int lineNumber) { /* NOWAY */  }
	private void handleFloat(Instruction currentDirective, int lineNumber) { /* NOWAY */ }
	private void handleAlign(Instruction currentDirective, int lineNumber) {/*NOWAY*/}
	private void handleExtern(Instruction currentDirective, int lineNumber) {/*NOWAY*/}
	private void handleGlobl(Instruction currentDirective, int lineNumber) {/*NOWAY*/}
	private void handleKdata(Instruction currentDirective, int lineNumber) {/*NOWAY*/}
	private void handleKtext(Instruction currentDirective, int lineNumber) {/*NOWAY*/}
	private void handleSet(Instruction currentDirective, int lineNumber) {/*NOWAY*/}	
}
