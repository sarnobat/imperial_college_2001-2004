/*
 * Created on 21-Nov-2003
 *
 */
package yams.GUI;

import javax.swing.*;

import yams.*;
import yams.GUI.hexTable.*;
import yams.processor.*;

/**
 * Panel showing the data segment in main memory for a program
 */
public class DataPanel extends YamsPanel {
	
	// window elements
	JHexEdit dataTable;
	
	public DataPanel(YAMSGui c) {
		super(c);
		setTitle("Data Segment");
		
		// initialization must happen after processor has been created
	}
	
	/**
	 * Creates a new Hex data table representing the
	 * data segment of a program
	 *
	 */
	public void init() {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		dataTable = new JHexEdit(new dataInterface(0x10000000));
		
		this.add(dataTable);
	}
	
	/**
	 * Updates the cells in the data table corresponding to the address given
	 * @param address to update
	 */
	public void memoryChanged(int address) {
		// convert address to a row and column
		// offset: 0x100000
		// columns: 16
		if (address >= 0x10000000) {
			int col = (address - 0x10000000) % 16;
			int row = (address - 0x10000000) / 16;
			dataTable.dataChanged(row, col+1);
			dataTable.dataChanged(row, col+2);
			dataTable.dataChanged(row, col+3);
			dataTable.dataChanged(row, col+4);
		}
	}
	
	/**
	 * Clears all values in the Data segment table
	 */
	public void reset() {
		// reset panel
		try {
			this.remove(dataTable);
		}
		catch (Exception e) {}
		dataTable = new JHexEdit(new dataInterface(0x10000000));
		this.add(dataTable);
	}
	
	
	/**
	 * The HexData class where the table gets it's data from
	 */
	class dataInterface implements HexData {
		
		int startOffset;
		Processor processor;
		
		dataInterface(int s) {
			startOffset = s;
			processor = controller.getProcessor();
		}
		
		public int getRowCount() {
			// fixed row count
			return 100;
		}
		
		public int getColumnCount() {
			// fixed column count
			return 16;
		}
		
		public int getLastRowSize() {
			// for some reason this needs to be 17 rather than 16
			return 17;
		}
		
		public byte getByte(int row, int col) {
			// get the byte from the memory manager and return it
			return (byte)processor.memoryManager.getByte(startOffset + (row*getColumnCount() + col));
		}
		
		public void setByte(int row, int col, byte value) {
			// no editing of memory values in this version
		}
		
		public byte[] getRow(int row) {
			byte[] ret = new byte[16];
			
			for(int i=0; i<16; i++) {
				ret[i] = getByte(row, i);
			}
			
			return ret;
		}
		
	}

}
