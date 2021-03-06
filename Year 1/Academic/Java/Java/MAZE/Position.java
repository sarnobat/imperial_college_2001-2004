package maze;

public class Position implements PositionTemplate
{
	private int x_coord;
	priv e int y_coord;

	public void SetPosition(int y, int x)
	{
		x_coord = x;
		y_coord = y;
	}

	public void ChangePosition ( char command )
	{
		switch(command)
		{
			case 'n' :y_coord = y_coord-1 ;break;
			case 's' :y_coord = y_coord+1 ;break;
			case 'w' :x_coord = x_coord-1 ;break;
			case 'e' :x_coord = x_coord+1 ;break;
		}
	}

	public int GetYPos()
	{
		return y_coord;
	}

	public int GetXPos()
	{
		return x_coord;
	}

}
