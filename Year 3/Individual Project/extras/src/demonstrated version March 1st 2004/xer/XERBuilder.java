/*
 * Created on 24-Feb-2004
 *
 */
package xer;

import org.apache.log4j.Logger;
import org.jdom.Element;

import xsd.XSDSchemaManipulator;
import xsd.componentParsers.*;

/**
 * @author ss401
 * Takes an XSD Schema Info model and generates an XER model
 */
public class XERBuilder {
	ModelGroupParser modelGroupParser;
	Logger logger = Logger.getLogger(this.getClass());
	ElementParser elementParser;
	AttributeParser attributeParser;
	XERModel model;
	XSDSchemaManipulator schemaWalker;
	
	public XERBuilder(String schemaPath) {
		this.schemaWalker = new XSDSchemaManipulator(schemaPath);
		this.elementParser = new ElementParser(schemaWalker,this);
		this.attributeParser = new AttributeParser(schemaWalker,this);
		this.modelGroupParser = new ModelGroupParser(schemaWalker,this);
	}

	/**
	 * @param schemaURL - the relative path of the XML Schema
	 * @return - an XER Model of the schema
	 */
	public XERModel build() {
		// This is a recursive build rather than an iterative build
		// However, new entities and relationships created must be registered with the XERModel
		model = new XERModel();
		Element rootElement = schemaWalker.getRootElement();
		XEREntity rootEntity = elementParser.parseRootElement(rootElement);		
		return model;
	}

	/**
	 * @return - The XER model containing all the entities and relationship 
	 */
	public XERModel getModel() {
		return model;
	}

	/**
	 * Convenience method
	 * @return - The xsd:attribute parser
	 */
	public AttributeParser getAttributeParser() {
		return attributeParser;
	}

	/**
	 * Convenience method
	 * @return - The xsd:element parser
	 */
	public ElementParser getElementParser() {
		return elementParser;
	}

	/**
	 * Convenience method
	 * @return - The parser for xsd:group, xsd:all, xsd:choice and xsd:sequence
	 */
	public ModelGroupParser getModelGroupParser() {
		return modelGroupParser;		
	}

}
