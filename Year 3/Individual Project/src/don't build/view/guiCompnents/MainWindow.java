package view.guiCompnents;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.log4j.Logger;

import model.Model;

/**
 * 
 * @author ss401
 *
 */
/**
* This code was generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a
* for-profit company or business) then you should purchase
* a license - please visit www.cloudgarden.com for details.
*/

public class MainWindow extends javax.swing.JFrame {

	Logger logger = Logger.getLogger(this.getClass());

	JPanel importPanel;
	JPanel exportPanel;
	JTabbedPane importAndExportTabsPane;
	Model model;

	/**
	 * 
	 * @param model - The model in the Model-View-Controller architecture. It contains vital methods
	 * for populating the GUI components such as the list of databases
	 */
	public MainWindow(Model model) {
		this.model = model;
		initGUI();
	}

	/**
	* Initializes the GUI.
	* Auto-generated code - any changes you make will disappear.
	*/
	public void initGUI() {
		try {

			try {
				javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			}
			catch (Exception e) {
				logger.error("Couldn't set look and feel.");
			}
			importAndExportTabsPane = new JTabbedPane();
			importPanel = new JPanel();
			exportPanel = new ExportPanel(model,this);

			BorderLayout thisLayout = new BorderLayout();
			this.getContentPane().setLayout(thisLayout);
			thisLayout.setHgap(0);
			thisLayout.setVgap(0);
			this.setResizable(false);
			this.setTitle("XTractor");
			this.setSize(new java.awt.Dimension(507, 527));
			this.setLocation(new java.awt.Point(200, 200));
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

			// Tabbed pane
			importAndExportTabsPane.setPreferredSize(new java.awt.Dimension(500, 500));
			this.getContentPane().add(importAndExportTabsPane, BorderLayout.CENTER);

			// Export tab
			exportPanel.setVisible(true);
			exportPanel.setPreferredSize(new java.awt.Dimension(495, 475));
			importAndExportTabsPane.add(exportPanel);
			importAndExportTabsPane.setTitleAt(0, "Export XML File");

			// Import tab
			importPanel.setPreferredSize(new java.awt.Dimension(495, 475));
			importAndExportTabsPane.add(importPanel);
			importAndExportTabsPane.setTitleAt(1, "Import XML File");

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void refresh(){
		getContentPane().invalidate();
		
	}
}
