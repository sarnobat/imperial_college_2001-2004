/*
 * Created on 03-Dec-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package yams.assembler;

import java.util.List;
import java.util.ArrayList;
import yams.parser.*;

/**
 * This component is designed to take a provided set of operands for a given instruction, along with its 
 * core machine code representation (without any operand values filled in). It then performs the correct 
 * translation of the operands as required for the specific instruction and substitutes them into the 
 * machine code, leaving us with a machine word that can be written to memory ready for execution.
 *<BR><BR>
 * It operates on ONE machine word at a time from the instruction, carrying out substitution to generate the final
 * code.
 *
 * @author jkm01
 */
public class OperandHandler {

	private SymbolTable symbolTable;
	private ToBeDoneTable toBeDoneTable; 
	private InstructionTable instructionTable;
	
	private static int FIXED_BLOCK_ADDRESS=0x10008000;
	private static int BOUNDARY_16_32 = 32767; 

	// passed state variables
	private Instruction currentInstruction;
	private String currentInstructionName;
	private int lineNumber;
	private int currentLineAddress;
	private String machineWord;
	private Representation currentRepresentation;

	// handler state variables
	private String opsCoding;
	private int encodeBits;
	private int outputNumberOfBits;
	private String mask;
	private int offSetMode;
	private String operandType;
	private List operandList;

	//handler variables
	private ArrayList translatedList;
	private String rsRegister;
	private boolean symbolTableError;
	
	public OperandHandler(SymbolTable st, ToBeDoneTable tbdt, InstructionTable instrT)
	{
		//checked
		symbolTable = st;
		toBeDoneTable = tbdt;
		instructionTable = instrT;
	}


	/**
	 * Recalculates the values of the operand list within the class, adding them to the translateList structure.
	 * <BR><BR>These operands are then available for substitution into the final machine word.
	 * 
	 * @throws AssemblerException
	 */
	public void reCalculateOperandValues() throws AssemblerException
	{
		char searchOperandType;
		OperandDetails currentOperand;
		for (int count =0; count<operandList.size(); count++)
			{
				searchOperandType = opsCoding.charAt(count);
				
				if (currentRepresentation.isOperand(count+1,""+searchOperandType))
					
					{
						//index operand by number
						currentOperand = currentRepresentation.getOperand(count+1,""+searchOperandType);
						encodeBits = currentOperand.getEncodeBits();
						outputNumberOfBits = currentOperand.getOutputBits();
						mask = currentOperand.getMask();
						offSetMode = currentOperand.getOffset();
						operandType = currentOperand.getType();
					}
				handleOperand((Operand)operandList.get(count));		// handle the operands and encode the value
			}
		
	}
	
	public boolean isSymbolTableError()
	{
		return symbolTableError;
	}


	public void resetHandler()
	{
		// to deal with a new instruction
		symbolTableError=false;
	}

	/**
	 * <BR>
	 * INPUT:	
	 * <BR><BR>
	 * Receives a combination of operands (see below).<BR><BR>
	 * The final two operands imply redundant reference to certain objects, but note that they are used to highlight any errors encountered with the whole line.
	 * <BR><BR>NOTE: “uncoded? refers to machine code that hasn’t had the correct values for its operands substituted yet e.g. 001000bbbbbaaaaacccccccccccccccc
	 * <BR><BR>NOTE: Some instructions within the supported set can be multiple words in length, and the Assembler essentially will pass the OperandHandler a SINGLE word from this representation, and the operands themselves ready to be substituted. The other variables are used to setup the handler to translate these operands correctly pre-substitution e.g. if one operand is a label, then the OperandHandler must establish whether the offset should be calculated from the current address or from the FIXED_LINE_ADDRESS (see addressing modes section later in this report).
	 * <BR><BR>
	 * HANDLING:
	 * <BR><BR>
	 * The handler must encode the provided operands, and substitute their translated values into the provided “uncoded? machine word.
	 * <BR><BR>
	 * OUTPUT:
	 * <BR><BR>
	 * Returns a machine word which is fully coded, with all operands successfully substituted into their correct positions, ready to be written to memory. Must also provide an indication of whether there has been some error in referencing the SymbolTable (referencing label not yet encountered in the MIPS program).
	 * <BR><BR>
	 * 
	 * @param word A single 32 bit bitstring, which is an “uncoded? machine code representation of some of the instruction which is required to be translated.
	 * @param address An integer representing the current text address (NEXT_TEXT_ADDRESS from Assembler).
	 * @param representation Current Representation structure for given instruction and operand combination.
	 * @param opList A list structure containing the Operand objects retrieved from the current Instruction representation (passed from parser).
	 * @param operandsCoding A coded representation of the types of the operands in the list above.
	 * @param line The current line number of the Instruction
	 * @param current The full instruction itself from the original LineList AST.
	 * @return Encoded string with operands included
	 * @throws AssemblerException
	 */
	public String encodeWord (String word,
												int address,
												Representation representation,
												List opList,
												String operandsCoding,
												int line,
												Instruction current) throws AssemblerException
			{
				currentLineAddress = address;
				machineWord =  word;
				currentInstruction = current;
				lineNumber = line;
				currentRepresentation = representation;
				opsCoding = operandsCoding;
				operandList = opList;
				
				translatedList = new ArrayList();
				rsRegister = null;
				
				reCalculateOperandValues();
				
				for (int count = 0; count<translatedList.size(); count++)
					{
						if (!(translatedList.get(count).equals("LABEL_ERROR")))
							{
								machineWord = replaceOperand((String)translatedList.get(count),machineWord,encodeMarker(count));
							}
						//replace all operands in the word 
					}
				if (rsRegister!=null)
					{
						machineWord = replaceOperand(rsRegister,machineWord,'z');
					}
				return machineWord;
			}
			
	/**
	 * 
	 * Distinguishes between four main types of operand (from parser point-of-view): Registers / Immediates / Labels / Addresses
	 * <BR><BR>Passes on Operand to other handlers to deal with appropriately.
	 * 
	 * @param currentOperand
	 * @throws AssemblerException
	 */
	public void handleOperand(Operand currentOperand) throws AssemblerException
	{
		//checked
		if (currentOperand instanceof RegisterOperand)
			{handleGeneralRegisterOperand((RegisterOperand)currentOperand);}
		else if (currentOperand instanceof ImmediateOperand)
			{handleImmediateOperand((ImmediateOperand)currentOperand);}
		else if (currentOperand instanceof LabelOperand)
			{handleGeneralLabelOperand((LabelOperand)currentOperand);}
		else if (currentOperand instanceof AddressingOperand)
			{handleAddressingOperand((AddressingOperand)currentOperand);}
		else throw new AssemblerException("[Error A20] Current operand ("+currentOperand.toString()+") is not a valid operand, error in parser",lineNumber,currentInstruction);
	}

	/**
	 * 
	 * Formats the given inputString by applying the bit mask variable (corresponding "set" bits in given string 
	 * are included in output, "unset" not included). Additionally it checks whether the resulting string is the correct
	 * length ready for subsitution into the machine word.
	 * 
	 * @param inputString
	 * @return returns correctly formatted string
	 * @throws AssemblerException
	 */
	private String formatString(String inputString) throws AssemblerException
	{
		//checked
		String outputString="";
		for (int count =0; count<inputString.length(); count++)
			{
				if (mask.charAt(count)=='1') {outputString+=inputString.charAt(count);}
			}
		if (outputNumberOfBits==outputString.length()) return outputString;
		else throw new AssemblerException("[Error A21] Required output length ("+outputString.length()+") and bit mask ("+outputNumberOfBits+") contradict one another for current instruction XML representation",lineNumber,currentInstruction);
	}
	
	/**
	 * 
	 * Handles an immediate operand, distinguising between 16 / 32 bit immediates and adding the correct
	 * translation to the translatedList as appropriate.
	 * 
	 * @param currentOperand
	 * @throws AssemblerException
	 */
	private void handleImmediateOperand(ImmediateOperand currentOperand) throws AssemblerException
	{
		//checked
		int imm = currentOperand.getValue();
		String outputString=""+intToBinaryString(imm,encodeBits);
		if (outputNumberOfBits<32)
			{
				translatedList.add(formatString(outputString));
			}
		else if (outputNumberOfBits==32)
			{
				// if 32 bit number, break into separate components
				translatedList.add(formatString(outputString).substring(16,32));
				translatedList.add(formatString(outputString).substring(0,16));
			}
	}
		
	/**
	 * 
	 * Handler adds the encoded representation of a given Register operand to the translated list ready for substitution
	 * 
	 * @param currentOperand
	 * @throws AssemblerException
	 */
	private void handleGeneralRegisterOperand(RegisterOperand currentOperand) throws AssemblerException
	{
		//checked
		String register = currentOperand.getValue();
		translatedList.add(encodeRegister(register));						
	}
	
	/**
	 * 
	 * Handles a general label operand (not an address operand), taking into account symbol table errors and offset modes, and adding
	 * the computed values to the translatedList.
	 * 
	 * @param currentOperand
	 * @throws AssemblerException
	 */
	private void handleGeneralLabelOperand(LabelOperand currentOperand) throws AssemblerException
	{
		//checked
		String label = currentOperand.getLabel();
		int this_address;
		if (!symbolTable.containsLabel(label))
			{
				translatedList.add("LABEL_ERROR");
				symbolTableError=true;
			}
		else
			{
				if (offSetMode==0)			{this_address=currentLineAddress;}
				else if (offSetMode==1) 	{this_address=FIXED_BLOCK_ADDRESS;}
				else if (offSetMode==2)		{this_address=0;}
				else						throw new AssemblerException("[Error A22] Provided offset mode ("+offSetMode+") for Label Addressingcurrent instruction is incorrect",lineNumber,currentInstruction);
				String address = ""+intToBinaryString(symbolTable.returnAddress(label)-this_address,encodeBits);
				translatedList.add(formatString(address));
			}
	}

	/**
	 * 
	 * Distinguishes between the different types of AddressingOperands : Immediate/Immediate_Register/Label/Label_Plus_Immediate/Label_Plus_Immediate_Register/Register and passes
	 * the given operand down to separate handlers to deal with appropriately.
	 * <BR><BR>
	 * These handlers can be found below, and deal generically with the operand according to the mode settings that have been set e.g. OffsetMode, encode bits etc
	 * 
	 * @param currentOperand
	 * @throws AssemblerException
	 */
	private void handleAddressingOperand(AddressingOperand currentOperand) throws AssemblerException
	{
		//checked
		switch (currentOperand.getType())
		{
			case AddressingOperand.IMMEDIATE 									:	{handleAddressingImmediate(currentOperand);break;}
			case AddressingOperand.IMMEDIATE_REGISTER 							: 	{handleAddressingImmediateRegister(currentOperand);break;}
			case AddressingOperand.LABEL										: 	{handleAddressingLabel(currentOperand);break;}
			case AddressingOperand.LABEL_PLUS_IMMEDIATE							: 	{handleAddressingLabelImmediate(currentOperand);break;}
			case AddressingOperand.LABEL_PLUS_IMMEDIATE_REGISTER 				: 	{handleAddressingLabelImmediateRegister(currentOperand);break;}
			case AddressingOperand.REGISTER						 				: 	{handleAddressingRegister(currentOperand);break;}
			default																:	{throw new AssemblerException("[Error A23] Addressing operand has unrecognised type ("+currentOperand.getType()+") Operand is not recognised",lineNumber,currentInstruction);} //0
		}
	}
	
	private void handleAddressingImmediate(AddressingOperand currentOperand) throws AssemblerException
	{
		//checked
		int imm = currentOperand.getImmediate();
		if (offSetMode==2)
			{
				translatedList.add(formatString(""+intToBinaryString(imm,encodeBits)).substring(16,32));
				translatedList.add(formatString(""+intToBinaryString(imm,encodeBits)).substring(0,16));
			}
		else if (offSetMode==1)
			{
				translatedList.add(formatString(""+intToBinaryString(imm-FIXED_BLOCK_ADDRESS,encodeBits)));				
			}
		else if (offSetMode==0)
			{
				translatedList.add(formatString(""+intToBinaryString(imm-currentLineAddress,encodeBits)));								
			}
		else throw new AssemblerException("[Error A24] Offset mode ("+offSetMode+") is invalid for Address(Immediate) Addressing for current instruction",lineNumber,currentInstruction);
	}

	private void handleAddressingRegister(AddressingOperand currentOperand) throws AssemblerException
	{
		//checked
		String register = currentOperand.getRegister();
		if (offSetMode==2)
			{
				translatedList.add(encodeRegister(register));
			}
		else if (offSetMode==1 || offSetMode==0)
			{
				rsRegister = encodeRegister(register);
			}
		else throw new AssemblerException("[Error A25] Offset mode ("+offSetMode+") is invalid for Address(Register) Addressing for current instruction",lineNumber,currentInstruction);
	}
	
	private void handleAddressingImmediateRegister(AddressingOperand currentOperand) throws AssemblerException
	{
		//checked
		int imm = currentOperand.getImmediate();
		String register = currentOperand.getRegister();
		if (offSetMode==2)
			{
				translatedList.add(formatString(""+intToBinaryString(imm,encodeBits)).substring(16,32));
				translatedList.add(formatString(""+intToBinaryString(imm,encodeBits)).substring(0,16));
				translatedList.add(encodeRegister(register));
			}
		else if (offSetMode==1 || offSetMode==0)
			{
				translatedList.add(formatString(""+intToBinaryString(imm,encodeBits)));				
				rsRegister = encodeRegister(register);
			}
		else throw new AssemblerException("[Error A26] Offset mode ("+offSetMode+")  is invalid for Address(RegisterImmediate) Addressing for current instruction",lineNumber,currentInstruction);
	}
	
	private void handleAddressingLabel(AddressingOperand currentOperand) throws AssemblerException
	{
		//checked
		String label = currentOperand.getLabel();
		if (!symbolTable.containsLabel(label))
			{
				translatedList.add("LABEL_ERROR");
				symbolTableError=true;
			}
		else
			{
				if (offSetMode==2)
					{
						translatedList.add(formatString(""+intToBinaryString(symbolTable.returnAddress(label),encodeBits)).substring(16,32));
						translatedList.add(formatString(""+intToBinaryString(symbolTable.returnAddress(label),encodeBits)).substring(0,16));
					}
				else if (offSetMode==1)
					{
						translatedList.add(formatString(""+intToBinaryString(symbolTable.returnAddress(label)-FIXED_BLOCK_ADDRESS,encodeBits)));
					}
				else if (offSetMode==0)
					{
						translatedList.add(formatString(""+intToBinaryString(symbolTable.returnAddress(label),encodeBits)));
					}
				else throw new AssemblerException("[Error A27] Offset mode ("+offSetMode+")  is invalid for Address(Label) Addressing for current instruction",lineNumber,currentInstruction);
			}
	}
	
	private void handleAddressingLabelImmediate(AddressingOperand currentOperand) throws AssemblerException
	{
		//chceked
		String label = currentOperand.getLabel();
		int immediate = currentOperand.getImmediate();

		if (!symbolTable.containsLabel(label))
			{
				translatedList.add("LABEL_ERROR");
				symbolTableError=true;				
			}
		else
			{
				if (offSetMode==2)
					{
						translatedList.add(formatString(""+intToBinaryString(symbolTable.returnAddress(label)+immediate,encodeBits)).substring(16,32));
						translatedList.add(formatString(""+intToBinaryString(symbolTable.returnAddress(label)+immediate,encodeBits)).substring(0,16));
					}
				else if (offSetMode==1)
					{
						translatedList.add(formatString(""+intToBinaryString(symbolTable.returnAddress(label)+immediate-FIXED_BLOCK_ADDRESS,encodeBits)));
					}
				else if (offSetMode==0)
					{
						translatedList.add(formatString(""+intToBinaryString(symbolTable.returnAddress(label)+immediate,encodeBits)));
					}
				else throw new AssemblerException("[Error A28] Offset mode ("+offSetMode+")  is invalid for Address(LabelImmediate) Addressing for current instruction",lineNumber,currentInstruction);
			}
	}
	
	private void handleAddressingLabelImmediateRegister(AddressingOperand currentOperand) throws AssemblerException
	{
		//checked
		String label = currentOperand.getLabel();
		int immediate = currentOperand.getImmediate();
		String register = currentOperand.getRegister();

		if (!symbolTable.containsLabel(label))
			{
				translatedList.add("LABEL_ERROR");
				symbolTableError=true;				
			}
		else
			{
				if (offSetMode==2)
					{
						translatedList.add(formatString(""+intToBinaryString(symbolTable.returnAddress(label)+immediate,encodeBits)).substring(16,32));
						translatedList.add(formatString(""+intToBinaryString(symbolTable.returnAddress(label)+immediate,encodeBits)).substring(0,16));
					}
				else if (offSetMode==1)
					{
						translatedList.add(formatString(""+intToBinaryString(symbolTable.returnAddress(label)-FIXED_BLOCK_ADDRESS+immediate,encodeBits)));														
					}
				else if (offSetMode==0)
					{
						translatedList.add(formatString(""+intToBinaryString(symbolTable.returnAddress(label)-currentLineAddress+immediate,encodeBits)));														
					}
				else throw new AssemblerException("[Error A29] Offset mode ("+offSetMode+")  is invalid for Address(LabelImmediateRegister) Addressing for current instruction",lineNumber,currentInstruction);
				translatedList.add(encodeRegister(register));
			}
	}

	/**
	 * Given a register name, this method returns the machine code representation
	 * of any register available to the assembler and modelled in the processor.
	 * 
	 * @param registerName
	 * @return returns the binary representation of the register
	 * @throws AssemblerException
	 */
	private String encodeRegister(String registerName) throws AssemblerException
	{		
		if 			(registerName.equals("$zero")) 	{return "00000";}
		else if 	(registerName.equals("$at")) 		{return "00001";}
		else if 	(registerName.equals("$v0")) 		{return "00010";}
		else if 	(registerName.equals("$v1"))	 	{return "00011";}
		else if 	(registerName.equals("$a0")) 		{return "00100";}
		else if 	(registerName.equals("$a1")) 		{return "00101";}
		else if 	(registerName.equals("$a2")) 		{return "00110";}
		else if 	(registerName.equals("$a3"))	 	{return "00111";}
		else if 	(registerName.equals("$t0")) 		{return "01000";}
		else if 	(registerName.equals("$t1")) 		{return "01001";}
		else if 	(registerName.equals("$t2")) 		{return "01010";}
		else if 	(registerName.equals("$t3")) 		{return "01011";}
		else if 	(registerName.equals("$t4")) 		{return "01100";}
		else if 	(registerName.equals("$t5")) 		{return "01101";}
		else if 	(registerName.equals("$t6")) 		{return "01110";}
		else if 	(registerName.equals("$t7")) 		{return "01111";}
		else if 	(registerName.equals("$s0"))	 	{return "10000";}
		else if 	(registerName.equals("$s1")) 		{return "10001";}
		else if 	(registerName.equals("$s2")) 		{return "10010";}
		else if 	(registerName.equals("$s3")) 		{return "10011";}
		else if 	(registerName.equals("$s4")) 		{return "10100";}
		else if 	(registerName.equals("$s5")) 		{return "10101";}
		else if 	(registerName.equals("$s6")) 		{return "10110";}
		else if 	(registerName.equals("$s7")) 		{return "10111";}
		else if 	(registerName.equals("$t8")) 		{return "11000";}
		else if 	(registerName.equals("$t9"))		{return "11001";}
		else if 	(registerName.equals("$k0")) 		{return "11010";}
		else if 	(registerName.equals("$k1")) 		{return "11011";}
		else if 	(registerName.equals("$gp")) 		{return "11100";}
		else if 	(registerName.equals("$sp"))	 	{return "11101";}
		else if 	(registerName.equals("$fp")) 		{return "11110";}
		else if 	(registerName.equals("$ra")) 		{return "11111";}
		else
			{
				throw new AssemblerException("[Error A30] This ("+registerName+") is not a register -> this should have been picked up by parser",lineNumber,currentInstruction);
			}
	}
		
	/**
	 * 
	 * Given a translatedOperand (String of specific length), a machineWord (32 bits) and a character (representing
	 * which operand string we wish to replace e.g. a = first operand etc) this method substitutes the correct operand
	 * values into their positions within the machine word.
	 * 
	 * @param translatedOperand
	 * @param machineCode
	 * @param oprep
	 * @return returns string with operands replaced
	 */
	public String replaceOperand(String translatedOperand, String machineCode, char oprep)
	{
		
		int length = translatedOperand.length();
		String replaceString = "";
		for (int count = 0; count<length; count++)
		{
			replaceString = replaceString+oprep;
		}
		
		while (machineCode.indexOf(oprep)!=-1)
			{
				machineCode = machineCode.replaceFirst(replaceString,translatedOperand);
			}
		return machineCode;		
	}
	
	private char encodeMarker(int number) throws AssemblerException
	{
		switch (number)
			{
				case 0	:	{return 'a';}
				case 1	:	{return 'b';}
				case 2	:	{return 'c';}
				case 3	:	{return 'd';}
				case 4	:	{return 'e';}
				case 5 :	{return 'f';}
				case 6 :	{return 'g';}
				case 7 :	{return 'h';}
				case 8 :	{return 'i';}
				case 9 :	{return 'j';}
				default :	{throw new AssemblerException("[Error A31] Too many operands for an instruction",lineNumber,currentInstruction);}
			}
	}

	/**
	 * 
	 * This method transforms a decimal immediate into twos complement binary with accuracy definted by the numberOfBitsRequired parameter.
	 * It will thrw an error if the number cannot be represented in the given space e.g. if the number is too big 
	 * for a certain number of bits.
	 * 
	 * @param immediate
	 * @param numberOfBitsRequired
	 * @return binary string representation of int
	 * @throws AssemblerException
	 */
	public String intToBinaryString(int immediate,int numberOfBitsRequired) throws AssemblerException
	{
		String binaryString="";
		if (immediate<0)
			{
				binaryString = encodeToBinary(-(immediate),numberOfBitsRequired);
				if (binaryString.length()<numberOfBitsRequired) 
					{
						binaryString = padOutPositiveBinaryString(binaryString,numberOfBitsRequired);
						return toTwosComplement(binaryString);
					}
				else if (binaryString.length()==numberOfBitsRequired && binaryStringToInteger(binaryString.substring(1,numberOfBitsRequired))==0)
					{
						return toTwosComplement(binaryString);
					}
				else	throw new AssemblerException("[Error A32] This immediate ("+immediate+") cannot be represented as a twos complement number within current bounds ("+numberOfBitsRequired+")",lineNumber,currentInstruction);
			}
		else
			{
				binaryString = encodeToBinary(immediate,numberOfBitsRequired);
				if (binaryString.length()<numberOfBitsRequired) 
					{
						binaryString = padOutPositiveBinaryString(binaryString,numberOfBitsRequired);
						return binaryString;
					}
				else	throw new AssemblerException("[Error A33] This immediate ("+immediate+") cannot be represented as a twos complement number within current bounds ("+numberOfBitsRequired+")",lineNumber,currentInstruction);
			}
	}

	/**
	 * 
	 * Method used in translating an immediate to binary to convert any bitstring given into twos complement.
	 * It throws exceptions if the number is not a binary string.
	 * 
	 * @param positiveBinaryNumber
	 * @return returns Twos complement representation of string
	 * @throws AssemblerException
	 */
	public String toTwosComplement(String positiveBinaryNumber) throws AssemblerException
	{
		//copy up to and including first 1, then invert remainder
		int count = positiveBinaryNumber.length()-1;
		String newOutput = "";
		while (count>=0 && positiveBinaryNumber.charAt(count)=='0')
			{
				newOutput = "0"+newOutput;
				count--;
			}
		if (positiveBinaryNumber.charAt(count)=='1')
			{
				newOutput = "1"+newOutput;
				count--;
			}
		while (count>=0)
			{
				if (positiveBinaryNumber.charAt(count)=='1') {newOutput = "0"+newOutput;}
				else if (positiveBinaryNumber.charAt(count)=='0') {newOutput = "1"+newOutput;}
				else {throw new AssemblerException("[Error A34] The number ("+positiveBinaryNumber+") for some reason is not in binary",lineNumber,currentInstruction);}
				count--;
			}
		return newOutput;
	}
	
	/**
	 * 
	 * Encodes any deciaml immediate into its positive binary representation (modulus)
	 * 
	 * @param immediate
	 * @param numberOfBitsRequired
	 * @return returns positive binary representation of immediate
	 */
	public String encodeToBinary(int immediate,int numberOfBitsRequired)
	{
		int value = immediate;
		String output = "";
		while (value>=1)
			{
				output = (value%2)+output;
				value = value/2;
			}
		return output;
	}
	
	/**
	 * 
	 * Pads out the high end bits of a binary string till it fills the number of bits required (using twos complement rules: 1s used if negative, 0 if positive)
	 * 
	 * @param binaryString
	 * @param numberOfBitsRequired
	 * @return returns padded out string
	 * 
	 */
	public String padOutPositiveBinaryString(String binaryString,int numberOfBitsRequired)
	{
		while (binaryString.length()<numberOfBitsRequired)
			{
				binaryString = "0"+binaryString;
			}	
		return binaryString;
	}
	
	/**
	 * Converts any binary string to its integer value.
	 * 
	 * @param binaryString
	 * @return returns integer representation of binary string
	 * @throws AssemblerException
	 */
	public int binaryStringToInteger(String binaryString) throws AssemblerException
	{

		if (binaryString.charAt(0)=='1')
			{
				// is a negative number
				// convert back to positive number
				return -(binaryToPositiveInteger(toTwosComplement(binaryString)));
			}
		else
			{
				return binaryToPositiveInteger(binaryString);
			}

	}
	
	public int binaryToPositiveInteger(String binaryString)
	{
		int value = 0;
		int equivalent = 1;
		int count = binaryString.length()-1;
		while (count>=0)
		{
			if (binaryString.charAt(count)=='1') {value+=equivalent;}
			equivalent=equivalent*2;	
			count--;
		}
		return value;
	}
	
	public char getOperandCode(Operand op) throws AssemblerException
	{
		//checked
		if (op instanceof ImmediateOperand)
			{
				int number = BOUNDARY_16_32;
				int number2 = ((ImmediateOperand)op).getValue();
				if (((ImmediateOperand)op).getValue()<=BOUNDARY_16_32)		{return '0';}
				else																									{return 'a';}
			}
		else if (op instanceof RegisterOperand) 												{return '1';}
		else if (op instanceof LabelOperand) 													{return '2';}
		else if (op instanceof AddressingOperand)  							//3
			{
				switch (((AddressingOperand)op).getType())
				{
					case AddressingOperand.IMMEDIATE 						:	{return '4';}
					case AddressingOperand.IMMEDIATE_REGISTER 				: 	{return '5';}
					case AddressingOperand.LABEL							: 	{return '6';}
					case AddressingOperand.LABEL_PLUS_IMMEDIATE				: 	{return '7';}
					case AddressingOperand.LABEL_PLUS_IMMEDIATE_REGISTER	: 	
						{
							if (((AddressingOperand)op).getImmediate()<0) 
								{
									{return 'c';}																	//negative immediate thus register subbed
								}
							else
								{
									{return '8';}																	//positive immediate thus register added
								}
						}
					case AddressingOperand.REGISTER						 	: 	{return '9';}
					default													:	{ throw new AssemblerException("[Error A35] Addressing operand unrecognised type  Operand is not recognised",lineNumber,currentInstruction);}
				}
			}
		else throw new AssemblerException("[Error A36] Addressing operand has unrecognised type  Operand is not recognised",lineNumber,currentInstruction);
	}

	public String encodeOperandList(List anOperandList) throws AssemblerException
	{
		//checked		
		String coding="";
		for (int count=0; count<anOperandList.size(); count++)
			{
				coding+=""+getOperandCode((Operand)anOperandList.get(count));
			}
		if (anOperandList.size()==0) 		return "b";				//has no operands thus represented by b
		else 									 		return coding;
	}
}