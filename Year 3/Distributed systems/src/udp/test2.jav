import junit.framework.TestCase;

/*
 * Created on 15-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

/**
 * @author ss401
 *
 * public class test2 extends TestCase {


	protected void setUp() throws Exception {
		super.setUp();
		String[]  args = {"8000","8001"};
		UDPServer.main(args);
		/*String[]  args2 = {"cpu1.doc.ic.ac.uk","8001","8003","6","Goodbye"};
		UDPClient.main(args2);*/
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	public void testDummY(){
		assertTrue(true);
	}

}
