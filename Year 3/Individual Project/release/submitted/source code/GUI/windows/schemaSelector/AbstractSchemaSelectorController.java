/*
 * Created on 01-Jun-2004
 *
 */
package GUI.windows.schemaSelector;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import GUI.GUIController;
import GUI.windows.AbstractFrameController;
import GUI.windows.schemaSelector.components.SchemaTable;

/**
 * @author ss401
 *
 */
public abstract class AbstractSchemaSelectorController extends AbstractFrameController {

	protected Logger logger = Logger.getLogger(this.getClass());
	
	protected String selectedSchemaName;

	// Components
	protected JLabel label;
	protected SchemaTable table;
	protected TableModel tableModel;
	protected JTextField selectedSchema;
	protected JButton confirm;

	/**
	 * @param c
	 */
	public AbstractSchemaSelectorController(GUIController mainGUIController) {
		super(mainGUIController);
		//this.xmlFile = xmlFile;
		
		this.label = createLabel();
		this.confirm = createButton();
		this.frame = createFrame();
		
		showFrame();
	}

	public void showFrame() {
		//this.frame = getFrame();//new AbstractSelectSchemaFrame(mainGUIController, this, xmlFile);

		Container pane = frame.getContentPane();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		//label = getLabel(); //ew JLabel("Select the schema you wish to import the data into");
		c.gridx = 0;
		c.gridy = 0;
		pane.add(label, c);

		JScrollPane scrollPane = createTablePane(); //new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(200, 200));
		c.gridx = 0;
		c.gridy = 1;
		pane.add(scrollPane, c);

		selectedSchema = new JTextField();
		selectedSchema.setPreferredSize(new Dimension(100, 25));
		c.gridx = 0;
		c.gridy = 2;
		pane.add(selectedSchema, c);

		//confirm = getButton();//new JButton("Import Data");
		//beginImporting.addActionListener(new ButtonListener());
		c.gridx = 0;
		c.gridy = 3;
		pane.add(confirm, c);

		frame.show();
	}

	protected JScrollPane createTablePane() {
		// Table of database schemas
		String[] columnNames = { "Schema Name" };
		String[][] data = this.getDatabaseSchemas();
		tableModel = new DefaultTableModel(data, columnNames);
		table = new SchemaTable(this, tableModel);

		// Allow user selection
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel rowSM = table.getSelectionModel();
		rowSM.addListSelectionListener(new TableListener());

		JScrollPane jsp = new JScrollPane(table);
		return jsp;

	}

	/**
	 * @return
	 */
	public String[][] getDatabaseSchemas() {
		return mainGUIController.getDatabaseSchemas();

	}

	protected abstract JLabel createLabel();

	protected abstract JButton createButton();
	
	protected abstract AbstractSelectSchemaFrame createFrame();

	/**
	 * 
	 * @author ss401
	 * Specifies the behaviour of the JTable
	 */
	class TableListener implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent e) {
			ListSelectionModel lsm = (ListSelectionModel) e.getSource();
			int selectedRow = lsm.getMinSelectionIndex();
			selectedSchemaName = (String) tableModel.getValueAt(selectedRow, 0);
			logger.info(selectedSchemaName + " schema selected.");
			selectedSchema.setText(selectedSchemaName);
		}
	}
	/**
	 * 
	 */
	public void disposeFrame() {
		frame.dispose();		
	}


}
