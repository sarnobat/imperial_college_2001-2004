/*
 * Created on 25-Feb-2004
 *
 */
package xtractor.schemaConverter.xsd.componentParsers;

import org.apache.log4j.Logger;
import org.jdom.Element;

import xtractor.schemaConverter.xer.XERBuilder;
import xtractor.schemaConverter.xer.XEREntity;
import xtractor.schemaConverter.xer.XERRelationship;
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
public class ModelGroupParser extends AbstractParser{
	Logger logger = Logger.getLogger(this.getClass());
	SequenceParser sequenceParser;
	ChoiceParser choiceParser;

	public ModelGroupParser(XSDSchemaManipulator schemaWalker, XERBuilder xerBuilder) {
		super(schemaWalker,xerBuilder);
		sequenceParser = new SequenceParser(schemaWalker,xerBuilder);
		choiceParser = new ChoiceParser(schemaWalker,xerBuilder);
	}

	/**
	 * @param modelGroupElement - JDOM representation of the xsd:group, xsd:all, xsd:choice or xsd:sequence component
	 * @param parent - XEREntity which this model group belongs to
	 */
	public void parseModelGroup(Element modelGroupElement, XEREntity parent) {
		String elementType = modelGroupElement.getName();
			
		if (elementType.equals("sequence")) {
			XEREntity referredEntity = xerBuilder.getXERFactory().createXEREntity(parent.getName()+"_sequence");
			XERRelationship relationship = xerBuilder.getXERFactory().createXERRelationship(parent.getName()+"_sequence",parent,referredEntity,modelGroupElement);
			sequenceParser.parseSequence(modelGroupElement, referredEntity);
		}
		else if (elementType.equals("choice")) {
			choiceParser.parseChoice(modelGroupElement,parent);
		}
		else if (elementType.equals("group")) {
			logger.warn("Unimplemented");
		}
		else if (elementType.equals("all")) {
			logger.warn("Unimplemented");
		}
		else {
			logger.warn("Unimplemented");
		}
	}

}

