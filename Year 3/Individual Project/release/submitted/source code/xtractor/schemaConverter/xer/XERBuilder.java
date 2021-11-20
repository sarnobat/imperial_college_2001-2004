/*
 * Created on 24-Feb-2004
 *
 */
package xtractor.schemaConverter.xer;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

import xtractor.schemaConverter.xsd.XSDReader;
import xtractor.schemaConverter.xsd.componentParsers.AttributeParser;
import xtractor.schemaConverter.xsd.componentParsers.ElementParser;
import xtractor.schemaConverter.xsd.componentParsers.KeyParser;
import xtractor.schemaConverter.xsd.componentParsers.ModelGroupParser;
import xtractor.schemaConverter.xsd.componentParsers.RelationshipParser;
import xtractor.schemaConverter.xsd.componentParsers.SimpleTypeDefinitionDetails;

/**
 * @author ss401
 * Takes an XSD Schema Info model and generates an XER model
 */
public class XERBuilder {

	Logger logger = Logger.getLogger(this.getClass());

	//Parsers
	SimpleTypeDefinitionDetails simpleTypeParser;
	ElementParser elementParser;
	AttributeParser attributeParser;
	ModelGroupParser modelGroupParser;
	KeyParser keyParser;
	RelationshipParser relationshipParser;

	XERFactory factory;
	XERModel model;
	XSDReader schemaWalker;
	String schemaName;

	/**
	 * @param schemaFile
	 */
	public XERBuilder(File schemaFile) {
		this.schemaWalker = new XSDReader(schemaFile);
		this.factory = new XERFactory(this);
		this.elementParser = new ElementParser(schemaWalker, this);
		this.attributeParser = new AttributeParser(schemaWalker, this);
		this.modelGroupParser = new ModelGroupParser(schemaWalker, this);
		this.relationshipParser = new RelationshipParser(schemaWalker, this);
		this.keyParser = new KeyParser(schemaWalker, this);

		String fileName = schemaFile.getName();
		String[] pathFragments = fileName.split(".x");
		this.schemaName = pathFragments[0];
	}

	/**
	 * @param schemaURL - the relative path of the XML Schema
	 * @return - an XER Model of the schema
	 */
	public XERModel build() {
		// This is a recursive build rather than an iterative build
		// However, new entities and relationships created must be registered with the XERModel
		model = factory.createXERModel(schemaName);
		Element rootElement = schemaWalker.getRootElement();
		elementParser.parseRootElement(rootElement);

		// Now deal with explicit relationships and keys
		List rootElementChildren = rootElement.getChildren();
		for (Iterator iter = rootElementChildren.iterator(); iter.hasNext();) {
			Element child = (Element) iter.next();

			String elementName = child.getName();

			if (elementName.equals("key")) {
				// Deal with the key
				getKeyParser().parseKey(child);
			}
			else if (elementName.equals("keyref")) {
				// Deal with a relationship
				getKeyParser().parseKeyRef(child);
			}

		}

		//model.setRootElement(rootEntity);
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

	/**
	 * Convenience method
	 * @return - The XER Factory to create the XER components
	 */
	public XERFactory getXERFactory() {
		return factory;
	}

	/**
	 * Conveneince method
	 * @return - The parser for <xsd:simpleType...> elements
	 */
	public SimpleTypeDefinitionDetails getSimpleTypeParser() {
		return simpleTypeParser;
	}

	/**
	 * Conveneince method
	 * @return - The parser for <xsd:key..> elements
	 */
	public KeyParser getKeyParser() {
		return keyParser;
	}

	/**
	 * Convenience method
	 * @return - The parser for creating relationships between XER constructs
	 */
	public RelationshipParser getRelationshipParser() {
		return this.relationshipParser;
	}

}
