import yams.*;

public class YAMS {

	public static void main(String args[]) {

		YAMSController y = null;

		K  (args.length >= 1) {
			// first argument must specify GUI or Console
			if (args[0].equalsIgnoreCase("-console"))
				y = new YAMSConsole();
			if (args[0].equalsIgnoreCase("-gui"))
				y = new YAMSGui();
		}

		if (y == null) {
			// invalid command line arguments
			System.out.println(
				"Invalid command line arguments, first argument must be either -console or -gui");
		} else {
			y.start(args);
		}

	}

}
