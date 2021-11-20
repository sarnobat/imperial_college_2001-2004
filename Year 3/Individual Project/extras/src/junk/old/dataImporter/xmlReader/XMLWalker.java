/*
 * Created on 17-Mar-2004
 *  
 */
package xtractor.dataImporter.xmlReader;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

import xtractor.schemaConverter.exception.NoAssociatedSchemaException;
import xtractor.schemaConverter.xsd.JDomUtilities;

/**
 * @author ss401
 *  
 */

public class XMLWalker {
	String databaseSchemaName;
	Logger logger = Logger.getLogger(this.getClass());
	//DatabaseManager databaseManager;
	Document xmlDocument;
	File xmlDataFile;
	//Connection conn;
	DataPopulator dataPopulator;

	public XMLWalker(File xmlDataFile) throws NoAssociatedSchemaException {
		this.xmlDataFile = xmlDataFile;
		this.xmlDocument = JDomUtilities.getDocument(xmlDataFile);
		String schemaFileName = getAssociatedSchema();
		this.databaseSchemaName = schemaFileName.split(".x")[0];
		//this.databaseManager = new DatabaseManager(this.databaseSchemaName);
		//this.conn = DatabaseConnection.getConnection();
		this.dataPopulator = new DataPopulator(this.databaseSchemaName);
	}

	/**
	 * Populates the database with the information contained in the xml File specified in the field variable (that was set in the
	 * constructor)
	 */
	public void translateDocument() {
		Element root = xmlDocument.getRootElement();
		
		int rootId = dataPopulator.getGlobalId();
		translateChildren(root, rootId);
		
		dataPopulator.populateDatabaseWithRoot(root);
	}

	/**
	 * This recursively walks the whole XML file
	 * @param element - The element whose children are to be translated
	 * @param parentId - The id of the parent, which is required in the 'pid' field of each meta table
	 */
	private void translateChildren(Element parentElement, int parentId) {

		int order = 1;		
		List childrenElements = parentElement.getChildren();
		for (Iterator iter = childrenElements.iterator(); iter.hasNext();) {
			Element child = (Element) iter.next();
			String childName = child.getName();
			int childId = dataPopulator.getGlobalId();

			/*
			 * Note that the traversal must be POST ORDER,
			 * so we translate the children before the parent.
			 * However, we need to know the global id of the parent
			 * before we can populate the children data
			 */
			 // Translate child's children
			if (child.getChildren().size() > 0) {
				translateChildren(child, childId);
			}
			
			// Translate child itself
			dataPopulator.populateDatabaseWithElement(child, parentId,order);

			order++;

		}
	}

	/**
	 * @return
	 */
	private String getAssociatedSchema() throws NoAssociatedSchemaException {
		Element root = xmlDocument.getRootElement();

		List attributeNamesToValues = root.getAttributes();
		for (Iterator iter = attributeNamesToValues.iterator(); iter.hasNext();) {
			Attribute a = (Attribute) iter.next();
			if (a.getName().equals("schemaLocation")) {
				String schemaLocationAttributeValue = a.getValue();
				String[] locationComponents = schemaLocationAttributeValue.split(" ");
				return locationComponents[locationComponents.length - 1];
			}
		}
		logger.error("No schema has been associated with current xml document");
		throw new NoAssociatedSchemaException("Coudln't find associated XML Schema for document" + xmlDataFile.getName());
		/*
		THIS DOESN'T WORK - PERHAPS A BUG WITH JDOM
		 String schemaLocationAttributeValue = root.getAttributeValue("schemaLocation");	
		 if(schemaLocationAttributeValue == null) {
			logger.error("No schema has been associated with current xml document");
		}
		 //The attribute will be of the form xsi:schemaLocation="http://www.w3schools.com schemaFile.xsd" 
		String[] locationComponents = schemaLocationAttributeValue.split(" ");
		 */
	}




}
