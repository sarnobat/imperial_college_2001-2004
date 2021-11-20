/**
 * Complete the implementation of this class in line with the FSP model
 */
import display.*;

public class Controller
{

	public static int Max = 9;
	protected NumberCanvas passengers;
	// declarations required
	private int count = 0;
	private int carSize = 0;//not sure if this should really be its default value


	public Controller(NumberCanvas nc)
	{
		passengers = nc;
	}

	public void newPassenger() throws InterruptedException
	{/*
		// complete implementation
		// use "passengers.setValue(integer value)" to update display
		while ( !(count<Max) )
		{
			wait();
		}
		count++;
		passengers.setValue(count);
	*/}

	public int getPassengers(int mcar) throws InterruptedException
	{
		/*// complete implementation for part I
		// update for part II
		// use "passengers.setValue(integer value)" to update diplay
		carSize = mcar;
		while( !(carSize>0 && count>=carSize) )
		{
			wait();
		}
		count -= carSize;
		carSize = 0;
		passengers.setValue(count);
		return 0; // dummy value to allow compilation*/
	}

	public synchronized void goNow()
	{
  		// complete implementation for part II
	}

}
