/*
 * Created on 19-May-2004
 *
 */
package xtractor.schem onverter.xer.xerConstructs;

/**
 * @author ss401
 *
 */
public class XERImplicitRelationship extends XERRelationship {

	public XERImplicitRelationship(XERCompoundConstruct parent, XERCompoundConstruct child, int minOccurs, int maxOccurs) {
		super(parent, child, minOccurs, maxOccurs);
	}

}
