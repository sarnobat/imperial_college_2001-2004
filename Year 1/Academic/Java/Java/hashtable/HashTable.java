package hashtable;
import java.io.*;	

public class HashTable implements HashTableInterface
{
	private final int SIZE = 13;
	private HashTableNode[] ht;
	private int methusage;
	private int nodeops;

	public HashTable()
	{
		ht = new HashTableNode[SIZE];
		for (int i = 0; i < SIZE ;i++)
		{
			ht[i] = null;
		}
		methusage = 0;
		nodeops = 0;
	}

	public void getWords (String file)
	{
		try
		{
			FileReader f = new FileReader(file);
			BufferedReader b = new BufferedReader(f);

			String newWord = b.readLine();

			while (newWord != null)
			{
				addWord(newWord);
				newWord = b.readLine();
			}
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Please specify the correct file and path");
		}
		catch (IOException e)
		{
			System.out.println("Input/Output error");
		}
	}

	public void addWord (String word)
	{
		String lword = word.toLowerCase();
		int firstCharIndex = charIndex( lword.charAt(0) );
		ht[firstCharIndex] = addWord_aux( lword , ht[firstCharIndex] );
		methusage++;

	}

	public boolean isWordPresent (String word)
	{
		String lword = word.toLowerCase();
		int firstCharIndex = charIndex( lword.charAt(0) );
		boolean present = isWordPresent_aux( lword , ht[firstCharIndex] );
		methusage++;
		return present;
	}
	
	public int howManyUsages()
	{
		return methusage;
	}
	
	
	public int howManyInternalNodeOperations()
	{
		return nodeops;
	}
	
	
	private HashTableNode addWord_aux(String word,HashTableNode n)
	{//pre: s is lower case
	
		nodeops++;		
		HashTableNode updatedTree = null;

		if (n == null)
		{
			updatedTree = new HashTableNode(word);
			
		}
		
		else
		{
			if( (n.getWord()).compareTo(word) > 0 )
			{//inserting new item item into left subtree
				n.setLeft( addWord_aux( word , n.getLeft() ) ) ;
				updatedTree = n;
				nodeops++;
			}
			else if ( n.getWord().equals(word) )
			{//do not make any changes if word exists
				
			}
			
			else
			{//inserting new item into right subtree
				n.setRight( addWord_aux( word , n.getRight() ) );
				updatedTree = n;
				nodeops++;
			}
		}
		
		return updatedTree;
	}	
	
	private boolean isWordPresent_aux(String s,HashTableNode n)
	{//pre: s is lower case
	
		boolean present = false;
			
		if (n != null)
		{			
			if ( (n.getWord()).equals(s) )
			{
				present =  true;
			}
			
			else
			{
				if ( (n.getWord()).compareTo(s) > 0 )
				{
					present = isWordPresent_aux(s,n.getLeft());
					nodeops++;
					
				}
				else if ( (n.getWord()).compareTo(s) < 0 )
				{
					present = isWordPresent_aux(s,n.getRight());
					nodeops++;
				}
			}
		}
		
		return present;
	}
	
	private int charIndex(char c)
	{
		int returnIndex = -1;
		
		switch(c)
		{
			case 'a':	{returnIndex = 0;break;}
			case 'b':	{returnIndex = 0;break;}
			case 'c':	{returnIndex = 1;break;}
			case 'd':	{returnIndex = 1;break;}
			case 'e':	{returnIndex = 2;break;}
			case 'f':	{returnIndex = 2;break;}
			case 'g':	{returnIndex = 3;break;}
			case 'h':	{returnIndex = 3;break;}
			case 'i':	{returnIndex = 4;break;}
			case 'j':	{returnIndex = 4;break;}
			case 'k':	{returnIndex = 5;break;}
			case 'l':	{returnIndex = 5;break;}
			case 'm':	{returnIndex = 6;break;}
			case 'n':	{returnIndex = 6;break;}
			case 'o':	{returnIndex = 7;break;}
			case 'p':	{returnIndex = 7;break;}
			case 'q':	{returnIndex = 8;break;}
			case 'r':	{returnIndex = 8;break;}
			case 's':	{returnIndex = 9;break;}
			case 't':	{returnIndex = 9;break;}
			case 'u':	{returnIndex = 10;break;}
			case 'v':	{returnIndex = 10;break;}
			case 'w':	{returnIndex = 11;break;}
			case 'x':	{returnIndex = 11;break;}
			case 'y':	{returnIndex = 11;break;}
			case 'z':	{returnIndex = 11;break;}
			default:	{returnIndex = 12;break;}
		}
		
		return returnIndex;
	
	}
		
	
}
