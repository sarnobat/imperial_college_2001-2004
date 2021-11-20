/*
 * Created on 19-May-2004
 *
 */
package xtractor.schemaConverter.xer.xerConstructs;

import java.util.Collection;

/**
 * @author ss401
 *
 */
public class XERExplicitRelationship extends XERRelationship {
	
	// A collection of XERForeignKey
	Collection parentsForeignKeyAttributes;
	
	/**
	 * We assume that the parent's foreign key is only one attribute rather than many
	 * @param parent - The xer compound construct making the reference
	 * @param foreignKeyAttributes - A collection of XERForeignKey attributes which refer to the child
	 * @param child - The compound construct BEING referenced
	 * @param minOccurs - The minimum number of times the parent can refer to a child
	 * @param maxOccurs - The maximum number of times the parent can refer to a child
	 */
	public XERExplicitRelationship(XERCompoundConstruct parent,Collection foreignKeyAttributes ,XERCompoundConstruct child, int minOccurs, int maxOccurs) {
		super(parent, child, minOccurs, maxOccurs);
		//sanity checks
		if(!(parent instanceof XEREntity)){
			logger.error("Attempting to create an explicit relationship between constructs other than entities");
		}
		if(!(child instanceof XEREntity)){
			logger.error("Attempting to create an explicit relationship between constructs other than entities");
		}
		this.parentsForeignKeyAttributes = foreignKeyAttributes;
	}

	/**
	 * @return Returns the parentsForeignKeyAttributes.
	 */
	public Collection getParentsForeignKeyAttributes() {
		return parentsForeignKeyAttributes;
	}

}
