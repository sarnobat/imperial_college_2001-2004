/*
 * Created on 21-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package yams.GUI;

import java.awt.Dimension;

import javax.swing.JEditorPane;

import yams.processor.*;

/**
 * @author ss401
 *
 */
public class DialogFrame extends YamsInternalFrame {

	/**
	 * 
	 */
	public DialogFrame(int width,int height, Processor processor) {
		super(width,height,processor);
		setTitle("Dialog");
		
		//JTextArea t = new JTextArea("<html><b>This message will self destruct in 30 seconds...</b></html>");
		JEditorPane t = new JEditorPane("text/html","<html><b><font color=red>This message will self destruct in 30 seconds...</font></b></html>");
		t.setVisible(true);
		t.setEditable(false);
		t.setPreferredSize(new Dimension(width,height));
		/*t.setLineWrap(true);
		t.setWrapStyleWord(true);
<<<<<<< DialogFrame.java
		*/getContentPane().add(t);
=======
		getContentPane().add(t);
		
>>>>>>> 1.5
	}

}
