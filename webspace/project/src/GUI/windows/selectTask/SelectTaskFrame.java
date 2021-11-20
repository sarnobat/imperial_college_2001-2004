/*
 * Created on 31-May-2004
 */
package GUI.windows.selectTask;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import GUI.GUIController;
import GUI.windows.XTractorFrame;

/**
 * @author ss401
 *
 */
public class SelectTaskFrame extends XTractorFrame {

	Logger logger = Logger.getLogger(this.getClass());

	JFileChooser fileChooser;
	JButton buttonGenerateRDB;
	JButton buttonImportData;
	JButton buttonExportData;
	JButton buttonViewDB;

	String htmlBegin = "<html>"; //"<html><p align=center>";
	String htmlEnd = "<html>"; //"</p><html>";
	/**
	 * @param c
	 */
	public SelectTaskFrame(GUIController c) {
		super(c);

		setup();
	}

	protected void setup() {
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());

		fileChooser = new JFileChooser("./xml/");
		fileChooser.addActionListener(new FileChooserListener());
		XMLFileFilter filter = new XMLFileFilter();
		fileChooser.setFileFilter(new XMLFileFilter());

		buttonGenerateRDB = new JButton("Create Relational Schema");
		buttonImportData = new JButton("Import XML Data");
		buttonExportData = new JButton("Export XML Data");
		buttonViewDB = new JButton("View Database");

		JLabel labelGenerateRDB = new JLabel(htmlBegin + "Create a relational database schema from an XML Schema." + htmlEnd);
		JLabel labelImportData =
			new JLabel(
				htmlBegin
					+ "Add a the data in an XML Document to a relational database that has been generated from this XML document's schema."
					+ htmlEnd);
		JLabel labelExportData =
			new JLabel(htmlBegin + "Reconstruct an XML Document that was imported into the database." + htmlEnd);
		JLabel labelViewDB = new JLabel(htmlBegin + "View a relational database that was created from an XML Schema." + htmlEnd);

		buttonGenerateRDB.addActionListener(new GenerateRDBListener());
		buttonImportData.addActionListener(new ImportDataListener());
		buttonExportData.addActionListener(new ExportDataListener());
		buttonViewDB.addActionListener(new ViewDBListener());

		Dimension d = new Dimension(width * 2 / 3, height / 14 + 10);

		labelGenerateRDB.setPreferredSize(d);
		labelImportData.setPreferredSize(d);
		labelExportData.setPreferredSize(d);
		labelViewDB.setPreferredSize(d);

		labelGenerateRDB.setMinimumSize(d);
		labelImportData.setMinimumSize(d);
		labelExportData.setMinimumSize(d);
		labelViewDB.setMinimumSize(d);

		buttonGenerateRDB.setEnabled(false);
		buttonImportData.setEnabled(false);

		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		pane.add(fileChooser, c);

		c.gridwidth = 1;

		c.weightx = 0;
		c.gridx = 1;
		c.gridy = 1;
		pane.add(buttonGenerateRDB, c);
		c.gridy++;
		pane.add(buttonImportData, c);
		c.gridy++;
		pane.add(buttonExportData, c);
		c.gridy++;
		pane.add(buttonViewDB, c);

		c.weightx = 1000;
		c.gridx = 0;
		c.gridy = 1;
		pane.add(labelGenerateRDB, c);
		c.gridy++;
		pane.add(labelImportData, c);
		c.gridy++;
		pane.add(labelExportData, c);
		c.gridy++;
		pane.add(labelViewDB,c);

	}

	class GenerateRDBListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			File xsdFile = fileChooser.getSelectedFile();
			if (xsdFile != null) {
				guiController.launchXERFrame(xsdFile);
			}
		}
	}
	class ImportDataListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			File xmlFile = fileChooser.getSelectedFile();
			if (xmlFile != null) {
				guiController.launchImportSelectorFrame(xmlFile);
			}
		}
	}
	class ExportDataListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			guiController.launchExportSchemaSelectorFrame();
		}
	}
	
	
	class ViewDBListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			guiController.launchViewDatabaseSelectionFrame();
		}
	}
	class FileChooserListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (fileChooser.getSelectedFile() != null) {
				File f = fileChooser.getSelectedFile();
				if (f.getName().endsWith(".xsd") || f.getName().endsWith(".xsd.xml")) {
					buttonGenerateRDB.setEnabled(true);
					buttonImportData.setEnabled(false);
				}
				else if (f.getName().endsWith(".xml")) {
					buttonImportData.setEnabled(true);
					buttonGenerateRDB.setEnabled(false);
				}
			}
		}
	}

	class XMLFileFilter extends FileFilter {

		public boolean accept(File file) {
			if (file.getName().endsWith(".xml") || file.getName().endsWith(".xsd") || file.isDirectory()) {
				return true;
			}
			return false;
		}

		public String getDescription() {
			return "*.xml, *.xsd";
		}
	}

}
