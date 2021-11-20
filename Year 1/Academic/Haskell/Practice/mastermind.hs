import List

data Colour = R | B | G
	deriving (Eq, Ord, Show)

type Colours = [Colour]
type Score = (Int,Int)
type Result = (Colours, Score)

colour :: Int -> Colour
--pre: int is 0,1 or 2
colour c
	|c == 0	= R
	|c == 1 = B
	|c == 2 = G

base3 :: Int -> Int -> [Int]
--pre:n is large enough to represent x in the list
base3 x 0 = []
base3 x n
	|p > x = 0:(base3 x (n-1))
	|otherwise   = (div x p):(base3 (x-p*(div x p)) (n-1))
	where p = 3^(n-1)

blacks :: Colours -> Colours -> Int
--pre:assume the given guess and secret code are of the correct format
blacks [] [] = 0
blacks (h1:t1) (h2:t2)
	|h1 == h2  = 1 + q
	|otherwise = q
	where q = blacks t1 t2

whites :: Colours -> Colours -> Int
whites g s = length g - length (g\\s) - blacks g s

score :: Colours -> Colours -> Result
score g s = (g,(blacks g s,whites g s))

buildlist :: Int -> [Int]
buildlist 0 = []
buildlist n = [0,1..(3^n-1)]

binlist :: Int -> [[Int]]
binlist n = map (flip base3 n)(buildlist n)

colourconv :: [Int] -> Colours
colourconv l = map colour l

allguesses :: Int -> [Colours]
allguesses n = map colourconv (binlist n)

consistent :: Colours -> [Result] -> Bool
--pre:
consistent g r = foldr1 (&&) (zipWith (==) resultingscores r)
	where resultingscores = map (flip score g) allguesses
	      allguesses = map fst r

strike :: [Colours] -> [Result] -> [Colours]
strike [] r = []
strike (h1:t1) r
	|consistent h1 r = h1:(strike t1 r)
	|otherwise	 =  strike t1 r
