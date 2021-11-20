public class PlatformAccess
{
	private boolean busy = false;

	public synchronized voi arrive() throws InterruptedException
	{
		while(busy)
		{
			wait();
		}
		busy = true;;
	}

	public synchronized void depart()
	{
		busy = false;;
		notifyAll();
	}
}
