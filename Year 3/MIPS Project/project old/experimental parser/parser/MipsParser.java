package parser;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.*;

public class MipsParser{
    // Global value so it can be ref'd by the tree-adapter

	TreeMap instructionMap = new TreeMap();
	Errors errors = new Errors();
	RegisterOperandType registers = new RegisterOperandType(errors);
	NumericOperandType numeric = new NumericOperandType(errors);
	AlphaNumericOperandType alphaNumeric = new AlphaNumericOperandType(errors);

	public static void main(String argv[])
	{
		if (argv.length != 1) {
			System.err.println("Usage: java DomEcho filename");
			System.exit(1);
		}
		MipsParser aParser = new MipsParser(argv[0]);
	}

	public MipsParser(String xmlfilelocation)
	{
		DocumentBuilderFactory factory =
			DocumentBuilderFactory.newInstance();
		//factory.setValidating(true);
		//factory.setNamespaceAware(true);
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse( new File(xmlfilelocation) );

			Node MIPS_root_Node = document.getFirstChild();
			//System.out.println(MIPS_root_Node.getNodeType());
			System.out.println(MIPS_root_Node.getNodeName());

			NodeList MIPS_children = MIPS_root_Node.getChildNodes();

			// Read in the permitted instructions, registers and ranges
			for (int i = 0; i <MIPS_children.getLength(); i++)
			{
				Node currentNode = MIPS_children.item(i);
				//System.out.println(i+" "+currentNode.getNodeName() + " " +
				//                   currentNode.getNodeType() + " " +
				//                   currentNode);

				if ((currentNode.getNodeName()).equals("Instructions"))
				{
					parseInstructions(currentNode);
				}
				else if ((currentNode.getNodeName()).equals("Registers"))
				{
     					parseRegisters(currentNode);
					//System.out.println("Current Node: " + currentNode);
				}
				else if ((currentNode.getNodeName()).equals("Ranges"))
				{
					parseRanges(currentNode);
				}
			}
			System.out.println(instructionMap.keySet());

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
		}
		validInstruction(new MIPSInstruction("lw $a1 $a2 $a4",0,"lw","$a1","$a2","$a4"));
		//validInstruction(new MIPSInstruction("sw $a1 $a2 $a3",1,"sw","$a1","$a2","$a3"));
		//validInstruction(new MIPSInstruction("lui $a1 $a2 $a4",2,"lui","$a1","$storelocation1","storelocation2"));
		//validInstruction(new MIPSInstruction("lw $a1 $a5 $a4",3,"lw","$a1","$a5","$a4"));
		System.out.println("Parse Errors");
		errors.printErrors();
		//System.out.println("Valid Registers: " + registers);

	}

	public boolean validInstruction(MIPSInstruction m)
	{
		if (!instructionMap.containsKey(m.getInstruction()))
		{
			errors.addError(m,m.getInstruction(),"Invalid Instruction Name");
			return false;
		}
		else return ((Instruction)instructionMap.get(m.getInstruction())).isValid(m);
	}

	public void addInstruction(Instruction i)
	{
		instructionMap.put(i.getInstruction(),i);
	}

	public void parseInstructions(Node current)
	{
		NodeList instructionList = current.getChildNodes();
		for (int i = 0; i<instructionList.getLength(); i++)
		{
			Node thisNode = instructionList.item(i);
			if ((thisNode.getNodeName()).equals("Instr"))
			{
				parseInstruction(thisNode);
			}
		}
	}

	public void parseRanges(Node current)
	{
		NodeList instructionList = current.getChildNodes();
		for (int i = 0; i<instructionList.getLength(); i++)
		{
			Node thisNode = instructionList.item(i);
			if ((thisNode.getNodeName()).equals("Range"))
			{
				numeric.addValidInstance(thisNode);
			}
		}
	}


	public void parseInstruction(Node current)
	{
		String instruction = "Empty";
		OperandType operand_1 = null;
		OperandType operand_2 = null;
		OperandType operand_3 = null;
		NodeList instructionList = current.getChildNodes();
		for (int i = 0; i<instructionList.getLength(); i++)
		{
			Node thisNode = instructionList.item(i);
			if ((thisNode.getNodeName()).equals("Code"))
			{
				instruction = thisNode.getFirstChild().getNodeValue();
			}
				else if ((thisNode.getNodeName()).equals("Operand_1"))
			{
				operand_1 = getOperandType(thisNode.getFirstChild().getNodeValue());
			}
			else if ((thisNode.getNodeName()).equals("Operand_2"))
			{
				operand_2 = getOperandType(thisNode.getFirstChild().getNodeValue());
			}
			else if ((thisNode.getNodeName()).equals("Operand_3"))
			{
				operand_3 = getOperandType(thisNode.getFirstChild().getNodeValue());
			}
		}
		addInstruction(new Instruction(instruction,operand_1,operand_2,operand_3));
	}

	public OperandType getOperandType(String groupName)
	{
		if (groupName.equals("Register")) {return registers;}
		else if (groupName.equals("Numeric")) {return numeric;}
		else if (groupName.equals("AlphaNumeric")) {return alphaNumeric;}
		else return null;
	}

	public void parseRegisters(Node a)
	{
		System.out.println(a.getChildNodes().item(1).getChildNodes().item(1).getFirstChild().getNodeValue());//


		NodeList A = a.getChildNodes();

		System.out.println(A.getLength());

		for(int i = 1;i<A.getLength();i++ )
		{
			Node b = A.item(i);

			//NodeList B = b.getChildNodes();
			/*------------------------ CONTINUE WORKING HERE --------------------------------------*/
			if(b.getNodeName().equals("Zero")){
			}
			else if("Call_arguments"){
			}
			else if("Call_results"){
			}
			else if("Caller_temp"){
			}
			else if("Callee_temp"){
			}
			else if("Stack_pointer"){
			}
			else if("Frame_pointer"){
			}
			else if("Return_address"){
			}

			/*for(int j=1;j<B.getLength();j++)
			{
				Node c = B.item(j);
				System.out.println(c.getFirstChild().getNodeValue());

				if ((c.getNodeName()).equals("Register"))
				{
					registers.addValidInstance(c.getFirstChild().getNodeValue());
				}
			}*/
			System.out.println(b.getNodeName());

		}
		/*NodeList A = a.getChildNodes();
		for (int j = 0; j<A.getLength(); j++)
		{
			NodeList B = registerCategoryList.item(j);
			for(int i = 0; i<registerCategoryList.getLength(); i++)
			{
				Node thisNode = registerCategoryList.item(i);
				if ((thisNode.getNodeName()).equals("Register"))
				{
					registers.addValidInstance(thisNode.getFirstChild().getNodeValue());
				}
			}
		}*/
	}
}
