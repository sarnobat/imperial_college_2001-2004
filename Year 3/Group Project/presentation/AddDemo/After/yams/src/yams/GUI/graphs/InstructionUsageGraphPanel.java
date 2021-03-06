package yams.GUI.graphs;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import yams.GUI.StatisticsFrame;
import yams.processor.Processor;
import yams.processor.StatisticsManager;

/*
 * Created on 10-Dec-2003
 *
 */
/**
 * @author ss401
 * Panel displaying a horizontal bar chart of line access counts 
 */
public class InstructionUsageGraphPanel extends JPanel {

	/*
	 * Dimension data
	 */
	private int windowHeight;
	private int windowWidth;

	private String content = "";
	private JEditorPane textPane;

	private String[] instructionNames;
	private Integer[] instructionCounts;
	private StatisticsFrame sf;

	public InstructionUsageGraphPanel(StatisticsFrame sf, Processor p) {
		this.setLayout(new GridLayout(1, 1));
		this.windowHeight = sf.WINDOW_HEIGHT;
		this.windowWidth = sf.WINDOW_WIDTH;
		this.setPreferredSize(new Dimension(windowWidth, windowHeight));

		this.instructionNames = p.statisticsManager.getInstr();
		this.instructionCounts = p.statisticsManager.getInstrCount();
		this.sf = sf;

		this.textPane = new JEditorPane("text/html", "");
		try {
			render();
		}
		catch (Exception e) {
		}
		this.textPane.setText(sf.preAmble + content);
		this.textPane.setEditable(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView(textPane);
		this.add(scrollPane);
	}

	/**
	 * The generation of the html body takes place here.
	 * It is accumulated in the String 'content'
	 */
	private void render() throws Exception {
		//sanity check
		if (instructionCounts.length != instructionNames.length) {
			throw new Exception("Arrays for instruction names and counts are not equal.");
		}

		content += "<table><tr><td><b>Instruction</b></td><td><b>Execution Frequency</b></td></tr>";
		int total = 0;
		for (int i = 0; i < instructionNames.length; i++) {
			int count = instructionCounts[i].intValue();
			//many instructions will not be used in the program, so do not print bars for them
			if (count > 0) {
				int width = (int) (((double) count / max()) * windowWidth);
				content += "<tr></tr><tr><td>"
					+ instructionNames[i]
					+ "</td><td><div bgcolor=" +StatisticsFrame.GREEN +" height=1 width="
					+ width
					+ "></div></td><td>&nbsp;"
					+ count
					+ "</td></tr>";
					total += count;
			}
		}
		content += "<tr><td>------------</td><td>"
			+ "---------------------------------------------------------------------------"
			+ "</td><td>------"
			+ "</td></tr><tr><td>TOTAL</td><td></td><td>"
			+ total
			+ "</td></tr></table>";
	}

	/**
	 * @return The maximum count of any instruction execution
	 */
	private int max() {
		int max = 0;
		for (int i = 0; i < instructionCounts.length; i++) {
			int count = instructionCounts[i].intValue();
			if (count > max) {
				max = count;
			}
		}
		return max;
	}
}
