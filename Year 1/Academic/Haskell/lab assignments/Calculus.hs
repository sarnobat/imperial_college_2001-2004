data Expr  =	Num Float | Id [Char]
		| Add Expr Expr | Sub Expr Expr | Mul Expr Expr | Div Expr Expr
		deriving (Eq, Ord, Show)

type Environment  =  [([Char],Float)]

--------------------------------------------------------------------------------

reveal :: String -> Environment -> Float
--pre:arguments represent the variable name and the list specifying its value
--post:returns the value of the requested variable
reveal str []		=  error "No such string specified in environment"
reveal str (h:t)
	|str == fst h	=  snd h
	|otherwise	=  reveal str t

eval :: Expr -> Environment -> Float
--pre: expression is a number, variable, sum, difference, product or quotient of
--     sub-expressions 
--post:returns the resulting floating point number
eval (Num n) env	=  n
eval (Id e)  env	=  reveal e env
eval (Add e1 e2) env	=  (eval e1 env) + (eval e2 env)
eval (Sub e1 e2) env	=  (eval e1 env) - (eval e2 env)
eval (Mul e1 e2) env	=  (eval e1 env) * (eval e2 env)
eval (Div e1 e2) env	=  (eval e1 env) / (eval e2 env)

--------------------------------------------------------------------------------

differentiate :: Expr -> [Char] -> Expr
--pre:Expr is the is the expression whose differential is desired; 
--    [Char] specifies the name of the variable to differentiate with 
--    respect to
differentiate (Num n) dx	=	Num 0
differentiate (Id x) dx
	|x == dx		=	Num 1
	|otherwise		=	Num 0
differentiate (Add e1 e2) dx	=	Add (differentiate e1 dx)
					    (differentiate e2 dx)
differentiate (Sub e1 e2) dx	=	Sub (differentiate e1 dx)
					    (differentiate e2 dx)
differentiate (Mul e1 e2) dx	=	Add (Mul e2 (differentiate e1 dx))
					    (Mul e1 (differentiate e2 dx))
differentiate (Div e1 e2) dx	=	Div (Sub (Mul e2 (differentiate e1 dx))
					    (Mul e1 (differentiate e2 dx)))
					    (Mul e1 e2)
--------------------------------------------------------------------------------

putExpr :: Expr -> [Char]
--pre: argument represents the expression in Expr form
--post:returns the expression as a string using infix operator notation
putExpr (Num n)		=	show n
putExpr (Id x)		=	x
putExpr (Add e1 e2)	=	"("	     ++ 
				(putExpr e1) ++ 
				"+" 	     ++
				(putExpr e2) ++ 
				")"
putExpr (Sub e1 e2)	=	"("	     ++ 
				(putExpr e1) ++ 
				"-" 	     ++
				(putExpr e2) ++ 
				")"
putExpr (Mul e1 e2)	=	"("	     ++ 
				(putExpr e1) ++ 
				"*" 	     ++
				(putExpr e2) ++ 
				")"
putExpr (Div e1 e2)	=	"("	     ++ 
				(putExpr e1) ++ 
				")"	     ++
				"/" 	     ++
				"("	     ++
				(putExpr e2) ++ 
				")"

--------------------------------------------------------------------------------

derivatives:: Expr -> [Expr]
--post:argument is the expression whose derivatives are to be found
--post:returns a list of higher order derivatives
derivatives exp	= iterate (flip differentiate "x") exp

fact :: Float -> Float
--pre:Argument is a number
--post:Returns the factorial of that number
fact n	=	product [1 .. n ]

coeffs :: Float -> [Float]
--pre:Argument is an arbitrary value of the variable
--post:returns the MacLaurin coefficients to the higher order differentials
coeffs x = [1] ++ (zipWith (/) (map (x^) [1,2 .. ]) (map fact [1,2 .. ] ))

zerovals :: Expr -> [Float]
--pre:argument is the expression
--post:returns the value of the differentials whne the variable has value zero
zerovals e	=	map (flip eval [("x",0)]) (derivatives e)

series :: Expr -> Float -> [Float]
--pre:arguments represent the expression and the value of the variable
--post:returns a list of all the MacLurin series terms
series e x	=	zipWith (*) (coeffs x) (zerovals e)

maclaurin :: Expr -> Float -> [Float]
--pre:arguments are the expression and the value of the variable
--post:returns estimated values of the expression to a certain number of 
--     MacLaurin terms
maclaurin e x = scanl1 (+) (series e x)
