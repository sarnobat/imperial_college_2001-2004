package parser;

public class Instruction
{
  private String instruction;
  OperandType operand_1;
  OperandType operand_2;
  OperandType operand_3;
  public Instruction(String i, OperandType o1, OperandType o2, OperandType o3)
  {
    instruction=i;
    operand_1=o1;
    operand_2=o2;
    operand_3=o3;
  }
  public String getInstruction() {return instruction;}
  public String getOperand_1() {return operand_1.toString();}
  public String getOperand_2() {return operand_2.toString();}
  public String getOperand_3() {return operand_3.toString();}
  public boolean isValid(MIPSInstruction m)
  {
    boolean op1validity = operand_1.isValidOperand(m.getOperand_1(),m);
    boolean op2validity = operand_2.isValidOperand(m.getOperand_2(),m);
    boolean op3validity = operand_3.isValidOperand(m.getoperand_3(),m);
    return (instruction.equals(m.getInstruction())&&
            op1validity&&
            op2validity&&
            op3validity);
  }
}
