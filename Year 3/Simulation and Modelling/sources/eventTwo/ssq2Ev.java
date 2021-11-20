package eventTwo;
import ic.doc.simulation.tools.*;

class SSQSim extends Sim {
	Queue q = new Queue();
	Resource s = new Resource();
	Histogram h = new Histogram(0, 5, 10);
	int n = 0;

	class Arrival extends Event {
		public Arrival(double t) {
			super(t);
		}
		public void call() {
			new Arrival(Exp.exp(1.7742230453151224));
			n++;
			q.enqueue(new Double(now()));
			if (!s.isBusy()) {
				s.claim();
				new Departure(Exp.exp((10 * 1000000 / 8) / (18408.402122856918)));
			}
		}
	}

	class Departure extends Event {
		public Departure(double t) {
			super(t);
		}
		public void call() {
			h.add(now() - ((Double) q.dequeue()).doubleValue());
			if (q.queueLength() > 0)
				new Departure(Exp.exp((10 * 1000000 / 8) / (18408.402122856918)));
			else
				s.release();
		}
	}

	class Tick extends Event {
		public Tick(double t) {
			super(t);
		}
		public void call() {
			//h.add(now() - ((Double) q.dequeue()).doubleValue());
			if (q.queueLength() > 0)
				new Tick(1);
			else
				s.release();
		}
	}

	public boolean stop() {
		return n > 1000000;
	}

	public SSQSim() {
		new Arrival(Exp.exp(1.7742230453151224));
		new Tick(100);
		execute();
		System.out.println("Utilisation = " + s.utilisation());
		System.out.println("Mean queue length = " + q.meanQueueLength());
		System.out.println("Mean response time = " + q.meanTimeInQueue());
		System.out.println("Throughput = " + n / now());

		h.display();
		System.out.println("Average number of Documents waiting in buffer = " + q.meanQueueLength());
		double lambda = 1 / q.meanQueueLength();
		double variance = (1 / Math.pow(lambda, 2));
		System.out.println("Variance = " + variance);
		double e = (1.96 * Math.sqrt(variance) /Math.sqrt(n) );
		System.out.println("e = " + e);
		
		System.out.println("Average time taken for document to be transmitted having been placed in buffer = " + q.meanTimeInQueue());
		lambda = 1 / q.meanTimeInQueue();
		variance = (1 / Math.pow(lambda, 2));
		System.out.println("Variance = " + variance);
		e = (1.96 * Math.sqrt(variance) /Math.sqrt(n) );
		System.out.println("e = " + e);
	}
}

public class ssq2Ev {

	public static void main(String args[]) {
		/*for (int i = 1; i < 7; i++) {
			System.out.println();
			System.out.println("Run " + i);
			System.out.println("------\n");*/
			
			new SSQSim();
			/*double upper1 = 0.02684620206610429+5.261852974030611E-5;
			double lower1 = 0.02684620206610429-5.261852974030611E-5;
			double upper2 = 0.015137102863375003+ 2.9668706777865323E-5;
			double lower2 = 0.015137102863375003-2.9668706777865323E-5;
			
			System.out.println(lower1+ "< Mean Buffer Size <" +upper1  );
			System.out.println(lower2+ "< Mean Time in Buffer <" +upper2  );*/
		//}
		
	}
}
