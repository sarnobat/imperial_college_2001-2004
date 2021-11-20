quad :: Int -> Int -> Int -> Int -> Int
--post: returns evaluated quadratic expression
quad a b c x = a*x^2 + b*x + c

quad_is_zero :: Int -> Int -> Int -> Int -> Bool
--post: returns true if a quadratic expression evaluates to zero.
quad_is_zero a b c x = quad a b c x == 0

quadratic_solver :: Float -> Float -> Float -> (Float,Float)
--pre: the arguments represent the co-efficients of a solvable quadratic
--equation
--post: gives the tow roots of a quadratic equation
quadratic_solver a b c = (((-b+d)/e),((-b-d)/e)) 
	where d= sqrt (b^2-4*a*c)
	      e= 2*a

real_roots :: Float -> Float -> Float -> Bool
--pre: the arguments represent the co-efficients of a quadratic equation
--post: returns True if the quadratic equation has real roots.
real_roots a b c = b^2-4*a*c >= 0

bigger :: Int -> Int -> Int
--pre: arguments represent the numbers in question
--post: returns the larger integer
bigger a b = if (a-b) > 0 then a
             		  else b

smaller :: Int -> Int -> Int
--pre: arguments represent the numbers in question
--post: returns the smaller integer
smaller a b = if (a-b) >0 then b
			  else a

biggest :: Int -> Int -> Int -> Int
--pre: arguments represent numbers in question
--post: returns the largest number
biggest a b c = bigger (bigger a b) c

is_digit :: Char -> Bool
--post: returns true if the character represents a digit '0'..'9'
is_digit a = ord a >= 48 && ord a <= 57

is_alpha :: Char -> Bool
--post: returns true if the character represnets an alphabetic
--character either in the range 'a'..'z' or in the range 'A'..'Z'
is_alpha a = (ord a >= 65
	   && ord a <= 90)
	  || (ord a >= 97 
	   && ord a <= 122)
 
digit_to_int :: Char -> Int
--pre: the given character represnts a digit.
--post: returns the integer [0..9] corresponding to the given character
digit_to_int a = ord a - ord '0'

to_upper_case :: Char -> Char
--pre : the argument represents the alphabetical character to be capitalized
--post: returns the corresponding upper case character
to_upper_case a
	|a >= 'A' && a <= 'Z' = a
	|a >= 'a' && a <= 'z' = chr (ord a -32)
