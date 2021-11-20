/*
 * Created on 21-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package GUI;

import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 * @author ss401
 *
 */
public class DataFrame extends YamsInternalFrame {

	/**
	 * 
	 */
	public DataFrame(int width, int height) {
		super(width, height);
		setTitle("Data Segment");
		
		JLabel l = new JLabel("I don't have a clue what to put here");
		
		//getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
		getContentPane().add(l);

	}

}
