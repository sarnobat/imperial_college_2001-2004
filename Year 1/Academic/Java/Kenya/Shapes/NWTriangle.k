void nwtriangle (int n){
//post: draws a triangle pointing north west
	int row; int col;
		for (row =   row < n; row ++){
				for (col = 0; col < n-row; col ++){
					print ("* ");
				}
				println ("");
		}
}

print ("What size of NWTriangle would you like? --> ");
nwtriangle (readInt());
