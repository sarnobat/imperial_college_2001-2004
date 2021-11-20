package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import model.Model;

import org.apache.log4j.Logger;

import xtractor.XTractor;

import controller.Controller;
import exception.NoDatabaseSchemasExistException;
import exception.NoDocumentsExistException;

/*
 * Created on 15-May-2004
 *
 */

/**
 * @author ss401
 *
 */
public class Console {

	Logger logger = Logger.getLogger(this.getClass());

	Model model;
	//Model sessionModel;
	PrintStream out;
	BufferedReader in;
	String tag = "<X>->";
	Controller controller;
	//from String (not integer) to String. Populated when printSchemaMenu is called
	Map schemaNumbersToNames;

	/**
	 * The application is automatically set running
	 */
	public Console() {

		this.out = System.out;
		this.in = new BufferedReader(new InputStreamReader(System.in));
		this.model = new Model();
		this.schemaNumbersToNames = new HashMap();

		run();

	}

	/**
	 * 
	 */
	private void run() {
		while (true) {
			//TODO: it would be great if you could erase standard output before each phase{
		
			printTaskMenu("Please select a task number and press return.");
			
			// Find out what the user wants to do
			int taskType = Integer.parseInt(readString(model.getOptionsAsStrings()));
			

			if (taskType == model.IMPORT_DATA) {
				// Give the io for xml importing 
			}
			else if (taskType == model.EXPORT_DATA) {

				//TODO: At this point you need to see what schema the user is interested in,
				// and thus create the controller object
				try {
					printSchemaMenu("Please select the number of the database from which you wish to export data.");
				}
				catch (NoDatabaseSchemasExistException e1) {

					out.println("\n[NO SCHEMAS CURRENTLY IN DATABASE]\n");
					continue;

				}
				Collection schemaNumbersAsStrings = schemaNumbersToNames.keySet();
				String schemaNumber = readString(schemaNumbersAsStrings);
				String schemaName = (String)schemaNumbersToNames.get(schemaNumber);
				

				this.controller = new Controller(schemaName);
				String[][] fileMenu = controller.getFiles();

				Map fileNumbersToNames = null;
				try {
					fileNumbersToNames = printXMLListing("Select the XML file you wish to generate.", fileMenu);
				}
				catch (NoDocumentsExistException e) {
					out.println("\n[NO DOCUMENTS HAVE BEEN IMPORTED TO THIS DATABASE]\n");
					continue;
				}

				int documentId = Integer.parseInt(readString(fileNumbersToNames.keySet()));
				

				XTractor.exportXMLFile(documentId, schemaName);

			}
			out.println("\nPlease select an option:\n");
			out.println("[1]\t-\tReturn to main menu");
			out.println("[2]\t-\tQuit application\n");
			String[] options = { "1", "2" };
			String response = readString(options);
			if (response.equals("1")) {
				continue;
			}
			else if (response.equals("2")) {
				break;
			}
			else {
				logger.error("Invalid option selected.");
			}
		}

	}

	/**
	 * @param validOptions - A set of valid strings for the user to input
	 * @return - The valid string given by the user
	 */
	private String readString(String[] validOptions) {
		return readString(Arrays.asList(validOptions));
	}

	/**
	 * 
	 * @param validOptions - A set of valid strings for the user to input
	 * @return - The valid string given by the user
	 */
	private String readString(Collection validOptions) {

		out.print(tag);
		String s;
		try {
			s = in.readLine();
			while (!validOptions.contains(s)) {
				out.println("Invalid option, please try again.");
				out.print(tag);
				s = in.readLine();
			}
			return s;
		}
		catch (IOException e) {
			logger.error("Error trying to read from input stream." + e);
			return null;
		}
	}

	/**
	 * @return - A Map of xml document ids (Strings) to xml docmuent names (Strings) 
	 */
	private Map printXMLListing(String request, String[][] fileTable) throws NoDocumentsExistException {

		if (fileTable.length == 0) {

			throw new NoDocumentsExistException();
		}
		else {
			out.println(request);
			out.println();
			Map fileNumbersToNames = new HashMap();

			for (int i = 0; i < fileTable.length; i++) {

				out.println(
					"["
						+ fileTable[i][controller.FILE_NUMBER]
						+ "]\t"
						+ fileTable[i][controller.FILENAME]
						+ "\t"
						+ fileTable[i][controller.TIME_ADDED]
						+ "\t"
						+ fileTable[i][controller.COMMENT]);
				//fileNumbers.add(new String(fileTable[i][controller.FILE_NUMBER]));
				fileNumbersToNames.put(fileTable[i][controller.FILE_NUMBER], fileTable[i][controller.FILENAME]);
			}
			return fileNumbersToNames;
		}
	}



	/**
	 * @param request - The request the console makes to the user to select a database schema
	 */
	private void printSchemaMenu(String request) throws NoDatabaseSchemasExistException {
		String[][] schemaTable = model.getDatabaseSchemas();
		if (schemaTable.length == 0) {

			throw new NoDatabaseSchemasExistException();

		}
		else {

			out.println();
			out.println(request);
			out.println();

			for (int i = 0; i < schemaTable.length; i++) {
				out.println(
					"[" + (i + 1) + "]\t" + schemaTable[i][model.SCHEMA_NAME] + "\t" + schemaTable[i][model.SCHEMA_COMMENT]);
				this.schemaNumbersToNames.put(Integer.toString(i + 1), schemaTable[i][model.SCHEMA_NAME]);
			}
		}

	}

	/**
	 * Prints the set of tasks a user may carry out. These are importing and exporting data
	 */
	private void printTaskMenu(String request) {
		String[][] menu = model.geTaskTypeTable();
		out.println(request);
		out.println();
		for (int i = 0; i < menu.length; i++) {
			out.print("[" + menu[i][model.OPTION_NUMBER] + "]\t" + menu[i][model.ACTION] + "\t-\t" + menu[i][model.DESCRIPTION]);

			out.println();
		}
		out.println();
	}

}
