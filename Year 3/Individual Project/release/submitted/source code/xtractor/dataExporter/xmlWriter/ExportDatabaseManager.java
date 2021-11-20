/*
 * Created on 13-May-2004
 */
package xtractor.dataExporter.xmlWriter;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import databaseConnection.Nomenclature;
import databaseConnection.databaseManager.PostDatabaseManager;

/**
 * @author ss401
 */
public class ExportDatabaseManager extends PostDatabaseManager {

	public ExportDatabaseManager(String databaseSchemaName) {
		super(databaseSchemaName);
	}

	/**
	 * @param id - The globally unique id of an element
	 * @return - The Collection (of Strings) of names of all the elements (aliases?) which 
	 * can (in theory) appear as a child of the given element 
	 */
	private Collection getChildElementNames(int id) {

		Set s = new HashSet();

		String originalParentElementName = getElementName(id);
		String parentAlias = getElementAlias(originalParentElementName);

		// Note that 'parent_element' in the element catalog is the element alias, but 'element' is 
		// the original element name
		String sql =
			"SELECT * FROM "
				+ getSystemTableName(Nomenclature.ELEMENTS)
				+ " WHERE "
				+ Nomenclature.ELEMENTS_parent_name
				+ "='"
				+ parentAlias
				+ "'";
		ResultSet rs = executeQuery(sql);
		try {
			while (rs.next()) {
				String childElementOriginalName = rs.getString(Nomenclature.ELEMENTS_name);
				String childElementAliasName = getElementAlias(childElementOriginalName);
				s.add(childElementAliasName);
			}
		}
		catch (SQLException e) {
			//logger.error("Couldn't determine what meta tables contain pid references to the given id. "+ e);
			logger.error("Couldn't determine children of " + parentAlias);
		}

		return s;

		//		Set s = new HashSet();
		//		String originalElementName = getElementName(id);
		//		String unqualifiedMetaTableName =
		//			getUnqualifiedTableName(getMetaTableName(originalElementName))
		//				.toLowerCase();
		//
		//		try {
		//			String metaPrefix = (Nomenclature.META_PREFIX).toLowerCase();
		//			DatabaseMetaData metaData = conn.getMetaData();
		//			ResultSet rs = metaData.getImportedKeys(null, schemaName, null);
		//			while (rs.next()) {
		//				String parentTable = rs.getString("PKTABLE_NAME");
		//				String childTable = rs.getString("FKTABLE_NAME");
		//
		//				if (parentTable.equals(unqualifiedMetaTableName)) {
		//					if (childTable.startsWith(metaPrefix)) {
		//						//System.out.println(childTable + "-->" + parentTable);
		//						s.add(Nomenclature.removeMetaDecorator(childTable));
		//					}
		//				}
		//
		//			}
		//		} catch (SQLException e) {
		//			logger.error(
		//				"Couldn't determine what meta tables contain pid references to the given id. "
		//					+ e);
		//		}
		//
		//		return s;

	}

	/**
	 * @param parentId - the id of the parent element
	 * @return - a map of all child elements' order to their unique id
	 */
	public TreeMap getOrdsToIds(int parentId) {
		TreeMap ordsToIds = new TreeMap();

		// The first thing we need to do is determine what tables to look in
		Collection childElementNames = getChildElementNames(parentId);
		for (Iterator iter = childElementNames.iterator(); iter.hasNext();) {
			String childElementName = (String) iter.next();

			if (isSimpleElement(getOriginalElementName(childElementName))) {
				continue;
			}

			String metaTableName = getMetaTableName(childElementName);

			String sql = "SELECT id,ord FROM " + metaTableName + " WHERE pid=" + parentId;
			ResultSet rs = executeQuery(sql);

			try {
				while (rs.next()) {
					int ord = rs.getInt("ord");
					int id = rs.getInt("id");
					ordsToIds.put(new Integer(ord), new Integer(id));
				}
			}
			catch (SQLException e) {
				logger.error("Couldn't create ord to id map." + e);
			}
		}

		return ordsToIds;
	}

	public Map getIdsToElementNames(int parentId) {
		TreeMap idsToElementNames = new TreeMap();

		// The first thing we need to do is determine what tables to look in
		Collection childElementNames = getChildElementNames(parentId);
		for (Iterator iter = childElementNames.iterator(); iter.hasNext();) {
			String childElementName = (String) iter.next();

			if (isSimpleElement(getOriginalElementName(childElementName))) {
				continue;
			}

			String metaTableName = getMetaTableName(childElementName);

			String sql = "SELECT id,ord FROM " + metaTableName + " WHERE pid=" + parentId;
			ResultSet rs = executeQuery(sql);
			try {
				while (rs.next()) {
					int id = rs.getInt("id");
					idsToElementNames.put(new Integer(id), childElementName);
				}
			}
			catch (SQLException e) {
				logger.error("Couldn't create ord to id map." + e);
			}
		}

		return idsToElementNames;
	}

	/**
	 * 
	 * @param parentId
	 * @return - A map from Integer (id) to a Map (of attribute name,attribute value pairs)
	 */
	public Map getIdsToAttributeMap(int parentId) {
		Map idsToAttributeMap = new TreeMap();

		// The first thing we need to do is determine what tables to look in
		Collection childElementNames = getChildElementNames(parentId);
		for (Iterator iter = childElementNames.iterator(); iter.hasNext();) {
			String childElementName = (String) iter.next();

			if (isSimpleElement(getOriginalElementName(childElementName))) {
				continue;
			}

			// Get the result set for this particular element type
			String sql = "SELECT ";
			Collection dataColumnNames = getDataColumnNames(childElementName);
			/*for (Iterator iterator = dataColumnNames.iterator(); iterator.hasNext();) {
				String dataColumnName = (String) iterator.next();
				sql += dataColumnName;
				if (iter.hasNext()) {
					sql += ",";
				}
			}*/
			sql += "* FROM " + getCompleteTableName(childElementName) + " WHERE pid=" + parentId;

			// Get the attribute names and values
			ResultSet rs = executeQuery(sql);
			try {
				while (rs.next()) {
					Map attributeMap = new HashMap();
					int childId = rs.getInt("id");
					for (Iterator iterator = dataColumnNames.iterator(); iterator.hasNext();) {
						String dataColumnName = (String) iterator.next();
						Object dataColumnValue = rs.getObject(dataColumnName);
						attributeMap.put(dataColumnName, dataColumnValue);
					}
					idsToAttributeMap.put(new Integer(childId), attributeMap);
				}
			}
			catch (SQLException e) {
				logger.error("Couldn't get attributes for id." + e);
			}
		}

		return idsToAttributeMap;
	}

	/**
	 * @param elementName
	 * @return
	 */
	private Collection getDataColumnNames(String elementName) {
		Collection columnNames = new LinkedList();
		String sql =
			"SELECT * FROM "
				+ getSystemTableName(Nomenclature.ATTRIBUTES)
				+ " WHERE "
				+ Nomenclature.ATTRIBUTES_element
				+ "='"
				+ elementName
				+ "'";
		ResultSet rs = executeQuery(sql);
		try {
			while (rs.next()) {
				String columnName;
				String attributeAlias = rs.getString(Nomenclature.ATTRIBUTES_alias);
				if (attributeAlias == null) {
					columnName = rs.getString(Nomenclature.ATTRIBUTES_name);
				}
				else {
					columnName = attributeAlias;
				}

				columnNames.add(columnName);
			}
		}
		catch (SQLException e) {
			logger.error("Couldn't determine data columns." + e);
		}
		return columnNames;
	}

	public String getCharacterData(int id) {

		String originalElementName = getElementName(id);
		if (isSimpleElement(originalElementName)) {
			String sql = "SELECT * FROM " + getCompleteTableName(originalElementName) + " WHERE id=" + id;
			Object cData = getOnlyResult(sql, originalElementName);

			return Nomenclature.convertAttributeValueToString(cData);
		}
		else if (isMixedElement(originalElementName)) {
			String sql = "SELECT * FROM " + getCompleteTableName(originalElementName) + " WHERE id=" + id;
			Object cData = getOnlyResult(sql, originalElementName);
			return Nomenclature.convertAttributeValueToString(cData);
		}
		else {
			logger.error("Unexpected case.");
			return null;
		}

	}

	/**
	 * @return
	 */
	public String[][] getDocuments() {
		// collection of type string[]
		Collection entries = new LinkedList();

		String rootTableName = getMetaTableName(getRootElementName());
		String sql = "SELECT * FROM " + rootTableName;
		ResultSet rs = executeQuery(sql);
		int width = 1;
		try {
			while (rs.next()) {
				String[] entry = { new Integer(rs.getInt("id")).toString()};
				width = entry.length;
				entries.add(entry);
			}
			String[][] entriesArr = new String[entries.size()][width];
			int i = 0;
			for (Iterator iter = entries.iterator(); iter.hasNext();) {
				String[] entry = (String[]) iter.next();
				entriesArr[i] = entry;
				i++;
			}
			return entriesArr;
		}
		catch (SQLException e) {
			return new String[0][0];
		}
	}
	/**
	 * @param id - The globally unique element schema id for an element
	 * @return - The element's original name for 'id'
	 */
	public String getElementName(int id) {
		String sql = "SELECT table_name FROM " + getSystemTableName(Nomenclature.IDS_TO_TABLES) + " WHERE id = " + id;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String elementAliasName = rs.getString("table_name");
				return (String) aliasToElementName.get(elementAliasName);
			}
			logger.error("No table contains a row with id " + id);
		}
		catch (SQLException e) {
			logger.error("Couldn't determine which table id " + id + " belongs to." + e);
			logger.error("Statement was: " + sql);
		}
		return null;
	}

	/**
	 * @param elementAlias - The name of an XML element, but in database nomenclature
	 * @return - The XML element name in XML nomenclature
	 */
	public String getOriginalElementName(String elementAlias) {
		String originalName = (String) aliasToElementName.get(elementAlias);
		if (originalName == null) {
			return elementAlias;

		}
		else {
			return originalName;
		}
	}
	public String getOriginalAttributeName(String attributeAlias) {

		String originalName = (String) aliasToAttributeName.get(attributeAlias);
		if (originalName == null) {
			return attributeAlias;

		}
		else {
			return originalName;
		}
	}

}
