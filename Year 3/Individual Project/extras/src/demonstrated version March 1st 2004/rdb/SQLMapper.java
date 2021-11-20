/*
 * Created on 24-Feb-2004
 *
 */
package rdb;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Element;

import xer.XEREntity;
import xsd.JDomUtilities;

/**
 * @author ss401
 *
 */
public class SQLMapper {
	Logger logger = Logger.getLogger(this.getClass());
	//Map from String for XML Schema type to String for SQL type 
	Map typeMap;
	String nameSpacePrefix;

	public SQLMapper(String namespacePrefix) {
		typeMap = new HashMap();
		this.nameSpacePrefix = namespacePrefix;
		Collection mappingPairs = (JDomUtilities.getRootElement("typeMappings.xml")).getChildren();
		for (Iterator iter = mappingPairs.iterator(); iter.hasNext();) {
			Element element = (Element) iter.next();
			String xsdType = element.getChild("xsd_type").getText();
			String sqlType = element.getChild("sql_type").getText();
			typeMap.put(nameSpacePrefix + ":" + xsdType, sqlType);
		}
	}

	/**
	 * @param xsdType - The type specified in the xml schema
	 * @return The SQL type which corresponds to
	 */
	public String resolveDataType(String xsdType) {
		String sqlType = (String) typeMap.get(xsdType);
		if (sqlType == null) {
			logger.error("Couldn't find entry in xml file for " + xsdType);
		}
		return sqlType;
	}

	/**
	 * @param attributeRequired - Specifies whether the attribute is required or not
	 * @return - The SQL text which reflects the requirement of the attribute 
	 */
	public String resolveAttriubteCardinality(boolean attributeRequired) {
		if(attributeRequired){
			return "NOT NULL";
		}
		else{
			return "";
		}
	}

	/**
	 * @param entity - The XEREntity which is a foreign entity
	 * @return - The SQL which states that an attribute references a foreign table
	 */
	public String resolveAttributeReference(XEREntity entity) {
		//UNIMPLEMENTED: Unimplemented Method
		logger.warn("Unimplemented Method");
		return null;
		//will be along the lines of "REFERENCES Order(order_id)"
	}
}
