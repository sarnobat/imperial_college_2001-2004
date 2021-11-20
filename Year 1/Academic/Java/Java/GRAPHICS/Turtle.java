package graphics;
//import java.lang.math

public class Turtle
{
	public int x_coord = 0;
	public int y_coord = 0;

	public int orientation = 180; 	// orientation is to the nearest 45 degrees
					//IMPORTANT CONVENTION: angle will be between 0 and 360

	public String pen_state = new String();
	public char c_type = ' ';

	public void Move(int length,VirtualScreen v)
	{

		int x = x_coord;
		int y = y_coord;

		for (int i = 0; i < length; i++)
		{
			switch(orientation/45)
			{
				case 0:		y--	;	break;	//N
				case 1:	x++;	y--	;	break;	//NE
				case 2:	x++		;	break;	//E
				case 3:	x++;	y++	;	break;	//SE
				case 4:		y++	;	break;	//S
				case 5:	x--;	y++	;	break;	//SW
				case 6:	x--		;	break;	//W
				case 7:	x--;	y--	;	break;	//N
			}

			x = x%v.length;
			y = y%v.height;

			if(x == -1)
			{
				x = v.length -1;
			}
			if(y == -1)
			{
				y = v.height -1;
			}

			if(pen_state.equals("down") || i == length-1)
			{
				v.screen[y][x] = c_type;
			}

		}

		x_coord	= x;
		y_coord	= y;

	}

	public void right(int angle)
	{
		if (angle - 45*(angle/45) < 22.5)
		{
			orientation = (orientation + (45*(angle/45)))%360;
		}

		else
		{
			orientation = (orientation + (45*(angle/45 + 1)))%360;
		}

	}

	public void left(int angle)
	{

		if (angle - 45*(angle/45) < 22.5)
		{
			orientation = (orientation - (45*(angle/45))%360)%360;
		}

		else
		{
			orientation = (orientation - (45*(angle/45 + 1))%360)%360;
		}

	}

	public void pen(String st)
	{
		if (st.equals("up") || st.equals("down"))
		{
			pen_state = st;

		}

		else
		{
			c_type = st.charAt(0);
		}
	}
}
