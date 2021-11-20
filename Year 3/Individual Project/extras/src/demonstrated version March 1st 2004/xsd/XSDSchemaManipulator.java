/*
 * Created on 24-Feb-2004
 *
 */
package xsd;

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
public class XSDSchemaManipulator {
	Logger logger = Logger.getLogger(this.getClass());
	XSDSchema xsdSchema;
	/**
	 * @param schemaPath - relative path of xsd file
	 */
	public XSDSchemaManipulator(String schemaPath) {
		xsdSchema = XSDLoader.loadXSDInfoModel(schemaPath);
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
	 * @param element
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

}
