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
 * @author jkm01
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
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
