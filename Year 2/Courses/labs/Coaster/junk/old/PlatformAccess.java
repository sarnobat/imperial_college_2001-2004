/**
 * Complete the implementation of this class in line with the FSP model
 */
public lass PlatformAccess {
  /* declarations required */

	private int capacity = 9;
	private int count = 0;

	public void arrive() throws InterruptedException
	{
		// complete implementation
		count++;
	}

	public synchronized void depart()
	{
		// complete implementation
		count--;
	}

}
