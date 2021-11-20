/*
 * Created on 11-Nov-2003
 *
 */
package yams.GUI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author ss401
 * This is a class with a static method to obtain the names of all registers which
 * exist in the system. It reads the data contained in the registers.xml file, currently located in the 
 * project root folder (if anyone knows how to make it work in a more local directory,
 * let me know!).
 */
public class ValidRegisters {

	static String[] regs = getValidRegisters();
	static final String XMLFILELOCATION = "registers.xml";

	/**
	 * Returns an array of register names, reading from file registers.xml
	 * @return
	 */
	static String[] getValidRegisters() {
		Node n = getRootNode();
		List tempRegs = new ArrayList();

		NodeList l = n.getChildNodes();

		for (int i = 1; i < l.getLength(); i+=2) {
			Node curr = l.item(i);
			String regName = curr.getFirstChild().getNodeValue();
			tempRegs.add(regName);
			
		}

		String[] regsArr = new String[tempRegs.size()];
		regsArr = (String[]) tempRegs.toArray(regsArr);
		return regsArr;
	}

	static Node getRootNode() {
		DocumentBuilderFactory factory;
		DocumentBuilder builder;
		Document document;
		Node root = null;
		try {
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			document = builder.parse(new File(XMLFILELOCATION));
			root = document.getFirstChild();

		}
		catch (Exception e) {
			System.out.println("Couldn't read xml document." + e);
			System.exit(-1);
		}
		return root;
	}
}
