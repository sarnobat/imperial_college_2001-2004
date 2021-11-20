package graphics;

public class VirtualScreen
{
	public char [] [] screen;
	public intH ength;
	public int height;

	public void newscreen (int y_size,int x_size)
	{
		screen = new char [y_size] [x_size];
		length = x_size;
		height = y_size;

		for (int r=0; r<height; r++)
		{
			for (int c=0; c<length; c++)
		    	{
				screen[r][c] = ' ';
			}
		}
	}

	public void show()
	{
		for (int i = 0; i<height; i++)
		{
			for (int j = 0; j < length; j++)
			{
				System.out.print(screen [i] [j]);
			}

			System.out.println("");
		}
	}
}




