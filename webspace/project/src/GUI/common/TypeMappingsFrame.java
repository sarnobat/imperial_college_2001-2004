/*
 * Created on 10-Jun-2004
 *
 */
package GUI.common;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import xtractor.schemaConverter.xsd.JDomUtilities;
import GUI.GUIController;

/**
 * @author ss401
 *
 */
public class TypeMappingsFrame extends PopupFrame {

	protected Logger logger = Logger.getLogger(this.getClass());

	Element rootElement;
	Document settingsDocument;
	protected File typesMappingsFile;

	/**
	 * @param c
	 */
	public TypeMappingsFrame(GUIController c) {
		super(c);
		this.typesMappingsFile = new File(GUIController.absoluteFolderPath + "/config/typeMappings.xml");
		this.settingsDocument = JDomUtilities.getDocument(typesMappingsFile);
		this.rootElement = settingsDocument.getRootElement();
		createFrame();

	}

	private void createFrame() {

		this.setTitle("Type Mappings");
		Container pane = this.getContentPane();
		JPanel mappingsPanel = new JPanel();
		JScrollPane jsp = new JScrollPane(mappingsPanel);
		GridBagConstraints c = new GridBagConstraints();

		jsp.setPreferredSize(new Dimension(350, 400));
		pane.setLayout(new GridBagLayout());
		mappingsPanel.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		//pane.add(jsp,c);
		pane.add(jsp, c);

		c.gridx = 0;
		c.gridy = 1;
		mappingsPanel.add(new JLabel("<html><b>XML SCHEMA TYPE</b><br></html>"),c);

		c.gridx = 1;
		c.gridy = 1;
		mappingsPanel.add(new JLabel("<html><b>SQL TYPE</b><br></html>"),c);

		int y = 2;
		List types = rootElement.getChildren();
		for (Iterator iter = types.iterator(); iter.hasNext();) {
			Element mappingElement = (Element) iter.next();
			Element xsdTypeElement = mappingElement.getChild("xsd_type");
			Element sqlTypeElement = mappingElement.getChild("sql_type");

			JLabel label = new JLabel(xsdTypeElement.getText());
			JTextField field = new JTextField(sqlTypeElement.getText());
			field.setPreferredSize(new Dimension(100, 25));
			field.addKeyListener(new ValueListener(sqlTypeElement));

			c.gridx = 0;
			c.gridy = y;
			mappingsPanel.add(label, c);
			c.gridx = 1;
			mappingsPanel.add(field, c);
			y++;
		}
		JButton saveMappingsButton = new JButton("Save mappings");
		saveMappingsButton.addActionListener(new SaveListener());
		c.gridx = 0;
		c.gridy = 1;
		pane.add(saveMappingsButton, c);
		this.pack();
	}

	class ValueListener implements KeyListener {

		Element xmlElement;
		public ValueListener(Element xmlElement) {
			this.xmlElement = xmlElement;
		}

		public void keyReleased(KeyEvent e) {
			JTextField textBox = (JTextField) e.getSource();
			String newSQLType = textBox.getText();
			xmlElement.setText(newSQLType);
		}

		public void keyTyped(KeyEvent e) {
		}

		public void keyPressed(KeyEvent e) {
		}
	}

	class SaveListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			try {
				FileWriter fileWriter = new FileWriter(typesMappingsFile);

				XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

				xmlOutputter.output(settingsDocument, fileWriter);
				dispose();
			}
			catch (IOException e1) {
				String error = "Couldn't save settings." + e;
				logger.error(error);
				System.out.println(error);
			}
		}
	}

}
