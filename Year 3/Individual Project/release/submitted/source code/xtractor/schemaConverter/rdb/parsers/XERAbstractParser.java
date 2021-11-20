/*
 * Created on 20-May-2004
 *
 */
package xtractor.schemaConverter.rdb.parsers;

import java.util.Collection;

import org.apache.log4j.Logger;

import xtractor.schemaConverter.rdb.RDBBuilder;
import xtractor.schemaConverter.rdb.SQLMapper;
import xtractor.schemaConverter.xer.xerConstructs.XERAttribute;
import xtractor.schemaConverter.xer.xerConstructs.XERCompoundConstruct;
import xtractor.schemaConverter.xer.xerConstructs.XERConstruct;
import xtractor.schemaConverter.xer.xerConstructs.XEREntity;
import xtractor.schemaConverter.xer.xerConstructs.XERGeneralization;
import xtractor.schemaConverter.xer.xerConstructs.XERRelationship;

/**
 * @author ss401
 *
 */
public class XERAbstractParser {

	Logger logger = Logger.getLogger(this.getClass());

	final String COMMENT = "--------------------";
	final String META_PREFIX = "META_";
	String DELIMITER;
	SQLMapper typeMapper;
	String databaseSchema;
	//writer not included (to prevent attribute parser from using it)

	Collection parsedEntities;

	XERAttributeParser attributeParser;
	SystemTableHandler systemTableHandler;

	public XERAbstractParser(RDBBuilder builder) {

		this.DELIMITER = builder.getDelimiter();
		this.typeMapper = builder.getSqlMapper();
		this.databaseSchema = builder.getPrefix();

		this.parsedEntities = builder.getParsedEntities();
		this.attributeParser = builder.getAttributeParser();
		this.systemTableHandler = builder.getSystemTableHandler();
	}

	/**
	 * @param xsdTypeName - The name of a predefined XSD type
	 * @return
	 */
	protected String resolveXSDType(String xsdTypeName) {

		return typeMapper.resolveDataType(xsdTypeName);
	}

	/**
	 * @param compoundConstruct - An XER compound construct (e.g. part)
	 * @return - The fully qualified name of the data table (e.g. ss.part)
	 */
	protected String getDataTableName(XERCompoundConstruct compoundConstruct) {
		if (compoundConstruct instanceof XEREntity) {
			return databaseSchema + "." + ((XEREntity) compoundConstruct).getName();
		}
		else if (compoundConstruct instanceof XERGeneralization) {
			return databaseSchema + "." + ((XERGeneralization) compoundConstruct).getName();
		}
		else {
			logger.error("Invalid case.");
			return null;
		}
	}

	/**
	 * Note: it makes no sense to have a meta table for anything other than an XER Entity (I think)
	 * @param entity - An XER entity (e.g. part)
	 * @return - The fully qualified name of the meta table (e.g. ss.META_part)
	 */
	protected String getMetaTableName(XEREntity entity) {
		return databaseSchema + "." + META_PREFIX + entity.getName();
	}

	/**
	 * 
	 * @param setName - The name for a group of tables which appears in the comment
	 * @return - A header comment which makes the SQL more readable
	 */
	protected String getComment(String setName) {
		return COMMENT + DELIMITER + setName + DELIMITER + COMMENT + "\n\n";
	}

	/**
	 * @param multiValuedAttribute - An XERAttribute which is multi-valued
	 * @return - The name of the table which holds the multi-values
	 */
	protected String getDataTableName(XERAttribute multiValuedAttribute) {
		// sanity check
		if (!(multiValuedAttribute.getMaxOccurs() > 1)) {
			logger.error("A table is not allocated to single-valued attributes.");
			return null;
		}
		else {
			return databaseSchema + "." + multiValuedAttribute.getName();
		}
	}

	protected String getDataTableName(XERRelationship relationship) {
		return databaseSchema + "." + relationship.getName();
	}

	protected String getDataTableName(XERConstruct xerConstruct) {
		if (xerConstruct instanceof XERCompoundConstruct) {
			return getDataTableName((XERCompoundConstruct) xerConstruct);
		}
		else if (xerConstruct instanceof XERRelationship) {
			return getDataTableName((XERRelationship) xerConstruct);
		}
		else if (xerConstruct instanceof XERAttribute) {
			return getDataTableName((XERAttribute) xerConstruct);
		}
		else {
			logger.error("Invalid case.");
			return null;
		}
	}

	/**
	 * @param sql
	 * @return
	 */
	public String removeLastComma(String sql) {
		if (sql.charAt(sql.length() - 1) == ',') {
			sql = sql.substring(0, sql.length() - 1);
		}
		return sql;

	}

}
