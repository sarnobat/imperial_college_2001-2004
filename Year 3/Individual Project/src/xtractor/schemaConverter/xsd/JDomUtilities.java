package xtractor.schemaConverter.xsd;
import java.io.File;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.DOMBuilder;
import org.jdom.input.SAXBuilder;

/*
 * Created on 16-Feb-2004
 *
 */

/**
 * @author ss401
 *
 */

public class JDomUtilities {
	
	/**
	 * 
	 * @param domElem - A w3c.org DOM element model
	 * @return - A JDOM element
	 */
	public static Element convertToJDomElement(org.w3c.dom.Element domElem){
		DOMBuilder builder = new DOMBuilder();
		return builder.build(domElem);
	}
	
	/**
	 * @param xmlFile - The file object of the xml file
	 * @return - A JDom representation of the root element in the xml file
	 */
	public static Element getRootElement(File xmlFile){
		
		return getDocument(xmlFile).getRootElement();	      
	}

	/**
	 * @param xmlDataFile - The xml file object
	 * @return - A jdom Document of the xml tree
	 */
	public static Document getDocument(File xmlFile) {
		Document d = null;
		try {
			d = new SAXBuilder().build(xmlFile);
		}
		catch (JDOMException e) {
			System.out.println("Couldn't build JDom tree. " +e);
		}
		catch (IOException e) {
			System.out.println("Couldn't find file " + xmlFile +". " +e);
		}
		return d;
	}
}
