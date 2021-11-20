/*
 * Created on 15-Mar-2004
 *
 */
package xtractor.schemaConverter.xsd.componentParsers;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

import xtractor.schemaConverter.xer.XERBuilder;
import xtractor.schemaConverter.xer.XEREntity;
import xtractor.schemaConverter.xer.XERGeneralization;
import xtractor.schemaConverter.xer.XERRelationship;
import xtractor.schemaConverter.xsd.XSDSchemaManipulator;

/**
 * @author ss401
 *
 */
public class ChoiceParser extends AbstractParser {
	Logger logger = Logger.getLogger(this.getClass());

	public ChoiceParser(XSDSchemaManipulator schemaWalker, XERBuilder builder) {
		super(schemaWalker,builder);
	}
	/**
	 * @param choiceElement - A jdom <xsd:choice..> element
	 * @param parent - The xsd component which contains the choice tag
	 */
	public void parseChoice(Element choiceElement, XEREntity parent) {

		// Create the intermediate generalization entity
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

		}
		
		
		
		/*List choiceAlternativeElements = choiceElement.getChildren();
		Collection entities = new LinkedList();
		for (Iterator iter = choiceAlternativeElements.iterator(); iter.hasNext();) {
			Element choiceAlternative = (Element) iter.next();
			XEREntity entity = xerBuilder.getElementParser().parseComplexElement(choiceAlternative);
			entities.add(entity);
		}
		
		String generalizationName = parent.getName() + "_choice";
		XERGeneralization xerGeneralization = xerBuilder.getXERFactory().createXERGeneralization(generalizationName,entities);*/

	}
}
