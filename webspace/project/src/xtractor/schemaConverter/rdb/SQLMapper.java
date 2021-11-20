/*
 * Created on 24-Feb-2004
 *
 */
package xtractor.schemaConverter.rdb;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Element;

import xtractor.FolderDiscloser;
import xtractor.schemaConverter.xer.xerConstructs.XEREntity;
import xtractor.schemaConverter.xsd.JDomUtilities;

/**
 * @author ss401
 *
 */
public class SQLMapper {
	Logger logger = Logger.getLogger(this.getClass());
	//Map from String for XML Schema type to String for SQL type 
	Map typeMapWithPrefix;
	Map typeMapWithoutPrefix;
	//Map from String for XSD data type to trigger experssion in SQL
	Map triggerMap;
	String nameSpacePrefix;

	public SQLMapper(String namespacePrefix) {
		typeMapWithPrefix = new HashMap();
		typeMapWithoutPrefix = new HashMap();
		triggerMap = new HashMap();
		this.nameSpacePrefix = namespacePrefix;
		File typeMappingsFile = new File(FolderDiscloser.workingFolderPath + "/config/typeMappings.xml");
		Collection mappingPairs = (JDomUtilities.getRootElement(typeMappingsFile)).getChildren();
		for (Iterator iter = mappingPairs.iterator(); iter.hasNext();) {
			Element element = (Element) iter.next();
			String xsdType = element.getChild("xsd_type").getText();
			String sqlType = element.getChild("sql_type").getText();
			Element sqlCondition = element.getChild("trigger");
			typeMapWithPrefix.put(nameSpacePrefix + ":" + xsdType, sqlType);
			typeMapWithPrefix.put(xsdType, sqlType);
			if (sqlCondition != null) {
				triggerMap.put(nameSpacePrefix + ":" + xsdType, sqlCondition.getText());
			}

		}
	}

	/**
	 * @param xsdType - The type specified in the xml schema
	 * @return The SQL type which corresponds to
	 */
	public String resolveDataType(String xsdType) {
		//TODO: This doesn't solve the problem of not knowing if xsdTypes are prefixed by 'xsd:' or not
		String sqlType = (String) typeMapWithPrefix.get(xsdType);
		if (sqlType == null) {
			logger.error("Couldn't find entry in xml file for " + xsdType);
		}
		
		return sqlType.toUpperCase();
	}

	/**
	 * @param attributeRequired - Specifies whether the attribute is required or not
	 * @return - The SQL text which reflects the requirement of the attribute 
	 */
	public String resolveAttriubteCardinality(boolean attributeRequired) {
		if (attributeRequired) {
			return "NOT NULL";
		}
		else {
			return "";
		}
	}
	
	/**
	 * It doesn't matter whether the prefix is included or not
	 * @param typeName - A type which you want to determine if it is predefined 
	 * @return - True if it is a predefined XML Schema Datatype
	 */
	//USE XSDTYPES.isXSDDataType()
	/*public boolean isPredefinedType(String typeName){
		return typeMapWithPrefix.keySet().contains(typeName) || typeMapWithoutPrefix.keySet().contains(typeName);
	}*/

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

	/**
	 * @param xsdDataType - an XSD Data type
	 * @return - True if  the type cannot be represented exactly with a corresponding SQL data type 
	 * and thus requires a trigger to ensure correct values only are entered
	 */
	public boolean xsdTypeRequiresTriggers(String xsdDataType) {
		return triggerMap.keySet().contains(xsdDataType);
	}

	/**
	 * @param xsdDataType - An xsd data type
	 * @return - The sql boolean condition (excluding 'CHECK') that is required to ensure data is of this format
	 */
	public String getSQLCondition(String xsdDataType) {
		return (String) triggerMap.get(xsdDataType);
	}

	

}
