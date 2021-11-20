/*
 * Created on 31-May-2004
 *
 */
package GUI.windows.xerModelView;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import xtractor.schemaConverter.xer.XERModel;
import xtractor.schemaConverter.xer.xerConstructs.XERAttribute;
import xtractor.schemaConverter.xer.xerConstructs.XERCompoundConstruct;
import xtractor.schemaConverter.xer.xerConstructs.XERConstruct;
import xtractor.schemaConverter.xer.xerConstructs.XEREntity;
import xtractor.schemaConverter.xer.xerConstructs.XERGeneralization;
import xtractor.schemaConverter.xer.xerConstructs.XERRelationship;
import GUI.GUIController;
import GUI.windows.AbstractFrameController;
import GUI.windows.xerModelView.treeRenderer.XERTreeyRenderer;
import GUI.windows.xerModelView.xerTreeComponents.XERAttributeNode;
import GUI.windows.xerModelView.xerTreeComponents.XERCategoryNode;
import GUI.windows.xerModelView.xerTreeComponents.XEREntityNode;
import GUI.windows.xerModelView.xerTreeComponents.XERGeneralizationNode;
import GUI.windows.xerModelView.xerTreeComponents.XERNode;
import GUI.windows.xerModelView.xerTreeComponents.XERRelationshipNode;
import GUI.windows.xerModelView.xerTreeComponents.XERRootNode;

/**
 * @author ss401
 *
 */
public class XERModelFrameController extends AbstractFrameController {

	Logger logger = Logger.getLogger(this.getClass());

	//GUIController guiController;

	// GUI Components

	//public XERModelFrame frame;
	public XERTree xerTree;
	public SummaryPanel summary;
	public CreateRDBButton createRDBButton;
	public JTextField commentField;
	public JLabel label;

	// Objects
	XERModel xerModel;
	File xsdFile;

	public XERModelFrameController(GUIController guiController, XERModel xerModel) {

		super(guiController);

		this.frame = new XERModelFrame(this, guiController);
		this.xerModel = xerModel;

		this.summary = new SummaryPanel(this);
		this.createRDBButton = new CreateRDBButton(this, "Create Relational Database");
		this.commentField = new JTextField("");
		this.label = new JLabel("Enter a comment regarding this schema and press the button to proceed.");
		label.setPreferredSize(new Dimension(100,20));
		showFrame();

	}

	public void showFrame() {
		Container pane = frame.getContentPane();

		populateTree();

		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.4;
		c.weighty = 1;
		//c.gridheight = 2;
		c.gridheight = 1;
		pane.add(xerTree, c);

		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 1;
		//c.weightx = 0.66;
		//c.weighty = 1;
		pane.add(summary, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridheight = 1;
		c.gridwidth = 2;
		c.weighty = 0;
		pane.add(label, c);

		c.gridx = 0;
		c.gridy = 2;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		pane.add(commentField, c);

		c.gridx = 1;
		c.gridy = 2;
		c.gridheight = 1;
		c.weightx = 0.6;
		c.weighty = 0;
		c.gridheight = 1;
		pane.add(createRDBButton, c);

		frame.show();

	}

	private void populateTree() {
		XERRootNode rootNode = new XERRootNode(this, xerModel);
		this.xerTree = new XERTree(this, rootNode);
		//xerTree.expandPath();

		xerTree.setCellRenderer(new XERTreeyRenderer());
		// Entities
		XERCategoryNode entitiesNode = new XERCategoryNode("Entities");
		rootNode.add(entitiesNode);
		Collection entities = xerModel.getEntities();
		for (Iterator iter = entities.iterator(); iter.hasNext();) {
			XEREntity entity = (XEREntity) iter.next();
			addEntityToTree(entity, entitiesNode);
		}

		// Generalizations
		XERCategoryNode generalizationsNode = new XERCategoryNode("Generlalizations");
		rootNode.add(generalizationsNode);
		Collection generalizations = xerModel.getGeneralizations();
		for (Iterator iter = generalizations.iterator(); iter.hasNext();) {
			XERGeneralization generalization = (XERGeneralization) iter.next();
			//XERGeneralizationNode generalizationNode = new XERGeneralizationNode(generalization);
			addGeneralizationToTree(generalization, generalizationsNode);

		}

		// Relationships
		XERCategoryNode relationshipsNode = new XERCategoryNode("Relationships");
		rootNode.add(relationshipsNode);
		Collection relationships = xerModel.getRelationships();
		for (Iterator iter = relationships.iterator(); iter.hasNext();) {
			XERRelationship relationship = (XERRelationship) iter.next();
			addRelationshipToTree(relationship, relationshipsNode);
		}
	}

	public XERModel getXERModel() {
		return xerModel;
	}

	public void updateObjectSummary(Object construct, XERNode node) {
		summary.removeAll();
		summary.addSummary(construct);
		// Both of these are needed.			
		frame.update(frame.getGraphics());
		summary.validate();

		logger.debug("Updated summary panel. Now displaying info for " + construct);

	}

	public void createRelationalDatabase() {
		mainGUIController.launchRDBCreationDialog(xerModel, commentField.getText());
	}

	private void addEntityToTree(XEREntity entity, XERNode parentNode) {
		XEREntityNode entityNode = new XEREntityNode(entity);
		parentNode.add(entityNode);
		Collection attributes = entity.getAttributes();
		for (Iterator iter = attributes.iterator(); iter.hasNext();) {
			XERAttribute attribute = (XERAttribute) iter.next();
			addAttributeToTree(attribute, entityNode);
		}
	}

	private void addAttributeToTree(XERAttribute attribute, XERNode parentNode) {
		parentNode.add(new XERAttributeNode(attribute));

	}

	private void addGeneralizationToTree(XERGeneralization generalization, XERNode parent) {
		XERGeneralizationNode generalizationNode = new XERGeneralizationNode(generalization);

		Collection specializations = generalization.getSpecializations();
		for (Iterator iterator = specializations.iterator(); iterator.hasNext();) {
			XERConstruct construct = (XERConstruct) iterator.next();
			if (construct instanceof XEREntity) {
				addEntityToTree((XEREntity) construct, generalizationNode);
			}
			else if (construct instanceof XERAttribute) {
				addAttributeToTree((XERAttribute) construct, generalizationNode);
			}
		}

		parent.add(generalizationNode);
	}

	private void addRelationshipToTree(XERRelationship relationship, XERNode parentNode) {

		XERRelationshipNode relationshipNode = new XERRelationshipNode(relationship);
		parentNode.add(relationshipNode);

		// Parent
		XERCompoundConstruct parent = relationship.getParent();
		if (parent instanceof XEREntity) {
			addEntityToTree((XEREntity) parent, relationshipNode);
		}
		else if (parent instanceof XERGeneralization) {
			addGeneralizationToTree((XERGeneralization) parent, relationshipNode);
		}

		// Child
		XERCompoundConstruct child = relationship.getChild();
		if (child instanceof XEREntity) {
			addEntityToTree((XEREntity) child, relationshipNode);
		}
		else if (child instanceof XERGeneralization) {
			addGeneralizationToTree((XERGeneralization) child, relationshipNode);
		}

	}
}
