/*
 * Created on 31-May-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package GUI.windows.dialog;

import java.io.IOException;
import java.io.Writer;

/**
 * @author ss401
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DialogWriter extends Writer {

	DialogArea dialog;

	//String buffer = "";

	/**
	 * @param out
	 */
	public DialogWriter(DialogArea dialog) {
		this.dialog = dialog;
	}

	public void write(char[] cbuf, int off, int len) throws IOException {
	}

	public void write(String s) {
		dialog.append(s);
		/*dialog.invalidate();
		dialog.repaint();*/
		dialog.select(dialog.getText().length(),dialog.getText().length());
	}

	public void flush() throws IOException {
	}

	/* (non-Javadoc)
	 * @see java.io.Writer#close()
	 */
	public void close() throws IOException {
	}

}
