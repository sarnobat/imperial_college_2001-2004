package xtractor.schemaConverter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import xtractor.schemaConverter.rdb.DatabaseManager;
import xtractor.schemaConverter.rdb.RDBBuilder;
import xtractor.schemaConverter.xer.XERBuilder;
import xtractor.schemaConverter.xer.XERModel;
import databaseConnection.DatabaseConnection;
import databaseConnection.SchemaDisplayer;

/*
 * Created on 16-Feb-2004
 *
 */

/**
 * @author ss401
 * Root class to run schema converter
 */

public class SchemaConverter {
	Logger logger = Logger.getLogger("SchemaConverter");
	String schemaName;
	String comment;
	File xsdSchemaFile;
	DatabaseManager databaseManager;
	XERBuilder xerBuilder;

	/**
	 *@param schemaFile - The schema file which needs translating	 
	 */
	public SchemaConverter(File schemaFile, String comment) {

		this.comment = comment;

		// Build an XER Model		
		String schemaName = schemaFile.getName().split(".x")[0];
		databaseManager = new DatabaseManager(schemaName);
		this.xerBuilder = new XERBuilder(schemaFile);
	}

	public File convertSchema() {

		XERModel xerModel = xerBuilder.build();

		//logger.info("Resulting XER Model:\n" + xerModel + "\n\n\n\n\n\n");

		// Parse the XER Model,creating the relational database
		File databaseCreationScript = new File("relationalSchema.sql");
		try {
			// Create the database from it
			//Writer sqlWriter = new OutputStreamWriter(System.out);

			FileWriter sqlWriter = new FileWriter(databaseCreationScript);
			RDBBuilder rdbBuilder = new RDBBuilder(xerModel, xsdSchemaFile, sqlWriter, comment);
			rdbBuilder.build();
		}
		catch (IOException e) {
			logger.error("Couldn't write database creation script." + e);
		}
		try {
			Map errors = executeSQLScript(databaseCreationScript, null);
		}
		catch (IOException e1) {
			logger.error("Couldn't write errors to output stream.");
		}

		SchemaDisplayer relationalSchema = new SchemaDisplayer(schemaName);
		//logger.debug(relationalSchema.getSummary());
		return databaseCreationScript;
	}

	public File createSQLScript(XERModel xerModel, File xsdFile) {
		File databaseCreationScript = new File("programOutput.sql");
		try {
			FileWriter sqlWriter = new FileWriter(databaseCreationScript);
			RDBBuilder rdbBuilder = new RDBBuilder(xerModel, xsdFile, sqlWriter,comment);
			rdbBuilder.build();
		}
		catch (IOException e) {
			logger.error("Couldn't write database creation script." + e);
		}
		//Map errors = executeSQLScript(databaseCreationScript);
		//SchemaDisplayer relationalSchema = new SchemaDisplayer(schemaName);

		return databaseCreationScript;
	}

	/**
	 * In addition to executing what's in the script, we create complete views
	 * and also append them to the script (for debugging)
	 * @param sqlFile - The file containing the list of sql statements
	 * @param errorOutput - Where to write the errors to
	 * @return - A Map from String (failed statement) to SQLException (containing reason)
	 */
	public Map executeSQLScript(File sqlFile, Writer dialogOut) throws IOException {
		if (dialogOut == null) {
			dialogOut = new PrintWriter(System.err);
		}

		String s = "Creating database schema...";
		dialogOut.write(s + "\n");
		logger.debug(s);

		Connection conn = DatabaseConnection.getConnection();
		createSchemaListing(conn);
		Map statementToException = new TreeMap();
		int statementTotal = 0;
		try {

			FileReader r = new FileReader(sqlFile);
			Collection statements = getStatements(r);
			r.close();
			PreparedStatement ps;

			/*
			 * Execute table creation code from script
			 */
			for (Iterator iter = statements.iterator(); iter.hasNext();) {
				String statement = (String) iter.next();
				//dialogOut.write("Executing statement " + (statementTotal + 1) + "...");
				writePreStatementOutput(dialogOut, statementTotal);
				try {
					ps = conn.prepareStatement(statement);
					ps.execute();
					writePostStatementOutput(dialogOut, statement);
					//dialogOut.write("Success\n\tStatement was: " + statement);
				}
				catch (SQLException e) {
					String error = "Couldn't execute statement." + statement + "\n\tREASON: " + e;
					dialogOut.write(error);
					logger.error(error);
					statementToException.put(statement, e);
				}
				statementTotal++;
			}

			/*
			 * Create the further views (which rely on the database already existing)
			 */
			Collection sqlViewCreationCommands = new LinkedList();
			try {
				dialogOut.write("Obtaining data and meta table information to create views...");
				String sql = databaseManager.createCompleteViews();
				sql += databaseManager.createIdMapping();
				dialogOut.write("done\n");
				String[] sqlCommands = sql.split(";");
				sqlViewCreationCommands = Arrays.asList(sqlCommands);
			}
			catch (SQLException e) {
				String error = "Couldn't determine how to create element schema views. " + e;
				dialogOut.write(error);
				logger.error(error);
			}

			// invoke the code (we add the code to the script for debugging purposes)
			Writer fileWriter = new FileWriter(sqlFile, true);
			fileWriter.write("--------------------\n");
			fileWriter.write("------ VIEWS -------\n");
			fileWriter.write("--------------------\n");

			for (Iterator iter = sqlViewCreationCommands.iterator(); iter.hasNext();) {
				String statement = (String) iter.next();
				fileWriter.write(statement + ";");
				try {
					writePreStatementOutput(dialogOut, statementTotal);
					ps = conn.prepareStatement(statement);
					ps.execute();
					writePostStatementOutput(dialogOut, statement);
				}
				catch (SQLException e) {
					String error = "Couldn't execute statement." + statement + "\n\tREASON:" + e + "\n";
					dialogOut.write(error);
					logger.error(error);
					statementToException.put(statement, e);
				}
				statementTotal++;
			}
			fileWriter.flush();
		}
		catch (IOException e1) {
			String error = "Couldn't read sql script." + e1;
			logger.error(error);
			dialogOut.write(error);
		}

		logger.info("Executed " + (statementTotal - statementToException.size()) + " of " + statementTotal + " successfully.");
		dialogOut.write(
			"Executed " + (statementTotal - statementToException.size()) + " of " + statementTotal + " successfully.");
		return statementToException;

	}

	private void writePreStatementOutput(Writer dialogOut, int statementTotal) {
		try {
			dialogOut.write("Executing statement " + (statementTotal + 1) + "...");
		}
		catch (IOException e) {
			logger.error("Couldn't write to dialog." + e);
		}
	}
	private void writePostStatementOutput(Writer dialogOut, String sqlStatement) {
		try {
			dialogOut.write("Success\n"); //\tStatement was: " + sqlStatement);
		}
		catch (IOException e) {
			logger.error("Couldn't write to dialog." + e);
		}
	}

	private void createSchemaListing(Connection conn) {
		String sql = "CREATE TABLE XTractor_schemas (name VARCHAR(50) PRIMARY KEY);";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
		}
		catch (SQLException e) {
			logger.error("Table already exists." + e);
		}

	}

	public Collection getStatements(FileReader r) throws IOException {

		int c;
		String acc = "";
		while ((c = r.read()) != -1) {
			acc += (char) c;
		}

		String[] statements = acc.split(";");
		return Arrays.asList(statements);

	}

	public XERModel createXERModel() {
		return xerBuilder.build();
	}

}
