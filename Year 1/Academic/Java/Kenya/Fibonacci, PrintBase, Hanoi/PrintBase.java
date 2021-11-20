import kenya.io.InputReader;

public class PrintBase {

	public static void main(String[] args) {
		
		System.out.print( "Enter the number which you wish to convert followed by the base to which you wish to convert ->"  );
		printBase( InputReader.readInt(), InputReader.readInt());
	}
	
  static void  printBase( int number , int base )
	{
		if ( number < base )
		{
			putOneDigit( number);
		}
		else
		{
			int quotient;
			int remainder;
			quotient = number / base;
			remainder = number % base;
			printBase( quotient, base);
			putOneDigit( remainder);
		}
	}
	
  static void  putOneDigit( int digit )
	{
		if ( digit < 10 )
		{
			System.out.print( digit );
		}
		else
		{
			switch ( digit )
			{
				case 10: 
				{
					System.out.print( "A"  );
					break;
				}
				case 11: 
				{
					System.out.print( "B"  );
					break;
				}
				case 12: 
				{
					System.out.print( "C"  );
					break;
				}
				case 13: 
				{
					System.out.print( "D"  );
					break;
				}
				case 14: 
				{
					System.out.print( "E"  );
					break;
				}
				case 15: 
				{
					System.out.print( "F"  );
					break;
				}
				default :
				{
					System.out.println( "No such number"  );
				}
			}
		}
	}
	
}
