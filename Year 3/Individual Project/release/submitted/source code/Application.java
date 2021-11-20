import java.io.File;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import xtractor.FolderDiscloser;

import GUI.GUIController;

/*
 * Created on 15-May-2004
 *
 */

/**
 * @author ss401
 * This is the class that is invoked when the application is launched
 */
public class Application {
	static Logger logger = Logger.getRootLogger();
	
	public static String absoluteFolderPath;
	
	public static void main(String[] args) {
		// Initialize logger
		BasicConfigurator.configure();
		logger.setLevel(Level.OFF);

		File f = new File(".");		
		absoluteFolderPath = f.getAbsolutePath().substring(0, f.getAbsolutePath().length() - 1);
		System.out.println(absoluteFolderPath);
		
		FolderDiscloser.workingFolderPath = absoluteFolderPath; 

		GUIController g = new GUIController();
	}
	
	/**
	 * 
	 * @return - The absolute path of the root folder where the application is being run
	 */
	public static String getRootFolderPath(){
		return absoluteFolderPath;
	}

}
