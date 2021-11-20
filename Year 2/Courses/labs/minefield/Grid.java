import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Grid extends JPanel
{
	private static final int SIZE = 6;
	private static final int B = 9;
	private AbstractPiece[] [] g = new AbstractPiece [SIZE] [SIZE];
	private int bombCount = 0;
	private int flagsAvailable;
	private JLabel flags;
	private boolean active;

	public Grid()
	{// Initializes grid with default bomb placement
		super();
		setLayout(new GridLayout(SIZE,SIZE));
		defaultGrid();

		addFieldsAndListeners();
		flagsAvailable = bombCount;
		active = true;

	}
	public Grid(int i)
	{//i must be 1 to generate a random grid
		super();
		if (i == 1)
		{
			setLayout(new GridLayout(SIZE,SIZE));
			randomizeGrid();
			addFieldsAndListeners();
			flagsAvailable = bombCount;
			active = true;
		}
		else
		{
			System.out.println("WARNING: GRID NOT INITIALIZED");
		}
	}

	private void defaultGrid()
	{
		g[0] [0] = new Bomb(this,0,0);
		g[0] [1] = new Bomb(this,0,1);
		g[0] [2] = new Bomb(this,0,2);
		g[0] [5] = new Bomb(this,0,5);

		g[1] [0] = new Bomb(this,1,0);
		g[1] [2] = new Bomb(this,1,2);

		g[2] [0] = new Bomb(this,2,0);
		g[2] [1] = new Bomb(this,2,1);
		g[2] [2] = new Bomb(this,2,2);

		g[4] [5] = new Bomb(this,4,5);

		g[5] [2] = new Bomb(this,5,2);
		g[5] [5] = new Bomb(this,5,5);

		bombCount = 12;
	}

	public void addFieldsAndListeners()
	{
		PieceController m = new PieceController();

		for (int i=0;i<SIZE;i++)
		{
			for (int j=0;j<SIZE;j++)
			{
				if (g[i] [j] == null)
				{
					g[i] [j] = new Field(this,i,j);
				}

				add(g[i] [j]);
				(g[i] [j]).addMouseListener(m);
			}
		}
		labelFields();
	}

	public void randomizeGrid()
	{
		bombCount = 0;

		for (int i=0;i<SIZE;i++)
		{
			for (int j=0;j<SIZE;j++)
			{
				double r = Math.random();
				if (r >= 0.66)
				{
					g[i] [j] = new Bomb(this,i,j);
					bombCount++;
				}
			}
		}
	}

	public void labelFields()
	{// puts appropriate numbers in Field squares
		for (int i=0;i<SIZE;i++)
		{
			for (int j=0;j<SIZE;j++)
			{
				if ((g[i] [j]).isField())
				{
					int bombCount = getAdjBombs(i,j);
					Field f = (Field) g[i] [j];
					f.setAdjBombs(bombCount);


				}
			}
		}
	}

	public void decrAvailableFlags()
	{
		flagsAvailable--;
		flags.setText(Integer.toString(flagsAvailable));

		if (flagsAvailable==0)
		{
			checkForWin();
			checkForLoss();
		}

	}

	private void checkForLoss()
	{
		for (int i=0;i<SIZE;i++)
		{
			for (int j=0;j<SIZE;j++)
			{
				if ((g[i] [j]).pieceIs(1) && !(g[i] [j]).isBomb())
				{

					loseGame();
				}
			}
		}
	}

	public void checkForWin()
	{
		boolean allBombsFlaged = false;

		for (int i=0;i<SIZE;i++)
		{//Loop checks that all bombs are flagged
			for (int j=0;j<SIZE;j++)
			{
				AbstractPiece currPiece = g[i] [j];
				if (	currPiece.isBomb() && !(currPiece.pieceIs(1)) 	)
				{//If any bombs are NOT flagged, the player hasn't yet won
					return;
				}
			}
		}
		//If the loop is successfully completed, the game has been won
		revealAll();
		flags.setText("You win");
		active = false;
	}

	public void incrAvailableFlags()
	{
		flagsAvailable++;
		flags.setText(Integer.toString(flagsAvailable));

	}
	public void loseGame()
	{
		revealAll();
		flags.setText("You lose");
		active = false;

	}

	public boolean gameIsActive()
	{
		return active;
	}





	public int getBombCount()
	{
		return bombCount;
	}

	public void setLabel(JLabel l)
	{
		flags = l;
	}



	public AbstractPiece getPiece(int row,int col)
	{
		return (g [row] [col]);
	}


	private int getAdjBombs(int row,int col)
	{
		int adjBombs=0;

		if (isValidRow(row-1))
		{
			if (isValidCol(col-1))
			{// here, check square to NW of current one
				adjBombs += checkForBomb(row-1,col-1);
			}

			// here check the square immediately above the current one
			adjBombs += checkForBomb(row-1,col);

			if (isValidCol(col+1))
			{// here, check square to NE of current one
				adjBombs += checkForBomb(row-1,col+1);
			}
		}
		if (isValidCol(col-1))
		{// here, check square to W of current one
			adjBombs += checkForBomb(row,col-1);
		}
		if (isValidCol(col+1))
		{// here, check square to E of current one
			adjBombs += checkForBomb(row,col+1);
		}
		if (isValidRow(row+1))
		{
			if (isValidCol(col-1))
			{// here, check square to SW of current one
				adjBombs += checkForBomb(row+1,col-1);
			}

			// here check the square immediately below the current one
			adjBombs += checkForBomb(row+1,col);

			if (isValidCol(col+1))
			{// here, check square to SE of current one
				adjBombs += checkForBomb(row+1,col+1);
			}
		}

		return adjBombs;
	}

	public boolean isValidRow(int r)
	{
		return ( (r >-1 && r<SIZE));
	}

	public boolean isValidCol(int c)
	{
		return ( (c >-1 && c<SIZE));
	}

	public void revealAdjFields(int row,int col)
	{
		int adjBombs=0;

		if (isValidRow(row-1))
		{
			if (isValidCol(col-1))
			{
    				(g[row-1] [col-1]).reveal();
			}
			(g[row-1] [col]).reveal();
			if (isValidCol(col+1));
			{
				(g[row-1] [col+1]).reveal();
			}
		}
		if (isValidCol(col-1))
		{
			(g[row] [col-1]).reveal();
		}
		if (isValidCol(col+1));
		{
			(g[row] [col+1]).reveal();
		}
		if (isValidRow(row+1))
		{
			if (isValidCol(col-1))
			{
				(g[row+1] [col-1]).reveal();
			}

			(g[row+1] [col]).reveal();

			if (isValidCol(col+1))
			{
				(g[row+1] [col+1]).reveal();
			}
		}
	}

	public void revealAll()
	{
		for (int i=0;i<SIZE;i++)
		{
			for (int j=0;j<SIZE;j++)
			{
				(g[i] [j]).reveal();
			}
		}
	}

	private int checkForBomb(int i,int j)
	{
		if ((g[i] [j]).isBomb())
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}

}
