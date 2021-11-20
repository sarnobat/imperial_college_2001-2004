package yams.GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

import yams.*;

import java.io.*;

/**
 * Panel listing files of a user's choice which could be run
 */
public class FileSelectorPanel extends YamsPanel {

	// list of File objects, as stored in the JList window
	public DefaultListModel fileList = new DefaultListModel();
	
	// window elements
	public JList lstFileList;
	public JButton butAddFile;
	public JButton butAddFileList;
	public JButton butRemFile;
	public JButton butLoadFile;

	public FileSelectorPanel(YAMSGui controller) {
		super(controller);
		setTitle("File Selector");
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));		
		
		// Add the JList to this frame 
		lstFileList = new JList(fileList);
		lstFileList.setVisible(true);
		lstFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstFileList.addListSelectionListener(new ListController(controller));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView(lstFileList);
		this.add(scrollPane);
		
		// create a panel for the buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2,2));
		
		buttonPanel.setMinimumSize(new Dimension(0,80));
		buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,80));
		
		// add some buttons
		butAddFile = new JButton("Add File...");
		butAddFileList = new JButton("Add File List...");
		butRemFile = new JButton("Remove File");
		butLoadFile = new JButton("Load File");
		// initially, LoadFile and RemoveFile are false
		butRemFile.setEnabled(false);
		butLoadFile.setEnabled(false);
		
		buttonPanel.add(butAddFile);
		buttonPanel.add(butAddFileList);
		buttonPanel.add(butRemFile);
		buttonPanel.add(butLoadFile);
		
		this.add(buttonPanel);
		
		// add button listeners
		ButtonController buttonController = new ButtonController(this,controller);
		butAddFile.addActionListener(buttonController);
		butAddFileList.addActionListener(buttonController);
		butRemFile.addActionListener(buttonController);
		butLoadFile.addActionListener(buttonController);
		
	}
	
	/**
	 * Adds a file to the visible lsit of files
	 * @param file
	 */
	public void addFile(File file) {
		// adds a file to the list, and to the file list
		fileList.addElement(file);
	}
	

}




/**
 * Listens for changes to the file list. This is 
 * necessary when determining whether to enable/disable
 * buttons
 */
class ListController implements ListSelectionListener {
	
	private YAMSGui controller;
	
	ListController(YAMSGui c) {
		controller = c;
	}
	
	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting() == false ) {
			if ( ((JList)e.getSource()).getSelectedIndex()== -1 ) {
				// nothing slected, disable load file
				controller.setRemoveLoadStatus(false);
			} else {
				controller.setRemoveLoadStatus(true);
			}
		}
	}
	
}



/**
 * Listens for button clicks and calls the appropriate method
 */
class ButtonController implements ActionListener {
	
	private FileSelectorPanel filePanel;
	private YAMSGui controller;
	
	ButtonController(FileSelectorPanel f, YAMSGui c) {
		filePanel = f;
		controller = c;
	}
	
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton)e.getSource();
		
		if (source == filePanel.butAddFile){
			controller.addFile();
			
		} else if (source == filePanel.butAddFileList) {
			controller.addFileList();
			
		} else if (source == filePanel.butRemFile){
			controller.removeFile();
			
		} else if (source == filePanel.butLoadFile){
			controller.loadFile();
			
		}
	}
	
}

