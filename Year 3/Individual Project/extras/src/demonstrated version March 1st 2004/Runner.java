import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import rdb.RDBBuilder;

import xer.XERBuilder;
import xer.XERModel;
/*
 * Created on 16-Feb-2004
 *
 */

/**
 * @author ss401
 * Root class to run the application
 */

public class Runner {
	static Logger logger = Logger.getRootLogger();

	public static void main(String[] args) {

		BasicConfigurator.configure();
		logger.setLevel(Level.ALL);
		String schemaURL = "schemas/purchaseOrder.xsd";
		XERBuilder xerBuilder = new XERBuilder(schemaURL);
		XERModel xerModel = xerBuilder.build();
		RDBBuilder rdbBuilder = new RDBBuilder(xerModel,schemaURL);
		String sql = rdbBuilder.build();
		System.out.println(sql);
		
	}
}
