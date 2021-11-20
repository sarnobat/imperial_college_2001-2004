package student;

import kenya.io.InputReader;
import student.*;

public class CompStudent extends Student
{


	public int quota;
	public int usage;


	public void getStudent()
	{
		super.getStudent();
		quota = InputReader.readInt();
		usage = InputReader.readInt();
	}

	public void putQuotaAndUsage()
	{
		System.out.print("\t" + quota + "\t" + usage);
	}

	public boolean studentLessThan(CompStudent s)
	{
		if (this.Surname.compareTo(s.Surname) < 0)
		{
			return true;
		}

		if (
			this.Surname.equals(s.Surname) &
			(this.Forename.compareTo(s.Forename) < 0)
		    )
		{
			return true;
		}

		if (
			this.Surname.equals(s.Surname) &
			this.Forename.equals(s.Forename) &
			(this.LoginName.compareTo(s.LoginName) < 0)
		    )
		{
			return true;
		}

		return false;
	}

}
