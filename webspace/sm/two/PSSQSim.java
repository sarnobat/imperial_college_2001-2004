/*
 * Created on 20-Nov-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package two;

import ic.doc.simulation.tools.CustomerMeasure;
import ic.doc.simulation.tools.Exp;
import ic.doc.simulation.tools.Histogram;
import ic.doc.simulation.tools.PSim;
import ic.doc.simulation.tools.Queue;
import ic.doc.simulation.tools.SimProcess;

/**
 * @author ss401
 *
 */
class PSSQSim extends PSim {

	int completions = 0;
	Histogram h = new Histogram(0.25, 2.5, 10);
	Queue q = new Queue();
	Worker worker = new Worker();
	Tick clock = new Tick();

	public PSSQSim() throws InterruptedException {
		clock.activate();
		new Arrivals().activate();
		worker.activate();
		execute();
		System.out.println("Time finished: " + now());
		System.out.println("Utilisation = " + worker.utilisation());
		System.out.println("Mean queue length = " + q.meanQueueLength());
		System.out.println("Mean response time = " + q.meanTimeInQueue());
		System.out.println("Throughput = " + completions / now());

		h.display();

		System.out.println("Average number of Documents waiting in buffer = " + q.meanQueueLength());
		double variance = q.varQueueLength(); 
		System.out.println("Variance = " + variance);
		double e = (1.96 * Math.sqrt(variance) / Math.sqrt(now()));
		System.out.println("e = " + e);

		System.out.println("Average time taken for document to be transmitted having been placed in buffer = " + q.meanTimeInQueue());
		variance = q.varTimeInQueue();
		System.out.println("Variance = " + variance);
		e = (1.96 * Math.sqrt(variance) / Math.sqrt(now()));
		System.out.println("e = " + e);

	}

	public boolean stop() {
		//EXPONENTIAL DISTRIBUTION
		return now() > 100000.0;

		//CAUCHY DISTRIBUTION
		//return now() > 6000000.0;

		//SHORTENED SIMULATIONS
		//return now() > 10000;

	}

	class Arrivals extends SimProcess {
		public void runProcess() throws InterruptedException {
			while (true) {
				//UNIFORM distribution
				//hold(Math.random());

				Exp sampler = new Exp(1.7742230453151224);
				double d;
				hold(d = sampler.next());

				//System.out.println("Arrival: "+ d);

				new Customer().activate();
			}
		}
	}

	class Customer extends SimProcess {
		public void runProcess() throws InterruptedException {
			double arrTime = now();
			q.enqueue(this);
			if (!worker.isActive())
				worker.activate();
			passivate();
			h.add(now() - arrTime);
		}
	}

	class Worker extends SimProcess {
		public void runProcess() throws InterruptedException {
			while (true) {
				while (q.queueLength() > 0) {

					double networkSpeed = 10 * 1000000 / 8; // network speed (bytes/sec)
					double meanDocumentSize = 18408.402122856918; //mean document size (bytes)

					//EXPONENTIAL DISTRIBUTION
					Exp sampler = new Exp(networkSpeed / meanDocumentSize);
					double serviceTime = sampler.next();
										
					//CAUCHY DISTRIBUTION
					/*double s = 2382.88313; //1121.796181;
					double cauchyDocSize = s * Math.tan(Math.random() * Math.atan(433663396 / s));
					double serviceTime = cauchyDocSize / networkSpeed;*/

					hold(serviceTime);

					//System.out.println("Service time: " + serviceTime);
					 ((Customer) q.dequeue()).activate();
					completions++;
				}
				passivate();
			}
		}
	}

	class Tick extends SimProcess {
		public void runProcess() throws InterruptedException {
			
			
			
			//EXPONENTIAL DISTRIBUTION
			double m = 0.02680404919004544;
			double e = 0.000098;

			double lower = m - e;
			double actual;
			double upper = m + e;

			while (true) {

				actual = q.meanQueueLength();
				
				//EXPONENTIAL DISTRIBUTION
				if (actual < lower || actual > upper) {

					System.out.println(now() / 1000 + "\t" + q.meanQueueLength());

				}
				else {
					System.out.println("------------------------------------------------------------------------------------------------------------");
				}
				hold(5);
				
				//CAUCHY DISTRIBUTION
				/*System.out.println("Mean Queue Length at time " + now() + ":\t" + q.meanQueueLength());
				hold(Math.random() * 700);*/


			}
		}
	}

}
