/*
 * Created on 09-Dec-2003
 *
 * To change the template for this generated file go to
 * indow&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package yams.GUI.hexTable;

public interface HexData
{
   public int getRowCount();
   public int getColumnCount();
   public int getLastRowSize();
   public byte getByte(int row, int col);
   public void setByte(int row, int col, byte value);
   public byte[] getRow(int row);
}
