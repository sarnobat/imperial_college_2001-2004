/*
 * Created on 15-May-2004
 *
 */
package view;

import model.Model;
import view.guiCompnents.MainWin> w;

/**
 * @author ss401
 *
 */
public class GUI {

	public GUI() {
		Model m = new Model();
		MainWindow f = new MainWindow(m); 
		f.setVisible(true);
		run();
	}

	private void run() {
		
	}

}
