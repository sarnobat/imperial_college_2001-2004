/*
 *      CatAdmin.java:
 *      Test harness for implementation class Database.java,
 *	which implements the interface class (DatabaseInterface.java)
 *	offering access procedures for a database of cats
 *	in a cattery and their dietary requirements.
 */

import database.*;

public class CatAdmin
{


public static void main(String[] args)
{
	boolean		finished	= false;
	String		command;
	String		name;
	int		tins;
	Database	db		= new Database();

	System.out.println("Cattery Administration Tool in Java");
	printMenu();

	while (! finished) {
		System.out.print("Select a menu option: ");
		command = kenya.io.InputReader.readString();

		if (command.equals("add")) {
			name = kenya.io.InputReader.readString();
			tins = kenya.io.InputReader.readInt();
			db.add(name, tins);

		} else if (command.equals("lookup")) {
			name = kenya.io.InputReader.readString();
			tins = db.lookup(name);
			System.out.println("NAME\tNO. OF TINS");
			if (tins != -1) {
				System.out.println(name + "\t" + tins);
			} else {
				System.out.println(name + "\t***Not found***");
			}

		} else if (command.equals("count")) {
			System.out.println("NO. OF CATS\tTOTAL NO. OF TINS");
			System.out.println
				(db.countCats() + "\t\t" + db.countTins());

		} else if (command.equals("delete")) {
			name = kenya.io.InputReader.readString();
			db.delete(name);

		} else if (command.equals("print")) {
			System.out.println("NAME\tNO. OF TINS");
			db.printDatabase();

		} else if (command.equals("help")) {
			printMenu();

		} else if (command.equals("quit")) {
			finished = true;

		} else {
			System.out.println
			 ("***Command `" + command + "' not recognized***");
			printMenu();

		}

		System.out.println();
	}

	System.out.println("Thank you for using Cattery Administration Tool");
}


public static void printMenu()
{
	System.out.println("");
        System.out.println("	****** MENU OPTIONS ******");
        System.out.println("");
        System.out.println("	add name tins	- add a cat");
        System.out.println("	delete name	- delete a cat");
        System.out.println("	print		- print the whole database");
        System.out.println("	lookup name	- look up a cat by name");
        System.out.println("	count		- count the cats and tins");
        System.out.println("	help		- show this menu again");
        System.out.println("	quit		- quit!");
        System.out.println("");
}


}
