/*
 * Created on 20-May-2004
 *
 */
package xtractor.schemaConverter.rdb.parsers;

import java.util.Collection;
import java.util.Iterator;

import xtractor.schemaConverter.rdb.RDBBuilder;
import xtractor.schemaConverter.xer.xerConstructs.XERAttribute;
import xtractor.schemaConverter.xer.xerConstructs.XERConstruct;
import xtractor.schemaConverter.xer.xerConstructs.XEREntity;
import xtractor.schemaConverter.xer.xerConstructs.XERGeneralization;
import xtractor.schemaConverter.xer.xerConstructs.XERPrimaryKey;

/**
 * @author ss401
 *
 */
public class XERGeneralizationParser extends XERAbstractParser {

	/**
	 * @param builder
	 */
	public XERGeneralizationParser(RDBBuilder builder) {
		super(builder);
	}

	/**
	 * @param generalization
	 */
	public String parseGeneralization(XERGeneralization generalization) {

		// TODO: The null checker sql doesn't work for more than 2 specializations
		//it would be a very complicated expression trying to say that 2 out of 3 should be null

		//String systemElementLocationEntry ="";
		String generalizationSystemInfo = "";
		String nullCheckerSQL = DELIMITER + "CHECK (";
		//String tableContainmentInfo = "";
		Collection specializations = generalization.getSpecializations();
		String sql = getComment(generalization.getName());
		sql += "CREATE TABLE " + databaseSchema + "." + generalization.getName() + " (\n";
		for (Iterator iter = specializations.iterator(); iter.hasNext();) {
			XERConstruct specialization = (XERConstruct) iter.next();

			//tableContainmentInfo  += systemTableHandler.addGeneralization(generalization.getName(),specialization.getName());

			if (specialization instanceof XEREntity) {
				XEREntity entity = (XEREntity) specialization;

				String keyAttributeNames = "";
				nullCheckerSQL += "(";
				Collection keyAttributes = entity.getPrimaryKeys();
				for (Iterator iterator = keyAttributes.iterator(); iterator.hasNext();) {
					XERPrimaryKey key = (XERPrimaryKey) iterator.next();
					String keyName = key.getName();
					sql += DELIMITER + keyName + DELIMITER + resolveXSDType(key.getType());

					keyAttributeNames += keyName;
					sql += ",\n";

					nullCheckerSQL += keyName + " IS NULL";
					if (iterator.hasNext()) {
						nullCheckerSQL += " AND ";
						keyAttributeNames += ",";
					}
				}
				nullCheckerSQL += ")";
				sql += DELIMITER
					+ "FOREIGN KEY ("
					+ keyAttributeNames
					+ ") REFERENCES "
					+ getDataTableName(specialization)
					+ "("
					+ keyAttributeNames
					+ ")";
					
					

			}
			else if (specialization instanceof XERAttribute) {
				XERAttribute attribute = (XERAttribute) specialization;
				sql += DELIMITER + attribute.getName() + DELIMITER + resolveXSDType(attribute.getType());
				nullCheckerSQL += "(" + attribute.getName() + " IS NULL)";
				//systemElementLocationEntry += systemTableHandler.addElementLocation(attribute.getName(),generalization.getName(),attribute.getName());

			}
			else {
				logger.error("Invalid case.");
			}

			generalizationSystemInfo += systemTableHandler.insertGeneralizationInformation(generalization.getName(),specialization.getName());
			if (iter.hasNext()) {
				sql += ",\n";
				nullCheckerSQL += " OR ";
			}
		}
		nullCheckerSQL += ")";
		if (specializations.size() > 1) {
			sql += ",\n" + nullCheckerSQL;
		}
		sql += "\n);";

		//return sql + systemElementLocationEntry + tableContainmentInfo;
		return sql + generalizationSystemInfo;
	}

}
