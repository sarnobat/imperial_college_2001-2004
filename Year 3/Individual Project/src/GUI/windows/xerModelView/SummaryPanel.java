/*
 * Created on 31-May-2004
 *
 */
package GUI.windows.xerModelView;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import xtractor.schemaConverter.xer.XERModel;
import xtractor.schemaConverter.xer.xerConstructs.XEREntity;
import xtractor.schemaConverter.xer.xerConstructs.XERGeneralization;
import xtractor.schemaConverter.xer.xerConstructs.XERRelationship;
import GUI.windows.xerModelView.xerConstructSummary.XEREntitySummary;
import GUI.windows.xerModelView.xerConstructSummary.XERGeneralizationSummary;
import GUI.windows.xerModelView.xerConstructSummary.XERRelationshipSummary;

/**
 * @author ss401
 *
 */
public class SummaryPanel extends JPanel {

	Logger logger = Logger.getLogger(this.getClass());

	XERModelFrameController c;

	public SummaryPanel(XERModelFrameController c) {
		this.c = c;
		//Dimension d = new Dimension(GUIController.width * 2/3,GUIController.height);
		//this.setPreferredSize(d);
		//this.setSize(d);
	}

	/**
	 * @param construct
	 */
	public void addSummary(Object construct) {
		if (construct instanceof XERModel) {

		}
		else if (construct instanceof XERRelationship) {
			this.add(new XERRelationshipSummary(c, (XERRelationship) construct));
		}
		else if (construct instanceof XERGeneralization) {
			this.add(new XERGeneralizationSummary(c, (XERGeneralization) construct));
		}
		else if (construct instanceof XEREntity) {
			this.add(new XEREntitySummary(c, (XEREntity) construct));
		}
		else {
			logger.error("Invalid user object for node. " + construct.getClass());
		}
	}

}
