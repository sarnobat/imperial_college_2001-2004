package hashtable;

public class HashTableNode
{// this class composes a tree struc re

	private String word;
	private HashTableNode left;
	private HashTableNode right;
	
	public HashTableNode(String s)
	{
		word = s;
		left = null;
		right = null;
	}

	public String getWord()
	{
		return word;
	}
	
	public HashTableNode getLeft()
	{
		return left;
	}
	
	public HashTableNode getRight()
	{
		return right;
	}
	
	public void setLeft(HashTableNode n)
	{
		left = n;
	}
	
	public void setRight(HashTableNode n)
	{
		right = n;
	}
}