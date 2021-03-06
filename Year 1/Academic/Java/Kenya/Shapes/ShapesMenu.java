import kenya.io.InputReader;

public class ShapesMenu {

	public static void main(String[] args) {
		
		System.out.println( "***********Shapes Menu****************"  );
		System.out.println( "1. Square"  );
		System.out.println( "2. SWTriangle"  );
		System.out.println( "3. NWTrianlge"  );
		System.out.println( "4. NETriangle"  );
		System.out.println( "5. Hollow squares"  );
		System.out.println( ""  );
		System.out.println( "*****enter your choice (1 to 5):******"  );
		processMenu( InputReader.readInt());
		
		////////////////////////////////////////////////////////
 
		
		/////////////////////// SQUARE /////////////////////////
 
		
		////////////////////////////////////////////////////////
 
		
		////////////////////////////////////////////////////////
 
		
		/////////////////// SWTRIANGLE /////////////////////////
 
		
		////////////////////////////////////////////////////////
 
		
		///////////////////////////////////////////////////////////////
 
		
		/////////////////// NWTRIANGLE ////////////////////////////////
 
		
		///////////////////////////////////////////////////////////////
 
		
		///////////////////////////////////////////////////////////////
 
		
		//////////////////// NETRIANGLE ///////////////////////////////
 
		
		///////////////////////////////////////////////////////////////
 
		
		////////////////////////////////////////////////////////////
 
		
		/////////////////// SERIES OF HOLLOW SQUARES ///////////////
 
		
		////////////////////////////////////////////////////////////
 
		
		//////////////////////////////////////////////////////////////
 
		
		/////////////////// SINGLE HOLLOW SQUARE  ////////////////////
 
		
		//////////////////////////////////////////////////////////////
 
		
		//////////// (end of helper function hsquare) /////////////////
 
		
		////////////////////////////////////////////////////////////////
 
		
		////////////////////////////////////////////////////////////////
 
		
		////////////////////////////////////////////////////////////////
 
	}
	
  static void  processMenu( int reply )
	{
		//assert is not implemented in versions of Java below 1.4
		//assert ( 1 <= reply && reply <= 5 ) : "must be a number between 1 and 5" ;
		switch ( reply )
		{
			case 1: 
			{
				putSquare( );
				break;
			}
			case 2: 
			{
				putSWTriangle( );
				break;
			}
			case 3: 
			{
				putNWTriangle( );
				break;
			}
			case 4: 
			{
				putNETriangle( );
				break;
			}
			case 5: 
			{
				putHSquareSeries( );
				break;
			}
		}
	}
	
  static void  putSquare(  )
	{
		int row;
		int col;
		int n;
		System.out.print( "What size of square would you like? --> "  );
		n = InputReader.readInt();
		for( row = 0 ; row < n ; row++)
		{
			for( col = 0 ; col < n ; col++)
			{
				System.out.print( "* "  );
			}
			System.out.println( ""  );
		}
	}
	
  static void  putSWTriangle(  )
	{
		int row;
		int col;
		int n;
		System.out.print( "What size of SWTriangle would you like? --> "  );
		n = InputReader.readInt();
		for( row = 0 ; row < n ; row++)
		{
			for( col = 0 ; col < row + 1 ; col++)
			{
				System.out.print( "* "  );
			}
			System.out.println( ""  );
		}
	}
	
  static void  putNWTriangle(  )
	{
		int row;
		int col;
		int n;
		System.out.print( "What size of NWTriangle would you like? --> "  );
		n = InputReader.readInt();
		for( row = 0 ; row < n ; row++)
		{
			for( col = 0 ; col < n - row ; col++)
			{
				System.out.print( "* "  );
			}
			System.out.println( ""  );
		}
	}
	
  static void  putNETriangle(  )
	{
		int row;
		int col;
		int n;
		System.out.print( "What size of NETriangle would you like? --> "  );
		n = InputReader.readInt();
		for( row = 0 ; row < n ; row++)
		{
			for( col = 0 ; col < row ; col++)
			{
				System.out.print( "  "  );
			}
			for( col = 0 ; col < n - row ; col++)
			{
				System.out.print( "* "  );
			}
			System.out.println( ""  );
		}
	}
	
  static void  putHSquareSeries(  )
	{
		int p;
		int q;
		int first;
		int last;
		System.out.print( "Enter the square range you wish to print --> "  );
		first = InputReader.readInt();
		last = InputReader.readInt();
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
		
		//post: deals with row zero
 
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
		
		/////////the following deals with row 1 to row n-1 ///////////
 
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
		
		//////////// the following deals with last row ////////////////
 
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
