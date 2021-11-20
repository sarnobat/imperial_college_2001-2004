package xsd;
/*
 * Created on 11-Feb-2004
 *
 */
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;
import org.eclipse.xsd.util.XSDResourceImpl;
import org.jdom.Element;

/**
 * @author ss401
 *
 * Convenience class for accessing schema elements using Eclipse's XSD Infomodel API
 */
public class XSDSchemaManipulator {

	public XSDSchema schema;
	Logger logger = Logger.getLogger(this.getClass());

	public XSDSchemaManipulator(String schemaURL) {
		schema = loadSchema(schemaURL);
	}

	/**
	 * 
	 * @param schemaURL The URL of the XML Schema 
	 * @return The XML Schema object
	 */
	public XSDSchema loadSchema(String schemaURL) {
		//For more information, see XSDFindTypesMissingFacets.java
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xsd", new XSDResourceFactoryImpl());

		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getLoadOptions().put(XSDResourceImpl.XSD_TRACK_LOCATION, Boolean.TRUE);
		XSDResourceImpl xsdSchemaResource = (XSDResourceImpl) resourceSet.getResource(URI.createURI(schemaURL), true);

		for (Iterator resources = resourceSet.getResources().iterator(); resources.hasNext();) {
			Resource resource = (Resource) resources.next();
			if (resource instanceof XSDResourceImpl) {
				XSDResourceImpl xsdResource = (XSDResourceImpl) resource;
				return xsdResource.getSchema();
			}
		}

		logger.error("loadSchemaUsingResourceSet(" + schemaURL + ") did not contain any schemas.");
		return null;
	}

	/**
	 * Returns the name of the top-most element in a schema. This will correspond to the
	 * database name. If there is more than one top-level element, the first one will be returned
	 * @param schemaURL
	 * @return The name of the top level element
	 * @throws Exception
	 */
	public String getRootElement() {
		EList elements = schema.getElementDeclarations();
		if (elements.size() != 1) {
			logger.warn("WARNING: More than or less than 1 top level element declaration");
		}
		return ((XSDElementDeclaration) elements.get(0)).getName();
	}

	/**
	 * @param rootElementName The Element whose type is being queried
	 * @return The name of the element type
	 */
	public String getElementType(String elementName) {
		return getElementDeclarationElement(elementName).getAttribute("type").getValue();

	}

	/**
	 * @param elementName
	 * @return A JDom model of the "element" element
	 */
	public Element getElementDeclarationElement(String elementName) {
		return jDomConverter.toJDomElement(getElementDeclaration(elementName).getElement());

	}

	/**
	 * 
	 * @param elementName The name of the element
	 * @return The XSD model of this element declaration
	 */
	public XSDElementDeclaration getElementDeclaration(String elementName) {
		EList elements = schema.getElementDeclarations();
		for (Iterator iter = elements.iterator(); iter.hasNext();) {
			XSDElementDeclaration elemDeclaration = (XSDElementDeclaration) iter.next();
			if (elemDeclaration.getName().equals(elementName)) {
				return elemDeclaration;
			}

		}
		//logger.debug("no element declaration found for \"" + elementName + "\"");
		return null;
	}

	/**
	 * Returns null if the type isn't declared in this XML schema. In other words, it's not
	 * part of the default namespace (e.g. 'xsd:string' isn't part of the default namespace)
	 * @param typeName - The name of a type
	 * @return - the XSD type definition object for 'typeName'
	 */
	public XSDTypeDefinition getTypeDefinition(String typeName) {

		//TODO: Need to check if the type name is part of this namespace. 
		// It's no use looking for the type definition of XSD string for example

		EList typeDefs = schema.getTypeDefinitions();
		for (Iterator iter = typeDefs.iterator(); iter.hasNext();) {
			XSDTypeDefinition typeDef = (XSDTypeDefinition) iter.next();
			if (typeDef.getName().equals(typeName)) {
				return typeDef;
			}
		}
		//logger.warn("no type definition found for '" + typeName + "'.");
		return null;
	}

	/**
	 * Returns null if the type isn't defined as part of the schema's default namespace
	 * @param typeName
	 * @return
	 */
	public Element getTypeDefinitionElement(String typeName) {
		XSDTypeDefinition domTypeDefElement = getTypeDefinition(typeName);
		if (domTypeDefElement != null) {
			return jDomConverter.toJDomElement(domTypeDefElement.getElement());
		}
		else {
			//logger.debug("No type definition element for " + typeName + ". Returning null.");
			return null;
		}
	}

	/**
	 * @param rootElementType
	 * @return
	 */
	public boolean isComplexType(String elementType) {
		//XSDTypeDefinition typeDef = getTypeDefinition(elementType).getComplexType() != null;
		//logger.debug("Type Definition: " +getTypeDefinition(elementType).getComplexType());

		// TODO: Need to have a more genuine test to see if the type is complex. May need
		// to examine namespace

		XSDTypeDefinition typeDef = getTypeDefinition(elementType);
		if (typeDef == null) {
			//logger.debug("Type definition for " + elementType + " is null. Concluding that it is a simple type.");
			//TODO: Not sure this is a valid conclusion. It may be complex but be in a different namespace
			return false;
		}
		XSDParticle complexTypeDef = typeDef.getComplexType();
		boolean isComplex = (complexTypeDef != null);
		return (isComplex);
	}

	/**
	 * @return A data structure containing all the information about the structure of a complex type
	 */
	public XSDComplexTypeContent getContentOfComplexType(String complexTypeName) {
		return (new XSDComplexTypeContent(complexTypeName, this));

	}

}