/*
 * Created on 11-Feb-2004
 *
 */
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.resource.*;
import org.eclipse.emf.ecore.resource.impl.*;
import org.eclipse.xsd.*;
import org.eclipse.xsd.util.*;
import org.jdom.Element;
import org.jdom.input.DOMBuilder;

/**
 * @author ss401
 *
 * Convenience class for accessing schema elements using Eclipse's
 * XSD Infomodel API
 */
public class XSDSchemaManipulatorOld {

	XSDSchema schema;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 
	 */
	public XSDSchemaManipulator(String schemaURL) {
		schema = loadSchema(schemaURL);
		
	}

	/**
	 * Returns and XSD Schema model for the supplied schema file
	 * @param schemaURL
	 * @return
	 * @throws Exception
	 */
	private XSDSchema loadSchema(String schemaURL) {
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

		System.err.println("loadSchemaUsingResourceSet(" + schemaURL + ") did not contain any schemas!");
		return null;
	}

	/**
	 * Returns the name of the top-most element in a schema. This will correspond to the
	 * database name
	 * @param schemaURL
	 * @return
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
	 * Gets the names of the elements contained inside the complex type with
	 * the given name. Returns a List of Strings.
	 * @param rootElementName
	 * @return
	 */
	public List getAttributesOfComplexType(String elementName) {
		List attributes = new LinkedList();
		XSDTypeDefinition typeDef = getTypeDefinition(elementName);
		Element typeDefElement = toJDomElement(typeDef.getElement());
		List children = typeDefElement.getChildren();
		logger.info("Complex Type Element: " + typeDefElement);
		logger.info("Children of Complex Type Element: " + children);

		for (Iterator iter = children.iterator(); iter.hasNext();) {
			Element child = (Element) iter.next();
			String childName = child.getName();
			
			if (childName.equals("choice")) {
				attributes.addAll(getChildAttributesOfChoiceElement(child));
			}
			else if (childName.equals("attribute")){
				attributes.add(child.getAttribute("name").getValue());
			}
			else {
				System.err.println("NOT YET IMPLEMENTED");
			}
		}

		return attributes;
	}

	/**
	 * Returns a list of Strings. Each string represents the name attribute of the <xsd:attribute..> tag
	 * which is a child of 'choiceElement'
	 * @param choiceElement
	 * @return
	 */
	private List getChildAttributesOfChoiceElement(Element choiceElement) {

		List childElementValues = new LinkedList();
		/*Element parent = choiceElement.getParent();
		List children = parent.getChildren();*/
		List children = choiceElement.getChildren();

		for (Iterator iter = children.iterator(); iter.hasNext();) {
			Element child =  (Element) iter.next();
			String childName = child.getName();

			if (childName.equals("choice")) {
				//return getChildrenOfChoiceElement(choiceElement);
				//Not quite sure how to append this
			}
			else if (childName.equals("element")) {
				logger.info("Child name: " + child.getName());
				logger.info("Child content: " + child.getAttributeValue("name"));
				childElementValues.add(child.getAttributeValue("name"));
			}
			else {
				System.err.println("NOT YET IMPLEMENTED");
			}
		}
		//return listToStringArray(childElementValues);
		return childElementValues;

	}

	/**
	 * Converts a List of Strings to an array of Strings
	 * @param childElementValues
	 * @return
	 */
	private String[] listToStringArray(List stringList) {
		String[] array = new String[stringList.size()];
		for (int i = 0; i < stringList.size(); i++) {
			array[i] = (String) stringList.get(i);
		}
		return array;
	}

	private XSDTypeDefinition getTypeDefinition(String name) {
		EList typedefs = schema.getTypeDefinitions();
		for (Iterator iter = typedefs.iterator(); iter.hasNext();) {
			XSDTypeDefinition typeDef = (XSDTypeDefinition) iter.next();
			//System.out.println(typeDef.getName());
			if (typeDef.getName().equals(name)) {
				return typeDef;
			}
		}
		return null;
	}

	private org.jdom.Element toJDomElement(org.w3c.dom.Element elem) {
		DOMBuilder builder = new DOMBuilder();
		return builder.build(elem);
	}

	/**
	 * Returns a list of values (as Strings) for child elements of the
	 * supplied tableName (analogous to a ComplexType)
	 * @param tableName
	 * @return
	 */
	public List getAttributes(String tableName) {
		List attributes = getAttributesOfComplexType(tableName);
		//System.out.println(attributes);
		return attributes;
	}
}