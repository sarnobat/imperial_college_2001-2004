/*
 * Created on 26-May-2004
 *
 */
package xtractor.dataImporter.xmlReader.dataPopulator;

import java.util.Comparator;

import org._3pq.jgrapht.DirectedGraph;

/**
 * @author ss401
 *
 */
public class VertexDegreeComparator implements Comparator {
DirectedGraph g;
	public VertexDegreeComparator(DirectedGraph g) {
		this.g = g;
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		int o1Edges = this.g.outgoingEdgesOf(o1).size();
		int o2Edges = this.g.outgoingEdgesOf(o2).size();
		if(o1Edges == o2Edges){
			return 0;
		}
		else if(o1Edges > o2Edges){
			return 1;
		}
		else{
			return -1;
		}
		
	}

}
