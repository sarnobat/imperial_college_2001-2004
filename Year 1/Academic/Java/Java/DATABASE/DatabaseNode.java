package database;

public class DatabaseNode
{
	private String name = new String();
	private int tins;
	
	private DatabaseNode left  = null;
	private DatabaseNode right = null;
	
	public DatabaseNode()
	{
	}
	
	public DatabaseNode(String n,int t)
	{
		tins = t;
		name = n;
	}
	
	public String getName()
	{
		return name;	
	}
	
	public int getTins()
	{
		return tins;
	}
	public void setTins(int t)
	{
		tins = t;
	}
	
	public DatabaseNode getLeft()
	{
		return left;
	}
	
	public DatabaseNode getRight()
	{
		return right;
	}
	
	public void setLeft(DatabaseNode l)
	{
		left = l;
	}
	
	public void setRight(DatabaseNode r)
	{
		right = r;
	}
			
	public void setItem(DatabaseNode n) // I think this should work, but not sure
	{
		name = n.getName();
		tins = n.getTins();
	}

	
}