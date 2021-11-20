import kenya.io.InputReader;

public class Owari {
  static final int numberOfStones = 1;  static final int winningNumber = 24;  static final int numberOfBowls = 12;  static final int bowlsOnOneSide = 6;  static final int totalNumberOfStones = 48;
	public static void main(String[] args) {
		
		
		///////////////// Initialize Board /////////////////////
 
		
		//////////////// Receive move //////////////////////////
 
		
		/////////////////Print Move////////////////////////////
 
		
		/////////////////Display Scores and board///////////////
 
		
		///////////////Can Player Move?//////////////////
 
		
		///////////Switch Player///////////
 
		
		/////////////////DoMove/////////////////
 
		
		/////////////////////////////////////////////////////////////////
 
		
		///////////////////////////////////////////////////////////
 
		
		// there are some assertion statements reqired
 
		
		// when looping around the board, the program crashes
 
		
		// find out how to resume the game after an illegal user input
 
		main( );
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
		int move;
		move = InputReader.readInt();
		while ( move < 1 || move > 12 )
		{
			System.out.println( "<Sorry, invalid bowl, try again (1-12)>"  );
			move = InputReader.readInt();
		}
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
		System.out.println( ""  );
	}
	
  static boolean  CanMove( int player , int []board )
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
	
  static int  SwitchPlayer( int player )
	{
		player = player % 2 + 1;
		return player;
	}
	
  static int  DoMove( int []board , int bowl , int playerno )
	{
		
		//println("entering do move!");
 
		int stonespickedup = board[bowl - 1];
		board[bowl - 1] = 0;
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
		int stoneswon = 0;
		int i;
		int target;
		for( i = 0 ; i < stonespickedup ; i++)
		{
			target = ( bowl + i ) % 12;
			board[target] = board[target] + 1;
			if ( capturable[target] )
			{
				stoneswon = stoneswon + board[target];
				board[target] = 0;
			}
		}
		
		//println("exiting domove");
 
		return stoneswon;
	}
	
  static void  main(  )
	{
		int [ ] aBoard = new int [numberOfBowls];
		aBoard = InitialiseBoard( aBoard);
		int [ ] playerscores = { 0 , 0 };
		DisplayScoreAndBoard( aBoard, playerscores);
		int playerno = 2;
		while ( playerscores[0] < 24 && playerscores[1] < 24 )
		{
			int oneplayerscore;
			playerno = SwitchPlayer( playerno);
			if ( !CanMove( playerno, aBoard) )
			{
				System.out.println( "Player "  + playerno + "cannot make a move."  );
				playerno = SwitchPlayer( playerno);
			}
			System.out.println( "<Player "  + playerno + ": Please make a move>"  );
			int bowl;
			bowl = GetMove( );
			while ( aBoard[bowl - 1] == 0 )
			{
				System.out.println( "<The selected bowl has no stones in it"  );
				System.out.println( "Choose a valid bowl.>"  );
				bowl = GetMove( );
			}
			if ( playerno == 1 )
			{
				while ( bowl < 1 || bowl > 6 )
				{
					System.out.println( "<You may only select bowls 1 to 6, try again>"  );
					bowl = GetMove( );
				}
			}
			if ( playerno == 2 )
			{
				while ( bowl < 7 || bowl > 12 )
				{
					System.out.println( "<You may only select bowls 7 to 12, try again>"  );
					bowl = GetMove( );
				}
			}
			PutMove( playerno, bowl);
			playerscores[playerno - 1] = playerscores[playerno - 1] + DoMove( aBoard, bowl, playerno);
			DisplayScoreAndBoard( aBoard, playerscores);
		}
		System.out.println( "<<Player "  + playerno + " wins>>"  );
	}
	
}
