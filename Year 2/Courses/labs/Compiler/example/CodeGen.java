import antlr.CommonAST;
import antlr.DumpASTVisitor;
import java.io.*;
import java.util.*;

public class CodeGen {

  MAST treetop ;
  MSymbolTable symbolTable ;
  PrintStream codeout;
  DumpASTVisitor visitor = new DumpASTVisitor();

  public CodeGen(PrintStream codefile, MAST tree, MSymbolTable symbols) {
    treetop = tree;
    symbolTable = symbols;
    codeout = codefile;
  }
  
  void internalError( String whatswrong, MAST thetree ) {
    // print an error message, dump the tree and quit
    System.err.println(whatswrong);
    visitor.visit(thetree);
    throw new Error(whatswrong);
  }

 void debug( String whatswrong, MAST thetree ) {
    // print an error message and dump the tree 
   System.err.println(whatswrong);
   visitor.visit(thetree);
 }

  public void module(MAST tree ) {

  }

}
