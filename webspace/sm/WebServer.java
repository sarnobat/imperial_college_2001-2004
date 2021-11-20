import ic.doc.simulation.tools.CustomerMeasure;
import ic.doc.simulation.tools.Measure;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.util.StringTokenizer;

/*
 * Created on 18-Nov-2003
 */

/**
 * @author ss401
 *
 */
public class WebServer {

	public static void main(String[] args) {
		
		//Question 1
		Measure m = new CustomerMeasure();

		BufferedReader buf = null;
		try {
			buf = new BufferedReader(new FileReader("access_log.0980035200.dat"));
		}
		catch (FileNotFoundException e) {
			System.out.println("Could not read in file");
			System.exit(-1);
		}

		String line = null;
		int largerstNum = 0;
		do {
			try {
				line = buf.readLine();
			}
			catch (IOException e2) {
				System.out.println("Could not read in a line");
				System.exit(-1);
			}
			if (line == null) {
				System.out.println("End of file reached");
				break;
			}

			StringTokenizer t = new StringTokenizer(line, " ", false);
			t.nextElement();
			t.nextElement();
			t.nextElement();

			String x = (String) t.nextElement();
			int value;
			if (x.equals("-")) {
				value = 0;
			}
			else {
				value = Integer.parseInt(x);
			}

			m.add(value);
			
			if(value > largerstNum){
				largerstNum = value;
			}
		}while (line != null);
		
		Time t0 = new Time(00,00,02);
		Time tn = new Time(23,59,57);
		
		//period in SECONDS
		long period = (tn.getTime()-t0.getTime())/1000;
		
		System.out.println("Successfully read in file");

		System.out.println("Total requests: " + m.count());
		System.out.println("Period: " + period);
		System.out.println("Reqests per second: " + (double) m.count()/period);
		System.out.println("Mean: " + m.mean());
		System.out.println("Variance: " + m.variance());
		System.out.println("Standard Deviation: " + Math.sqrt(m.variance()));
		
		System.out.println("Largest Number: " + largerstNum);

	}
}
