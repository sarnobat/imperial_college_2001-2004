/*
 * Created on 24-Feb-2004
 *
 */
package xtractor.schemaConverter.xer.xerConstructs;

/**
 * @author ss401
 *
 */
public abstract class XERRelationship extends XERConstruct {

	XERCompoundConstruct parent;
	XERCompoundConstruct child;
	// We don't need to store the backwards cardinalities because they will almost always be 1:1 (need to check this)
	int minOccurs, maxOccurs;

	/**
	 * @param - relationshipName - The name of the attribute that should eventually appear in the parent 
	 * as a foreign key attribute
	 * @param parent - The entity MAKING the reference
	 * @param child - The entity BEING referenced
	 * @param minOccurs - the minimum number of times the child occur 'in' the parent
	 * @param maxOccurs - the maximum number of times the child occur 'in' the parent
	 */
	public XERRelationship(XERCompoundConstruct parent, XERCompoundConstruct child, int minOccurs, int maxOccurs) {
		super(null);
		this.parent = parent;
		this.child = child;
		//setName(parent.getName() + "_HAS_" + child.getName());
		this.minOccurs = minOccurs;
		this.maxOccurs = maxOccurs;
	}

	/**
	 * @return - The XERCompoundConstruct which is making the reference
	 */
	public XERCompoundConstruct getParent() {
		return parent;
	}

	/**
	 * @return - The XERCompoundConstruct that is BEING referenced
	 */
	public XERCompoundConstruct getChild() {
		return child;
	}

	/**
	 * 
	 * @param occurValue - the value of an XML's minOccurs/maxOccurs/... attribute
	 * @return - The numerical cardinality corresponding to the xml representation of a cardinality
	 */
	public int resolveCardinality(String occurValue) {
		if (occurValue == null) {
			return 1;
		}
		else if (occurValue.equals("union")) {
			logger.error("Unimplemented. Same for others presumably");
			return -1;
		}
		else if (occurValue.equals("unbounded")) {
			return 9999;
		}
		else {
			return Integer.parseInt(occurValue);
		}
	}

	//	public String toString() {
	//		return parent.getName() + "-->" + child.getName();
	//	}


	/**
	 * @return - The maximum number of entities that can be referred to by the parent 
	 */
	public int getMaxOccurs() {
		return maxOccurs;
	}

	/**
	 * @return - The minimum number of entities that can be referred to by the parent
	 */
	public int getMinOccurs() {
		return minOccurs;
	}
	
	public String getOriginalName(){
		originalName =parent.getName() + "_HAS_" + child.getName(); 
		return originalName;
	}


}
