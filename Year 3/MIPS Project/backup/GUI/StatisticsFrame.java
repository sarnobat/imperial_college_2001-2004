/*
 * Created on 21-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package yams.GUI;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import yams.processor.*;

/**
 * @author ss401
 *
 */
public class StatisticsFrame extends YamsInternalFrame {

	public StatisticsFrame(int width,int height, Processor processor){
		super(width,height,processor);
		setTitle("Statistics");
		
		int time = 5;
		int instructionCount = 20000;
		int regCount = 45;
		
		//TODO: Can you have a JLabel that spans multiple lines?
		/*JLabel l = new JLabel(
			"Time for program to run = " + time + " light years\n" +
			"\nNumber of instructions = " + instructionCount + 
			"\nRegisters used = " + regCount+
			"Conclusion: get a new compiler.");*/
		
		/*JLabel l1 = new JLabel("Time for program to run = " + time + " light years");
		JLabel l2 = new JLabel("Number of instructions = " + instructionCount);
		JLabel l3 = new JLabel("Registers used = " + regCount);
		JLabel l4 = new JLabel("Conclusion: get a new compiler.");
			
		getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
		getContentPane().add(l1);
		getContentPane().add(l2);
		getContentPane().add(l3);
		getContentPane().add(l4);*/
		
		String display = "<html><table>" +					"<tr><td>Time for program to run</td><td><font color=green>" + time + " light years</font></td></tr>" +					"<tr><td>Number of instructions</td><td><font color=green>" + instructionCount + "</font></td></tr>" +
					"<tr><td>Registers used</td><td><font color=green>" + regCount + "</font></td></tr>"+
				"</table></html>";
		JLabel l1 = new JLabel(display);
		getContentPane().add(l1); 
	}

}
