/*
 * Created on 17-Jun-2004
 *
 */
package GUI.windows.viewDatabase;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import GUI.GUIController;
import databaseConnection.Nomenclature;

/**
 * @author ss401
 *
 */
public class DatabaseTreeRenderer extends DefaultTreeCellRenderer {

	Icon metaTableIcon = new ImageIcon(GUIController.absoluteFolderPath + "/icons/metaTable.gif");
	Icon dataTableIcon = new ImageIcon(GUIController.absoluteFolderPath + "/icons/dataTable.gif");

	public Component getTreeCellRendererComponent(
		JTree tree,
		Object value,
		boolean sel,
		boolean expanded,
		boolean leaf,
		int row,
		boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		String nodeName = ((String) node.getUserObject()).toUpperCase();
		if (nodeName.startsWith(Nomenclature.META_PREFIX)) {
			setIcon(metaTableIcon);

		}
		else if (nodeName.startsWith(Nomenclature.SYSTEM_PREFIX)) {
			setIcon(metaTableIcon);

		}else{
			setIcon(dataTableIcon);
		}
		return this;
	}

}
