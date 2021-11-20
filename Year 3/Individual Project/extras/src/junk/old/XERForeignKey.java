/*
 * Created on 01-Mar-2004
 *
 */
package xtractor.schemaConverter.xer;

import org.apache.log4j.Logger;

/**
 * @author ss401
 *
 */
public class XERForeignKey extends XERAttribute {
	Logger logger = Logger.getLogger(this.getClass());
	XEREntity foreignEntity;

	/**
	 * @param xerRelationship - the relationship which requires the changes to the XER Model
	 * @param parentEntityKeyAttribute - the XER Attribute whiich will act as part of the foreign key
	 */
	public XERForeignKey(XERRelationship xerRelationship,XERAttribute parentEntityKeyAttribute) {
		XEREntity referredEntity = xerRelationship.getChild();
		int minOccurs = xerRelationship.getMinOccurs();
		String requirement = resolveMinimumOccurenceCount(minOccurs);
		//TODO: if maxOccurs is greater than 1, need to create an intermediate table

		super.setProperties(xerRelationship.getName(), parentEntityKeyAttribute.getType(), requirement);
		this.foreignEntity = referredEntity;

	}

	public String resolveMinimumOccurenceCount(int minOccurs) {
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
		return requirement;
	}

	/**
	 * @return - The entity this foreign key refers to
	 */
	public XEREntity getForeignEntity() {
		return foreignEntity;
	}
}
