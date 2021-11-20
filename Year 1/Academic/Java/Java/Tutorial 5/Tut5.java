//Sridhar Sarnobat Programming II Tutorial 5 - Assessed
import java.util.*;
import java.lang.*;

/**	Tut5 is a simple grades database	*/
public class Tut5
{
	protected Vector grades;

	/**	Tut5 is the constructor method	*/
	public Tut5()
	{
		grades = new Vector();
	}

	/**	getGrades returns the set of grades	*/
	public Vector getGrades()
	{
		return grades;
	}

	/**	toString returns a string which states the number of entries in the object	*/
	public String toString()
	{
		int x = grades.size();
		String s = "This grade sheet has " + x + " entries";
		return s;
	}

	/**	addGrade adds a new entry to the object
	  *	@param g - the grade the user wishes to input	*/
	public void addGrade(double g)
	{
		grades.add(new Double (g));
	}

	/**	addGrade adds a new entry to the object
	  *	@param g - the grade the user wishes to input	*/
	public int gradeSheetSize()
	{
		return grades.size();
	}

	/**	specificGrade creates a new object containing only the grade d
	  *	@param d - the specific grade the user wishes to input	*/
	public Vector specificGrade(double d)
	{
		Enumeration enum = grades.elements();
		Vector specgrades = new Vector();
		int i = 0;

		while (enum.hasMoreElements())
		{
			if (((Double)(enum.nextElement())).doubleValue() == d)
			{
				specgrades.add(new Double (d));
			}
		}

		return specgrades;
	}

	/**	removeGrade removes all instances of grade d
	  *	@param d - the specific grade the user wishes to remove	*/
	public void removeGrade(double d)
	{
		Iterator it = grades.iterator();

		while (it.hasNext())
		{
			if (((Double)(it.next())).doubleValue() == d)
			{
				it.remove();
			}
		}
	}

}
