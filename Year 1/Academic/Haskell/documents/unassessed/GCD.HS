remainder :: Int -> Int -> Int
remainder a b
        |a-b == 0 = 0
	|a-b < 0  = a
	|a-b>0 = remainder (a-b) b
	
findgcd :: Int -> Int -> Int
findgcd m n
	|remainder m n == 0 = n
	|otherwise = findgcd n (remainder m n)
	 prime :: Int -> Int -> Bool
rprime x y	
	|findgcd x y == 1 = True
	|otherwise = False