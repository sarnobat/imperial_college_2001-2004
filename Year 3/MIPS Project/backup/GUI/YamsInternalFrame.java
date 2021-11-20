/*
 * Created on 21-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package yams.GUI;

import yams.processor.*;

import java.awt.Dimension;

import javax.swing.JInternalFrame;

/**
 * @author ss401
 *
 */
public class YamsInternalFrame extends JInternalFrame {

	public YamsInternalFrame(int width,int height, Processor processor) {
		setPreferredSize(new Dimension(width, height));
		setClosable(true);

		setResizable(true);
		setMaximizable(true);

		setEnabled(true);
		setIconifiable(true); //Enables minimize button in window

		show();
	}
}
