/*
 * Created on 24-Feb-2004
 *
 */
package xtractor.schemaConverter.xer.xerConstructs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import xtractor.schemaConverter.xer.XERBuilder;
import xtractor.schemaConverter.xsd.componentParsers.RelationshipParser;

/**
 * @author ss401
 * Models and XER Entity 
 */
public class XEREntity extends XERCompoundConstruct {

	Map attributes;
	Collection primaryKeys;
	Collection foreignKeys;
	Collection generalizations;
	
	boolean convertedToSQL = false;
	RelationshipParser relationshipParser;
	XEREntity implicitParent;

	public XEREntity(String name, XERBuilder xerBuilder) {
		super(xerBuilder);
		setOriginalName(name);
		this.attributes = new TreeMap();
		this.primaryKeys = new ArrayList();
		this.foreignKeys = new ArrayList();

		this.generalizations = new ArrayList();

		this.relationshipParser = xerBuilder.getRelationshipParser();

	}

	/**
	 * @return - A collection of XERAttribute for each attribute that exists in this entity
	 */
	public Collection getAttributes() {
		return attributes.values();

	}

	/**
	 * @return - The Collection of XERAttribute which uniquely identify the entity
	 */
	public Collection getPrimaryKeys() {
		return primaryKeys;
	}

	/**
	 * @param attribute - The attribute to be added to this entity
	 */
	public void addAttribute(XERAttribute attribute) {
		if(attribute instanceof XERPrimaryKey){
			addPrimaryKeyAttribute((XERPrimaryKey)attribute);
		}		
		else if (attribute instanceof XERForeignKey){
			addForeignKeyAttribute((XERForeignKey) attribute);
		}
		else{
			if (!attributes.keySet().contains(attribute.getName())) {
				attributes.put(attribute.getName(), attribute);
			}
		}
	}

	/**
	 * Add a new attribute to this entity which is a primary key
	 * @param attribute
	 */
	public void addPrimaryKeyAttribute(XERPrimaryKey attribute) {
		addAttribute(attribute);
		promoteAttributeToPrimaryKey(attribute);
	}

	/**
	 * Adds an exisiting attribute to the primary key
	 * @param attribute
	 */
	public void promoteAttributeToPrimaryKey(XERAttribute attribute) {
		if (attributes.values().contains(attribute)) {

			XERPrimaryKey key = getXERFactory().createXERPrimaryKey(attribute, this);

			// Replace the xer attribute in the general attribute map with an xer primary key subclass 
			attributes.remove(attribute.getName());
			attributes.put(key.getName(), key);

			// Add the xer primary key to a separate map too
			primaryKeys.add(key);
		}
		else {
			logger.error("The attribute " + attribute.getName() + " doesn't currently reside in this entity.");
		}
	}

	/**
	 * @param attribute - The XER attribute which is a foreign key
	 */
	public void addForeignKeyAttribute(XERForeignKey attribute) {
		foreignKeys.add(attribute);
		addAttribute(attribute);
	}


	public String getSummary() {
		String str = "";
		str += getName() + "(";
		for (Iterator iter = attributes.values().iterator(); iter.hasNext();) {
			XERAttribute attribute = (XERAttribute) iter.next();
			if (primaryKeys.contains(attribute)) {
				str += "[" + attribute.getName() + "]";
			}
			else {
				str += attribute.getName();
			}

			str += ",";
		}
		str = removeLastComma(str);
		str += ",";
		for (Iterator iter = generalizations.iterator(); iter.hasNext();) {
			XERGeneralization xerGeneralization = (XERGeneralization) iter.next();
			str += xerGeneralization.getName();

			str += ",";

		}
		str = removeLastComma(str);
		str += ")";
		return str;
	}
	

	/**
	 * @return - True if this entity has no attributes which constitute the primary key
	 */
	public boolean hasNoKey() {
		return !hasPrimaryKey();
	}

	/**
	 * @return - True if this entity has at any attributes which constitute the primary key
	 */
	public boolean hasPrimaryKey() {
		return primaryKeys.size() > 0;
	}

	/**
	 * @return - A String list of comma separated attribute names
	 */
	public String getPrimaryKeyNames() {
		String str = "";
		for (Iterator iter = primaryKeys.iterator(); iter.hasNext();) {
			XERAttribute keyAttribute = (XERAttribute) iter.next();
			str += keyAttribute.getName();
			if (iter.hasNext()) {
				str += ",";
			}
		}
		return str;
	}

	/**
	 * @return - True if the table has already been converted to SQL
	 */
	public boolean convertedToSQL() {
		return convertedToSQL;
	}

	/**
	 * Sets the flag indicating that this entity has already been converted to SQL
	 */
	public void setConverted() {
		convertedToSQL = true;
	}

	/* (non-Javadoc)
	 * @see xtractor.schemaConverter.xer.XERCompoundConstruct#addStructure(xtractor.schemaConverter.xer.XERConstruct)
	 */
	public void addConstruct(XERConstruct xerConstruct, int minOccurs, int maxOccurs) {
		if (xerConstruct instanceof XEREntity) {
			relationshipParser.parseImplicitRelationship(this, (XERCompoundConstruct) xerConstruct, minOccurs, maxOccurs);
		}
		else if (xerConstruct instanceof XERAttribute) {
			// May need to handle key attributes differently
			addAttribute((XERAttribute) xerConstruct);
			//no need to go through the xer factory because no new XER constrcut (i.e. a relationship) needs to be created
		}
		else if (xerConstruct instanceof XERGeneralization) {
			relationshipParser.parseImplicitRelationship(this, (XERCompoundConstruct) xerConstruct, minOccurs, maxOccurs);
			//generalizations.add((XERGeneralization) xerConstruct);
			 ((XERGeneralization) xerConstruct).setMostRecentParentEntity(this);
		}
		else {
			logger.warn("Unimplemented case");
		}
	}

	/**
	 * @param attributeName = The name of an attribute
	 * @return - the XER Attribute of it
	 */
	public XERAttribute getAttribute(String attributeName) {
		return (XERAttribute) attributes.get(attributeName);
	}

	/**
	 * Note that the attribute may not be the full part of a foreign key
	 * @param attribute - The XERAttribute which we have discovered is PART OF a foreign key
	 * @param child - The XER Compound Construct which is BEING referenced
	 */
	public XERForeignKey promoteAttributeToForeignKey(XERAttribute attribute, XERCompoundConstruct child) {
		if (attributes.values().contains(attribute)) {
			XERForeignKey fKey = xerBuilder.getXERFactory().createXERForeignKey(attribute, child);
			attributes.remove(attribute.getName());
			attributes.put(fKey.getName(), fKey);
			foreignKeys.add(fKey);
			return fKey;
		}
		else {
			logger.error("The attribute " + attribute.getName() + " doesn't currently reside in this entity.");
			return null;
		}
	}

	/**
	 *@param parent - The XER construct which contains this XER entity in the XML hierarchy
	 *(We allow this not to be an entity. When it isn't, the 'getmostrecententity' method
	 *takes care of it)
	 */
	public void setImplicitParent(XERCompoundConstruct parent) {
		if (parent instanceof XERGeneralization) {
			this.implicitParent = ((XERGeneralization) parent).getMostRecentParentEntity();
		}
		else if (parent instanceof XEREntity) {
			this.implicitParent = (XEREntity) parent;
		}
	}

	/**
	 *@return - The most recent XER entity above this XER entity in the XML hierarchy
	 */
	public XEREntity getImplicitParent() {

		return this.implicitParent;
	}

	/**
	 * @return Returns the foreignKeyAttributes.
	 */
	public Collection getForeignKeyAttributes() {
		return foreignKeys;
	}

}
