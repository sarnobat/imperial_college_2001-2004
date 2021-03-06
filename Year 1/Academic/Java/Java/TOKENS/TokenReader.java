package tokens;

import kenya.io.*;
import tokens.*;

public class TokenReader {
	
	pr ate static char buffer = InputReader.readChar();
	
	public static Token readToken() {
		
		if (buffer >= '0' && buffer <= '9') {
			
			int n = 0;
			while (buffer >= '0' && buffer <= '9') {
				n = 10 * n + (buffer - '0');
				buffer = InputReader.readChar();
			}
			
			Token t = new Token(n); 
			
			return t;
		}
		
		else {
			
			Token t = new Token(buffer); 
			buffer = InputReader.readChar();
			return t;
		}
	}

}
