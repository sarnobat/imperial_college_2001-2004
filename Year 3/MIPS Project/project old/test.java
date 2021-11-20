import java.text.*;
import com.sun.java.swing.plaf.windows.*;

import javax.swing.*;
public class test
{
	public static void main(String[] args)
	{ 
	
			//t();
		JFrame frame = new JFrame("SwingApplication");
        //...create the components to go into the frame...
        //...stick them in a container named contents...
        frame.getContentPane().add(frame, BorderLayout.CENTER);

        //Finish setting up the frame, and show it.
        //frame.addWindowListener(...);
        frame.pack();
        frame.setVisible(true);
		
	}
	public static void t(){
		
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			String input = JOptionPane.showInputDialog("Type an integer Celsius temperature:");
		 	int c = new Integer(input).intValue();  // convert  input  into an int	
			double f = ((9.0 / 5.0) * c) + 32;
			DecimalFormat formatter = new DecimalFormat("0.0");
			System.out.println("For Celsius degrees " + c + ",");
			System.out.println("Degrees Fahrenheit = " + formatter.format(f));
		}
		catch(Exception e) {
			t();
		}
	}
}