/*
 * Created on 31-May-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package GUI.windows.selectTask;

import GUI.GUIController;
import GUI.windows.AbstractFrameController;

/**
 * @author ss401
 *
 */
public class SelectTaskFrameController extends AbstractFrameController{

	//SelectTaskFrame frame;


	/**
	 * 
	 */
	public SelectTaskFrameController(GUIController c) {
		super(c);
		setup();
	}
	
	public void setup(){
		frame = new SelectTaskFrame(mainGUIController);
	}

	/**
	 * 
	 */
	public void showFrame() {
		frame.show();		
	}

	/**
	 * 
	 */
	public void disposeFrame() {
		frame.dispose();
	}

}
