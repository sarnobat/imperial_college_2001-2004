/*
 * Created on 31-May-2004
 *
 */
package GUI.windows.xerModelView.xerConstructSummary;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import xtractor.schemaConverter.xer.xerConstructs.XERConstruct;
import GUI.windows.xerModelView.XERModelFrameController;

/**
 * @author ss401
 *
 */
public abstract class XERConstructSummary extends JPanel {

	final Logger logger = Logger.getLogger(this.getClass());
	JLabel info;
	XERModelFrameController c;

	public XERConstructSummary(XERModelFrameController c) {
		this.c = c;
	}

	protected abstract void setup();


	class ConstructNameListener implements KeyListener {

		XERConstruct xerConstruct;

		public ConstructNameListener(XERConstruct construct) {
			this.xerConstruct = construct;
		}

		public void keyReleased(KeyEvent e) {
			JTextField textBox = (JTextField) e.getSource();
			String newName = textBox.getText();

			xerConstruct.setAlias(newName);
		}

		public void keyTyped(KeyEvent e) {}
		public void keyPressed(KeyEvent e) {}

	}	
	
	
}