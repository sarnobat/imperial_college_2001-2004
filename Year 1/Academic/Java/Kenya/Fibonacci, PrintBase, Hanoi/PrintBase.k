void printBase (int number, int base){
 if (number < base){
		putOneDigit (number);
 }
 else{
 int quotient; int remainder;
 quotient = number/base;
 remainder = number % base;
 printBase (quotient,base);
 putOneDigit (remainder);
 }
}

void putOneDigit (int digit){
 if (digit <10){
		print (digit);}
 else{
	switch (digit){
		case 10:	{print ("A");break;}
		case 11:	{print ("B");break;}
		case 12:	{print ("C");break;}
		case 13:	{print ("D");break;}
		case 14:	{print ("E");break;}
		case 15:	{print ("F");break;}
		default:	{println("No such number");}
  }
 }
}

print ("Enter the number which you wish to convert followed by the base to which you wish to convert ->");
printBase(readInt(),readInt());
