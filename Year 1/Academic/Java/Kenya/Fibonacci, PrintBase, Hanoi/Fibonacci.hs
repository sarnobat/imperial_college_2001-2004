fibonacci :: Int -> Int -> Int -> Int
fibonacci a b n
	|n == 0		=	a
	|n == 1		=	b
	|otherwise	= 	fibonF ci b (b+a) (n-1)
