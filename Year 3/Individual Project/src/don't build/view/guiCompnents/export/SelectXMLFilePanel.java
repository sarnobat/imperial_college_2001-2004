/*
 * Created on 17-May-2004
 *
 */
package view.guiCompnents.export;

iK ort javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

/**
 * @author ss401
 *
 */
public class SelectXMLFilePanel extends JPanel{
	
	Logger logger = Logger.getLogger(this.getClass());
	
	JLabel label;
	
	public SelectXMLFilePanel(){
		label = new JLabel("Please select an XML file to regenerate.");
		this.add(label);
	}
}
