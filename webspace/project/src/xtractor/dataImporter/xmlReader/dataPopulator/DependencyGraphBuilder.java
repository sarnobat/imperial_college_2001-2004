/*
 * Created on 26-May-2004
 *
 */
package xtractor.dataImporter.xmlReader.dataPopulator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.graph.DefaultDirectedGraph;
import org.apache.log4j.Logger;

import xtractor.dataImporter.xmlReader.dataPopulator.databaseManager.GraphDatabaseManager;
/**
 * @author ss401
 *
 */
public class DependencyGraphBuilder {

	Logger logger = Logger.getLogger(this.getClass());

	//String schemaName;
	GraphDatabaseManager databaseManager;

	/**
	 * @param databaseSchemaName
	 */
	public DependencyGraphBuilder(String databaseSchemaName) {
		//this.schemaName = databaseSchemaName;
		this.databaseManager = new GraphDatabaseManager(databaseSchemaName);
	}

	/**
	 * @return - A graph of all connetions between elements (amalgamating meta and data tables)
	 * Note that duplicate edges are ignored. This may be a problem later
	 */
	//THIS WON'T WORK BECAUSE YOU'RE NOT CAPTURING ALL OF THE ELEMENTS
	public DirectedGraph build() {

		Map references = databaseManager.getEdges();
		Set nodes = new HashSet();

		// Add all the nodes
		Collection foreignElementCollections = references.values();
		for (Iterator iter = foreignElementCollections.iterator(); iter.hasNext();) {
			Collection foreignElement = (Collection) iter.next();
			nodes.addAll(foreignElement);
		}

		DirectedGraph g = new DefaultDirectedGraph();
		g.addAllVertices(nodes);

		// Add all edges
		Collection referringElements = references.keySet();
		g.addAllVertices(referringElements);
		for (Iterator iter = referringElements.iterator(); iter.hasNext();) {
			String referringElement = (String) iter.next();
			Collection foreignElements = (Collection) references.get(referringElement);
			for (Iterator iterator = foreignElements.iterator(); iterator.hasNext();) {
				String foreignElement = (String) iterator.next();
				g.addEdge(referringElement, foreignElement);
			}
		}
		/*System.out.println(g);
		System.out.println("Number of nodes: " + g.vertexSet().size());
		System.out.println("Number of edges: " + g.edgeSet().size());*/
		
		return g;

	}

}
