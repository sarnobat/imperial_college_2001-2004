package yams.GUI.graphs;
import java.awt.*;
import javax.swing.*;
import yams.GUI.StatisticsFrame;
import yams.parser.Directive;
import yams.parser.Instruction;
import yams.parser.LineList;
import yams.processor.Processor;
/*
 * Created on 10-Dec-2003
 *
 */
/**
 * @author ss401
 * Panel displaying a horizontal bar chart of line access counts 
 */
public class LineAccessGraphPanel extends JPanel {

	
	/*
	 * Dimension data
	 */
	// set the maximum pixel widths for bars 
	private int maxBarWidth;
	
	private String content = "";
	
	private JEditorPane textPane;
	private int totalRegisters;
	private Processor p;
	private LineList l;
	
	public LineAccessGraphPanel(StatisticsFrame sf, Processor p, LineList l) {
		this.setLayout(new GridLayout(1, 1));
		this.maxBarWidth = sf.WINDOW_WIDTH-50;
		this.setPreferredSize(new Dimension(maxBarWidth, sf.WINDOW_WIDTH));
		this.p = p;
		this.l = l;
		totalRegisters = p.registerManager.getTotalNumberOfRegisters();

		this.textPane = new JEditorPane("text/html", "");
		render();
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
	private void render() {
		int maxCount = max();
		content += "<table><tr><td><b>Line Number</b></td><td><b>Instruction</b></td><td><b>Exectution Count</b></td></tr>";
		for (int i = 1; i <= l.totalLines(); i++) {
			int count = p.statisticsManager.getLineCount(i);
			int width = 0;
			if (maxCount != 0) {
				width = (maxBarWidth-5) * count / maxCount;
			}
			Instruction instr = l.getLine(i).getInstruction();
			if (instr == null || instr instanceof Directive) {
				continue;
			}
			content += "<tr>"
				+ "<td width=10 valign=center>"
				+ (i + 1)
				+ "</td>"
				+ "<td width=100 valign=center>"
				+ l.getLine(i).getOriginal()
				+ "</td>"
				+ "<td valign=center style=\"font-size=1px\"><div height=6 style=\"font-size:1px\">&nbsp;</div>"
				+ "<div bgcolor="+StatisticsFrame.BLUE+" height=8 width="+ width+ " style=\"font-size:1px\">&nbsp;</div>"
				+ "</td>"
				+ "<td valign=center>&nbsp;&nbsp;"
				+ count
				+ "</td>"
				+ "</tr>";
				
		}
		content += "</table>";
		
	}

	/**
	 * Returns the number of times the most freqeuently 
	 * executed line has been executed
	 * @return
	 */
	private int max() {
		int max = 0;
		for (int i = 1; i <= l.totalLines(); i++) {
			if (p.statisticsManager.getLineCount(i) > max) {
				max = p.statisticsManager.getLineCount(i);
			}
		}
		return max;
	}
}
