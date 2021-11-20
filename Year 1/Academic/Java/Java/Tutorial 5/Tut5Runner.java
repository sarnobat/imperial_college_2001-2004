import java.util.*;
public class Tut5Runner extends Tut5 {

  public static void main(String args[]) {

    Tut5 sheet = new Tut5();
	
    sheet.grades.add(new Double(8.5));
    sheet.grades.add(new Double(4.5));
    sheet.grades.add(new Double(9.0));
    sheet.grades.add(new Double(10.0));
    sheet.grades.add(new Double(3.5));
    sheet.grades.add(new Double(7.0));
    sheet.grades.add(new Double(6.5));
    sheet.grades.add(new Double(10.0));
    sheet.grades.add(new Double(8.5));
    sheet.grades.add(new Double(9.5));
    sheet.grades.add(new Double(10.0));
    sheet.grades.add(new Double(2.5));
    sheet.grades.add(new Double(8.0));
    sheet.grades.add(new Double(7.5));
    sheet.grades.add(new Double(6.5));
    sheet.grades.add(new Double(8.5));
    sheet.grades.add(new Double(9.5));
    sheet.grades.add(new Double(2.0));

    System.out.println("Grade sheet: " + sheet.getGrades());
    System.out.println(sheet.toString());

    if (sheet.gradeSheetSize() > 5 ) { sheet.addGrade(-2.0); }

    System.out.println("All top marks are " + sheet.specificGrade(10.0));
    sheet.removeGrade(10.0);
    System.out.println("All grades other than 10.0 are " + sheet.grades);
  }
}
