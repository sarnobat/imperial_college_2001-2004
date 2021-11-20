package maze;
import java.io.*;

public class Maze implements MazeTemplate
{

	private char M [] [] = new char [100] [100];
	private int rows;
	private int cols;

	public void GetMaze (String file)
	{
		try
		{
			FileReader fr = new FileReader (file);
			BufferedReader b = new BufferedReader (fr);

			String line = b.readLine();
			int row = 0;
			int col;

			while (line != null)
			{
				cols = line.length();

				for (col = 0; col < line.length(); col++)
				{
					M [row] [col] = line.charAt(col);
				}
					
				line = b.readLine();
				row = row + 1;
				rows = row;
			}

		}
		catch (FileNotFoundException e)
		{
			System.out.println("Please specify the correct maze file and path");
		}
		catch (IOException e)
		{
			System.out.println("Input/Output error");
		}

	}


	public void PutMaze()
	{
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				System.out.print(M[i][j]);
			}
			System.out.println("");
		}
	}

	public Position FindMazeEntrance ()
	{
		Position p = new Position();

		int i;
		int y = 0;
		
		for (i = 0; i < rows; i++)
		{
			if (M [i] [0] == ' ')
			{
				y = i;
				p.SetPosition(y,0);
			}							
		}

		return p;
	}

	public boolean MoveOK(Position pos)
	{

		boolean possible = false;

		if ((pos.GetYPos() < rows-1 & pos.GetYPos() >= 0) &  (pos.GetXPos() < cols-1 & pos.GetXPos() >= 0) & M [pos.GetYPos()] [pos.GetXPos()] == ' ')
		{
			possible = true;
		}
		
		return  possible;

	}

	public boolean IsSolved(Position pos)
	{
		boolean exit = false;
		if (pos.GetXPos() == cols-1 & M [pos.GetYPos()] [pos.GetXPos()] == ' ' )
		{
			exit = true;
		}

		return exit;
	}

	public void PlaceMarker (Position pos)
	{
		if (MoveOK(pos))
		{
			M [pos.GetYPos()] [pos.GetXPos()] = '*';
		}
	}

	public void RemoveMarker ( Position pos)
	{
		if (M [pos.GetYPos()] [pos.GetXPos()] == '*')
		{
			M [pos.GetYPos()] [pos.GetXPos()] = ' ';
		}
	}

	public int ShowStep ()
	{
		return 1;
	}
}
