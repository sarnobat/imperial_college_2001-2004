/*
 * Created on 21-Nov-2003
 *
 */
package GUI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.ScrollPane;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author ss401
 *
 */
public class ProgramCodeFrame extends YamsInternalFrame {

	
	/**
	 * 
	 */
	public ProgramCodeFrame(int width,int height) {
		super(width,height);
		int leftWidth = 17;
		int rightWidth = width-leftWidth;
		//TODO: This should also give the name of the file 
		setTitle("Program Code");
		
		JPanel root = new JPanel();
		getContentPane().add(root);
		root.setLayout(new BoxLayout(root,BoxLayout.X_AXIS));
		JPanel left = new JPanel();
		JPanel right = new JPanel();
		left.setPreferredSize(new Dimension(leftWidth,height));
		left.setMaximumSize(new Dimension(leftWidth,height));
		left.setMinimumSize(new Dimension(leftWidth,height));
		
		//right.setMinimumSize(new Dimension(rightWidth,height));
		//right.setPreferredSize(new Dimension(rightWidth,height));
		
		root.add(left);
		root.add(right);
		
		String fakeProgramCode = "";
		left.setLayout(new BoxLayout(left,BoxLayout.Y_AXIS));
		//left.add(Box.createRigidArea(new Dimension(0,4)));
		for (int i = 0; i < 20; i++) {
			JCheckBox l = new JCheckBox();
			left.add(Box.createRigidArea(new Dimension(0,3)));
			left.add(l);
			fakeProgramCode += "instruction\n";
		}
		
		JTextArea codeArea = new JTextArea(fakeProgramCode);
		codeArea.setPreferredSize(new Dimension(300,height));
		codeArea.setVisible(true);
		//codeArea.setPreferredSize(new Dimension(rightWidth,height));
		//t.setWrapStyleWord(true);
		right.add(codeArea);
		
	
		getContentPane().add(new JScrollPane(root));
		
		
	}

}
