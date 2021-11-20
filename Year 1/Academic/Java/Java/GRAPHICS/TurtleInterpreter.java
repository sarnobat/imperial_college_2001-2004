import kenya.io.InputReader;
import graphics.*;

public class TurtleInterpreter
{
	public static void main(String[] args)
	{
		Turtle t = new Turtle();

		VirtualScreen screen = new VirtualScreen();

		String command = new String();
		command = InputReader.readString();

		while (!InputReader.isEOF())
		{
			if(command.equals("new"))
			{
				int h = InputReader.readInt();
				int w = InputReader.readInt();
				screen.newscreen(h,w);
			}

			if(command.equals("pen"))
			{
				String s = InputReader.readString();
				t.pen(s);
			}

			if(command.equals("move"))
			{
				int le = InputReader.readInt();
				t.Move(le,screen);
			}

			if(command.equals("right"))
			{
				int a = InputReader.readInt();
				t.right(a);
			}

			if(command.equals("left"))
			{
				int b = InputReader.readInt();
				t.left(b);
			}

			if(command.equals("show"))
			{
				screen.show();
			}

			command = InputReader.readString();

		}


	}
}