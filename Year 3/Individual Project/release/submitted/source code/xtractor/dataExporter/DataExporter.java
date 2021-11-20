/*
 * Created on 13-May-2004
 */
package xtractor.dataExporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import xtractor.FolderDiscloser;
import xtractor.dataExporter.xmlWriter.RDBWalker;

/**
 * @author ss401
 *
 */
public class DataExporter {

	Logger logger = Logger.getLogger("DataExporter");
	String databaseSchemaName;
	RDBWalker rdbWalker;
	Writer consoleWriter;

	public DataExporter(String schemaName, Writer consoleWriter) {
		this.databaseSchemaName = schemaName;
		this.consoleWriter = consoleWriter;
		rdbWalker = new RDBWalker(databaseSchemaName, consoleWriter);
	}

	/**
	 * XML file will be created in the default location
	 * @param id - The id of the document which the user wants to regenerate
	 */
	public File exportData(int id) throws IOException {

		String outputFileName = id + ".xml";
		File xmlFile = new File(FolderDiscloser.workingFolderPath + "/"+outputFileName);
		Writer fileWriter = null;
		boolean newFile;
		try {
			
			newFile = xmlFile.createNewFile();
			if (!newFile) {
				String s = "File " + xmlFile.getName() + " already exists, and is being overwritten.\n";
				logger.warn(s);
				consoleWriter.write(s);
			}
		}
		catch (IOException e) {
			String s = "Couldn't create new file: " + e;
			logger.error(s);
			consoleWriter.write(s);
		}

		try {
			fileWriter = new FileWriter(xmlFile);
			//rdbWalker = new RDBWalker(fileWriter,databaseSchemaName);
			consoleWriter.write("Beginning document reconstruction...\n");
			Document document = rdbWalker.writeXMLFile(id, fileWriter);
			XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

			xmlOutputter.output(document, fileWriter);
			String s = "Successfully wrote file. Can be found at: " + xmlFile.getAbsolutePath();
			logger.info(s);
			consoleWriter.write(s);
		}
		catch (IOException e1) {
			String s = "Couldn't open file for writing." + e1;
			logger.error(s);
			consoleWriter.write(s);
		}

		return xmlFile;
	}

	/**
	 * @return
	 */
	public String[][] getFileList() {
		return rdbWalker.getDocuments();
	}

	/**
	 * @param selectedDocumentNumber
	 * @param writer
	 * @return
	 */
	public File getFile(int selectedDocumentNumber) {
		try {
			return exportData(selectedDocumentNumber);
		}
		catch (IOException e) {
			logger.error("Couldn't write to console. " + e);
			return null;
		}
	}
}
