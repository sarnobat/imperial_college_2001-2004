/*
 * Created on 31-May-2004
 *
 */
package GUI.windows.xerModelView.xerTreeComponents;
import xtractor.schemaConverter.xer.xerConstructs.XERRelationship;

/**
 * @author ss401
 *
 */
public class XERRelationshipNode extends XERNode {

	XERRelationship xerRelationship;

	/**
	 * @param userObject
	 */
	public XERRelationshipNode(XERRelationship relationship) {
		super(relationship, true);
		this.xerRelationship = relationship;
		//addEntities();
		
		
		

	}

	/**
	 * 
	 */
	private void addEntities() {
		/*XERCompoundConstruct parent = xerRelationship.getParent();
		this.add(new XER(parent));
				
		XERCompoundConstruct child = xerRelationship.getChild();
		this.add(new XERCompoundConstructNode(child));*/

	}

}
