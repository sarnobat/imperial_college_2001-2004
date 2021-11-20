import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;

/*
 * Created on 09-Dec-2003
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
public class RegisterGraphsOLD {

	private static int heightMultiple = 3;
	private static int totalMaxCounts;
	
	private static int WINDOW_HEIGHT;
	private static int fontSize = 3;
	
	private static String htmlCode = "";

	public static void main(String[] args) {
		try {
			render();
		} catch (Exception e) {
			System.out.println("Could not create graphs window, because supplied array lengths don't match");
		}
	}
	
	private static void render() throws Exception {

//		TODO: Make images accessible locally, not via the web
		
		int[] sRegCounts = { 3, 6, 4, 2, 1, 0, 1, 2 };
		String[] sRegNames = { "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7" };
		int[] aRegCounts = { 3, 6, 4, 2 };
		String[] aRegNames = { "$a0", "$a1", "$a2", "$a3" };

		int[] zeroRegCounts = { 3 };
		String[] zeroRegNames = { "$zero" };
		int[] spRegCounts = { 3 };
		String[] spRegNames = { "$sp" };
		int[] fpRegCounts = { 3 };
		String[] fpRegNames = { "$fp" };
		int[] raRegCounts = { 3 };
		String[] raRegNames = { "$ra" };

		int[] tRegCounts = { 3, 6, 4, 2, 1, 0, 1, 2, 2, 2 };
		String[] tRegNames = { "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$t8", "$t9" };
		int[] vRegCounts = { 3, 6 };
		String[] vRegNames = { "$v0", "$v1" };


		
		WINDOW_HEIGHT = totalMaxCounts*heightMultiple;
		System.out.println(WINDOW_HEIGHT);
		 

		String htmlSRegs = getHTMLCode(sRegCounts, sRegNames, imageURL,maxHeight1);
		String htmlARegs = getHTMLCode(aRegCounts, aRegNames, imageURL,maxHeight1);

		String htmlZeroReg = getHTMLCode(zeroRegCounts, zeroRegNames, imageURL,maxHeight2);
		String htmlSpReg = getHTMLCode(spRegCounts, spRegNames, imageURL,maxHeight2);
		String htmlFpReg = getHTMLCode(fpRegCounts, fpRegNames, imageURL,maxHeight2);
		String htmlRaReg = getHTMLCode(raRegCounts, raRegNames, imageURL,maxHeight2);

		String htmlTRegs = getHTMLCode(tRegCounts, tRegNames, imageURL,maxHeight3);
		String htmlVRegs = getHTMLCode(vRegCounts, vRegNames, imageURL,maxHeight3);

		openFrame(
			preAmble
				+ "<table><tr><td valign=bottom>"
				+ htmlSRegs
				+ "</td>"
				+ tableColumnBreak
				+ "<td valign=bottom>"
				+ htmlARegs
				+ "</td></tr></table>"
				+ "<table><tr><td valign=bottom>"
				+ htmlZeroReg
				+ "</td>"
				+ tableColumnBreak
				+ tableColumnBreak
				+ tableColumnBreak
				+ "<td valign=bottom>"
				+ htmlSpReg
				+ "</td>"
				+ tableColumnBreak
				+ tableColumnBreak
				+ tableColumnBreak
				+ "<td valign=bottom>"
				+ htmlFpReg
				+ "</td>"
				+ tableColumnBreak
				+ tableColumnBreak
				+ tableColumnBreak
				+ "<td valign=bottom>"
				+ htmlRaReg
				+ "</td></tr></table>"
				+ "<table><tr><td valign=bottom>"
				+ htmlTRegs
				+ "</td>"
				+ tableColumnBreak
				+ htmlVRegs
				+ "</td></tr></table>",
			16 * imgWidth);

	}


	/*private static void openFrame(String htmlCode, int windowWidth) {
		JFrame f = new JFrame("Register Usage Historgram");

		//f.setSize(new Dimension(800, 200));
		f.getContentPane().setLayout(new FlowLayout());

		JEditorPane t = new JEditorPane("text/html", "<html>" + htmlCode + "</html>");

		//TODO: Make images accessible locally, not via the web
		//System.out.println(WINDOW_HEIGHT+100);
		t.setPreferredSize(new Dimension(windowWidth, WINDOW_HEIGHT+200));
		t.setEditable(false);
		t.setVisible(true);
		f.getContentPane().add(t);
		f.pack();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setResizable(false);
		f.setVisible(true);

		System.out.print(t.getText());
	}*/
	/**
	 * @return
	 */
	public static JComponent getComponent() {
		try {
			render();
		} catch (Exception e) {
			System.out.println("Array arguments supplied of incorrect length.");
			System.exit(-1);
		}
		
		JEditorPane t = new JEditorPane("text/html", "<html>" + htmlCode + "</html>");

		
		//t.setPreferredSize(new Dimension(windowWidth, WINDOW_HEIGHT+200));
		t.setEditable(false);
		t.setVisible(true);
			

		return t;
	}

}
