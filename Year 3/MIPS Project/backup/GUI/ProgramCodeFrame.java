/*
 * Created on 21-Nov-2003
 *
 */
package yams.GUI;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import yams.processor.*;

/**
 * @author ss401
 *
 */
public class ProgramCodeFrame extends YamsInternalFrame {

	int width;
	int height;
	int scrollbaWidth = 50;
	int leftWidth = 17;
	int rightWidth = 17;
	int centreWidth;
	
	JLabel l;
	String codePosition = "";
	String marker;
	String markerIdle = "*";
	String markerActive = "<b><font color=red>" +markerIdle + "</font></b>";
	String fakeProgramCode = "";
	/**
	 * 
	 */
	public ProgramCodeFrame(int width,int height, Processor processor) {
		super(width,height,processor);
		this.width = width;
		this.height = height;
		centreWidth = width - leftWidth - rightWidth-scrollbaWidth;

		
		//TODO: This should also give the name of the file 
		setTitle("Program Code");

		JPanel root = new JPanel();
		getContentPane().add(root);
		root.setLayout(new BoxLayout(root, BoxLayout.X_AXIS));

		root.add(getRadioBoxColumn());
		root.add(getCheckBoxColumn());
		root.add(getProgramCodeTextArea());


		getContentPane().add(new JScrollPane(root));

	}
	private JPanel getProgramCodeTextArea() {
		JPanel centre = new JPanel();
		
		JTextArea codeArea = new JTextArea(fakeProgramCode);
		codeArea.setPreferredSize(new Dimension(centreWidth, height));
		codeArea.setMinimumSize(new Dimension(centreWidth, height));
		codeArea.setMaximumSize(new Dimension(centreWidth, height));
		codeArea.setVisible(true);
		codeArea.setEditable(false);
		centre.add(codeArea);
		
		return centre;

	}
	/**
	 * Creates the panel to specify where to place a breakpoint
	 * @return
	 */
	private JPanel getCheckBoxColumn() {
		JPanel left = new JPanel();
		left.setPreferredSize(new Dimension(leftWidth, height));
		left.setMaximumSize(new Dimension(leftWidth, height));
		left.setMinimumSize(new Dimension(leftWidth, height));

		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
		//left.add(Box.createRigidArea(new Dimension(0,4)));
		for (int i = 0; i < 20; i++) {
			JCheckBox l = new JCheckBox();
			left.add(Box.createRigidArea(new Dimension(0, 3)));
			left.add(l);
			fakeProgramCode += "instruction\n";
		}

		return left;

	}
	/**
	 * Creates the panel to indicate at what point in the program the simulation is
	 * @return
	 */
	private JPanel getRadioBoxColumn(){
		JPanel right = new JPanel();
		right.setPreferredSize(new Dimension(leftWidth, height));
		right.setMaximumSize(new Dimension(leftWidth, height));
		right.setMinimumSize(new Dimension(leftWidth, height));

		/*right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
		//left.add(Box.createRigidArea(new Dimension(0,4)));
		ButtonGroup g = new ButtonGroup();
		for (int i = 0; i < 20; i++) {
			JRadioButton l = new JRadioButton();
			l.set
			right.add(Box.createRigidArea(new Dimension(0, 3)));
			g.add(l);
			right.add(l);
		}*/
		
		l = new JLabel();
		reset();
		//initializeMarker();
		right.add(l);
		

		return right;
	}
	public void stepForward(){
		//TODO: Need to check if we've reached the end of the program; unless this cannot be called in such a case
		codePosition += "<br>";
		refreshPosition();

	}
	public void refreshPosition(){
		l.setText("<html>"+ codePosition + marker +"</html>");
				
	}
	public void reset() {
		//markerActive = marker;
		codePosition = "";
		marker = markerIdle;
		refreshPosition();
		marker = markerActive;
		
	}/*public void initializeMarker(){
		l.setText("<html>"+ codePosition + marker +"<html>");
	}*/

}
