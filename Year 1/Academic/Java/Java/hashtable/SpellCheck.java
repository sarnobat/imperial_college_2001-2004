import hashtable.*;

import kenya.io.InputReader;


public class SpellCheck
{
	public static void main( String args[] )
	{
		HashTable h = new HashTable();
		h.getWords(args[0]);

		int counter = 0;// the purpose of this counter is purely for 
						// determining when to start a new line for printing
		
		String word = new String();
		
		while (! InputReader.isEOF())
		{
			word = InputReader.readString();
			
			if (counter%10 == 0)
			{
				System.out.println("");
			}

			if (h.isWordPresent(word))
			{
				System.out.print(word + " ");
			}
			else
			{
				System.out.print(">>>" + word + "<<< ");
			}
			counter++;
					
		}
		System.out.println("");
		
		System.out.println("");
		System.out.println("---------SUMMARY-----------");
		System.out.println(h.howManyUsages() + " uses of the Hash Table");
		System.out.println(h.howManyInternalNodeOperations() + " node operations");
		System.out.println( (double) h.howManyInternalNodeOperations() / (double) h.howManyUsages() + " node operations per table usage (average)");
		System.out.println("");
		
	}
		
	
}
