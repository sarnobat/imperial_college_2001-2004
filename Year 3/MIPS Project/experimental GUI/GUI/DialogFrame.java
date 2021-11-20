/*
 * Created on 21-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package GUI;

import java.awt.Dimension;

import javax.swing.JTextArea;


/**
 * @author ss401
 *
 */
public class DialogFrame extends YamsInternalFrame {

	/**
	 * 
	 */
	public DialogFrame(int width,int height) {
		super(width,height);
		setTitle("Dialog");
		
		JTextArea t = new JTextArea("This message will self destruct in 30 seconds...");
		t.setVisible(true);
		t.setPreferredSize(new Dimension(width,height));
		//t.setWrapStyleWord(true);
		getContentPane().add(t);
	}

}
