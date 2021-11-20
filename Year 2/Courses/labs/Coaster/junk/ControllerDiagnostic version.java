import display.*;

public class Controller {

	public static int Max = 9;
	protected NumberCanvas passengers;
	//protected NumberCanvas car2;

	private int carSize = 0;
	private int count = 0;// count is the number of people on the platform
	private boolean goNow = false;


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
//		System.out.println("new passenger on platform");
		notifyAll();
	}

  	public synchronized int getPassengers(int mcar) throws InterruptedException
	{/*	mcar is the capacity of the car
		returns the number of people who board the car (these are not necessarily the same)	*/
		int countBoarded;
		carSize = mcar;
//		System.out.println("Car " + mcar + " waiting");
		while( count<mcar && !goNow)
		{
			wait();
		}
//		System.out.println("Car " + mcar + " ready");
		countBoarded = min(count,carSize);
		goNow = false;
		System.out.println(countBoarded + " people boarded the car of size " + carSize + ",reducing the number of people on the platform from " + count);
		count -= countBoarded;
		System.out.print(count);
		passengers.setValue(count);
		carSize = 0;
		return countBoarded;
	}
	private int min(int a,int b)
	{
		if(a<b)
		{
			return a;
		}
		else
		{
			return b;
		}
	}

	public synchronized void goNow()
	{

		if(0<count && count<carSize && carSize>0)
		{
			System.out.println("\ncarSize = " + carSize + "\ncount = " + count);
			goNow = true;
			notifyAll();
		}
		System.out.println("\ncarSize = " + carSize + "\ncount = " + count);
	}

}
