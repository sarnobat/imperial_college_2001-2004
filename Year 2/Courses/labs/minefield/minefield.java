import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class minefield extends JApplet
{
	Container c = new Container();
	Grid g;
	JButton def = new JButton("Default bombs");
	JButton ran = new JButton("Randomize");
	ButtonController bc = new ButtonController();
	JPanel buttons = new JPanel();
	JLabel com;

	public void init()
	{
		c = getContentPane();

		g = new Grid();

		def.addMouseListener(bc);
		ran.addMouseListener(bc);

		buttons.add(def);
		buttons.add(ran);

		String defBombs = Integer.toString(g.getBombCount());
		com = new JLabel(defBombs,javax.swing.SwingConstants.CENTER);
		c.add(com,BorderLayout.NORTH);
		c.add(g,BorderLayout.CENTER);
		c.add(buttons,BorderLayout.SOUTH);

		c.add(com,BorderLayout.NORTH);
		validate();
	}
	public void start()
	{
		String bombs = Integer.toString(g.getBombCount());
		com.setText(bombs);
		g.setLabel(com);
		c.add(com,BorderLayout.NORTH);
		validate();
	}

	public void newGrid(int i)
	{//if i is 1, a random grid will be generated. Otherwise the default one will be.
		c.remove(g);
		if (i==1)
		{
			g = new Grid(i);
		}
		else
		{
			g = new Grid();
		}
		c.add(g,BorderLayout.CENTER);
		start();
	}
	class ButtonController extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			if (SwingUtilities.isLeftMouseButton(e))
			{
				JButton b = (JButton) e.getSource();
				if (b == def)
				{
					newGrid(0);
				}
				else if (b == ran)
				{
					newGrid(1);
				}
			}
		}
	}
}
