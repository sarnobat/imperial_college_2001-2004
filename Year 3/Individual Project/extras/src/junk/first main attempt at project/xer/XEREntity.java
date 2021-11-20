/*
 * Created on 16-Feb-2004
 *
 */
package xer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import xsd.XSDAttribute;

/**
 * @author ss401
 *
 */
public class XEREntity extends XERComponent {

	Logger logger = Logger.getLogger(this.getClass());
	String name;
	//Map from String attribute name to XERAttribute
	Map entityAttributes = new TreeMap();
	//Map from String for key name to XEREntity
	Map foreignEntities = new TreeMap();

	public XEREntity(String entityName) {
		name = entityName;

	}

	/**
	 * @param list - A list of Strings containing attributes to be added to the entity
	 */
	/*public void addAttributeList(List list) {
		//TODO: This list will have to be of elements so that their min and max occurs constraints
		// can be determined
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			String attributeName = (String) iter.next();
			entityAttributes.put(attributeName,new XERAttribute(attributeName,true));
			
		}
		
	}*/

	public void addAttribute(XSDAttribute xsdAttribute) {
		String attributeName = xsdAttribute.getName();
		int minOccurs = xsdAttribute.getMinOccurs();
		int maxOccurs = xsdAttribute.getMaxOccurs();
		if (maxOccurs == 1) {
			//XSD Attribute becomes and XER Attribute
			XERAttribute xerAttribute = new XERAttribute(attributeName, minOccurs, maxOccurs);
			this.entityAttributes.put(attributeName, xerAttribute);
		}
		else if (maxOccurs > 1) {
			//TODO: Create a foreign key attribute AND a new entity
			logger.warn("Unimplemented");
		}
		else {
			logger.error("Invalid cardinality constraints in xml schema");
		}

	}

	/**
	 * @param attributes - A map from attribute name to XSDAttribute
	 */
	/*public void addAttributes(Map attributes) {
		//TODO: Here is where you decide on the exact form of the attribute (i.e. required etc.)
	}*/

	public String sql() {
		return name;
	}
	/**
	 * 
	 * @return A List of Strings for the attribute names
	 */
	public List getAttributeNames() {
		return new ArrayList(entityAttributes.values());
	}
	/**
	 * 
	 * @return A List of XERAttributes contained in this entity
	 */
	public List getXERAttributes() {
		return new ArrayList(entityAttributes.values());
	}

	/**
	 * @return Name of entity
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param keyReference - The name of the attribute which will act as a foreign key to 'foreignEntity'
	 * @param foreignEntity - The name of the entity being referenced
	 */
	public void addForeignEntity(XEREntity foreignEntity) {
		foreignEntities.put(foreignEntity.getName(), foreignEntity);
	}

	/**
	 * @param string
	 */
	/*public void addXERAttribute(String attributeName) {
		//TODO: Cardinality constraints will have to be specified accordingly. They're not all 1..1
		entityAttributes.put(attributeName, new XERAttribute(attributeName, 1, 1));
		return;
	}*/

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String str = name + "(";
		for (Iterator iter = entityAttributes.keySet().iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			str += element;
			if(iter.hasNext()){
				str+=",";
			}
		}
		str += ")";
		return str;
	}

}
