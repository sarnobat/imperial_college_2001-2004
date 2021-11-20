package xtractor.dataImporter.xmlReader.databaseManager;

/*
 * Created on 17-Mar-2004
 *  
 */

import java.sql.*;
import java.util.*;

import databaseConnection.*;

/**
 * @author ss401 Reads from and writes to the database. No xml parsing should occur here (i.e. if you need to import jdom Element etc. you're doing
 *               it wrong!).
 */
//TODO: Make this a subclass of the databaseConnectiion.databaseManager class
public class DatabaseManager extends AbstractDatabaseManager {

	public DatabaseManager(String databaseSchemaName) {
		super(databaseSchemaName);
	}

	/**
	 * The attribute names and values lists must be in the same order
	 * 
	 * @param elementName - The name of the table which the attributes belong to
	 * @param columnNamesToValues - A List of Column Names (as Strings) and Values (as Strings, Integers etc.)	 
	 */
	public void addData(String elementName, Map columnNamesToValues) {

		Map columnNamesToTypes = null;
		try {
			columnNamesToTypes = getColumnTypesForTable(elementName);
		}
		catch (SQLException e1) {
			// We have to assume the error was thrown because a table doesn't exist
			logger.warn("It is likely that a data table called " + elementName + " doesn't exist. " + e1);
			//TODO: however, we must still make sure that meta data is added.
			return;
		}

		try {
			performInsert(elementName, columnNamesToValues, columnNamesToTypes);
			logger.info("Successfully added data.");
		}
		catch (SQLException e) {
			//TODO:You'll need to catch an exception if you need to perform an update rather than an insert
			logger.warn("Couldn't add data. " + e);
		}

	}

	/**
	 * @param elementName - Omit the prefix 'meta' from the name. This is automatically added
	 * @param columnNamesToValues - The column names and the values to be assigned to them
	 * 								e.g		"id"	->	nextVal('schema.id')
	 * 										"pid"	->	1
	 * 										"ord"	->	1
	 * 							(note that the values may take the form of an Integer)
	 */
	public void addMetaData(String elementName, Map columnNamesToValues) {

		/*
		 * 	We cannot use PreparedStatement here because it will try to treat the string "nextval('schema.id')"
		 *	sequence as an int
		 */

		/* 
		 * 	The map isn't ordered by its tabular correspondence, but its natural one
		 *	However, this doesn't matter because we exlicitly state the columns
		 */

		String tableName = "meta_" + elementName;
		Map columnNamesToTypes = null;
		try {
			columnNamesToTypes = getColumnTypesForTable(tableName);
		}
		catch (SQLException e) {
			logger.error("Couldn't determine types for table" + e);
			//logger.warn("It is likely that the table called " +tableName+ "doesn't exist" + e);
			return;
		}

		try {
			performInsert(tableName, columnNamesToValues, columnNamesToTypes);
		}
		catch (SQLException e1) {
			logger.warn("Couldn't insert data. " + e1);
			//TODO: this isn't necessarily an error. It means you require an update instead.
		}

	}

	/**
	 * Useful for determining what the most recently added column was uniquely identified by
	 * @return - The current value of the sequence number used for assigning globally unique ids
	 * in the meta table of the element schema
	 */
	public int getNextGlobalId() {

		try {

			ResultSet rs = executeQuery("SELECT nextval('" + schemaName + ".id')");
			while (rs.next()) {
				return Integer.parseInt(rs.getString("nextval"));
			}
		}
		catch (SQLException e) {
			logger.error("Couldn't determine id of root." + e);
		}
		return -1;

	}

	/**
	 * Note these are the GENUINE key fields and not our internal ones. They will be the fields that
	 * will be in both the meta table AND the data table
	 * @param element - The name of the XML element whose key attributes you wish to determine
	 * @return - A list of Strings for each column name
	 */
	public List getPrimaryKeyAttributesNames(String elementName) {

		//String tableName = getDataTableName(elementName);
		List keyColumnNames = new LinkedList();

		try {
			ResultSet rs = metaData.getPrimaryKeys("pg_class", schemaName, elementName);
			while (rs.next()) {
				String keyColumnName = rs.getString("column_name");
				keyColumnNames.add(keyColumnName);
			}
			return keyColumnNames;
		}
		catch (SQLException e) {
			logger.error("Couldn't determine key fields." + e);
			return null;
		}

	}

	/**
	 * @param tableName
	 * @return - A list of Integers for each type
	 */
	private Map getColumnTypesForTable(String tableName) throws SQLException {

		//We cannot use this because getAttributes throws an AbstractMethodError, presumably
		//because it isn't implemented in the latest postgresql driver
		/*DatabaseMetaData metaData = conn.getMetaData();
		ResultSet rs = metaData.getAttributes("pg_attribute", schemaName + "." + tableName, "%", "%");
		while (rs.next()) {
			String keyColumnName = rs.getString("column_name");
			System.out.println();
		}
		return null;*/

		//The following isn't ideal because we cannot be sure that a thrown exception was due to a
		// table not exisiting; but we have no choice
		String sql = "SELECT * FROM " + schemaName + "." + tableName;
		PreparedStatement ps = conn.prepareStatement(sql);
		//logger.debug("Performing query: " + sql);
		ResultSet rs = ps.executeQuery();
		ResultSetMetaData metaData = rs.getMetaData();
		Map columnNamesToTypes = new TreeMap();
		for (int i = 1; i <= metaData.getColumnCount(); i++) {
			columnNamesToTypes.put(metaData.getColumnName(i), new Integer(metaData.getColumnType(i)));
		}
		return columnNamesToTypes;

	}

	/**
	 * @param tableName - This MUST contain the exact table name (including prefixes like 'meta' etc.)
	 * @param columnNames
	 * @param columnTypes
	 * @param columnValues
	 */
	//TODO:Eventually this method should have an exception throwable
	private void performInsert(String tableName, Map columnNamesToValues, Map columnNamesToTypes) throws SQLException {

		// sanity check
		if (columnNamesToTypes.size() != columnNamesToValues.size()) {
			logger.error("Inconsistent types and values counts");
			return;
		}

		// this needs to be an ordered list to make sure we do the 2 loops in the same order
		List columnNames = new LinkedList(columnNamesToValues.keySet());

		String sql = "INSERT INTO " + resolveTableName(tableName) + " (";
		String questionMarks = "";

		// Get the column names
		for (Iterator iter = columnNames.iterator(); iter.hasNext();) {

			String columnName = (String) iter.next();
			sql += columnName;
			questionMarks += "?";

			if (iter.hasNext()) {
				sql += ",";
				questionMarks += ",";
			}
		}

		sql += ") VALUES (" + questionMarks + ");";
		PreparedStatement ps = conn.prepareStatement(sql);

		// Fill in the values to be added
		for (int i = 0; i < columnNames.size(); i++) {
			String name = (String) columnNames.get(i);
			Object value = columnNamesToValues.get(name);

			Integer type = (Integer) columnNamesToTypes.get(name);
			setPreparedStatementValue(ps, i + 1, value, type);
		}
		//logger.debug("performing insert:\t" + ps);
		ps.execute();
		//logger.debug("Success.");
	}

	/**
	 * @param ps
	 * @param i
	 * @param value
	 */
	private void setAutoId(PreparedStatement ps, int attributeIndex) {

		int id = getNextGlobalId();
		try {
			ps.setInt(attributeIndex, id);
		}
		catch (SQLException e) {
			logger.error("Error setting attribute value. " + e);
		}

	}

	/**
	 * @param ps - The sql statement where the values are being set
	 * @param columnIndex - The question mark that is about to have a value set
	 * @param value - The value about to be set for the question mark indexed
	 * @param sqlType - The type of the value about to be set (can be resolved in java.sql.Types)
	 * 					(http://java.sun.com/j2se/1.4.2/docs/api/constant-values.html)
	 */
	private void setPreparedStatementValue(PreparedStatement ps, int columnIndex, Object value, Integer sqlType)
		throws SQLException {

		int type = sqlType.intValue();
		switch (type) {
			case Types.VARCHAR :
				{
					ps.setString(columnIndex, (String) value);
					break;
				}
			case Types.NUMERIC :
				{
					// Allow to fall through to 'integer'
				}
			case Types.INTEGER :
				{
					int intValue;
					if (value instanceof String) {
						// number is disguised as a string
						intValue = Integer.parseInt((String) value);
					}
					else {
						// number is held as an Integer
						intValue = ((Integer) value).intValue();
					}
					ps.setInt(columnIndex, intValue);
					break;
				}
			case Types.DECIMAL :
				{
					// Allow to fall through to 'double'
				}
			case Types.DOUBLE :
				{
					double doubleValue;
					if (value instanceof String) {
						// number is disguised as a string
						doubleValue = Double.parseDouble((String) value);
					}
					else {
						// number is held as an Double
						doubleValue = ((Double) value).doubleValue();
					}

					ps.setDouble(columnIndex, doubleValue);
					break;
				}
			default :
				{
					break;
				}
		}
	}

	/**
	 * Adds the schema prefix to a local table name
	 * @param localTableName - The name of the table within the schema, e.g. 'tableName'
	 * @return - The name of the table that is globally unique, i.e. schemaName.tableName
	 */
	private String resolveTableName(String localTableName) {
		return schemaName + "." + localTableName;
	}

	/**
	 * @param elementName - The name of an XML element (e.g. name)
	 * @return - True if its table uses a sequence to determine the primary key value
	 * that should be inserted.
	 */
	public boolean dataTableUsesAutoKey(String elementName) {

		try {
			String[] types = { "SEQUENCE" };
			ResultSet rs = metaData.getTables("pg_class", schemaName, elementName + "%", types);
			if (rs.next()) {
				return true;
			}
			else {
				return false;
			}

		}
		catch (SQLException e) {
			logger.error("Couldn't determine whether " + elementName + " requires an auto key. " + e);
			return false;
		}

	}

	/**
	 * @param elementName - e.g. ps_db
	 * @return - The next primary key value that could be given to a row in its table
	 */
	public int getNextId(String elementName) {
		String sequenceName = getSequenceName(elementName);
		ResultSet rs = executeQuery("SELECT nextval('" + sequenceName + "')");
		try {
			if (rs.next()) {
				return rs.getInt("nextval");
			}
			else {
				logger.error("Couldn't determine next sequence value for primary key of " + elementName);
				return 0;
			}
		}
		catch (SQLException e) {
			logger.error("Couldn't determine next sequence value for primary key of " + elementName + ". " + e);
			return 0;
		}
	}

	/**
	 * @param keyName - The name of a primary key column in a table
	 * @return - True if it relies on a sequence to determine its value (and not an XML attribute value)
	 * (it's an 'implicit key'0
	 */
	public boolean isAutoKey(String keyName) {
		String[] types = { "SEQUENCE" };
		try {
			ResultSet rs = metaData.getTables("pg_class", schemaName, keyName + "%", types);
			if (rs.next()) {
				return true;
			}
		}
		catch (SQLException e) {
			logger.error("Couldn't determine if key relies on a sequence. " + e);
		}
		return false;
	}

	/**
	 * The attribute names must be from the data table.
	 * @param elementName - The name of an XML element (e.g. part)
	 * @param attributeNames - The names of the attributes whose values you want returned
	 * @return - A map from attribute name to attribute value
	 */
	public Map getAttributeValues(String elementName, List attributeNames) {

		Map attributeNamesToValues = new HashMap();
		String tableName = getDataTableName(elementName);

		ResultSet rs = executeQuery("SELECT * FROM " + tableName);
		
		try {
			rs.next();
			ResultSetMetaData rsMeta = rs.getMetaData();
			for (Iterator iter = attributeNames.iterator(); iter.hasNext();) {
				String attributeName = (String) iter.next();
				Object attributeValue = rs.getObject(attributeName);
				attributeNamesToValues.put(attributeName, attributeValue);
			}
		}
		catch (SQLException e) {
			logger.error("Couldn't get attribure values for element " + elementName + ". " + e);
		}
		return attributeNamesToValues;
	}

}
