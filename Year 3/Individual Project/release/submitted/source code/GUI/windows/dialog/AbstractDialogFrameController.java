package GUI.windows.dialog;

/*
 * Created on 01-Jun-2004
 *
 */

import java.io.Writer;

import GUI.GUIController;
import GUI.windows.AbstractFrameController;

/**
 * @author ss401
 *
 */
public abstract class AbstractDialogFrameController extends AbstractFrameController {

	protected DialogArea dialog;
	protected DialogWriter dialogWriter;

	/**
	 * @param c
	 */
	public AbstractDialogFrameController(GUIController c) {
		super(c);

		this.dialog = new DialogArea(this);
		this.dialogWriter = new DialogWriter(dialog);
	}
	/**
	 * @return
	 */
	public Writer getWriter() {
		return dialogWriter;
	}
	
	

	/* (non-Javadoc)
	 * @see GUI.windows.AbstractFrameController#disposeFrame()
	 */
	public final void disposeFrame() {
		frame.dispose();

	}

}
