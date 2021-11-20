/*
 * Created on 17-Jun-2004
 *
 */
package GUI.windows.viewDatabase;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import GUI.GUIController;
import GUI.windows.AbstractFrameController;

/**
 * @author ss401
 *
 */
public class DatabaseViewerController extends AbstractFrameController {

	String schemaName;

	JTree tableTree;

	// very complex - see constructor javadoc for details
	Map tables;
	Collection tableNames;
	
	
	DefaultMutableTreeNode rootNode;
	TableViewPanel tablePanel;

	/**
	 * 
	 * @param controller
	 * @param selectedSchemaName
	 * @param tables - Map of Strings to Collections of maps
	 * {
	 * 		tableName --> {Map,Map,Map,...},
	 * 		tableName --> {Map,Map,Map,...},
	 * 		tableName --> {Map,Map,Map,...}
	 * }
	 * Each map corresponds to a row in the table. These maps are from column name to column value 
	 */
	public DatabaseViewerController(GUIController controller, String selectedSchemaName, Map tables) {
		super(controller);
		this.schemaName = selectedSchemaName;
		
		this.tables = tables;
		this.tableNames = tables.keySet();
		showFrame();
	}

	public void showFrame() {
		frame = new DatabaseViewerFrame(mainGUIController);
		Container pane = frame.getContentPane();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		rootNode = new DefaultMutableTreeNode("Tables");
		populateTree();

		tableTree = new JTree(rootNode, true);
		tableTree.setCellRenderer(new DatabaseTreeRenderer());
		tableTree.addTreeSelectionListener(new TableNodeListener());

		tablePanel = new TableViewPanel();
		
		tablePanel.setPreferredSize(new Dimension(GUIController.width * 3/4,GUIController.height));

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 5;
		c.weighty = 5;
		pane.add(tableTree, c);

		c.gridx = 1;
		c.weightx =10;
		c.weighty =10;
		pane.add(tablePanel,c);

		frame.show();
	}

	private void populateTree() {
		SortedSet tableNamesSorted = new TreeSet(tableNames);
		for (Iterator iter = tableNamesSorted.iterator(); iter.hasNext();) {
			String tableName = (String) iter.next();

			DefaultMutableTreeNode tableNode = new DefaultMutableTreeNode(tableName,false);
			rootNode.add(tableNode);
		}
	}

	/**
	 * 
	 * @param construct
	 * @param rows - A collection of maps; each map is from string to string
	 */
	public void showTablePanel(Object construct, Collection rows) {
		tablePanel.removeAll();
		tablePanel.addSummary(rows);
		// Both of these are needed.			
		frame.update(frame.getGraphics());
		tablePanel.validate();

		logger.debug("Updated summary panel. Now displaying info for " + construct);

	}

	class TableNodeListener implements TreeSelectionListener {

		public void valueChanged(TreeSelectionEvent e) {
			JTree tree = (JTree) e.getSource();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

			String tableName = (String) node.getUserObject();
			
			Collection rows = (Collection) tables.get(tableName);
			showTablePanel(tableName, rows );

		}

	}

}
