package view.guiCompnents.export;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import model.Model;
import view.guiCompnents.ExportPanel;

/* OF THE FORM:
 * Please select the database from which you wish to export an xml file:
 * 
 * -------- TABLE -------------
 * 
 * -------- TEXT BOX STATING CHIOCE ----------
 * 
 * -------- NEXT BUTTON ----------------
 *
 */

/**
 * 
 * @author ss401
 *
 */
public class SelectDatabaseSchemaPanel extends JPanel {

	Model model;
	Logger logger = Logger.getLogger(this.getClass());

	JLabel requestLabel;
	JTable table;
	TableModel tableModel;
	JTextField text;
	JButton button;
	ExportPanel panel;
	GridBagConstraints c;

	String databaseSchemaName;
	String request = "Please select a database schema from which to export an xml file.";

	boolean ALLOW_ROW_SELECTION = true;

	public SelectDatabaseSchemaPanel(Model model, ExportPanel panel) {
		this.panel = panel;
		this.model = model;

		initGUI();
	}

	public void initGUI() {
		//this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.setLayout(new GridBagLayout());
		this.c = new GridBagConstraints();
		//this.setPreferredSize(new Dimension(300, 300));

		// Initialize components
		this.requestLabel = createLabel();
		this.table = createTable();
		this.button = createButton();

		// Add components to panel
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		this.add(this.requestLabel, c);

		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 1;
		this.add(this.table, c);

		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridy = 2;
		this.add(this.button, c);

	}
	/**
	 * @return - A JLabel asking the user to select a database schema
	 */
	private JLabel createLabel() {
		JLabel l = new JLabel(request);
		return l;
	}

	/**
	 * @return - A JButton to move to the next stage
	 */
	private JButton createButton() {
		JButton button = new JButton("Next");
		//button.setHorizontalAlignment(JButton.LEFT);
		//button.addActionListener();
		button.addActionListener(new ButtonListener());
		return button;
	}

	/**
	 * @return - A JTable of the schemas
	 */
	private JTable createTable() {
		String[] columnNames = { "Schema Name", "Comment" };
		String[][] data = model.getDatabaseSchemas();

		this.tableModel = new SchemaTableModel(data, columnNames);
		JTable table = new JTable(tableModel); //data, columnNames);
		table.setPreferredSize(new Dimension(200,200));
		//table.setPreferredScrollableViewportSize(new Dimension(300, 100));

		//Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		//Add the scroll pane to this panel.
		add(scrollPane);

		// Allow user selection
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel rowSM = table.getSelectionModel();
		rowSM.addListSelectionListener(new TableListener());

		return table;
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

			databaseSchemaName = (String) tableModel.getValueAt(selectedRow, 0);
			logger.info(databaseSchemaName + " schema selected.");
			System.out.println("dhjakhgdlah");
		}

	}

	/**
	 * 
	 * @author ss401
	 * Specifies behaviour of the 'Next' button
	 */
	class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			System.out.println("dfsafda");
			if(databaseSchemaName != null){
				setVisible(false);
				validate();
				repaint();
				panel.schemaSelected(databaseSchemaName);
			}
		}

	}

	class SchemaTableModel extends AbstractTableModel {

		String[][] data;
		String[] columnNames;

		SchemaTableModel(String[][] data, String[] columnNames) {
			// sanity check
			if (data[0].length != columnNames.length) {
				logger.error("Inconsistent data and column names");
			}
			this.data = data;
			this.columnNames = columnNames;
		}

		public int getRowCount() {
			return data.length;
		}
		public int getColumnCount() {
			return columnNames.length;
		}
		
		public Object getValueAt(int rowIndex, int columnIndex) {
			return data[rowIndex][columnIndex];
		}

	}
}
