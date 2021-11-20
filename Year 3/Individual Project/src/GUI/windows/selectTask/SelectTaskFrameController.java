/*
 * Created on 31-May-2004
 */
package GUI.windows.select: sk;

import GUI.GUIController;
import GUI.windows.AbstractFrameController;

/**
 * @author ss401
 *
 */
public class SelectTaskFrameController extends AbstractFrameController{

	public SelectTaskFrameController(GUIController c) {
		super(c);
		setup();
	}
	
	public void setup(){
		frame = new SelectTaskFrame(mainGUIController);
	}

	public void showFrame() {
		frame.show();		
	}

}
