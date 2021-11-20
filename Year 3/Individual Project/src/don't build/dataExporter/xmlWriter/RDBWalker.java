/*
 * Created on 13-May-2004
 */
package xtractor.dataExporter.xmlWriter;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import xtractor.dataExporter.xmlWriter.databaseManager.ExportDatabaseManager;

/**
 * @author ss401
 */
public class RDBWalker {

	Logger logger = Logger.getLogger(this.getClass());
	Writer fileWriter;
	ExportDatabaseManager databaseManager;
	String databaseSchemaName;

	/**
	 * @param fileWriter - The writer to write the xml output to
	 */
	public RDBWalker(Writer fileWriter,String databaseSchemaName) {
		this.fileWriter = fileWriter;
		this.databaseSchemaName = databaseSchemaName;
		this.databaseManager = new ExportDatabaseManager(databaseSchemaName);
	}

	/**
	 * Does the file writing  
	 * @param rootId - The globally unique root identifier for the element schema
	 */
	
	public void writeXMLFile(int id) {
	//TODO: most of this code should be moved to the other method, as it doesn't just apply to the root
	//TODO: we should be using the jdom api to write an xml document
		String elementName = databaseManager.getElementName(id); 
		try {
			//TODO: we'll somehow need to keep track of the indentation level
			fileWriter.write("<" + elementName + ">");
			
			Collection childElementNames = databaseManager.getChildElementNames(id);
			//For each type of child element
			for (Iterator iter = childElementNames.iterator(); iter.hasNext();) {
				String childElementName = (String) iter.next();
				List elementAttributes = databaseManager.getAttributeOrdering(childElementName);
				/*
				 * for each table
				 * 	find out which rows have 'id' as their parent
				 * 	for each row
				 * 		return a map of attribute names to values
				 */
				 
				 //List of maps
				 List childElements = databaseManager.getChildrenElements(id,childElementName);
				 for (Iterator iterator = childElements.iterator(); iterator.hasNext();) {
					Map attributeMap = (Map) iterator.next();
					//Collection attributeNames = elementAttributes.keySet();
					
					fileWriter.write("<" + childElementName+" ");
					
					for (Iterator iterator2 = elementAttributes.iterator(); iterator2.hasNext();) {
						String attributeName = (String) iterator2.next();
						String attriubteValue = (String) attributeMap.get(attributeName);
						fileWriter.write(attributeName+"=\""+attriubteValue +"\"");
						if(iterator2.hasNext()){
							fileWriter.write(" ");
						}
						else{
							fileWriter.write(">");
						}
					}
					//TODO: We need to determine if the tag is supposed to be self-closed
					//Actually, JDOM doesn't seem to let you determine this. The only
					// way is to self close them if they don't have any children
					fileWriter.write("</" + childElementName+">");
				}
			}
			
			//TODO: Call recursively for child elements
			/*List childIds = databaseManager.getChildren(rootId);
			for (Iterator iter = childIds.iterator(); iter.hasNext();) {
				Integer id = (Integer) iter.next();
				writeElement(id.intValue());
			}*/
			
			fileWriter.write("\n");
			fileWriter.write("</" + elementName + ">");
			fileWriter.flush();
		}
		catch (IOException e) {
			logger.error("Unable to write XML Document."+e);
		}
		

	}

	/**
	 * @param id - id of the element schema element
	 */
	private void writeElement(int id) {
		logger.warn("Unimplemented Method");
	}

}
