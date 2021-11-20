/*
 * Created on 18-May-2004
 *
 */
package xtractor.schemaConverter.xsd.componentParsers;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.jdom.Element;

import xtractor.schemaConverter.xer.xerConstructs.XERAttribute;

/**
 * @author ss401
 * This is NOT a parser in the same respect as the others. It is simply a meta model
 * for a simple type
 */
public class SimpleTypeDefinitionDetails {

	Logger logger = Logger.getLogger(this.getClass());
	String baseType;
	Element xsdRestrictionElement;

	/**
	 * We assume this element has an <xsd:restriction> as an immediate child
	 * @param simpleTypeDefinition - an <xsd:simpleType> element
	 */
	public SimpleTypeDefinitionDetails(Element xsdSimpleTypeDefinition) {
		
		this.xsdRestrictionElement = xsdSimpleTypeDefinition.getChild("restriction");
		String globalBaseType = this.xsdRestrictionElement.getAttributeValue("base");
		String prefix = xsdSimpleTypeDefinition.getDocument().getRootElement().getNamespacePrefix();
		this.baseType = removePrefix(globalBaseType,prefix);
		
	}
	
	/**
	 * 
	 * @param xsdType - The name of an xsd type which may or may not have a prefix
	 * @param prefix - The prefix to be removed
	 * @return - The xsd type without the prefix
	 */
	private String removePrefix(String xsdType,String prefix){
		if(xsdType.startsWith(prefix +":")){
			//the "+1" is to also remove the colon
			return xsdType.substring(prefix.length() +1 );
		}
		return xsdType;
	}

	/**
	 * Note that this is the local name (it does not have the prefix)
	 * @return - The xsd type that underlies this simple type definition. 
	 */
	public String getBaseType() {
		return baseType;
	}

	/**
	 * @param simpleTypeDefinition
	 * @param attribute
	 */
	public void setConstraints(XERAttribute attribute) {
		Collection restrictionChildren = xsdRestrictionElement.getChildren();
		for (Iterator iter = restrictionChildren.iterator(); iter.hasNext();) {
			Element child = (Element) iter.next();
			//TODO: Also need to deal with nested simpelThypes, attribute/attributeGroup, anyAttribute
			String value = child.getAttributeValue("value");
			if (child.getName().equals("length")) {
				attribute.setStrExactLength(Integer.parseInt(value));
			}
			else if (child.getName().equals("minExclusive")) {
				attribute.setNumMinVal(Integer.parseInt(value) + 1);
			}
			else if (child.getName().equals("minInclusive")) {
				attribute.setNumMinVal(Integer.parseInt(value));
			}
			else if (child.getName().equals("maxExclusive")) {
				attribute.setNumMaxVal(Integer.parseInt(value) - 1);
			}
			else if (child.getName().equals("maxInclusive")) {
				attribute.setNumMaxVal(Integer.parseInt(value));
			}
			else if (child.getName().equals("maxLength")) {
				attribute.setStrMaxLength(Integer.parseInt(value));
			}
			else if (child.getName().equals("minLength")) {
				attribute.setStrMinLength(Integer.parseInt(value));
			}
			else if (child.getName().equals("totalDigits")) {
				attribute.setNumTotalDigits(Integer.parseInt(value));
			}
			else if (child.getName().equals("enumeration")) {
				attribute.addEnumerationValue(value);
			}
			else if (child.getName().equals("fractionDigits")) {
				attribute.setNumFactionDigits(Integer.parseInt(value));
			}
			else if (child.getName().equals("pattern")) {
				logger.warn("Unimplemeneted case.");
			}
			else if (child.getName().equals("whitespace")) {
				logger.warn("Unimplemeneted case.");
			}
			else {
				logger.warn("Unimplemeneted case.");
			}
		}
		
	}
	


}
