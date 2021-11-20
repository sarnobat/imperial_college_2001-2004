/*
 * Created on 28-May-2004
 *
 */
package databaseConnection.databaseManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import databaseConnection.Nomenclature;

/**
 * @author ss401
 * This manager assumes that the schema has already been created and thus
 * we can use the schema to deduce information about the XML files
 */
public class PostDatabaseManager extends AbstractDatabaseManager {

	// A map from an XML element's original name to its alias (not just those changed, but all of them)
	protected Map elementNameToAlias;
	// A map from an XML attributes's original name to its alias (not just those changed, but all of them)
	protected Map attributeNameToAlias;

	// ...and vice versa
	protected Map aliasToElementName;
	protected Map aliasToAttributeName;

	

	/**
	 * @param databaseSchemaName
	 */
	public PostDatabaseManager(String databaseSchemaName) {
		super(databaseSchemaName);
		this.elementNameToAlias = new HashMap();
		this.attributeNameToAlias = new HashMap();
		this.aliasToAttributeName = new HashMap();
		this.aliasToElementName = new HashMap();
		populateElementNames();
		populateAttributeNames();

	}


	/**
	 * 
	 */
	private void populateAttributeNames() {
		String sql = "SELECT * FROM " + getSystemTableName(Nomenclature.ATTRIBUTES);
		ResultSet rs = executeQuery(sql);
		try {
			while (rs.next()) {
				String originalName = rs.getString(Nomenclature.ATTRIBUTES_name);
				String databaseEquivalent = rs.getString(Nomenclature.ATTRIBUTES_alias);
				attributeNameToAlias.put(originalName, databaseEquivalent);
				aliasToAttributeName.put(databaseEquivalent, originalName);
			}
		}
		catch (SQLException e) {
			logger.error("Couldn't get aliases for XML attributes. " + e);
		}
	}

	/**
	 * 
	 */
	private void populateElementNames() {
		String sql = "SELECT * FROM " + getSystemTableName(Nomenclature.ELEMENTS);
		ResultSet rs = executeQuery(sql);
		try {
			while (rs.next()) {
				String originalName = rs.getString(Nomenclature.ELEMENTS_name);
				String databaseEquivalent = rs.getString(Nomenclature.ELEMENTS_alias);
				if (databaseEquivalent == null) {
					databaseEquivalent = originalName;
				}
				elementNameToAlias.put(originalName, databaseEquivalent);
				aliasToElementName.put(databaseEquivalent, originalName);
			}
		}
		catch (SQLException e) {
			logger.error("Couldn't get aliases for XML elements. " + e);
		}
	}

	/**
	 * @param elementName - The name of an XML element (e.g. name)
	 * @return - True if its table uses a sequence to determine the primary key value
	 * that should be inserted.
	 */
	public boolean dataTableUsesAutoKey(String originalElementName) {
		String elementName = getElementAlias(originalElementName);
		try {
			String[] types = { "SEQUENCE" };
			ResultSet rs = metaData.getTables("pg_class", schemaName, elementName + "%", types);
			if (rs.next()) {
				return true;
			}
			else {
				return false;
			}

		}
		catch (SQLException e) {
			logger.error("Couldn't determine whether " + elementName + " requires an auto key. " + e);
			return false;
		}

	}

	/**
	 * @param keyName - The name of a primary key column in a table
	 * @return - True if it relies on a sequence to determine its value (and not an XML attribute value)
	 * (it's an 'implicit key'0
	 */
	public boolean isAutoKey(String keyName) {
		String[] types = { "SEQUENCE" };
		try {
			ResultSet rs = metaData.getTables("pg_class", schemaName, keyName + "%", types);
			if (rs.next()) {
				return true;
			}
		}
		catch (SQLException e) {
			logger.error("Couldn't determine if key relies on a sequence. " + e);
		}
		return false;
	}

	/**
	 * @param elementName - The name of a SIMPLE XML element which we are trying to determine
	 * can occur more than once.
	 * @return - True if this element's character data is represented in a multi-value foreign table
	 * (e.g. true for 'supplies')
	 */
	public boolean isMultiValued(String originalElementName) {
		// At the moment, we'll check by seeing if there is a table with the name 'elementName'
		// AND an attribute with the name 'elementName'

		String elementName = getElementAlias(originalElementName);
		try {
			ResultSet rs = metaData.getColumns(null, schemaName, elementName, elementName);
			if (rs.next()) {
				return true;
			}

		}
		catch (SQLException e) {
			logger.error("Couldn't deterine whether " + elementName + " is a multi-occurring element. " + e);
		}
		return false;
	}

	/**
	 * Note these are the GENUINE key fields and not our internal ones. They will be the fields that
	 * will be in both the meta table AND the data table
	 * @param element - The name of the XML element whose key attributes you wish to determine
	 * @return - A list of Strings for each column name
	 */
	public List getPrimaryKeyAttributesNames(String originalElementName) {
		String elementName = getElementAlias(originalElementName);
		List keyColumnNames = new LinkedList();

		try {
			ResultSet rs = metaData.getPrimaryKeys("pg_class", schemaName, elementName);
			while (rs.next()) {
				String keyColumnName = rs.getString("column_name");
				keyColumnNames.add(keyColumnName);
			}
			return keyColumnNames;
		}
		catch (SQLException e) {
			logger.error("Couldn't determine key fields." + e);
			return null;
		}

	}
	/**
	 * @param element
	 * @return
	 */
	public boolean isSimpleElement(String originalElementName) {
		String elementName = getElementAlias(originalElementName);
		String sql =
			"SELECT * FROM "
				+ getSystemTableName(Nomenclature.SIMPLE_ELEMENTS)
				+ " WHERE "
				+ Nomenclature.SIMPLE_ELEMENTS_element
				+ "='"
				+ elementName
				+ "';";
		ResultSet rs = executeQuery(sql);
		try {
			if (rs.next()) {
				return true;
			}
			else {
				return false;
			}
		}
		catch (SQLException e) {
			logger.error("Couldn't determine whether " + elementName + " has character data. " + e);
			return false;
		}
	}

	//	private void getColumnTypesInformation() {

	//The following isn't ideal because we cannot be sure that a thrown exception was due to a
	// table not exisiting; but we have no choice

	//		for (Iterator iter = tableNameToColumnNames.keySet().iterator(); iter.hasNext();) {
	//			String unqualifiedTableName = (String) iter.next();
	//			DatabaseMetaData metaData = conn.getMetaData();
	//			ResultSet rs = metaData.getcol 
	//
	//		}

	//		String sql = "SELECT * FROM " + tableName;
	//		PreparedStatement ps = conn.prepareStatement(sql);
	//
	//		ResultSet rs = ps.executeQuery();
	//		ResultSetMetaData metaData = rs.getMetaData();
	//		Map columnNamesToTypes = new TreeMap();
	//		for (int i = 1; i <= metaData.getColumnCount(); i++) {
	//			columnNamesToTypes.put(metaData.getColumnName(i), new Integer(metaData.getColumnType(i)));
	//		}

	//}

	/**
		* @param element
		* @return
		*/
	public boolean isMixedElement(String originalElementName) {
		String elementName = getElementAlias(originalElementName);
		String sql =
			"SELECT * FROM "
				+ getSystemTableName(Nomenclature.MIXED_ELEMENTS)
				+ " WHERE "
				+ Nomenclature.MIXED_ELEMENTS_element
				+ "='"
				+ elementName
				+ "';";
		ResultSet rs = executeQuery(sql);
		try {
			if (rs.next()) {
				return true;
			}
			else {
				return false;
			}
		}
		catch (SQLException e) {
			logger.error("Couldn't determine whether " + elementName + " has mixed content. " + e);
			return false;
		}
	}
	/**
		* The attribute names must be from the data table.
		* @param elementName - The name of an XML element (e.g. part)
		* @param attributeNames - The names of the attributes whose values you want returned
		* @return - A map from attribute name to attribute value
		*/
	public Map getAttributeValues(String originalElementName, List attributeNames) {
		String elementName = getElementAlias(originalElementName);
		Map attributeNamesToValues = new HashMap();
		String tableName = getDataTableName(originalElementName);
		String sql = "SELECT * FROM " + tableName;
		ResultSet rs = executeQuery(sql);

		try {
			rs.next();
			ResultSetMetaData rsMeta = rs.getMetaData();
			for (Iterator iter = attributeNames.iterator(); iter.hasNext();) {
				String attributeName = (String) iter.next();
				Object attributeValue = rs.getObject(attributeName);
				attributeNamesToValues.put(attributeName, attributeValue);
			}
		}
		catch (SQLException e) {
			logger.error("Couldn't get attribure values for element " + elementName + ". " + e);
			logger.error("Statement was: " + sql);
		}
		return attributeNamesToValues;
	}

	/**
			* Determines the key values given an element and its element schema global id
			* (by looking in the database)
			* @param id
			* @return - The names will be have database nomenclature, not XML nomenclature
			* (i.e. aliases are used)
			*/
	public Map getPrimaryKeyNamesAndValues(String originalElementName, int id) {
		String elementName = getElementAlias(originalElementName);
		Map keyNamesToValues = new TreeMap();

		String sql = "SELECT ";
		List primaryKeyNames = getPrimaryKeyAttributesNames(originalElementName);
		for (Iterator iter = primaryKeyNames.iterator(); iter.hasNext();) {
			String keyAttributeName = (String) iter.next();
			sql += keyAttributeName;
			if (iter.hasNext()) {
				sql += ",";
			}
		}
		sql += " FROM " + getCompleteTableName(originalElementName) + " WHERE id=" + id;

		ResultSet rs = executeQuery(sql);
		try {
			rs.next();
			ResultSetMetaData rsMeta = rs.getMetaData();
			int columnCount = rsMeta.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				keyNamesToValues.put(rsMeta.getColumnName(i), rs.getObject(i));
			}
		}
		catch (SQLException e) {
			logger.error("Couldn't determine key values for " + elementName + ". " + e);
			logger.error("Statement was:\n\t" + sql);
		}

		return keyNamesToValues;
	}
	/**
		* @param elementName
		* @return
		*/
	public boolean isSpecialization(String originalElementName) {
		String elementName = getElementAlias(originalElementName);
		String sql =
			"SELECT * FROM "
				+ getSystemTableName(Nomenclature.GENERALIZATIONS)
				+ " WHERE "
				+ Nomenclature.GENERALIZATIONS_specialization_table
				+ "='"
				+ elementName
				+ "'";
		ResultSet rs = executeQuery(sql);
		try {
			if (rs.next()) {
				return true;
			}
			else {

				return false;
			}
		}
		catch (SQLException e) {
			logger.error("Couldn't determine if " + elementName + " was part of a generalization hierarchy. " + e);
			return false;
		}
	}

	/**
	 * @return - The name of the root xml element
	 */
	protected String getRootElementName() {
		String sql =
			"SELECT * FROM "
				+ getSystemTableName(Nomenclature.ELEMENTS)
				+ " WHERE "
				+ Nomenclature.ELEMENTS_parent_name
				+ " IS null";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getString(Nomenclature.ELEMENTS_name);
			}
			logger.error("No root table was specified.");
		}
		catch (SQLException e) {
			logger.debug("Attempting to find root element name: " + sql);
			logger.error("Couldn't determine root element." + e);
		}
		return null;
	}

	protected String getElementAlias(String xmlElementName) {
		String alias = (String) elementNameToAlias.get(xmlElementName);
		if (alias == null) {
			return xmlElementName;
		}
		else {
			return alias;
		}
	}

	/**
	 * @param xmlAttributeName - can be an XML attribute or simple element
	 */
	protected String getAttributeAlias(String xmlAttributeName) {
		String alias = (String) attributeNameToAlias.get(xmlAttributeName);
		if (alias == null) {
			// Need to check the element aliases in some cases
			alias = getElementAlias(xmlAttributeName);
			if (alias == null) {
				return xmlAttributeName;
			}
			else {
				return alias;
			}

		}
		else {
			return alias;
		}
	}

	protected String getOriginalElementName(String elementAlias) {
		String originalName = (String) aliasToElementName.get(elementAlias);
		if (originalName == null) {
			return elementAlias;
		}
		else {
			return originalName;
		}

	}

	/**
	 * NOTE: the column may have originated from an ELEMENT
	 * @param columnName - The name of a column which must be turned into XML nomenclature
	 * @return
	 */
	protected String getOriginalAttributeName(String columnName) {
		String originalName = (String) aliasToAttributeName.get(columnName);

		if (originalName == null) {
			// see if it corresponds to an element
			originalName = getOriginalElementName(columnName);
			if (originalName == null) {
				return columnName;
			}
			else {
				return originalName;

			}
		}
		else {
			return originalName;
		}
	}

	/**
	 * @param attributeNamesToValues - XML attribute names to XML Attribute values
	 * @return - The same map, but with table COLUMN names (i.e. attribute aliases) to values
	 * (values unaffected obviously)
	 */
	protected Map getAttributeAliases(Map attributeNamesToValues) {
		Map columnNamesToValues = new HashMap();
		for (Iterator iter = attributeNamesToValues.keySet().iterator(); iter.hasNext();) {
			String originalAttributeName = (String) iter.next();
			String attributeAlias = getAttributeAlias(originalAttributeName);
			columnNamesToValues.put(attributeAlias, attributeNamesToValues.get(originalAttributeName));
		}
		return columnNamesToValues;
	}
}
