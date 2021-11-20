package student;

import kenya.io.InputReader;

public class Course
{
        private int students = 0;	
        private CompStudent c [] = new CompStudent[100];

	public void getCourse()
	{
                for (int n=0; n<100; n++)
                   c[n] = null;

                students = 0;

		while (! InputReader.isEOF() && students < 100)
		{
			c[students] = new CompStudent();
			c[students] .getStudent();
			students = students + 1;
		}
	}


	public void sortCourse()
	{
		CompStudent temp;
		boolean changed = true;

		while (changed)
		{
			int j;
			changed = false;
			for (j = 0; j < students-1; j++)
			{
				if (c[j+1].studentLessThan(c[j]))
				{
					temp = c[j+1];
					c[j+1] = c[j];
					c[j] = temp;
					changed = true;
				}
			}
		}
	}




	public void putCourse(boolean check)
	{
		int l;
		int m;

		if (check==true)
		{
			for (l = 0; l <students; l++)
			{
				if (c[l].quota < c[l].usage)
				{
					c[l].putStudent();
					c[l].putQuotaAndUsage();
					System.out.println("");
				}
			}
		}

		if (check==false)
		{
			for (m = 0; m<students ; m++)
			{
				c[m].putStudent();
                System.out.println("");
			}
		}
	}

}
