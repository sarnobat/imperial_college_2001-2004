package xsd;
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
	 * Internally creates a JDom document
	 * @param xmlFileURL - The relative path of the xml file
	 * @return - A JDom representation of the root element in the xml file
	 */
	public static Element getRootElement(String xmlFileURL){
		Document d = null;
		try {
			d = new SAXBuilder().build(new File("rdb/" +xmlFileURL));
		}
		catch (JDOMException e) {
			System.out.println("Couldn't build JDom tree. " +e);
		}
		catch (IOException e) {
			System.out.println("Couldn't find file " + xmlFileURL +". " +e);
		}
		return d.getRootElement();	      
	}
}
