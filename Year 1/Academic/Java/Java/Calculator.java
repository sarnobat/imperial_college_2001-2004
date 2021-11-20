import tokens.*;

public class Calculator {
	
	public static void main (String[] args)
	{
		try
		{
			System.out.println("Please enter an expression");	
			
			TokenStack constants = new TokenStack();
			
			TokenStack operators = new TokenStack();		
						
			int answer = solveExpr(constants,operators);
			
			System.out.println(answer);
		}
		
		catch (CalculatorException c)
		{
			System.out.println("Check your symbols");
		}
		
		catch (StackException s)
		{
			System.out.println("cannot have consecutive symbols");
		}
	}
	
	public static int solveExpr(TokenStack constants, TokenStack operators)
	{
	
		TokenReader r = new TokenReader();
		Token t = new Token();
		t = r.readToken();
		
		while(t.tokenToChar() != '=' && t.tokenToChar() != ')')
		{
			if (t.isIntegerValued())
			{//i.e. if the current token is a number	
				constants.push(t);			
//System.out.println(t.tokenToInt());		
			}
			
			else
			{//i.e. if current token is an operator
				if (t.tokenToChar() == '(')
				{
					TokenStack tempN = new TokenStack();
					TokenStack tempO = new TokenStack();
					Token temp = new Token();
					
					int bracketed = solveExpr(tempN,tempO);
					
					temp.intToToken(bracketed);
					
					constants.push(temp);
				}
				
				else
				{
					if (operators.isEmpty() || 
						isTighterBinding(t.tokenToChar(),(operators.top()).tokenToChar())
						)
					{
						operators.push(t);
					}
					
					else
					{
						evaluate(constants,operators);
						operators.push(t);
					}
				}	
//System.out.println(t.tokenToChar());
				
			}
			
			t = r.readToken();
			
		}
				
		while (! operators.isEmpty() )
		{
			evaluate (constants,operators);
		}	
		
		return (constants.top ()).tokenToInt();
		
	}
	
		
	public static void evaluate (TokenStack numstack, TokenStack opstack) throws CalculatorException
	{
		
		/*TokenStack numstackT = new TokenStack();
		TokenStack opstackT = new TokenStack();
		
		numstackT = numstack;
		opstackT = opstack;
		
//System.out.println("1");
		Token num = numstackT.top(); 
		
		
		numstackT.pop();
		char opT = opstackT.top().tokenToChar();
		opstack.pop();
		
		
		
		if (opT == '-' && opstackT.isEmpty() && numstackT.isEmpty())
		{
//System.out.println("2");
			int n = (-1) * (num.tokenToInt());
			Token p = new Token();
			p.intToToken(n);
			numstackT.push(p);
			numstack = numstackT;
			return;
		}*/
		
		
		
		int num2 = (numstack.top()).tokenToInt();
		numstack.pop();
		int num1 = (numstack.top()).tokenToInt();
		numstack.pop();		
		char op = (opstack.top()).tokenToChar();
		opstack.pop();
		
		int ans = 0;
		
		switch(op){
			
			case '+' :	{ans = num1 + num2; break;}
			case '-' :	{ans = num1 - num2; break;}
			case '*' :	{ans = num1 * num2; break;}
			case '/' :	{ans = num1 / num2; break;}
			default	 :  {throw new CalculatorException ("not a valid symbol: " + op);}
			
		}
		
		Token answer = new Token();
		answer.intToToken(ans);
		numstack.push(answer);
		
	}
	
	public static boolean isTighterBinding(char op1,char op2)
	{		 
		 if ((op1  == '*' || op1  == '/') && (op2 == '+' || op2 == '-') || (op2 == '('))
		 {		 	
		 	return true;
		 }
		 		 
		 return false;
	}

/*	public static void printStacks(TokenStack nums,TokenStack ops)
	{
		System.out.println("-------------------------");
		
		while (! nums.isEmpty())
		{
			System.out.println((nums.top()).tokenToInt());
			nums.pop();
					
		}
		
		System.out.println("-------------------------");
	
		while (! ops.isEmpty())
		{
			System.out.println((ops.top()).tokenToChar());
			ops.pop();
					
		}
	
		System.out.println("-------------------------");
	
	}
*/
}