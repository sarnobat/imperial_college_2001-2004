/*
 * Created on 31-May-2004
 *
 */
package GUI.windows.xerModelView.xerConstructSummary;

import javax.swing.JLabel;

import xtractor.schemaConverter.xer.xerConstructs.XERRelationship;
import GUI.windows.xerModelView.XERModelFrameController;

/**
 * @author ss401
 *
 */
public class XERRelationshipSummary extends XERConstructSummary {

	XERRelationship relationship;
	
	
	// Panel components
	JLabel info;

	public XERRelationshipSummary(XERModelFrameController c, XERRelationship relationship) {
		super(c);
		this.relationship = relationship;
		
		setup();
	}

	protected void setup() {
		info = new JLabel(relationship.getParent().getName() + " --> " + relationship.getChild().getName());
		this.add(info);
	}


}
