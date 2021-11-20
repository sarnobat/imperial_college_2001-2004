/*
 * Created on 15-May-2004
 *
 */
package controller;

import org.apache.log4j.Logger;

/**
 * @author ss401
 *
 */
public class Controller {
	
	// Columns in file table
	public final int FILE_NUMBER = 0;
	public final int FILENAME = 1;
	public final int TIME_ADDED = 2;
	public final int COMMENT = 3;

	Logger logger = Logger.getLogger(this.getClass());
	String databaseSchemaName;
	ControllerDatabaseManager databaseManager;

	/**
	 * @param databaseSchemaName - The name of a database schema 
	 */
	public Controller(String databaseSchemaName) {
		this.databaseSchemaName = databaseSchemaName;
		this.databaseManager = new ControllerDatabaseManager(databaseSchemaName);
	}

	/**
	 * @return - A 2D array representation of a table listing all xml files that have
	 * been imported into a database
	 */
	public String[][] getFiles() {
		return databaseManager.getListOfFiles();

	}

}
