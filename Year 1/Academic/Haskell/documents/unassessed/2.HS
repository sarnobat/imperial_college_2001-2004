is_prime :: Int -> Bool
--pre: the argument is a non-negative integer
--post: returns True if the number is prime
is_prime n
 	|n `mod` 2 == 0 = False