/*
 * Created on 15-May-2004
 *
 */
package model;

import controller.ControllerDatabaseManager;

/**
 * @author ss401
 *
 */
public class AbstractModel {


	ControllerDatabaseManager databaseManager;

	// Columns in file table
	public final int FILE_NUMBER = 0;
	public final int FILENAME = 1;
	public final int TIME_ADDED = 2;
	public final int COMMENT = 3;

	public AbstractModel(String databaseSchemaName) {		
		this.databaseManager = new ControllerDatabaseManager(databaseSchemaName);
	}

	/**
	 * @return - A 2D array representation of the XML File table, from which the user
	 * selects which document to produce
	 */
	public String[][] getXMLFileTable() {
		return databaseManager.getListOfFiles();
	}

}
