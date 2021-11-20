const int  numberOfBowls = 12;
const int  numberOfStones = 1;

const int totalNumberOfStones = 48;
const int winningNumber =  24;
const int bowlsOnOneSide = 6;

///////////////// Initialize Board /////////////////////

int [ ] InitialiseBoard(int [ ] board)
{
// Sets up board at start of game
    int i;
    for (i = 0 ; i < numberOfBowls; i++)
    {
    board [i] = numberOfStones;
    }
    return board;
}

//////////////// Receive move //////////////////////////

int GetMove()
{
  int move;
  move = readInt();
  while (move < 1 || move > 12) {
    println("<Sorry, invalid bowl, try again (1-12)>");
    move = readInt();
  }
  return move;
}

/////////////////Print Move////////////////////////////

void PutMove (int player, int bowlChosen)
{
	// displays a move
	println ("Player: " + player + " has chosen bowl " + bowlChosen);
}

/////////////////Display Scores and board///////////////

void DisplayScoreAndBoard ( int [ ] board, int [ ] player )
{
	// displays board and scores
	int i;
	println ("Player 2: " + player [1] + " stones taken");

	for (i = numberOfBowls -1; i >=  numberOfBowls / 2; i--)
	{
	   print (" " + board [i] + "\t" );
	}
	println("");

	for (i = numberOfBowls ; i >  numberOfBowls / 2; i--)
	{
    print ( "(" +i  + ")" + "\t");
	}
	println("");
	println("");

	for (i = 1; i <= numberOfBowls / 2; i++)
	{
    print ( "(" + i + ")" + "\t");
	}
	println("");

	for (i = 0; i < numberOfBowls / 2; i++)
	{
    print (" " +  board [i] + "\t" );
	}
	println("");

	println ("Player 1: " + player [0] + " stones taken");
	println("");
}



///////////////Can Player Move?//////////////////

boolean CanMove (int player, int [ ] board)
{
	if (player == 1)
	{
		int i;
		for (i = 0; i <(board.length)/2; i++)
		{
			if (board[i] != 0)
			{
				return true;
			}
		}
	}

	else
	{
		if (player == 2)
			{
				int j;
				for (j=(board.length)/2;j<board.length;j++)
				{
					if (board[j] != 0)
					{
						return true;
					}
				}
			}
	}
	return false;
}

///////////Switch Player///////////

int SwitchPlayer (int player)
{
	player = player%2 + 1;
	return player;
}

/////////////////DoMove/////////////////

int DoMove (int [ ] board,int bowl,int playerno)

{
//println("entering do move!");

	int stonespickedup = board[bowl-1];
	board[bowl-1] = 0;

	boolean capturable [12];
	int k;
	for (k=0;k<capturable.length;k++)
	{
		capturable[k] = false;
	}

	int j;
	for (j=0;j<board.length;j++)
	{
		if (board[j]==1)
		{
			capturable[j] = true;
		}

	}

	int stoneswon = 0;

	int i;
  int target;
	for (i=0;i<stonespickedup;i++)
	{

    target = (bowl + i) % 12;

		board[target] = board[target] + 1;

		if (capturable[target])
		{
			stoneswon = stoneswon + board[target];
			board[target] = 0;
		}


	}

//println("exiting domove");
	return stoneswon;

}
/////////////////////////////////////////////////////////////////

void main()
{
	int aBoard [numberOfBowls];
	aBoard = InitialiseBoard(aBoard);
	int [ ] playerscores  = {0,0};
	DisplayScoreAndBoard(aBoard,playerscores);
	int playerno = 2;

	while (playerscores[0]<24 && playerscores[1]<24)
	{
		int oneplayerscore;

		playerno = SwitchPlayer(playerno);
		if (!CanMove(playerno,aBoard))
		{
			println("Player " + playerno + " cannot make a move.");
			playerno = SwitchPlayer(playerno);	
		}

		println("<Player " + playerno + ": Please make a move>");

		int bowl;
		bowl = GetMove();

		while(aBoard[bowl-1] == 0)
		{
			println("<The selected bowl has no stones in it");
			println("Choose a valid bowl.>");
			bowl = GetMove();
		}
		if (playerno == 1)
		{
			while (bowl < 1 || bowl >6)
			{
				println("<You may only select bowls 1 to 6, try again>");
				bowl = GetMove();
			}
		}
		if (playerno == 2)
		{
			while (bowl < 7 || bowl > 12)
			{
				println("<You may only select bowls 7 to 12, try again>");
				bowl = GetMove();
			}
		}

		PutMove(playerno,bowl);
		playerscores[playerno-1] = playerscores[playerno-1] + DoMove(aBoard,bowl,playerno);
		DisplayScoreAndBoard(aBoard,playerscores);
	}
	println("<<Player " + playerno + " wins>>");


}

///////////////////////////////////////////////////////////
// there are some assertion statements reqired
// when looping around the board, the program crashes
// find out how to resume the game after an illegal user input

main();
