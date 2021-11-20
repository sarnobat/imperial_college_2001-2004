/*
 * Created on 25-May-2004
 *
 */
package xtractor.schemaConverter.rdb;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import databaseConnection.Nomenclature;
import databaseConnection.databaseManager.AbstractDatabaseManager;

/**
 * @author ss401
 *
 */
public class DatabaseManager extends AbstractDatabaseManager {

	/**
	 * @param databaseSchemaName
	 */
	public DatabaseManager(String databaseSchemaName) {
		super(databaseSchemaName);
	}

	/**
	 * 
	 * @return - the SQL to create the views
	 * @throws SQLException - if it was unable to determine how to create the views
	 */
	public String createCompleteViews() throws SQLException {
		/*try {}
		catch (SQLException e) {
			logger.error("Couldn't determine SQL code to create views. " + e);
			throw e;
		}*/

		String acc = "";

		//Connection conn = DatabaseConnection.getConnection();
		DatabaseMetaData metaData = conn.getMetaData();
		//String viewColumnNames = "";
		String[] types = { "TABLE" };
		ResultSet rs = metaData.getTables("pg_class", schemaName, "meta%", types);
		while (rs.next()) {
			String qualifiedMetaTableName = schemaName + "." + rs.getString("table_name");
			String elementName = Nomenclature.getCorrespondingElementName(qualifiedMetaTableName);
			String qualifiedDataTableName = schemaName + "." + elementName;

			// Determine all distinct columns that occur in either the data table or meta table
			Map attributeNameToQualifiedAttribueName = new HashMap();
			// Determine what the key unqualified column names in the data table are
			Collection keyColumnNames = new LinkedList();

			/*
			 * Get meta table attributes
			 */
			String sql1 = null;
			ResultSetMetaData rs1Meta;
			try {
				sql1 = "SELECT * FROM " + qualifiedMetaTableName;
				PreparedStatement ps1 = conn.prepareStatement(sql1);
				ResultSet rs1 = ps1.executeQuery();
				rs1Meta = rs1.getMetaData();
				int columnCount1 = rs1Meta.getColumnCount();
				for (int i = 1; i <= columnCount1; i++) {
					String columnName = rs1Meta.getColumnName(i);
					String qualifiedColumnName = qualifiedMetaTableName + "." + columnName;
					attributeNameToQualifiedAttribueName.put(columnName, qualifiedColumnName);
				}

			}
			catch (SQLException e) {
				logger.error("Couldn't determine SQL code to create views. " + e);
				logger.debug("Statement was: " + sql1);
				//logger.debug("Meta data statement was: "+ rs1Meta);			
				throw e;
			}

			/*
			 * Get data table attributes
			 */
			String sql2 = null;
			ResultSetMetaData rs2Meta;
			try {
				sql2 = "SELECT * FROM " + qualifiedDataTableName;
				PreparedStatement ps2 = conn.prepareStatement(sql2);
				ResultSet rs2 = ps2.executeQuery();
				rs2Meta = rs2.getMetaData();
				int columnCount2 = rs2Meta.getColumnCount();
				for (int i = 1; i <= columnCount2; i++) {
					String columnName = rs2Meta.getColumnName(i);

					if (attributeNameToQualifiedAttribueName.keySet().contains(columnName)) {
						keyColumnNames.add(columnName);
					}
					else {
						String qualifiedColumnName = qualifiedDataTableName + "." + columnName;
						attributeNameToQualifiedAttribueName.put(columnName, qualifiedColumnName);
					}
				}
			}
			catch (SQLException e) {
				logger.error("Couldn't determine SQL code to create views. " + e);
				logger.debug("Statement was: " + sql2);
				throw e;
			}

			/*
			 * Create the view
			 */
			String sql = "CREATE VIEW " + schemaName + "." + Nomenclature.getCompleteViewName(elementName) + " AS (\n\tSELECT ";
			Collection distinctColumnNames = attributeNameToQualifiedAttribueName.keySet();
			for (Iterator iter = distinctColumnNames.iterator(); iter.hasNext();) {
				String columnName = (String) iter.next();
				String qualifiedColumnName = (String) attributeNameToQualifiedAttribueName.get(columnName);
				sql += qualifiedColumnName;
				if (iter.hasNext()) {
					sql += ",";
				}
			}
			sql += "\n\tFROM " + qualifiedMetaTableName;
			sql += "\n\tINNER JOIN " + qualifiedDataTableName;
			sql += "\n\tON ";
			for (Iterator iter = keyColumnNames.iterator(); iter.hasNext();) {
				String keyColumnName = (String) iter.next();
				sql += qualifiedDataTableName + "." + keyColumnName + "=" + qualifiedMetaTableName + "." + keyColumnName;
				if (iter.hasNext()) {
					sql += " AND ";
				}
			}
			sql += "\n);\n\n";

			acc += sql;
		}
		return acc;

	}

	/**
	 * @return
	 */
	public String createIdMapping() {
		String sql = "CREATE VIEW " + getSystemTableName(Nomenclature.IDS_TO_TABLES) + " AS (";
		try {
			DatabaseMetaData metaData = conn.getMetaData();
			String[] types = { "TABLE" };
			//ResultSet rs = metaData.getTables(null, schemaName, Nomenclature.META_PREFIX + "%", types);
			ResultSet rs = metaData.getTables(null, schemaName, (Nomenclature.META_PREFIX).toLowerCase() + "%", types);
			while (rs.next()) {
				String metaTableName = rs.getString("table_name");
				String tableName = Nomenclature.removeMetaDecorator(metaTableName);
				sql += "\n"
					+ Nomenclature.DELIMITER
					+ "(SELECT id,text '"
					+ tableName
					+ "' AS table_name FROM "
					+ schemaName
					+ "."
					+ metaTableName
					+ ") UNION";
			}
			//remove last union word
			sql = sql.substring(0, sql.length() - "UNION".length());
			sql += "\n);";
		}
		catch (SQLException e) {
			logger.error("Couldn't create ID to Table mapping. " + e);
		}

		return sql;
	}

	protected String getElementAlias(String xmlElementName) {
		// before the database is created, we don't look for any alias
		return xmlElementName;
	}

	protected String getAttributeAlias(String xmlAttributeName) {
		// before the database is created, we don't look for any alias
		return xmlAttributeName;
	}
}