------------------EUCLID'S ALGORITHM-------------------------------

divisor :: Int -> Int -> Int
--pre: arguments represent numbers for which GCD is to be found; b and a must be
--positive
--post: returns the GCD of the 2 numbers
divisor a b 
	      | b-a == 0 = a
	      | b-a > 0 = divisor (b-a) a
	      | a-b > 0 = divisor (a-b) b

-------------------RUNGE KUTTA ALGORITHM----------------------------

f :: Float -> Float -> Float
--pre: arguments represent x and y in differential equation
--post: returns the value of dy/dx
f x y = x*y

nextY :: Float -> Float -> Float ->Float
--pre: arguments represent xn, yn and dx
--post: returns the value of y(n+1)
nextY xn yn dx = yn + 1/6*(k1 + 2*k2 + 2*k3 + k4)
    where 
	k1 = f xn yn *dx;
	k2 = f (xn + 1/2*dx) (yn + 1/2*k1) *dx;
	k3 = f (xn + 1/2*dx) (yn + 1/2*k2) *dx;
	k4 = f (xn + dx) (yn + k3) *dx

solve :: Float -> Float -> Float -> Float -> Float
--pre: arguments represent x, xn, yn and dx
--post:returns the estimate of y at the given value of x
solve x xn yn dx
	| abs (x-xn) < 0.00001 = yn
	| otherwise = solve x (xn+dx) ( nextY xn yn dx) dx
	
errorY :: Float -> Float -> (Float,Float,Float)
errorY x dx = (approxY,exactY, abs (approxY - exactY))
	where approxY = solve x 0 1 dx
	      exactY  = exp((x^2)/2)

----------------------GENERATING PRIME NUMBERS-----------------

is_prime :: Int ->Int -> Bool
--pre: arguments represent the number and the factor in test (Note: 2nd argument
--must be 2 for this to be a test for prime numbers)
--post: returns true or false
is_prime x y
	|x <= 1 = False
	|x == y = True
	|x `mod` y == 0 = False
	|otherwise = is_prime x (y+1)

next_prime :: Int -> Int
--pre: argument represents current number
--post: returns next prime number
next_prime n
	|is_prime (n+1) 2 = n + 1
	|otherwise = next_prime (n+1)
	where
		is_prime :: Int ->Int -> Bool
		--pre: arguments represent the number and the factor in test (Note: 2nd argument
		--must be 2 for this to be a test for prime numbers)
		--post: returns true or false
		is_prime x y
			|x <= 1 = False
			|x == y = True
			|x `mod` y == 0 = False
			|otherwise = is_prime x (y+1)
