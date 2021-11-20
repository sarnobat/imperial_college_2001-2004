/*
 * Created on 25-Feb-2004
 *
 */
package xtractor.schemaConverter.xsd.componentParsers;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.jdom.Element;

import xtractor.schemaConverter.xer.XERBuilder;
import xtractor.schemaConverter.xer.XEREntity;
import xtractor.schemaConverter.xsd.XSDSchemaManipulator;
/*
 * TODO: we still need separate tables otherwise you can't handle nested sequences
 * 
 *
 */
/**
 * @author ss401
 *
 */
public class SequenceParser extends AbstractParser{
	Logger logger = Logger.getLogger(this.getClass());

	public SequenceParser(XSDSchemaManipulator schemaWalker, XERBuilder xerBuilder) {
		super(schemaWalker,xerBuilder);
	}

	/**
	 * @param sequenceComponent - JDOM representation of the xsd:sequence component
	 * @param sequenceEntity - XEREntity which this sequence's contents is going to be added to
	 */
	public void parseSequence(Element sequenceComponent, XEREntity sequenceEntity) {
		Collection children = sequenceComponent.getChildren();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			Element child = (Element) iter.next();
			String childType = child.getName();

			if (childType.equals("element")) {
				//MULTIPLE
				//if(sequenceEntity.containtsAttribute(child.getAttributeValue()))
				xerBuilder.getElementParser().parseElement(child,sequenceEntity);
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
