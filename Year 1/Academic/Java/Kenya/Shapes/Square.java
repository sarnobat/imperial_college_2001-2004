import kenya.io.InputReader;

public class Square {

	public static void main(String[] args) {
		
		Sy em.out.print( "What size of square would you like? --> "  );
		square( InputReader.readInt());
	}
	
  static void  square( int n )
	{
		int row;
		int col;
		for( row = 0 ; row < n ; row++)
		{
			for( col = 0 ; col < n ; col++)
			{
				System.out.print( "* "  );
			}
			System.out.println( ""  );
		}
	}
	
}
