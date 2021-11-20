/*
 * Created on 20-May-2004
 *  
 */
package xtractor.schemaConverter.rdb.parsers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import xtractor.schemaConverter.rdb.RDBBuilder;
import xtractor.schemaConverter.xer.xerConstructs.XERAttribute;
import xtractor.schemaConverter.xer.xerConstructs.XERCompoundConstruct;
import xtractor.schemaConverter.xer.xerConstructs.XERConstruct;
import xtractor.schemaConverter.xer.xerConstructs.XEREntity;
import xtractor.schemaConverter.xer.xerConstructs.XERExplicitRelationship;
import xtractor.schemaConverter.xer.xerConstructs.XERForeignKey;
import xtractor.schemaConverter.xer.xerConstructs.XERGeneralization;
import xtractor.schemaConverter.xer.xerConstructs.XERImplicitRelationship;
import xtractor.schemaConverter.xer.xerConstructs.XERMixedEntity;
import xtractor.schemaConverter.xer.xerConstructs.XERPrimaryKey;
import xtractor.schemaConverter.xer.xerConstructs.XERRelationship;

/**
 * @author ss401
 *  
 */
public class XERRelationshipParser extends XERAbstractParser {

	/**
	 * @param builder
	 */
	public XERRelationshipParser(RDBBuilder builder) {
		super(builder);
	}

	/**
	 * @param relationship
	 * @return
	 */
	public String parseRelationship(XERRelationship relationship) {

		String sql = "";

		if (relationship instanceof XERImplicitRelationship) {
			// we know that it cannot be many-to-many, so no need to create an intermediate table
			sql += getElementSystemInformation(relationship);
			sql += parseImplicitRelationship((XERImplicitRelationship) relationship);
		}
		else if (relationship instanceof XERExplicitRelationship) {
			sql += parseExplicitRelationship((XERExplicitRelationship) relationship);
		}
		else {
			logger.error("Invalid case.");
		}
		return sql;

	}

	/**
	 * @param relationship
	 * @return
	 */
	private String getElementSystemInformation(XERRelationship relationship) {
		String sql = "";
		XERCompoundConstruct parent = relationship.getParent();
		XERCompoundConstruct child = relationship.getChild();
		String type = "complex";
		if (child instanceof XERMixedEntity) {
			type = "mixed";
		}

		if (parent instanceof XEREntity && child instanceof XEREntity) {

			sql += systemTableHandler.insertElementInformation((XEREntity) child, parent.getName(), null, type);
		}
		else if (child instanceof XERGeneralization && parent instanceof XEREntity) {
			XERGeneralization generalization = (XERGeneralization) child;
			Collection childEntities = generalization.getSpecializations();
			for (Iterator iter = childEntities.iterator(); iter.hasNext();) {
				XERConstruct childEntity = (XERConstruct) iter.next();
				if(childEntity instanceof XEREntity){
					type = "complex";
					sql += systemTableHandler.insertElementInformation((XEREntity)childEntity, parent.getName(), null, type);
				}
				else if(childEntity instanceof XERAttribute){
					type = "simple";
					sql += systemTableHandler.insertElementInformation((XERAttribute)childEntity, parent.getName(), null, type);
				}
				else{
					logger.warn("Unexpected containment case.");				
				}
								
			}
			
		}

		return sql;
	}

	/**
	 * @param relationship
	 * @return
	 */
	private String parseExplicitRelationship(XERExplicitRelationship relationship) {

		//TODO: It may be possible to have many-to-many, relationships,
		//but I don't know how. In such a case we need an intermediate table
		// Perhaps we can detect them when we find two separate relationships which are 'mirrors'
		// of each other

		String sql = "";

		XERCompoundConstruct parent = relationship.getParent();
		Collection parentsForeignKeyAttributes = relationship.getParentsForeignKeyAttributes();

		if (relationship.getMaxOccurs() < 2) {
			// The parent's foreign keys will be in the the expected data table
			return parseCompositeSingleRelationship(relationship);
		}
		else {
			// The multi-valued attribute will mean we have to consult the multi-value table
			return parseAtomicMultiValuedRelationship(relationship);

		}

	}

	/**
	 * By atomic we mean that the foreign key only contains one column in its foreign key By MultValued we mean that the foreign key may
	 * contain a column which has many values
	 * 
	 * @param relationship
	 */
	private String parseAtomicMultiValuedRelationship(XERExplicitRelationship relationship) {
		String sql = "";

		XEREntity child = (XEREntity) relationship.getChild();
		Collection parentsForeignKeys = relationship.getParentsForeignKeyAttributes();
		Collection childsPrimaryKeys = child.getPrimaryKeys();
		//sanity checks
		if (parentsForeignKeys.size() > 1) {
			logger.warn("Unimplemented case where foreign key is a composite and contains a multi-valued attribute.");
			return "";
		}
		//this should be a consequence of the first anyway, but just as a double check		
		if (childsPrimaryKeys.size() > 1) {
			logger.warn("Unimplemented case where foreign key is a composite and contains a multi-valued attribute.");
			return "";
		}

		// Get the parent's single foreign key
		Iterator iter = parentsForeignKeys.iterator();
		XERForeignKey mvaForeignKey = (XERForeignKey) iter.next();
		String foreignKeyName = mvaForeignKey.getName();
		// Get the child's single primary key
		iter = childsPrimaryKeys.iterator();
		XERPrimaryKey primaryKey = (XERPrimaryKey) iter.next();
		String primaryKeyName = primaryKey.getName();

		sql += "ALTER TABLE "
			+ super.getDataTableName(mvaForeignKey)
			+ " ADD CONSTRAINT "
			+ foreignKeyName
			+ "_fkey FOREIGN KEY ("
			+ foreignKeyName
			+ ") REFERENCES "
			+ super.getDataTableName(child)
			+ "("
			+ primaryKeyName
			+ ");\n";

		return sql;

	}

	/**
	 * By composite we mean that the key can consist of several columns
	 * By Single we mean that no column is multi-valued
	 * @param relationship
	 * This is probably the 'obvious' case
	 */
	private String parseCompositeSingleRelationship(XERExplicitRelationship relationship) {

		String sql = "";

		// Determine the names of the foreign keys in the parent
		XERCompoundConstruct parent = relationship.getParent();
		Collection parentsForeignKeys = relationship.getParentsForeignKeyAttributes();
		String foreignKeyNames = "";
		for (Iterator iter = parentsForeignKeys.iterator(); iter.hasNext();) {
			// Determine the name of the foreign key
			XERForeignKey foreignKey = (XERForeignKey) iter.next();
			foreignKeyNames += foreignKey.getName();
		}

		// Determine the names of the primary keys in the child
		XEREntity child = (XEREntity) relationship.getChild();
		Collection childsPrimaryKeys = child.getPrimaryKeys();
		String primaryKeyNames = "";
		for (Iterator iter = childsPrimaryKeys.iterator(); iter.hasNext();) {
			XERPrimaryKey key = (XERPrimaryKey) iter.next();
			primaryKeyNames += key.getName();
		}

		sql += "ALTER TABLE "
			+ super.getDataTableName(parent)
			+ " ADD CONSTRAINT "
			+ parent.getName()
			+ "_fkey FOREIGN KEY ("
			+ foreignKeyNames
			+ ") REFERENCES "
			+ super.getDataTableName(child)
			+ "("
			+ primaryKeyNames
			+ ") MATCH FULL;\n";

		return sql;
	}

	/**
	 * @param relationship
	 * @return
	 */

	//TODO: To take account of the fact that relationships may have a maximum cardinality of 1, we need to add 'UNIQUE' int the child
	private String parseImplicitRelationship(XERImplicitRelationship relationship) {
		String sql = "";

		XERCompoundConstruct parent = relationship.getParent();
		XERCompoundConstruct child = relationship.getChild();

		/*
		 * IF parent is a generalization
		 * 		create a primary key in its table
		 * ELSE IF parent is an entity
		 * 		IF parent has no key attributes
		 * 			create a primary key in its table
		 * 	
		 * Create a foreign key in the child referencing the key of the parent
		 * (we don't need to know what the child is at XER level;
		 * we're just manipulating the table representing whatever it is)
		 * 
		 */

		/* Ensure that the table for the parent compound construct has a primary key
		 * so that the child can reference it */
		Map parentKeyNamesToTypes = new HashMap();

		// If no primary key exists in the table for the parent, we must create one
		if (parent instanceof XERGeneralization) {
			// An XER Generalization cannot possibly have a key
			sql += createPrimaryKeyForTable(parent, parentKeyNamesToTypes);
		}
		else if (parent instanceof XEREntity) {
			if (((XEREntity) parent).getPrimaryKeys().size() == 0) {
				// The parent entity doesn't have a primary key, so we must create one
				sql += createPrimaryKeyForTable(parent, parentKeyNamesToTypes);
			}
			else {
				// parent already has a primary key, so we need to determine it
				Collection keys = ((XEREntity) parent).getPrimaryKeys();
				for (Iterator iter = keys.iterator(); iter.hasNext();) {
					XERPrimaryKey key = (XERPrimaryKey) iter.next();
					parentKeyNamesToTypes.put(key.getName(), resolveXSDType(key.getType()));
				}
			}

		}
		else {
			logger.error("Invalid case.");
		}

		/* Now alter the child's table so that it contains foreign key column(s) referencing
		 * the parent's table */
		String columnNames = "";
		Collection parentsKeyNames = parentKeyNamesToTypes.keySet();
		for (Iterator iter = parentsKeyNames.iterator(); iter.hasNext();) {
			String parentKeyName = (String) iter.next();

			// Create the extra column in the child's data table
			sql += "ALTER TABLE "
				+ getDataTableName(child)
				+ " ADD COLUMN "
				+ parentKeyName
				+ " "
				+ parentKeyNamesToTypes.get(parentKeyName)
				+ ";\n";

			columnNames += parentKeyName;

			if (iter.hasNext()) {
				columnNames += ",";
			}
		}

		// Add the referential integrity constraint
		sql += "ALTER TABLE "
			+ getDataTableName(child)
			+ " ADD CONSTRAINT "
			+ parent.getName()
			+ "_fkey FOREIGN KEY ("
			+ columnNames
			+ ") REFERENCES "
			+ getDataTableName(parent)
			+ "("
			+ columnNames
			+ ") MATCH FULL;\n";

		return sql;
	}

	/**
	 * Adds a primary key attribute to the table corresponding to 'parent'
	 * and a foreign key in the meta table
	 * @param xerCompoundConstruct - a compound construct whose table has no primary key 
	 * @param namesToTypes - A map of names (String) to SQL types (String)
	 * @return - The SQL code necessary to create the primary key
	 */
	private String createPrimaryKeyForTable(XERCompoundConstruct parent, Map namesToTypes) {
		String sql = "";
		String dataTableName = getDataTableName(parent);
		String dataTableKeyColumnName = parent.getName() + "_pkey";

		// Create the extra column
		sql += "ALTER TABLE " + dataTableName + " ADD COLUMN " + dataTableKeyColumnName + " INT;\n";
		sql += "ALTER TABLE " + dataTableName + " ALTER COLUMN " + dataTableKeyColumnName + " SET NOT NULL;\n";

		// Make it a key column
		sql += "ALTER TABLE " + dataTableName + " ADD PRIMARY KEY (" + dataTableKeyColumnName + ");\n";

		// Create a sequence so we can guarantee the key value is unique
		sql += "CREATE SEQUENCE " + databaseSchema + "." + dataTableKeyColumnName + "_seq MINVALUE 1;\n";

		if (parent instanceof XEREntity) {
			// XER Entity has a meta table too, so we need to add a foreign key to the data table
			String metaTableName = getMetaTableName((XEREntity) parent);

			// Create the extra column
			sql += "ALTER TABLE " + metaTableName + " ADD COLUMN " + dataTableKeyColumnName + " INT;\n";
			sql += "ALTER TABLE " + metaTableName + " ALTER COLUMN " + dataTableKeyColumnName + " SET NOT NULL;\n";

			// Add the referential integrity constraint
			sql += "ALTER TABLE "
				+ metaTableName
				+ " ADD CONSTRAINT "
				+ parent.getName()
				+ "_fkey FOREIGN KEY ("
				+ dataTableKeyColumnName
				+ ") REFERENCES "
				+ dataTableName
				+ "("
				+ dataTableKeyColumnName
				+ ") MATCH FULL;\n";
		}

		// Add information about this created key to a map for use by the caller 
		namesToTypes.put(dataTableKeyColumnName, "INT");
		return sql;
	}
}
