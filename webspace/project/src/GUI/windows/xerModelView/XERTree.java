/*
 * Created on 31-May-2004
 *
 */
package GUI.windows.xerModelView;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import xtractor.schemaConverter.xer.XERModel;
import GUI.windows.xerModelView.xerTreeComponents.XERNode;

/**
 * @author ss401
 *
 */
public class XERTree extends JTree {

	XERModel xerModel;
	XERModelFrameController c;

	/**
	 * 
	 */
	public XERTree(XERModelFrameController c, DefaultMutableTreeNode topNode) {
		super(topNode);
		this.c = c;
		this.xerModel = c.getXERModel();
		this.setEditable(true);
		this.addTreeSelectionListener(new XERTreeSelectionListener());
		this.setExpandedState(getAnchorSelectionPath(),true);
	
	}

	class XERTreeSelectionListener implements TreeSelectionListener {

		public void valueChanged(TreeSelectionEvent e) {
			XERTree tree = (XERTree) e.getSource();
			XERNode node = (XERNode) tree.getLastSelectedPathComponent();
			Object construct = node.getUserObject();
			//XERConstruct construct = (XERConstruct) node.getUserObject();
			c.updateObjectSummary(construct,node);

		}

	}

}
