/////////////// SERIES OF HOLLOW SQUARES ///////////////

void hSquareSeries (int first, int last){
	int p; int q;
	for (q = first; q <= last; q++){
		hsquare (last-q,q);
		println ("");
		println ("");
	}
}


//////////////// ONE HOLLOW SQUARE  ////////////////////

void hsquare (int m,int n){
int row; int col;
//comment: the following deals with row zero
	for (row = 0; row < 1; row ++){
		for (col = 0; col < m; col ++){
			print ("  ");
		}
		for (col = m; col < n+m; col ++){
			print ("* ");
		}
	println ("");
	}

//comment: the following deals with row 1 to row n-1
	for	(row = 1; row < n-1; row ++){
		for (col = 0; col < m; col ++){
			print ("  ");
		}
		for (col = m; col <m+1; col++){
			print ("* ");
		}
		for (col = m+1; col < n-1+m; col ++){
			print ("  ");
		}
		for  (col = n-1+m; col < n+m; col++){
			print ("* ");
		}
	println ("");
	}

//comment: the following deals with last row
	for (row = n-1; row < n; row ++){
		for (col = 0; col < m; col ++){
			print ("  ");
		}
		for (col = m; col < n+m; col ++){
			print ("* ");
		}
	}
}

//////////// (end of helper function hsquare) //////////

print ("Enter the square range you wish to print --> ");
hSquareSeries (readInt(),readInt());
