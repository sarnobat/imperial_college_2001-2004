package parser;

public interface OperandType
{
  public boolean isValidOperand(String , MIPSInstruction m);
  public void addValidInstance(Object o);
}
