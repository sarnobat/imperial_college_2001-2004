import kenya.io.InputReader;

public class NWTriangle {

	public static void main(String[] arg  {
		
		System.out.print( "What size of NWTriangle would you like? --> "  );
		nwtriangle( InputReader.readInt());
	}
	
  static void  nwtriangle( int n )
	{
		int row;
		int col;
		for( row = 0 ; row < n ; row++)
		{
			for( col = 0 ; col < n - row ; col++)
			{
				System.out.print( "* "  );
			}
			System.out.println( ""  );
		}
	}
	
}
