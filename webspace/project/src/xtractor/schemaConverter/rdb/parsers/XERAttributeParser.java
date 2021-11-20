/*
 * Created on 19-May-2004
 *
 */
package xtractor.schemaConverter.rdb.parsers;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import xtractor.schemaConverter.exception.EntityNotParsedException;
import xtractor.schemaConverter.exception.MultiValuedAttributeException;
import xtractor.schemaConverter.rdb.RDBBuilder;
import xtractor.schemaConverter.xer.xerConstructs.XERAttribute;
import xtractor.schemaConverter.xer.xerConstructs.XEREntity;
import xtractor.schemaConverter.xer.xerConstructs.XERForeignKey;
import xtractor.schemaConverter.xer.xerConstructs.XERPrimaryKey;

/**
 * @author ss401
 *
 */
public class XERAttributeParser extends XERAbstractParser {

	//Writer out;
	//SHOULD NOT HAVE ACCESS TO THE WRITER; IT SHOULD ONLY RETURN STRINGS

	/**
	 * @param builder
	 */
	public XERAttributeParser(RDBBuilder builder) {
		//this.out = builder.getWriter();
		super(builder);
	}

	/**
	 * 
	 * @param attribute - The XER Attribute whose line of SQL we wish to create
	 * @return - The line of SQL which declares the column within a table 
	 * @throws IOException
	 * @throws EntityNotParsedException - If the attribute is a foreign key to an entity's table which doesn't yet exist
	 */
	public String parseAttribute(XERAttribute attribute)
		throws IOException, EntityNotParsedException, MultiValuedAttributeException {

		// Determine if the attribute is multi-valued
		if (attribute.getMaxOccurs() < 2) {
			// Single valued

			String sql = attribute.getName() + DELIMITER + resolveXSDType(attribute.getType());

			// Add extras
			if (attribute instanceof XERPrimaryKey) {
				//sql += DELIMITER + "PRIMARY KEY";
				// This is done at the end of all the attributes

			}
			else if (attribute instanceof XERForeignKey) {
				//TODO: Handle foreign keys
				//throw an exception if it references something not yet created

				//We should use all relationship information, not just the entity field in the xerforeignkey object
			}
			return sql;
		}
		else {

			throw new MultiValuedAttributeException();

		}

	}

	/**
	 * @param attribute
	 * @param xerEntity
	 * @return
	 */
	public String parseMultiValuedAttribute(XERAttribute attribute, XEREntity parentEntity) {
		String metaElementInformation = getElementSystemInformation(attribute,parentEntity);
		//String characterDataMetaEntry = systemTableHandler.addCharacterDataElement(attribute);
		//String foreignTableSQL = systemTableHandler.addElementLocation(attribute.getName(),attribute.getName(),attribute.getName());
		String foreignTableSQL = "";

		/*
		 * Create the data table 
		 */
		String dataTableName = getDataTableName(attribute);		
		foreignTableSQL += "CREATE TABLE " + dataTableName + " (\n";
		Collection foreignKeyAttributes = parentEntity.getPrimaryKeys();
		String foreignKeyAttributeNames = "";
		Collection allDataTableColumns = new LinkedList(foreignKeyAttributes);
		for (Iterator iter = foreignKeyAttributes.iterator(); iter.hasNext();) {
			XERPrimaryKey foreignKey = (XERPrimaryKey) iter.next();

			// Data table entry
			foreignTableSQL += DELIMITER
				+ foreignKey.getName()
				+ DELIMITER
				+ resolveXSDType(foreignKey.getType())
				+ DELIMITER
				+ "REFERENCES "
				+ databaseSchema
				+ "."
				+ parentEntity.getName()
				+ "("
				+ foreignKey.getName()
				+ "),\n";

			// Other
			foreignKeyAttributeNames += foreignKey.getName();
			if (iter.hasNext()) {
				foreignKeyAttributeNames += ",";
			}

		}
		allDataTableColumns.add(attribute);
		foreignTableSQL += DELIMITER + attribute.getName() + DELIMITER + resolveXSDType(attribute.getType()) + ",\n";
		foreignTableSQL += DELIMITER + "PRIMARY KEY(" + foreignKeyAttributeNames + "," + attribute.getName() + ")" + "\n);\n";

		/*
		 * Create the meta table
		 */
		foreignTableSQL += "CREATE TABLE " + databaseSchema + ".META_" + attribute.getName() + " (\n";
		// id
		foreignTableSQL += DELIMITER + "id" + DELIMITER + "SERIAL" + DELIMITER + "PRIMARY KEY,\n";
		// pid
		foreignTableSQL += DELIMITER
			+ "pid"
			+ DELIMITER
			+ "INT"
			+ DELIMITER
			+ "NOT NULL"
			+ DELIMITER
			+ "REFERENCES "
			+ databaseSchema
			+ ".META_"
			+ parentEntity.getName()
			+ "(id),\n";
		// ord
		foreignTableSQL += DELIMITER + "ord" + DELIMITER + "INT,\n";
		// foreign keys - all columns in data table 
		String allDataTableColumnNames = "";
		for (Iterator iter = allDataTableColumns.iterator(); iter.hasNext();) {
			XERAttribute dataTableAttribute = (XERAttribute) iter.next();
			foreignTableSQL += DELIMITER
				+ dataTableAttribute.getName()
				+ DELIMITER
				+ resolveXSDType(dataTableAttribute.getType());
			allDataTableColumnNames += dataTableAttribute.getName();

			if (iter.hasNext()) {
				allDataTableColumnNames += ",";
			}
			/*+ DELIMITER
			+ "REFERENCES "
			+ dataTableName
			+ "("
			+ dataTableAttribute.getName()
			+ ")"*/;

			foreignTableSQL += ",\n";
		}
		foreignTableSQL += DELIMITER
			+ "FOREIGN KEY ("
			+ allDataTableColumnNames
			+ ") REFERENCES "
			+ dataTableName
			+ " ("
			+ allDataTableColumnNames
			+ ")";
		//foreignTableSQL += "\n);" + characterDataMetaEntry;
		foreignTableSQL += "\n);" +metaElementInformation;
		return foreignTableSQL;
	}

	/**
	 * @param attribute
	 * @param parentEntity
	 * @return
	 */
	private String getElementSystemInformation(XERAttribute attribute, XEREntity parentEntity) {
		String sql = systemTableHandler.insertElementInformation(attribute,parentEntity.getName(),null,"simple");
		return sql;
	}
}
