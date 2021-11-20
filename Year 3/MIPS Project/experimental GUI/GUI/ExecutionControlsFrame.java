/*
 * Created on 21-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package GUI;

import java.awt.FlowLayout;

import javax.swing.JButton;


/**
 * @author ss401
 *
 */
public class ExecutionControlsFrame extends YamsInternalFrame {

	/**
	 * 
	 */
	public ExecutionControlsFrame(int width,int height) {
		super(width,height);
		setTitle("Execution Controls");
		
		getContentPane().setLayout(new FlowLayout());
		
		JButton a = new JButton("Step Forward");
		JButton b = new JButton("Run to Breakpoint");
		JButton c = new JButton("Run to Completion");
		
		getContentPane().add(a);
		getContentPane().add(b);
		getContentPane().add(c);
	}

}
