remainder :: Int -> Int -> Int
remainder a b
	|a-b == 0 = 0
	|a-b < 0  = a
	|a-b>0 = remainder (a-#  b