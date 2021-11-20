/*
 * Created on 25-Feb-2004
 *
 */
package xsd.componentParsers;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.jdom.Element;

import xer.XERBuilder;
import xer.XEREntity;
import xsd.XSDSchemaManipulator;

/**
 * @author ss401
 *
 */
public class SequenceParser {
	Logger logger = Logger.getLogger(this.getClass());
	XSDSchemaManipulator schemaWalker;
	XERBuilder xerBuilder;

	public SequenceParser(XSDSchemaManipulator schemaWalker, XERBuilder builder) {
		this.schemaWalker = schemaWalker;
		this.xerBuilder = builder;
	}

	/**
	 * @param sequenceComponent - JDOM representation of the xsd:sequence component
	 * @param parent - XEREntity which this sequence belongs to
	 */
	public void parseSequence(Element sequenceComponent, XEREntity parent) {
		Collection children = sequenceComponent.getChildren();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			Element child = (Element) iter.next();
			String childType = child.getName();

			if (childType.equals("element")) {
				//MULTIPLE
				xerBuilder.getElementParser().parseElement(child,parent);
			}
			else if (childType.equals("choice")) {
				//MULTIPLE
			}
			else if (childType.equals("group")) {
				//MULTIPLE
			}
			else if (childType.equals("sequence")) {
				//MULTIPLE
			}
			else if (childType.equals("any")) {
				//MULTIPLE
			}
			else {
				logger.error("Invalid case.");
			}
		}
	}

}
