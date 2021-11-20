/*
 * Created on 03-Dec-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package yams.assembler;

import yams.processor.*;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * This component is designed to read from the “Instruction_file.xml? instruction repository, 
 * by parsing through an internal representation of the file at runtime. It searches for 
 * expected tags and builds up a table of data on the instructions stored within the repository 
 * (table is InstructionTable object).
 * 
 * @author jkm01
 */
public class AssemblerXMLHandler {

	private static String INSTRUCTIONS_TAG = "Instructions";
	private static String INSTRUCTION_TAG = "Instruction";
	private static String INSTRUCTION_TYPE_TAG = "Type";
	private static String CORE_TAG = "CoreMachineCode";
	private static String NAME_TAG = "Name";
	private static String MACHINE_CODE_REPRESENTATION_TAG = "MachineCodeRepresentations";	
	private static String OPERANDS_TYPE_TAG = "Type";
	private static String MACHINE_CODE_TAG = "MachineCode";
	private static String OPERANDS_NUMBER_TAG = "Number";
	private static String OPERANDS_CODING_TAG = "OperandsCoding";
	private static String OPERANDS_ENCODE_BITS_TAG = "EncodeBits";
	private static String OPERANDS_OUTPUT_BITS_TAG = "OutputBits";
	private static String OPERANDS_MASK_TAG = "Mask";
	private static String OPERANDS_OFFSET_MODE_TAG = "OffSetMode";
	private static String OPERANDS_TAG = "Operands";
	private static String OPERAND_TAG = "Op";
	private static String REPRESENTATION_TAG = "Representation";


	
	/**
	 * 
	 * This public method is called at creation by the Assembler to begin a single parse of the XML file and create the
	 * Instruction Table and its required substructures to represent all of the details in the "Instruction_file.xml"
	 * <BR><BR>
	 * It takes an XML File Path to the Instruction Repository, an empty InstructionTable and a reference to the Statistics Manager
	 * so that this can also be updated with the information retrieved from the file.
	 * <BR><BR>
	 * This method uses a try / catch statement to DOM parse the XML file and then call a series of handlers provided
	 * in the other private methods within the same class.
	 * <BR><BR>
	 * @param xmlFilePath
	 * @param instructionTable
	 * @param sm
	 */
	public static void LoadTableFromXML(String xmlFilePath, InstructionTable instructionTable, StatisticsManagerInterface sm)
	{
		// take the xml file path and parse through the xml loading appropriate data into respective tables
		
		DocumentBuilderFactory factory =
			DocumentBuilderFactory.newInstance();
		//factory.setValidating(true);
		//factory.setNamespaceAware(true);
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			yams.util.FileReader fr = new yams.util.FileReader();
			Document document = builder.parse(fr.readFile(xmlFilePath));

			NodeList children = document.getChildNodes();

			for (int i = 0; i <children.getLength(); i++)
			{
				Node currentNode =children.item(i);
//				System.out.println(i+" "+currentNode.getNodeName() + " " + currentNode.getNodeType() + " " +currentNode.getNodeValue());
				if ((currentNode.getNodeName()).equals(INSTRUCTIONS_TAG))
				{
					handleInstructions(instructionTable, sm, currentNode);
				}
			}
			instructionTable.printTable();

		} catch (SAXException sxe) {
			// Error generated during parsing)
			Exception  x = sxe;
			if (sxe.getException() != null)
			x = sxe.getException();
			x.printStackTrace();

		} catch (ParserConfigurationException pce) {
			// Parser with specified options can't be built
			pce.printStackTrace();

		} catch (IOException ioe) {
			// I/O error
			ioe.printStackTrace();
		} catch (AssemblerException ae) {
			ae.printStackTrace();
		}	
		
	}

	/**
	 * This handler iterates through the current node level looking for Instruction tags, and calling an Instruction handler when it finds them.
	 * 
	 * @param instructionTable
	 * @param sm
	 * @param currentNode	The current node that has been navigated to so far.
	 * @throws AssemblerException
	 */
	private static void handleInstructions(InstructionTable instructionTable, StatisticsManagerInterface sm, Node currentNode) throws AssemblerException
	{
//		System.out.println("Instructions");
		NodeList instructionNodes = currentNode.getChildNodes();
		for (int i = 0; i<instructionNodes.getLength(); i++)
		{
			if (instructionNodes.item(i).getNodeName().equals(INSTRUCTION_TAG))
				{
					InstructionTableRow currentRow = new InstructionTableRow();
					if (handleInstruction(instructionTable, sm, instructionNodes.item(i), currentRow))
					{
						instructionTable.addInstruction(currentRow.getName(),currentRow);
					}
				}
		}
	}

	/**
	 * This handler iterates through the current node level looking for MachineCodeRepresentation tags, and calling an Representations handler  to deal with the many representations when it finds them.

	 * @param instructionTable
	 * @param sm
	 * @param currentNode The current node level being examined
	 * @param currentRow The current row in the InstructionTable entry that can have Representations added to
	 * @return true if the current instruction tag contains a name for the instruction
	 * @throws AssemblerException
	 */
	private static boolean handleInstruction(InstructionTable instructionTable, StatisticsManagerInterface sm, Node currentNode, InstructionTableRow currentRow) throws AssemblerException
	{
//		System.out.println("Instruction");
		NodeList instructionNodes = currentNode.getChildNodes();
		String instructionName =getChildValue(NAME_TAG,currentNode);
		if (instructionName==null) return false;
		String instructionType =getChildValue(INSTRUCTION_TYPE_TAG,currentNode);		
		String coreCode =getChildValue(CORE_TAG,currentNode);
		currentRow.setInstructionName(instructionName);
		currentRow.setInstructionType(instructionType);
		currentRow.setCoreMachineCode(coreCode);
		
//		System.out.println(instructionName);
		sm.addInstruction(instructionName.toUpperCase());

		for (int i = 0; i<instructionNodes.getLength(); i++)
		{
			if (instructionNodes.item(i).getNodeName().equals(MACHINE_CODE_REPRESENTATION_TAG))
				{
					handleMachineCodeRepresentation(instructionTable,instructionNodes.item(i),currentRow);
				}
		}
		return true;
	}
	
	/**
	 * 
	 * Handler to search for Representation tags at the current node level, and pass on functionality to another handler.
	 * 
	 * @param instructionTable
	 * @param currentNode
	 * @param currentRow
	 */
	private static void handleMachineCodeRepresentation(InstructionTable instructionTable, Node currentNode, InstructionTableRow currentRow)
	{
//		System.out.println("MachineCodeRepresentations");

		NodeList instructionNodes = currentNode.getChildNodes();
		for (int i = 0; i<instructionNodes.getLength(); i++)
		{
			if (instructionNodes.item(i).getNodeName().equals(REPRESENTATION_TAG))
				{
					Representation currentRepresentation = new Representation();
					handleRepresentation(instructionTable,instructionNodes.item(i),currentRepresentation);
					currentRow.addRepresentation(currentRepresentation.getEncoding(),currentRepresentation);
				}
		}
	}

	/**
	 * Handler to search for Operands tags at the current node level, and pass on functionality to another handler.
	 * @param instructionTable
	 * @param currentNode
	 * @param currentRepresentation The current representation within the InstructionTable for this current instruction
	 */
	private static void handleRepresentation(InstructionTable instructionTable, Node currentNode, Representation currentRepresentation)
	{
//		System.out.println("Representation");

		NodeList instructionNodes = currentNode.getChildNodes();
		String operandsCoding =getChildValue(OPERANDS_CODING_TAG,currentNode);
		String machineCode=getChildValue(MACHINE_CODE_TAG,currentNode);
		currentRepresentation.setOperandsEncoding(operandsCoding);
		currentRepresentation.setMachineCode(machineCode);
		for (int i = 0; i<instructionNodes.getLength(); i++)
		{
			if (instructionNodes.item(i).getNodeName().equals(OPERANDS_TAG))
				{
					handleOperands(instructionTable,instructionNodes.item(i),currentRepresentation);
				}
		}
	}
	
	/**
	 * Handler to search for Operand tags at the current node level, and fill in OperandDetails objects with the correct data from the subtags below it, before adding this to the
	 * InstructionTable for the correct representation.

	 * @param instructionTable
	 * @param currentNode
	 * @param currentRepresentation
	 */
	private static void handleOperands(InstructionTable instructionTable, Node currentNode, Representation currentRepresentation)
	{
//		System.out.println("Operands");

		NodeList instructionNodes = currentNode.getChildNodes();
		for (int i = 0; i<instructionNodes.getLength(); i++)
		{
			if (instructionNodes.item(i).getNodeName().equals(OPERAND_TAG))
				{	
					
//					System.out.println("Operand");					

					int number = Integer.parseInt(getChildValue(OPERANDS_NUMBER_TAG,instructionNodes.item(i)));
//					System.out.println("Number"+number);
					String type =getChildValue(OPERANDS_TYPE_TAG,instructionNodes.item(i));
//					System.out.println("1"+type);
					String mask =getChildValue(OPERANDS_MASK_TAG,instructionNodes.item(i));
//					System.out.println("2"+mask);
					int encode =Integer.parseInt(getChildValue(OPERANDS_ENCODE_BITS_TAG,instructionNodes.item(i)));
//					System.out.println("3"+encode);
					int output =Integer.parseInt(getChildValue(OPERANDS_OUTPUT_BITS_TAG,instructionNodes.item(i)));
//					System.out.println("4"+output);
					int offset =Integer.parseInt(getChildValue(OPERANDS_OFFSET_MODE_TAG,instructionNodes.item(i)));
//					System.out.println("5"+offset);
					OperandDetails currentOperand = new OperandDetails(number, type,mask,encode,output,offset);

					currentRepresentation.addOperand(currentOperand.getNumber(),currentOperand);
				}
		}
	}

	/**
	 * Given a tag name and a current node, this method will retrieve the VALUE contained within the XML TAG NAME and return it to the user.
	 * <BR><BR>
	 * The TAG NAME must be a chile node of the current node, else no return value will be given.
	 * 
	 * @param name	Required Tag Name
	 * @param node	Current Node
	 * @return A string returned with the value of the requested tag under the given node
	 */
	private static String getChildValue(String name, Node node)
	{
		try
		{

			NodeList childNodes = node.getChildNodes();
			int i = 0;
			while (i<childNodes.getLength() && !childNodes.item(i).getNodeName().equals(name))
			{
				i++;
			}
			if (i<childNodes.getLength())
			{
				return childNodes.item(i).getFirstChild().getNodeValue();
			}
			else
			{
				return null;
			}
		}
		catch (NullPointerException e) {return null;}
	}
}
