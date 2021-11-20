import kenya.io.InputReader;

public class Owari {
  static final int bowlsOnOneSide = 6;  static final int numberOfStones = 4;  static final int numberOfBowls = 12;  static final int winningNumber = 24;  static final int totalNumberOfStones = 48;
	public static void main(String[] args) {
		
		int [ ] aBoard = new int [numberOfBowls];
		aBoard = InitialiseBoard( aBoard);
		int [ ] player = { 0 , 0 };
		DisplayScoreAndBoard( aBoard, player);
	}
	
  static int [] InitialiseBoard( int []board )
	{
		
		// Sets up board at start of game
 
		int i;
		for( i = 0 ; i < numberOfBowls ; i++)
		{
			board[i] = numberOfStones;
		}
		return board;
	}
	
  static int  GetMove(  )
	{
		
		// returns a move input from keyboard
 
		int move = InputReader.readInt();
		System.out.println( move );
		return move;
	}
	
  static void  PutMove( int player , int bowlChosen )
	{
		
		// displays a move
 
		System.out.println( "Player: "  + player + " has chosen bowl "  + bowlChosen );
	}
	
  static void  DisplayScoreAndBoard( int []board , int []player )
	{
		
		// displays board and scores
 
		int i;
		System.out.println( "Player 2: "  + player[1] + " stones taken"  );
		for( i = numberOfBowls - 1 ; i >= numberOfBowls / 2 ; i--)
		{
			System.out.print( " "  + board[i] + "\t"  );
		}
		System.out.println( ""  );
		for( i = numberOfBowls ; i > numberOfBowls / 2 ; i--)
		{
			System.out.print( "("  + i + ")"  + "\t"  );
		}
		System.out.println( ""  );
		System.out.println( ""  );
		for( i = 1 ; i <= numberOfBowls / 2 ; i++)
		{
			System.out.print( "("  + i + ")"  + "\t"  );
		}
		System.out.println( ""  );
		for( i = 0 ; i < numberOfBowls / 2 ; i++)
		{
			System.out.print( " "  + board[i] + "\t"  );
		}
		System.out.println( ""  );
		System.out.println( "Player 1: "  + player[0] + " stones taken"  );
	}
	
}
