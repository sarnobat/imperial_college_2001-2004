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
	
	public void add (String name, int tins)
	{
		name = Capitalize(name);
		addNode (name,tins,this);
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
		name = Capitalize(name);
		root = deleteItem(name,root);
	}
	
	public int lookup (String name)
	{
		return lookup_aux(name,root);
	}
	
	
	
	
	
	
	
	
	
	
	private DatabaseNode deleteItem (String s,DatabaseNode n)
	{		
		if (n == null)
		{// database has reached its end or is empty
			System.out.println(s + " not found. Database unchanged.");
		}
		
		else
		{
			if ( s.equals(n.getName()) )
			{// node to be deleted found
				totalcats = totalcats - 1;
				totaltins = totaltins - n.getTins();
				n = disconnect(n);
			}
			
			else if ( s.compareTo(n.getName()) < 0 )
			{//current node greater than the one to be deleted
				DatabaseNode newLeft = deleteItem ( s,n.getLeft() );
				n.setLeft(newLeft);
			}
			else
			{//current node smaller than the one to be deleted
				DatabaseNode newRight = deleteItem( s,n.getRight() );
				n.setRight(newRight);
			}
		}
		return n;
	}	
	private DatabaseNode disconnect(DatabaseNode n)
	{
		if (n.getLeft() == null && n.getRight() == null)
		{// n is a leaf
			n = null;
		}
		
		else if (n.getLeft() == null && n.getRight() != null)
		{// if n has only a right subtree
			n = n.getRight();
		}
		else if (n.getLeft() != null && n.getRight() == null)
		{// if n has only a left subtree
			n =  n.getLeft();
		}
		
		else
		{
			DatabaseNode replacementnode = findLeftMost(n.getRight());
			DatabaseNode newRight = deleteLeftMost(n.getRight());
			n.setItem(replacementnode);
			n.setRight(newRight);			
		}
		return n;
	}
	
	private DatabaseNode findLeftMost(DatabaseNode l)
	{
		if (l.getLeft() != null)
		{
			findLeftMost(l.getLeft());
		}
		return l;
	}
	
	private DatabaseNode deleteLeftMost(DatabaseNode l)
	{
		if (l.getLeft() == null)
		{
			l = null;
		}
		else
		{
			deleteLeftMost(l.getLeft());
		}
		return l;
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
	

	
	private void addNode (String name, int tins, Database db)
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
				setTotalTins(totaltins - db.getRoot().getTins());
			}

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
			{// if a node is to be overwritten
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
					addNode(name,tins,dbtempR);

				}

			}
		}
	}

}
