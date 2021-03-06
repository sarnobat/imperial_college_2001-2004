package parser;

public class AlphaNumericOperandType implemen=  OperandType
{
  Errors errorHandler;
  public AlphaNumericOperandType(Errors e)
  {
    errorHandler = e;
  }

  public boolean isValidOperand(String o,MIPSInstruction m)
  {
    if (Character.isDigit(o.charAt(0)) || o.charAt(0)=='$')
    {
      errorHandler.addError(m,o,"Expected Label, Invalid case encountered");
      return false;
    }
    else return true;
  }

  public void addValidInstance(Object i)
  {
  }
}
