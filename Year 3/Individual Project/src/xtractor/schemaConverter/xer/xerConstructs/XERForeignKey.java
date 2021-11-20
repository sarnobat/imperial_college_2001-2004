/*
 * Created on 18-May-2004
 *
 */
package xtractor.schemaConverter.xer.xerConstructs;

import xtractor.schemaConverter.xer.XERBuilder;

/**
 * @author ss401
 *
 */
public class XERForeignKey extends XERAttribute {

	// The construct being referenced (in part) by this foreign key
	XERCompoundConstruct child;

	/**
	 * Note that the attribute may not be the full part of a foreign key
	 * @param attribute - The XERAttribute which we have discovered is PART OF a foreign key
	 * @param child - The XER Compound Construct which is BEING referenced
	 * @param xerBuilder- needed to invoke super constructor
	 */
	public XERForeignKey(XERAttribute attribute, XERCompoundConstruct child, XERBuilder xerBuilder) {
		super(attribute.getName(), attribute.getType(), attribute.getMinOccurs(), attribute.getMaxOccurs(), xerBuilder,attribute.getOrigination());
		this.child = child;
	}
}
