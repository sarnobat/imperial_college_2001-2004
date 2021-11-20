/*
 * Created on 24-Feb-2004
 *
 */
package xtractor.schemaConverter.xsd;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.jdom.Element;

/**
 * @author ss401
 * Utility class which performs queries posed by the XERBuilder on the XML Schema,
 * making use of Eclipse's XSD Infomodel
 *
 */
public class XSDReader {
	Logger logger = Logger.getLogger(this.getClass());
	XSDSchema xsdSchema;
	String prefix;
	/**
	 * @param schemaFile - xsd file
	 */
	public XSDReader(File schemaFile) {
		xsdSchema = XSDLoader.loadXSDInfoModel(schemaFile);
		this.prefix = xsdSchema.getDocument().getPrefix();
	}

	/**
	 * @return - A jdom element modelling the "xsd:element" component in the schema
	 */
	public Element getRootElement() {
		EList elements = xsdSchema.getElementDeclarations();
		if (elements.size() != 1) {
			logger.warn("WARNING: More than or less than 1 top level element declaration");
		}
		return JDomUtilities.convertToJDomElement(((XSDElementDeclaration) elements.get(0)).getElement());
	}

	/**
	 * This should work for both simple and complex types, and attributes
	 * @param element - either an <xsd:element...> or and <xsd:attribute...>
	 * @return
	 */
	public Element getTypeDefinitionElement(Element element) {
		// To determine whether the type is defined locally or globally, see if 'element'
		// contains a 'type' attribute name. If so, it's global. If not, it's local.
		String typeName = element.getAttributeValue("type");

		if (typeDefinedLocally(element)) {
			// return the xsd:simpleType or xsd:complexType element which is a child of the xsd:element
			Collection childElements = element.getChildren();
			for (Iterator iter = childElements.iterator(); iter.hasNext();) {
				Element childElement = (Element) iter.next();
				if (childElement.getName().equals("simpleType")) {
					return childElement;
				}
				if (childElement.getName().equals("complexType")) {
					return childElement;
				}
			}
			logger.error("Couldn't find local type defintion for " + element.getAttributeValue("name") + ".");
		}
		else {

			// locate the top-level xsd:simpleType or xsd:complexType element
			Collection typeDefs = xsdSchema.getTypeDefinitions();
			for (Iterator iter = typeDefs.iterator(); iter.hasNext();) {
				XSDTypeDefinition typeDefinition = (XSDTypeDefinition) iter.next();
				if (typeDefinition.getName().equals(element.getAttributeValue("type"))) {
					return JDomUtilities.convertToJDomElement(typeDefinition.getElement());
				}
			}
			logger.error("Couldn't find global type defintion for " + element.getAttributeValue("name") + ".");
		}
		return null;
	}

	/**
	 * Note that if the type has NOT been defined locally, you can't assume it's defined globally:
	 * the element may have type=<<some simple type>>
	 * @param element - The xsd:element whose type defnition location you are trying to determine 
	 * @return - True if the type of this element is defined locally
	 */
	private boolean typeDefinedLocally(Element element) {
		String elementTypeName = element.getAttributeValue("type");
		if (elementTypeName == null) {
			// type attribute not specified, so type must be locally definied
			return true;
		}
		else {
			// type attribute HAS been specified
			return false;
		}

	}

	/**
	 * 
	 * @param xsdElement - an xsd:element whose type nature is to be determined
	 * @return - true if the element's type is a complex one
	 */
	public boolean isComplexType(Element xsdElement) {
		//logger.debug("Trying to determine if " + xsdElement.getAttributeValue("name") + " is complex...");

		/* BASIC ALGORITHM
		 * 
		 * 	IF type defined locally
		 * 		Go through each child element
		 * 			Check to see if the child element is called <xsd:complexType..> or <xsd:simpleType..> 
		 * 	ELSE
		 * 		IF 'type' attribute value is part of the default namespace
		 * 			return false
		 * 		ELSE
		 * 			assume the type is defined globally
		 * 			get the type defintion
		 * 			Check to see if the child element is called <xsd:complexType..> or <xsd:simpleType..>
		 */

		if (typeDefinedLocally(xsdElement)) {
			// type definition is a child of the xsdElement
			for (Iterator iter = xsdElement.getChildren().iterator(); iter.hasNext();) {
				Element child = (Element) iter.next();
				if (child.getName().equals("simpleType") || child.getName().equals("complexType")) {
					return typeDeclarationIsComplex(child);
				}
			}
			logger.error("Couldn't find local type declaration for " + xsdElement.getAttributeValue("name"));
		}
		else {
			// type not specified via a local type declaration
			String type = xsdElement.getAttributeValue("type");
			String prefix = xsdElement.getDocument().getRootElement().getNamespacePrefix();
			if (typeFromDefaultNamespace(type, prefix)) {
				//Element's type is an xsd datatype specified by the "type" attribute
				return false;
			}
			else {
				// assume type is defined globally
				Element globalTypeDefinition = getTypeDefinitionElement(xsdElement);
				return typeDeclarationIsComplex(globalTypeDefinition);
			}
		}
		logger.error("Error locating type for xsd:element");
		return false;
	}

	/**
	 * @param - typeDeclarationElement - The xsd:complexType or xsd:simpleType
	 * @return - true if the element is an xsd:complexType
	 */
	private boolean typeDeclarationIsComplex(Element typeDefinitionElement) {
		String typeType = typeDefinitionElement.getName();
		if (typeType.equals("complexType")) {
			return true;
		}
		else if (typeType.equals("simpleType")) {
			return false;
		}
		else {
			logger.error("Invalid xsd:element type");
			return false;
		}
	}

	/**
	 * See p.227 of Skonnard XML Reference or 
	 * http://www.w3.org/2001/XMLSchema-datatypes for full list
	 * @param type
	 * @return - True if 'type' is a built-in xsd data type 
	 */
	private boolean typeFromDefaultNamespace(String type, String prefix) {
		return XSDTypes.isXSDDatatype(type, prefix);
	}
	/**
	 * @return - The default prefix for names in this document (could be 'xsd' etc.)
	 */
	public String getPrefix() {
		return prefix;
	}

}
