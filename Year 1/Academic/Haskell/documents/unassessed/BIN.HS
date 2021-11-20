binary :: Int -> Int
binary d
	|d<2 = d
	|otherwise =  d `mod` 2 + 10*(binary (d `div` 2))
