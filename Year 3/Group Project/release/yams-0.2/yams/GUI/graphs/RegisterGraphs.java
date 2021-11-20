package yams.GUI.graphs;
import java.awt.Dimension;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.text.html.HTMLEditorKit;

import yams.GUI.StatisticsFrame;
import yams.processor.Processor;
/*
 * Created on 10-Dec-2003
 *
 */
/**
 * @author ss401
 * Panel containing vertical bar charts of register
 * access frequency 
 */
public class RegisterGraphs extends JPanel {

	/*
	 * Size configuration data. Most of this should be determined
	 * from the window height
	 */
	private int imgWidth;
	private int windowHeight;
	private long totalMaxCounts;
	private int fontSize;
	private int cellHeight;

	/*
	 * Rendering data
	 */
	private String tableColumnBreak = "<td width=" + imgWidth + ">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>";
	private String content;
	private JEditorPane textPane;

	private Processor p;
	private int totalRegisters;
	private int[] regOrder;

	public RegisterGraphs(StatisticsFrame sf, Processor p) {
		try {
			//set fields from parameters so whole class can access them
			this.windowHeight = sf.WINDOW_HEIGHT;
			this.imgWidth = (sf.WINDOW_WIDTH + sf.WIDTH_BUFFER) / 13;
			this.cellHeight = sf.WINDOW_HEIGHT / 3;
			this.p = p;
			totalRegisters = p.registerManager.getTotalNumberOfRegisters();
			//create display
			textPane = new JEditorPane("text/html", "");
			textPane.setEditorKit(new HTMLEditorKit());
			regOrder = initializeRegOrder();
			try {
				render();
			}
			catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
			textPane.setText(sf.preAmble + content);
			this.add(textPane);
			this.setSize(new Dimension(sf.WINDOW_WIDTH, windowHeight));
		}
		catch (Exception e1) {
			System.out.println("Inconsistent data");
			e1.printStackTrace();
		}
	}

	/**
	 * Returns the maximum register access counts for registers in 
	 * each of the 3 rows to be displayed
	 * @return
	 * @throws Exception
	 */
	private long[] getMaxCounts() throws Exception {
		long[] maxCounts = new long[3];
		int currentRow = 0;
		int currentRowMax = 0;
		int i;
		//sanity check
		if (filterRegIds(regOrder).length != totalRegisters) {
			System.out.println("Too many registers in display configuration. There should only be 32 registers in the display.");
			throw new Exception();
		}
		for (i = 0; i < regOrder.length; i++) {
			if (regOrder[i] == -1) {
				continue;
			}
			else if (regOrder[i] == -2) {
				maxCounts[currentRow] = currentRowMax;
				currentRow++;
				currentRowMax = 0;
			}
			else {
				int registerAccessCount = p.statisticsManager.getRegCount(regOrder[i]);
				if (registerAccessCount > currentRowMax) {
					currentRowMax = registerAccessCount;
				}
			}
		}
		maxCounts[currentRow] = currentRowMax;
		return maxCounts;
	}
	/**
	 * removes non-data values (i.e. negative values) from register id array
	 * @param regIds
	 * @return
	 */
	private int[] filterRegIds(int[] regIds) {
		int[] filtered = new int[totalRegisters];
		int j = 0;
		for (int i = 0; i < regIds.length; i++) {
			if (regIds[i] > -1) {
				filtered[j] = regIds[i];
				j++;
			}
		}
		return filtered;
	}

	/**
	 * Returns a string URL of the image which
	 * should be used for a particular register
	 * @param i
	 * @return
	 */
	private String getColor(int i) {
		String filename = null;
		//$t
		if ((7 < i && i < 16) || i == 24 || i == 25) {
			filename = "blue";
		}
		//$s
		else if ((15 < i && i < 24)) {
			filename = "orange";
		}
		//$a
		else if (3 < i && i < 8) {
			filename = "red";
		}
		//$v
		else if (i == 2 || i == 3) {
			filename = "green";
		}
		//$k
		else if (i == 26 || i == 27) {
			filename = "yellow";
		}
		//other registers
		else {
			filename = "purple";
		}
		return filename;
	}

	/**
	 * Returns an array of ints, each representing a register.
	 * The array specifies what order to display the registers in the
	 * graph.
	 * 	'-1' means leave a blank space between two bars,
	 * 	'-2' means insert the remaining registers on a new line 
	 * @return
	 * @throws Exception
	 */
	private int[] initializeRegOrder() throws Exception {
		int[] regOrder =
			{
				8,
				9,
				10,
				11,
				12,
				13,
				14,
				15,
				-1,
				2,
				3,
				-1,
				0,
				-2,
				16,
				17,
				18,
				19,
				20,
				21,
				22,
				23,
				-1,
				4,
				5,
				6,
				7,
				-2,
				26,
				27,
				-1,
				1,
				-1,
				28,
				-1,
				29,
				-1,
				30,
				-1,
				31 };
		return regOrder;
	}

	/**
	 * Generates all html code necessary to display graphs. 
	 * The code is accumulated in the String 'content'	   
	 * @throws Exception
	 */
	private void render() throws Exception {
		long[] maxCounts = getMaxCounts();
		totalMaxCounts = maxCounts[0] + maxCounts[1] + maxCounts[2];
		long[] counts = new long[totalRegisters];
		int[] heights = new int[totalRegisters];
		content = "<table><tr>";
		int j = 0;
		int rowNo = 0;
		int rowHeight;
		if (totalMaxCounts > 0) {
			rowHeight = (int) ((double) (maxCounts[rowNo] / totalMaxCounts) * windowHeight);
		}
		else {
			rowHeight = windowHeight / 3;
		}

		//Go through each register, fetch it's access count and display
		//it with a vertical image bar
		for (int i = 0; i < regOrder.length; i++) {
			if (regOrder[i] == -1) {
				//insert a blank cell
				content += tableColumnBreak;
				continue;
			}
			else if (regOrder[i] == -2) {
				//Start a new row
				content += "</tr><tr>";
				rowNo++;
				double proportion = ((double) maxCounts[rowNo]) / ((double) totalMaxCounts);
				if (totalMaxCounts > 0) {
					rowHeight = (int) (proportion * windowHeight);
				}
				continue;
			}
			else {
				//create a vertical block with size proportional to the register access count
				int currentRegId = regOrder[i];
				long count = p.statisticsManager.getRegCount(currentRegId);
				counts[j] = count;
				int height;
				if (totalMaxCounts > 0) {
					height = (int) (windowHeight * count / totalMaxCounts);
				}
				else {
					height = windowHeight / 3;
				}
				heights[j] = height;
				content += "<td height=" + rowHeight + " valign=bottom><table><tr><td align=center valign=bottom>";
				String countStr = "(" + String.valueOf(count) + ")";
				if (count > 0) {
					countStr = "<b>(" + count + ")</b>";
				}
				if (height > 0 && count > 0) {
					//display a small bar for images that are of height less that 1
					
					String imageTag =
						"<table hieght=100% width=95%><tr><td>&nbsp;</td></tr><tr><td bgcolor="
							+ getColor(regOrder[i])
							+ " height="
							+ height
							+ ">&nbsp;</td></tr></table>";
					content += imageTag;
				}
				else if (count > 0 && height == 0) {
					// Don't print anything
				}
				j++;
				content += "</td></tr><tr><td align=center valign=top>---------<br>"
					+ p.registerManager.getRegName(regOrder[i])
					+ "<br>"
					+ countStr
					+ "<br></td></tr></table></td>";
			}
		}
		content += "</tr></table>";
	}
}
