package yams.GUI;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import yams.*;
import yams.parser.*;
import yams.processor.*;

/**
 * Panel to display statistics (cycles and memory used) and provide a button
 * to display graphs
 */
public class StatisticsPanel extends YamsPanel implements ActionListener {
	
	// window elements
	JLabel lblCycles;
	JLabel lblMemory;
	JButton butGraphs;

	public StatisticsPanel(YAMSGui controller){
		super(controller);
		setTitle("Statistics");
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		lblCycles = new JLabel();
		lblCycles.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblCycles.setText("Number of Cycles: ");
		
		lblMemory = new JLabel();
		lblMemory.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblMemory.setText("Memory Used: ");
		
		butGraphs = new JButton("Graphs");
		butGraphs.setAlignmentX(Component.CENTER_ALIGNMENT);
		butGraphs.addActionListener(this);
		
		this.add(lblCycles);
		this.add(Box.createRigidArea(new Dimension(0,15)));
		this.add(lblMemory);
		this.add(Box.createRigidArea(new Dimension(0,15)));
		this.add(butGraphs);
	}
	
	/**
	 * Update the statistics
	 */
	public void update() {
		// update statistics labels
		Processor p = controller.getProcessor();
		int cycles = p.statisticsManager.getCycles();
		int memory = p.statisticsManager.getMemoryUsed();
		lblCycles.setText("Number of Cycles: " + cycles);
		lblMemory.setText("Memory Used: " + memory);
	}
	
	/**
	 * Display graphs window
	 * @param source LineList containing the source code from which to generate the graph
	 */
	public void displayGraphs(LineList source) {
		StatisticsFrame sf = new StatisticsFrame(controller.getProcessor(), source);
	}
	
	// Button Handler
	public void actionPerformed(ActionEvent e) {
		controller.displayStats();
	}
	
}

