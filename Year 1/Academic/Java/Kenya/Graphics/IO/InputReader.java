package kenya.io;

import java.io.*;

public class InputReader
{
    static InputStreamReader isr;
    static StreamTokenizer tokenizer;

    static {
		isr =  new InputStreamReader( System.in );
		tokenizer = new StreamTokenizer( isr );
		tokenizer.resetSyntax();
		tokenizer.whitespaceChars( 0, ' ' );
		tokenizer.wordChars( 33 , 255 );
    }

	public static boolean isEOF() {
		try {
			if (tokenizer.nextToken() == StreamTokenizer.TT_EOF) {
				return true;
			} else {
				tokenizer.pushBack();
				return false;	
			}
		} catch (java.io.IOException e){ System.out.println(e); }
		return false;
	}

    public static int readInt() {
	
	try {

	    tokenizer.resetSyntax();
	    tokenizer.whitespaceChars( 0 , ' ' );
	    tokenizer.wordChars(33,255);

	    if ( tokenizer.nextToken() == StreamTokenizer.TT_EOF ) {
			System.out.println("End Of File found.");
	    } else {
			return (int)Double.parseDouble(tokenizer.sval);
	    }
	}
	catch (NumberFormatException nfe){ System.out.println("Could not read integer, incorrectly formatted number (" + tokenizer.sval + ")" ); System.exit(1);}
	catch (java.io.IOException e){ System.out.println(e); }
      
	return 0;
    } 

    public static double readDouble() {

	tokenizer.resetSyntax();
	tokenizer.whitespaceChars( 0 , ' ' );
	tokenizer.wordChars(33,255);


	try {
	    if ( tokenizer.nextToken() == StreamTokenizer.TT_EOF ) {
		System.out.println("End Of File found.");
	    } else {
		return Double.parseDouble(tokenizer.sval);
	    }
	}
	catch (NumberFormatException nfe){ System.out.println("Could not read double, incorrectly formatted number (" + tokenizer.sval + ")" ); }
	catch (java.io.IOException e){System.out.println(e);}
	
	return 0.0;
    }

    public static String readString() {

	tokenizer.resetSyntax();
	tokenizer.whitespaceChars( 0 , ' ' );
	tokenizer.wordChars( 33 , 255 );

	try {

	    if ( tokenizer.nextToken() == StreamTokenizer.TT_EOF ) {
		System.out.println("End Of File found."); 
	    } else {
		return tokenizer.sval;
	    }
	}
	catch (java.io.IOException e){System.out.println(e);}
 
	return "";
    }


    public static char readChar() {

	tokenizer.resetSyntax();
	tokenizer.whitespaceChars( ' ' , ' ' );

	try {
	    if ( tokenizer.nextToken() == StreamTokenizer.TT_EOF ) {
		System.out.println("End Of File found."); 
	    } else {
		return (char)tokenizer.ttype;
	    }
	}
	catch (java.io.IOException e){System.out.println(e);}
 	return ' ';


    }

    public static char read() {

        char c = ' ';
        try {  
	    c = (char)isr.read(); }
	catch ( IOException e ) { System.out.println(e); }
	return c;
    }

}
