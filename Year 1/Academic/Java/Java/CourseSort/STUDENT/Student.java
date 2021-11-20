package student;

import kenya.io.InputReader;
import student.*;

public class Student 
{

protected String  LoginName;
protected String  Forename;
protected String  Surname;

public void getStudent()
	{
//    gets one student record from standard input, fields whitespace separated 
 
		LoginName = InputReader.readString();
		Forename = InputReader.readString();
		Surname = InputReader.readString();
	}
	
public void  putStudent()
	{
//    puts one student record on standard output, fields whitespace separated 
 
//		System.out.println( LoginName + '\t'  + Forename + '\t'  + Surname );
		System.out.print( LoginName + '\t'  + Forename + '\t'  + Surname );
	}

}
