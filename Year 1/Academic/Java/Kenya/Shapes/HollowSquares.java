import kenya.io.InputReader;

public class HollowSquares {

	public static void main(String[] args) {
		
		
		/////////////// SERIES OF HOLLOW SQUARES ///////////////
 
		
		//////////////// ONE HOLLOW SQUARE  /////////////////
 
		
		//comment: (end of helper function hsquare)
 
		System.out.print( "Enter the square range you wish to print --> "  );
		hSquareSeries( InputReader.readInt(), InputReader.readInt());
	}
	
  static void  hSquareSeries( int first , int last )
	{
		int p;
		int q;
		for( q = first ; q <= last ; q++)
		{
			hsquare( last - q, q);
			System.out.println( ""  );
			System.out.println( ""  );
		}
	}
	
  static void  hsquare( int m , int n )
	{
		int row;
		int col;
		
		//comment: the following deals with row zero
 
		for( row = 0 ; row < 1 ; row++)
		{
			for( col = 0 ; col < m ; col++)
			{
				System.out.print( "  "  );
			}
			for( col = m ; col < n + m ; col++)
			{
				System.out.print( "* "  );
			}
			System.out.println( ""  );
		}
		
		//comment: the following deals with row 1 to row n-1
 
		for( row = 1 ; row < n - 1 ; row++)
		{
			for( col = 0 ; col < m ; col++)
			{
				System.out.print( "  "  );
			}
			for( col = m ; col < m + 1 ; col++)
			{
				System.out.print( "* "  );
			}
			for( col = m + 1 ; col < n - 1 + m ; col++)
			{
				System.out.print( "  "  );
			}
			for( col = n - 1 + m ; col < n + m ; col++)
			{
				System.out.print( "* "  );
			}
			System.out.println( ""  );
		}
		
		//comment: the following deals with last row
 
		for( row = n - 1 ; row < n ; row++)
		{
			for( col = 0 ; col < m ; col++)
			{
				System.out.print( "  "  );
			}
			for( col = m ; col < n + m ; col++)
			{
				System.out.print( "* "  );
			}
		}
	}
	
}
