/*
 * Created on 01-Jun-2004
 *
 */
package GUI.windows;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

import GUI.GUIController;

/**
 * @author ss401
 *
 */
public abstract class AbstractFrameController {
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	protected JFrame frame;

	protected GUIController mainGUIController;
	/**
	 * 
	 */
	public AbstractFrameController(GUIController c) {
		this.mainGUIController = c;
	}
	
	public abstract void showFrame();
	
	public abstract void disposeFrame();

}
