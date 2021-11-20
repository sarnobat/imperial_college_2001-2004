/*
 * Created on 24-Feb-2004
 *
 */
package xtractor.schemaConverter.xsd.componentParsers;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

import xtractor.schemaConverter.xer.XERAttribute;
import xtractor.schemaConverter.xer.XERAttributeOrEntity;
import xtractor.schemaConverter.xer.XERBuilder;
import xtractor.schemaConverter.xer.XEREntity;
import xtractor.schemaConverter.xer.XERGeneralization;
import xtractor.schemaConverter.xer.XERRelationship;
import xtractor.schemaConverter.xsd.XSDSchemaManipulator;
import xtractor.schemaConverter.xsd.XSDTypes;

/*
 * Rules:
 * 
 * SIMPLE TYPE:
 *	If the maxOccurs attribute is 1, create an Entity attribute
 *	If it's greater than 1, create a foreign Entity
 * COMPLEX TYPE:
 * 	Create a new Entity (referred entity)
 * 	Create a relationship between the parent entity and the referred entity 
 */
/**
 * @author ss401
 *
 */
public class ElementParser extends AbstractParser {
	Logger logger = Logger.getLogger(this.getClass());
	SimpleTypeParser simpleTypeParser;

	/**
	 * The xerBuilder is required so that the XERFactory can be accessed
	 */
	public ElementParser(XSDSchemaManipulator schemaWalker, XERBuilder xerBuilder) {
		super(schemaWalker, xerBuilder);
		this.simpleTypeParser = xerBuilder.getSimpleTypeParser();
	}

	/**
	 * @param rootElement - the top level xsd:element component in the schema
	 * @return
	 */
	public XEREntity parseRootElement(Element rootElement) {
		if (schemaWalker.isComplexType(rootElement)) {
			return parseComplexElement(rootElement);
		}
		else {
			logger.warn("Unimplemented.");
			System.out.println("This is a trivial database, because the top level element is a simple type.");
			return null;
		}
	}

	/**
	 * 
	 * @param xsdElement - The xsd:element contained inside a xsd:choice
	 */
	/*public void parseElement(Element xsdElement,XERGeneralization xerGeneralization) {
		logger.debug("Parsing '" + xsdElement.getAttributeValue("name") + "'...");

		// create an XER entity and add it to the generalization
		if (schemaWalker.isComplexType(xsdElement)) {
			XEREntity entity = parseComplexElement(xsdElement);
			xerGeneralization.addEntity(entity);
		}
		else {
			// Element is a simple type BUT may be multioccurring
			XERAttributeOrEntity xerAttributeOrEntity = parseElement  
			xerGeneralization.addXERComponent();
		}

	}*/

	/**
	 * @param xsdElement - JDOM representation of the xsd:element component
	 * @param parent - XEREntity which this element belongs to
	 */
	public void parseElement(Element xsdElement, XEREntity parent) {

		logger.debug("Parsing '" + xsdElement.getAttributeValue("name") + "'...");

		if (schemaWalker.isComplexType(xsdElement)) {

			//create a relationship, NOT attribute
			XEREntity foreignEntity = parseComplexElement(xsdElement);
			String relationshipName = xsdElement.getAttributeValue("name");
			XERRelationship relationship =
				xerBuilder.getXERFactory().createXERRelationship(relationshipName, parent, foreignEntity, xsdElement);
		}

		else {
			
			//create an entity
			//create a foreign key in parent
			//create a primary key in referenced entity
			//create an attribute in referenced entity for value
			//(This is the only way to handle multi valued attributes
			  
			 
			String type = xsdElement.getAttributeValue("type");
			String elementName = xsdElement.getAttributeValue("name");
			String minOccurs = xsdElement.getAttributeValue("minOccrs");
			XEREntity referredEntity = xerBuilder.getXERFactory().createXEREntity(xsdElement.getAttributeValue("name"));
			XERRelationship relationship =
				xerBuilder.getXERFactory().createXERRelationship(referredEntity.getName(), parent, referredEntity, xsdElement);
			XERAttribute attribute = null;
			if (XSDTypes.isXSDDatatype(type, xsdElement.getNamespacePrefix())) {
				// type is a standard xsd data type
				attribute = xerBuilder.getXERFactory().createXERAttributeFromXSDElemnt(xsdElement, referredEntity);
			}
			else {

				// type is a simple type defined in this document
				Element xsdTypeDefinition = schemaWalker.getTypeDefinitionElement(xsdElement);
				attribute = simpleTypeParser.parseSimpleType(xsdTypeDefinition, referredEntity, elementName, minOccurs);
				//attribute = xerBuilder.getXERFactory().createXERAttributeFromXSDElemnt(xsdElement,referredEntity);
			}
			//referredEntity.addAttribute(attribute); 
		}
	}

	/**
	 * Only use this if it is not associated with a parent entity (but, say, an XER Generalization)
	* @param element - A JDom model of an xsd:element
	* @return - an XEREntity representation of the element
	*/
	private XEREntity parseComplexElement(Element element) {
		String name = element.getAttributeValue("type");
		// element's complex structure defined locally 
		if (name == null) {
			name = element.getAttributeValue("name");
			//This shouldn't be a problem because no other element will need to use this type
		}
		XEREntity entity = xerBuilder.getXERFactory().createXEREntity(name);

		Element complexTypeDefinitionElement = schemaWalker.getTypeDefinitionElement(element);
		parseComplexElementContent(complexTypeDefinitionElement, entity);
		return entity;
	}

	/**
	 * @param element - The xsd:element whose type definition location is trying to be determined
	 * @return True if the element's type is specified via a global complex type definition
	 */
	private boolean complexTypeIsGlobal(Element element) {
		String typeName = element.getAttributeValue("type");
		return (typeName != null);
	}

	/**
	 * 
	 * @param typeDefinitionElement - The JDOM element where the type is declared
	 * @param parent - The XEREntity create for the complex element whose type is specified by 'complexTypeDefinitionElement'
	 */
	private void parseComplexElementContent(Element typeDefinitionElement, XEREntity parent) {
		List children = typeDefinitionElement.getChildren();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			Element child = (Element) iter.next();
			String componentType = child.getName();
			if (componentType.equals("attribute")) {
				//INDEFINITE NUMBER
				xerBuilder.getAttributeParser().parseAttribute(child, parent);
			}
			else if (componentType.equals("group")) {
				//ONCE
				xerBuilder.getModelGroupParser().parseModelGroup(child, parent);
			}
			else if (componentType.equals("all")) {
				//ONCE
				xerBuilder.getModelGroupParser().parseModelGroup(child, parent);
			}
			else if (componentType.equals("choice")) {
				//ONCE
				xerBuilder.getModelGroupParser().parseModelGroup(child, parent);
			}
			else if (componentType.equals("sequence")) {
				//ONCE
				xerBuilder.getModelGroupParser().parseModelGroup(child, parent);
			}
			else if (componentType.equals("attributeGroup")) {
				//INDEFINITE NUMBER
				logger.warn("Unimplemented");
			}
			else if (componentType.equals("anyAttribute")) {
				//ONCE
				logger.warn("Unimplemented");
			}
			else if (componentType.equals("key")) {
				// INDEFINITE NUMBER
				xerBuilder.getKeyParser().parseKey(child, typeDefinitionElement, parent);
			}
			else if (componentType.equals("keyRef")) {
				// INDEFINITE NUMBER
			}
			else if (componentType.equals("unique")) {
				// INDEFINITE NUMBER
			}
			else {
				logger.error("Invalid child component of <xsd:element>.");
			}
		}
	}

}
