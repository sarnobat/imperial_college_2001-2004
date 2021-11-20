/*
 * Created on 15-Mar-2004
 *
 */
package xtractor.schemaConverter.xsd.componentParsers.modelGroupParsers;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

import xtractor.schemaConverter.xer.XERBuilder;
import xtractor.schemaConverter.xer.xerConstructs.XERCompoundConstruct;
import xtractor.schemaConverter.xer.xerConstructs.XERGeneralization;
import xtractor.schemaConverter.xsd.XSDReader;
import xtractor.schemaConverter.xsd.componentParsers.AbstractParser;

/**
 * @author ss401
 *
 */
public class ChoiceParser extends AbstractParser {
	Logger logger = Logger.getLogger(this.getClass());

	public ChoiceParser(XSDReader schemaWalker, XERBuilder builder) {
		super(schemaWalker, builder);
	}
	/**
	 * @param choiceElement - A jdom <xsd:choice..> element
	 * @param parent - The xsd component which contains the choice tag
	 */
	public void parseChoice(Element choiceElement, XERCompoundConstruct parent) {

		/*// Create the intermediate generalization entity
		String supersetName = parent.getName() + "_choice";
		
		XEREntity superEntity = xerBuilder.getXERFactory().createXEREntity(supersetName); 
		
		// Associate the parent with the super entity
		XERRelationship parentToSuperEntity =
			xerBuilder.getXERFactory().createXERRelationship(supersetName, parent, superEntity, choiceElement);
		
		for (Iterator iter = choiceElement.getChildren().iterator(); iter.hasNext();) {
		
			Element child = (Element) iter.next();
			String elementType = child.getName();
		
			if (elementType.equals("element")) {
				xerBuilder.getElementParser().parseElement(child, superEntity);
		
			}
			else if (elementType.equals("choice") || elementType.equals("group") || elementType.equals("sequence")) {
		
				xerBuilder.getModelGroupParser().parseModelGroup(child, superEntity);
			}
			else if (elementType.equals("any")) {
				logger.warn("Unimplemented.");
			}
			else {
			}
		
		}*/
		String generalizationName = parent.getName() + "_choice";
		int minOccurs = resolveOccurrenceCardinality(choiceElement.getAttributeValue("minOccurs"));
		int maxOccurs = resolveOccurrenceCardinality(choiceElement.getAttributeValue("maxOccurs"));
		XERGeneralization xerGeneralization = xerBuilder.getXERFactory().createXERGeneralization(generalizationName,new LinkedList(),minOccurs,maxOccurs);
		parent.addConstruct(xerGeneralization,minOccurs,maxOccurs);

		// parse each child of the <xsd:choice> element 
		List choiceAlternativeElements = choiceElement.getChildren();
		Collection xerComponents = new LinkedList();
		for (Iterator iter = choiceAlternativeElements.iterator(); iter.hasNext();) {
			Element choiceAlternative = (Element) iter.next();
			String elementType = choiceAlternative.getName();
			if (elementType.equals("element")) {					
				xerBuilder.getElementParser().parseElement(choiceAlternative, xerGeneralization);
			}
		}
	}
}

