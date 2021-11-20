/*
 * Created on 25-May-2004
 *
 */
package xtractor.dataImporter.xmlReader;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jdom.Attribute;
import org.jdom.Element;

import xtractor.dataImporter.xmlReader.databaseManager.DatabaseManager;

/**
 * @author ss401
 *
 */
public class DataPopulator {

	//private String databaseSchemaName;
	private DatabaseManager dbManager;

	public DataPopulator(String databaseSchemaName) {
		//this.databaseSchemaName = databaseSchemaName;
		this.dbManager = new DatabaseManager(databaseSchemaName);
	}

	/**
	 * @param root
	 * @return - The id of the root to be used in child table references
	 */
	public int populateDatabaseWithRoot(Element root, int rootId) {

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
			String primaryKeyName = dbManager.getPrimaryKeyName(elementName);

			// The key isn't explicitly stated in the XML, so we need the sequence value
			int primaryKeyValue = dbManager.getNextId(elementName);

			// add the primary key in the data table
			dataColumnsAndValues.put(primaryKeyName, new Integer(primaryKeyValue));
			// add the foreign key in the meta table
			metaColumnsAndValues.put(primaryKeyName, new Integer(primaryKeyValue));
		}

		// Perform the data additions.
		dbManager.addData(elementName, dataColumnsAndValues);
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
	public int populateDatabaseWithElement(Element element, int elementId, int parentId, int order) {
		//PICKUP: you'll need to reconsider how to tackle this
		Element parentElement = element.getParent();

		/*
		 * Get the key attributes, so that they can be put in both the
		 * data table and the meta table
		 */
		Map keyAttributeNamesToValues = new TreeMap();
		List keyAttributes = dbManager.getPrimaryKeyAttributesNames(element.getName());
		for (Iterator iterator = keyAttributes.iterator(); iterator.hasNext();) {
			String keyName = (String) iterator.next();
			if (dbManager.isAutoKey(keyName)) {
				// get the next id from the sequence
				int id = dbManager.getNextId(element.getName());
				keyAttributeNamesToValues.put(keyName, new Integer(id));
			}
			else {
				// get the key value from the XML element
				keyAttributeNamesToValues.put(keyName, element.getAttributeValue(keyName));
			}
		}

		/*
		 * Perform the data insert 
		 */

		// Add primary keys
		Map dataColumnNamesAndValues = new TreeMap(keyAttributeNamesToValues);

		// Add foreign keys for implicit containment
		if (dbManager.isSpecialization(element.getName())) {
			// do nothing; we don't add any backward pointers in the specialized element's data table
			// TODO: But we should add something in the choice table
		}
		else {
			// The expected parent's table
			List parentKeyNames = dbManager.getPrimaryKeyAttributesNames(parentElement.getName());
			Map parentKeyNamesToValues = dbManager.getAttributeValues(parentElement.getName(), parentKeyNames);
			dataColumnNamesAndValues.putAll(parentKeyNamesToValues);
		}

		// Add remaining attributes
		List attributes = element.getAttributes();
		for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
			Attribute attribute = (Attribute) iterator.next();
			dataColumnNamesAndValues.put(attribute.getName(), attribute.getValue());
		}

		// Add character data if necessary
		//TODO: This doesn't handle all character data
		if (dbManager.isMixedElement(element) || dbManager.isSimpleElement(element)) {
			// assume it is a simple element
			dataColumnNamesAndValues.put(element.getName(), element.getText());
			if (keyAttributeNamesToValues.containsKey(element.getName())) {
				keyAttributeNamesToValues.put(element.getName(), element.getText());
			}
		}

		dbManager.addData(element.getName(), dataColumnNamesAndValues);

		/*
		 * Perform the meta data insert 
		 */
		Map metaColumnNamesAndValues = new TreeMap(keyAttributeNamesToValues);
		//int elementId = dbManager.getNextGlobalId();
		metaColumnNamesAndValues.put("id", new Integer(elementId));
		metaColumnNamesAndValues.put("pid", new Integer(parentId));
		metaColumnNamesAndValues.put("ord", new Integer(order));

		dbManager.addMetaData(element.getName(), metaColumnNamesAndValues);

		return elementId;

		//TODO: The big question is what to do after taking care of the parent's attribute but
		// before moving on to the childs - because the parent may have character data
		//ANSWER: put them as a column named the same thing as the data table itself

	}

	//	/**
	//	 * @param element - The jdom model of the element (i.e. <element>...</element>)
	//	 * @param attributes - A map from the name of the attribute to the value of the attribute (both are Strings)
	//	 */
	//	private void translateElementXXX(Element element, Map attributes) {
	//
	//		/* Iterate through child elements and call translateElement again
	//		 * This must be done before translating the parent because the parent
	//		 * will be referencing it, and SQL will refuse to accept references to 
	//		* rows not populated yet. In other words, this is a post-order traversal
	//		*/
	//		Collection childElements = element.getChildren();
	//		for (Iterator iter = childElements.iterator(); iter.hasNext();) {
	//			Element child = (Element) iter.next();
	//			translateElement(child);
	//		}
	//
	//		if (attributes.size() > 0) {
	//
	//			String elementName = element.getName();
	//			Map attributeNamesToValues = new TreeMap();
	//
	//			for (Iterator iter = attributes.keySet().iterator(); iter.hasNext();) {
	//
	//				String attributeName = (String) iter.next();
	//				String attributeValue = (String) attributes.get(attributeName);
	//
	//				attributeNamesToValues.put(attributeName, attributeValue);
	//			}
	//
	//			dbManager.addData(elementName, attributeNamesToValues);
	//		}
	//
	//		//TODO:Don't forget to deal with mixed content - perhaps this will be taken care of elsewhere
	//	}
	//	/**
	//	 * Performs the database updates specified by the root node of an xml file, and recursively does so for it's child elements
	//	 * @param element - The jdom element of an xml files
	//	 */
	//	private void translateElementXXX(Element element) {
	//		Map attributesToValues = getAttributeNamesAndValues(element);
	//		translateElement(element, attributesToValues);
	//	}
	/**
	 * @param element - A jdom model of an xml element
	 * @return - A map (from String to String) of attribute name to attribute value
	 */
	private Map getAttributeNamesAndValues(Element element) {
		Map namesToValues = new TreeMap();

		//populate the map with the name-value pairs
		for (Iterator iter = element.getAttributes().iterator(); iter.hasNext();) {
			Attribute attribute = (Attribute) iter.next();
			namesToValues.put(attribute.getName(), attribute.getValue());
		}
		return namesToValues;
	}

	/**
	 * @return - The element schema's next available globally unique id 
	 */
	public int getGlobalId() {
		return dbManager.getNextGlobalId();
	}
}
