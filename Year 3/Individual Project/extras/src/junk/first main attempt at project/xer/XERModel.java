/*
 * Created on 16-Feb-2004
 *
 */
package xer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author ss401
 *
 */
public class XERModel {

	//Mapping from entity name to XEREntity
	Map entities = new HashMap();
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 
	 */
	public XERModel() {

	}

	/**
	 * Creates a new entity to the XER Model
	 * @param rootElementName The name of the entity to be created
	 */
	public void addNewEntity(String elementName) {
		//entities.put(rootElementName, new XEREntity(rootElementName));
		addEntity(new XEREntity(elementName));
	}

	public void addEntity(XEREntity entity) {
		entities.put(entity.getName(), entity);
		logger.debug("XER Entity " + entity.getName() + " added.");
	}

	/**
	 * @param rootElementName
	 * @return
	 */
	public XEREntity getEntity(String rootElementName) {

		return (XEREntity) entities.get(rootElementName);
	}
	public String getSQL() {
		String sql = "";

		//Create tables
		for (Iterator iter = entities.values().iterator(); iter.hasNext();) {
			XEREntity entity = (XEREntity) iter.next();
			sql += "CREATE TABLE " + entity.sql() + "{\n";
			Collection entityAttributes = entity.getAttributeNames();
			for (Iterator iterator = entityAttributes.iterator(); iterator.hasNext();) {
				XERAttribute xerAttribute = (XERAttribute) iterator.next();
				sql += "\t" + xerAttribute.sql();
				if (iterator.hasNext()) {
					sql += ",\n";
				}
			}
			if (entityAttributes.size() > 0) {
				sql += "\n";
			}
			sql += "}\n";
		}

		return sql;
	}

	/**
	 * @param xerComponent
	 */
	public void addComponent(XERComponent xerComponent) {
		if (xerComponent instanceof XEREntity) {
			XEREntity entity = (XEREntity) xerComponent;
			entities.put(entity.getName(), entity);
		}
		else {
			logger.warn("Adding non-entity components not yet implemented.");
		}
	}

}
