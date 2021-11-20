/*
 * Created on 18-May-2004
 *
 */
package xtractor.schemaConverter.xer.xerConstructs;

import xtractor.schemaConverter.xer.XERBuilder;

/**
 * @author ss401
 * An XERCompoundConstruct is an XER component which can contain any other constructs
 * (i.e. it's non-terminal node in the XER hierarchy)
 */
public abstract class XERCompoundConstruct extends XERConstruct{

	//Note the attributes collection contains ALL attributes (including primary and foreign key attributes)
	/*Map attributes;
	Collection keyAttributes;
	Collection foreignKeyAttributes;*/

	/**
	 * @param xerBuilder
	 */
	public XERCompoundConstruct(XERBuilder xerBuilder) {
		super(xerBuilder);
	}

	/**
	 * The implementations of this methods will have to use instanceof
	 * to see what to do exactly.
	 * @param xerConstruct - An XEREntity, XERGeneralization etc.
	 * @param minOccurs
	 * @param maxOccurs
	 * Note that the cardinalities will be of use when creating relationships 
	 * (so when adding an attribute they serve little purpose), but the information
	 * should still be available
	 */
	public abstract void addConstruct(XERConstruct xerConstruct,int minOccurs,int maxOccurs);
	
	/**
	 * @param attributeName - The name of the attribute you are trying to fetch
	 * @return - The XERAttribute which this compound construct contains
	 */
	//public abstract XERAttribute getAttribute(String attributeName);

	



	


//	SHOULDN'T BE HERE. THEY ONLY APPLY TO XER ENTITIES
//	/**
//	 * @param parent - The parent XERCompoundConstruct in the XML hierarchy
//	 */
//	public abstract void setImplicitParent(XERCompoundConstruct parent);
//	
//	/**
//	 * @return - Gets the parent XERStructure in the XML hierarchy(e.g. for 'name' it should be 'supplier')
//	 */
//	public abstract XERCompoundConstruct getImplicitParent();
	

}
