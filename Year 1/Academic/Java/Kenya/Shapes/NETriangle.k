void nwtriangle (int n){
//post: draws a triangle pointing north east
	int row; int col;
		for (row =   row < n; row ++){
					for (col = 0; col < row; col ++){
					print ("  ");
					}					
					for (col = 0; col < n-row; col ++){
					print ("* ");
					}
					println ("");
		}
}

print ("What size of NETriangle would you like? --> ");
nwtriangle (readInt());
