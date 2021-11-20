/*
 * Created on 25-Feb-2004
 *
 */
package xtractor.schemaConverter.xsd.componentParsers;

import org.apache.log4j.Logger;
import org.jdom.Element;

import xtractor.schemaConverter.xer.XERBuilder;
import xtractor.schemaConverter.xer.xerConstructs.XERCompoundConstruct;
import xtractor.schemaConverter.xsd.XSDReader;
import xtractor.schemaConverter.xsd.componentParsers.modelGroupParsers.ChoiceParser;
import xtractor.schemaConverter.xsd.componentParsers.modelGroupParsers.SequenceParser;
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

	public ModelGroupParser(XSDReader schemaWalker, XERBuilder xerBuilder) {
		super(schemaWalker,xerBuilder);
		
		choiceParser = new ChoiceParser(schemaWalker,xerBuilder);
		sequenceParser = new SequenceParser(schemaWalker,xerBuilder); 
	}

	/**
	 * @param modelGroupElement - JDOM representation of the xsd:group, xsd:all, xsd:choice or xsd:sequence component
	 * @param parent - XERCompoundConstruct to which this model group's contents will be added to
	 */
	public void parseModelGroup(Element modelGroupElement, XERCompoundConstruct parent) {
		String elementType = modelGroupElement.getName();
			
		if (elementType.equals("sequence")) {
			/*XEREntity referredEntity = xerBuilder.getXERFactory().createXEREntity(parent.getName()+"_sequence");
			XERRelationship relationship = xerBuilder.getXERFactory().createXERRelationship(parent.getName()+"_sequence",parent,referredEntity,modelGroupElement);
			sequenceParser.parseSequence(modelGroupElement, referredEntity);*/
			sequenceParser.parseSequence(modelGroupElement,parent);
		}
		else if (elementType.equals("choice")) {
			choiceParser.parseChoice(modelGroupElement,parent);
		}
		/*else if (elementType.equals("group")) {
			logger.warn("Unimplemented");
		}
		else if (elementType.equals("all")) {
			logger.warn("Unimplemented");
		}
		else {
			logger.warn("Unimplemented");
		}*/
	}

}

