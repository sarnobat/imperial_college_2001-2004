

/**
 * Complete the implementation of this class in line with the FSP model
 */

import display.*;

public class Controller {

	public static int Max = 9;
	protected NumberCanvas passengers;

	private int carSize = 0;
	public int count = 0;// count is the number of people on the platform
	//private PlatformAccess platform;


  	public Controller(NumberCanvas nc)
	{
  		passengers = nc;
  	}

	public synchronized void newPassenger() throws InterruptedException
	{
		while ( !(count<Max) )
		{
			wait();
		}
		count++;
		passengers.setValue(count);
		notifyAll();//not sure if this is supposed to be included in Part I
	}

  	public int getPassengers(int mcar) throws InterruptedException
	{//mcar is the capacity of the car
	//returns the number of people who board the car (these are not necessarily the same)
		carSize = mcar;
		while(!(carSize>0 && count>=carSize))
		{
			wait();
		}
		count -= carSize;
		passengers.setValue(count);
		carSize = 0;
		return carSize;
	}

	public synchronized void goNow()
	{
		// complete implementation for part II
	}

}
