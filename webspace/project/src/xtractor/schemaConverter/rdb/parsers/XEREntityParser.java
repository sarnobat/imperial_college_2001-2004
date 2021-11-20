/*
 * Created on 19-May-2004
 *
 */
package xtractor.schemaConverter.rdb.parsers;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import xtractor.schemaConverter.exception.EntityNotParsedException;
import xtractor.schemaConverter.exception.ForeignTableException;
import xtractor.schemaConverter.exception.MultiValuedAttributeException;
import xtractor.schemaConverter.rdb.RDBBuilder;
import xtractor.schemaConverter.xer.xerConstructs.XERAttribute;
import xtractor.schemaConverter.xer.xerConstructs.XEREntity;
import xtractor.schemaConverter.xer.xerConstructs.XERMixedEntity;
import xtractor.schemaConverter.xer.xerConstructs.XERPrimaryKey;

/**
 * @author ss401
 *
 */
public class XEREntityParser extends XERAbstractParser {

	Writer out;

	/**
	 * @param builder
	 */
	public XEREntityParser(RDBBuilder builder) {
		super(builder);
		// The writer we will write our SQL to		
		this.out = builder.getWriter();

	}

	/**
	 * @param xerEntity - The entity we wish to parse
	 */
	public void parseEntity(XEREntity xerEntity) throws IOException {

		if (parsedEntities.contains(xerEntity)) {
			return;
		}

		String sql = "";
		//sql = systemTableHandler.addElementLocation(xerEntity.getName(), xerEntity.getName());
		try {
			String additions = "";
			sql += "\n" + COMMENT + DELIMITER + xerEntity.getName() + DELIMITER + COMMENT + "\n";
			try {
				sql += createDataTable(xerEntity);

			}
			catch (ForeignTableException e1) {
				logger.debug("Adding foreign multi-value table.");
				sql += e1.getParentDataTable();
				additions = e1.getForegnDataAndMetaTables();
			}
			sql += "\n";
			sql += createMetaTable(xerEntity);
			sql += "\n";
			sql += additions;

			// DO NOT CREATE VIEWS UNTIL AFTER THE TABLES HAVE FINISHED BEING ALTERED

			parsedEntities.add(xerEntity);
			out.write(sql);
			out.flush();
		}
		catch (EntityNotParsedException e) {
			logger.debug(
				"The tables for "
					+ xerEntity.getName()
					+ " reference table(s) of an entity yet to be created. Creating child entity's tables first.");
			parseEntity(e.getMissingEntity());
			parseEntity(xerEntity);
		}

	}

	/**
	 * 
	 * @param xerEntity - The entity whose meta table we wish to create
	 * @return - A string of SQL to create a meta table
	 * @throws IOException 
	 * @throws EntityNotParsedException - Thrown when the 'pid' refers to an id 
	 * in an entity whose meta table has not yet been created 
	 */
	private String createMetaTable(XEREntity xerEntity) throws IOException, EntityNotParsedException {
		String sql = "";
		sql += "CREATE TABLE " + getMetaTableName(xerEntity) + "(\n";
		sql += DELIMITER + "id" + DELIMITER + "SERIAL" + DELIMITER + "PRIMARY KEY,\n";
		sql += DELIMITER + "pid" + DELIMITER + "INT" + DELIMITER + "NOT NULL";

		if (xerEntity.getImplicitParent() != null) {
			logger.debug("Assuming that " + xerEntity.getName() + " has no implicit parent");
			XEREntity implicitParent = xerEntity.getImplicitParent();
			if (!parsedEntities.contains(implicitParent)) {
				logger.debug("Tried to parse " + xerEntity.getName() + " before " + implicitParent.getName() + ". Postponing...");
				throw new EntityNotParsedException(implicitParent);
			}
			else {
				sql += DELIMITER + "REFERENCES " + getMetaTableName(implicitParent) + "(id)";
			}
		}
		sql += ",\n";
		sql += DELIMITER + "ord" + DELIMITER + "INT" + DELIMITER + "NOT NULL";

		Collection keyAttributes = xerEntity.getPrimaryKeys();
		if (keyAttributes.size() > 0) {
			sql += ",\n";
		}
		for (Iterator iter = keyAttributes.iterator(); iter.hasNext();) {
			XERPrimaryKey key = (XERPrimaryKey) iter.next();

			/* No need to throw exception here for referenced table not existing;
			 * by simply calling this method AFTER the createDataTable one
			 * we ensure the referential integrity */
			sql += DELIMITER
				+ key.getName()
				+ DELIMITER
				+ attributeParser.resolveXSDType(key.getType())
				+ DELIMITER
				+ "REFERENCES "
				+ databaseSchema
				+ "."
				+ xerEntity.getName()
				+ "("
				+ key.getName()
				+ ")";

		}
		sql += "\n);";
		return sql;

	}

	/**
	 * @param xerEntity
	 * @return - The SQL to create the table which the end user will see
	 * @throws IOException
	 * @throws EntityNotParsedException - Thrown if this table has a foreign key to another entity's
	 * data table which has not yet been created
	 * @throws ForeignTableException - Thrown so that we know to create the foreign table after both the
	 * data and meta table have been created (we can't add it in this method because it relies on the
	 * parent's meta table exisiting)
	 */
	private String createDataTable(XEREntity xerEntity) throws IOException, EntityNotParsedException, ForeignTableException {
		// A collection of strings (for sql insertions)
		String attributeSystemInformationSQL = "";
		String foreignTablesSQL = "";
		String dataTableName = databaseSchema + "." + xerEntity.getName();
		String sql = "CREATE TABLE " + dataTableName + "(";

		if (xerEntity instanceof XERMixedEntity) {
			XERMixedEntity mixedEntity = (XERMixedEntity)xerEntity;
			if (mixedEntity.isMixedContentEnabled()) {
				sql += "\n" + DELIMITER + xerEntity.getName() + DELIMITER + "VARCHAR(50),";
			}
		}

		// Examine each attribute
		Collection attributes = xerEntity.getAttributes();
		int attributeCount = 0;
		Collection primaryKeyNames = new LinkedList();
		for (Iterator iter = attributes.iterator(); iter.hasNext();) {
			XERAttribute attribute = (XERAttribute) iter.next();

			try {
				sql += "\n" + DELIMITER + attributeParser.parseAttribute(attribute);
				if (iter.hasNext()) {
					sql += ",";
				}
				attributeCount++;
				attributeSystemInformationSQL += systemTableHandler.insertAttribute(attribute, xerEntity.getName(), null);

			}
			catch (MultiValuedAttributeException e) {
				foreignTablesSQL += attributeParser.parseMultiValuedAttribute(attribute, xerEntity) + "\n";
			}
			if (attribute instanceof XERPrimaryKey) {
				primaryKeyNames.add(attribute.getName());
			}

		}

		if (primaryKeyNames.size() > 0) {
			sql += ",\n" + DELIMITER + "PRIMARY KEY (";
			for (Iterator iter = primaryKeyNames.iterator(); iter.hasNext();) {
				String keyName = (String) iter.next();
				sql += keyName;
				if (iter.hasNext()) {
					sql += ",";
				}
			}
			sql += ")";
		}

		sql = super.removeLastComma(sql);

		sql += "\n);" + attributeSystemInformationSQL; // + mixedEntityMetaEntry;
		if (foreignTablesSQL.length() > 0) {
			throw new ForeignTableException(sql, foreignTablesSQL);
		}
		else {
			return sql;
		}

	}

}
