/*
 *	DatabaseInterface.java:
 *	interface class offering access procedures for a database of cats
 *	in a cattery and their dietary requirements.
 *
 *	Note that the database itself never appears as a parameter to any of
 *	the access procedures; rather, it is an encapsulated variable,
 *	declared and initialized in the implementation class (Database.java).
 */

package database;
import database.*;

public interface DatabaseInterface
{


public void add (String name, int tins);
/*
 * If the named cat is not present in the database, adds a fresh entry,
 * in the correct place, with the given name and number of tins.
 * Otherwise, that existing cat has its tins changed to the given value.
 */


public int lookup (String name);
/*
 * If the cat is in the database, returns that cat's weekly quota of tins.
 * Otherwise returns -1.
 */


public int countCats();
/*
 * Returns the total number of cats in the database.
 */


public int countTins();
/*
 * Returns the total number of tins eaten each week by all the cats.
 */


public void delete (String name);
/*
 * If the named cat is present in the database, deletes it.
 * Otherwise leaves the database unchanged.
 */


public void printDatabase();
/*
 * Displays contents of the database as <Name> [tab] <Tins>
 * (e.g., Tibbles	14), in alphabetical order of cats, one to a line.
 */


}
