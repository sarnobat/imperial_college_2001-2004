import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import controller.Controller;

/*
 * Created on 14-May-2004
 *
 */

/**
 * @author ss401
 *
 */

public class Console {

	static Logger logger = Logger.getLogger("Console");
	static String tag = "<X>->";
	static Controller controller = new Controller("ss");
	static PrintStream out = System.out;
	static InputStream in = System.in;
	static BufferedReader br = new BufferedReader(new InputStreamReader(in));

	public static void main(String[] args) {
		// Initialize logger
		BasicConfigurator.configure();
		logger.setLevel(Level.ALL);

		// TODO: it would be great if you could erase standard output before each phase
		
		printTaskMenu("Please select a task number and press return.");

		// Find out what the user wants to do
		int taskType = getTaskTypeResponse();

		if (taskType == controller.IMPORT_DATA) {
			// Give the io for xml importing 
		}
		else if (taskType == controller.EXPORT_DATA) {

			//TODO: At this point you need to see what schema the user is interested in,
			// and thus create the controller object

			Collection fileNumbers = printXMLListing("Select the XML file you wish to generate.");

			int fileNumber = getFileNumberResponse(fileNumbers);
			
			// Create a new session object constructed with the given file
			
			
		}

	}

	/**
	 * 
	 * @param fileNumbers - A collection of Strings, each a valid id of an XML file 
	 * @return - The number of the XML file which the user wants to be generated
	 */
	private static int getFileNumberResponse(Collection fileNumbers) {

		out.print(tag);
		String s;
		try {
			s = br.readLine();
			while (!fileNumbers.contains(s)) {
				out.println("Invalid file number, please try again.");
				out.print(tag);
				s = br.readLine();
			}
			return Integer.parseInt(s);
		}
		catch (IOException e) {
			logger.error("Error trying to read from input stream." + e);
		}

		return -1;
	}

	/**
	 * @return - A collection of Strings, each the id of an xml file 
	 */
	private static Collection printXMLListing(String request) {
		String[][] fileTable = controller.getXMLFileTable();
		Collection fileNumbers = new LinkedList();
		out.println(request);
		out.println();

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
			fileNumbers.add(new String(fileTable[i][controller.FILE_NUMBER]));
		}
		return fileNumbers;
	}

	/**
	 * @return - The number of the task which the user wants to perform (a task being
	 * either to import or export a document) 
	 */
	private static int getTaskTypeResponse() {

		Collection optionNumbersAsStrings = getOptionsAsStrings();
		out.print(tag);
		String s;
		try {
			s = br.readLine();
			while (!optionNumbersAsStrings.contains(s)) {
				out.println("Invalid option, please try again.");
				out.print(tag);
				s = br.readLine();
			}
			return Integer.parseInt(s);
		}
		catch (IOException e) {
			logger.error("Error trying to read from input stream." + e);
		}

		return -1;
	}

	/**
	 * Prints the set of tasks a user may carry out. These are importing and exporting data
	 */
	private static void printTaskMenu(String request) {
		String[][] menu = controller.geTaskTypeTable();
		out.println(request);
		out.println();
		for (int i = 0; i < menu.length; i++) {
			out.print(
				"["
					+ menu[i][controller.OPTION_NUMBER]
					+ "]\t"
					+ menu[i][controller.ACTION]
					+ "\t-\t"
					+ menu[i][controller.DESCRIPTION]);

			out.println();
		}
		out.println();
	}

	/**
	 * 
	 * @return - A collection of Characters, where each character represents the number for
	 * a task option 
	 */
	private static Collection getOptionsAsCharacters() {
		Set s = new HashSet();
		for (int i = 0; i < controller.options.length; i++) {
			s.add(new Character(Character.forDigit(controller.options[i], 10)));
		}
		return s;
	}

	/**
	 * 
	 * @return - A collection of Strings, where each String represents the number for
	 * a task option 
	 */
	private static Collection getOptionsAsStrings() {
		Set s = new HashSet();
		for (int i = 0; i < controller.options.length; i++) {
			s.add(Integer.toString(controller.options[i]));
		}
		return s;
	}
}
