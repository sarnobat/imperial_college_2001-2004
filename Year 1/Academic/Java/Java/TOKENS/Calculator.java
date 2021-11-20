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
			System.out.println("Invalid symbol or divide by zero error");
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
			case '/' :	{
						if (num2 == 0)
						{
							throw new CalculatorException ("Cannot divide by Zero");

						}
						else
						{
							ans = num1 / num2; break;
						}
					}
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
}
