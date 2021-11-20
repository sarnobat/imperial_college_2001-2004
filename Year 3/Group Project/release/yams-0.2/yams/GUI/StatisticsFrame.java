package yams.GUI;

import java.awt.Dimension;

import javax.swing.*;

//import yams.GUI.graphs.InstructionUsageGraphs;
import yams.GUI.graphs.LineAccessGraphs;
import yams.GUI.graphs.RegisterGraphs;
import yams.parser.LineList;
import yams.processor.*;

/**
 * Separate window containing two graphs, separated by tabbed panels  
 */
public class StatisticsFrame extends JFrame {

	// window elements
	private JTabbedPane tabbedPane;
	private JPanel registerPanel;
	private JPanel lineAccessPanel;
	private JPanel instructionUsagePanel;
	
	public final int WINDOW_HEIGHT = 330;
	public final int HEIGHT_BUFFER = 300;
	public final int WINDOW_WIDTH = 300;
	public final int WIDTH_BUFFER = 100; 
	public String preAmble = "<head><STYLE TYPE=text/css>.regname{valign: top;}td{padding:0px; text-align: center}body{ font-size: 10px}</STYLE></head>";

	public StatisticsFrame(Processor p,LineList l) {
				
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		tabbedPane = new JTabbedPane();
		registerPanel = new RegisterGraphs(this,p);
		lineAccessPanel = new LineAccessGraphs(this,p,l);
//		instructionUsagePanel = new InstructionUsageGraphs(this,p);
		
		tabbedPane.addTab("Register Usage", registerPanel);
		tabbedPane.add("Line Access Frequency", lineAccessPanel);
		tabbedPane.addTab("Instruction Usage",instructionUsagePanel);
		
		this.setTitle("Statistics Graphs");
		this.setResizable(false);
		this.setSize(new Dimension(WINDOW_WIDTH+WIDTH_BUFFER,WINDOW_HEIGHT+HEIGHT_BUFFER));
		
		this.getContentPane().add(tabbedPane);
		
		this.pack();
		
		this.show();
		
	}

}
