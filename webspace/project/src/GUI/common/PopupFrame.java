/*
 * Created on 16-Jun-2004
 *
 */
package GUI.common;

import javax.swing.JFrame;

i ort GUI.GUIController;

/**
 * @author ss401
 *
 */
public class PopupFrame extends JFrame {

	GUIController c;

	public PopupFrame(GUIController c) {
		super();
		this.c = c;
		this.setLocation(GUIController.locationX + 100, GUIController.locationY + 100);
	}

}
