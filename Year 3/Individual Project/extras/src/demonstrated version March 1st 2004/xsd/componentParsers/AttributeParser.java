/*
 * Created on 25-Feb-2004
 *
 */
package xsd.componentParsers;

import org.apache.log4j.Logger;
import org.jdom.Element;

import xer.XERAttribute;
import xer.XERBuilder;
import xer.XEREntity;
import xsd.XSDSchemaManipulator;

/**
 * @author ss401
 *
 */
public class AttributeParser {
	Logger logger = Logger.getLogger(this.getClass());
	XSDSchemaManipulator schemaWalker;
	XERBuilder xerBuilder;

	public AttributeParser(XSDSchemaManipulator schemaWalker, XERBuilder xerBuilder) {
		this.schemaWalker = schemaWalker;
		this.xerBuilder = xerBuilder;
	}
	/**
	 * @param child - JDOM representation of the xsd:attribute component
	 * @param parent - XEREntity which this attribute belongs to
	 */
	public void parseAttribute(Element attribute, XEREntity parent) {
		//TODO: Determine whether this is a key or not
		String attributeType = attribute.getAttributeValue("type");
		String schemaComponentPrefix = attribute.getDocument().getRootElement().getNamespacePrefix();
		if (attributeType != null && attributeType.equals(schemaComponentPrefix + ":ID")) {
			parent.addKeyAttribute(new XERAttribute(attribute));
		}
		else {
			// Note: xsd:attribute cannot act as a foreign key		
			XERAttribute xerAttribute = new XERAttribute(attribute);
			parent.addAttribute(xerAttribute);
		}
	}
}
