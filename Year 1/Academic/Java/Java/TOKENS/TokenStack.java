package tokens;

public class TokenStack {
	
	private Token[] stack;
	static final int limit = 1000;
	public int top;
		
	public TokenStack() {
		
		top = -1;
		stack = new Token[limit];
	
	}
	
	public void push(Token t)
	{
		
		top++;
		stack[top] = t;
		
	}
	
	public void pop() throws StackException 
	{
		if (isEmpty())
		{
			throw new StackException ("StackException: cannot pop from an empty stack");
		}
		
		top--;
				
	}

	public Token top() throws StackException 
	{	
		if (isEmpty())
		{
			throw new StackException ("StackException: cannot read values from an empty stack");
		}
		
		return stack[top];
	}
	
	public boolean isEmpty() {
	
		return (top == -1);
		
	}

}