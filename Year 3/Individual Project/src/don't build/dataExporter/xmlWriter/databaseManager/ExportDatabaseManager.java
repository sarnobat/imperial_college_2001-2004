/*
 * Created on 13-May-2004
 */
package xtractor.dataExporter.xmlWriter.databaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import databaseConnection.AbstractDatabaseManager;

/**
 * @author ss401
 */
public class ExportDatabaseManager extends AbstractDatabaseManager {

	//Logger logger = Logger.getLogger(this.getClass());
	//Connection conn;
	//String schemaName;

	public ExportDatabaseManager(String databaseSchemaName) {
		super(databaseSchemaName);
		//this.conn = DatabaseConnection.getConnection();
		//this.schemaName = databaseSchemaName;
	}

	/**
	 * @param id - The globally unique id of an element
	 * @return - The Collection (of Strings) of names of all the elements which 
	 * can (in theory) appear as a child of the given element 
	 */
	public Collection getChildElementNames(int id) {

		// Sub query determines what element name 'id' has
		String subquery = "SELECT table_name FROM " + IDS_TO_TABLES + " WHERE id=" + id;
		// Main query determines the children of the element
		String sql =
			"SELECT DISTINCT child_element FROM " + CONTAINS + " WHERE parent_element=(" + subquery + ")";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			Collection childElementNames = new LinkedList();
			while (rs.next()) {
				childElementNames.add(rs.getString("child_element"));
			}
			return childElementNames;
		}
		catch (SQLException e) {
			logger.error("." + e);
		}
		return null;
	}

	/**
	 * @param parentId - The globally unique id of a parent element whose children
	 * we wish to determine
	 * @return - A list of ids for elements SORTED BY ORDER (not id number) 
	 */
	/*public List getChildren(int parentId) {
		logger.warn("Unimplemented Method");
		//Collections.sort((List) ids); - THIS SHOULD BE SORTED BY ORDER, NOT ID
		return null;
	}*/

	/**
	 * @param id - The globally unique element schema id for an element
	 * @return - The element name (i.e. table name) for 'id'
	 */
	public String getElementName(int id) {
		String sql = "SELECT table_name FROM " + IDS_TO_TABLES + " WHERE id = " + id;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getString("table_name");
			}
			logger.error("No table contains a row with id " + id);
		}
		catch (SQLException e) {
			logger.error("Couldn't determine which table id " + id + " belongs to." + e);
		}
		return null;
	}

	/**
	 * This will not be an exhaustive list of children. Only the children which are of type 'childElementName'
	 * @param parentId - The id of the parent element
	 * @param childElementName - The name of the element (i.e. the table) to look for children in
	 * @return - A List of Maps, where each map contains (name,value) pairs for each column
	 */
	public List getChildrenElements(int parentId, String childElementName) {

		List attributeMaps = new LinkedList();
		String sql = "SELECT id FROM " + getCompleteTableName(childElementName) + " WHERE pid=" + parentId + " ORDER BY ord";
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int childId = rs.getInt("id");
				attributeMaps.add(getAttributeMap(childId, childElementName));
			}
		}
		catch (SQLException e) {
			logger.error("Couldn't obtain child element information." + e);
		}
		return attributeMaps;
	}

	/**
	 * 
	 * @param id - The globally unique id of the row whose data we wish to fetch
	 * @return - A Map from attribute names to attribute values for 'id'
	 */
	private Map getAttributeMap(int id, String elementName) {

		String dataTable = getDataTableName(elementName);
		String metaTable = getMetaTableName(elementName);
		Collection keyCols = getKeyColumnNames(elementName);
		//A Semi Join
		String sql =
			"SELECT " + dataTable + ".* FROM " + dataTable + "," + metaTable + " WHERE " + metaTable + ".id = " + id + " AND ";
		for (Iterator iter = keyCols.iterator(); iter.hasNext();) {
			String keyColumnName = (String) iter.next();
			sql += dataTable + "." + keyColumnName + "=" + metaTable + "." + keyColumnName;
			if (iter.hasNext()) {
				sql += " AND ";
			}
		}
		Map attributeMap = new HashMap();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsMeta = rs.getMetaData();

			List attributes = getAttributeOrdering(elementName);

			while (rs.next()) {
				for (Iterator iter = attributes.iterator(); iter.hasNext();) {
					String columnName = (String) iter.next();
					String columnValue = rs.getString(columnName);
					attributeMap.put(columnName, columnValue);
				}
			}
		}
		catch (Exception e) {
			logger.debug("Getting attribute map:\n" + sql);
			logger.error("Couldn't get attribute map. " + e);
		}

		return attributeMap;
	}

	/**
	 * The ordering will rely on the order that the attributes appear in the table.
	 * @param childElementName - The name of an xml element
	 * @return - A list (of Strings) in the order the attributes should appear
	 * inside the element tag.
	 */
	public List getAttributeOrdering(String elementName) {
		List attributes = new LinkedList();
		String sql = "SELECT * FROM " + getDataTableName(elementName);

		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			//rs.next();
			ResultSetMetaData rsMeta = rs.getMetaData();
			int columnCount = rsMeta.getColumnCount();

			for (int i = 1; i <= columnCount; i++) {
				attributes.add(rsMeta.getColumnName(i));
			}
		}
		catch (SQLException e) {
			logger.debug("Attempting to get a list of attributes for " + elementName + "\n\t" + sql);
			logger.error("Couldn't determine ordering of columns." + e);
		}
		return attributes;
	}

}
