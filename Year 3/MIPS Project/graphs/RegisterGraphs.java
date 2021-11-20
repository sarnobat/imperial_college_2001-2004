import javax.swing.JEditorPane;

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
public class RegisterGraphs extends JEditorPane {

	/*
	 * Pseudo Statistical Data
	 */
	String[] sRegNames = { "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7" };
	String[] aRegNames = { "$a0", "$a1", "$a2", "$a3" };
	String[] zeroRegNames = { "$zero" };
	String[] spRegNames = { "$sp" };
	String[] fpRegNames = { "$fp" };
	String[] raRegNames = { "$ra" };
	String[] tRegNames = { "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$t8", "$t9" };
	String[] vRegNames = { "$v0", "$v1" };

	int[] sRegCounts = { 3, 6, 4, 2, 1, 0, 1, 2 };
	int[] aRegCounts = { 3, 6, 4, 2 };
	int[] zeroRegCounts = { 3 };
	int[] spRegCounts = { 3 };
	int[] fpRegCounts = { 3 };
	int[] raRegCounts = { 3 };
	int[] tRegCounts = { 3, 6, 4, 2, 1, 0, 1, 2, 2, 2 };
	int[] vRegCounts = { 3, 6 };
	/*
	 * Numerical setup Data
	 */
	private int imgWidth;// = 30;
	private  int windowHeight;
	private int totalMaxCounts;
	private int fontSize;
	/*
	 * Rendering data
	 */
	private String imageURL = "http://web.mit.edu/civenv/K12Edu/images/bluedot.jpg";
	private String tableColumnBreak = "<td width=" + imgWidth + ">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>";
	private String content;
	

	public RegisterGraphs() {
		super("text/html", "");
		this.windowHeight = StatisticsVisualizer.WINDOW_HEIGHT;
		this.imgWidth = (StatisticsVisualizer.WINDOW_WIDTH+ StatisticsVisualizer.WIDTH_BUFFER)/13;
		//this.fontSize = StatisticsVisualizer.fontSize;
		try {
			render();
		} catch (Exception e) {
			System.out.println("Array arguments supplied of incorrect length.");
			System.exit(-1);
		}
		this.setText(StatisticsVisualizer.preAmble + content);
	}

	private void render() throws Exception {
		/*
		 * Find the maximum heights of the bars
		 */
		int[][] row1Counts = { sRegCounts, aRegCounts };
		int[][] row2Counts = { zeroRegCounts, spRegCounts, fpRegCounts, raRegCounts };
		int[][] row3Counts = { tRegCounts, vRegCounts };
		int maxHeight1 = max(row1Counts);
		int maxHeight2 = max(row2Counts);
		int maxHeight3 = max(row3Counts);
		totalMaxCounts = maxHeight1 +maxHeight2+maxHeight3;

		String htmlSRegs = getHTMLCode(sRegCounts, sRegNames, imageURL, maxHeight1);
		String htmlARegs = getHTMLCode(aRegCounts, aRegNames, imageURL, maxHeight1);

		String htmlZeroReg = getHTMLCode(zeroRegCounts, zeroRegNames, imageURL, maxHeight2);
		String htmlSpReg = getHTMLCode(spRegCounts, spRegNames, imageURL, maxHeight2);
		String htmlFpReg = getHTMLCode(fpRegCounts, fpRegNames, imageURL, maxHeight2);
		String htmlRaReg = getHTMLCode(raRegCounts, raRegNames, imageURL, maxHeight2);

		String htmlTRegs = getHTMLCode(tRegCounts, tRegNames, imageURL, maxHeight3);
		String htmlVRegs = getHTMLCode(vRegCounts, vRegNames, imageURL, maxHeight3);

		content =
			
				"<table><tr><td valign=bottom>"
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
				+ "</td></tr></table>";
		
	}
	public String getHTMLCode(int[] regAccesses, String[] regNames, String imageURL,int maxVal) throws Exception {
		String content = "";
		String content2 = "";
		String content3 = "";

		if (regAccesses.length != regNames.length) {
			throw new Exception("Array lengths are not equal");
		}

		if (regAccesses == null) {
			content = "<b>Nothing to Display</b>";
		} else {
			content += "<table>" + "<tr>";
			for (int i = 0; i < regAccesses.length; i++) {

				content += "<td valign=bottom>"
					+ "<img src="
					+ imageURL
					+ " height="
					+ (windowHeight* regAccesses[i]/totalMaxCounts)
					+ " width="
					+ imgWidth
					+ "></td>";
				content2 += "<td valign=top>"+ regNames[i] + "</td>";
				content3 += "<td valign=top>"+ "(" + regAccesses[i] + ")</td>";
			}
			content += "</tr>" + "<tr>" + content2 + "</tr>" + "<tr>" + content3 + "</tr></table>";
		}

		return content;
	}
	private static int max(int[][] arrays) {
		int max = 0;
		for (int i = 0; i < arrays.length; i++) {
			for (int j = 0; j < arrays[i].length; j++) {
				if (arrays[i][j] > max) {
					max = arrays[i][j];
				}
			}
		}
		return max;
	}
}
