/*
 * Created on 08-Jun-2004
 *
 */
package GUI.windows.xerModelView.xerTr Components;

import xtractor.schemaConverter.xer.xerConstructs.XERAttribute;

/**
 * @author ss401
 *
 */
public class XERAttributeNode extends XERNode {

	/**
	 * @param object
	 * @param allowsChildren
	 */
	public XERAttributeNode(XERAttribute attribute) {
		super(attribute, false);
	}

}
