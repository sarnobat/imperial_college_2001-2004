fact :: Int-> Int
fact a
	|a == 0 = 1
	|otherwise = a*fact (a-1)