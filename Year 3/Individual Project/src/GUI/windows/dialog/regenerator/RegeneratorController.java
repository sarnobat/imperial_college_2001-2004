/*
 * Created on 01-Jun-2004
 *
 */
package GUI.windows.dialog.regenerator;

import javax.swing.JScrollPane;

import GUI.GUIController;
import GUI.windows.dialog.AbstractDialogFrameController;

/**
 * @author ss401
 *
 */
public class RegeneratorController extends AbstractDialogFrameController {

	/**
	 * @param c
	 */
	public RegeneratorController(GUIController c) {
		super(c);
		
		showFrame();
		
	}

	/* (non-Javadoc)
	 * @see GUI.windows.AbstractFrameController#showFrame()
	 */
	public void showFrame() {
		frame = new RegeneratorDialog(mainGUIController);

		// Dialog Console
		frame.getContentPane().add(dialog);
		JScrollPane jsp = new JScrollPane(dialog);
		frame.getContentPane().add(jsp);
		
		frame.show();
	}


}
