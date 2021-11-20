quotient :: Int -> Int -> Int
quotient a b
	|a-b == 0 = 1
	|b-a>0 = 0
	|a-b>0 = 1+quotient (a-b) b