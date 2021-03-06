data Colour = R | B | G deriving (Eq,Ord,Show)
type Colours = [Colour]
type Score = (Int,Int)
type Res% t = (Colours,Score)

colour :: Int -> Colour
colour x
	|x == 0 = R
	|x == 1 = B
	|x == 2 = G

base3:: Int -> Int -> [Int]
base3 x 0 = []
base3 x n
	|3^(n-1) > x = 0:(base3 x (n-1))
	|otherwise = (x `div` 3^(n-1)):(base3 (x `mod` 3^(n-1)) (n-1))
