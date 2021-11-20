newbase :: Int -> Int -> Int
newbase x b
	|x < b = x
	|otherwise =  x `mod` b + 10*( newbase (x `div` b) b)
