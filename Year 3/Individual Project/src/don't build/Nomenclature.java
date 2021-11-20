/*
 * Created on 25-May-2004
 *
 */
package databaseConnection;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * @author ss401
 *
 */
public class Nomenclature {

	static Logger logger = Logger.getLogger("Nomenclaure");

	public static final String DELIMITER = "\t";
	public static final String PRIMARY_KEY_SUFFIX = "_pkey";
	public static final String SEQUENCE_SUFFIX = "_seq";
	public static final String META_PREFIX = "META_";
	public static final String COMPLETE_VIEW_PREFIX = "COMPLETE_";
	public static final String SYSTEM_PREFIX = "SYSTEM_";

	// Table
	public static final String GENERALIZATIONS = "generalizations";
	public static final String GENERALIZATIONS_generalization_table = "general_table";
	public static final String GENERALIZATIONS_specialization_table = "specialization_table";

	// Table
	private static final String ELEMENT_LOCATIONS = "element_locations";
	public static final String ELEMENT_LOCATIONS_element_name = "element_name";
	public static final String ELEMENT_LOCATIONS_table_name = "table_name";
	public static final String ELEMENT_LOCATIONS_column_name = "column_name";

	// Table
	private static final String MIXED_ELEMENTS = "mixed_entities";
	public static final String MIXED_ELEMENTS_entity = "entity";
	
	// Table
	private static final String SIMPLE_ELEMENTS = "simple_elements";
	public static final String SIMPLE_ELEMENTS_element = "element";
	
	// Table
	public static final String IDS_TO_TABLES = "ids_to_tables";
	public static final String IDS_TO_TABLES_id = "id";
	public static final String IDS_TO_TABLES_table = "table";

	/**
	 * 
	 * @param elementName - The name of an XML element without any of the extras (e.g. ps_db)
	 * @return -The name of the primary key in the database (e.g. ps_db_pkey)
	 */
	static String getPrimaryKeyName(String elementName) {
		return elementName + PRIMARY_KEY_SUFFIX;
	}

	/**
	 * 
	 * @param primaryKeyName - The name of an auto primary key attribute (e.g. ps_db_pkey)
	 * @return - The name of the sequence stored in the database (e.g. ps_db_pkey_seq)
	 */
	static String getPrimaryKeySequence(String primaryKeyName) {
		return primaryKeyName + SEQUENCE_SUFFIX;
	}

	/**
	 * @param metaTableName - with the schema prefix
	 * @return
	 */
	public static String getCorrespondingElementName(String metaTableName) {
		String[] localMetaTableNameParts = metaTableName.split("\\.");
		String localMetaTableName = localMetaTableNameParts[1];
		if (localMetaTableName.startsWith(META_PREFIX.toLowerCase())) {
			return localMetaTableName.substring(META_PREFIX.length(), localMetaTableName.length());
		}
		else {
			logger.error("Not a meta table: " + metaTableName);
			return null;
		}
	}

	/**
	 * @param elementName - The name of an XML element
	 * @return - The UNQUALIFIED name of the complete element schema view for this element
	 */
	public static String getCompleteViewName(String elementName) {
		return COMPLETE_VIEW_PREFIX + elementName;
	}

	/**
	 * @param table
	 * @return - True if the table is a meta table
	 */
	public static boolean isMetaTable(String tableName) {
		return tableName.startsWith(META_PREFIX.toLowerCase());
	}

	/**
	 * @param table - e.g. meta_part
	 * @return - e.g. part
	 */
	public static String removeMetaDecorator(String tableName) {
		if (isMetaTable(tableName)) {
			return tableName.substring(META_PREFIX.length(), tableName.length());
		}
		else {
			logger.warn("Nothing to remove from table name");
			return tableName;
		}

	}

	/**
	 * @return - True if the table is a system table
	 */
	public static boolean isSystemTable(String tableName) {

		return tableName.startsWith(SYSTEM_PREFIX);
	}

	/**
	 * xsd schema filename --> sql schema name
	 * @param xsdFile - An xml Schema file
	 * @return - The name of the schema that this corresponds to in the database
	 */
	public static String getSchemaName(File xsdFile) {
		String fileName = xsdFile.getName();
		String[] fileFragments = fileName.split(".x");
		return fileFragments[0];
	}
}
