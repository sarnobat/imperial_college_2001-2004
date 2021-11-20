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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
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
public class DatabaseConfigFrame extends PopupFrame {

	Logger logger = Logger.getLogger(this.getClass());

	Element rootElement;
	Document settingsDocument;
	File dbConfigFile;

	JTextField driverField;
	JTextField urlField;
	JTextField usernameField;
	JPasswordField passwordField;

	JButton setConfigButton;

	/**
	 * @param root
	 */
	public DatabaseConfigFrame(GUIController c) {
		super(c);
		
		this.dbConfigFile = new File(GUIController.absoluteFolderPath + "/config/databaseConnection.xml");
		this.settingsDocument = JDomUtilities.getDocument(dbConfigFile);
		this.rootElement = settingsDocument.getRootElement();
		createFrame();
	}

	private void createFrame() {

		this.setTitle("Configure Database");

		Container pane = this.getContentPane();

		this.setSize(400, 200);
		//this.setLocation(c.locationX + 100, c.locationY + 100);
		this.setResizable(false);

		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		Element driverElement = rootElement.getChild("driver");
		Element urlElement = rootElement.getChild("url");
		Element usernameElement = rootElement.getChild("username");
		Element passwordElement = rootElement.getChild("password");

		JLabel driverLabel = new JLabel("Database Driver");
		JLabel urlLabel = new JLabel("Database URL");
		JLabel usernameLabel = new JLabel("Username");
		JLabel passwordLabel = new JLabel("Password");

		this.driverField = new JTextField(driverElement.getText());
		this.urlField = new JTextField(urlElement.getText());
		this.usernameField = new JTextField(usernameElement.getText());
		this.passwordField = new JPasswordField(passwordElement.getText());
		this.setConfigButton = new JButton("Save Configuration");

		driverField.addKeyListener(new ValueListener(driverElement));
		urlField.addKeyListener(new ValueListener(urlElement));
		usernameField.addKeyListener(new ValueListener(usernameElement));
		passwordField.addKeyListener(new ValueListener(passwordElement));
		setConfigButton.addActionListener(new SaveListener());

		Dimension d = new Dimension(150, 25);

		c.gridx = 0;
		c.gridy = 0;
		pane.add(driverLabel, c);
		c.gridx = 1;
		c.gridy = 0;
		driverField.setPreferredSize(d);
		pane.add(driverField, c);

		c.gridx = 0;
		c.gridy = 1;
		pane.add(urlLabel, c);
		c.gridx = 1;
		c.gridy = 1;
		pane.add(urlField, c);
		urlField.setPreferredSize(d);

		c.gridx = 0;
		c.gridy = 2;
		pane.add(usernameLabel, c);
		c.gridx = 1;
		c.gridy = 2;
		pane.add(usernameField, c);
		usernameField.setPreferredSize(d);

		c.gridx = 0;
		c.gridy = 3;
		pane.add(passwordLabel, c);
		c.gridx = 1;
		c.gridy = 3;
		pane.add(passwordField, c);
		passwordField.setPreferredSize(d);

		c.gridx = 1;
		c.gridy = 4;
		pane.add(setConfigButton, c);
		//passwordField.setPreferredSize(new Dimension(70,20));

	}

	class ValueListener implements KeyListener {

		Element xmlElement;
		public ValueListener(Element xmlElement) {
			this.xmlElement = xmlElement;
		}

		public void keyReleased(KeyEvent e) {
			JTextField textBox = (JTextField) e.getSource();
			String newName = textBox.getText();

			xmlElement.setText(newName);
		}

		public void keyTyped(KeyEvent e) {
		}
		public void keyPressed(KeyEvent e) {
		}

	}

	class SaveListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			try {
				FileWriter fileWriter = new FileWriter(dbConfigFile);
				//rdbWalker = new RDBWalker(fileWriter,databaseSchemaName);
				
				XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
				
				xmlOutputter.output(settingsDocument, fileWriter);
				dispose();
			}
			catch (IOException e1) {
				String error = "Couldn't save database settings." + e;
				logger.error(error);
				System.out.println(error);
			}
		}
	}
}
