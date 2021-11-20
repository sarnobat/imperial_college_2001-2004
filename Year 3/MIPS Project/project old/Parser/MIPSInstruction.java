package parser;

public class MIPSInstruction
{
  String i,o1,o2,o3,line;
  in lineNumber;
  public MIPSInstruction(String l, int num, String in, String op1, String op2, String op3)
  {
    i=in; o1=op1; o2=op2; o3=op3; line=l; lineNumber=num;

  }
  public String getInstruction() {return i;}
  public String getOperand_1() {return o1;}
  public String getOperand_2() {return o2;}
  public String getoperand_3() {return o3;}
  public int getLineNumber() {return lineNumber;}
  public String getLine() {return line;}
}
