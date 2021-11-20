/*
 * Created on 21-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package GUI;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author ss401
 *
 */
public class RegistersFrame extends YamsInternalFrame {

	/**
	 * 
	 */
	public RegistersFrame(int width, int height) {
		super(width, height);
		setTitle("Register Contents");

		getContentPane().setLayout(new FlowLayout());

		//TODO: At the moment, 2 sub-panels are required to enforce 2 columns
		//that are read down THEN across (as opposed to page-wise of across then
		//down). What is needed is a 'vertical' flow layout, but there doesn't appear
		//to be one.
		JPanel left = new JPanel();
		JPanel right = new JPanel();

		left.setVisible(true);
		right.setVisible(true);

		left.setLayout(new GridLayout(0, 2));

		getContentPane().add(left);
		getContentPane().add(right);

		left.setLayout(new GridLayout(0,2));
		for (int i = 0; i < 5; i++) {

			JLabel registerName = new JLabel("Register" + i);
			JTextField b = new JTextField("0x11111");
			b.setVisible(true);
			registerName.setVisible(true);

			left.add(registerName);
			left.add(b);
		}
		//right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
		right.setLayout(new GridLayout(0,2));
		for (int i = 0; i < 5; i++) {

			JLabel registerName1 = new JLabel("Register" + i);
			JTextField b1 = new JTextField("0x0000");
			b1.setVisible(true);
			registerName1.setVisible(true);
			right.add(registerName1);
			right.add(b1);
		}

		//TODO: To find out what the actual registers are, you can use the 'ValidRegisters' class

	}

}
