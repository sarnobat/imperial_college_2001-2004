/*
 * Created on 16-Mar-2004
 *
 */
package xtractor.dataImporter;

import java.io.File;

import org.apache.log4j.Logger;

import xtractor.dataImporter.xmlReader.XMLWalker;
import xtractor.schemaConverter.exception.NoAssociatedSchemaException;

/**
 * @author ss401
 *
 * Root class of dataImporter package. Main application makes static call to importData()
 */
public class DataImporter {
	Logger logger = Logger.getLogger(this.getClass());

	public void importData(File xmlDataFile) throws NoAssociatedSchemaException{
		/*
		 * Get root Element
		 * Iterate through child elements
		 * For each child element 
		 * 	insert attributes into element 
		 * 
		 * 
		 * 
		 * Add all processing instructions, mixed content, whitespace and comments
		 *  
		 */
		 
		//TODO:Make sure you handle overlapping documents properly

		XMLWalker xmlWalker = new XMLWalker(xmlDataFile);
		xmlWalker.translateDocument();
	}
}
