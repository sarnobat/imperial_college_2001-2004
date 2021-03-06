import antlr.CommonAST;
import antlr.Token;

public class MAST extends CommonAST {
  private int value;

  pub c MAST() {
    super();
  }
  public MAST ( int val ) {
    super();
    value = val;
  }
  
  public MAST ( Token t ) {
    super(t);
  }


  public void setValue(int val) {
    value = val;
  }

  public int getValue() {
    return value;
  }
  public static MAST newInstance() {
    return new MAST();
  }

}
