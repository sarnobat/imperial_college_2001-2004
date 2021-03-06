package maze;

import java.io.*;
import maze.*;

public interface MazeTemplate
{

stat  final char MazeMarker = '*';
static final char MazePath = ' ';

public void GetMaze ( String  file);

public void PutMaze();

public Position FindMazeEntrance ();

public boolean MoveOK(Position pos);

public boolean IsSolved(Position pos);

public void PlaceMarker (Position pos);

public void RemoveMarker ( Position pos);

public int ShowStep ();

}
