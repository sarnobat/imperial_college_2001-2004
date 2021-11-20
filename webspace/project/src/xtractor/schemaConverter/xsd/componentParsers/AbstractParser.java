/*
 * Created on 16-Mar-2004
 *
 */
package 
xtractor.schemaConverter.xsd.componentParsers;

import org.apache.log4j.Logger;

import xtractor.schemaConverter.xer.XERBuilder;
import xtractor.schemaConverter.xer.XERFactory;
import xtractor.schemaConverter.xsd.XSDReader;

/**
 * @author ss401
 * Creates an XERConstruct from an XML Schema construct
 * Note that the parsers don't do everything. The 'add' methods in the XERCompoundConstruct
 * are responsible for creating implicit relationships between entities 
 *
 */
public abstract class AbstractParser {
	
	final int UNBOUNDED = 999999;

	Logger logger = Logger.getLogger(this.getClass());
		
	XSDReader schemaWalker;
	protected XERBuilder xerBuilder;
	XERFactory xerFactory;

	public AbstractParser(XSDReader schemaWalker, XERBuilder builder) {
		this.schemaWalker = schemaWalker;
		this.xerBuilder = builder;
		this.xerFactory = builder.getXERFactory();
	}
	
	/**
	 * This is for <xsd:attribute use="required"> etc. (separate one for minOccurs/maxOccurs)
	 * Converts attribute usage expressed in XML schema terminology into numbers
	 * @param use - "required","optional" etc.
	 * @return - The minimum number of times the attribute must occur
	 */
	protected int resolveUseCardinality(String use) {
		if (use == null) {
			//Presumalby this is correct
			return 1;
		}
		else if (use.equals("required")) {
			return 1;
		}
		else if (use.equals("optional")) {
			return 0;
		}
		else {
			logger.error("Invalid requirement specification");
			return -1;
		}
	}

	/**
	 * This is for <xsd:element minOccurs=? maxOccurs=?>
	 * @param occurs - The numerical value of the minOccurs/maxOccurs attributes (could be null)
	 * @return - A numerical equivalent
	 */
	protected int resolveOccurrenceCardinality(String occurs) {
		if (occurs == null) {
			return 1;
		}
		else if(occurs.equals("unbounded")){
			return this.UNBOUNDED;
		}
		else {
			return Integer.parseInt(occurs);
		}
	}

}
