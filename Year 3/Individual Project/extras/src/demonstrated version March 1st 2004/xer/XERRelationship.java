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
public class XERRelationship {

	Logger logger = Logger.getLogger(this.getClass());
	XEREntity parent;
	XEREntity child;
	int minOccurs, maxOccurs;

	public XERRelationship(XEREntity parent, XEREntity child, Element xsdComplexElement) {
		this.parent = parent;
		this.child = child;
		minOccurs = resolveCardinality(xsdComplexElement.getAttributeValue("minOccurs"));
		maxOccurs = resolveCardinality(xsdComplexElement.getAttributeValue("maxOccurs"));
	}

	/**
	 * @return - The entity which is making the reference
	 */
	public XEREntity getParent() {
		return parent;
	}

	/**
	 * @return - The entity that is BEING referenced
	 */
	public XEREntity getChild() {
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
		else if (occurValue.equals("union")){
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
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return parent.getName() + "-->" + child.getName();
	}

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

}
