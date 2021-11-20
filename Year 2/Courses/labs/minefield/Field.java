import javax.swing.*;
import java.awt.Color;

public class Field extends AbstractPiece
{
	private int adjBombs;
	public Field(Grid g, int r, int c)
	{
		super(g,r,c);
	}
	public boolean isField()
	{
		return true;
	}
	public boolean isBomb()
	{
		return false;
	}
	public void setAdjBombs(int b)
	{
		adjBombs = b;
	}
	private void displayCount(int c)
	{
		switch (c)
		{
			case 0:	{setIcon(c0);	break;}
			case 1:	{setIcon(c1);	break;}
			case 2:	{setIcon(c2);	break;}
			case 3:	{setIcon(c3);	break;}
			case 4:	{setIcon(c4);	break;}
			case 5:	{setIcon(c5);	break;}
			case 6:	{setIcon(c6);	break;}
			case 7:	{setIcon(c7);	break;}
			case 8:	{setIcon(c8);	break;}
		}
	}
	public void reveal()
	{
		if (visible == true)
		{
			return;
		}

		visible = true;

		displayCount(adjBombs);

		if (adjBombs == 0)
		{
			for (int i=-1;i<2;i++)
			{
				for (int j=-1;j<2;j++)
				{
					if (owner.isValidRow(row+i) && owner.isValidCol(col+j))
					{
						(owner.getPiece(row+i,col+j)).reveal();
					}

				}
			}
		}

		setBackground(Color.green);
	}
}
