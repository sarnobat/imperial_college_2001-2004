/*
 * Created on 17-Jun-2004
 *
 */
package GUI.windows.schemaSelector.viewDBSchemaSelector;

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
public class ViewDBSchemaSelectorController extends AbstractSchemaSelectorController {

	/**
	 * @param mainGUIController
	 */
	public ViewDBSchemaSelectorController(GUIController mainGUIController) {
		super(mainGUIController);
	}

	protected JLabel createLabel() {
		JLabel label = new JLabel("Select the Relational Database Schema whose tables you wish to view.");
		return label;
	}

	protected JButton createButton() {
		JButton button = new JButton("View Database");
		button.addActionListener(new ButtonListener());
		return button;
	}

	protected AbstractSelectSchemaFrame createFrame() {
		return new ViewDBSchemaFrame(mainGUIController, this);
	}

	class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (selectedSchemaName != null) {
				mainGUIController.launchDatabaseViewer(selectedSchemaName);
			}
		}

	}
}
