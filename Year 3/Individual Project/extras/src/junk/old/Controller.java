package controller;

import org.apache.log4j.Logger;

/*
 * Created on 14-May-2004
 *
 */

/**
 * @author ss401
 *  This contains application structures of use for both the GUI and console version of the application
 *  (e.g. tables)
 */

public class Controller {

	// Columns in file table
	public final int FILE_NUMBER = 0;
	public final int FILENAME = 1;
	public final int TIME_ADDED = 2;
	public final int COMMENT = 3;

	//Columns in task table
	public final int OPTION_NUMBER = 0;
	public final int ACTION = 1;
	public final int DESCRIPTION = 2;

	// Tasks (i.e. rows) in task table
	public final int IMPORT_DATA = 1;
	public final int EXPORT_DATA = 2;
	public final int[] options = { IMPORT_DATA, EXPORT_DATA };

	Logger logger = Logger.getLogger(this.getClass());
	ControllerDatabaseManager databaseManager;

	public Controller(String databaseSchemaName) {
		this.databaseManager = new ControllerDatabaseManager(databaseSchemaName);
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
	 * @return - A 2D array representation of the XML File table, from which the user
	 * selects which document to produce
	 */
	public String[][] getXMLFileTable() {
		return databaseManager.getListOfFiles();
	}

}
