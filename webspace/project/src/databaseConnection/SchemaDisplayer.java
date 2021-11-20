/*
 * Created on 24-May-2004
 *
 */
package databaseConnection;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author ss401
 *
 */
public class SchemaDisplayer {
	Logger logger = Logger.getLogger(this.getClass());

	String databaseSchemaName;
	String summary;
	java.sql.Connection conn;
	DatabaseMetaData metaData;

	/**
	 * @param schemaName - The name of the database schema under which all the tables are 
	 * created
	 */
	public SchemaDisplayer(String schemaName) {
		this.databaseSchemaName = schemaName;
		conn = DatabaseConnection.getConnection();
		try {
			metaData = conn.getMetaData();
		}
		catch (SQLException e) {
			logger.error("Error getting meta data." + e);
		}
		createSummary();
	}

	private String getTableSummary(String tableName) throws SQLException {
		String acc = "";
		Collection attributes = getAttributes(tableName);
		acc += tableName + "(";
		for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
			String attributeName = (String) iterator.next();
			acc += attributeName;
			if (iterator.hasNext()) {
				acc += ",";
			}
		}
		acc += ")\n";

		return acc;
	}

	private void createSummary() {
		summary = "\n\n";
		String metaSummary = "";
		String systemSummary = "";
		try {
			List tableNames = getTableNames();
			Collections.sort(tableNames);

			for (Iterator iter = tableNames.iterator(); iter.hasNext();) {
				String tableName = (String) iter.next();

				if (tableName.startsWith("meta_")) {
					metaSummary += "\t" + getTableSummary(tableName);
				}
				else if (tableName.startsWith("system_")) {
				systemSummary += "\t" + getTableSummary(tableName);
			}
				else {
					summary += "\t" + getTableSummary(tableName);
				}

			}

			summary += "\n" + metaSummary +"\n"+systemSummary;

		}
		catch (SQLException e) {
			logger.error("." + e);
		}

	}

	/**
	 * @param tableName
	 * @return
	 */
	private Collection getAttributes(String tableName) throws SQLException {
		Collection attributes = new LinkedList();
		String sql = "SELECT * FROM " + databaseSchemaName + "." + tableName;
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		ResultSetMetaData meta = rs.getMetaData();
		int columnCount = meta.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			attributes.add(meta.getColumnName(i));
		}
		return attributes;
	}

	/**
	 * @return - A list of Strings for each qualified table name
	 */
	private List getTableNames() throws SQLException {
		List tableNames = new LinkedList();

		//pg_class fetches a list of tables, indexes, sequences ("relations")
		String[] types = { "TABLE" };
		ResultSet rs = metaData.getTables("pg_class", databaseSchemaName, "%", types);
		while (rs.next()) {
			String tableName = rs.getString("table_name");
			tableNames.add(tableName);
		}

		return tableNames;
	}

	/**
	 * @return - A string summarising the whole schema
	 */
	public String getSummary() {
		return summary;

	}

}
