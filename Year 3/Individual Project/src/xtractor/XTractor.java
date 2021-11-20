package xtractor;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.log4j.Logger;

import xtractor.dataExporter.DataExporter;
import xtractor.dataImporter.DataImporter;
import xtractor.schemaConverter.SchemaConverter;
import xtractor.schemaConverter.xer.XERModel;

/*
 * Created on 13-Mar-2004
 *
 */

/**
 * @author ss401
 * This is the API class with all the business logic.
 */
public class XTractor {
	static Logger logger = Logger.getRootLogger();

	/**
	 * 
	 * @param xsdFile - An XML Schema file
	 * @return - The XER model of the schema
	 */
	public static XERModel getXERModel(File xsdFile) {
		SchemaConverter converter = new SchemaConverter(xsdFile,"");
		return converter.createXERModel();
	}

	/**
	 * 
	 * @param model - an XER model of an XML Schema
	 * @param xsdFile - an XML Schema file
	 * @param writer - A writer for writing dialog output to
	 */
	public static void createRelationalDatabase(XERModel model, File xsdFile, Writer writer,String comment) {
		SchemaConverter converter = new SchemaConverter(xsdFile,comment);
		File script = converter.createSQLScript(model, xsdFile);
		try {
			converter.executeSQLScript(script, writer);
		}
		catch (IOException e) {
			logger.error("Couldn't execute SQL script.");
		}
	}

	/**
	 * 
	 * @return - An array listing all relational schemas that have been created
	 */
	public static String[][] getDatabaseSchemas() {		
		return DatabaseSchemas.getSchemas();
	}

	/**
	 * 
	 * @param xmlFile - An XML file whose data is to be imported
	 * @param databaseSchemaName - The relational schema where the data should be imported
	 * @param out - A dialog output writer
	 */
	public static void importData(File xmlFile,String databaseSchemaName,Writer out){
		DataImporter importer = new DataImporter(xmlFile,databaseSchemaName,out);
		importer.importData();
	}

	/**
	 * @return - An array of document names that were imported into the relational schema
	 */
	public static String[][] getDocuments(String databaseSchemaName) {
		DataExporter exporter = new DataExporter(databaseSchemaName,null);
		return exporter.getFileList();
	}

	/**
	 * @param databaseSchema - The name of a relational schema from which you wish to export data
	 * @param selectedDocumentNumber - The number of the document that should be regenerated
	 * @param writer - A dialog output writer
	 * @return - A newly created XML file corresponding to the document number
	 */
	public static File exportData(String databaseSchemaName, int selectedDocumentNumber, Writer writer) {
		DataExporter exporter = new DataExporter(databaseSchemaName,writer);
		return exporter.getFile(selectedDocumentNumber);
	}

	/**
	 * @param selectedSchemaName
	 * @return - Map of Strings to Collections of maps
	 * {
	 * 		tableName --> {Map,Map,Map,...},
	 * 		tableName --> {Map,Map,Map,...},
	 * 		tableName --> {Map,Map,Map,...}
	 * }
	 * Each map corresponds to a row in the table. These maps are from column name to column value
	 */
	public static Map getTableData(String databaseSchemaName) {
		DataExporter exporter = new DataExporter(databaseSchemaName,new PrintWriter(System.out));
		return exporter.getTableData();
	}
}
