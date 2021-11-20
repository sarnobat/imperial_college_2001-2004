import Plot
-- 
-- EXERCISE 3  (LSystems)
-- L-system rewrite rules are expressed as TABLES, rather than functions 
-- 
 
 
import Plot  
 
type Rule = ( Char, String )

type Rules = [ Rule ] 
 
type Pos = ( Float, Float ) 
 
type State = ( Pos, Float ) 
 
type Line = ( Pos, Pos ) 
 
-- 
-- Move distance is hard-coded (and somewhat arbitrary) 
-- 
dist :: Float  
dist = 10 
 
-- 
-- some test systems 
-- 
base  :: Int -> String 
rules  :: Int -> Rules 
angle :: Int -> Float 
 
angle 1 = 90 
angle 2 = 90 
angle 3 = 60 
angle 4 = 60 
  
base 1  = "M-M-M-M" 
base 2  = "-M" 
base 3  = "N" 
base 4  = "M" 
 
rules 1  = [ ( 'M', "M-M+M+MM-M-M+M" ), 
             ( '+', "+" ), 
             ( '-', "-" )  ] 
rules 2  = [ ( 'M', "M+M-M-M+M" ), 
             ( '+', "+" ), 
             ( '-', "-" )  ] 
rules 3  = [ ( 'M', "N+M+N" ), 
             ( 'N', "M-N-M" ), 
             ( '+', "+" ), 
             ( '-', "-" )  ] 
rules 4  = [ ( 'M', "M+N++N-M--MM-N+" ), 
             ( 'N', "-M+NN++N+M--M-N" ), 
             ( '+', "+" ), 
             ( '-', "-" )  ] 
mapper	 = [ ('M', "F"),
	     ('N', "F"),
	     ('-', "R"),
	     ('+', "L")     ]

--------------------------------------------------------------------------------
--------------------------------------------------------------------------------
 
lookupChar :: Char -> Rules -> String
--post: returns the corresponding string in the tuple
--	containing the Char to be looked up.
lookupChar a (h:t)
	|a == fst h = snd h
	|otherwise  = lookupChar a t

expandOne :: String -> Rules -> String
--post: returns a string construced by replacing each character
--	in the string given with the substring found by calling lookupChar
--	with the character and list of tuples
expandOne [] rule    = []
expandOne (h:t) rule = lookupChar h rule ++ expandOne t rule

expand :: String -> Rules -> Int -> String
--post: returns the string produced by applying expandOne a given 
--	number of times
expand base rule n
	|n == 0    = []
	|n == 1    = expandOne base rule
	|otherwise = expand ( expandOne base rule) rule (n-1)

move :: Char -> State -> Float -> State
-- pre: The Char represents a turtle command, either L, R or F.
--	The State represents the position of the turtle in (x,y)
--	co-ordinates and the turtle's angle of orientation
--	The Float represents the angular step used for turtle rotations.
-- post:returns a new State produced by either advancing a fixed distance
--	or rotating the turtle by a given angle.
move turtcom ((x,y),orientation) angstep
	|turtcom == 'L' = ((x,y) , (orientation + angstep))
	|turtcom == 'R' = ((x,y) , (orientation - angstep))
	|turtcom == 'F' = ((x + 10 * cos (orientation * pi/180),
			    y + 10 * sin (orientation * pi/180)), orientation )

trace :: String -> Float -> [Line]
-- pre: The string represents a sequence of turtle commands.
--	The Float is the anfular step used for turtle rotations.
-- post:returns a list of lines traced by the individual movements of the 
--	turtle. The turtle is assumed to start at the origin
trace turtcom angstep
	= tracehelp turtcom ((0,0),90)
	where
	  tracehelp :: String -> State -> [Line]
	  --pre: String is the same set of turtle commands as in parent function
	  --	 State is of format (current co-ordinates,current orientation)
	  --post:Returns a list with all the lines traced out
	  tracehelp [] state = []
	  tracehelp (h:t) (position,heading)
	  	|h == 'F'	=	(position,newpos):rest
		|otherwise	=	tracehelp t (newpos,newheading)
		where
		   (newpos,newheading) = move h (position,heading) angstep
		   rest		       = tracehelp t (newpos,newheading)

lSystem :: Int -> Int -> [Line]
--pre: Integers represent the the set of rules and the number of cycles
--post:returns a list of lines
lSystem a b = trace (expandOne (expand (base a) (rules a) b) mapper ) (angle a)
