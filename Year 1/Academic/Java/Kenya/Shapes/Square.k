void square (int n){
//post: draws a solid square
	int row; int col;
		for (row = 0; row < n;  row ++){
			for (col = 0; col <n; col ++){
					print ("* ");
			}
			println ("");
	}
}

print ("What size of square would you ike? --> ");
square (readInt());
