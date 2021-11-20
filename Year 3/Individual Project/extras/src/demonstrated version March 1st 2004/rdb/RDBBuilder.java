/*
 * Created on 24-Feb-2004
 *
 */
package rdb;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import xer.XERAttribute;
import xer.XEREntity;
import xer.XERModel;
import xer.XERRelationship;
import xsd.XSDSchemaManipulator;

/**
 * @author ss401
 * Takes an XER model and generates a relational database model 
 */
public class RDBBuilder {
	private XERModel model;
	Logger logger = Logger.getLogger(this.getClass());
	SQLMapper sqlMapper;

	/**
	 * @param xerModel - The XER model to convert to a relational schema
	 */
	public RDBBuilder(XERModel xerModel, String schemaURL) {
		this.model = xerModel;
		XSDSchemaManipulator schemaWalker = new XSDSchemaManipulator(schemaURL);
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
		Collection entities = model.getEntities();
		Collection relationships = model.getRelationships();

		//Go through each XER Relationship and make the necessary additions to the parent XER Entity
		for (Iterator iter = relationships.iterator(); iter.hasNext();) {
			XERRelationship xerRelationship = (XERRelationship) iter.next();
			XEREntity parent = xerRelationship.getParent();
			XEREntity foreignEntity = xerRelationship.getChild();
			// test whether the foreign entity has a key attribute
			if (foreignEntity.hasNoKey()) {
				// Assign it a numerical one
				foreignEntity.addKeyAttribute(new XERAttribute(foreignEntity));
			}
			for (Iterator iterator = foreignEntity.getPrimaryKey().iterator(); iterator.hasNext();) {
				// Add all key attributes of the foreign entity to the parent entity as a foreign key
				XERAttribute keyAttribute = (XERAttribute) iterator.next();
				parent.addAttribute(new XERAttribute(keyAttribute, foreignEntity, xerRelationship.getMinOccurs()));
			}

		}

		// Go through each XER Entity and generate the SQL to create the table
		for (Iterator iter = entities.iterator(); iter.hasNext();) {
			XEREntity entity = (XEREntity) iter.next();
			sql += "CREATE TABLE " + entity.getName() + " {\n";
			// specify each row that should exist in the table
			for (Iterator iterator = entity.getAttributes().iterator(); iterator.hasNext();) {
				XERAttribute attribute = (XERAttribute) iterator.next();
				String sqlDataType = sqlMapper.resolveDataType(attribute.getType());
				String sqlNullCondition = sqlMapper.resolveAttriubteCardinality(attribute.isRequired());
				String sqlReference = sqlMapper.resolveAttributeReference(attribute.getReferredEntity());
				sql += "\t" + attribute.getName() + "\t" + sqlDataType + "\t" + sqlNullCondition;
				if (iterator.hasNext()) {
					sql += ",\n";
				}
			}
			// specify what the primary key is
			//TODO: Check with book to see if this is the right syntax
			if (entity.hasPrimaryKey()) {
				sql += ",\n\tPRIMARY KEY(";
				for (Iterator iterator = entity.getPrimaryKey().iterator(); iterator.hasNext();) {
					XERAttribute keyAttribute = (XERAttribute) iterator.next();
					sql += keyAttribute.getName();
					if (iterator.hasNext()) {
						sql += ",";
					}
				}
				sql += ")";

			}
			sql += "\n}\n\n";
		}

		return sql;
	}

}
