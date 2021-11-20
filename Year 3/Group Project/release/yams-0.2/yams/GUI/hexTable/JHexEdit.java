/*
 * Created on 09-Dec-2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package yams.GUI.hexTable;

import java.awt.*;
import javax.swing.*;


public class JHexEdit extends JPanel {
//	public JHexEdit(File file) throws IOException {
//		this(new HexFile(file));
//	}

	HexTableModel model;
	JTable table;

	public JHexEdit(HexData data) {
		model = new HexTableModel(data);
		table = new HexTable(model);
		setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		setLayout(new GridLayout());
		add(new JScrollPane(table));
		setPreferredSize(
			new Dimension(table.getPreferredSize().width + 8, 400));
	}
	
	public void dataChanged(int row, int col) {
		model.fireModelChangeEvent(row, col);
	}
	
}
