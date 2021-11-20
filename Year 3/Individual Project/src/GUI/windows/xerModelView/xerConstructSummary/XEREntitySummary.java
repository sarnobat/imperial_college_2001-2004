/*
 * Created on 31-May-2004
 *
 */
package GUI.windows.xerModelView.xerConstructSummary;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import xtractor.schemaConverter.xer.xerConstructs.XERAttribute;
import xtractor.schemaConverter.xer.xerConstructs.XEREntity;
import xtractor.schemaConverter.xer.xerConstructs.XERMixedEntity;
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
			attributeNameField.setPreferredSize(new Dimension(100, 20));
			attributeNameField.addKeyListener(new ConstructNameListener(attribute));
			c.gridx = 0;
			this.add(attributeNameField, c);
			JLabel attributeTypeLabel = new JLabel(attribute.getType());
			c.gridx = 1;
			this.add(attributeTypeLabel, c);
		}

		c.gridy++;
		if (entity instanceof XERMixedEntity) {
			XERMixedEntity mixedEntity = (XERMixedEntity) entity;
			JCheckBox checkEnableMixedContent = new JCheckBox("Include Mixed Content.", mixedEntity.isMixedContentEnabled());
			checkEnableMixedContent.addActionListener(new EnableMixedContentListener(mixedEntity));

			c.gridx = 1;
			c.gridwidth = 2;
			this.add(checkEnableMixedContent, c);

		}
		c.gridwidth = 1;
		c.gridy++;
	}

	class EnableMixedContentListener implements ActionListener {
		XERMixedEntity mixedEntity;
		public EnableMixedContentListener(XERMixedEntity entity) {
			mixedEntity = entity;
		}

		public void actionPerformed(ActionEvent e) {
			JCheckBox checkBox = (JCheckBox) e.getSource();
			if (checkBox.isSelected()) {
				//mixedEntity.demote();
				mixedEntity.setPromoted();

			}
			else {
				mixedEntity.setDemoted();
			}

			logger.debug("Mixed content enabled is now " + mixedEntity.isMixedContentEnabled());
		}

	}

}
