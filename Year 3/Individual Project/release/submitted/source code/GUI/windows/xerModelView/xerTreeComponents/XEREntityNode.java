/*
 * Created on 08-Jun-2004
 *
 */
package GUI.windows.xerModelView.xerTreeCompon ts;

import xtractor.schemaConverter.xer.xerConstructs.XEREntity;

/**
 * @author ss401
 *
 */
public class XEREntityNode extends XERNode {

	/**
	 * @param construct
	 */
	public XEREntityNode(XEREntity entity) {
		super(entity,true);
	}

}
