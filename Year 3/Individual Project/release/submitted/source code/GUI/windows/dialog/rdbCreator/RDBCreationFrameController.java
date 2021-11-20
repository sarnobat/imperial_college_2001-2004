/*
 * Created on 31-May-2004
 *
 */
package GUI.windows.dialog.rdbCreator;

import java.io.File;

import javax.swing.JScrollPane;

import xtractor.schemaConverter.xer.XERModel;
import GUI.GUIController;
import GUI.XTractorThread;
import GUI.windows.dialog.AbstractDialogFrameController;

/**
 * @author ss401
 *
 */
public class RDBCreationFrameController extends AbstractDialogFrameController {

	File xsdFile;

	public RDBCreationFrameController(GUIController controller, File xsdFile) {
		super(controller);
		this.xsdFile = xsdFile;

	}

	/**
	 * 
	 */
	public void showFrame() {
		super.frame = new RDBCreationFrame(this, mainGUIController);

		// Dialog Console
		frame.getContentPane().add(dialog);
		JScrollPane jsp = new JScrollPane(dialog);
		frame.getContentPane().add(jsp);

		frame.show();
	}

	/**
	 * @param model
	 */
	public void createDatabase(XERModel model,XTractorThread thread) {		
		thread.requestDatabaseCreation(model,xsdFile,dialogWriter);
	}
}
