/*
 * Created on 13-May-2004
 */
package xtractor.dataExporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;

import xtractor.dataExporter.xmlWriter.RDBWalker;

/**
 * @author ss401
 *
 */
public class DataExporter {

	Logger logger = Logger.getLogger("DataExporter");
	String databaseSchemaName;

	public DataExporter(String schemaName) {
		this.databaseSchemaName = schemaName;
	}

	/**
	 * XML file will be created in the default location
	 * @param id - The id of the document which the user wants to regenerate
	 */
	public void exportData(int id) {

		String outputFilePath = "xml/output/" + id + ".xml";
		File xmlFile = new File(outputFilePath);
		Writer fileWriter = null;
		boolean newFile;
		try {
			newFile = xmlFile.createNewFile();
			if (!newFile) {
				logger.warn("File " + xmlFile.getName() + " already existed, and was overwritten.");
			}
		}
		catch (IOException e) {
			logger.error("Couldn't create new file: " + e);
		}

		try {
			fileWriter = new FileWriter(xmlFile);
		}
		catch (IOException e1) {
			logger.error("Couldn't open file for writing." + e1);
		}
		
		RDBWalker rdbWalker = new RDBWalker(fileWriter,databaseSchemaName);
		rdbWalker.writeXMLFile(id);
	}
}
