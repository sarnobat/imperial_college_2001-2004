/*
 * Created on 31-May-2004
 *
 */
package GUI.windows.xerModelView.xerTreeComponents;
 mport javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author ss401
 *
 */
public abstract class XERNode extends DefaultMutableTreeNode {


	/**
	 * @param userObject
	 * @param allowsChildren
	 */
	protected XERNode(Object object, boolean allowsChildren) {
		super(object, allowsChildren);
	}

}
