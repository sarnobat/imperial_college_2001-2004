/*
 * Created on 31-May-2004
 *
 */
package GUI.windows.xerModelView.xerTreeComponents;

import xtractor.schemaConverter.xer.XERModel;
import GUI.windows.xerModelView.XERModelFrameController;
/**
 * @author ss401
 *
 */
public class XERRootNode extends XERNode {

	//XERModel xerModel;

	/**
	 * @param userObject
	 */
	public XERRootNode(XERModelFrameController c,XERModel xerModel) {
		super(xerModel,true);
		//this.xerModel = xerModel;
		//addChildRelationships();		
	}

//	private void addChildRelationships() {
//		Collection relationships = xerModel.getRelationships();
//		for (Iterator iter = relationships.iterator(); iter.hasNext();) {
//			XERRelationship relationship = (XERRelationship) iter.next();
//			XERRelationshipNode relationshipNode = new XERRelationshipNode(relationship);
//			this.add(relationshipNode);
//		}
//		
//	}
}
