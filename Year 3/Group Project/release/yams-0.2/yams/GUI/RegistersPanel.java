
package yams.GUI;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;

import yams.*;
import yams.processor.*;


public class RegistersPanel extends YamsPanel {


	// window elements
	RegisterTableModel registerTableModel;
	JPanel tablePanel;
	JTable registerTable;
	JScrollPane scrollPane;
	
	
	public RegistersPanel(YAMSGui controller) {
		super(controller);
		setTitle("Register Contents");
		
		// initisation must be done AFTER processor is created
	}
	
	/**
	 * Creates the table which displays the contents of the registers
	 * in the newly created processor
	 */
	public void init() {
		this.setLayout(new GridLayout());

		tablePanel = new JPanel();
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.LINE_AXIS));
		
		registerTableModel = new RegisterTableModel(controller);
		registerTable = new JTable(registerTableModel);
		
		TableColumn column;
		
		column = registerTable.getColumnModel().getColumn(0);
		column.setMaxWidth(70);
		column.setMinWidth(70);
		
		scrollPane = new JScrollPane(registerTable);
		
		tablePanel.add(scrollPane);
		
		this.add(tablePanel);
		
		controller.getMainFrame().show();
		
	}
	
	/**
	 * Updates the table cell with the given register to reflect it's change in value
	 * @param regID The register that has changed in value
	 */
	public void regChanged(String regID) {
		registerTableModel.regChanged(regID);
	}
}


/**
 * Table model for the register table
 */
class RegisterTableModel extends AbstractTableModel {
		
	private String[] columnNames = { "Register", "Value" };
		
	private String[] registerNames = {
		"PC", "LO", "HI", "$zero", "$at", "$v0", "$v1", "$a0",
		"$a1", "$a2", "$a3", "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6",
		"$t7", "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6",
		"$s7", "$t8", "$t9", "$k0", "$k1", "$gp", "$sp", "$fp", "$ra"
	};
		
	protected Processor processor;
		
	RegisterTableModel(YAMSGui controller) {
		processor = controller.getProcessor();
	}
		
	public void regChanged(String reg) {
		fireTableDataChanged();
	}
		
	public String getColumnName(int col) {
		return columnNames[col].toString();
	}
			
	public int getRowCount() {
		return 35;
	}
		
	public int getColumnCount() {
		return columnNames.length;
	}
		
	public Object getValueAt(int row, int column) {
		if (column == 1) {
			int regValue;
			switch(row) {
				case 0:
					regValue = processor.registerManager.getRegOnly("PC");
					break;
				case 1:
					regValue = processor.registerManager.getRegOnly("LO");
					break;
				case 2:
					regValue = processor.registerManager.getRegOnly("HI");
					break;
				default:
					regValue = processor.registerManager.getRegOnly(row-3);
					break;
			}
			String value = ("00000000" + Integer.toHexString(regValue).toUpperCase());
			return "0x" + value.substring(value.length() - 8);
		} else {
			return registerNames[row];
		}
	}
		
	
}
