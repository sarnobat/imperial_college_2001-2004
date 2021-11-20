import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

import xer.XEREntity;
import xsd.XSDAbstractModelGroup;
import xsd.XSDAttribute;
import xsd.XSDSequence;

/*
 * Created on 17-Feb-2004
 *
 */

/**
 * @author ss401
 *
 */
public class ModelGroupParser {

	XERBuilder builder = new XERBuilder();

	/**
	 * @param builder
	 */
	public ModelGroupParser(XERBuilder builder) {
		this.builder = builder;
	}

	int sequenceCount = 0;

	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @param modelGroup
	 * @return
	 */
	public XEREntity parseAll(XSDAbstractModelGroup modelGroup) {
		// UNIMPLEMENTED: Auto-generated method stub
		logger.warn("Unimplemented");
		return null;
	}

	/**
	 * @param modelGroup
	 * @return
	 */
	public XEREntity parseGroup(XSDAbstractModelGroup modelGroup) {
		// UNIMPLEMENTED: Auto-generated method stub
		logger.warn("Unimplemented");
		return null;
	}

	/**
	 * @param modelGroup
	 * @return
	 */
	public XEREntity parseChoice(XSDAbstractModelGroup modelGroup) {
		// UNIMPLEMENTED: Auto-generated method stub
		logger.warn("Unimplemented");
		return null;
	}

	/**
	 * @param modelGroup
	 * @return
	 */
	public XEREntity parseSequence(XSDAbstractModelGroup modelGroup) {

		//Entity is ensured to have a unique name by using a counter
		// TODO: Perhaps use the parent table name in the name instead
		XEREntity entity = new XEREntity(modelGroup.getModelGroupName());//modelGroup.getModelGroupId());
		sequenceCount++;

		XSDSequence sequence = (XSDSequence) modelGroup;
		List sequenceElements = sequence.getChildrenElements();
		for (Iterator iter = sequenceElements.iterator(); iter.hasNext();) {
			Element child = (Element) iter.next();
			//This should add all the attributes etc. to the entity passed
			parseComponent(child, entity);
		}
		return entity;
	}

	/**
	 * @param child - A JDom element in an XML Schema
	 * @param entity - The XER entity which this element originates from
	 * @return - The XERComponent which the element corresponds to  
	 */
	private void parseComponent(Element component, XEREntity entity) {
		String componentName = component.getName();
		if (componentName.equals("element")) {
			parseElement(component, entity);
		}
		else if (componentName.equals("complexType")) {
			logger.warn("Unimplemented");

		}
		else if (componentName.equals("attribute")) {
			logger.warn("Unimplemented");

		}
		else if (componentName.equals("simpleType")) {
			logger.warn("Unimplemented");

		}
		else {
			logger.error("Invalid component.");

		}
	}

	/**
	 * @param element - A JDom model of an <xsd:element..> component
	 * @param entity - The entity to which the element belongs
	 */
	private void parseElement(Element element, XEREntity parentEntity) {
		String type = element.getAttributeValue("type");
		//logger.debug("Attempting to determine whether " + type + " (the type of '" + element.getAttributeValue("name") + "') is complex...");
		if (builder.isComplexType(type)) {
			//TODO: Parse complex type, but need to pass on the parent entity. Partly reusing code
			//from builder
			parseComplexElement(element, parentEntity);
		}
		else if (builder.isSimpleType(type)) {
			Element typeDefinitionElement = builder.getXsdWalker().getTypeDefinitionElement(type);
			// The following variable will only be non-null if the type is declared in this xml schema
			if (typeDefinitionElement == null) {
				//logger.debug("Type not part of default namespace. Assuming it's part of the W3C XML Schema namespace.");
				parentEntity.addAttribute(new XSDAttribute(element));
			}
			else {
				parseSimpleElement(typeDefinitionElement, parentEntity);
			}
		}
		else {
			logger.warn("Unimplemented");
		}
	}

	/**
	 * @param element
	 */
	private void parseComplexElement(Element element, XEREntity parentEntity) {
		builder.parseComplexElement(element.getAttributeValue("name"), element.getAttributeValue("type"), parentEntity);
	}

	/**
	 * @param type
	 * @param parentEntity
	 */
	private void parseSimpleElement(Element typeDefinitionElement, XEREntity parentEntity) {
		parentEntity.addAttribute(new XSDAttribute(typeDefinitionElement));
	}

}
