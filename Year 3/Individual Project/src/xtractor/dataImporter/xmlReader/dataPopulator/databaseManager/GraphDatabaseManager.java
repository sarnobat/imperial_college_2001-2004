/*
 * Created on 26-May-2004
 *
 */
package xtractor.dataImporter.xmlReader.dataPopulator.databaseManager;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import databaseConnection.Nomenclature;
import databaseConnection.databaseManager.PostDatabaseManager;

/**
 * @author ss401
 *
 */
public class GraphDatabaseManager extends PostDatabaseManager {

	/**
	 * @param databaseSchemaName
	 */
	public GraphDatabaseManager(String databaseSchemaName) {
		super(databaseSchemaName);
	}

	/**
	 * @return - A map of String to Collection of String, with each member being of the form
	 * (e.g.	ps_db_choice --> [ps_db,part,supplier]) 
	 */
	public Map getEdges()  {

		Map edges = new HashMap();

		try {
			DatabaseMetaData metaData = conn.getMetaData();
			// The following works even though the statement is null
			ResultSet rs2 = metaData.getImportedKeys(null, schemaName, null);
			while (rs2.next()) {
				String referringTable = rs2.getString("FKTABLE_NAME");
				String foreignTable = rs2.getString("PKTABLE_NAME");
			
				String referringElement = referringTable;
				String foreignElement = foreignTable;
			
				// Remove any meta prefixes
				if (Nomenclature.isMetaTable(referringTable)) {
					referringElement = Nomenclature.removeMetaDecorator(referringTable);
				}
				if (Nomenclature.isMetaTable(foreignTable)) {
					foreignElement = Nomenclature.removeMetaDecorator(foreignTable);
				}
				if (Nomenclature.isSystemTable(referringTable)) {
					// do not add it
					continue;
				}
				
				// Get original XML element name for the tables
				referringElement = getOriginalElementName(referringElement);
				foreignElement = getOriginalElementName(foreignElement);
			
				if (!referringElement.equals(foreignElement)) {
			
					if (edges.keySet().contains(referringElement)) {
						((Set) edges.get(referringElement)).add(foreignElement);
					}
					else {
						Set s = new HashSet();
						s.add(foreignElement);
						edges.put(referringElement, s);
					}
				}
			}
		}
		catch (SQLException e) {
			logger.error("Couldn't build edge map. " + e);
		}
		return edges;
	}


}
