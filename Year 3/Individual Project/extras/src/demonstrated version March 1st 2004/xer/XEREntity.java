/*
 * Created on 24-Feb-2004
 *
 */
package xer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * @author ss401
 * Models and XER Entity 
 */
public class XEREntity {
	Logger logger = Logger.getLogger(this.getClass());
	Collection attributes;
	Collection keyAttributes;
	String name;

	public XEREntity(String name,XERModel xerModel) {
		this.name = name;
		this.attributes = new ArrayList();
		this.keyAttributes = new ArrayList();
		xerModel.addEntity(this);
	}

	/**
	 * @return - A collection of XERAttribute for each attribute that exists in this entity
	 */
	public Collection getAttributes() {
		return attributes;

	}

	/**
	 * @return - The Collection of XERAttribute which uniquely identify the entity
	 */
	public Collection getPrimaryKey() {
		return keyAttributes;
	}

	/**
	 * 'attribute' should not be a primary key. Use addKeyAttribute(...) instead. 
	 * @param attribute - The attribute to be added to this entity
	 */
	public void addAttribute(XERAttribute attribute) {
		addAttribute(attribute,false);		
	}
	
	public void addKeyAttribute(XERAttribute attribute) {
		addAttribute(attribute,true);
	}
	
	/**
	 * Only use this for primary key attributes. Otherwise use addAttribute(XEREntity).
	 * @param attribute - The attribute to be added to this entity
	 * @param isKey - specifies whether the attribute is part of the primary key
	 */
	private void addAttribute(XERAttribute attribute,boolean isPrimaryKey){
		if(isPrimaryKey){
			keyAttributes.add(attribute);
		}
		attributes.add(attribute);
	}

	/**
	 * 
	 * @return - The name of this XER Entity
	 */
	public String getName(){
		return name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String acc = "";
		acc += getName() + "(";
		for (Iterator iter = attributes.iterator(); iter.hasNext();) {
			XERAttribute attribute = (XERAttribute) iter.next();
			acc+=attribute.toString();
			if(iter.hasNext()){
				acc+=",";
			}
			
		}
		acc+=")";
		return acc; 
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
		return keyAttributes.size() > 0;
	}

}
