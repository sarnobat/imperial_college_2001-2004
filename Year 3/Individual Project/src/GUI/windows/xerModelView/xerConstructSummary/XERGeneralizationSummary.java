/*
 * Created on 20-Jun-2004
 *
 */
package GUI.windows.xerModelView.xerConstructSummary;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;

import xtractor.schemaConverter.xer.xerConstructs.XERGeneralization;
import GUI.windows.xerModelView.XERModelFrameController;

/**
 * @author ss401
 *
 */
public class XERGeneralizationSummary extends XERConstructSummary {

	XERGeneralization generalization;
	/**
	 * @param c
	 * @param generalization
	 */
	public XERGeneralizationSummary(XERModelFrameController c, XERGeneralization generalization) {
		super(c);
		this.generalization = generalization;
		setup();
	}
	protected void setup() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// Relationship Name
		JLabel generalizationNameLabel = new JLabel("Name: ");
		generalizationNameLabel.setPreferredSize(new Dimension(50, 50));
		c.gridx = 0;
		c.gridy = 0;
		this.add(generalizationNameLabel, c);
		JTextField geeneralizationNameField = new JTextField(generalization.getName());
		geeneralizationNameField.setPreferredSize(new Dimension(180, 20));
		geeneralizationNameField.addKeyListener(new ConstructNameListener(generalization));
		c.gridx = 1;
		c.gridy = 0;
		this.add(geeneralizationNameField, c);

	}

}
