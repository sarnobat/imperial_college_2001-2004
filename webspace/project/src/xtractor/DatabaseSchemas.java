/*
 * Created on 01-Jun-2004
 *
 */
package xtractor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import databaseConnection.DatabaseConnection;

/**
 * @author ss401
 *
 */
public class DatabaseSchemas {

	static Logger logger = Logger.getLogger("DatabaseSchemas");

	public static String[][] getSchemas() {
		java.sql.Connection conn = DatabaseConnection.getConnection();
		String sql = "SELECT * FROM XTractor_schemas";
		try {
			Collection schemaEntries = new LinkedList();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			int columns = 0;
			while (rs.next()) {
				ResultSetMetaData rsMeta = rs.getMetaData();
				columns = rsMeta.getColumnCount();
				String[] entry = new String[columns];
				for (int i = 1; i <= columns; i++) {
					Object o = rs.getObject(i);
					entry[i - 1] = o.toString();
				}
				schemaEntries.add(entry);
			}

			String[][] entries = new String[schemaEntries.size()][columns];
			int j = 0;
			for (Iterator iter = schemaEntries.iterator(); iter.hasNext();) {
				String[] entry = (String[]) iter.next();
				entries[j] = entry;
				j++;
			}
			return entries;
		}
		catch (SQLException e) {
			logger.debug("Table doesn't exist." + e);
			return new String[0][0];
		}
	}
}
