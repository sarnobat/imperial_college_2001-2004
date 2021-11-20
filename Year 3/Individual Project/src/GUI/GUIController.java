/*
 * Created on 31-May-2004
 */
package GUI;

import java.io.File;
import java.io.Writer;
import java.util.Map;

import org.apache.log4j.Logger;

import xtractor.FolderDiscloser;
import xtractor.XTractor;
import xtractor.schemaConverter.xer.XERModel;
import GUI.windows.dialog.dataPopulator.DataPopulatorController;
import GUI.windows.dialog.rdbCreator.RDBCreationFrameController;
import GUI.windows.dialog.regenerator.RegeneratorController;
import GUI.windows.schemaSelector.AbstractSchemaSelectorController;
import GUI.windows.schemaSelector.exportSchemaSelector.ExportSchemaSelectorController;
import GUI.windows.schemaSelector.importSchemaSelector.ImportSchemaSelectorController;
import GUI.windows.schemaSelector.viewDBSchemaSelector.ViewDBSchemaSelectorController;
import GUI.windows.selectDocument.SelectDocumentController;
import GUI.windows.selectTask.SelectTaskFrameController;
import GUI.windows.viewDatabase.DatabaseViewerController;
import GUI.windows.xerModelView.XERModelFrameController;

/**
 * @author ss401
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GUIController {

	Logger logger = Logger.getLogger(this.getClass());

	// Dimensions
	public static int height = 600;
	public static int width = 610;
	public static int locationX = 200;
	public static int locationY = 100;

	// Frame controllers
	SelectTaskFrameController taskSelectFrameController;
	XERModelFrameController xerWindowController;
	RDBCreationFrameController rdbWindowController;
	DataPopulatorController dataPopulatorController;
	AbstractSchemaSelectorController schemaSelectorController;
	SelectDocumentController documentSelectorController;
	RegeneratorController regeneratorController;
	ViewDBSchemaSelectorController viewDBSchemaSelectController;
	DatabaseViewerController dbViewController;

	File xsdFile;

	XTractorThread thread;

	public static String absoluteFolderPath;

	public GUIController() {
		absoluteFolderPath = FolderDiscloser.workingFolderPath;
		this.thread = new XTractorThread(this);
		launchTaskSelectorFrame();
		thread.run();
	}

	private XERModel createXERModel(File xsdFile) {
		this.xsdFile = xsdFile;
		return XTractor.getXERModel(xsdFile);
	}

	public void launchTaskSelectorFrame() {
		taskSelectFrameController = new SelectTaskFrameController(this);
		taskSelectFrameController.showFrame();
	}

	/*
	 * Creating a schema
	 */
	public void launchXERFrame(File xsdFile) {
		xerWindowController = new XERModelFrameController(this, createXERModel(xsdFile));
		taskSelectFrameController.disposeFrame();
	}
	public void launchRDBCreationDialog(XERModel model,String comment) {
		// demote mixed entites
		//		for (Iterator iter = model.getDemotedMixedEntities().iterator(); iter.hasNext();) {
		//			XERMixedEntity mixedEntity = (XERMixedEntity) iter.next();
		//
		//			if (!mixedEntity.isMixedContentEnabled()) {
		//				mixedEntity.demote();
		//			}
		//		}
		rdbWindowController = new RDBCreationFrameController(this, xsdFile);
		rdbWindowController.showFrame();
		xerWindowController.disposeFrame();
		rdbWindowController.createDatabase(model, thread,comment);
	}

	/*
	 * Importing a document
	 */
	public void launchImportSelectorFrame(File xmlFile) {
		taskSelectFrameController.disposeFrame();
		schemaSelectorController = new ImportSchemaSelectorController(this, xmlFile);
	}

	public void launchImportDialog(String databaseSchemaName, File xmlFile) {
		schemaSelectorController.disposeFrame();
		dataPopulatorController = new DataPopulatorController(this);
		dataPopulatorController.showFrame();
		Writer out = dataPopulatorController.getWriter();
		//XTractor.importData(xmlFile, databaseSchemaName, out);
		thread.requestImportFile(xmlFile, databaseSchemaName, out);
	}
	/*
	 * Exporting a document
	 */
	public void launchExportSchemaSelectorFrame() {
		taskSelectFrameController.disposeFrame();
		schemaSelectorController = new ExportSchemaSelectorController(this);
	}
	public void launchExportDocumentSelector(String databaseSchemaName) {
		schemaSelectorController.disposeFrame();
		documentSelectorController = new SelectDocumentController(this, databaseSchemaName);
	}
	public void launchExportDocumentDialog(String databaseSchema, int selectedDocumentNumber) {
		documentSelectorController.disposeFrame();
		regeneratorController = new RegeneratorController(this);
		//File xmlFile = XTractor.exportData(databaseSchema, selectedDocumentNumber, regeneratorController.getWriter());
		thread.requestExportFile(databaseSchema, selectedDocumentNumber, regeneratorController.getWriter());

	}

	/*
	 * Helper methods
	 */
	public String[][] getDatabaseSchemas() {
		return XTractor.getDatabaseSchemas();
	}

	public String[][] getDocuments(String databaseSchemaName) {
		return XTractor.getDocuments(databaseSchemaName);
	}

	/**
	 * 
	 */
	public void launchViewDatabaseSelectionFrame() {
		taskSelectFrameController.disposeFrame();
		viewDBSchemaSelectController= new ViewDBSchemaSelectorController(this);
	}

	/**
	 * @param selectedSchemaName
	 */
	public void launchDatabaseViewer(String selectedSchemaName) {
		viewDBSchemaSelectController.disposeFrame();
		// collection of maps (from table name to collection of maps (from string to string)) 
		// - see DatabaseViewerController constructor for details
		
		Map tableData = XTractor.getTableData(selectedSchemaName);
		dbViewController = new DatabaseViewerController(this,selectedSchemaName,tableData);
	}

	
}
