package GUI;

import java.awt.Dimension;

import javax.swing.JList;

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

	public FileSelectorFrame(int width, int height) {
		super(width, height);
		setTitle("File Selector");

		//Add the JList to this frame 
		String[] list = { "file 1", "file 2","file 3","file 4","file 5","file 6","file 7","file 8" };
		JList files = new JList(list);
		files.setVisible(true);
		files.setPreferredSize(new Dimension(width, height));
		files.setMaximumSize(new Dimension(width, height));
		getContentPane().add(files);
	}

}
