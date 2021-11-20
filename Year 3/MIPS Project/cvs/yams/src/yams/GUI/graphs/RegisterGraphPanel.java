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
public class RegisterGraphPanel extends JPanel {

	/*
	 * Size configuration data. Most of this should be determined
	 * from the window dimensions
	 */
	// total number of pixels allocated to bars in window
	private int totalBarHeight;
	private int totalBarWidth;
	
	private int singleBarWidth;
	private int CELLS_PER_ROW = 13;
	private int ROWS = 3; 
	
	private long totalMaxCounts;
	private int fontSize;
	private int cellHeight;

	/*
	 * Rendering data
	 */
	private String tableColumnBreak = "<td width=" + singleBarWidth + ">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>";
	private String content;
	private JEditorPane textPane;

	private Processor p;
	private StatisticsFrame sf;
	private int totalRegisters;
	private int[] regOrder;
	/*
	 * Colors
	 */

	public RegisterGraphPanel(StatisticsFrame sf, Processor p) {
		try {
			//set fields from parameters so whole class can access them
			this.totalBarHeight = sf.WINDOW_HEIGHT-50;
			this.totalBarWidth = sf.WINDOW_WIDTH-50;
			this.singleBarWidth = this.totalBarWidth/this.CELLS_PER_ROW;
			this.p = p;
			this.sf = sf;
			
			this.setSize(new Dimension(sf.WINDOW_WIDTH, sf.WINDOW_HEIGHT));
			this.totalRegisters = p.registerManager.getTotalNumberOfRegisters();
			//create display
			this.textPane = new JEditorPane("text/html", "");
			this.textPane.setEditable(false);
			this.textPane.setEditorKit(new HTMLEditorKit());
			this.regOrder = initializeRegOrder();
			try {
				render();
			}
			catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
			this.textPane.setText(sf.preAmble + content);
			this.add(textPane);
		}
		catch (Exception e1) {
			System.out.println("Inconsistent data");
			e1.printStackTrace();
		}
	}

	/**
	 * Generates the html code for the vertical bar
	 * graphs to reflect the statistics
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
			rowHeight = (int) ((double) (maxCounts[rowNo] / totalMaxCounts) * totalBarHeight);
		}
		else {
			rowHeight = totalBarHeight / ROWS;
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
					rowHeight = (int) (proportion * totalBarHeight);
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
					height = (int) (totalBarHeight * count / totalMaxCounts);
				}
				else {
					height = totalBarHeight / ROWS;
				}
				heights[j] = height;
				content += "<td valign=bottom><table><tr><td align=center valign=bottom>";
				String countStr = "(" + String.valueOf(count) + ")";
				if (count > 0) {
					countStr = "<b>(" + count + ")</b>";
				}
				if (height > 0 && count > 0) {
					//display a small bar for images that are of height less that 1

					String imageTag =
						"<table height=100% width=95%><tr><td>&nbsp;</td></tr><tr>"
							+ "<td>"
							+ "<div style=\"font-size:1px;\""
							+ " height="
							+ height
							+ " bgcolor="
							+ getColor(currentRegId)
							+ ">&nbsp;"
							+ "</div></td></tr></table>";
					content += imageTag;
				}
				else if (count > 0 && height == 0) {
					// Don't print anything
				}
				j++;
				content += "</td></tr><tr><td align=center valign=top>---------<br>"
					+ p.registerManager.getRegName(currentRegId)
					+ "<br>"
					+ countStr
					+ "<br></td></tr></table></td>";
			}
		}
		content += "</tr></table>";
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
			filename = StatisticsFrame.BLUE;
		}
		//$s
		else if ((15 < i && i < 24)) {
			filename = StatisticsFrame.PURPLE;
		}
		//$a
		else if (3 < i && i < 8) {
			filename = StatisticsFrame.ORANGE;
		}
		//$v
		else if (i == 2 || i == 3) {
			filename = StatisticsFrame.GREEN;
		}
		//$k
		else if (i == 26 || i == 27) {
			filename = StatisticsFrame.YELLOW;
		}
		//other registers
		else {
			filename = StatisticsFrame.RED;
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

}
