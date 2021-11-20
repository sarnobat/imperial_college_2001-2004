/*
 * Created on 01-Jun-2004
 *
 */
package GUI.windows.dialog.dataPopulator;

import GUI.GUIController;
import GUI.windows.dialog.AbstractDialogFrameController;

/**
 * @author ss401
 *
 */
public class DataPopulatorController extends AbstractDialogFrameController {

	/**
	 * @param c
	 */
	public DataPopulatorController(GUIController c) {
		super(c);
	}

	/* (non-Javadoc)
	 * @see GUI.windows.AbstractFrameController#showFrame()
	 */
	public void showFrame() {
		frame = new DataPopulatorFrame(mainGUIController);
		
		frame.getContentPane().add(dialog);
		
		frame.show();
	}


}
