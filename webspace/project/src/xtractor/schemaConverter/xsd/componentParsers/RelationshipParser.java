/*
 * Created on 18-May-2004
 *
 */
package xtractor.schemaConverter.xsd.componentParsers;

import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import xtractor.schemaConverter.xer.XERBuilder;
import xtractor.schemaConverter.xer.xerConstructs.XERCompoundConstruct;
import xtractor.schemaConverter.xer.xerConstructs.XEREntity;
import xtractor.schemaConverter.xer.xerConstructs.XERForeignKey;
import xtractor.schemaConverter.xsd.XSDReader;

/**
 * @author ss401
 *
 */
public class RelationshipParser extends AbstractParser {

	/**
	 * @param schemaWalker
	 * @param builder
	 */
	public RelationshipParser(XSDReader schemaWalker, XERBuilder builder) {
		super(schemaWalker, builder);
	}

	/**
	 * An implicit relationship is one where the child is CONTAINED in the parent (like a composition in UML).
	 * The child cannot exist without belonging to a parent (e.g. supplier-->name; a name cannot exist without a supplier)
	 * This may involve creating a new attribute in the parent or chlid (or both) - but not at the XER building stage.
	 * @param parent
	 * @param child
	 * @param minOccurs
	 * @param maxOccurs
	 */
	public void parseImplicitRelationship(
		XERCompoundConstruct parent,
		XERCompoundConstruct child,
		int minOccurs,
		int maxOccurs) {
		//logger.info("Creating implicit relationship between " + parent.getName() + " and " + child.getName());
		xerFactory.createXERImplicitRelationship(parent, child, minOccurs, maxOccurs);

	}

	/**
	 * An explicit relationship is one where the child is related to a parent but not via a containment, similar to an
	 * aggregation in UML (for example, the supplier/suppies-->@pno is explicit; a part isn't 'contained' in a supplier 
	 * - it can exist independently of a supplier). This is between existing attributes and so we do not create new attributes.
	 * @param parentCompoundConstruct - The compound construct making the reference
	 * @param parentsForeignKeys - The RELEVANT XERForeignKey attributes of the parent
	 * @param childCompoundConstruct - The compound construct being referenced
	 * @param minOccurs
	 * @param maxOccurs
	 */
	public void parseExplicitRelationship(
		XEREntity parentCompoundConstruct,
		Collection parentsForeignKeys,
		XEREntity childCompoundConstruct,
		int minOccurs,
		int maxOccurs) {

		xerFactory.createXERExplicitRelationship(
			parentCompoundConstruct,
			parentsForeignKeys,
			childCompoundConstruct,
			minOccurs,
			maxOccurs);

	}

	/**
	 * @param parentCompoundConstruct
	 * @param foreignKeys
	 * @param childCompoundConstruct
	 */
	public void parseExplicitRelationship(
		XEREntity parentCompoundConstruct,
		Collection foreignKeys,
		XEREntity childCompoundConstruct) {

		// We must find out the cardinalities by taking the MINIMUM of all foreign keys
		SortedSet minOccurs = new TreeSet();
		SortedSet maxOccurs = new TreeSet();

		for (Iterator iter = foreignKeys.iterator(); iter.hasNext();) {
			XERForeignKey foreignKey = (XERForeignKey) iter.next();
			minOccurs.add(new Integer(foreignKey.getMinOccurs()));
			maxOccurs.add(new Integer(foreignKey.getMaxOccurs()));
		}

		xerFactory.createXERExplicitRelationship(
			parentCompoundConstruct,
			foreignKeys,
			childCompoundConstruct,
			((Integer) minOccurs.first()).intValue(),
			((Integer) maxOccurs.first()).intValue());

	}

	/*public void parseRelationship(XERCompoundConstruct parent, XERCompoundConstruct child, int minOccurs, int maxOccurs) {
		// Also need to promote the attribute to a foreign key so we know what attribute the relationship relies on
		if (parent instanceof XEREntity) {
			((XEREntity) parent).promoteAttributeToForeignKey(parentAttribute);
		}
		else {
			logger.warn("Unimplemented case.");
		}
	}*/

}
