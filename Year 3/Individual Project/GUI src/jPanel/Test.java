/*
 * Created on 17-May-2004
 *
 */
package jPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author ss401
 *
 */
public class Test {

	static JPanel p;
	static JFrame f;
	static JLabel l1;
	static JLabel l2;
	static JButton b;

	public static void main(String[] args) {
		f = new JFrame();
		p = new JPanel();
		l1 =new JLabel("AAAAAAA");
		l2 =new JLabel("AAAAAAA");
		b = new JButton("Next");

		b.addActionListener(new ButtonListener());

		p.add(l1);
		p.add(b);
		
		f.getContentPane().add(p);
		f.show();
		f.setSize(500,500);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	static class ButtonListener implements ActionListener{

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			p.remove(l1);
			l1 = null;
			p.add(l2);
			f.getContentPane().invalidate();				
		}
		
	}
}
