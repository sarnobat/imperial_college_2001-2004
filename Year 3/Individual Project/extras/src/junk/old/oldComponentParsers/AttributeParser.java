/*
 * Created on 25-Feb-2004
 *
 */
package xtractor.schemaConverter.xsd.componentParsers;

import org.apache.log4j.Logger;
import org.jdom.Element;

import xtractor.schemaConverter.xer.XERAttribute;
import xtractor.schemaConverter.xer.XERBuilder;
import xtractor.schemaConverter.xer.XEREntity;
import xtractor.schemaConverter.xsd.XSDSchemaManipulator;
import xtractor.schemaConverter.xsd.XSDTypes;

/**
 * @author ss401
 *
 */
public class AttributeParser extends AbstractParser{
	Logger logger = Logger.getLogger(this.getClass());
	SimpleTypeParser simpleTypeParser;

	public AttributeParser(XSDSchemaManipulator schemaWalker, XERBuilder xerBuilder) {
		super(schemaWalker,xerBuilder);
		this.simpleTypeParser = xerBuilder.getSimpleTypeParser();
	}
	/**
	 * @param child - JDOM representation of the <xsd:attribute..> component
	 * @param parent - XEREntity which this attribute belongs to
	 */
	public void parseAttribute(Element xsdAttribute, XEREntity parent) {
		String attributeType = xsdAttribute.getAttributeValue("type");
		String schemaComponentPrefix = xsdAttribute.getDocument().getRootElement().getNamespacePrefix();

		// See if the xsd:attribute is a PRIMARY key (Note: it can never act as a FOREIGN key)
		if (attributeType != null && attributeType.equals(schemaComponentPrefix + ":ID")) {
			// The attribute acts as a primary key
			xerBuilder.getXERFactory().createXERPrimaryKey(xsdAttribute, parent);
		}
		else if (XSDTypes.isXSDDatatype(attributeType, schemaComponentPrefix)) {
			// The attribute is a default xsd data type
			XERAttribute xerAttribute = xerBuilder.getXERFactory().createXERAttributeFromXSDAttribute(xsdAttribute, parent);
		}
		else {
			// assume the simple type definition can be found in the document
			Element xsdSimpleTypeElement = schemaWalker.getTypeDefinitionElement(xsdAttribute);
			String name = xsdAttribute.getAttributeValue("name");
			String minOccurs = xsdAttribute.getAttributeValue("minOccurs");
			XERAttribute xerAttribute = simpleTypeParser.parseSimpleType(xsdSimpleTypeElement, parent, name, minOccurs);
		}
	}
}
