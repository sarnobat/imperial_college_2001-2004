void swtriangle (int n){
//post: draws a triangle pointing south west
	int row; int col;
		for (row =   row < n; row ++){
				for (col = 0; col < row+1; col ++){
					print ("* ");
				}
				println ("");
		}
}

print ("What size of SWTriangle would you like? --> ");
swtriangle (readInt());
