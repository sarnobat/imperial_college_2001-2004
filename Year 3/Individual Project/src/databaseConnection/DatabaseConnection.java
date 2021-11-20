package databaseConnection;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import xtractor.FolderDiscloser;

/*
 * Created on 17-Mar-2004
 *  
 */

/**
 * @author ss401
 *  
 */

public class DatabaseConnection {
	static Logger logger = Logger.getLogger("DatabaseConnection");
	static Connection conn;
	static File connectionConfig = new File(FolderDiscloser.workingFolderPath+ "/config/databaseConnection.xml");

	public static Connection getConnection() {

		if (conn == null) {
			try {
				//Class.forName("org.postgresql.Driver");
				Class.forName(getDriver());
				//conn = DriverManager.getConnection("jdbc:postgresql://db/ss401", "ss401", "ss401");
				conn = DriverManager.getConnection(getURL(), getUsername(), getPassword());

			}
			catch (ClassNotFoundException e) {
				logger.fatal("Couldn't locate database driver." + e);
				System.exit(-1);
			}
			catch (SQLException e) {
				logger.fatal("Couldn't connect to database." + e);
				System.exit(-1);
			}
		}
		return conn;
	}

	public static void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
			}
			catch (SQLException e) {
				System.err.println("Couldn't close connection");
			}
		}
	}

	private static Element getRootElement() {
		SAXBuilder builder = new SAXBuilder();
		try {
			Document d = builder.build(connectionConfig);
			return d.getRootElement();
		}
		catch (JDOMException e) {
			logger.error("Couldn't build JDOM model." + e);
		}
		catch (IOException e) {
			logger.error("Couldn't read config file." + e);
		}
		return null;

	}

	public static String getDriver() {
		Element root = getRootElement();
		return root.getChild("driver").getTextTrim();
	}
	public static String getURL() {
		Element root = getRootElement();
		return root.getChild("url").getTextTrim();
	}
	public static String getUsername() {
		Element root = getRootElement();
		return root.getChild("username").getTextTrim();
	}
	public static String getPassword() {
		Element root = getRootElement();
		return root.getChild("password").getTextTrim();
	}
}