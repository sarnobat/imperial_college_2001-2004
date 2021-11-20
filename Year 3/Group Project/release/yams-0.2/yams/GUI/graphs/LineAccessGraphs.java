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
public class LineAccessGraphs extends JPanel {

	/*
	 * Dimension data
	 */
	private int windowHeight;
	private int windowWidth;
	
	private String content = "";

	private JEditorPane textPane;
	private int totalRegisters;
	private Processor p;
	private LineList l;

	public LineAccessGraphs(StatisticsFrame sf, Processor p, LineList l) {
		this.setLayout(new GridLayout(1, 1));
		this.windowHeight = sf.WINDOW_HEIGHT;
		this.windowWidth = sf.WINDOW_WIDTH;
		this.setPreferredSize(new Dimension(windowWidth, windowHeight));
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
		System.out.println(textPane.getText());
	}

	/**
	 * The generation of the html body takes place here.
	 * It is accumulated in the String 'content'
	 */
	private void render() {
		//content += "<font size=7><table><tr><td><b>Line Number</b></td><td><b>Instruction</b></td><td><b>Execution Count</b></td></tr>";
		int maxCount = max();
		content += "<table><tr><td><b>Line Number</b></td><td><b>Instruction</b></td><td><b>Exectution Count</b></td></tr>";
		for (int i = 1; i <= l.totalLines(); i++) {
			int count = p.statisticsManager.getLineCount(i);
			int width = 0;
			if (maxCount != 0) {
				width = windowWidth * count / maxCount;
			}
			Instruction instr = l.getLine(i).getInstruction();
			if (instr == null || instr instanceof Directive) {
				continue;
			}
			/*content += "<tr><td>" + (i + 1) + "</td><td>" + l.getLine(i).getOriginal() + "</td><td align=left>";
			if (maxCount > 0 && (windowWidth * count / maxCount) > 0) {
				//content += "<img src=" + imageURL + " width=" + (windowWidth * count / maxCount) + " height=" + barHeight + ">&nbsp;" + count;
				content += "<table><tr><td><table height=1%><tr><td></td></tr></table><table width=100%><tr><td width="+width+" bgcolor=blue></td><td>"+count+"</td></tr></table><table height=1%><tr><td></td></tr></table></td></tr></table>";
			}
			content += "</td></tr>";*/
			//content += "<tr><td padding=8><table width=100%><tr><td width=10>" +(i+1)+"</td><td width=100>"+l.getLine(i).getOriginal()+"</td><td bgcolor=blue width="+width+"></td><td>"+count+"</td></tr></table></td></tr>";
			//content += "<table width=100%><tr><td width=10>" +(i+1)+"</td><td width=60>"+l.getLine(i).getOriginal()+"</td><td></td><td>"+count+"</td></tr></table>";
			content += "<tr>"
				+ "<td width=10 valign=center>"
				+ (i + 1)
				+ "</td>"
				+ "<td width=100 valign=center>"
				+ l.getLine(i).getOriginal()
				+ "</td>"
				+ "<td valign=center>"
				+ "<div bgcolor=blue height=1 width="+ width+ "></div>"
				+ "</td>"
				+ "<td valign=center>&nbsp;"
				+ count
				+ "</td>"
				+ "</tr><tr><div></div></tr>";
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
