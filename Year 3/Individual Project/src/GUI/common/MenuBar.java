/*
 * Created on 31-May-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package GUI.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.apache.log4j.Logger;

import GUI.GUIController;
import GUI.windows.XTractorFrame;

/**
 * @author ss401
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MenuBar extends JMenuBar {

	Logger logger = Logger.getLogger(this.getClass());

	JMenu menuReturnToStart;
	JMenu menuConfigureDatabase;
	JMenu menuConfigureTypeMappings;

	GUIController c;
	XTractorFrame frame;

	/**
	 * 
	 */
	public MenuBar(GUIController c, XTractorFrame f) {
		this.c = c;
		this.frame = f;
		this.menuReturnToStart = new JMenu("Main Menu");
		this.add(menuReturnToStart);

		this.menuConfigureDatabase = new JMenu("Database Configuration");
		this.menuConfigureTypeMappings = new JMenu("Type Mappings");

		JMenuItem itemConfigureDB = new JMenuItem("Configure", KeyEvent.VK_T);
		JMenuItem itemConfigureTypeMappings = new JMenuItem("Configure");

		menuConfigureDatabase.add(itemConfigureDB);
		menuConfigureTypeMappings.add(itemConfigureTypeMappings);

		this.add(menuConfigureDatabase);
		this.add(menuConfigureTypeMappings);

		menuReturnToStart.addMenuListener(new ReturnListener());
		itemConfigureDB.addActionListener(new ConfigureDBListener());
		itemConfigureTypeMappings.addActionListener(new ConfigureTypeMappingsListener());
	}

	class ReturnListener implements MenuListener {

		public void menuSelected(MenuEvent e) {
			frame.dispose();
			c.launchTaskSelectorFrame();
		}

		public void menuDeselected(MenuEvent e) {

		}
		public void menuCanceled(MenuEvent e) {

		}
	}

	class ConfigureTypeMappingsListener implements ActionListener {
		JFrame typeMappingsFrame;

		public ConfigureTypeMappingsListener() {
			super();
			typeMappingsFrame = new TypeMappingsFrame(c);			
		}

		public void actionPerformed(ActionEvent e) {
			typeMappingsFrame.show();

		}

	}

	class ConfigureDBListener implements ActionListener {
		JFrame dbConfigFrame;

		public ConfigureDBListener() {
			super();
			dbConfigFrame = new DatabaseConfigFrame(c);
		}
		public void actionPerformed(ActionEvent e) {
			logger.warn("Menu Key action");
			dbConfigFrame.show();
		}
	}
}
