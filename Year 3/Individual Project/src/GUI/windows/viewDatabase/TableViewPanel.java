/*
 * Created on 17-Jun-2004
 *
 */
package GUI.windows.viewDatabase;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

/**
 * @author ss401
 *
 */
public class TableViewPanel extends JPanel {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	String[] columnNames;
	Object[][] data;

	public TableViewPanel() {
		super();
	}

	/**
	 * @param rows - A collection of maps; each map is from string to string
	 */
	public void addSummary(Collection rows) {
		showTableData(rows);
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		DefaultTableModel tableModel = new DefaultTableModel(data,columnNames);
		JTable table = new JTable(tableModel);
				
		JScrollPane jsp = new JScrollPane(table);
		c.weightx = 1;
		c.weighty = 1;
		this.add(jsp,c);
		

	}

	/**
	 * @param rows - A collection of maps; each map is from string to string
	 */
	private void showTableData(Collection rows) {
		
		if(rows.size() <1){
			return;
			//rows = new LinkedList();
		}
		
		List l = new LinkedList(rows);

		// Determine the column names
		Set columnNamesSet = ((Map) l.get(0)).keySet();
		columnNames = new String[columnNamesSet.size()];
		int colNum = 0;
		for (Iterator iter = columnNamesSet.iterator(); iter.hasNext();) {
			String columnName = (String) iter.next();
			columnNames[colNum] = columnName;
			colNum++;
		}

		// Determine the data
		data = new Object[rows.size()][columnNamesSet.size()];
		int rowNum = 0;
		for (Iterator iter = rows.iterator(); iter.hasNext();) {
			Map rowMap = (Map) iter.next();

			for (colNum = 0; colNum < columnNames.length; colNum++) {
				Object columnValue = rowMap.get(columnNames[colNum]);
				data[rowNum][colNum] = columnValue;
			}
			rowNum++;
		}
		logger.debug("Finished getting data for table");

	}

}
