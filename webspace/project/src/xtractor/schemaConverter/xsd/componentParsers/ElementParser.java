/*
 * Created on 18-May-2004
 *
 */
package xtractor.schemaConverter.xsd.componentParsers;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Element;

import xtractor.schemaConverter.xer.XERBuilder;
import xtractor.schemaConverter.xer.xerConstructs.XERAttribute;
import xtractor.schemaConverter.xer.xerConstructs.XERCompoundConstruct;
import xtractor.schemaConverter.xer.xerConstructs.XEREntity;
import xtractor.schemaConverter.xsd.XSDReader;
import xtractor.schemaConverter.xsd.XSDTypes;

/**
 * @author ss401
 *
 */
public class ElementParser extends AbstractParser {

	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * The xerBuilder is required so that the XERFactory can be accessed
	 */
	public ElementParser(XSDReader schemaWalker, XERBuilder xerBuilder) {
		super(schemaWalker, xerBuilder);
		//this.simpleTypeParser = xerBuilder.getSimpleTypeParser();
	}

	/**
	 * @param rootXSDElement - The top level xsdElement
	 * @return
	 */
	public void parseRootElement(Element rootXSDElement) {
		// Create the XER Model based on the entities and their implicit relationships
		if (schemaWalker.isComplexType(rootXSDElement)) {
			
			parseRootComplexElement(rootXSDElement);

		}
		else {
			System.out.println("This is a trivial database, because the top level element is a simple type.");
		}

	}

	/**
	* @param element - A JDom model of an xsd:element
	*/
	private void parseRootComplexElement(Element xsdComplexElement) {
		String name = xsdComplexElement.getAttributeValue("type");

		if (name == null) {
			// element's complex structure defined inline
			name = xsdComplexElement.getAttributeValue("name");
			//This shouldn't be a problem because no other element will need to use this type
		}
		XEREntity entity = xerBuilder.getXERFactory().createXEREntity(name);
		xerBuilder.getModel().setRootElement(entity);

		Element complexTypeDefinitionElement = schemaWalker.getTypeDefinitionElement(xsdComplexElement);
		parseComplexElementContent(complexTypeDefinitionElement, entity);

	}

	/**
	 * 
	 * @param xsdComplexElement - An xsd:element with complex type
	 * @param parent - The parent XER construct to which the result of the parsing must be added
	 */
	private void parseComplexElement(Element xsdComplexElement, XERCompoundConstruct parent) {
		String name = xsdComplexElement.getAttributeValue("type");
		int minOccurs = resolveOccurrenceCardinality(xsdComplexElement.getAttributeValue("minOccurs"));
		int maxOccurs = resolveOccurrenceCardinality(xsdComplexElement.getAttributeValue("maxOccurs"));

		if (name == null) {
			// element's complex structure defined inline
			name = xsdComplexElement.getAttributeValue("name");
			//This shouldn't be a problem because no other element will need to use this type
		}

		Element complexTypeDefinitionElement = schemaWalker.getTypeDefinitionElement(xsdComplexElement);
		Attribute mixedAttribute = complexTypeDefinitionElement.getAttribute("mixed");
		XEREntity entity;
		if (mixedAttribute == null || mixedAttribute.getValue().equals("false")) {
			entity = xerBuilder.getXERFactory().createXEREntity(name);
		}
		else {
			entity = xerBuilder.getXERFactory().createXERMixedEntity(name);
		}

		parent.addConstruct(entity, minOccurs, maxOccurs);

		parseComplexElementContent(complexTypeDefinitionElement, entity);

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
			/*else if (componentType.equals("group")) {
				//ONCE
				xerBuilder.getModelGroupParser().parseModelGroup(child, parent);
			}
			else if (componentType.equals("all")) {
				//ONCE
				xerBuilder.getModelGroupParser().parseModelGroup(child, parent);
			}
			else*/
			if (componentType.equals("choice")) {
				//ONCE
				xerBuilder.getModelGroupParser().parseModelGroup(child, parent);
			}
			else if (componentType.equals("sequence")) {
				//ONCE
				xerBuilder.getModelGroupParser().parseModelGroup(child, parent);
			}
			/*else if (componentType.equals("attributeGroup")) {
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
			}*/
		}
	}

	/**
	 * @param xsdElement - The xsd:element which is being parsed
	 * @param parent - The xer construct to which the result of the parsing should be added 
	 */
	public void parseElement(Element xsdElement, XERCompoundConstruct parent) {
		if (schemaWalker.isComplexType(xsdElement)) {
			parseComplexElement(xsdElement, parent);
		}
		else {
			parseSimpleElement(xsdElement, parent);
		}
	}

	/**
	 * @param xsdElement - The xsd:element with a simple type
	 * @param parent - The XER compound construct to which the results of the parsing should be added
	 */
	private void parseSimpleElement(Element xsdElement, XERCompoundConstruct parent) {
		// At this point we just consider it a multi-valued attributes, not foreign tables

		String name = xsdElement.getAttributeValue("name");
		String typeName = xsdElement.getAttributeValue("type");
		int minOccurs = resolveOccurrenceCardinality(xsdElement.getAttributeValue("minOccurs"));
		int maxOccurs = resolveOccurrenceCardinality(xsdElement.getAttributeValue("maxOccurs"));

		// See if the type is pre-defined or custom

		String prefix = xsdElement.getDocument().getRootElement().getNamespacePrefix();
		if (XSDTypes.isXSDDatatype(typeName, prefix)) {
			XERAttribute attribute = xerFactory.createXERAttributeFromXSDElement(name, typeName, minOccurs, maxOccurs);
			parent.addConstruct(attribute, minOccurs, maxOccurs);
		}
		else {
			Element simpleTypeDefinition = schemaWalker.getTypeDefinitionElement(xsdElement);
			SimpleTypeDefinitionDetails simpleTypeDefinitionData = new SimpleTypeDefinitionDetails(simpleTypeDefinition);
			String baseType = simpleTypeDefinitionData.getBaseType();
			XERAttribute attribute = xerFactory.createXERAttributeFromXSDElement(name, baseType, minOccurs, maxOccurs);

			simpleTypeDefinitionData.setConstraints(attribute);

			parent.addConstruct(attribute, minOccurs, maxOccurs);

		}
	}

}
