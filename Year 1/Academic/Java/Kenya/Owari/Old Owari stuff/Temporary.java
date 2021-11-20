import kenya.io.InputReader;

public class Temporary {

	public static void main(String[] args) {
		
		}
	
  static int [] getMove( int []board , int []scores , int p )
	{
		int move = InputReader.readInt();
		boolean [ ] capturable = new boolean [12];
		int k;
		for( k = 0 ; k < capturable.length ; k++)
		{
			capturable[k] = false ;
		}
		int j;
		for( j = 0 ; j < board.length ; j++)
		{
			if ( board[j] == 1 )
			{
				capturable[j] = true ;
			}
		}
		int i;
		for( i = move + 1 ; i < move + board[i] + 1 ; i++)
		{
			
			//check whether the for loop boundary condition should contain a +1
 
			board[i] = board[i] + 1;
			if ( capturable[i] )
			{
				scores[p] = scores[p] + board[i];
				board[i] = 0;
			}
		}
		return board;
	}
	
}
