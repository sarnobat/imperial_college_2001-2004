import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JEditorPane;
import javax.swing.JFrame;

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
public class LineAccessGraph {
	
	private static String imageURL = "http://web.mit.edu/civenv/K12Edu/images/bluedot.jpg";
	private static int widthMultiple = 4; 
	private static int barHeight = 10;
	private static int windowWidth = 400;
	
	public static void main(String[] args) {
		render();
	}
	private static int max(int[] counts) {
		int max = 0;
		for (int i = 0; i < counts.length; i++) {
			if (counts[i] > max) {
				max = counts[i];
			}	
		}
		return max;
	}
	private static void render(){
		int[] lineCounts = {21,3,5,43,21,6,43,6432};
		//There is no need for for the list of line names to be passed, for obvious reasons 
		openFrame(getContent(lineCounts));
	}
	private static String getContent(int[] lineCounts) {
		String content = "<table><tr><td>Line Number</td><td>Execution Count";
		int maxCount = max(lineCounts);
		for (int i = 0; i < lineCounts.length; i++) {
			int count = lineCounts[i];
			content += "<tr><td>" + (i+1) +" ("+ count+")</td><td align=left><img src=" + imageURL + " width=" + (windowWidth*count/maxCount) + " height=" + barHeight +"></td></tr>"; 
		}
		content += "</table>";
		return content;
	}
	private static void openFrame(String htmlCode) {
		JFrame f = new JFrame("Register Usage Historgram");

		f.getContentPane().setLayout(new FlowLayout());

		JEditorPane t = new JEditorPane("text/html", "<html>" + htmlCode + "</html>");

		t.setPreferredSize(new Dimension(windowWidth+80, 450));
		t.setEditable(false);
		t.setVisible(true);
		f.getContentPane().add(t);
		f.pack();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setResizable(false);
		f.setVisible(true);

		System.out.print(t.getText());
	}

}
