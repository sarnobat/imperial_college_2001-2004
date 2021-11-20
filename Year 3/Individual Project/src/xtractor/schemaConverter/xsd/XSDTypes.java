/*
 * Created on 08-Mar-2004
 *
 */
package xtractor.schemaConverter.xsd;

import java.io.File;
import java.util.Iterator;

import org.jdom.Element;

import xtractor.FolderDiscloser;

/**
 * @author ss401
 *
 */
public class XSDTypes {

	/**
	 * Checks the xsdDataTypes.xml file to see if 'type' exists
	 * @param type - The name of the type whose origin is trying to be uncovered
	 * @return - True if 'type' is a built-in xsd data type
	 */
	public static boolean isXSDDatatype(String type, String prefix) {
		File dataTypesFile = new File(FolderDiscloser.workingFolderPath + "/config/xsdDataTypes.xml");
		Element root = JDomUtilities.getRootElement(dataTypesFile);
		for (Iterator iter = root.getChildren().iterator(); iter.hasNext();) {
			Element element = (Element) iter.next();
			String xsdDataType = prefix + ":" + element.getAttributeValue("name");
			if (xsdDataType.equals(type)) {
				return true;
			}
		}
		return false;
	}
}