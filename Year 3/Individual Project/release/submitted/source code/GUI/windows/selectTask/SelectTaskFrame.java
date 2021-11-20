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

	String htmlBegin = "<html>";//"<html><p align=center>";
	String htmlEnd = "<html>";//"</p><html>";
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

		JLabel generateRDBLabel = new JLabel(htmlBegin + "Create a relational database schema from an XML Schema." + htmlEnd);
		JLabel importDataLabel =
			new JLabel(
				htmlBegin
					+ "Add a the data in an XML Document to a relational database that has been generated from this XML document's schema."
					+ htmlEnd);
		JLabel exportDataLabel =
			new JLabel(htmlBegin + "Reconstruct an XML Document that was imported into the database." + htmlEnd);

		buttonGenerateRDB.addActionListener(new GenerateRDBListener());
		buttonImportData.addActionListener(new ImportDataListener());
		buttonExportData.addActionListener(new ExportDataListener());

		Dimension d = new Dimension(width * 2 / 3, height / 14);
		//Dimension d = new Dimension(width , height );

		generateRDBLabel.setPreferredSize(new Dimension(d));
		importDataLabel.setPreferredSize(new Dimension(d));
		exportDataLabel.setPreferredSize(new Dimension(d));
		
		generateRDBLabel.setMinimumSize(new Dimension(d));
				importDataLabel.setMinimumSize(new Dimension(d));
				exportDataLabel.setMinimumSize(new Dimension(d));

		buttonGenerateRDB.setEnabled(false);
		buttonImportData.setEnabled(false);

		
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		pane.add(fileChooser, c);

		//c.weightx = 0;
		//c.weighty = 0;
		//c.gridx = 1;
		//c.gridy = 1;
		c.gridwidth = 1;

		c.weightx = 0;
		c.gridx = 1;
		c.gridy = 1;
		pane.add(buttonGenerateRDB, c);
		c.gridy = 2;
		pane.add(buttonImportData, c);
		c.gridy = 3;
		pane.add(buttonExportData, c);

		c.weightx = 1000;		
		c.gridx = 0;
		c.gridy = 1;
		pane.add(generateRDBLabel, c);
		c.gridy = 2;
		pane.add(importDataLabel, c);
		c.gridy = 3;
		pane.add(exportDataLabel, c);

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
