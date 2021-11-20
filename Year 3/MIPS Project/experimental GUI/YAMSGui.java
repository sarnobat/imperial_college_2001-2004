//ckage yams.src.yams;

/*
 * Created on 21-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.UIManager;

import GUI.DataFrame;
import GUI.DialogFrame;
import GUI.ExecutionControlsFrame;
import GUI.FileSelectorFrame;
import GUI.ProgramCodeFrame;
import GUI.RegistersFrame;
import GUI.StatisticsFrame;

/**
 * @author ss401
 *
 */
public class YAMSGui {
	
	private static int leftWidth = 200;
	private static int centreWidth = 400;
	private static int rightWidth = 200;
	
	//height of root window = 800
	private static int fileListHeight =  500;
	private static int statisticsHeight =  300;
	
	private static int registersHeight = 400;
	private static int dataHeight =  200;
	private static int errorsHeight =  200;
	
	private static int execControlHeight = 200;
	private static int codeHeight = 600;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch (Exception e) {
			//System.out.println("Couldn't set look and feel. Default will be used." + e);
		}
		JFrame frame = new JFrame("Yet Another MIPS Simulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = frame.getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.X_AXIS));

		JMenuBar menu = new JMenuBar();
		menu.add("File", new JLabel("File2"));
		menu.add("View", new JLabel("View2"));
		menu.add("Help", new JLabel("Help2"));
		frame.setJMenuBar(menu);

		JPanel left = addPanel(c);
		JPanel centre = addPanel(c);
		JPanel right = addPanel(c);

		left.add(new FileSelectorFrame(leftWidth,fileListHeight));
		left.add(new StatisticsFrame(leftWidth,statisticsHeight));
		centre.add(new RegistersFrame(centreWidth,registersHeight));
		centre.add(new DataFrame(centreWidth,dataHeight));
		centre.add(new DialogFrame(centreWidth,errorsHeight));
		right.add(new ExecutionControlsFrame(rightWidth,execControlHeight));
		right.add(new ProgramCodeFrame(rightWidth,codeHeight));

		c.add(left);
		c.add(centre);
		c.add(right);

		frame.setSize(600, 600);
		//frame.pack();
		frame.show();

	}
	private static JPanel addPanel(Container c) {
		JPanel p = new JPanel();

		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		c.add(p);
		return p;

	}
}
