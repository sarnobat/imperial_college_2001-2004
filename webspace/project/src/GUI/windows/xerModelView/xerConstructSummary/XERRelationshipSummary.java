/*
 * Created on 31-May-2004
 *
 */
package GUI.windows.xerModelView.xerConstructSummary;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;

import xtractor.schemaConverter.xer.xerConstructs.XERCompoundConstruct;
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
		/*info = new JLabel(relationship.getParent().getName() + " --> " + relationship.getChild().getName());
		this.add(info);*/

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// Relationship Name
		JLabel entityNameLabel = new JLabel("Name: ");
		entityNameLabel.setPreferredSize(new Dimension(50, 50));
		c.gridx = 0;
		c.gridy = 0;
		this.add(entityNameLabel, c);
		JTextField entityNameField = new JTextField(relationship.getName());
		entityNameField.setPreferredSize(new Dimension(180, 20));
		entityNameField.addKeyListener(new ConstructNameListener(relationship));
		c.gridx = 1;
		c.gridy = 0;
		this.add(entityNameField, c);

		Dimension d = new Dimension(50, 20);
		String htmlBegin = "<html><b>";
		String htmlEnd = "</b></html>";

		// Parent information
		XERCompoundConstruct parent = relationship.getParent();
		JLabel parentName = new JLabel(htmlBegin + parent.getName() + htmlEnd);
		c.gridx = 0;
		c.gridy++;
		this.add(parentName, c);
		JLabel minOccursLabel = new JLabel("Minimum occurrences: ");
		c.gridx = 0;
		c.gridy++;
		this.add(minOccursLabel, c);
		String s = (new Integer(1)).toString();
		JTextField minOccursValue = new JTextField(s);
		minOccursValue.setPreferredSize(d);
		c.gridx = 1;
		this.add(minOccursValue, c);
		JLabel maxOccursLabel = new JLabel("Maximum occurrences: ");
		c.gridx = 0;
		c.gridy++;
		this.add(maxOccursLabel, c);
		JTextField maxOccursValue = new JTextField(s);
		maxOccursValue.setPreferredSize(d);
		c.gridx = 1;
		this.add(maxOccursValue, c);

		// Child information
		XERCompoundConstruct child = relationship.getChild();
		JLabel childName = new JLabel(htmlBegin + child.getName() + htmlEnd);
		c.gridx = 0;
		c.gridy++;
		this.add(childName, c);
		JLabel minOccursLabel2 = new JLabel("Minimum occurrences: ");
		c.gridx = 0;
		c.gridy++;
		this.add(minOccursLabel2, c);
		JTextField minOccursValue2 = new JTextField(new Integer(relationship.getMinOccurs()).toString());
		minOccursValue2.setPreferredSize(d);
		c.gridx = 1;
		this.add(minOccursValue2, c);
		JLabel maxOccursLabel2 = new JLabel("Maximum occurrences: ");
		c.gridx = 0;
		c.gridy++;
		this.add(maxOccursLabel2, c);
		JTextField maxOccursValue2 = new JTextField(new Integer(relationship.getMaxOccurs()).toString());
		maxOccursValue2.setPreferredSize(d);
		c.gridx = 1;
		this.add(maxOccursValue2, c);
	}

}
