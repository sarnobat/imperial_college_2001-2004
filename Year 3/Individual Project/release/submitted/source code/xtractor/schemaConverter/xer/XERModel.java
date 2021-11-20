/*
 * Created on 24-Feb-2004
 *
 */
package xtractor.schemaConverter.xer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import xtractor.schemaConverter.xer.xerConstructs.XERCompoundConstruct;
import xtractor.schemaConverter.xer.xerConstructs.XERConstruct;
import xtractor.schemaConverter.xer.xerConstructs.XEREntity;
import xtractor.schemaConverter.xer.xerConstructs.XERGeneralization;
import xtractor.schemaConverter.xer.xerConstructs.XERRelationship;

/**
 * @author ss401
 *
 */
public class XERModel {

	Logger logger = Logger.getLogger(this.getClass());
	Map relationships;
	Map entities;
	Map generalizations;
	String name;
	XERCompoundConstruct root;
	XERFactory xerFactory;
	
	// Map from XERConstruct to alias (String)
	Map aliases;

	XERModel(String modelName, XERFactory xerFactory) {
		this.name = modelName;
		this.relationships = new TreeMap();
		this.entities = new TreeMap();
		this.generalizations = new TreeMap();
		this.aliases = new HashMap();
		this.xerFactory = xerFactory;		
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
	void addEntity(XEREntity entity) {
		entities.put(entity.getName(), entity);
	}
	/**
	 * Adds a relationship to this model
	 * @param entity - The relationship to be added
	 */
	void addRelationship(XERRelationship relationship) {
		relationships.put(relationship.getName(), relationship);
	}

	/**
	 * Adds a generalization to this model
	 * @param generalization - The XER Generalization to be added
	 */
	public void addGeneralization(XERGeneralization generalization) {
		generalizations.put(generalization.getName(), generalization);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String getSummary() {
		String str = "";
		str += "ENTITIES:\n";
		for (Iterator iter = entities.values().iterator(); iter.hasNext();) {
			XEREntity entity = (XEREntity) iter.next();
			str += entity.toString();
			/*String entityName = entity.getName();
			str += entityName + "(";
			Collection attributes = entity.getAttributes();
			for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
				XERAttribute attribute = (XERAttribute) iterator.next();
				str += attribute.getName();
				if (iterator.hasNext()) {
					str += ",";
				}
			
			}
			str += ")";*/
			if (iter.hasNext()) {
				str += "\n";
			}

		}
		str += "\n\nRELATIONSHIPS:\n";
		for (Iterator iter = relationships.keySet().iterator(); iter.hasNext();) {
			String relationshipName = (String) iter.next();
			str += relationshipName;
			if (iter.hasNext()) {
				str += ",\n";
			}
		}
		str += "\n\nGENERALIZATIONS:\n";
		for (Iterator iterator = generalizations.values().iterator(); iterator.hasNext();) {
			XERGeneralization generalization = (XERGeneralization) iterator.next();
			str += generalization + "\n";
		}
		return str;
	}

	public String toString() {
		return this.getName();
	}

	/**
	 * @param rootEntity - The XER Entity which is the root of the model
	 */
	public void setRootElement(XEREntity rootEntity) {
		this.root = rootEntity;
	}

	/**
	 * @return - The XER entity which is the root of the model
	 */
	public XERCompoundConstruct getRootEntity() {
//		if (root == null) {
//
//			List allCompoundConstructs = new LinkedList(entities.values());
//			allCompoundConstructs.addAll(generalizations.values());
//			//Collections.copy(new LinkedList(entities.values()), allCompoundConstructs);
//			//Collections.copy(new LinkedList(generalizations.values()), allCompoundConstructs);
//			for (Iterator iter = relationships.values().iterator(); iter.hasNext();) {
//				XERRelationship relationship = (XERRelationship) iter.next();
//				XERCompoundConstruct child = relationship.getChild();
//				allCompoundConstructs.remove(child);
//			}
//
//			// sanity check
//			if (allCompoundConstructs.size() > 1) {
//				logger.error("Couldn't determine root entity");
//			}
//			else {
//				root = (XERCompoundConstruct) allCompoundConstructs.get(0);
//			}
//		}
		return root;
	}

	/**
	 * @return - The name of the XER model
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name - The name of the entity
	 * @return - True if the entity with the given name already exists
	 */
	public boolean entityExists(String name) {
		return entities.keySet().contains(name);
	}

	/**
	 * @param name
	 * @return
	 */
	public XEREntity getEntity(String name) {
		return (XEREntity) entities.get(name);
	}

	/**
	 * We need this so that when adding one XERCompoundConstruct to another, we can
	 * create a relationship between the two
	 * @return - The XER Factory that created the model
	 */
	public XERFactory getXERFactory() {
		return xerFactory;
	}

	/**
	 * @param entityName - The name of the entity
	 * @return - The XEREntity representing it
	 */
	public XEREntity getXEREntity(String entityName) {
		if (entities.keySet().contains(entityName)) {
			return (XEREntity) entities.get(entityName);
		}
		else {
			logger.error("Couldn't find an XER compound construct named " + entityName);
			return null;
		}
	}

	/**
	 * @return - A collection of all the generalizations in this model
	 */
	public Collection getGeneralizations() {
		return generalizations.values();
	}

	/**
	 * @param construct
	 * @param alias
	 */
	public void addAlias(XERConstruct construct, String alias) {
		aliases.put(construct,alias);		
	}
	
	/**
	 * 
	 * @return - A Map from XERConstruct to alias (String)
	 */
	public Map getAliases(){
		return aliases;
	}
}
