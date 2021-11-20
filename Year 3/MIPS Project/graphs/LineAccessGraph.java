import javax.swing.JEditorPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/*
 * Created on 10-Dec-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

/**
 * @author ss401
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class LineAccessGraph extends JEditorPane {
	int[] lineCounts = { 21, 3, 5, 43, 21, 6, 43, 642, 43, 21, 6, 43, 642 , 43, 21, 6, 43, 642 , 642 , 43, 21, 6, 43, 642 , 43, 21, 6, 43, 642 , 43, 21, 6, 43, 642 , 43, 21, 6, 43, 642 , 43, 21, 6, 43 };
	String content = "";
	private int windowHeight;
	private int windowWidth;
	private int barHeight = 4;
	private String imageURL = "http://web.mit.edu/civenv/K12Edu/images/bluedot.jpg";

	public LineAccessGraph() {
		super("text/html", "");
		this.windowHeight = StatisticsVisualizer.WINDOW_HEIGHT;
		this.windowWidth = StatisticsVisualizer.WINDOW_WIDTH;
		render();
		this.setText(StatisticsVisualizer.preAmble+content);
		
	}
	private void render() {
		content += "<table><tr><td><b>Line Number</b></td><td><b>Execution Count</b></td></tr>";
		int maxCount = max(lineCounts);
		for (int i = 0; i < lineCounts.length; i++) {
			int count = lineCounts[i];
			content += "<tr><td>" + (i + 1) + " (" + count + ")</td><td align=left>";
			if ((windowWidth * count / maxCount) > 0) {
				content += "<img src="
					+ imageURL
					+ " width="
					+ (windowWidth * count / maxCount)
					+ " height="
					+ barHeight
					+ ">";
			}
			content += "</td></tr>";

			//System.out.println(windowWidth * count / maxCount);
		}
		content += "</table>";
		System.out.println(content);
	}
	private int max(int[] counts) {
		int max = 0;
		for (int i = 0; i < counts.length; i++) {
			if (counts[i] > max) {
				max = counts[i];
			}
		}
		return max;
	}

}
