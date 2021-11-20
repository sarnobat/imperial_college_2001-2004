int Position;
int FirstValue;
int SecondValue;

int Fibonacci (int Position,int FirstValue, int SecondValue){
assert (Position >= 0): "cannot be a negative ordinal";
	if (Position == 0){
				return FirstValue;
	}
	if (Position == 1){
				return SecondValue;
	}
	else {
		return (Fibonacci (Position -1, SecondValue, 												SecondValue	+ FirstValue));
	}
}
print ("Please state the position of the required term followed by the first two terms of your Fibonacci sequence -> ");
Position = readInt(); FirstValue = readInt(); SecondValue = readInt(); 
println ("");
println ("The required term is " + Fibonacci (Position,FirstValue,SecondValue));
