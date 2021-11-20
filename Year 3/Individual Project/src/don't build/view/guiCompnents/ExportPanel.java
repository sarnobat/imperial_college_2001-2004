/*
 * Created on 16-May-2004
 *
 */
package view.guiCompnents;

import javax.swing.JPanel;

import model.Model;

import view.guiCompnents.export.SelectDatabaseSchemaPanel;

/**
 * @author ss401
 *
 */
public class ExportPanel extends JPanel{
	
	SelectDatabaseSchemaPanel schemaSelectPanel;
	MainWindow rootFrame;

	public ExportPanel(Model model,MainWindow rootFrame) {
		this.schemaSelectPanel = new SelectDatabaseSchemaPanel(model,this);
		this.rootFrame = rootFrame;
				
		this.add(schemaSelectPanel);
	}

	/**
	 * Called by the button listener in the SelectDatabaseSchemaPanel
	 * @param databaseSchemaName - The name of the database schema which the user has
	 * selected
	 */
	public void schemaSelected(String databaseSchemaName) {
		//.setVisible(false);
		this.remove(schemaSelectPanel);
		this.add(new JPanel());
		validate();
		rootFrame.refresh();//invalidate();
	}

}
