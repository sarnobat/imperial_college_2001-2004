/*
 * Created on 01-Jun-2004
 *
 */
package GUI.windows.schemaSelector.exportSchemaSelector;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

import GUI.GUIController;
import GUI.windows.schemaSelector.AbstractSchemaSelectorController;
import GUI.windows.schemaSelector.AbstractSelectSchemaFrame;

/**
 * @author ss401
 *
 */
public class ExportSchemaSelectorController extends AbstractSchemaSelectorController {

	/**
	 * @param mainGUIController
	 * @param xmlFile
	 */
	public ExportSchemaSelectorController(GUIController mainGUIController) {
		super(mainGUIController);

	}

	/* (non-Javadoc)
	 * @see GUI.windows.schemaSelector.AbstractSchemaSelectorController#getLabel()
	 */
	protected JLabel createLabel() {
		return new JLabel("Please select the relational database schema containing the document you wish to regenerate.");
	}

	/* (non-Javadoc)
	 * @see GUI.windows.schemaSelector.AbstractSchemaSelectorController#getButton()
	 */
	protected JButton createButton() {
		JButton b = new JButton("Export Data");
		b.addActionListener(new ButtonListener());
		return b;
	}

	/* (non-Javadoc)
	 * @see GUI.windows.schemaSelector.AbstractSchemaSelectorController#getFrame()
	 */
	protected AbstractSelectSchemaFrame createFrame() {
		return new ExportSchemaSelectorFrame(mainGUIController, this);
	}

	class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (selectedSchemaName != null) {
				mainGUIController.launchExportDocumentSelector(selectedSchemaName);
			}
		}

	}
}
