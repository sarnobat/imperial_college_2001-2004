/*
 * Created on 24-May-2004
 *
 */
package xtractor.schemaConverter.rdb.parsers;

import databaseConnection.Nomenclature;
import xtractor.schemaConverter.rdb.RDBBuilder;
import xtractor.schemaConverter.xer.xerConstructs.XERAttribute;
import xtractor.schemaConverter.xer.xerConstructs.XEREntity;

/**
 * @author ss401
 *
 */
public class SystemTableHandler {

	final String DELIMITER;
	final String schemaName;
	final String SYSTEM_PREFIX = Nomenclature.SYSTEM_PREFIX;

	/*
	 * System tables
	 */

	//
	final String GENERALIZATIONS = Nomenclature.GENERALIZATIONS;
	final String GENERALIZATIONS_generalization_table = Nomenclature.GENERALIZATIONS_generalization_table;
	final String GENERALIZATIONS_specialization_table = Nomenclature.GENERALIZATIONS_specialization_table;

	//
	final String ELEMENT_LOCATIONS = Nomenclature.ELEMENT_LOCATIONS;
	final String ELEMENT_LOCATIONS_element_name = Nomenclature.ELEMENT_LOCATIONS_element_name;
	final String ELEMENT_LOCATIONS_table_name = Nomenclature.ELEMENT_LOCATIONS_table_name;
	final String ELEMENT_LOCATIONS_column_name = Nomenclature.ELEMENT_LOCATIONS_column_name;

	// 
	final String MIXED_ELEMENTS = Nomenclature.MIXED_ELEMENTS;
	final String MIXED_ELEMENTS_entity = Nomenclature.MIXED_ELEMENTS_entity;

	//
	final String CHARACTER_ELEMENTS = Nomenclature.SIMPLE_ELEMENTS;
	final String CHARACTER_ELEMENTS_element = Nomenclature.SIMPLE_ELEMENTS_element;
	
	//
	final String IDS_TO_TABLES = Nomenclature.IDS_TO_TABLES;
	final String IDS_TO_TABLES_id = Nomenclature.IDS_TO_TABLES_id ;
	final String IDS_TO_TABLES_table = Nomenclature.IDS_TO_TABLES_table;

	public SystemTableHandler(RDBBuilder rdbBuilder) {
		this.DELIMITER = rdbBuilder.getDelimiter();
		this.schemaName = rdbBuilder.getSchemaName();
		

	}
	/**
	 * @return - A string of SQL code to create a table with the following structure:
	 * ss.SYSTEM_element_locations(element_name,table_name,column_name)
	 * 		columnName - The name of the column where a simple element's data should be stored 
	 * 					(null if the element is complex)
	 * 		table_name - The name of the table where this column is located OR
	 * 					 the name of the table holding a complex element's attributes
	 * This will also be useful if the user renames the tables or columns
	 */
	private String createElementLocation() {
		String sql = "CREATE TABLE " + getSystemTableName(ELEMENT_LOCATIONS) + " (\n";
		sql += DELIMITER + ELEMENT_LOCATIONS_element_name + DELIMITER + "VARCHAR(50) NOT NULL UNIQUE,\n";
		sql += DELIMITER + ELEMENT_LOCATIONS_table_name + DELIMITER + "VARCHAR(50) NOT NULL,\n";
		sql += DELIMITER + ELEMENT_LOCATIONS_column_name + DELIMITER + "VARCHAR(50)\n";

		sql += "\n);";
		return sql;
	}

	/**
	 * Gets the SQL code to add a system table entry saying where an XML element's data should be
	 * inserted
	 * @param elementName - An element of simple type
	 * @param tableName - The table where the attribute that the XML element's value should be inserted is found
	 * @param attributeName - The name of the attribute where the XML element's value should be inserted
	 * @return
	 */
	public String addElementLocation(String elementName, String tableName, String attributeName) {
		String sql =
			"INSERT INTO "
				+ getSystemTableName("element_locations")
				+ "("
				+ ELEMENT_LOCATIONS_element_name
				+ ","
				+ ELEMENT_LOCATIONS_table_name
				+ ","
				+ ELEMENT_LOCATIONS_column_name
				+ ") VALUES ('"
				+ elementName
				+ "','"
				+ tableName
				+ "','"
				+ attributeName
				+ "');\n";
		return sql;
	}

	/**
	 * 
	 * @param elementName - An element of complex type
	 * @param tableName
	 * @return
	 */
	public String addElementLocation(String elementName, String tableName) {
		String sql =
			"INSERT INTO "
				+ getSystemTableName("element_locations")
				+ "("
				+ ELEMENT_LOCATIONS_element_name
				+ ","
				+ ELEMENT_LOCATIONS_table_name
				+ ") VALUES ('"
				+ elementName
				+ "','"
				+ tableName
				+ "');\n";
		return sql;
	}

	/**
	 * 
	 * @param tableName - The name of the table excluding any schema name or prefix
	 * @return - The fully qualified name of the table
	 */
	private String getSystemTableName(String tableName) {
		return schemaName + "." + SYSTEM_PREFIX + tableName;
	}

	/**
	 * @return - SQL code to generate a system table which says which table references which
	 * "table" is the name of a table containing a foreign key to "foreign_table". Do not think
	 * about it in any other terms (e.g.Entity relationships) otherwise you may get it the
	 * wrong way round
	 */
	public String createGeneralization() {
		String sql = "";

		sql += "CREATE TABLE " + getSystemTableName(GENERALIZATIONS) + " (\n";
		sql += DELIMITER + GENERALIZATIONS_generalization_table + DELIMITER + "VARCHAR(50),\n";
		sql += DELIMITER + GENERALIZATIONS_specialization_table + DELIMITER + "VARCHAR(50)";
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
	public String addGeneralization(String generalization, String specialization) {
		String sql =
			"INSERT INTO "
				+ getSystemTableName(GENERALIZATIONS)
				+ "("
				+ GENERALIZATIONS_generalization_table
				+ ","
				+ GENERALIZATIONS_specialization_table
				+ ") VALUES ('"
				+ generalization
				+ "','"
				+ specialization
				+ "');\n";
		return sql;
	}
	/**
	 * @param xerEntity
	 * @return
	 */
	public String addMixedEntity(XEREntity xerEntity) {
		String sql =
			"INSERT INTO "
				+ getSystemTableName(MIXED_ELEMENTS)
				+ "("
				+ MIXED_ELEMENTS_entity
				+ ") VALUES ('"
				+ xerEntity.getName()
				+ "');";
		return sql;
	}

	public String createMixedEntitySummary() {
		String sql = "";
		sql += "CREATE TABLE " + getSystemTableName(MIXED_ELEMENTS) + " (";
		sql += DELIMITER + MIXED_ELEMENTS_entity + DELIMITER + "VARCHAR(50)";
		sql += "\n);";
		return sql;
	}
	/**
	 * @return
	 */
	public String createCharacterDataElements() {
		String sql = "";
		sql += "CREATE TABLE " + getSystemTableName(CHARACTER_ELEMENTS) + " (";
		sql += DELIMITER + CHARACTER_ELEMENTS_element + DELIMITER + "VARCHAR(50)";
		sql += "\n);";
		return sql;
	}
	/**
	 * @param attribute
	 * @return
	 */
	public String addCharacterDataElement(XERAttribute attribute) {
		String sql =
			"INSERT INTO "
				+ getSystemTableName(CHARACTER_ELEMENTS)
				+ "("
				+ CHARACTER_ELEMENTS_element
				+ ") VALUES ('"
				+ attribute.getName()
				+ "');";
		return sql;
	}
}
