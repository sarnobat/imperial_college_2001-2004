import java.awt.event.*;
import javax.swing.*;

public class PieceController extends MouseAdapter
{
	public void mouseClicked(MouseEvent e)
	{//overrides method from superclass

		AbstractPiece p = (AbstractPiece)e.getSource();

		if (p.getOwner().gameIsActive())
		{
			if (SwingUtilities.isLeftMouseButton(e))
			{
				if (p.isBomb())
				{
					if (!p.pieceIs(1))
					{
						p.owner.loseGame();
					}

				}
				else if (p.isField() && p.pieceIs(0))
				{
					p.reveal();
				}
			}
			else if (SwingUtilities.isRightMouseButton(e))
			{
				p.nextRightClick();

			}
		}
	}
}
