void hanoi (int n) {
//post: solves the hanoi puzzle for n disks
	if (n==0) 
  {
    return;
	}
	else 
	{
		move (n,'A','B','C');
	}				
}

void move (int n,char source,char via,char dest)
{
 //post:n is the number of disks to be moved; p, q and r
 //are names of the pegs (r plays the role of spare)
	if (n==0)
	{
		return;
	}
	else 
	{
		move (n-1,source,dest,via);
		translate (n,source,dest);
		move (n-1,via,source,dest);
	}
}

void translate (int d,char source,char destination)
{
	//post:prints the instruction corresponding to the disk 
	//number, the source and destination peg into laypersons 
	//language
	println ("translate disk " + d + " from " + source + " to " + destination);
}

println ("Please enter the number of disks in your Hanoi tower -> ");
hanoi (readInt());
