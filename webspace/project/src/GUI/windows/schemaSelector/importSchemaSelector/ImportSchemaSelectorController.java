/*
 * Created on 01-Jun-2004
 *
 */
package GUI.windows.schemaSelector.importSchemaSelector;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;

import GUI.GUIController;
import GUI.windows.schemaSelector.AbstractSchemaSelectorController;
import GUI.windows.schemaSelector.AbstractSelectSchemaFrame;

/**
 * @author ss401
 *
 */
public class ImportSchemaSelectorController extends AbstractSchemaSelectorController {

	File xmlFile;

	/**
	 * @param mainGUIController
	 * @param xmlFile
	 */
	public ImportSchemaSelectorController(GUIController mainGUIController, File xmlFile) {
		super(mainGUIController);
		this.xmlFile = xmlFile;
	}

	/* (non-Javadoc)
	 * @see GUI.windows.schemaSelector.AbstractSchemaSelectorController#getLabel()
	 */
	protected JLabel createLabel() {
		return new JLabel("Please select the database schema you wish to import the XML file's data to.");
	}

	/* (non-Javadoc)
	 * @see GUI.windows.schemaSelector.AbstractSchemaSelectorController#getButton()
	 */
	protected JButton createButton() {
		JButton b = new JButton("Import");
		b.addActionListener(new ButtonListener());
		return b;
	}

	/* (non-Javadoc)
	 * @see GUI.windows.schemaSelector.AbstractSchemaSelectorController#getFrame()
	 */
	protected AbstractSelectSchemaFrame createFrame() {
		return new ImportSchemaSelectorFrame(mainGUIController, this, xmlFile);
	}

	class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (selectedSchemaName != null) {
				mainGUIController.launchImportDialog(selectedSchemaName, xmlFile);
			}
		}
	}
}
