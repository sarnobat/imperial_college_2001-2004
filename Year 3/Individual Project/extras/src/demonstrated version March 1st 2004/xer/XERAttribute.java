/*
 * Created on 24-Feb-2004
 *
 */
package xer;

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * @author ss401
 *
 */
public class XERAttribute {
	Logger logger = Logger.getLogger(this.getClass());
	String type;
	boolean required;
	String name;
	XEREntity referredEntity;

	/**
	 * Constructor used for creating non-foreign key attributes
	 * @param attributeComponent - The JDom representation of an xsd:attribute component
	 */
	public XERAttribute(Element attributeComponent) {
		setProperties(
			attributeComponent.getAttributeValue("name"),
			attributeComponent.getAttributeValue("type"),
			attributeComponent.getAttributeValue("use"));
	}
	/**
	 * Constructor used for creating primary key attributes.
	 * @param keyedEntity - The entity which a primary key needs to be created for
	 */
	public XERAttribute(XEREntity keyedEntity) {
		setProperties(keyedEntity.getName() + "_ID","primary_key_auto_number","required");
		// "primary_key_auto_number" isn't really and xsd keyword
		//keyedEntity.addKeyAttribute(this); - DONE ELSEWHERE
	}

	/**
	 * Use this for creating XER attributes which are foreign keys
	 * @param xerEntity - the foreign entity which this attribute will act as a key for
	 */
	public XERAttribute(XERAttribute parentEntityKeyAttribute, XEREntity referredEntity, int minOccurs) {
		String requirement = null;
		if (minOccurs == 0) {
			requirement = "optional";
		}
		else if (minOccurs > 0) {
			requirement = "required";
		}
		else {
			logger.error("Invalid requirement specification");
		}
		setProperties(referredEntity.getName() + "_" + parentEntityKeyAttribute.getName(), parentEntityKeyAttribute.getType(), requirement);
		//TODO: Not sure how to specify maxOccurs in SQL
		this.referredEntity = referredEntity;
	}
	/**
	 * Private constructor which performs the common operations of the public constructors
	 * @param name - The name of the attribute
	 * @param type - The XML Schema type of the attribte
	 * @param requirement - "optional", "required" etc. 
	 */
	private void setProperties(String name, String type, String requirement) {
		this.name = name;
		this.type = type;
		this.required = resolveCardinality(requirement);
	}

	/**
	 * Converts cardinalities expressed in XML schema terminology into boolean
	 * @param requirement - "required","optional" etc.
	 * @return - True if the attribute is required
	 */
	public boolean resolveCardinality(String requirement) {
		if (requirement == null) {
			//Presumalby this is correct
			return true;
		}
		else if (requirement.equals("required")) {
			return true;
		}
		else if (requirement.equals("optional")) {
			return false;
		}
		else {
			logger.error("Invalid requirement specification");
			return false;
		}
	}

	/**
	 * @return - The XML Schema datatype which this attribute conforms to
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return - Indicates whether this attribute is required in the entity
	 */
	public boolean isRequired() {
		return required;

	}
	/**
	 * @return - The name of the attribute
	 */
	public String getName() {
		return name;
	}
	/**
	 * Returns null if the attribute isn't a foreign key
	 * @return - The XEREntity which the attribute acts as a foreign key to
	 */
	public XEREntity getReferredEntity() {
		return referredEntity;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getName();
	}

}
