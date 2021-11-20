import kenya.io.InputReader;

public class Fibonacci {

	public static void main(String[] args) {
		
		int Position;
		int FirstValue;
		int SecondValue;
		System.out.print( "Please state the position of the required term followed by the first two terms of your Fibonacci sequence -> "  );
		Position = InputReader.readInt();
		FirstValue = InputReader.readInt();
		SecondValue = InputReader.readInt();
		System.out.println( ""  );
		System.out.println( "The required term is "  + Fibonacci( Position, FirstValue, SecondValue) );
	}
	
  static int  Fibonacci( int Position , int FirstValue , int SecondValue )
	{
		//assert is not implemented in versions of Java below 1.4
		//assert ( Position >= 0 ) : "cannot be a negative ordinal" ;
		if ( Position == 0 )
		{
			return FirstValue;
		}
		if ( Position == 1 )
		{
			return SecondValue;
		}
		else
		{
			return ( Fibonacci( Position - 1, SecondValue, SecondValue + FirstValue) );
		}
	}
	
}
