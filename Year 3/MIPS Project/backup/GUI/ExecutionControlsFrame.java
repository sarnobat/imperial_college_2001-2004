/*
 * Created on 21-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package yams.GUI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;

import yams.processor.*;
import yams.YAMSGui;

/**
 * @author ss401
 *
 */
public class ExecutionControlsFrame extends YamsInternalFrame {

	/**
	 * 
	 */
<<<<<<< ExecutionControlsFrame.java
	public ExecutionControlsFrame(int width, int height) {
		super(width, height);
=======
	public ExecutionControlsFrame(int width,int height, Processor processor) {
		super(width,height,processor);
>>>>>>> 1.5
		setTitle("Execution Controls");

		getContentPane().setLayout(new GridLayout(0, 2));

		JButton resetButton = new JButton("Reset");
		JButton a = new JButton("Run to Completion");
		JButton b = new JButton("Run to Breakpoint");
		JButton c = new JButton("Step Forward");

		StepForwardListener a1 = new StepForwardListener();
		Information a2 = new Information();
		c.addActionListener(a1);
		c.addMouseListener(a2);
		ResetActionlistener r1 = new ResetActionlistener();
		resetButton.addActionListener(r1);

		JLabel al = new JLabel("");
		JLabel bl = new JLabel("<html>Please check box to specify breakpoint</html>");
		JLabel cl = new JLabel("<html>Run the next instruction</html>");

		getContentPane().add(resetButton);
		getContentPane().add(a);
		getContentPane().add(al);
		getContentPane().add(b);
		getContentPane().add(bl);
		getContentPane().add(c);
		getContentPane().add(cl);

	}

	/**
	 * The following listener is to handle clicks on the Step Forward button.
	 * @author ss401
	 *
	 */
	class StepForwardListener implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			YAMSGui.getCodeFrame().stepForward();
		}
	}

	class ResetActionlistener implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			YAMSGui.getCodeFrame().reset();
		}
	}
	class Information implements MouseListener {
		public void mouseEntered(MouseEvent a) {
			System.out.println("mouse entered");
			YAMSGui.getHelpPanel().setText("Execute next instruction");
		}
		public void mouseReleased(MouseEvent a) {
		}
		public void mouseClicked(MouseEvent a) {
		}
		public void mousePressed(MouseEvent a) {
		}
		public void mouseExited(MouseEvent a) {
		}
	}

}
