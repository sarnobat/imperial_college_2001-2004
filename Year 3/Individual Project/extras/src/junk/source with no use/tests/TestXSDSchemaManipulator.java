/*
 * Created on 24-Feb-2004
 *
 */
package tests;

import org.jdom.Element;

import junit.framework.TestCase;
import xsd.JDomUtilities;
import xsd.XSDSchemaManipulator;
import xsd.componentParsers.ElementParser;

/**
 * @author ss401
 *
 */
public class TestXSDSchemaManipulator extends TestCase {
	
	XSDSchemaManipulator schemaWalker;
	ElementParser p;

	/**
	 * Constructor for TestXSDSchemaManipulator.
	 * @param arg0
	 */
	public TestXSDSchemaManipulator(String arg0) {
		//schemaWalker = new XSDSchemaManipulator("../schemas/purchaseOrder.xsd");
		//p = new ElementParser(schemaWalker,);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		Element root = JDomUtilities.getRootElement("../schemas/purchaseOrder.xsd");
		System.out.println(root.getNamespace().getPrefix());
		return;
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDummy(){
		//p.complexTypeIsGlobal("USAddress");
		assertTrue(true);
	}

}
