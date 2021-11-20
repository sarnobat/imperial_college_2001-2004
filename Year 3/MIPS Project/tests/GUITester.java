import yams.GUI.ProgramCodePanel;
import yams.GUI.RegistersPanel;
import yams.GUI.graphs.RegisterGraphPanel;

/*
 * Created on 11-Dec-2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author ss401
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GUITester {

	public static void main(String[] args) {
		long[] maxCounts = {12340, 268435460, 268468224};
		long totalMaxCounts = 536916024;
		long windowHeight = 330;
		int rowHeight = (int) (((double) 268435460/536916024) * 330);//maxCounts[1] / totalMaxCounts;// * windowHeight;
		System.out.println(rowHeight);
		
		ProgramCodePanel p = new ProgramCodePanel(null);
		RegistersPanel r = new RegistersPanel(null,null);
		
	}
}
