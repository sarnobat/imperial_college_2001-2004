/*
 * Created on 24-Feb-2004
 *
 */
package xtractor.schemaConverter.rdb;

import java.io.File;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import xtractor.schemaConverter.xer.XERModel;
import xtractor.schemaConverter.xer.xerConstructs.XERAttribute;
import xtractor.schemaConverter.xer.xerConstructs.XEREntity;
import xtractor.schemaConverter.xer.xerConstructs.XERForeignKey;
import xtractor.schemaConverter.xer.xerConstructs.XERPrimaryKey;
import xtractor.schemaConverter.xsd.XSDSchemaManipulator;
/*
 * TODO: Need to create Triggers for schema datatypes which don't correspond exactly to SQL datatypes
 */
/**
 * @author ss401
 * Takes an XER model and generates a relational database model 
 */
public class RDBBuilder {
	private XERModel model;
	Logger logger = Logger.getLogger(this.getClass());
	SQLMapper sqlMapper;
	String deleteSQL;

	/**
	 * @param xerModel - The XER model to convert to a relational schema
	 * @param schemaFile
	 */
	public RDBBuilder(XERModel xerModel, File schemaFile) {
		this.model = xerModel;
		XSDSchemaManipulator schemaWalker = new XSDSchemaManipulator(schemaFile);
		String schemaPrefix = schemaWalker.getRootElement().getNamespace().getPrefix();
		this.sqlMapper = new SQLMapper(schemaPrefix);
	}

	/**
	 * Note the XERModel will be changed by this method because the relationships will be converted into
	 * attributes which are foreign keys
	 * @param xerModel
	 * @return - The SQL which when invoked will build the database schema for the XER Model
	 */
	public String build() {
		String sql = "";
		deleteSQL = "";
		Collection entities = model.getEntities();
		String schemaName = model.getName();
		
		sql += "CREATE SCHEMA " + schemaName + ";\n\n";
		
		//Iteratively (and Recursively) Create tables
		for (Iterator iter = entities.iterator(); iter.hasNext();) {
			XEREntity entity = (XEREntity) iter.next();
			if (!entity.convertedToSQL()) {
				sql += createTable(entity, schemaName);
			}
		}
		/*XEREntity root = model.getRootEntity();		
		sql += createTable(root, schemaName);*/

		return sql;
	}

	/**
	 * Referenced tables are also created
	 * @param entity - The entity which must be created using SQL
	 * @return - The SQL to create 'entity' IN ADDITION TO any tables it references
	 */
	public String createTable(XEREntity entity, String schemaName) {
		entity.setConverted();
		String sqlParentTable = "CREATE TABLE " + schemaName + "." + entity.getName() + " (\n";
		String sqlReferencedTable = "";

		// specify each row that should exist in the table
		for (Iterator iterator = entity.getAttributes().iterator(); iterator.hasNext();) {
			XERAttribute attribute = (XERAttribute) iterator.next();
			String xsdDataType = attribute.getType();
			String sqlDataType = sqlMapper.resolveDataType(xsdDataType);
			String sqlNullCondition = sqlMapper.resolveAttriubteCardinality(attribute.isRequired());
			//String sqlReference = sqlMapper.resolveAttributeReference(attribute.getReferredEntity());
			sqlParentTable += "\t" + attribute.getName() + "\t" + sqlDataType + "\t" + sqlNullCondition;
			if (attribute instanceof XERPrimaryKey) {
				sqlParentTable += "\tPRIMARY KEY";
			}
			else if (attribute instanceof XERForeignKey) {
				XERForeignKey foreignKeyAttribute = (XERForeignKey) attribute;
				XEREntity foreignEntity = foreignKeyAttribute.getForeignEntity();

				//Create the referenced table first, so that any references to it are valid
				if (!foreignEntity.convertedToSQL()) {
					// Only create the table if it hasn't previously been created
					sqlReferencedTable += createTable(foreignEntity, schemaName);
				}

				//Add relevant sql for parent table
				sqlParentTable += "\tREFERENCES "
					+ schemaName
					+ "."
					+ foreignEntity.getName()
					+ "("
					+ foreignEntity.getKeyAttributeNames()
					+ ")";
			}
			if (sqlMapper.xsdTypeRequiresTriggers(xsdDataType)) {
				sqlParentTable += "\tCHECK (" + attribute.getName() + sqlMapper.getSQLCondition(xsdDataType) + ")";
			}
			if (iterator.hasNext()) {
				sqlParentTable += ",\n";
			}
		}
		sqlParentTable += "\n);\n\n";
		deleteSQL += "DROP TABLE " + entity.getName() + " CASCADE;\n";
		return (sqlReferencedTable + sqlParentTable);
	}

	/**
	 * @return - The sql code for deleting the schema from the database
	 */
	public String getDeleteSQL() {
		return deleteSQL;
	}


	/**
	 * @param out - The place to write the SQL code
	 */
	public void build(Writer out) {
		logger.warn("Unimplemented Method");
		
	}

}
