/*
 * Created on 17-Mar-2004
 *  
 */
package xtractor.dataImporter.xmlReader;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org._3pq.jgrapht.DirectedGraph;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Parent;

import xtractor.dataImporter.xmlReader.dataPopulator.DataPopulator;
import xtractor.dataImporter.xmlReader.dataPopulator.DependencyGraphBuilder;
import xtractor.dataImporter.xmlReader.dataPopulator.VertexDegreeComparator;
import xtractor.schemaConverter.exception.NoAssociatedSchemaException;
import xtractor.schemaConverter.xsd.JDomUtilities;
import exception.AddDataException;
import exception.AddMetaDataException;

/**
 * @author ss401
 *  
 */

public class XMLWalker {

	Writer out;
	String databaseSchemaName;
	Logger logger = Logger.getLogger(this.getClass());
	Document xmlDocument;
	File xmlDataFile;
	DataPopulator dataPopulator;
	DependencyGraphBuilder builder;

	// Ids are the globally unique element schema ids
	// XML Element to element schema id 
	Map elementsToGlobalIds;
	// XML Element to local order of apperance
	Map elementsToLocalOrders;

	/**
	 * @param xmlFile
	 * @param databaseSchemaName
	 */
	public XMLWalker(File xmlDataFile, String databaseSchemaName, Writer out) {

		this.out = out;
		this.xmlDataFile = xmlDataFile;
		this.xmlDocument = JDomUtilities.getDocument(xmlDataFile);
		this.databaseSchemaName = databaseSchemaName;
		/*String schemaFileName = getAssociatedSchema();
		this.databaseSchemaName = schemaFileName.split(".x")[0];*/
		this.dataPopulator = new DataPopulator(this.databaseSchemaName, out);
		this.builder = new DependencyGraphBuilder(databaseSchemaName);

		this.elementsToGlobalIds = new HashMap();
		this.elementsToLocalOrders = new HashMap();

	}

	/**
	 * Populates the database with the information contained in the xml File specified in the field variable (that was set in the
	 * constructor)
	 */
	public void translateDocument() throws IOException {
		out.write("Determining order in which elements must be written...");
		Map elementNameToElementCollection = getDocumentElements();		
		DirectedGraph g = builder.build();
		out.write("done\n");

		int elementCount = 0;
		int sucesses = 0;
		int statementNumber = 1;
		while (g.vertexSet().size() > 0) {
		//while(elementNameToElementCollection.size() > 0){
			// Obtain the next object
			List nodes = new LinkedList(g.vertexSet());
			Collections.sort(nodes, new VertexDegreeComparator(g));
			String elementName = (String) nodes.get(0);
			if (g.outgoingEdgesOf(elementName).size() > 0) {
				logger.error("Cyclic dependencies between element tables.");
				break;
			}

			Collection elements = (Collection) elementNameToElementCollection.get(elementName);
			if (elements != null) {
				elementCount += elements.size();
				for (Iterator iter = elements.iterator(); iter.hasNext();) {

					Element element = (Element) iter.next();
					try {
						out.write("Executing data addition " + statementNumber +"...");
						translateElement(element);
						sucesses++;
						out.write("success\n");
					}
					catch (AddMetaDataException e) {
						String error = "Couldn't add meta data.\n\tREASON:" + e;
						logger.error(error);
						out.write(error);
					}
					catch (AddDataException e) {
						String error = "Couldn't add data.\n\tREASON: " + e;
						logger.error(error);
						out.write(error);
					}
					statementNumber++;
				}
			}
			else {
				// Nothing needs to be done
				//logger.warn("Tables which do not have an equivalent XML Element (e.g. choice tables) must be handled differently.");
			}

			g.removeVertex(elementName);
			elementNameToElementCollection.remove(elementName);

			/*System.out.println("Nodes: " + g.vertexSet() + "(" + g.vertexSet().size() + ")");
			System.out.println("Edges: " + g.edgeSet() + "(" + g.edgeSet().size() + ")");*/
		}
		//System.out.println();
		out.write("Wrote " + sucesses + " out of " + elementCount + " elements successfully.");
	}

	/**
	 * @param elementsToGlobalIds - Element to Integer
	 * @return
	 */
	private String printMap(Map elementsToGlobalIds) {
		String s = "";
		for (Iterator iter = elementsToGlobalIds.keySet().iterator(); iter.hasNext();) {
			Element element = (Element) iter.next();
			Integer i = (Integer) elementsToGlobalIds.get(element);
			s += i.intValue() + " <-- " + element.getName() + element.getAttributes() + "\n";
		}

		return s;
	}

	/**
	 * @param element - an XML element
	 */
	private void translateElement(Element element) throws AddMetaDataException, AddDataException {

		Parent parentElement = element.getParent();
		int elementId = ((Integer) elementsToGlobalIds.get(element)).intValue();
		if (parentElement instanceof Document) {
			dataPopulator.populateDatabaseWithRoot(element, elementId);
		}
		else {
			int parentId = ((Integer) elementsToGlobalIds.get(parentElement)).intValue();
			int order = ((Integer) elementsToLocalOrders.get(element)).intValue();
			dataPopulator.populateDatabaseWithElement(element, elementId, parentId, order);
		}
	}

	/**
	 * @return - A map from element name (String) to Collection(of Jdom elements with that name)
	 */
	private Map getDocumentElements() {
		Map m = new HashMap();

		// traverse the whole document and add the elements to the correct collection
		Element root = xmlDocument.getRootElement();
		addElement(root, m);
		elementsToLocalOrders.put(root, new Integer(1));
		// This needs to be a list so that we retain the order in which the elements appear
		List roots = new LinkedList();
		roots.add(root);
		m.put(root.getName(), roots);
		addChildElements(root, m);

		return m;
	}

	/**
	 * Primary Helper method
	 * @param root
	 * @param m
	 */
	private void addChildElements(Element parentElement, Map m) {
		List children = parentElement.getChildren();
		int i = 1;
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			Element childElement = (Element) iter.next();
			addElement(childElement, m);
			addChildElements(childElement, m);
			elementsToLocalOrders.put(childElement, new Integer(i));
			i++;
		}
	}

	/**
	 * Secondary Helper method
	 * @param childElement
	 * @param m
	 */
	private void addElement(Element childElement, Map m) {
		if (m.keySet().contains(childElement.getName())) {
			((List) m.get(childElement.getName())).add(childElement);
		}
		else {
			// This needs to be a list so that we retain the order in which the children appear
			List l = new LinkedList();
			l.add(childElement);
			m.put(childElement.getName(), l);
		}
		int elementsId = dataPopulator.getGlobalId();
		elementsToGlobalIds.put(childElement, new Integer(elementsId));

	}

	/**
	 * @return
	 */
	private String getAssociatedSchema() throws NoAssociatedSchemaException {
		Element root = xmlDocument.getRootElement();

		List attributeNamesToValues = root.getAttributes();
		for (Iterator iter = attributeNamesToValues.iterator(); iter.hasNext();) {
			Attribute a = (Attribute) iter.next();
			if (a.getName().equals("schemaLocation")) {
				String schemaLocationAttributeValue = a.getValue();
				String[] locationComponents = schemaLocationAttributeValue.split(" ");
				return locationComponents[locationComponents.length - 1];
			}
		}
		logger.error("No schema has been associated with current xml document");
		throw new NoAssociatedSchemaException("Coudln't find associated XML Schema for document" + xmlDataFile.getName());
		/*
		THIS DOESN'T WORK - PERHAPS A BUG WITH JDOM
		 String schemaLocationAttributeValue = root.getAttributeValue("schemaLocation");	
		 if(schemaLocationAttributeValue == null) {
			logger.error("No schema has been associated with current xml document");
		}
		 //The attribute will be of the form xsi:schemaLocation="http://www.w3schools.com schemaFile.xsd" 
		String[] locationComponents = schemaLocationAttributeValue.split(" ");
		 */
	}

}
