module Database where

type Name	= [Char]
type Sort	= [Char]
type Tins	= Int

type CatEntry 	= (Name, Sort, Tins)
data Database	= Empty | Node Database CatEntry Database
		  deriving (Eq, Ord, Show)

--=============================================================================

create :: Database
create	=  Empty

-------------------------------------------------------------------------------
add :: Name -> Sort -> Tins -> Database -> Database
add name sort tins Empty    	=  Node Empty (name,sort,tins) Empty
add name sort tins (Node l (n,s,t) r)
	|(name,sort) == (n,s)   = error "Cat already exists"
	|(name,sort) <  (n,s)   = Node (add name sort tins l) (n,s,t) r
	|(name,sort) >  (n,s)   = Node l (n,s,t) (add name sort tins r)

-------------------------------------------------------------------------------	
lookup_cat :: (Name,Sort) -> Database -> Tins
lookup_cat (name,sort) Empty = -1
lookup_cat (name,sort) ( Node l (n,s,t) r )
	|(name,sort) == (n,s)	= t
	|(name,sort) <	(n,s)	= lookup_cat (name,sort) l
	|(name,sort) >	(n,s)	= lookup_cat (name,sort) r

-------------------------------------------------------------------------------
count :: Database -> (Int,Int,Int)
count Empty    			= (0,0,0)
count db 			= (total_cats db,total_sorts db,total_tins db)
 	where
 	  total_cats :: Database -> Int
	  total_cats Empty		=  0
	  total_cats (Node l (n,s,t) r) =  1 + total_cats l + total_cats r 

	  total_sorts :: Database -> Int
	  total_sorts db  		=  length (duplicate_rem (list_sorts db))
  
	  list_sorts :: Database -> [Sort]
	  list_sorts Empty		=  []
	  list_sorts (Node l (n,s,t) r) =  list_sorts l ++ [s] ++ list_sorts r
  
	  duplicate_rem :: [Sort] -> [Sort]
	  duplicate_rem []		=  []
	  duplicate_rem (h:t)
  		|h `elem` t		=  duplicate_rem t
		|otherwise		=  h:(duplicate_rem t)

	  total_tins :: Database -> Int
	  total_tins Empty		=  0
	  total_tins (Node l (n,s,t) r) =  t + (total_tins l) + (total_tins r)

--------------------------------------------------------------------------------  
delete :: (Name,Sort) -> Database -> Database
delete (name,sort) Empty		=  Empty 
delete (name,sort) (Node l (n,s,t) r)
	|lookup_cat (name,sort) (Node l (n,s,t) r) == -1 = (Node l (n,s,t) r)	
	|(name,sort) < (n,s)	=  Node (delete (name,sort) l) (n,s,t) r
	|(name,sort) > (n,s)	=  Node l (n,s,t) (delete (name,sort) r)
	|otherwise		=  append l r
  where
   append :: Database -> Database -> Database
   append Empty db		=  db
   append (Node l (n,s,t) r) db =  Node l (n,s,t) (append r db)

-------------------------------------------------------------------------------
putdatabase :: Database -> [Char]
putdatabase Empty		=  ""
putdatabase (Node l (n,s,t) r)	=    (putdatabase l)
				  ++ (n ++ "\t" ++ s ++ "\t" ++ show t ++ "\n")
				  ++ (putdatabase r)
