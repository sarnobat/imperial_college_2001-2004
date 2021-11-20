import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/*
 * Created on 10-Dec-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

/**
 * @author ss401
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class StatisticsVisualizer extends JFrame {
	
	//All sizes of components should automatically adjust to match these dimensions,
	//so these dimensions may be changed at no risk
	public static final int WINDOW_HEIGHT = 330;
	public static final int HEIGHT_BUFFER = 300;
	public static final int WINDOW_WIDTH = 400;
	public static final int WIDTH_BUFFER = 100; 
	public static String preAmble = "<head><STYLE TYPE=text/css>td{padding:0px}body{ font-size: 10px}</STYLE></head>";
	
	public StatisticsVisualizer(){
		//JFrame f = new JFrame("Graphs");
		this.setTitle("Graphs");
		//the height addition is for the text to fit on the page
		
		
		
		JTabbedPane tabbedPane = new JTabbedPane();
		
		LineAccessGraph l = new LineAccessGraph();
		RegisterGraphs r = new RegisterGraphs();
		tabbedPane.addTab("Register usage",r);
		//tabbedPane.addTab("Line execution count",l);
		//tabbedPane.add(new JScrollPane(l));
		tabbedPane.add("tab 1",new JLabel("Hello"));
		//f.geContentPane().add(new JScrollPane(l));
		this.getContentPane().add(tabbedPane);

		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setSize(new Dimension(WINDOW_WIDTH+WIDTH_BUFFER,WINDOW_HEIGHT+HEIGHT_BUFFER));
		
	}
}
