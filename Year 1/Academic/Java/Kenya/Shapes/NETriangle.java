import kenya.io.InputReader;

public class NETriangle {

	public static void main(String[] arg  {
		
		System.out.print( "What size of NETriangle would you like? --> "  );
		nwtriangle( InputReader.readInt());
	}
	
  static void  nwtriangle( int n )
	{
		
		//post: draws a right
 
		int row;
		int col;
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
	
}
