/*
 * Created on 25-Feb-2004
 *
 */
package xsd.componentParsers;

import org.apache.log4j.Logger;
import org.jdom.Element;

import xer.XERBuilder;
import xer.XEREntity;
import xsd.XSDSchemaManipulator;

/**
 * @author ss401
 *
 */
public class ModelGroupParser {
	Logger logger = Logger.getLogger(this.getClass());
	SequenceParser sequenceParser;
	XSDSchemaManipulator schemaWalker;
	XERBuilder xerBuilder;

	public ModelGroupParser(XSDSchemaManipulator schemaWalker, XERBuilder builder) {
		this.sequenceParser = new SequenceParser(schemaWalker, builder);
		this.schemaWalker = schemaWalker;
		this.xerBuilder = builder;
	}

	/**
	 * @param child - JDOM representation of the xsd:group, xsd:all, xsd:choice or xsd:sequence component
	 * @param parent - XEREntity which this model group belongs to
	 */
	public void parseModelGroup(Element child, XEREntity parent) {
		String elementType = child.getName();
		if (elementType.equals("sequence")) {
			sequenceParser.parseSequence(child, parent);
		}

	}
}
