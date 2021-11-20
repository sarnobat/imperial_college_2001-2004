package xtractor;
import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import xtractor.dataExporter.DataExporter;
import xtractor.dataImporter.DataImporter;
import xtractor.schemaConverter.SchemaConverter;
import xtractor.schemaConverter.xer.XERModel;
import GUI.GUIController;
import GUI.windows.dialog.DialogWriter;

/*
 * Created on 13-Mar-2004
 *
 */

/**
 * @author ss401
 * This is the API class with all the business logic.
 */
public abstract class XTractorThread implements Runnable {
	
	GUIController controller;
	/**
	 * @param controller
	 */
	public XTractorThread(GUIController controller) {
		this.controller = controller;
	}

	Logger logger = Logger.getRootLogger();

	boolean getModel = false;
	File xsdFile; 

	public void main(String[] args) {
		// Initialize logger
		BasicConfigurator.configure();
		logger.setLevel(Level.DEBUG);

		//try {
		//translateSchema("xml/ss.xsd");
		logger.info("Finished generating schema.");
		//importData("xml/parts.xml");
		logger.info("Finished importing data.");
		//exportXMLFile(1, "ss");
		/*}
		catch (NoAssociatedSchemaException e) {
			logger.fatal("Couldn't find associated schema." + e);
		}*/

	}

//	private void translateSchema(String xsdFilePath) {
//		/*
//		 * get the attached schema
//		 * validate the document against it
//		 * IF Schema not previously encountered
//		 * 	create schema
//		 * perform import of XML file data
//		 * add the document to the document.xml list which includes id,date,database,comment
//		 * 	 
//		 */
//		File schemaFile = new File(xsdFilePath);
//		//SchemaConverter.convertSchema(schemaFile);
//
//	}


//	private void listImportedDocuments() {
//		/*
//		 * Iterate through xml file
//		 * 	print a list of documents
//		 */
//	}

	public XERModel getXERModel(File xsdFile) {
		SchemaConverter converter = new SchemaConverter(xsdFile);
		return converter.createXERModel();
	}
	public void setModel(File xsdFile) {
		getModel = true;
		this.xsdFile = xsdFile;

	}
	public void createRelationalDatabase(XERModel model, File xsdFile, Writer writer) {
		SchemaConverter converter = new SchemaConverter(xsdFile);
		File script = converter.createSQLScript(model, xsdFile);
		try {
			converter.executeSQLScript(script, writer);
		}
		catch (IOException e) {
			logger.error("Couldn't execute SQL script.");
		}
	}

	/**
	 * @return
	 */
	public String[][] getDatabaseSchemas() {

		return DatabaseSchemas.getSchemas();
	}

	public void importData(File xmlFile, String databaseSchemaName, Writer out) {
		DataImporter importer = new DataImporter(xmlFile, databaseSchemaName, out);
		importer.importData();
	}

	/**
	 * @return
	 */
	public String[][] getDocuments(String databaseSchemaName) {
		DataExporter exporter = new DataExporter(databaseSchemaName, null);
		return exporter.getFileList();
	}

	/**
	 * @param databaseSchema
	 * @param selectedDocumentNumber
	 * @param writer
	 * @return
	 */
	public File exportData(String databaseSchemaName, int selectedDocumentNumber, Writer writer) {
		DataExporter exporter = new DataExporter(databaseSchemaName, writer);
		return exporter.getFile(selectedDocumentNumber);
	}

	public void run() {
		int i = 0;
		while (true) {
			if (getModel) {
			}
			if(requestDBCreation){
				createRelationalDatabase(model,xsdFile,dialogWriter);
				requestDBCreation = false;
			}
			
			System.out.println(i);
			try {
				Thread.sleep(5);
			}
			catch (InterruptedException e) {
				logger.error("." + e);
			}
			i++;
		}

	}
	
	XERModel model;
	Writer dialogWriter;
	boolean requestDBCreation = false;

	/**
	 * @param model
	 * @param xsdFile
	 * @param dialogWriter
	 */
	public void requestDatabaseCreation(XERModel model, File xsdFile, DialogWriter dialogWriter) {
		
		this.model = model;
		this.dialogWriter = dialogWriter;
		this.xsdFile = xsdFile;
		requestDBCreation = true;
		
	}
}
