/*
 * Created on 24-Feb-2004
 *
 */
package xer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * @author ss401
 *
 */
public class XERModel {

	Logger logger = Logger.getLogger(this.getClass());
	Map relationships;
	Map entities;

	public XERModel() {
		relationships = new TreeMap();
		entities = new TreeMap();
	}

	/**
	 * @return - A collection of XEREntity for each entity that exists in the XER Model  
	 */
	public Collection getEntities() {
		Collection xerEntities = entities.values();
		List l = new ArrayList(xerEntities);
		return xerEntities;
	}

	/**
	 * @return - A Collection of XERRelationship for all relationships that exist in this XER model
	 */
	public Collection getRelationships() {
		return relationships.values();
	}

	/**
	 * Adds an entity to this model
	 * @param entity - The entity to be added
	 */
	public void addEntity(XEREntity entity) {
		entities.put(entity.getName(), entity);
	}
	/**
	 * Adds a relationship to this model
	 * @param entity - The relationship to be added
	 */
	public void addRelationship(XERRelationship relationship) {
		relationships.put(relationship.getParent().getName() + "-->" + relationship.getChild().getName(), relationship);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String str = "";
		str += "ENTITIES:\n";
		for (Iterator iter = entities.keySet().iterator(); iter.hasNext();) {
			String entityName = (String) iter.next();
			str += entityName;
			if (iter.hasNext()) {
				str += ",";
			}
		}
		str += "\nRELATIONSHIPS:\n";
		for (Iterator iter = relationships.keySet().iterator(); iter.hasNext();) {
			String relationshipName = (String) iter.next();
			str += relationshipName;
			if (iter.hasNext()) {
				str += ",";
			}
		}
		return str;
	}

}
