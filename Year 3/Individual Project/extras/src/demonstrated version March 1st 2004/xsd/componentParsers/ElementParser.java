/*
 * Created on 24-Feb-2004
 *
 */
package xsd.componentParsers;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;


import xer.XERBuilder;
import xer.XEREntity;
import xer.XERRelationship;
import xsd.XSDSchemaManipulator;

/**
 * @author ss401
 *
 */
public class ElementParser {
	Logger logger = Logger.getLogger(this.getClass());
	XSDSchemaManipulator schemaWalker;
	XERBuilder xerBuilder;

	public ElementParser(XSDSchemaManipulator schemaWalker, XERBuilder xerBuilder) {
		this.schemaWalker = schemaWalker;
		this.xerBuilder = xerBuilder;
	}
	
	/**
	 * @param rootElement - the top level xsd:element component in the schema
	 * @return
	 */
	public XEREntity parseRootElement(Element rootElement) {
		if(isComplexType(rootElement)){
			return parseComplexElement(rootElement);
		}
		else{
			logger.warn("Unimplemented.");
			System.out.println("This is a trivial database, because the top level element is a simple type.");
			return null;
		}
	}
	/**
	 * @param xsdElement - JDOM representation of the xsd:element component
	 * @param parent - XEREntity which this element belongs to
	 */
	public void parseElement(Element xsdElement, XEREntity parent) {
		logger.debug("Parsing '" + xsdElement.getAttributeValue("name") +"'...");
		if (isComplexType(xsdElement)) {
			//create a relationship, NOT attribute
			XEREntity foreignEntity = parseComplexElement(xsdElement);
			XERRelationship relationship = new XERRelationship(parent,foreignEntity,xsdElement);
			xerBuilder.getModel().addEntity(foreignEntity);
			xerBuilder.getModel().addRelationship(relationship);
			
		}
		else {
			logger.warn("Unimplemented case: parsing of simple element: " + xsdElement.getAttributeValue("name"));
		}
		/*
		 * Basic algorithm:
		 * 
		 * IF element is complex type
		 * 	create a new entity		-|DONE ABOVE	
		 * 	DO NOT create a foreign key attribute in parent
		 * 	Create an XER Relationship 	
		 * 	add new entity to model list
		 * IF element is simple type
		 * 	consider it as an attribute
		 * 	make sure you've determined all the significat child elements of a simple type
		 * */
	}




	/**
	 * @param element - A JDom model of an xsd:element
	 * @return - an XEREntity representation of the element
	 */
	private XEREntity parseComplexElement(Element element) {
		String name = element.getAttributeValue("type");
		// element's complex structure defined locally 
		if(name == null){
			name = element.getAttributeValue("name");
			//This shouldn't be a problem because no other element will need to use this type
		}
		XEREntity entity = new XEREntity(name, xerBuilder.getModel());

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
	 * @param complexTypeDefinitionElement - The JDOM element where the type is declared
	 * @param parent - The XEREntity create for the complex element whose type is specified by 'complexTypeDefinitionElement'
	 */
	private void parseComplexElementContent(Element complexTypeDefinitionElement, XEREntity parent) {
		List children = complexTypeDefinitionElement.getChildren();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			Element child = (Element) iter.next();
			String componentType = child.getName();
			if (componentType.equals("attribute")) {
				//INDEFINITE NUMBER
				xerBuilder.getAttributeParser().parseAttribute(child, parent);
			}
			else if (componentType.equals("group")) {
				//ONCE
			}
			else if (componentType.equals("all")) {
				//ONCE
			}
			else if (componentType.equals("choice")) {
				//ONCE
			}
			else if (componentType.equals("sequence")) {
				//ONCE
				xerBuilder.getModelGroupParser().parseModelGroup(child, parent);
			}
			else if (componentType.equals("attributeGroup")) {
				//INDEFINITE NUMBER
			}
			else if (componentType.equals("anyAttribute")) {
				//ONCE
			}
		}
	}

	/**
	 * Determines whether an xsd:element is complex or not by looking for its type declaration and
	 * seeing if it's simple or complex
	 * @param xsdElement - The JDom representation of an xsd:element
	 * @return - True if the element is complex
	 */
	private boolean isComplexType(Element xsdElement) {
		logger.debug("Trying to determine if "+xsdElement.getAttributeValue("name") + " is complex...");
		Element typeDefinitionElement = schemaWalker.getTypeDefinitionElement(xsdElement);
		
		//TODO: this is a primitive way of determining if a simple element is a primitive type, by
		// seeing if it is unable to locate a simple type declaration
		if(typeDefinitionElement == null){
			// type not declared in this document, and presumably belongs to xsd namespace
			return false;
		}
		
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


}
