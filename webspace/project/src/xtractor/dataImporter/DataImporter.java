/*
 * Created on 16-Mar-2004
 *
 */
package xtractor.dataImporter;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;

import xtractor.dataImporter.xmlReader.XMLWalker;

/**
 * @author ss401
 *
 * Root class of dataImporter package. Main application makes static call to importData()
 */
public class DataImporter {

	Logger logger = Logger.getLogger(this.getClass());

	XMLWalker xmlWalker;
	//Writer dialogOut;

	/**
		 * @param xmlFile
		 * @param databaseSchemaName
		 */
	public DataImporter(File xmlFile, String databaseSchemaName,Writer out) {
		this.xmlWalker = new XMLWalker(xmlFile, databaseSchemaName,out);
		//this.dialogOut = out;
	}

	/**
	 * 
	 */
	public void importData() {
		try {
			xmlWalker.translateDocument();
		}
		catch (IOException e) {
			logger.error("Couldn't write to dialog." + e);
		}		
	}

	//	public void importData(File xmlDataFile) throws NoAssociatedSchemaException{
	//
	//		XMLWalker xmlWalker = new XMLWalker(xmlDataFile);
	//		xmlWalker.translateDocument();
	//	}

}
