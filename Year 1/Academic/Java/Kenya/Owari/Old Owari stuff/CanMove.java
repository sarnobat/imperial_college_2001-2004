public class CanMove {

	public static void main(String[] args) {
		
		}
	
  static boolean  CF Move( int player , int []board )
	{
		if ( player == 1 )
		{
			int i;
			for( i = 0 ; i < ( board.length ) / 2 ; i++)
			{
				if ( board[i] != 0 )
				{
					return true ;
				}
			}
		}
		else
		{
			if ( player == 2 )
			{
				int j;
				for( j = ( board.length ) / 2 ; j < board.length ; j++)
				{
					if ( board[j] != 0 )
					{
						return true ;
					}
				}
			}
		}
		return false ;
	}
	
}
