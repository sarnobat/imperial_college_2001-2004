twoSame :: [Int] -> Bool
twoSame [] = False
twoSame (h:t)
	|elem h t	=	True
	|otherwise	=	twoSame t

pos :: Int -> [Int] -> Int
--pre: assume x is in the list
pos x (h:t)
	|x == h = 0
	|otherwise = 1 + pos x t

transpose :: [Char] -> [Char] -> [Char] -> [Char]
transpose xs ys []	=	[]
transpose xs ys (h2:t2) = (xs!!(pos (ord h2) (map ord ys)))
				 :(transpose xs ys t2)

data Shape =  Triangle Float Float Float
	     | Square Float
	     | Circle Float
	     | Polygon [Vertex]
		deriving (Eq,Ord,Show)

type Vertex = (Float,Float)


area :: Shape -> Float
area (Triangle a b c) = sqrt (s*(s-a)*(s-b)*(s-c))
	where s = (a+b+c)/2
area (Square a)	= a*a
area (Circle r) = pi*r*r


type Date = (Day,Month,Year)
type Day = Int
type Month = Int
type Year = Int

age :: Date -> Date -> Int
age (dd,mm,yy) (d,m,y)
	|(m,d) >= (mm,dd) = y-yy
	|otherwise = y-yy-1

data Possibly a = Failure | Success a
	deriving (Eq,Ord,Show)

tableLookup :: Eq a => a -> [(a,b)] -> Possibly b
tableLookup item [] = Failure
tableLookup item ((x,y):t)
	|item == x = Success y
	|otherwise = tableLookup item t

type Database = [Ustaff]

type Ustaff = (Staffdata,Emptdata)

data Staffdata = Teaching Research [Course]| Support Supporttype
type Emptdata = (String,Sex,Date,Int)

data Research =  Systems | Software | Theory
type Course = Int
data Supporttype = Administrative | Technical
data Sex = M | F


--totalstaff :: Database -> Int
--totalstaff xs = length xs

--courseteacher :: Course -> Database -> String
--courseteacher n ((stafftype ):t)
--	|

--==============================================================================

notNull :: [a] -> Bool
notNull xs = not (null xs)

takeWhil :: (a-> Bool) -> [a] -> [a]
takeWhil p [] = []
takeWhil p (x:xs) = if p x then x:takeWhil p xs else []
