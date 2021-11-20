package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.apache.log4j.Logger;

import databaseConnection.DatabaseConnection;

/*
 * Created on 15-May-2004
 *
 */

/**
 * @author ss401
 * This should only be instantiated initially, and later subclassed to an
 * Import or Export model. And at the end of a session, superclassed
 */
public class Model {

	// Columns in task table
	public final int OPTION_NUMBER = 0;
	public final int ACTION = 1;
	public final int DESCRIPTION = 2;

	// Tasks (i.e. rows) in task table
	public final int IMPORT_DATA = 1;
	public final int EXPORT_DATA = 2;
	public final int[] options = { IMPORT_DATA, EXPORT_DATA };

	// Columns in schema table
	public final int SCHEMA_NAME = 0;
	public final int SCHEMA_COMMENT = 1;

	Logger logger = Logger.getLogger(this.getClass());
	
	public Model() {		
	}

	/**
	 * @return - An 2D array representation of the task table
	 */
	public String[][] geTaskTypeTable() {
		String[][] menu =
			{
				{
					Integer.toString(IMPORT_DATA),
					"IMPORT DATA",
					"Add data from an XML document to a Relational Database (If no database for it exists, it will be created)." },
				{
				Integer.toString(EXPORT_DATA), "EXPORT DATA", "Regenerate XML documents whose data was added to a database." }
		};
		return menu;
	}

	/**
	 * @return - A collection of Strings, where each String represents the number for
	 * a task option 
	 */
	public Collection getOptionsAsStrings() {
		Set s = new HashSet();
		for (int i = 0; i < options.length; i++) {
			s.add(Integer.toString(options[i]));
		}
		return s;
	}

	/**
	 * @return - A 2D array representation of a table of all schemas
	 */
	public String[][] getDatabaseSchemas() {
		// A collection of String[]
		Collection schemaMenu = new LinkedList();
		
		// we assume this table is a 'well_known' one
		String sql = "SELECT * FROM xtractor_schemas";
		PreparedStatement ps;
		try {
			ps = DatabaseConnection.getConnection().prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			int columnCount = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				String[] schemaRow = new String[columnCount];
				for (int i = 0; i < columnCount; i++) {
					schemaRow[i] = rs.getString(i + 1);
				}
				schemaMenu.add(schemaRow);
			}
			String[][] schemaMenuArr = new String[schemaMenu.size()][columnCount];
			int j = 0;
			for (Iterator iter = schemaMenu.iterator(); iter.hasNext();) {
				String[] row = (String[]) iter.next();
				schemaMenuArr[j] = row;
				j++;
			}
			return schemaMenuArr;
		}
		catch (SQLException e) {
			logger.error("Couldn't get table of schemas." + e);
			logger.debug("Query was " + sql);
			return null;
		}
		
	}
}
