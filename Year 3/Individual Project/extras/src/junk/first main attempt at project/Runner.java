import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logg= ;
/*
 * Created on 16-Feb-2004
 *
 */

/**
 * @author ss401
 *
 */

public class Runner {
	static Logger logger = Logger.getRootLogger();

	public static void main(String[] args) {
		BasicConfigurator.configure();
		logger.setLevel(Level.ALL);
		String schemaURL = "schemas/purchaseOrder.xsd";
		XERBuilder builder = new XERBuilder();
		builder.build(schemaURL);
		
		//XSDModelInvestigator schemaInvestigator = new XSDModelInvestigator(schemaURL);
		
		
		
	}
}
