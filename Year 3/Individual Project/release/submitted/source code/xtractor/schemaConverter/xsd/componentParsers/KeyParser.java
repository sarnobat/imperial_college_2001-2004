/*
 * Created on 16-Mar-2004
 *
 */
package xtractor.schemaConverter.xsd.componentParsers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.Namespace;

import xtractor.schemaConverter.xer.XERBuilder;
import xtractor.schemaConverter.xer.xerConstructs.XERAttribute;
import xtractor.schemaConverter.xer.xerConstructs.XERCompoundConstruct;
import xtractor.schemaConverter.xer.xerConstructs.XEREntity;
import xtractor.schemaConverter.xer.xerConstructs.XERForeignKey;
import xtractor.schemaConverter.xsd.XSDReader;

/**
 * @author ss401
 *
 */
public class KeyParser extends AbstractParser {

	Logger logger = Logger.getLogger(this.getClass());

	// A map from xsd:key name attribute values (e.g. "part_pk") to an XERCompoundConstruct (e.g. part)
	Map keyedCompoundConstructs;

	public KeyParser(XSDReader schemaWalker, XERBuilder builder) {
		super(schemaWalker, builder);
		keyedCompoundConstructs = new HashMap();
	}

	/**
	 * @param xsdKey - The <xsd:key..> element to be parsed
	 */
	public void parseKey(Element xsdKey) {

		// Determine the global xpath of the xer compound construct beng keyed
		Namespace namespace = xsdKey.getNamespace();
		Element xsdSelector = xsdKey.getChild("selector", namespace);
		String xPathSelector = xsdSelector.getAttributeValue("xpath");

		// Locate the XER Compound construct which should have the key
		String xerCompoundConstructName = getTerminalName(xPathSelector);
		XERCompoundConstruct xerCompoundConstruct = xerBuilder.getModel().getXEREntity(xerCompoundConstructName);
		keyedCompoundConstructs.put(xsdKey.getAttributeValue("name"), xerCompoundConstruct);

		// Get the xpath addresses of the key fields
		Element xsdField = xsdKey.getChild("field", namespace);
		Collection keyFieldNames = getKeyFieldNames(xsdKey);

		//Note: because one xpath value depends on the other, we have to 
		//concatenate the two and select the last two fragments

		for (Iterator iter = keyFieldNames.iterator(); iter.hasNext();) {
			String xPathField = (String) iter.next();

			// Determine the name of the key field
			String[] fullXPath = getFullPath(xPathSelector, xPathField);
			//String xerCompoundConstructName = fullXPath[fullXPath.length - 2];
			String xerAttributeName = fullXPath[fullXPath.length - 1];

			// Get rid of the @ symbol indicating it's an element
			if (xerAttributeName.charAt(0) == '@') {
				xerAttributeName = xerAttributeName.substring(1);
			}

			// if it's referring to an entity, promote the attribute to a key attribute
			if (xerCompoundConstruct instanceof XEREntity) {
				XEREntity entity = (XEREntity) xerCompoundConstruct;
				XERAttribute attribute = entity.getAttribute(xerAttributeName);
				entity.promoteAttributeToPrimaryKey(attribute);
			}
			else {
				logger.warn("Unimplemented case");
			}
		}
	}

	/**
	 * We make the assumptions that key refs can only exist between XER Entities
	 * @param xsdKeyref - an <xsd:keyref> element which we are about to parse
	 */
	public void parseKeyRef(Element xsdKeyref) {

		// Find out what XER compound construct is *being* referenced
		String xsdKeyName = xsdKeyref.getAttributeValue("refer");
		XEREntity childEntity = (XEREntity) keyedCompoundConstructs.get(xsdKeyName);

		Namespace ns = xsdKeyref.getNamespace();

		Element xsdSelector = xsdKeyref.getChild("selector", ns);
		String xPathSelector = xsdSelector.getAttributeValue("xpath");

		Collection foreignKeyFieldNames = getKeyFieldNames(xsdKeyref);
		Collection foreignKeys = new LinkedList();

		XEREntity parentEntity = null;
		String parentEntityName;
		for (Iterator iter = foreignKeyFieldNames.iterator(); iter.hasNext();) {
			String xPathField = (String) iter.next();

			String[] fullXPath = getFullPath(xPathSelector, xPathField);

			// Locate the parentCompoundConstruct
			if (parentEntity == null) {
				parentEntityName = fullXPath[fullXPath.length - 2];
				parentEntity = xerBuilder.getModel().getXEREntity(parentEntityName);
			}

			// Locate the attribute acting as a foreign key
			String xerAttributeName = fullXPath[fullXPath.length - 1];

			XERAttribute parentAttribute = parentEntity.getAttribute(xerAttributeName);

			//Promote the parentAttribute to a foreign key
			if (parentEntity instanceof XEREntity) {
				XERForeignKey xerForeignKey =
					((XEREntity) parentEntity).promoteAttributeToForeignKey(parentAttribute, childEntity);
				// Accumulate all the foreign key attributes
				foreignKeys.add(xerForeignKey);
			}
			else {
				logger.warn("Unimplemented case.");
			}

		}
		// Create the relationship; The attribute is promoted to a foreign key in the relationship parser
		if (parentEntity == null) {
			logger.error("Parent not initialized.");
		}
		xerBuilder.getRelationshipParser().parseExplicitRelationship(parentEntity, foreignKeys, childEntity);
	}

	/**
	 * @param xPathSelector - The xpath address of the first part
	 * @param xPathField - The xpath address of the second part
	 * @return - an array of strings where each part is a fragment of the address
	 * (e.g. {"ps_db","part","@pno} )
	 */
	private String[] getFullPath(String xPathSelector, String xPathField) {

		String fullxPath = trim(xPathSelector) + "/" + trim(xPathField);
		return fullxPath.split("/");
	}

	/**
	 * @param xPath - An xpath expression
	 * @return - The same xpath expression but without dots and slashes at the beginning or end
	 */
	private String trim(String xPath) {
		try {

			// remove leading and trailing dots
			if (xPath.charAt(0) == '.') {
				xPath = xPath.substring(1);
			}
			if (xPath.charAt(xPath.length() - 1) == '.') {
				xPath = xPath.substring(0, xPath.length() - 2);
			}

			// remove leading and trailing slashes
			if (xPath.charAt(0) == '/') {
				xPath = xPath.substring(1);
			}

			if (xPath.charAt(xPath.length() - 1) == '/') {
				xPath = xPath.substring(0, xPath.length() - 2);
			}
		}
		catch (IndexOutOfBoundsException e) {
			logger.debug("Small XPath address. Only trimmed to: \"" + xPath + "\"");
			// It should be safe to continue
		}
		return xPath;
	}

	/**
	 * @param xsdKey - The <xsd:key> or <xsd:keyref> whose <xsd:field> child elements 
	 * we are about to determine
	 * @return - A list of Strings, each for the name of a key attribute (or element)
	 */
	private Collection getKeyFieldNames(Element parentXSDElement) {
		Namespace ns = parentXSDElement.getNamespace();
		List fieldElements = parentXSDElement.getChildren("field", ns);

		Collection keyNames = new LinkedList();
		for (Iterator iter = fieldElements.iterator(); iter.hasNext();) {
			Element xsdField = (Element) iter.next();
			String xPathRemainingPart = xsdField.getAttributeValue("xpath");
			String fullXpathForField = trim(xPathRemainingPart);
			keyNames.add(getTerminalName(fullXpathForField));
		}
		return keyNames;
	}

	/**
	 * @param xPathAddress - of the form "supplier/@sno"
	 * @return - The name of the attribute or element at the end of the address (e.g. "@sno") 
	 */
	private String getTerminalName(String xPathAddress) {
		String[] addressParts = xPathAddress.split("/");
		return addressParts[addressParts.length - 1];
	}

}
