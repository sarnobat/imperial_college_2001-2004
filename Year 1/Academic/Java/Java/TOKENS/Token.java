package tokens;
import tokens.*;

public class Token
{
	
	
	private boolean IsNumber;
	private int Value;
	private char Operator;
	
	public Token() {
	}
	
	public Token (int N) {
		
		Value = N;
		IsNumber = true;
	}
	
	public Token (char c) {
	
		Operator = c;
		IsNumber = false;
	}
	
	public void intToToken (int N){

	     Value = N; 
	     IsNumber = true;
	}
	
	
	public void charToToken ( char Op) {
		
	     Operator = Op; 
	     IsNumber = false;
	}
	
	
	
	public boolean isIntegerValued () { 
	
		return IsNumber;
	}
	
	
	public int tokenToInt () {

	//	assert ( IsNumber ) : "Tokens.TokenToInt: wrong kind of Token!";
		return Value;
	}
	
	
	public char tokenToChar () {

	//	assert ( ! IsNumber ) : "Tokens.TokenToChar: wrong kind of Token!";
		return Operator;
	}

	
	
}
