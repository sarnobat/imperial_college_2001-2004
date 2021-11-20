/*
 * Created on 14-May-2004
 */
package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import databaseConnection.databaseManager.AbstractDatabaseManager;

/**
 * @author ss401
 */
public class ControllerDatabaseManager extends AbstractDatabaseManager {

	/* Available fields in superclass:
	 * logger 
	 * conn
	 * schemaName
	 */

	public ControllerDatabaseManager(String databaseSchemaName) {
		super(databaseSchemaName);
	}

	/**
	 * @return - A 2D array representation of the list of xml files that have been imported
	 */
	/*public String[][] getListOfFiles() {
		String rootElement = getRootElementName();
		String sql = "SELECT id,filename,time_added,comment FROM " + getMetaTableName(rootElement);
		ResultSet rs = executeQuery(sql);
		try {
			ResultSetMetaData rsMeta = rs.getMetaData();
			String[][] table = new String[getRowCount(sql)][rsMeta.getColumnCount()];
	
			int i = 0;
			while (rs.next()) {
				//TODO: See if getTimeStamp does the time of insert for you
	
				for (int j = 0; j < rsMeta.getColumnCount(); j++) {
					table[i][j] = rs.getString(j + 1);
				}
				i++;
	
			}
			return table;
		}
		catch (SQLException e) {
			logger.error("Couldn't obtain list of XML files." + e);
			logger.debug("Query was " + sql);
		}
		return null;
	
	}*/
	public String[][] getListOfFiles() {
		String rootElement = getRootElementName();
		String sql = "SELECT id,filename,time_added,comment FROM " + getMetaTableName(rootElement);
		ResultSet rs = executeQuery(sql);
		int columnCount;
		try {
			columnCount = rs.getMetaData().getColumnCount();

			Collection files = new LinkedList();
			while (rs.next()) {
				String[] fileDetails = new String[columnCount];
				for (int i = 0; i < fileDetails.length; i++) {
					fileDetails[i] = rs.getString(i + 1);
				}
				files.add(fileDetails);
			}
			String[][] filesArr = new String[files.size()][columnCount];
			int k = 0;
			for (Iterator iter = files.iterator(); iter.hasNext();) {
				String[] fileDetails = (String[]) iter.next();
				for (int l = 0; l < fileDetails.length; l++) {
					filesArr[k] = fileDetails;
				}
				k++;
			}
			return filesArr;
		}
		catch (SQLException e) {
			logger.error("Couldn't obtain file list." + e);
			logger.debug("Query was: " + sql);
			return null;
		}
	}

}
