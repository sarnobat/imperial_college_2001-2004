import kenya.io.InputReader;
import student.*;

public class CourseSort 
{

public static voidW ain(String[] args) 
{
	Main( );
}
	

	
public static void  Main(  )
	{
		
		// Main program
 
		Course ACourse = new Course();
		System.out.println( "Please input the course: "  );
		ACourse.getCourse ();
		ACourse.putCourse(false);
		ACourse.sortCourse();
		System.out.println( "Sorted course"  );
		ACourse.putCourse(true);
	}
	
}

