package parser;

import java.util.*;

public class RegisterOperandType implements OperandType
{
  List registerList;
  Errors errorHandler;
  public RegisterOperandType(Errors e)
  {
    registerList = new ArrayList();
    errorHandler = e;
  }
  public boolean isValidOperand(String o,MIPSInstruction m)
  {
    if (!registerList.contains(o))
    {
      errorHandler.addError(m,o,"Register Operand Expected, Invalid case encountered");
      return false;
    }
    else return true;
  }
  public void addValidInstance(Object i)
  {
    registerList.add((String)i);
  }
  public String toString()
  {
	return registerList.toString();/*StringBuffer regList = new StringBuffer("[");
	Iterator iterRegs = registerList.iterator();
	while(iterRegs.hasNext()){
		regList.append(iterRegs.next() +",");
	}
	regList.append("]");
	return regList.toString();*/
  }
}
