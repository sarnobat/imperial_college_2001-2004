/*
 * Created on 31-May-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package GUI.windows.xerModelView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * @author ss401
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CreateRDBButton extends JButton {

	XERModelFrameController c;
	/**
	 * @param string
	 */
	public CreateRDBButton(XERModelFrameController c, String string) {
		super(string);
		this.c = c;
		this.addActionListener(new CreateDRBButtonListener());
	}

	class CreateDRBButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			c.createRelationalDatabase();
		}

	}
}
