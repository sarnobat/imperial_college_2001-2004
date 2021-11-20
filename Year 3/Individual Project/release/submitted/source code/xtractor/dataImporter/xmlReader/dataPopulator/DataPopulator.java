/*
 * Created on 25-May-2004
 *
 */
package xtractor.dataImporter.xmlReader.dataPopulator;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Parent;

import xtractor.dataImporter.xmlReader.dataPopulator.databaseManager.ImportDatabaseManager;
import exception.AddDataException;
import exception.AddMetaDataException;

/**
 * @author ss401
 *
 */
public class DataPopulator {

	Logger logger = Logger.getLogger(this.getClass());

	private ImportDatabaseManager dbManager;
	Writer out;

	public DataPopulator(String databaseSchemaName, Writer out) {
		this.out = out;
		this.dbManager = new ImportDatabaseManager(databaseSchemaName);
	}

	/**
	 * @param root
	 * @return - The id of the root to be used in child table references
	 */
	public int populateDatabaseWithRoot(Element root, int rootId) throws AddMetaDataException {

		String elementName = root.getName();

		// Perform insert
		TreeMap dataColumnsAndValues = new TreeMap();
		TreeMap metaColumnsAndValues = new TreeMap();
		//int rootId = dbManager.getNextGlobalId();
		metaColumnsAndValues.put("id", new Integer(rootId));
		metaColumnsAndValues.put("pid", new Integer(0));
		// note: 0 means that it has no parent		
		metaColumnsAndValues.put("ord", new Integer(1));

		if (dbManager.dataTableUsesAutoKey(root.getName())) {
			String primaryKeyName = dbManager.getAutoKeyName(elementName);

			// The key isn't explicitly stated in the XML, so we need the sequence value
			int primaryKeyValue = dbManager.getNextId(elementName);

			// add the primary key in the data table
			dataColumnsAndValues.put(primaryKeyName, new Integer(primaryKeyValue));
			// add the foreign key in the meta table
			metaColumnsAndValues.put(primaryKeyName, new Integer(primaryKeyValue));
		}

		// Perform the data additions.
		try {
			dbManager.addData(elementName, dataColumnsAndValues);
		}
		catch (SQLException e) {
			//logger.error("." + e);
			try {
				out.write("Couldn't add data from element '" + elementName + "'.\n\tREASON: " + e);
			}
			catch (IOException e1) {
				//logger.error("." + e);
			}
		}
		dbManager.addMetaData(elementName, metaColumnsAndValues);

		// Determine id of root element
		return rootId;

	}

	/**
	 * 
	 * @param element - The element whose data we are trying to insert
	 * @param parentId - The element schema global id of the parent containing this element,
	 * so that we know the pid of 'element'
	 * @param order - the order that 'element' occurs in under the parent
	 * @return - The element schema global id of the element that's jsut been inserted
	 * (so that children may use it)
	 */
	public void populateDatabaseWithElement(Element element, int elementId, int parentId, int order)
		throws AddMetaDataException, AddDataException {

		if (dbManager.isSimpleElement(element.getName())) {
			//TODO: Only add a meta entry for simple elements; the data entry as an attribue should happen
			//when the parent is populated
			populateDatabaseWithSimpleElement(element, elementId, parentId, order);
		}
		else {
			populateDatabaseWithComplexElement(element, elementId, parentId, order);
		}

		if (dbManager.isSpecialization(element.getName())) {
			dbManager.addGeneralizationEntry(element, elementId, parentId);
		}

	}

	/**
	 * @param element
	 * @param elementId
	 * @param parentId
	 * @param order
	 */
	private void populateDatabaseWithSimpleElement(Element element, int elementId, int parentId, int order)
		throws AddMetaDataException, AddDataException {

		// sanity check
		if (element.getChildren().size() > 0) {
			logger.error("This is not a simple element");
		}

		if (dbManager.isMultiValued(element.getName())) {
			// If it's an element that can occur multiple times we know where to put both the data and meta information
			Map parentKeys = getParentKeys(element, parentId);

			// Data Table
			Map dataColumnNamesAndValues = new HashMap();
			dataColumnNamesAndValues.putAll(parentKeys);
			dataColumnNamesAndValues.put(element.getName(), element.getTextTrim());
			populateDataTable(element, dataColumnNamesAndValues);

			// Meta Table
			Map metaColumnNamesAndValues = new HashMap();
			// We put all columns from the data table in the meta table when the data table
			// is for a multi-valued attribute 
			metaColumnNamesAndValues.putAll(dataColumnNamesAndValues);
			populateMetaTable(element, elementId, parentId, order, metaColumnNamesAndValues);
		}
		else {
			// If it's a singleton element (effectively an attribute) we need to find out where the information goes
			// (it may be stored as an attribute rather than an element)
			//PICKUP: This is applicable to the 'sup' element

			Parent parentDocOrElem = element.getParent();
			if (parentDocOrElem instanceof Element) {
				Element parent = (Element) parentDocOrElem;
				String elementValue = element.getText();
				populateDataTableColumn(parent, element.getName(), elementValue, parentId);
			}
			else {
				logger.warn("Unimplemented case: simple element directly under the root.");
			}

		}

	}

	/**
	 * Adds a simple element's value to a particular row 
	 * @param element - The parent element containing the simple element (it is this element whose table the
	 * data should be inserted into)
	 * @param elementNamesToValues - A map from a SIMPLE element name to an element's character data value
	 * @param parentId - This will be needed to determine what row to insert the data into  
	 */
	private void populateDataTableColumn(Element parentElement, String attributeName, Object attributeValue, int parentId)
		throws AddDataException {
		try {
			dbManager.addSimpleElementData(parentElement.getName(), attributeName, attributeValue, parentId);
		}
		catch (SQLException e) {
			String error = "Couldn't add column data. " + e;
			logger.error(error);
			throw new AddDataException(e);
		}
	}

	/**
	 * @param element
	 * @param elementId
	 * @param parentId
	 * @param order
	 */
	private void populateDatabaseWithComplexElement(Element element, int elementId, int parentId, int order)
		throws AddMetaDataException, AddDataException {

		// Get the keys of the parent element for implicit referencing (could be none)
		Map parentKeys = getParentKeys(element, parentId);

		// Get the key attributes for insertion into the meta and data table
		Map keys = getKeyAttributes(element);

		// Get the non-key attributes
		Map attributes = getAttributeValues(element);

		// Get the character data
		Map characterData = getCharacterData(element);

		// Note: the above maps may overlap, but placing them all in one map removes duplicates

		/*
		 * Populate both tables
		 */

		Map dataTableColumnValues = new TreeMap();
		dataTableColumnValues.putAll(parentKeys);
		dataTableColumnValues.putAll(keys);
		dataTableColumnValues.putAll(attributes);
		dataTableColumnValues.putAll(characterData);
		populateDataTable(element, dataTableColumnValues);

		Map metaTableColumnValues = new TreeMap();
		metaTableColumnValues.putAll(keys);
		populateMetaTable(element, elementId, parentId, order, metaTableColumnValues);

		//logger.warn("Unimplemented Method");

	}

	/**
	 * @param element
	 * @param elementId		- id
	 * @param parentId		- pid
	 * @param order			- ord
	 * @param dataTableKeys - the primary key of the corresponding data table entry
	 */
	private void populateMetaTable(Element element, int elementId, int parentId, int order, Map dataTableKeys)
		throws AddMetaDataException {
		Map metaTableColumnValues = new HashMap();
		metaTableColumnValues.putAll(dataTableKeys);
		metaTableColumnValues.put("id", new Integer(elementId));
		metaTableColumnValues.put("pid", new Integer(parentId));
		metaTableColumnValues.put("ord", new Integer(order));

		dbManager.addMetaData(element.getName(), metaTableColumnValues);

	}

	/**
	 * @param element
	 * @param dataTableColumnValues
	 */
	private void populateDataTable(Element element, Map dataTableColumnValues) throws AddDataException {
		dbManager.addData(element.getName(), dataTableColumnValues);
	}

	/**
	 * @param element
	 * @return
	 */
	private Map getCharacterData(Element element) {
		Map m = new TreeMap();
		String cData = element.getTextTrim();
		if (cData.length() == 0) {
			return new TreeMap();
		}
		m.put(element.getName(), cData);
		// there should only be one memeber
		return m;
	}

	/**
	 * Gets the attributes (and single element children) values from an XML Element
	 * @param element
	 * @return
	 */
	private Map getAttributeValues(Element element) {

		Map attributeNamesToValues = new TreeMap();
		Collection attributes = element.getAttributes();

		for (Iterator iter = attributes.iterator(); iter.hasNext();) {
			Attribute attribute = (Attribute) iter.next();
			attributeNamesToValues.put(attribute.getName(), attribute.getValue());
		}

		// TODO: get child elements with character data only
		// don't forget simple child elements are effectively attributes

		return attributeNamesToValues;
	}

	/**
	 * These should be got from the DATABASE, not the XML file
	 * @param element
	 * @param parentId
	 * @return
	 */
	private Map getParentKeys(Parent childElement, int parentId) {

		if (childElement instanceof Document) {
			// There are no keys for the parent of the root element
			return new TreeMap();
		}
		else {
			Element childElementElem = (Element) childElement;
			if (dbManager.isSpecialization(childElementElem.getName())) {
				// Specialization tables do not refer to their implicit parent
				return new TreeMap();
			}
			else {
				Element parent = (Element) childElement.getParent();
				Map parentKeyNamesToValues = dbManager.getPrimaryKeyNamesAndValues(parent.getName(), parentId);
				return parentKeyNamesToValues;
			}
		}
	}

	/**
	 * This element doesn't yet exist in the database so we have to get it from the
	 * element attributes OR the sequence in the database 
	 * @param element
	 * @return
	 */
	private Map getKeyAttributes(Element element) {

		Map keyAttributeNamesToValues = new TreeMap();
		List keyAttributeNames = dbManager.getPrimaryKeyAttributesNames(element.getName());

		if (keyAttributeNames.size() == 1) {
			String singletonKeyName = (String) keyAttributeNames.get(0);
			if (dbManager.isAutoKey(singletonKeyName)) {
				// it uses an auto key
				int autoKey = dbManager.getNextId(element.getName());
				keyAttributeNamesToValues.put(singletonKeyName, new Integer(autoKey));
				return keyAttributeNamesToValues;
			}
		}

		for (Iterator iter = keyAttributeNames.iterator(); iter.hasNext();) {
			String keyAttributeName = (String) iter.next();
			String keyAttributeValue = element.getAttributeValue(keyAttributeName);
			keyAttributeNamesToValues.put(keyAttributeName, keyAttributeValue);
		}
		return keyAttributeNamesToValues;

	}
	public int getGlobalId() {
		return dbManager.getNextGlobalId();
	}

}
