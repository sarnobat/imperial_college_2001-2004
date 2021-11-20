/*
 * Created on 18-May-2004
 *
 */
package xtractor.schemaConverter.xer.xerConstructs;

import xtractor.schemaConverter.xer.XERBuilder;

/**
 * @author ss401
 * To create these, cast an existing XERAttribute and set the properties
 */
public class XERPrimaryKey extends XERAttribute {

	XEREntity keyedEntity;
	

	/**
	 * @param attribute
	 * @param entity
	 */
	public XERPrimaryKey(XERAttribute attribute, XEREntity entity, XERBuilder xerBuilder) {
		super(attribute.getName(), attribute.getType(), attribute.getMinOccurs(), attribute.getMaxOccurs(), xerBuilder,attribute.getOrigination());
		constraints = attribute.getConstraints();
		this.keyedEntity = entity;
	}

}
