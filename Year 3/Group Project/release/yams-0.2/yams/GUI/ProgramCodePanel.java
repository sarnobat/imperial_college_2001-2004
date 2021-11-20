
package yams.GUI;

import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import yams.*;
import yams.parser.*;


public class ProgramCodePanel extends YamsPanel {


	// window elements
	private JPanel buttonPanel;
	private JPanel tablePanel;
	
	private JLabel lblStatus;
	
	public JButton butRun;
	public JButton butStep;
	public JButton butStop;
	public JButton butSkipNext;
	
	private JSlider slideSpeed;
	
	private JTable breakPointTable;
	private JScrollPane scrollPane;
	
	private Vector breakPoints = new Vector();
	
	
	public ProgramCodePanel(YAMSGui controller) {
		super(controller);
		
		// create boxlayout
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		GridBagConstraints c = new GridBagConstraints();
		
		// create status label
		lblStatus = new JLabel("Processor Stopped");
		lblStatus.setHorizontalAlignment(JLabel.CENTER);
		
		// create buttonPanel
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		buttonPanel.setBorder(BorderFactory.createTitledBorder("Program Code Execution"));
		
		buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,80));
		
		c.weightx = 0.5;
		c.weighty = 0.5;
		
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridy = 1;
		c.gridheight = 1;
		c.gridwidth = 2;
		buttonPanel.add(lblStatus,c);
		
		c.gridheight = 1;
		c.gridwidth = 1;
		// add some buttons
		butRun = new JButton("Run");
		butStep = new JButton("Step");
		butStop = new JButton("Stop");
		butSkipNext = new JButton("Skip Next Instruction");
		
		// buttons initially disabled
		butRun.setEnabled(false);
		butStep.setEnabled(false);
		butStop.setEnabled(false);
		butSkipNext.setEnabled(false);
		
		// add listeners to buttons
		ProgramCodeButtonController buttonController = new ProgramCodeButtonController(this);
		butRun.addActionListener(buttonController);
		butStep.addActionListener(buttonController);
		butStop.addActionListener(buttonController);
		butSkipNext.addActionListener(buttonController);
		
		// add to panel
		c.gridx = 1;
		c.gridy = 2;
		buttonPanel.add(butRun,c);
		c.gridx = 2;
		c.gridy = 2;
		buttonPanel.add(butStep,c);
		c.gridx = 1;
		c.gridy = 3;
		buttonPanel.add(butStop,c);
		c.gridx = 2;
		c.gridy = 3;
		buttonPanel.add(butSkipNext,c);

		slideSpeed = new JSlider(JSlider.HORIZONTAL, 0,1000,300);
		slideSpeed.addChangeListener(new SlideController(controller));
		slideSpeed.setMajorTickSpacing(100);
		slideSpeed.setMinorTickSpacing(10);
		slideSpeed.setPaintTicks(true);
		slideSpeed.setPaintLabels(true);
		
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 2;
		buttonPanel.add(slideSpeed,c);
		
		this.add(buttonPanel);
		
		// create table panel
		tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout(1,1));
		tablePanel.setBorder(BorderFactory.createTitledBorder("Breakpoints"));
		
		this.add(tablePanel);
	}
	
	/**
	 * Creates a new Table for the program whose LineList has
	 * just been loaded
	 * @param source
	 */
	public void setSourceCode(LineList source) {
		// initalise breakpoint vector
		breakPoints = new Vector();
		for(int i=0; i < source.totalLines(); i++) {
			breakPoints.add(new Boolean(false));
		}
		
		// create a new table
		breakPointTable = new JTable(new BreakPointTable(controller, source, breakPoints));
		TableColumn column = breakPointTable.getColumnModel().getColumn(0);
		column.setMaxWidth(70);
		column.setMinWidth(70);
		
		scrollPane = new JScrollPane(breakPointTable);
		tablePanel.add(scrollPane);
		
		controller.getMainFrame().show();
	}
	
	/**
	 * Reset the breakpoint table ready for a new one to be loaded
	 */
	public void reset() {
		// if the scroll pane exists, remove it
		try {
			tablePanel.remove(scrollPane);
		}
		catch (Exception e) {}
	}
	
	/**
	 * Returns the current value the slider is pointing to
	 * @return slider value
	 */
	public int getSpeed() {
		return slideSpeed.getValue();
	}
	
	/**
	 * Highlights a particular row of the table
	 * @param currentLine
	 */
	public void setCurrentLine(int currentLine) {
		breakPointTable.getSelectionModel().setSelectionInterval(currentLine-1,currentLine-1);
	}
	
	
	/**
	 * Adds or removes a breakpoint from the list of breakpoints
	 * @param line The line which the user has requested to break on
	 * @param value Whether the user wishes to select or unselect a breakpoint
	 */
	public void setBreakPoint(int line, boolean value) {
		breakPoints.set(line-1, new Boolean(value));
	}
	
	/**
	 * Determines whether the given line has a breakpoint enabled
	 * @param line
	 * @return true if a breakpoint is set, false otherwise
	 */
	public boolean getBreakPoint(int line) {
		return ((Boolean)breakPoints.get(line-1)).booleanValue();
	}
	
	/**
	 * Resets all breakpoints to false
	 *
	 */
	public void resetBreakpoints() {
		for(int i=0; i< breakPoints.size(); i++) {
			breakPoints.set(i, new Boolean(false));
		}
	}
	
	/**
	 * Sets the text of the label of the processor status to the
	 * supplied string
	 * @param message
	 */
	public void setProcessorStatus(String message) {
		lblStatus.setText(message);
	}
	


}



/**
 * Action Listener which handles clicks on execution control buttons
 *
 */
class ProgramCodeButtonController implements ActionListener {
	
	private static ProgramCodePanel codePanel;
	
	ProgramCodeButtonController(ProgramCodePanel p) {
		codePanel = p;
	}
	
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton)e.getSource();
		
		if (source == codePanel.butRun) {
			codePanel.controller.processorStart();
		} else if (source == codePanel.butStep) {
			codePanel.controller.processorStep();
		} else if (source == codePanel.butSkipNext) {
			codePanel.controller.processorSkip();
		} else if (source == codePanel.butStop) {
			codePanel.controller.processorStop();
		}
		
		
	}
	
}

/**
 * Listens to state changes of the JSlider in the specified YAMSGui
 */
class SlideController implements ChangeListener {

	YAMSGui controller;
	
	SlideController(YAMSGui c) {
		controller = c;
	}

	public void stateChanged(ChangeEvent e) {
		// update processor handler speed
		JSlider slide = (JSlider)e.getSource();
		controller.setProcessorSpeed(slide.getValue());
	}
	
}


/**
 * Table model for the Breakpoint tablea
 */
class BreakPointTable extends AbstractTableModel {
	private String[] columnNames = {"Breakpoint", "Instruction"};
		
	YAMSGui controller;
	LineList data;
	Vector breakPoints;
	
	BreakPointTable(YAMSGui c, LineList source, Vector b) {
		controller = c;
		data = source;
		breakPoints = b;
		this.addTableModelListener(new BreakPointTableController(controller, breakPoints));
	}
		
	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.totalLines();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		if (col == 0) {
			return breakPoints.get(row);
		} else {
			return data.getLine(row+1).getOriginal();
		}
	}
		
	public void setValueAt(Object value, int row, int col) {
		if (col == 0) {
			breakPoints.setElementAt(value, row);
			fireTableCellUpdated(row, col);
		}
	}

	public Class getColumnClass(int c) {
		return getValueAt(0,c).getClass();
	}
		
	public boolean isCellEditable(int row, int col) {
		if (col == 0) {
			return true;
		} else {
			return false;
		}
	}

}



/**
 * Listens for any changes to the internal table model, and updates
 * the JTable display to reflect these changes 
 */
class BreakPointTableController implements TableModelListener {
	
	YAMSGui controller;
	Vector breakpoints;
	
	BreakPointTableController(YAMSGui c, Vector b) {
		controller = c;
		breakpoints = b;
	}

	
	public void tableChanged(TableModelEvent e) {
		// ensure currently executing line is re-selected
		controller.updateBreakPointPanel();
	}
	
}