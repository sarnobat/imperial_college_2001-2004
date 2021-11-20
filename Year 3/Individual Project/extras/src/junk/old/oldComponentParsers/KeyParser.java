/*
 * Created on 16-Mar-2004
 *
 */
package xtractor.schemaConverter.xsd.componentParsers;

import org.apache.log4j.Logger;
import org.jdom.Element;

import xtractor.schemaConverter.xer.XERBuilder;
import xtractor.schemaConverter.xer.XEREntity;
import xtractor.schemaConverter.xsd.XSDSchemaManipulator;

/**
 * @author ss401
 *
 */
public class KeyParser extends AbstractParser {
	Logger logger = Logger.getLogger(this.getClass());

	public KeyParser(XSDSchemaManipulator schemaWalker, XERBuilder builder) {
		super(schemaWalker, builder);
	}
	
	/**
	 * 
	 * @param xsdKey - A jdom representation of the <xsd:key..> element to be parsed 
	 * @param xsdElement - A jdom representation of the <xsd:element..> under which the key component is situated. 
	 * @param parent - The entity the xsdElement already belongs to
	 */
	public void parseKey(Element xsdKey, Element xsdElement, XEREntity parent) {
		//UNIMPLEMENTED: Unimplemented Method
		logger.warn("Unimplemented Method");
		//LATER: parsing of keys and key refs
		
	}

}
