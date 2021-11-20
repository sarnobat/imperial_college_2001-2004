package yams.GUI;

<<<<<<< FileSelectorFrame.java
import java.awt.Dimension;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
=======
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
>>>>>>> 1.5

<<<<<<< FileSelectorFrame.java
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
=======
import yams.processor.*;
>>>>>>> 1.5

/*
 * Created on 21-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

/**
 * @author ajb101
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FileSelectorFrame extends YamsInternalFrame {

<<<<<<< FileSelectorFrame.java
	JList files;
	DefaultListModel list = new DefaultListModel();
	
	public FileSelectorFrame(int width, int height) {
		super(width, height);
=======
	public FileSelectorFrame(int width, int height, Processor processor) {
		super(width, height, processor);
>>>>>>> 1.5
		setTitle("File Selector");

		//Add the JList to this frame 
<<<<<<< FileSelectorFrame.java
		
		files = new JList(list);
=======
		String[] list = { "file 1", "file 2","file 3","file 4","file 5","file 6","file 7","file 8" };
		
		getContentPane().setLayout(new FlowLayout());		
		JList files = new JList(list);
>>>>>>> 1.5
		files.setVisible(true);
		files.setPreferredSize(new Dimension(width, height-150));
		files.setMaximumSize(new Dimension(width, height-150));
		getContentPane().add(files);
		
		JButton addFile = new JButton("Add File...");
		JButton remFile = new JButton("Remove File");
		JButton loadFile = new JButton("Load File");
		
		getContentPane().add(addFile);
		getContentPane().add(remFile);
		getContentPane().add(loadFile);
		
		this.addInternalFrameListener(new FileSelectorListener());
		
		ButtonController buttonController = new ButtonController();
		
		addFile.addActionListener(buttonController);
		remFile.addActionListener(buttonController);
		loadFile.addActionListener(buttonController);
		
		
	}
	public int addFile(File f){
		JPanel p = new JPanel();
		
		Set validFileTypes = new HashSet();
		validFileTypes.add("txt");
		validFileTypes.add("");
		
		if(! validFileTypes.contains(getFileType(f)) ) {
			JOptionPane.showMessageDialog(null,"XSPIM cannot read files of type" + getFileType(f),"Invalid File Type",JOptionPane.WARNING_MESSAGE);
			return -1;
		}
		else{
			list.add(list.size(),f.getName());
			return 0;
		}
	}
	public String getFileType(File f){
		StringTokenizer t = new StringTokenizer(f.getName(),".",false);
		if (t.countTokens() == 1){
			return "";
		}
		String token = null;
		while(t.hasMoreTokens()){
			token = t.nextToken();
		}
		
		return token;
		
	}

}


class ButtonController implements ActionListener {
	
	public void actionPerformed(ActionEvent e) {
		System.out.println("Button: " + e.toString());	
	}
	
}


class FileSelectorListener implements InternalFrameListener {
	
	public void internalFrameClosing(InternalFrameEvent e) {
		System.out.println("frame Closing");
	}

	public void internalFrameClosed(InternalFrameEvent e) {
		System.out.println("Frame Closed");
	}

	public void internalFrameOpened(InternalFrameEvent e) {
		System.out.println("Frame Opened");
	}

	public void internalFrameIconified(InternalFrameEvent e) {
		System.out.println("Frame Iconified");
	}

	public void internalFrameDeiconified(InternalFrameEvent e) {
		System.out.println("Frame deiconinfied");
	}

	public void internalFrameActivated(InternalFrameEvent e) {
		System.out.println("Frame Activated");
	}

	public void internalFrameDeactivated(InternalFrameEvent e) {
		System.out.println("Frame deactivated");
	}
	
	public void actionPerformed(ActionEvent e) {
		System.out.println("Action Performed: " + e.toString());
	}

}

