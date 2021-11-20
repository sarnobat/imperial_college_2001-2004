/*
 * Created on 01-Jun-2004
 *
 */
package GUI.windows.schemaSelector.components;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import GUI.windows.schemaSelector.AbstractSchemaSelectorController;
;

/**
 * @author ss401
 *
 */
public class SchemaTable extends JTable {

	AbstractSchemaSelectorController dataImportController;
	TableModel tableModel;
	/**
	 * 
	 */
	public SchemaTable(AbstractSchemaSelectorController dataImportController, TableModel tableModel) {
		super(tableModel);
		this.dataImportController = dataImportController;

		//this.tableModel = tableModel;
		//this.setPreferredSize(new Dimension(200, 200));


	}
}
