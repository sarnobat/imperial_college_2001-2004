/*
 * Created on 08-Mar-2004
 *
 */
package xtractor.schemaConverter.xsd.componentParsers;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.jdom.Element;

import xtractor.schemaConverter.xer.XERAttribute;
import xtractor.schemaConverter.xer.XERBuilder;
import xtractor.schemaConverter.xer.XERCompoundConstruct;
import xtractor.schemaConverter.xer.XEREntity;
import xtractor.schemaConverter.xsd.XSDSchemaManipulator;

/**
 * @author ss401
 * Note: everything here must apply to both attributes and elements
 *
 */
public class SimpleTypeDefinitionParser extends AbstractParser{

	Logger logger = Logger.getLogger(this.getClass());

	public SimpleTypeDefinitionParser(XSDSchemaManipulator schemaWalker, XERBuilder xerBuilder) {
		super(schemaWalker,xerBuilder);
	}

	/**
	 * @param componentWithSimpleType - The <xsd:element..> or <xsd:attribute..> with the simple type 
	 * @param parentEntity - The XER entity which the component will belong
	 * @return The attribute conforming to the simple type restrictions
	 */
	/*public XERAttribute parseSimpleType(Element xsdSimpleTypeElement, XEREntity parentEntity, String name, String minOccurs) {
		//Element xsdSimpleTypeElement = schemaWalker.getTypeDefinitionElement(componentWithSimpleType);
		//String xerAttributeName = componentWithSimpleType.getAttributeValue("name");

		//String minOccurs = componentWithSimpleType.getAttributeValue("minOccurs");
		return parseSimpleType(xsdSimpleTypeElement, parentEntity, xerAttributeName, minOccurs);
	}*/
//	/**
//	 * 
//	 * @param xsdSimpleTypeElement - The <xsd:simpleType..> component
//	 * @param parentEntity - The entity which the attribute returned will belong to
//	 * @param xerAttributeName - The name you wish to give to the xer Attribute that is about to be created
//	 * @param minOccurs - The minOccurs attribute value of the component (attribute or element) wth simple type 
//	 * @return
//	 */
//	public XERAttribute parseSimpleTypeForElement(Element xsdSimpleElement, Element xsdSimpleTypeDefinitionElement) {
//		
//		String xerAttributeName = xsdSimpleElement.getAttributeValue("name");
//		
//		Collection children = xsdSimpleTypeDefinitionElement.getChildren();
//		for (Iterator iter = children.iterator(); iter.hasNext();) {
//			Element childComponent = (Element) iter.next();
//			if (childComponent.getName().equals("list")) {
//				logger.warn("Unimplemented");
//			}
//			else if (childComponent.getName().equals("union")) {
//				logger.warn("Unimplemented");
//			}
//			else if (childComponent.getName().equals("restriction")) {
//				return parseRestriction(childComponent, xerAttributeName,xsdSimpleElement);
//			}
//			else {
//				logger.error("Invalid child of simpleType element");
//			}
//
//		}
//		logger.error("Invalid child of simpleType element");
//		return null;
//	}
//
//	/**
//	 * 
//	 * @param xsdRestrictionElement - a jdom <xsd:restriction...> element
//	 * @param parent - The XER entity which the element belongs to
//	 * @param xerAttributeName - The name that should be given to the attribute to be created
//	 * @param  minOccurs - The minOccurs attribute value of the component (attribute or element) wth simple type
//	 * @return - The XER attribute conforming to the restrictinos
//	 */
//	public XERAttribute parseRestriction(Element xsdRestrictionElement, String xerAttributeName, Element xsdSimpleElement) {
//		//I think this is compulsory, so no need to deal with null
//		String baseType = xsdRestrictionElement.getAttributeValue("base");
//		XERAttribute attribute = xerBuilder.getXERFactory().createXERAttributeFromRestriction(xsdSimpleElement,xerAttributeName, baseType);
//
//		Collection children = xsdRestrictionElement.getChildren();
//		for (Iterator iter = children.iterator(); iter.hasNext();) {
//			Element child = (Element) iter.next();
//
//			//TODO: Also need to deal with nested simpelThypes, attribute/attributeGroup, anyAttribute
//			String value = child.getAttributeValue("value");
//			if (child.getName().equals("length")) {
//				attribute.setStrExactLength(Integer.parseInt(value));
//			}
//			else if (child.getName().equals("minExclusive")) {
//				attribute.setNumMinVal(Integer.parseInt(value) + 1);
//			}
//			else if (child.getName().equals("minInclusive")) {
//				attribute.setNumMinVal(Integer.parseInt(value));
//			}
//			else if (child.getName().equals("maxExclusive")) {
//				attribute.setNumMaxVal(Integer.parseInt(value) - 1);
//			}
//			else if (child.getName().equals("maxInclusive")) {
//				attribute.setNumMaxVal(Integer.parseInt(value));
//			}
//			else if (child.getName().equals("maxLength")) {
//				attribute.setStrMaxLength(Integer.parseInt(value));
//			}
//			else if (child.getName().equals("minLength")) {
//				attribute.setStrMinLength(Integer.parseInt(value));
//			}
//			else if (child.getName().equals("totalDigits")) {
//				attribute.setNumTotalDigits(Integer.parseInt(value));
//			}
//			else if (child.getName().equals("enumeration")) {
//				attribute.addEnumerationValue(value);
//			}
//			else if (child.getName().equals("fractionDigits")) {
//				attribute.setNumFactionDigits(Integer.parseInt(value));
//			}
//			else if (child.getName().equals("pattern")) {
//				logger.warn("Unimplemeneted case.");
//			}
//			else if (child.getName().equals("whitespace")) {
//				logger.warn("Unimplemeneted case.");
//			}
//			else {
//				logger.warn("Unimplemeneted case.");
//			}
//
//		}
//		return attribute;
//	}
}
