import javax.swing.*;



public abstract class AbstractPiece extends JButton
{
	protected ImageIcon c0;
	protected ImageIcon c1;
	protected ImageIcon c2;
	protected ImageIcon c3;
	protected ImageIcon c4;
	protected ImageIcon c5;
	protected ImageIcon c6;
	protected ImageIcon c7;
	protected ImageIcon c8;
	protected ImageIcon b;
	protected ImageIcon w;
	protected ImageIcon f;

	protected Grid owner;
	protected int row;
	protected int col;

	private int state;
	private static final int BLANK = 0;
	private static final int FLAGGED = 1;
	private static final int WARNED = 2;

	protected boolean visible;

	public AbstractPiece(Grid g,int r, int c)
	{
		super();
		owner = g;
		state = BLANK;
		createIcons();
		visible = false;
		row = r;
		col = c;
	}

	public abstract boolean isBomb();

	public abstract boolean isField();

	public abstract void reveal();

	public Grid getOwner()
	{
		return owner;
	}

	public boolean pieceIs(int id)
	{
		return (id == state);
	}


	private void createIcons()
	{
		Class here = null;
		try
		{
			here = Class.forName("minefield");
		}
		catch(ClassNotFoundException c)
		{
			System.err.println("Cannot find class minefield. Make sure it is in the right folder");
		}
		if(here != null)
		{
			c0 = new ImageIcon(here.getResource("Circle_0.gif"));
   			c1 = new ImageIcon(here.getResource("Circle_1.gif"));
   			c2 = new ImageIcon(here.getResource("Circle_2.gif"));
   			c3 = new ImageIcon(here.getResource("Circle_3.gif"));
   			c4 = new ImageIcon(here.getResource("Circle_4.gif"));
   			c5 = new ImageIcon(here.getResource("Circle_5.gif"));
   			c6 = new ImageIcon(here.getResource("Circle_6.gif"));
   			c7 = new ImageIcon(here.getResource("Circle_7.gif"));
   			c8 = new ImageIcon(here.getResource("Circle_8.gif"));
			b = new ImageIcon(here.getResource("Bomb.gif"));
			w = new ImageIcon(here.getResource("Help.gif"));
			f = new ImageIcon(here.getResource("RedFlag.gif"));
		}
	}

	public void nextRightClick()
	{
		switch (state)
		{
			case BLANK:	{	state = FLAGGED;
						setIcon(f);
						owner.decrAvailableFlags();
						break;		}
			case FLAGGED:	{	state = WARNED;
						setIcon(w);
						owner.incrAvailableFlags();
						break;		}
			case WARNED:	{	state = BLANK;
						setIcon(null);
						break;		}
		}


	}
}
