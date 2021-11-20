
/*
 * Created on 21-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package yams;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import yams.GUI.*;
import yams.assembler.*;
import yams.processor.*;


public class YAMSGui extends YAMSController {
	
	private static int leftWidth = 200;
	private static int centreWidth = 400;
	private static int rightWidth = 300;
	private static int width = leftWidth + centreWidth + rightWidth;
	private static int height = 800;

	//height of root window = 800
	private static int fileListHeight = 500;
	private static int statisticsHeight = 300;

	private static int registersHeight = 400;
	private static int dataHeight = 200;
	private static int dialogHeight = 200;

	private static int execControlHeight = 200;
	private static int codeHeight = 600;

	private static FileSelectorFrame files;
	private static StatisticsFrame stats;
	private static RegistersFrame regs;
	private static DataFrame data;
	private static DialogFrame dialog;
	private static ExecutionControlsFrame exec;
	private static ProgramCodeFrame code;
	private static JLabel help;



	public void start(String[] args) {

		// parse command line arguments
		
		Processor processor = new Processor(this, System.in, System.out);
		Assembler asm = new Assembler("Instruction_file.xml", processor.memoryManager);
		
		setUpGui(processor, asm);
	}
	
	
	private void setUpGui(Processor processor, Assembler assembler) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch (Exception e) {
			//System.out.println("Couldn't set look and feel. Default will be used." + e);
		}

		if (!(fileListHeight + statisticsHeight == height)
			&& (registersHeight + dataHeight + dialogHeight == height && (execControlHeight + codeHeight == height))) {
			System.out.println("inconsistent height dimensions; each column should total to height " + height);
			System.exit(-1);
		}

		JFrame frame = new JFrame("Yet Another MIPS Simulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container c = frame.getContentPane();
		JPanel helpPanel = new JPanel();
		JPanel leftCentreRight = new JPanel();
		
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		c.add(leftCentreRight);
		c.add(helpPanel);
		
		leftCentreRight.setLayout(new BoxLayout(leftCentreRight, BoxLayout.X_AXIS));

		frame.setJMenuBar(getMenu());

		JPanel left = addPanel(leftCentreRight);
		JPanel centre = addPanel(leftCentreRight);
		JPanel right = addPanel(leftCentreRight);

		left.add(new FileSelectorFrame(leftWidth,fileListHeight,processor));
		left.add(new StatisticsFrame(leftWidth,statisticsHeight,processor));
		centre.add(new RegistersFrame(centreWidth,registersHeight,processor));
		centre.add(new DataFrame(centreWidth,dataHeight,processor));
		centre.add(new DialogFrame(centreWidth,dialogHeight,processor));
		right.add(new ExecutionControlsFrame(rightWidth,execControlHeight,processor));
		right.add(new ProgramCodeFrame(rightWidth,codeHeight,processor));

		leftCentreRight.add(left);
		leftCentreRight.add(centre);
		leftCentreRight.add(right);
		helpPanel.add(help = new JLabel("dfasklhgdakljgksjdakldasjklfjdasl;"));

		frame.setSize(width, height);
		//frame.pack();
		frame.show();

	}

	private static JPanel addPanel(Container c) {
		JPanel p = new JPanel();

		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		c.add(p);
		return p;

	}
	private static JMenuBar getMenu() {
		JMenuBar menuBar = new JMenuBar();

		/*menu.add(new JLabel("File"));
		menu.add(new JLabel("View"));
		menu.add(new JLabel("Help"));*/

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		JMenuItem open = new JMenuItem("Open");
		fileMenu.add(open);
		OpenListener ol = new OpenListener();
		open.addActionListener(ol);
		JMenuItem close = new JMenuItem("Close");
		fileMenu.add(close);
		JMenuItem exit = new JMenuItem("Exit");
		fileMenu.add(exit);
		JMenuItem preferences = new JMenuItem("Preferences");
		fileMenu.add(preferences);

		JMenu viewMenu = new JMenu("View");
		menuBar.add(viewMenu);
		JMenuItem fs = new JMenuItem("File Selector");
		viewMenu.add(fs);

		return menuBar;

	}

	public static ProgramCodeFrame getCodeFrame() {
		return code;
	}
	public static ExecutionControlsFrame getExecFrame() {
		return exec;
	}
	public static DialogFrame getDialogFrame() {
		return dialog;
	}
	public static DataFrame getDataFrame() {
		return data;
	}
	public static RegistersFrame getRegsFrame() {
		return regs;
	}
	public static StatisticsFrame getStatsFrame() {
		return stats;
	}
	public static FileSelectorFrame getFileSelectorFrame() {
		return files;
	}
	public static JLabel getHelpPanel() {
			return help;
		}

	static class OpenListener implements ActionListener{
		public void actionPerformed(ActionEvent a){
			System.out.println("Selected Open");
			JFileChooser fc = new JFileChooser();
			//fc.setVisible(true);
			int returnVal = fc.showOpenDialog(regs);

			if(returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				int status = files.addFile(file);
				if(status==-1){
					fc.showOpenDialog(regs);
				}
			}				
		}
	}
}
