/*
 * Created on 01-Jun-2004
 *
 */
package GUI.windows.schemaSelector;

import org.apache.log4j.Logger;

import GUI.GUIController;
import GUI.windows.XTractorFrame;

/**
 * @author ss401
 *
 */
public abstract class AbstractSelectSchemaFrame extends XTractorFrame {

	Logger logger = Logger.getLogger(this.getClass());

	AbstractSchemaSelectorController dataImportController;

	/**
	 * @param c
	 */
	public AbstractSelectSchemaFrame(GUIController c, AbstractSchemaSelectorController dataImporterController) {
		super(c);
		this.dataImportController = dataImporterController;
		setup();
	}

	protected void setup() {
	}


}
