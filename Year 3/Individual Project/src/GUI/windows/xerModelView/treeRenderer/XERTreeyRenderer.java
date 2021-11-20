/*
 * Created on 08-Jun-2004
 *
 */
package GUI.windows.xerModelView.treeRenderer;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import xtractor.schemaConverter.xer.xerConstructs.XEREntity;
import xtractor.schemaConverter.xer.xerConstructs.XERMixedEntity;
import GUI.GUIController;
import GUI.windows.xerModelView.xerTreeComponents.XEREntityNode;
import GUI.windows.xerModelView.xerTreeComponents.XERGeneralizationNode;
import GUI.windows.xerModelView.xerTreeComponents.XERRelationshipNode;

/**
 * @author ss401
 *
 */
public class XERTreeyRenderer extends DefaultTreeCellRenderer {

	Icon entityIcon = new ImageIcon(GUIController.absoluteFolderPath + "/icons/entity.gif");
	Icon mixedEntityIcon = new ImageIcon(GUIController.absoluteFolderPath + "/icons/mixedEntity.gif");
	Icon relationshipIcon = new ImageIcon(GUIController.absoluteFolderPath + "/icons/relationship.gif");
	Icon geeneralizationIcon = new ImageIcon(GUIController.absoluteFolderPath + "/icons/generalization.gif");

	public Component getTreeCellRendererComponent(
		JTree tree,
		Object value,
		boolean sel,
		boolean expanded,
		boolean leaf,
		int row,
		boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		if (value instanceof XERGeneralizationNode) {
			setIcon(geeneralizationIcon);

		}
		else if (value instanceof XERRelationshipNode) {

			setIcon(relationshipIcon);
		}
		else if (value instanceof XEREntityNode) {
			XEREntity entity = (XEREntity) ((XEREntityNode) value).getUserObject();
			if (entity instanceof XERMixedEntity) {
				setIcon(mixedEntityIcon);
			}
			else {
				setIcon(entityIcon);
			}
		}
		return this;
	}

}
