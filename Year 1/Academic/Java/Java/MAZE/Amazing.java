import maze.*;
import java.io.*;

public class Amazing
{
	public static void main (String[] args)
	{
		Maze m = new Maze();

		m.GetMaze(args[0]);

		Position entrance = new Position();


		entrance = m.FindMazeEntrance(); //Runs okay
		//System.out.println("Entrance found");


		ExploreMaze (m,entrance);

	}

	public static void ExploreMaze (Maze m, Position pos)
	{
				if (m.MoveOK(pos))
				{
					m.PlaceMarker(pos);
					m.PutMaze();

					if (m.IsSolved(pos))
					{
						m.PutMaze();
					}

					else
					{
						Position c = new Position();
						c.SetPosition(pos.GetYPos(),pos.GetXPos());

						c.ChangePosition('n');
						//System.out.println("Trying to move north");
						//System.out.println(c.GetYPos());
						ExploreMaze (m,c);
						c.ChangePosition('s');


						c.ChangePosition('e');
						//System.out.println("Trying to move east");
						ExploreMaze (m,c);
						//c = pos;						c.ChangePosition('w');

						c.ChangePosition('s');
						//System.out.println("Trying to move south");
						ExploreMaze (m,c);
						//c = pos;
						c.ChangePosition('n');						
						c.ChangePosition('w');
						//System.out.println("Trying to move west");
						ExploreMaze (m,c);
						//c = pos;						c.ChangePosition('e');


					}
				}

				else
				{
//					m.RemoveMarker(pos);
					//System.out.println("can't make move");
//					m.PutMaze();
				}


				m.RemoveMarker(pos);


	}
}
