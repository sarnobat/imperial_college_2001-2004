/*
 * Created on 16-Mar-2004
 *
 */
package 
xtractor.schemaConverter.xsd.compone< Parsers;

import xtractor.schemaConverter.xer.XERBuilder;
import xtractor.schemaConverter.xsd.XSDSchemaManipulator;

/**
 * @author ss401
 *
 */
public abstract class AbstractParser {
	XSDSchemaManipulator schemaWalker;
	XERBuilder xerBuilder;

	public AbstractParser(XSDSchemaManipulator schemaWalker, XERBuilder builder) {
		this.schemaWalker = schemaWalker;
		this.xerBuilder = builder;
	}

}
