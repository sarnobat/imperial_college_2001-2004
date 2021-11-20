/*
 * Created on 31-May-2004
 *
 */
package GUI.windows.xerModelView.xerConstructSummary;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JTextField;

import xtractor.schemaConverter.xer.xerConstructs.XERAttribute;
import xtractor.schemaConverter.xer.xerConstructs.XEREntity;
import GUI.windows.xerModelView.XERModelFrameController;

/**
 * @author ss401
 *
 */
public class XEREntitySummary extends XERConstructSummary {

	XEREntity entity;


	/**
	 * @param c
	 */
	public XEREntitySummary(XERModelFrameController c, XEREntity entity) {
		super(c);
		this.entity = entity;
		setup();
	}

	protected void setup() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// Entity Name
		JLabel entityNameLabel = new JLabel("Name:");
		c.gridx = 0;
		c.gridy = 0;
		this.add(entityNameLabel, c);
		JTextField entityNameField = new JTextField(entity.getName());
		entityNameField.setPreferredSize(new Dimension(100, 20));
		entityNameField.addKeyListener(new ConstructNameListener(entity));
		c.gridx = 1;
		c.gridy = 0;
		this.add(entityNameField, c);
		

		// Attributes
		for (Iterator iter = entity.getAttributes().iterator(); iter.hasNext();) {
			c.gridy++;
			XERAttribute attribute = (XERAttribute) iter.next();
			JTextField attributeNameField = new JTextField(attribute.getName());
			attributeNameField.setPreferredSize(new Dimension(100,20));
			attributeNameField.addKeyListener(new ConstructNameListener(attribute));
			c.gridx = 0;
			this.add(attributeNameField,c);
			JLabel attributeTypeLabel = new JLabel(attribute.getType());
			c.gridx = 1;
			this.add(attributeTypeLabel,c);
		}
	}

}
