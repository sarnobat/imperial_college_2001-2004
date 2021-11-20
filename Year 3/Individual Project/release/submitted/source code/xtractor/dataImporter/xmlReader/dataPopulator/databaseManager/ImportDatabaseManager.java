package xtractor.dataImporter.xmlReader.dataPopulator.databaseManager;

/*
 * Created on 17-Mar-2004
 *  
 */

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Element;
import org.jdom.Parent;

import databaseConnection.Nomenclature;
import databaseConnection.databaseManager.PostDatabaseManager;
import exception.AddDataException;
import exception.AddMetaDataException;

/**
 * @author ss401 Reads from and writes to the database. No xml parsing should occur here (i.e. if you need to import jdom Element etc. you're doing
 *               it wrong!).
 */
//TODO: Make this a subclass of the databaseConnectiion.databaseManager class
public class ImportDatabaseManager extends PostDatabaseManager {

	public ImportDatabaseManager(String databaseSchemaName) {
		super(databaseSchemaName);
	}

	/**
	 * Populates the data table
	 * The attribute names and values lists must be in the same order 
	 * @param elementName - The name of the table which the attributes belong to
	 * @param columnNamesToValues - A List of Column Names (as Strings) and Values (as Strings, Integers etc.)	 
	 */
	public void addData(String originalElementName, Map attributeNamesToValues) throws AddDataException {
		String dataTableName = getDataTableName(originalElementName);

		Map columnNamesToValues = getAttributeAliases(attributeNamesToValues);
		Map columnNamesToTypes = null;
		//try {

		columnNamesToTypes = getColumnTypesForTable(dataTableName, columnNamesToValues.keySet());
		//		}
		//		catch (SQLException e1) {
		//			// We have to assume the error was thrown because a table doesn't exist
		//			logger.warn("It is likely that a data table called " + dataTableName+ " doesn't exist. " + e1);
		//			//however, we must still make sure that meta data is added.
		//			return;
		//		}

		try {

			performInsert(dataTableName, columnNamesToValues, columnNamesToTypes);
			logger.info("Successfully added data. <" + originalElementName + ">");
		}
		catch (SQLException e) {
			logger.debug("Unable to insert data. Attempting update...");
			try {
				performUpdate(dataTableName, columnNamesToValues, columnNamesToTypes);
			}
			catch (SQLException e2) {
				logger.warn("Couldn't add data via update or insert. " + e);
				throw new AddDataException(e2);
			}

		}

	}

	/**
	 * @param dataTableName
	 * @param columnNamesToValues
	 * @param columnNamesToTypes
	 */
	private void performUpdate(String dataTableName, Map columnNamesToValues, Map columnNamesToTypes) throws SQLException {
		// sanity check
		if (columnNamesToTypes.size() != columnNamesToValues.size()) {
			logger.error("Inconsistent types and values counts");
			return;
		}

		// Determine what columns are key columns
		List keyColumnNames = new LinkedList();
		String unqualifiedTableName = getUnqualifiedTableName(dataTableName);
		DatabaseMetaData metaData = conn.getMetaData();
		ResultSet rs = metaData.getPrimaryKeys(null, schemaName, unqualifiedTableName);
		while (rs.next()) {
			String keyColumnName = rs.getString("COLUMN_NAME");
			keyColumnNames.add(keyColumnName);
		}

		// Determine what columns are NOT key columns
		List nonKeyColumnNames = new LinkedList();
		nonKeyColumnNames.addAll(columnNamesToValues.keySet());
		nonKeyColumnNames.removeAll(keyColumnNames);

		// Attempt the update
		String sql = "UPDATE " + dataTableName + " SET ";
		
		if(nonKeyColumnNames.size() < 1){
			return;
		}
		
		for (Iterator iter = nonKeyColumnNames.iterator(); iter.hasNext();) {
			String nonKeyColumnName = (String) iter.next();

			if (columnNamesToValues.keySet().contains(nonKeyColumnName)) {
				sql += nonKeyColumnName + "=?";
			}

			if (iter.hasNext()) {
				sql += ",";
			}
		}
		sql += " WHERE ";
		for (Iterator iter = keyColumnNames.iterator(); iter.hasNext();) {
			String keyColumnName = (String) iter.next();
			sql += keyColumnName + "=?";
			if (iter.hasNext()) {
				sql += " AND ";
			}
		}

		PreparedStatement ps = conn.prepareStatement(sql);
		// This must be in the same order that we iterated over them above
		List allColumnNames = new LinkedList(nonKeyColumnNames);
		allColumnNames.addAll(keyColumnNames);
		int columnIndex = 1;
		for (Iterator iter = allColumnNames.iterator(); iter.hasNext();) {
			String columnName = (String) iter.next();

			if (columnNamesToValues.keySet().contains(columnName)) {
				Object columnValue = columnNamesToValues.get(columnName);
				Integer type = (Integer) columnNamesToTypes.get(columnName);
				setPreparedStatementValue(ps, columnIndex, columnValue, type);
			}
			else {
				logger.warn("No value for " + columnName + " specified.");
			}

			columnIndex++;
		}
		//logger.debug("About to execute update: " + ps.toString());
		ps.executeUpdate();

	}

	/**
	 * @param elementName - Omit the prefix 'meta' from the name. This is automatically added
	 * @param columnNamesToValues - The column names and the values to be assigned to them
	 * 								e.g		"id"	->	nextVal('schema.id')
	 * 										"pid"	->	1
	 * 										"ord"	->	1
	 * 							(note that the values may take the form of an Integer)
	 */
	public void addMetaData(String elementName, Map attributeNamesToValues) throws AddMetaDataException {

		/*
		 * 	We cannot use PreparedStatement here because it will try to treat the string "nextval('schema.id')"
		 *	sequence as an int
		 */

		/* 
		 * 	The map isn't ordered by its tabular correspondence, but its natural one
		 *	However, this doesn't matter because we exlicitly state the columns
		 */
		Map columnNamesToValues = getAttributeAliases(attributeNamesToValues);
		String tableName = getMetaTableName(elementName);
		Map columnNamesToTypes = null;
		try {
			columnNamesToTypes = getColumnTypesForTable(tableName);
		}
		catch (SQLException e) {
			logger.error("Couldn't determine types for table" + e);
			//logger.warn("It is likely that the table called " +tableName+ "doesn't exist" + e);
			return;
		}

		//try {
		// You should never be updating a meta table, only inserting
		try {
			performInsert(tableName, columnNamesToValues, columnNamesToTypes);
		}
		catch (SQLException e1) {
			throw new AddMetaDataException(e1);
		}
		logger.info("Successfully added meta data. <" + elementName + ">");
		//}
		//catch (SQLException e1) {

		//logger.warn("Couldn't insert data. " + e1);
		//}

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
	 * @param elementName - e.g. ps_db
	 * @return - The next primary key value that could be given to a row in its table
	 */
	public int getNextId(String originaElementName) {
		String elementName = getElementAlias(originaElementName);
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
	 * @param tableName - This MUST contain the exact table name (including prefixes like 'meta' etc.)
	 * @param columnNames
	 * @param columnTypes
	 * @param columnValues
	 */
	//TODO:Eventually this method should have an exception throwable
	private void performInsert(String tableName, Map columnNamesToValues, Map columnNamesToTypes) throws SQLException {

		// sanity check
		if (columnNamesToTypes.size() != columnNamesToValues.size()) {
			logger.warn("Inconsistent types and values counts");
			// It may be possible to continue
			//return;
		}

		// this needs to be an ordered list to make sure we do the 2 loops in the same order
		List columnNames = new LinkedList(columnNamesToValues.keySet());

		String sql = "INSERT INTO " + tableName + " (";
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
	 * Note that this method is only responsible for inserting an entry in the generalization table
	 * (e.g. ps_db_choice). The physical element tables (meta and data) are handled elsewhere
	 * @param specialization - The actual element whose generalization table you are trying to populate
	 * @param specializationId - The id of the specialization element (e.g. part) which does NOT reference
	 * the parent in the data table (but does in the meta table)
	 * @param parentId - The id of the parent element (e.g. ps_db) which contains the generalization
	 */
	public void addGeneralizationEntry(Element specialization, int specializationId, int parentId) {

		String childElementName = specialization.getName();
		Parent parentElement = specialization.getParent();
		if (!(parentElement instanceof Element)) {
			logger.error("Invalid parent of specialization.");
			return;
		}
		String parentElementName = ((Element) parentElement).getName();

		Map parentKeys = getPrimaryKeyNamesAndValues(parentElementName, parentId);
		Map childKeys = getPrimaryKeyNamesAndValues(childElementName, specializationId);

		Map columnNamesAndValues = new HashMap(parentKeys);
		columnNamesAndValues.putAll(childKeys);

		String generalizationTableName = getGeneralizationTableName(childElementName);
		try {
			//Map columnNamesToTypes = getColumnTypesForTable(generalizationTableName);
			Map columnNamesToTypes = getColumnTypesForTable(generalizationTableName, columnNamesAndValues.keySet());
			performInsert(generalizationTableName, columnNamesAndValues, columnNamesToTypes);
		}
		catch (SQLException e) {
			logger.error("Couldn't add generalization table entry. " + e);
		}
	}

	/**
	 * @param generalizationTableName
	 * @param Set - A set of column names whose types you want
	 * @return
	 */
	private Map getColumnTypesForTable(String tableName, Set columnNames) {
		Map subsetColumnNamesToTypes = new HashMap();

		try {
			Map allColumnsToTypes = getColumnTypesForTable(tableName);
			for (Iterator iter = columnNames.iterator(); iter.hasNext();) {
				String columnName = (String) iter.next();
				if (columnNames.contains(columnName)) {
					subsetColumnNamesToTypes.put(columnName, allColumnsToTypes.get(columnName));
				}
			}
		}
		catch (SQLException e) {
			logger.error("Couldn't determine column types. " + e);
		}
		return subsetColumnNamesToTypes;
	}

	/**
	 * @param childElementName
	 * @return
	 */
	private String getGeneralizationTableName(String specializationElementName) {
		return schemaName + "." + getGeneralization(specializationElementName);
	}

	/**
	 * @param childElementName
	 * @return
	 */
	private String getGeneralization(String specializationElementName) {
		String sql =
			"SELECT * FROM "
				+ getSystemTableName(Nomenclature.GENERALIZATIONS)
				+ " WHERE "
				+ Nomenclature.GENERALIZATIONS_specialization_table
				+ "='"
				+ getUnqualifiedTableName(getDataTableName(specializationElementName) + "'");
		Object generalizationTableName = getOnlyResult(sql, Nomenclature.GENERALIZATIONS_generalization_table);
		if (generalizationTableName instanceof String) {
			return (String) generalizationTableName;
		}
		else {
			logger.error("Unexpected generalization table name.");
			return null;
		}
	}
	/**
	 * Note that the element here isn't guaranteed to contain the column. It may be in one of the
	 * child tables (e.g. a generalization) 
	 * @param parentElementName - The name of the parent element (complex) containing the simple element
	 * @param attribtueName - (in XML nomenclature) 
	 * @param columnValue
	 * @param parentId - This will be needed to determine what row to insert the data into
	 */
	public void addSimpleElementData(String originalElementName, String attributeName, Object columnValue, int parentId)
		throws SQLException {

		String columnName = getAttributeAlias(attributeName);

		// Get the arguments
		Map keyColumnsNamesToValues = getPrimaryKeyNamesAndValues(originalElementName, parentId);
		Map columnNamesToValues = new HashMap();
		columnNamesToValues.putAll(keyColumnsNamesToValues);
		columnNamesToValues.put(columnName, columnValue);

		String tableName = getDataTableName(originalElementName);

		// see if this table contains this column
		DatabaseMetaData metaData = conn.getMetaData();
		ResultSet rs = metaData.getColumns(null, schemaName, tableName, columnName);
		if (rs.next()) {

			addData(originalElementName, columnNamesToValues);
		}
		else {
			// look through the whole database
			//TODO: this is not a satisfactory way to deal with the problem, but will have to do for now!

			ResultSet rs1 = metaData.getColumns(null, schemaName, "%", columnName);
			if (rs1.next()) {
				String alternateTableName = rs1.getString("TABLE_NAME");

				Map columnTypes = getColumnTypesForTable(alternateTableName);

				String sql = "UPDATE " + schemaName + "." + alternateTableName + " SET " + columnName + "=? WHERE ";
				List keyColumnNames = new LinkedList(keyColumnsNamesToValues.keySet());
				for (Iterator iter = keyColumnNames.iterator(); iter.hasNext();) {
					String keyColumnName = (String) iter.next();
					sql += keyColumnName + "=?";
					if (iter.hasNext()) {
						sql += " AND ";
					}
				}
				PreparedStatement ps = conn.prepareStatement(sql);
				Integer valueColumnType = (Integer) columnTypes.get(columnName);
				setPreparedStatementValue(ps, 1, columnValue, valueColumnType);
				int i = 2;
				for (Iterator iter = keyColumnNames.iterator(); iter.hasNext();) {
					String keyColumnName = (String) iter.next();
					Object keyColumnValue = keyColumnsNamesToValues.get(keyColumnName);
					Integer keyColumnType = (Integer) columnTypes.get(keyColumnName);
					setPreparedStatementValue(ps, i, keyColumnValue, keyColumnType);
				}
				logger.debug("About to execute update of simple element data. " + ps);
				ps.executeUpdate(sql);

			}
			else {
				String error = "Couldn't determine where to place simple element data.";
				logger.error(error);
				throw new AddDataException(error);
			}
		}
	}
}