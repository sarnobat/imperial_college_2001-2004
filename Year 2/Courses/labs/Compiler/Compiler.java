import java.io.*;
import antlr.*;
import  antlr.debug.misc.*;
// The main program which call the lexer, parser
// and optionally the code generator
public class Compiler  {

  public static void main ( String argv[] )
    throws  antlr.TokenStreamException
  {
    boolean generateCode = false;
    boolean printTree = ! generateCode ;
    String sourcename = "*STANDARD INPUT*";
    FileReader source = null;
    int sourceFiles = 0;
    for ( int i = 0; i < argv.length; i++) {
      if ("-notree".startsWith(argv[i]))
	printTree = false;
      else if ("-tree".startsWith(argv[i])) 
	printTree = true;
      else if ("-nocode".startsWith(argv[i]))
	generateCode = false;
      else if ("-code".startsWith(argv[i]))
	generateCode = true;
      else if (argv[i].startsWith("-")) {
	System.err.println("The option "+argv[i]+" is not recognised");
	System.err.println(" the option -[no]code controls code generation"); 
	System.err.println(" the option -[no]tree controls tree dump"); 
	System.exit(1);
      }
      else {
	sourcename = argv[i];
	if ( sourceFiles == 1 ) {
	  System.err.println("Only one source file can be compiled at time");
	  System.exit(1);
	}
	sourceFiles++;
      }
    }
    Decm2Lexer lexer;
    if ( sourceFiles > 0) {
      try {
	source = new FileReader(sourcename);
      }
      catch ( FileNotFoundException e ) {
	if (sourcename.indexOf('/') < 0) {
	  // doesnt have a directory name
	  String trythis = "/vol/lab/secondyear/compilertest/"+sourcename;
	  try {
	    source = new FileReader(trythis);
	  }
	  catch ( FileNotFoundException e1 ) {
	    System.err.println("The source "+sourcename+" is not readable");
	    System.exit(1);
	  }
	}
	else { 
	    System.err.println("The source "+sourcename+" is not readable");
	    System.exit(1);
	}
      }
      lexer = new Decm2Lexer(source);
    }
    else {
      lexer = new Decm2Lexer(System.in);
    }
    Decm2Parser parser = new Decm2Parser(lexer);
    // Add any builtin names to the symbol table
      parser.symbolTable.declareProcedure( "PutInteger",1);
      parser.symbolTable.declareProcedure( "PutLine",0);
    // specify what class is used to build the tree
    // start parsing a module
      parser.setASTNodeClass("MAST");
      try {
	parser.module();
      }
      catch (RecognitionException e) {
	System.err.println("error: "+e);
	System.exit(1);
      }
      catch (TokenStreamRecognitionException e) {
	System.err.println("error: "+e);
	System.exit(1);
      }
      
      if ( parser.errorcount == 0 ) {
	MAST tree = (MAST)parser.getAST();
	if (printTree && tree != null ) {
	  System.out.println("The ast for "+sourcename+ " is: ");
	  DumpASTVisitor visitor = new DumpASTVisitor();
	  visitor.visit(tree);
	}
	if (generateCode && tree != null) {
	  PrintStream code = System.out;
	  if ( !sourcename.equals("*STANDARD INPUT*")) {
	    String codename = sourcename;
	    int lasts = codename.lastIndexOf("/");
	    if ( lasts > 0 ) {
	      codename = codename.substring(lasts+1);
	    }
	    if ( sourcename.endsWith(".mod")) {
	      codename = codename.substring(0,codename.length()-4);
	    }
	    codename = codename+".asm";
	    FileOutputStream codeout = null;
	    try {
	      codeout = new FileOutputStream(codename);
	    }
	    catch ( IOException e) {
	      System.err.println("Error opening "+codename+e);
	      System.exit(1);
	    }
	    code = new PrintStream(codeout);
	  }
	  CodeGen codegen = new CodeGen(code,tree,parser.symbolTable);
	  codegen.module(tree);
	  code.close();
	}
      }
      else {
	if (parser.errorcount != 0) {
	  if (parser.errorcount == 1) {
	    System.out.println("There was an error compiling "+sourcename);
	  }
	  else {
	    System.out.println("There were "+parser.errorcount+" errors compiling "+sourcename);
	  }
	  System.exit(parser.errorcount);
	}
      }
  }
}
