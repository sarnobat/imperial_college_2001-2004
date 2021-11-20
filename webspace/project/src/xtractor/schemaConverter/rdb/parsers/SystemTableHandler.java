/*
 * Created on 28-May-2004
 *
 */
package xtractor.schemaConverter.rdb.parsers;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import xtractor.schemaConverter.xer.xerConstructs.XERAttribute;
import xtractor.schemaConverter.xer.xerConstructs.XERConstruct;
import xtractor.schemaConverter.xer.xerConstructs.XEREntity;
import databaseConnection.Nomenclature;
import databaseConnection.databaseManager.AbstractDatabaseManager;

/**
 * @author ss401
 * Note: we cannot use any methods which read the database
 */
public class SystemTableHandler extends AbstractDatabaseManager {

	// This is a list of SQL commands which could not be inserted into the sql
	// script on the fly (e.g. for attributes)
	Collection pendingSystemDataAdditions;

	String DELIMITER = Nomenclature.DELIMITER;

	String[] ELEMENTS_columns =
		{
			Nomenclature.ELEMENTS_name,
			Nomenclature.ELEMENTS_parent_name,
			Nomenclature.ELEMENTS_alias,
			Nomenclature.ELEMENTS_type };
	String[] ATTRIBUTES_columns =
		{ Nomenclature.ATTRIBUTES_name, Nomenclature.ATTRIBUTES_element, Nomenclature.ATTRIBUTES_alias };

	/**
	 * @param databaseSchemaName
	 */
	public SystemTableHandler(String databaseSchemaName) {
		super(databaseSchemaName);
		this.pendingSystemDataAdditions = new LinkedList();
	}

	/**
	 * 
	 */
	public String getTableCreationSQL() {
		String sql = "";
		sql += createElementSystemTable();
		sql += createAttributeSystemTable();
		sql += createGeneralizationSystemTable();

		sql += createViews();

		return sql;
	}

	/**
	 * @return
	 */
	private String createViews() {
		String sql = "";

		// Complex elements
		sql += "CREATE VIEW " + getSystemTableName(Nomenclature.COMPLEX_ELEMENTS) + " AS (\n";
		sql += DELIMITER
			+ "SELECT "
			+ Nomenclature.ELEMENTS_name
			+ "\n"
			+ DELIMITER
			+ "FROM "
			+ getSystemTableName(Nomenclature.ELEMENTS)
			+ "\n"
			+ DELIMITER
			+ "WHERE type='complex' OR type='mixed'";
		sql += "\n);\n";

		// Simple elements
		sql += "CREATE VIEW " + getSystemTableName(Nomenclature.SIMPLE_ELEMENTS) + " AS (\n";
		sql += DELIMITER
			+ "SELECT "
			+ Nomenclature.ELEMENTS_name
			+ "\n"
			+ DELIMITER
			+ "FROM "
			+ getSystemTableName(Nomenclature.ELEMENTS)
			+ "\n"
			+ DELIMITER
			+ "WHERE type='simple'";
		sql += "\n);\n";

		// Mixed elements
		sql += "CREATE VIEW " + getSystemTableName(Nomenclature.MIXED_ELEMENTS) + " AS (\n";
		sql += DELIMITER
			+ "SELECT "
			+ Nomenclature.ELEMENTS_name
			+ "\n"
			+ DELIMITER
			+ "FROM "
			+ getSystemTableName(Nomenclature.ELEMENTS)
			+ "\n"
			+ DELIMITER
			+ "WHERE type='mixed'";
		sql += "\n);\n";
		return sql;
	}

	/**
	 * @return
	 */
	private String createAttributeSystemTable() {
		String sql = "";
		String[] columns = ATTRIBUTES_columns;

		sql += "CREATE TABLE " + getSystemTableName(Nomenclature.ATTRIBUTES) + " (\n";
		for (int i = 0; i < columns.length; i++) {
			sql += "\n" + DELIMITER + columns[i] + DELIMITER + "VARCHAR(50),";
		}
		sql += "\n" + DELIMITER + "PRIMARY KEY(" + Nomenclature.ATTRIBUTES_name + ")";
		sql += "\n);";

		return sql;
	}
	/**
	 * @return
	 */
	private String createElementSystemTable() {
		String sql = "";
		String[] columns = ELEMENTS_columns;

		sql += "CREATE TABLE " + getSystemTableName(Nomenclature.ELEMENTS) + " (\n";
		for (int i = 0; i < columns.length; i++) {
			sql += "\n" + DELIMITER + columns[i] + DELIMITER + "VARCHAR(50),";
		}
		sql += "\n" + DELIMITER + "PRIMARY KEY(" + Nomenclature.ELEMENTS_name + ")";
		sql += "\n);";

		return sql;

	}

	public String insertElementInformation(XERAttribute xerAttribute, String parent_name, String alias, String type) {
		if (xerAttribute.originatesFromXMLAttribute()) {
			logger.error("Cannot add element alias to an xer attribute originating from an xml attribute.");
			return "";
		}
		return insertElementInformationForAnyXERConstruct(xerAttribute, parent_name, alias, type);
	}

	public String insertElementInformation(XEREntity xerEntity, String parent_name, String alias, String type) {
		return insertElementInformationForAnyXERConstruct(xerEntity, parent_name, alias, type);
	}

	/**
	 * We allow nulls
	 * @param xerConstruct - an XER construct that can arise from an XML element (i.e. an attribute or entity)
	 * @param parent_name - The name of the parent XML element (can be null)
	 * @param alias - The name the user gives to the element (can be null)
	 * @param type - simple/mixed/complex
	 */
	private String insertElementInformationForAnyXERConstruct(
		XERConstruct xerConstruct,
		String parent_name,
		String alias,
		String type) {

		// sanity check
		//		if(!(xerConstruct instanceof XERAttribute) && !(xerConstruct instanceof XEREntity)){
		//			logger.error("The xer construct " + xerConstruct.getOriginalName() + " cannot be an XML element.");
		//			return "";
		//		}

		String[] columns = ELEMENTS_columns;

		String elementName = xerConstruct.getOriginalName();

		String sql = "INSERT INTO " + getSystemTableName(Nomenclature.ELEMENTS) + "(";
		for (int i = 0; i < columns.length; i++) {
			sql += columns[i];
			if (i < columns.length - 1) {
				sql += ",";
			}
		}
		sql += ") VALUES (";

		sql += resolveValue(elementName)
			+ ","
			+ resolveValue(parent_name)
			+ ","
			+ resolveValue(alias)
			+ ","
			+ resolveValue(type)
			+ ");";
		// This may cause a problem with null
		//executeUpdate(sql);
		return sql;
	}

	/**
	 * 
	 * @param attributeName - The name of an XML attribute
	 * @param elementResidence - The XML element in which the attribute is found
	 * @param alias - The name the user wishes to give to the attribute
	 */
	public String insertAttribute(XERAttribute xerAttribute, String elementResidence, String alias) {
		String[] columns = ATTRIBUTES_columns;

		String attributeName = xerAttribute.getOriginalName();

		String sql = "INSERT INTO " + getSystemTableName(Nomenclature.ATTRIBUTES) + "(";
		for (int i = 0; i < columns.length; i++) {
			sql += columns[i];
			if (i < columns.length - 1) {
				sql += ",";
			}
		}
		sql += ") VALUES (";
		sql += resolveValue(attributeName) + "," + resolveValue(elementResidence) + "," + resolveValue(alias);
		sql += ");";
		// This may cause a problem with null
		//executeUpdate(sql);
		return sql;
	}

	/**
	 * @return - A string for all the pending SQL insertion commands
	 */
	public String getPendingSystemData() {
		String acc = "";
		for (Iterator iter = pendingSystemDataAdditions.iterator(); iter.hasNext();) {
			String dataInsertion = (String) iter.next();
			acc += dataInsertion + "\n";
		}
		return acc;
	}

	/**
	 * 
	 * @param systemDataSQL - System data which needs to be added to a system table
	 */
	public void addPendingDataInsertion(String systemDataSQL) {
		pendingSystemDataAdditions.add(systemDataSQL);
	}

	/**
	 * @param attributeSystemInformation
	 */
	public void addPendingDataInsertions(Collection systemDataSQLStatements) {
		for (Iterator iter = systemDataSQLStatements.iterator(); iter.hasNext();) {
			String insertionCommand = (String) iter.next();
			addPendingDataInsertion(insertionCommand);

		}

	}

	/**
	 * @return - SQL code to generate a system table which says which table references which
	 * "table" is the name of a table containing a foreign key to "foreign_table". Do not think
	 * about it in any other terms (e.g.Entity relationships) otherwise you may get it the
	 * wrong way round
	 */
	private String createGeneralizationSystemTable() {
		String sql = "";

		sql += "CREATE TABLE " + getSystemTableName(Nomenclature.GENERALIZATIONS) + " (\n";
		sql += DELIMITER + Nomenclature.GENERALIZATIONS_generalization_table + DELIMITER + "VARCHAR(50),\n";
		sql += DELIMITER + Nomenclature.GENERALIZATIONS_specialization_table + DELIMITER + "VARCHAR(50)";
		//sql += ",\n" + DELIMITER + "PRIMARY KEY (table,foreign_table)\n";
		sql += ");\n";
		return sql;
	}
	/**
	 * @param referringTable - The unqualified name of a data table which contains a foreign key reference
	 * to 'foreign table'
	 * @param foreignTable - The table whose primary key appears as a foreign key in the referring
	 * table
	 * @return - The SQL code to add an entry to the table_containment table 
	 */
	public String insertGeneralizationInformation(String generalization, String specialization) {
		String sql =
			"INSERT INTO "
				+ getSystemTableName(Nomenclature.GENERALIZATIONS)
				+ "("
				+ Nomenclature.GENERALIZATIONS_generalization_table
				+ ","
				+ Nomenclature.GENERALIZATIONS_specialization_table
				+ ") VALUES ("
				+ resolveValue(generalization)
				+ ","
				+ resolveValue(specialization)
				+ ");\n";
		return sql;
	}

	private String resolveValue(String columnValue) {
		if (columnValue == null) {
			columnValue = "NULL";
		}
		else {
			columnValue = "'" + columnValue + "'";
		}
		return columnValue;
	}

	/**
	 * Get the SQL which adds an alias for a given XER attribute
	 * @param alias
	 * @param attribute
	 * @return
	 */
	public String addAttributeAlias(String alias, XERAttribute attribute) {
				
		String sql;		
		if (attribute.originatesFromXMLAttribute()) {

			sql =
				"UPDATE "
					+ getSystemTableName(Nomenclature.ATTRIBUTES)
					+ " SET "
					+ Nomenclature.ATTRIBUTES_alias
					+ "="
					+ alias
					+ " WHERE "
					+ Nomenclature.ATTRIBUTES_name
					+ "="
					+ attribute.getOriginalName()
					+ ";";
		}
		else {

			sql =
				"UPDATE "
					+ getSystemTableName(Nomenclature.ATTRIBUTES)
					+ " SET "
					+ Nomenclature.ATTRIBUTES_alias
					+ "='"
					+ alias
					+ "' WHERE "
					+ Nomenclature.ATTRIBUTES_name
					+ "='"
					+ attribute.getOriginalName()
					+ "';";
		}
		return sql;

	}
	public String addElementAlias(String alias, XEREntity attribute) {

		String sql =
			"UPDATE "
				+ getSystemTableName(Nomenclature.ELEMENTS)
				+ " SET "
				+ Nomenclature.ELEMENTS_alias
				+ "='"
				+ alias
				+ "' WHERE "
				+ Nomenclature.ELEMENTS_name
				+ "='"
				+ attribute.getOriginalName()
				+ "';";
		return sql;
	}

	protected String getElementAlias(String xmlElementName) {

		return xmlElementName;
	}

	protected String getAttributeAlias(String xmlAttributeName) {

		return xmlAttributeName;
	}
}