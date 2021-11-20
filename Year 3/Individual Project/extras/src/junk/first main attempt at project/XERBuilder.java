import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import xer.*;
import xsd.*;

/*
 * Created on 16-Feb-2004
 *
 */

/**
 * Creates an Extensible Entity-Relationship Model of an XML Schema
 * @author ss401
 *
 */
public class XERBuilder {

	XERModel model;
	XSDSchemaManipulator xsdWalker;
	boolean flatSubstructures = false;

	ModelGroupParser modelGroupParser;

	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @param string The path of the XML Schema file
	 * @return An XER Model of the XML Schema
	 */
	public XERModel build(String schemaPath) {

		this.xsdWalker = new XSDSchemaManipulator(schemaPath);
		this.model = new XERModel();

		this.modelGroupParser = new ModelGroupParser(this);

		// Create an entity for the top level element
		XERComponent xerRoot = parseRootElement();
		model.addComponent(xerRoot);

		System.out.println(model.getSQL());
		return model;
	}

	XERComponent parseRootElement() {
		String rootElementName = xsdWalker.getRootElement();
		logger.debug("Root element name: " + rootElementName);

		// If the root element is a complex type, create an entity
		// TODO: This is according to the XER conversion process, but in practice will probably need to be changed
		//logger.warn("This is a trivial database, because the top level element is a simple type.");
		return parseTopLevelElement(rootElementName);
	}

	XEREntity parseComplexElement(String elementName, String elementType, XEREntity parentEntity) {
		// Create the new entity
		XEREntity entity;
		if (parentEntity == null) {
			entity = new XEREntity(elementName);
		}
		else {
			entity = parentEntity;
		}

		// Determine the child elements of the element, and add them to the entity 
		XSDComplexTypeContent content = xsdWalker.getContentOfComplexType(elementType);
		logger.info("Content of complex type defintion for " + "'" + elementType + "': " + content);

		//Add schema attributes to entity
		List attributes = content.getAttributes();
		for (Iterator iter = attributes.iterator(); iter.hasNext();) {
			XSDAttribute xsdAttribute = (XSDAttribute) iter.next();
			entity.addAttribute(xsdAttribute);
		}
		//TODO: Add simple type elements to entity here
		if (flatSubstructures) {
			// TODO: Create more entity attributes
			logger.warn("Unimplemented");
		}
		else {
			XEREntity foreignEntity = parseModelGroup(content.getModelGroup(), parentEntity);
			entity.addForeignEntity(foreignEntity);
			model.addEntity(foreignEntity);
		}

		return entity;

	}

	/**
	 * 
	 * @param modelGroup - A model of a Group/Choice/Element/Sequence
	 * @return - The name given to the model group for referencing as a foreign entity
	 */
	XEREntity parseModelGroup(XSDAbstractModelGroup modelGroup, XEREntity parent) {
		
		//TODO: Surely the following case should never occur?
		//add foreign key attribute unless the model is part of a top level element
		if (parent != null) {
			//parent.addXERAttribute();
			parent.addAttribute(new XSDAttribute(modelGroup.getModelGroupName(),1,1));
		}
		
		if (modelGroup instanceof XSDSequence) {
			return modelGroupParser.parseSequence(modelGroup);
		}
		else if (modelGroup instanceof XSDChoice) {
			return modelGroupParser.parseChoice(modelGroup);
		}
		else if (modelGroup instanceof XSDGroup) {
			return modelGroupParser.parseGroup(modelGroup);
		}
		else if (modelGroup instanceof XSDAll) {
			return modelGroupParser.parseAll(modelGroup);
		}
		else {
			logger.error("Invalid model group attempting to be parsed.");
			return null;
		}
	}

	XERComponent parseTopLevelElement(String elementName) {
		String elementType = xsdWalker.getElementType(elementName);
		logger.debug("Element type: " + elementType);
		if (xsdWalker.isComplexType(elementType)) {
			return parseComplexElement(elementName, elementType, null);
		}
		else {
			logger.warn("Not implemented parsing simple type");
			return null;
		}
	}

	boolean isComplexType(String typeName) {
		//logger.debug("Is " + typeName + " a complex type? " + xsdWalker.isComplexType(typeName));
		return xsdWalker.isComplexType(typeName);
	}

	/**
	 * @param type
	 * @return
	 */
	public boolean isSimpleType(String typeName) {
		//TODO: It may not be enough to find the complement of whether it's a complex type 
		return !isComplexType(typeName);
	}

	/**
	 * @return
	 */
	XSDSchemaManipulator getXsdWalker() {
		return xsdWalker;
	}

}
