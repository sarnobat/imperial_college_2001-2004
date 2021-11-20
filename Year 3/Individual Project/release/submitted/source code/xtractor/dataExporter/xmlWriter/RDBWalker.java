/*
 * Created on 13-May-2004
 */
package xtractor.dataExporter.xmlWriter;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

import databaseConnection.Nomenclature;

/**
 * @author ss401
 */
public class RDBWalker {

	Logger logger = Logger.getLogger(this.getClass());
	Writer fileWriter;
	Writer consoleWriter;
	ExportDatabaseManager databaseManager;
	String databaseSchemaName;

	/**
	 * @param fileWriter - The writer to write the xml output to
	 */
	public RDBWalker(String databaseSchemaName, Writer consoleWriter) {
		this.consoleWriter = consoleWriter;
		this.databaseSchemaName = databaseSchemaName;
		this.databaseManager = new ExportDatabaseManager(databaseSchemaName);
	}

	/**
	 * Does the file writing  
	 * @param rootId - The globally unique root identifier for the element schema
	 */

	public Document writeXMLFile(int rootId, Writer fileWriter) throws IOException {
		this.fileWriter = fileWriter;
		String rootElementAlias = databaseManager.getElementName(rootId);
		String rootElementName =
			databaseManager.getOriginalElementName(rootElementAlias);
		Element root =
			new Element(
				databaseManager.getOriginalElementName(rootElementName));
		Document doc = new Document(root);

		addChildElements(rootId, root);

		return doc;

	}

	/**
	 * Finds out all the child elements of 'parent', and adds them to it
	 * (actually, only elements corresponding to tables)
	 * @param parentId
	 * @param parent
	 */
	private void addChildElements(int parentId, Element parent) throws IOException {
		TreeMap ordsToIds = databaseManager.getOrdsToIds(parentId);
		Map idsToElementNames = databaseManager.getIdsToElementNames(parentId);
		Map idsToAttributeMap = databaseManager.getIdsToAttributeMap(parentId);

		// We are guaranteed to uncover the ids in the right order because we are
		// using a tree map where they key set is a set of ords
		for (Iterator iter = ordsToIds.keySet().iterator(); iter.hasNext();) {

			// Get all the information about this child
			Integer childOrd = (Integer) iter.next();
			Integer childId = (Integer) ordsToIds.get(childOrd);
			
			consoleWriter.write("Create element with id " +childId+"...");
			
			Map attributeNamesToValues = (Map) idsToAttributeMap.get(childId);
			String elementName = (String) idsToElementNames.get(childId);
			String characterData = null;

			if (databaseManager.isSimpleElement(elementName)
				|| databaseManager.isMixedElement(elementName)) {
				characterData =
					databaseManager.getCharacterData(childId.intValue());
			}

			// Create the child Element
			Element childElement =
				reconstructElement(
					parent,
					elementName,
					attributeNamesToValues,
					characterData);
			
			consoleWriter.write("success\n");		
			
			// Add it to our tree
			parent.addContent(childElement);

			// Recursively add the grandChildren
			addChildElements(childId.intValue(), childElement);
		}
	}

	/**
	 * Creates the JDOM element given all the necessary information
	 * @param parent - e.g. <ps_db>
	 * @param elementName - The name of an element in Database nomenclature
	 * @param attributeNamesToValues - String to String, e.g. ["pno"-->"100","colour"-->"red",price-->"20"]
	 */
	private Element reconstructElement(
		Element parent,
		String elementAlias,
		Map attributeNamesToValues,
		String characterData) {
		Element child =
			new Element(databaseManager.getOriginalElementName(elementAlias));

		for (Iterator iter = attributeNamesToValues.keySet().iterator();
			iter.hasNext();
			) {
			String attributeAlias = (String) iter.next();
			Object attributeValueObj =
				(Object) attributeNamesToValues.get(attributeAlias);
			String attributeValue =
				Nomenclature.convertAttributeValueToString(attributeValueObj);

			String attributeName =
				databaseManager.getOriginalAttributeName(attributeAlias);

			child.setAttribute(new Attribute(attributeName, attributeValue));
		}

		if (characterData != null) {
			child.setText(characterData);
		}

		return child;
	}

	/**
	 * @return
	 */
	public String[][] getDocuments() {
		return databaseManager.getDocuments();
	}

}
