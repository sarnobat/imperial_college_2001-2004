package ic.doc.simulation.tools;

public abstract class SimProcess {
	static ThreadGroup allThreads = new ThreadGroup("Processes");
	Semaphore processSem = new Semaphore();
	protected double creationTime;
	double wakeTime;
	boolean isActive = false;
	SystemMeasure utilisationMeasure = new SystemMeasure();

	public SimProcess() {
		creationTime = Time.vtime;
		(new SimThread()).start();
	}

	public abstract void runProcess() throws InterruptedException;

	class SimThread extends Thread {
		public SimThread() {
			super(allThreads, "SimProcess");
		}
		public void run() {
			try {
				processSem.down();
				runProcess();
				die();
			}
			catch (InterruptedException e) {
			}
		}
	}

	public void hold(double t) throws InterruptedException {
		wakeTime = Time.vtime + t;
		PSim.procList.insert(this);
		waitToBeWoken();
	}

	public void reset() {
		utilisationMeasure.reset();
	}

	public void passivate() throws InterruptedException {
		if (!isActive)
			System.out.println("ERROR: Attempt to passivate a passive SimProcess!");
		isActive = false;
		//utilisationMeasure.update(0.0);
		waitToBeWoken();
	}

	public boolean isActive() {
		return this.isActive;
	}

	// Note: you can ONLY activate a passivated process...
	// Sleeping processes are active

	public void activate() {
		if (isActive)
			System.out.println("ERROR: Attempt to activate an active SimProcess!");
		this.isActive = true;
		//utilisationMeasure.update(1.0);
		wakeTime = Time.vtime;
		PSim.procList.insert(this);
	}

	public double utilisation() {
		return utilisationMeasure.mean();
	}

	void waitToBeWoken() throws InterruptedException {
		PSim.managerSem.up();
		processSem.down();
	}

	void die() throws InterruptedException {
		PSim.managerSem.up();
	}
}
