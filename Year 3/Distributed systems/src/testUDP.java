import junit.framework.TestCase;

/*
 * Created on 13-Nov-2003
 */

/**
 * @author ss401
 *
 * public class testUDP extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		/*String[]  args = {"8000","8001"};
		UDPServer.main(args);*/
		String[]  args2 = {"cpu1.doc.ic.ac.uk","8002","8003","6","Goodbye"};
		UDPClient.main(args2);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	public void testDummY(){
		assertTrue(true);
	}
}
