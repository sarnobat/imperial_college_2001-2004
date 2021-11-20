package database;

public class Database implements DatabaseInterface
{
	private DatabaseNode root = null;
	private int totalcats;
	private int totaltins;
	
	public Database()
	{
		totalcats = 0;
		totaltins = 0;
	}
	
	public boolean IsIn()
	{
		return true;
	}
	
	public void add (String name, int tins)
	{
		name = Capitalize(name);
		addNode (name,tins,this);
	}

	private void addNode (String name, int tins, Database db) // THIS METHOD FAILS TO GET RID OF DUPLICATES
	{		
		if (db.getRoot() == null)
		{//if database is empty
			DatabaseNode tempnode = new DatabaseNode(name,tins);
			db.setRoot(tempnode);
			db.setTotalCats(db.countCats() + 1);
			db.setTotalTins(db.countTins() + tins);
		}
		
		else
		{
			if ( (db.getRoot().getName()).equals(name) )
			{
				System.out.println("(Note : previous " + name + " record has been overwritten.)");
				totalcats = totalcats - 1;
//DIAGNOSTIC: System.out.println("Totalcats decremented. New total = " + totalcats);
				setTotalTins(totaltins - db.getRoot().getTins());
			}


		//else
		//{
			//create a new node with the required info
			DatabaseNode newNode = new DatabaseNode(name,tins);

			
			if (name.compareTo(db.getRoot().getName()) < 0)
			{//if the name is alphabetically greater than the that of the current node

				if (db.getRoot().getLeft() == null)
				{//if the left node on the lower level is not in use
					db.getRoot().setLeft(newNode);
					totalcats = totalcats + 1;
					totaltins = totaltins + tins;
				}
				else
				{//left reference is already in use
					Database dbtempL = new Database();
					dbtempL.setRoot(db.getRoot().getLeft());
					addNode(name,tins,dbtempL);
				}
			}
			else if (name.equals(db.getRoot().getName()))
			{
				db.getRoot().setTins(tins);
				totalcats = totalcats + 1;
				totaltins = totaltins + tins;
			}

			else
			{//if the name is alphabetically greater than the that of the current node
				if (db.getRoot().getRight() == null)
				{//if the right node on the lower level is not in use
					db.getRoot().setRight(newNode);
					totalcats = totalcats + 1;
					totaltins = totaltins + tins;

				}
				else
				{//right reference is already in use
					Database dbtempR = new Database();
					dbtempR.setRoot(db.getRoot().getRight());
//String temp = new String();
//temp = (dbtempR.getRoot()).getName();
// DIAGNOSTIC: System.out.println("addNode (" + name + ","+ tins + "," + temp + ")");
					addNode(name,tins,dbtempR);

				}

			}
		}
	}

	private String Capitalize(String s)
	{//to convert any names to capital first letter
		return (s.substring(0,1)).toUpperCase() + s.substring(1,s.length());
	}

	private DatabaseNode getRoot()
	{
		return root;
	}

	private void setRoot(DatabaseNode n)
	{
		root = n;
	}


	public int lookup (String name)
	{
		return lookup_aux(name,root);
	}
	
	private int lookup_aux(String s, DatabaseNode n)
	{
		if (n == null)
		{//if you've come to the end of the tree
			return -1;
		}
		else if (s.equals(n.getName()))
		{//if the current node matches the one you're searching for
			return n.getTins();
		}
		else if ( s.compareTo( n.getName() ) > 0)
		{//if the node you're searching for is greater than the current node
			return lookup_aux(s,n.getRight());
		}
		else
		{//if the node you're searching for is smaller than the current node
			return lookup_aux(s,n.getLeft());
		}
			
	}
	
	private boolean IsIn(String s, DatabaseNode n)
	{
		if (s.equals(n.getName()))
		{
			return true;
		}
		else
		{
			if (n.getLeft() != null)
			{
				IsIn(s,n.getLeft());
			}
			
			if (n.getRight() != null)
			{
				IsIn(s,n.getRight());
			}
		}
		
		return false;
	
	}
	
	public int countCats()
	{
		return totalcats;
	}
	public void setTotalCats(int c)
	{
		totalcats = c;
	}
	
	public int countTins()
	{
		return totaltins;
	}
	public void setTotalTins(int t)
	{
		totaltins = t;
	}
	
	public void delete (String name)
	{
		
	}
	
	public void printDatabase()
	{
		if (root == null)
		{
			System.out.println("Database is empty");
		}
		else
		{
			printDatabase_aux(root);
		}
	}

	private void printDatabase_aux(DatabaseNode n)
	{// n is the starting node
		if (n.getLeft() != null)
		{
			printDatabase_aux(n.getLeft());
		}

		System.out.println(n.getName() + "\t" + n.getTins());

		if (n.getRight() != null)
		{
			printDatabase_aux(n.getRight());
		}
	}
}
