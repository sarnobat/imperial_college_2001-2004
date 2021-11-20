/*
 * Created on 18-May-2004
 *
 */
package xtractor.schemaConverter.xer.xerConstructs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import xtractor.schemaConverter.xer.XERBuilder;

/**
 * @author ss401
 *
 */
public class XERGeneralization extends XERCompoundConstruct {

	//Map entities;
	XEREntity mostRecentParentEntity;

	// This contains entities AND attributes 
	Map specializations;

	//TODO: I think these should be in XER relationship, not here
	int minOccurs;
	int maxOccurs;

	//Shouldn't have an implicit parent. This is purely for XML element schema purposes;
	//at this level, choice entities don't exist
	//XERCompoundConstruct implicitParent;

	/**
	 * @param generalizationName - The name of the generalization
	 * @param specializations - A collections of XER Constructs (compound or not)
	 * @param minOccurs - The minimum number of times the choice collection (AS A WHOLE) can be selected
	 * @param maxOccurs - The maximum number of times the choice collection (AS A WHOLE) can be selected
	 */
	public XERGeneralization(
		String generalizationName,
		Collection specializations,
		int minOccurs,
		int maxOccurs,
		XERBuilder xerBuilder) {
		super(xerBuilder);
		this.specializations = new HashMap();
		for (Iterator iter = specializations.iterator(); iter.hasNext();) {
			XERConstruct xerConstruct = (XERConstruct) iter.next();
			this.specializations.put(xerConstruct.getName(), xerConstruct);
		}
		setOriginalName(generalizationName);
		this.minOccurs = minOccurs;
		this.maxOccurs = maxOccurs;
	}

	public void addConstruct(XERConstruct xerConstruct, int minOccurs, int maxOccurs) {
		if (xerConstruct instanceof XEREntity) {
			XEREntity entity = (XEREntity) xerConstruct;
			specializations.put(entity.getName(), entity);
			entity.setImplicitParent(this);
		}
		else if (xerConstruct instanceof XERAttribute) {
			XERAttribute attribute = (XERAttribute) xerConstruct;
			specializations.put(attribute.getName(), attribute);

			/*if (attribute instanceof XERPrimaryKey) {
				XERPrimaryKey key = (XERPrimaryKey) attribute;
				super.keyAttributes.add(key);
			}
			else if (attribute instanceof XERForeignKey) {
				foreignKeyAttributes.add(attribute);
			}*/

		}
		else {
			logger.warn("Unimplemented case");
		}

	}

	public String getSummary() {
		String str = getName() + "(";
		for (Iterator iter = specializations.values().iterator(); iter.hasNext();) {
			XERConstruct xerEntity = (XERConstruct) iter.next();
			str += xerEntity.getName();
			if (iter.hasNext()) {
				str += ",";
			}
		}

		str += ")";
		return str;
	}

	/**
	 * @return - The minimum number of times the whole collection of choices can be selected
	 */
	public int getMinOccurs() {
		return minOccurs;
	}

	/**
	 * @return - The maximum number of times the whole collection of choices can be selected
	 */
	public int getMaxOccurs() {
		return maxOccurs;
	}

	/* (non-Javadoc)
	 * @see xtractor.schemaConverter.xer.xerConstructs.XERCompoundConstruct#getAttribute(java.lang.String)
	 */
	public XERAttribute getAttribute(String attributeName) {

		return (XERAttribute) specializations.get(attributeName);
	}

	/**
	 * @param entity - The most recent XEREntity above this generalization construct in the XER model hierarchy
	 */
	public void setMostRecentParentEntity(XEREntity entity) {
		this.mostRecentParentEntity = entity;
	}

	/**
	 * @return - The XEREntity which is closest to this generalization above it in the model hierarchy
	 */
	public XEREntity getMostRecentParentEntity() {
		return this.mostRecentParentEntity;
	}

	/**
	 * @return - A collection of XER Constructs which are specific forms of this generalization
	 */
	public Collection getSpecializations() {
		return this.specializations.values();
	}

	/**
	 * @param specializationEntity
	 * @param newEntity
	 */
	public void replaceSpecialization(XEREntity oldEntity, XEREntity newEntity) {
		for (Iterator iter = specializations.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			XERConstruct construct = (XERConstruct) specializations.get(key);
			if(construct instanceof XEREntity){
				if(construct.equals(oldEntity)){
					logger.debug("Replacing specialization");
					specializations.remove(key);
					specializations.put(newEntity.getName(),newEntity);
				}
			}
		}		
	}

}
