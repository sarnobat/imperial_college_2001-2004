/*s
 * Created on 24-May-2004
 *
 */
package databaseConnection;
= import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * @author ss401
 *
 */
public class SchemaDisplayTester {

	static Logger logger = Logger.getRootLogger();

	public static void main(String[] args) {

		BasicConfigurator.configure();
		logger.setLevel(Level.ERROR);
		SchemaDisplayer d = new SchemaDisplayer("ss");
		System.out.println(d.getSummary());
	}
}
