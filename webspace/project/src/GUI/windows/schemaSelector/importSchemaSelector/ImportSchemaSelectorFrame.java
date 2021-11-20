/*
 * Created on 01-Jun-2004
 *
 */
package GUI.windows.schemaSelector.importSchemaSelector;

import java.io.File;

import GUI.GUIController;
import GUI.windows.schemaSelector.AbstractSchemaSelectorController;
import GUI.windows.schemaSelector.AbstractSelectSchemaFrame;

/**
 * @author ss401
 *
 */
public class ImportSchemaSelectorFrame extends AbstractSelectSchemaFrame {
	File xmlFile;
	/**
	 * @param c
	 * @param dataImporterController
	 * @param xmlFile
	 */
	public ImportSchemaSelectorFrame(GUIController c, AbstractSchemaSelectorController dataImporterController, File xmlFile) {
		super(c, dataImporterController);
		this.xmlFile = xmlFile;
	}

}
