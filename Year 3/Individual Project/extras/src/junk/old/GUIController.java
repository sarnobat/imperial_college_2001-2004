import javax.swing.JFrame;

import GUIComponents.InitialWindow;

/*
 * Created on 19-M -2004
 *
 */

/**
 * @author ss401
 *
 */

public class GUIController {
	private static JFrame root;
	public static void main(String[] args) {
		setupGUI();
		root.setVisible(true);
	}
	
	/**
	 * 
	 */
	private static void setupGUI() {
		InitialWindow initialWindow = new InitialWindow();
		
	}
}
