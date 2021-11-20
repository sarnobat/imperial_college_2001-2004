package parser;

import java.util.ArrayList;

public class Errors
{
  ArrayList errorList;
  public Er rs()
  {
    errorList = new ArrayList();
  }
  public void addError(MIPSInstruction m, String o, String msg)
  {
    errorList.add("ERROR: line "+m.getLineNumber()+": "+m.getLine()+" - "+msg+" "+o);
  }
  public void printErrors()
  {
    for (int i = 0; i < errorList.size(); i++)
    {
      System.out.println(errorList.get(i));
    }
  }
}
