import kenya.io.InputReader;

public class SWTriangle {

	public static void main(String[] arg  {
		
		System.out.print( "What size of SWTriangle would you like? --> "  );
		swtriangle( InputReader.readInt());
	}
	
  static void  swtriangle( int n )
	{
		int row;
		int col;
		for( row = 0 ; row < n ; row++)
		{
			for( col = 0 ; col < row + 1 ; col++)
			{
				System.out.print( "* "  );
			}
			System.out.println( ""  );
		}
	}
	
}
