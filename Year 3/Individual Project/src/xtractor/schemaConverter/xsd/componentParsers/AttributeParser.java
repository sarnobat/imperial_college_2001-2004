/*
 * Created on 25-Feb-2004
 *
 */
package xtractor.schemaConverter.xsd.componentParsers;

import org.apache.log4j.Logger;
import org.jdom.Element;

import xtractor.schemaConverter.xer.XERBuilder;
import xtractor.schemaConverter.xer.xerConstructs.XERAttribute;
import xtractor.schemaConverter.xer.xerConstructs.XERCompoundConstruct;
import xtractor.schemaConverter.xsd.XSDReader;
import xtractor.schemaConverter.xsd.XSDTypes;

/**
 * @author ss401
 *
 */
public class AttributeParser extends AbstractParser {
	Logger logger = Logger.getLogger(this.getClass());

	public AttributeParser(XSDReader schemaWalker, XERBuilder xerBuilder) {
		super(schemaWalker, xerBuilder);
	}
	/**
	 * @param child - JDOM representation of the <xsd:attribute..> component
	 * @param parent - XEREntity which this attribute belongs to
	 */
	public void parseAttribute(Element xsdAttribute, XERCompoundConstruct parent) {
		String name = xsdAttribute.getAttributeValue("name");
		String attributeType = xsdAttribute.getAttributeValue("type");
		int minOccurs = resolveUseCardinality(xsdAttribute.getAttributeValue("use"));
		String schemaComponentPrefix = xsdAttribute.getDocument().getRootElement().getNamespacePrefix();

		if (XSDTypes.isXSDDatatype(attributeType, schemaComponentPrefix)) {
			// The attribute is a default xsd data type
			XERAttribute xerAttribute = xerBuilder.getXERFactory().createXERAttributeFromXSDAttribute(name, attributeType, minOccurs, 1);
			parent.addConstruct(xerAttribute,minOccurs,1);
		}
		else {
						Element simpleTypeDefinition = schemaWalker.getTypeDefinitionElement(xsdAttribute);
			SimpleTypeDefinitionDetails simpleTypeDefinitionData = new SimpleTypeDefinitionDetails(simpleTypeDefinition);
			String baseType = simpleTypeDefinitionData.getBaseType();

			XERAttribute attribute = xerFactory.createXERAttributeFromXSDAttribute(name, baseType, minOccurs, 1);
			simpleTypeDefinitionData.setConstraints(attribute);
			parent.addConstruct(attribute,minOccurs,1);
		}
	}
}
