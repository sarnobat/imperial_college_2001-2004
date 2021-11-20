/*
 * Created on 01-Jun-2004
 *
 */
package GUI.windows.selectDocument;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import GUI.GUIController;
import GUI.windows.AbstractFrameController;

/**
 * @author ss401
 *
 */
public class SelectDocumentController extends AbstractFrameController {

	int selectedDocumentNumber = -1;
	String databaseSchemaName;
	TableModel tableModel;
	JTextField selectedDocumentField;

	public SelectDocumentController(GUIController c, String databaseSchemaName) {
		super(c);
		this.databaseSchemaName = databaseSchemaName;
		showFrame();
	}

	/* (non-Javadoc)
	 * @see GUI.windows.AbstractFrameController#showFrame()
	 */
	public void showFrame() {
		frame = new SelectDocumentFrame(mainGUIController);
		Container pane = frame.getContentPane();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		JLabel label = new JLabel("Please select the document you wish to regenerate");
		c.gridx = 0;
		c.gridy = 0;
		pane.add(label,c);

		JScrollPane tablePane = getTablePane();
		tablePane.setPreferredSize(new Dimension(200,200));
		c.gridx = 0;
		c.gridy = 1;
		pane.add(tablePane,c);

		selectedDocumentField = new JTextField();
		selectedDocumentField.setPreferredSize(new Dimension(50,20));
		c.gridx = 0;
		c.gridy = 2;
		pane.add(selectedDocumentField,c);

		JButton confirm = new JButton("Regenerate XML File");
		confirm.addActionListener(new ButtonListener());
		c.gridx = 0;
		c.gridy = 3;
		pane.add(confirm,c);
		
		frame.show();

	}

	/**
	 * @return
	 */
	private JScrollPane getTablePane() {
		// Table of database schemas
		String[] columnNames = { "Document Number" };
		String[][] data = mainGUIController.getDocuments(databaseSchemaName);
		tableModel = new DefaultTableModel(data, columnNames);
		JTable table = new JTable(data, columnNames);

		// Allow user selection
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel rowSM = table.getSelectionModel();
		rowSM.addListSelectionListener(new TableListener());

		JScrollPane jsp = new JScrollPane(table);
		return jsp;
	}

	

	/**
	 * 
	 * @author ss401
	 * Specifies the behaviour of the JTable
	 */
	class TableListener implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent e) {
			ListSelectionModel lsm = (ListSelectionModel) e.getSource();
			int selectedRow = lsm.getMinSelectionIndex();
			selectedDocumentNumber = Integer.parseInt((String) tableModel.getValueAt(selectedRow, 0));
			logger.info(selectedDocumentNumber + " schema selected.");

			selectedDocumentField.setText(Integer.toString(selectedDocumentNumber));
		}
	}

	class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (selectedDocumentNumber > -1) {
				mainGUIController.launchExportDocumentDialog(databaseSchemaName,selectedDocumentNumber);
			}
		}

	}
}
