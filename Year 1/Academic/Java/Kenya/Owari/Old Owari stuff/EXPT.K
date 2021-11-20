const int  numberOfBowls = 12;
const int  numberOfStones = 4;

const int totalNumberOfStones = 48;
const int winningNumber =  24;
const int bowlsOnOneSide = 6;



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


int GetMove()
{
// returns a move input from keyboard
	int move = readInt();
	println (move);
	return move;
}


void PutMove (int player, int bowlChosen)
{
// displays a move
	println ("Player: " + player + " has chosen bowl " + bowlChosen);
}

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
	}



int aBoard [numberOfBowls];
aBoard = InitialiseBoard(aBoard);
int [ ] player = {0,0};
DisplayScoreAndBoard(aBoard, player);
