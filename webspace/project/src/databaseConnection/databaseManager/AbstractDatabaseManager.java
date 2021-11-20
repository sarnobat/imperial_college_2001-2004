/*
 * Created on 14-May-2004
 */
package databaseConnection.databaseManager;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import databaseConnection.DatabaseConnection;
import databaseConnection.Nomenclature;

/**
 * @author ss401
 */
public abstract class AbstractDatabaseManager {
	// The following variables are the core names of the schema tables in the database
	// (excluding prefixes, qualifiers etc.)  
	/*protected String ROOT_ELEMENT;
	protected String CONTAINS;
	protected String KEY_FIELDS;
	protected String TABLES;*/

	protected Logger logger = Logger.getLogger(this.getClass());
	protected Connection conn;
	protected String schemaName;
	protected DatabaseMetaData metaData;

	public AbstractDatabaseManager(String databaseSchemaName) {
		this.schemaName = databaseSchemaName;
		this.conn = DatabaseConnection.getConnection();
		/*this.ROOT_ELEMENT = getSystemTableName("root_element");
		this.CONTAINS = getSystemTableName("contains");
		this.KEY_FIELDS = getSystemTableName("key_fields");
		this.TABLES = getSystemTableName("tables");*/
		try {
			this.metaData = conn.getMetaData();
		}
		catch (SQLException e) {
			logger.error("Couldn't obtain database meta data." + e);
		}
	}
	/**
	 * @param dataTable - The name of the table whose key columns we wish to determine
	 * @return - A Collection (of Strings) where each member is the name of a key column.
	 */
	protected Collection getPrimaryKeyColumnNames(String originalElementName) {
		Collection keyCols = new LinkedList();
		String dataTable = getDataTableName(originalElementName);
		String sql =
			"SELECT key_attribute FROM " + getSystemTableName("key_fields") + " WHERE table_name = '" + getElementAlias(originalElementName) + "'";

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				keyCols.add(rs.getString("key_attribute"));
			}
		}
		catch (Exception e) {
			logger.debug("Trying to get key column names:\n" + sql);
			logger.error("Couldn't determine key columns for " + dataTable + ". " + e);
		}
		return keyCols;
	}

	/**
	 * 
	 * @param xmlAttributeName
	 * @return - The database column name for the equivalent XML attribute
	 */
	protected String getColumnNameForXMLAttribute(String xmlAttributeName) {
		return getAttributeAlias(xmlAttributeName);
	}

	protected String getColumnNameForXMLElement(String xmlElementName) {
		return getElementAlias(xmlElementName);
	}

	/**
	 * Convenience method
	 * @param elementName - The name of an xml element
	 * @return - The fully qualified data table name (includes schema name)
	 */
	protected String getDataTableName(String originalElementName) {
		return schemaName + "." +getElementAlias(originalElementName);
	}

	/**
	 * Convenience method
	 * @param elementName - The name of an xml element
	 * @return - The fully qualified meta table name (includes schema name)
	 */
	protected String getMetaTableName(String originalElementName) {
		return schemaName + "." + Nomenclature.META_PREFIX + getElementAlias(originalElementName);
	}

	/**
	 * Convenience method
	 * Note that this table is only a view, not a real table
	 * @param elementName - The name of an xml element
	 * @return - The fully qualified complete table name (includes schema name)
	 */
	protected String getCompleteTableName(String originalElementName) {
		return schemaName + "." + Nomenclature.COMPLETE_VIEW_PREFIX + getElementAlias(originalElementName);
	}

	/**
	 * Convenience method
	 * @param description - The unqualified, unprefixed name of the table storing 
	 * meta information at the schema level (as opposed to table level)
	 * 			e.g. contains, key_fields, root_element, tables, idsToTables
	 * @return - The fully qualified schema table 
	 */
	// This shouldn't need to be accessible by subclasses - use the final fields
	protected String getSystemTableName(String description) {
		return schemaName + "." + Nomenclature.SYSTEM_PREFIX + description;

	}

	/**
	 * 
	 * @param sqlSelectStatement - An SQL select statement
	 * @return - A resultset for the query
	 */
	protected ResultSet executeQuery(String sqlSelectStatement) {

		try {
			PreparedStatement ps = conn.prepareStatement(sqlSelectStatement);
			return ps.executeQuery();
		}
		catch (SQLException e) {
			logger.error("Couldn't perform query." + e);
			logger.debug("Statement was:\n\t" + sqlSelectStatement);
			return null;
		}
	}

	/**
	 * 
	 * @param sqlSelectStatement - An SQL select statement with only 1 row of results
	 * @param columnName - The name of the column whose value we wish to extract
	 * @return - The value of the 'columnName' field in the singleton result
	 */
	protected Object getOnlyResult(String sqlSelectStatement, String columnName) {
		ResultSet rs = executeQuery(sqlSelectStatement);
		try {
			while (rs.next()) {
				return rs.getObject(columnName);
			}
			logger.error("No query results.");
		}
		catch (SQLException e) {
			logger.error("Couldn't perform query." + e);
		}
		logger.debug("Statement was:\n\t" + sqlSelectStatement);
		return null;
	}
	/**
	 * For INSERT and UPDATE statements
	 */
	protected void executeUpdate(String sqlUpdateStatement) {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sqlUpdateStatement);
			ps.executeUpdate();
		}
		catch (SQLException e) {
			logger.error("Couldn't perform update." + e);
			logger.debug("Statement was:\n\t" + sqlUpdateStatement);
		}
	}

	/**
	 * @param elementName - The name of an XML element (e.g. ps_db)
	 * @return - The fully qualified name of the sequence for the primary key of this element
	 * (e.g. ps_db_pkey_seq)
	 */
	protected String getSequenceName(String originalElementName) {
		return schemaName + "." + Nomenclature.getPrimaryKeySequence(Nomenclature.getAutoKeyName(getElementAlias(originalElementName)));
	}

	/**
	 * 
	 * @param elementName - e.g. ps_db
	 * @return - e.g. ps_db_pkey
	 */
	public String getAutoKeyName(String originalElementName) {
		return Nomenclature.getAutoKeyName(getElementAlias(originalElementName));
	}

	/**
	 * 
	 * @param qualifiedTableName - e.g. ss.META_ps_db
	 * @return - e.g. META_ps_db
	 */
	protected String getUnqualifiedTableName(String qualifiedTableName) {
		return qualifiedTableName.split("\\.")[1];
	}

	/**
	 * @return - Converts an XML element's original name to its alias
	 */
	protected abstract String getElementAlias(String xmlElementName);

	/**
	 * @return - Converts an XML attribute's original name to its alias
	 */
	protected abstract String getAttributeAlias(String xmlAttributeName);

}
