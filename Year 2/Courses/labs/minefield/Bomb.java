import javax.swing.*;
import java.awt.Color;

public class Bomb extends AbstractPiece
{
	String label;

	publi
 Bomb(Grid g, int r, int c)
	{
		super(g,r,c);
	}
	public boolean isField()
	{
		return false;
	}
	public boolean isBomb()
	{
		return true;
	}

	public void reveal()
	{
		visible = true;
		setIcon(b);
		setBackground(Color.cyan);
	}
}
