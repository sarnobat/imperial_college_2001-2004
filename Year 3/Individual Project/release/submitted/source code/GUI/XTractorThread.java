/*
 * Created on 01-Jun-2004
 *
 */
package GUI;

import java.io.File;
import java.io.Writer;

import org.apache.log4j.Logger;

import xtractor.XTractor;
import xtractor.schemaConverter.xer.XERModel;
import GUI.windows.dialog.DialogWriter;

/**
 * @author ss401
 *
 */
public class XTractorThread implements Runnable {

	Logger logger = Logger.getLogger(this.getClass());
	GUIController controller;

	final int NONE = 0;
	int option = NONE;

	/**
	 * @param controller
	 */
	public XTractorThread(GUIController controller) {
		this.controller = controller;
	}

	final int CREATE_DATABASE = 1;
	XERModel model;
	File xsdFile;
	DialogWriter createDBdialogWriter;

	public void requestDatabaseCreation(XERModel model, File xsdFile, DialogWriter dialogWriter) {
		this.model = model;
		this.xsdFile = xsdFile;
		this.createDBdialogWriter = dialogWriter;
		this.option = this.CREATE_DATABASE;
	}

	final int IMPORT_FILE = 2;
	File xmlFile;
	String importDatabaseSchema;
	Writer importDialogWriter;

	public void requestImportFile(File xmlFile, String databaseSchemaName, Writer importDialogWriter) {
		this.xmlFile = xmlFile;
		this.importDatabaseSchema = databaseSchemaName;
		this.importDialogWriter = importDialogWriter;
		this.option = this.IMPORT_FILE;

	}

	final int EXPORT_FILE = 3;
	String exportDatabaseSchema;
	int selectedDocumentNumber;
	Writer exportDialogWriter;
	public void requestExportFile(String databaseSchema, int selectedDocumentNumber, Writer exportDialogWriter) {
		this.exportDatabaseSchema = databaseSchema;
		this.selectedDocumentNumber = selectedDocumentNumber;
		this.exportDialogWriter = exportDialogWriter;
		this.option = EXPORT_FILE;

	}

	public void run() {

		while (true) {

			switch (option) {

				case CREATE_DATABASE :
					{
						XTractor.createRelationalDatabase(model, xsdFile, createDBdialogWriter);
						break;
					}

				case IMPORT_FILE :
					{
						XTractor.importData(xmlFile, importDatabaseSchema, importDialogWriter);
						break;
					}
				case EXPORT_FILE :
					{
						XTractor.exportData(exportDatabaseSchema,selectedDocumentNumber,exportDialogWriter);
						break;
					}
				case NONE :
					{
						break;
					}
				default :
					{
						break;
					}
					
			}
			option = NONE;
			try {
				Thread.sleep(10);
			}
			catch (InterruptedException e) {
				logger.error("Couldn't sleep thread." + e);
			}
		}

	}

}
