/*
 * Created on 31-May-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package GUI.windows;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

import GUI.GUIController;
import GUI.common.MenuBar;

/**
 * @author ss401
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class XTractorFrame extends JFrame {

	protected Logger logger = Logger.getLogger(this.getClass());
	protected GUIController guiController;
	protected int height = GUIController.height;
	protected int width = GUIController.width;

	public XTractorFrame(GUIController c) {
		super();
		this.guiController = c;
		try {
			javax.swing.UIManager.setLookAndFeel(
				"com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			logger.error("Couldn't set look and feel.");
		}
		this.setSize(new Dimension(width,height));
		this.setLocation(new Point(GUIController.locationX, GUIController.locationY));
		this.setDefaultCloseOperation(super.EXIT_ON_CLOSE);
		setDefaultLookAndFeelDecorated(true);
		
		this.setTitle("XTractor");		

		// Menu bar
		MenuBar menuBar = new MenuBar(c,this);
		this.setJMenuBar(menuBar);
		
	}
	
	//protected abstract void setup();
}
