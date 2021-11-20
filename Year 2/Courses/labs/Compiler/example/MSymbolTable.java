import java.util.*;


public class MSymbolTable {
  /** A simple  symbol table based on a hashtable
      providing methods to declare and look for identifiers
      of different kinds. Thee is no support for multiple
      scopes (e.g local and global). There is an iterator
      to get a list of all declared variables 
  */

 private Hashtable symTable;

  static final int unknown = 0;
  static final int constant = 1;
  static final int variable = 2;
  static final int procedure = 3;

  public MSymbolTable() {
   symTable = new Hashtable(100);
  }

  public boolean isDeclared( String name) {
    return symTable.get(name) != null;
  }
  
  public int getValue( String name) {
    SymbolNode result = (SymbolNode) symTable.get(name);
    if ( result != null && result.nodekind == constant) 
      return result.nodevalue;
    else 
      throw new Error("getValue used on a non constant");
  }

  public boolean isVariable( String name ) {
    SymbolNode result = (SymbolNode) symTable.get(name);
    return ( result != null && result.nodekind == variable);
  }

  public boolean isConstant( String name ) {
    SymbolNode result = (SymbolNode) symTable.get(name);
    return ( result != null && result.nodekind == constant);
  }

  public boolean isProcedure( String name ) {
    SymbolNode result = (SymbolNode) symTable.get(name);
    return ( result != null && result.nodekind == procedure);
  }

  public void declareConstant(String name, int value) {
    SymbolNode here =  new SymbolNode(name,constant,value);
    symTable.put(name,here);
  }

  public void declareVariable(String name ) {
	symTable.put(name,new SymbolNode(name,variable));
   }
   
  public void declareProcedure(String name, int arguments) {
    SymbolNode here =  new SymbolNode(name,procedure);
    here.nodearguments = arguments;
    symTable.put(name,here);
  }


  public IterateVars iterateVars() { 
    return new IterateVars();
  }  

  private class IterateVars implements  Iterator {
    /** returns an iterator for the declared variables */
    Enumeration enum;
    SymbolNode thenext ;
    String nextkey ;
 
   public IterateVars() { 
      enum = symTable.keys();
      thenext = null;
   }
    public boolean hasNext() {
      while (enum.hasMoreElements()) {
	nextkey = (String)enum.nextElement();
	thenext = (SymbolNode)symTable.get(nextkey);
	if (thenext.nodekind == variable )
	  return true;
      }
      thenext = null;
      return false;
    }

    public Object next() {
      if (thenext != null) 
	return nextkey;
      if (hasNext()) 
	return nextkey;
      else
	throw new NoSuchElementException();
    }; 
    public void remove()  { 
      throw new UnsupportedOperationException();
    }
  } 

}
class SymbolNode {
  int nodekind;
  int nodevalue;
  int nodearguments;
  String nodename;
  
  SymbolNode( String name, int kind) {
    nodekind = kind;
    nodename = name;
  }

 SymbolNode( String name, int kind, int value) {
    nodekind = kind;
    nodename = name;
    nodevalue = value ; 
  }
} 
