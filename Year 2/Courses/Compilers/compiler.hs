type Name  = [Char]
type Label = Int

data Prog = Program [Decl] [Stat]

data Typedef = Integer |
               Array Int

data Decl = Declare Name Typedef

data Stat = Assign Lhs Expr |
            For Name Expr Expr [Stat]

data Lhs = Var Name |
           Arr Name Expr

data Expr = Const Int |
            Binop Binop Expr Expr |
            Ref Lhs

data Binop = Plus | Minus | Times | Divide | Max | Min

data Instruction =
                Data | Text |
                Comm [Char] Int |
                Label [Char] |
                Bra [Char] |
                Blt [Char] |
                Bgt [Char] |
                Mov Operand Operand |
                Add Operand Operand |
                Sub Operand Operand |
                Mul Operand Operand |
                Div Operand Operand |
                Cmp Operand Operand |
                Halt
     deriving Show

data Operand = Reg Register |
               Ind Register |
               Push | Pop |
               Abs [Char] |
               ImmNum Int |
               ImmName [Char]
     deriving (Eq, Show)


data Register = D Int | A7
     deriving (Eq, Ord)
instance Show Register where
  showsPrec p (D n) = showString "D" . shows n
  showsPrec p A7 = showString "A7"

--=======================================================================

translateDecls :: [Decl] -> [Instruction]
translateDecls ds =
	[Data] ++
	map transDec ds ++
	[Text]

transDec :: Decl -> Instruction
transDec  (Declare k Integer) =
	Comm k 4
transDec (Declare a (Array i)) =
	Comm a (4*i)
--=======================================================================

translateStats :: [Stat] -> [Register] -> Label -> ([Instruction], Label)
translateStats [] rs l = ([],l)
translateStats ss (r:[]) l = (is,1)
	where
		(is,l2) = translateStatsStack ss r l2
--if only 1 register available, take this path immediately. No other pattern
--match should be attempted
translateStats ( (Assign (Var v) e):rest ) (r:rs) l =
	(	transExp e (r:rs) ++
		[Mov (Reg r) (Abs v)]
		++
		restInstr
				, l)
	where
		(restInstr,l2) = translateStats rest (r:rs) l2
translateStats ( (Assign (Arr a i) e ):rest ) (loc:val:rs) l =
	(	( transExp i (loc:val:rs) )
		++
		[	( Mul (ImmNum 4) (Reg loc) ),
			( Add (ImmName a) (Reg loc) )	]
		++
		( transExp e (val:rs) )
		++
		[Mov (Reg val) (Ind loc)]
		++
		restInstr		,1)
	where
		(restInstr,l2) = translateStats rest (loc:val:rs) l2

translateStats ( (For v first last body):rest ) (r1:r2:rs) l =
--It would seem obvious to store the loop control variable in a register,
--but bearing in mind the exercise task which follows, this enhancement will not be used here
	(	evalFirst
		++
		evalLast
		++
		cond
		++
		main
		++
		[Add (ImmNum 1) (Abs v),Bra (addL labelOne)]
		++
		[Label (addL labelTwo)]
		++
		remaining	,	l)

	where
		(evalFirst,l1) =
			translateStats [(Assign (Var v) first)] (r1:r2:rs) l1
		--r1 is NOT resposible for storing the value of v throughout the loop
		--(see q. 2 for this)
		evalLast = transExp last (r1:r2:rs)
		cond = setLabel ([Cmp (Abs v) (Reg r1),Bgt (addL labelTwo)],labelOne)
		main = setLabel (translateStats body (r2:rs) labelTwo)
		(remaining,l3) = translateStats rest (r1:r2:rs) l3

		labelOne = 1 -- REALLY labelOne = newlabel
		labelTwo = 2 -- REALLY labelTwo = newlabel
--=======================================================================

translateStatsStack :: [Stat] -> Register -> Label -> ([Instruction], Label)
--register only for scratch work
translateStatsStack [] r l = ([],l)
translateStatsStack ( (Assign (Var v) e):rest ) r l  =
	(
		transExpStack e r
		++
		[Mov (Pop) (Abs v)]
		++
		restInstr
			,l)
	where
		(restInstr,l2) = translateStatsStack rest r l2



translateStatsStack ( (Assign (Arr a i) e ):rest ) r l =
--because it is the address i rather than value e that needs to be stored in
--a register (temporarily) to enable indirect addressing, the order of evaluation
--is the opposite to before
	(	transExpStack e r
		++
		transExpStack i r
		++
		[Mul (ImmNum 4) (Ind A7),
		Add (ImmName a) (Ind A7)]
		++
		[Mov (Pop) (Reg r),
		Mov (Ind r) (Ind A7)]
		++
		restInstr		,1)
	where
		(restInstr,l2) = translateStatsStack rest r l2

translateStatsStack ( (For v first last body):rest ) r l =
	(	evalFirst
		++
		evalLast
		++
		cond
		++
		main
		++
		[Add (ImmNum 1) (Abs v),Bra (addL labelFour)]
		++
		[Label (addL labelThree)]
		++
		remaining	,	l)

	where
		(evalFirst,l1) =
			translateStatsStack [(Assign (Var v) first)] r l1
		--loop control variable left in "v"
		evalLast = transExpStack last r
		--boundary value on top of stack
		cond = setLabel ([Cmp (Abs v) (Ind A7),Bgt (addL labelThree)],labelFour)
		main = setLabel (translateStatsStack body r labelFive)
		(remaining,l2) = translateStatsStack rest r l2

		labelThree = 3 --REALLY labelThree = newlabel
		labelFour = 4 --REALLY labelFour = newlabel
		labelFive = 5 --REALLY labelFour = newlabel

--=======================================================================

addL :: Int -> [Char]
addL i = "L" ++ (show i)

setLabel :: ([Instruction], Label) -> [Instruction]
setLabel (is,l) = [Label (addL l) ] ++ is

--=======================================================================

transExp :: Expr -> [Register] -> [Instruction]
transExp (Const c) (r:rs) =
	[Mov (ImmNum c) (Reg r)]
transExp (Binop op e1 e2) (r1:[]) =
--result WILL be left in register r1
	transExpStack e1 r1 ++
	transExpStack e2 r1 ++
	transOpStack op r1 ++
	[Mov (Pop) (Reg r1)]
transExp (Binop op e1 e2) (r1:r2:rs) =
	if (weight e1) > (weight e2) then
		transExp e1 (r1:r2:rs) ++
		transExp e2 (r2:rs) ++
		transOp op r1 r2
	else
		(transExp e2 (r2:r1:rs)) ++
		(transExp e1 (r1:rs)) ++
		(transOp op r1 r2)

transExp (Ref (Var v)) (r:rs) =
	[Mov (Abs v) (Reg r)]
transExp (Ref (Arr a i)) (r:rs) =
	transExp i (r:rs)
	++
	[Mul (ImmNum 4) (Reg r),
	Add (ImmName a) (Reg r),
	Mov (Ind r) (Reg r)]

--=======================================================================

transExpStack :: Expr -> Register -> [Instruction]
--post: result is left at the top of the stack.
--Register only for rough working. The result doesn't need to be stored here.
transExpStack (Const c) r = [Mov (ImmNum c) (Push)]
transExpStack (Binop op e1 e2) r =
--though the need to use the weight function isn't as great as with registers,
--a large program will take up a significant extra portion of memory if it isn't used.
	if (weight e1) > (weight e2) then
		transExpStack e1 r ++
		transExpStack e2 r ++
		transOpStack op r
	else
		transExpStack e2 r ++
		transExpStack e1 r ++
		transOpStack op r

transExpStack (Ref (Var v)) r =
	[Mov (Abs v) (Push)]
transExpStack (Ref (Arr a i)) r =
	transExpStack i r
	++
	[Mov (Pop) (Reg r),
	Mul (ImmNum 4) (Reg r),
	Add (ImmName a) (Reg r),
	Mov (Reg r) (Push)]

--=======================================================================

weight :: Expr -> Int
weight (Const n) = 1
weight (Ref (Var v)) 	= 1
weight (Ref (Arr a i))	= 1 + (weight i)
weight (Binop o e1 e2) =
	min c1 c2
		where
			c1 = max (weight e1) ((weight e2)+1)
			c2 = max ((weight e1)+1) (weight e2)

--=======================================================================

transOpStack :: Binop -> Register -> [Instruction]
transOpStack Plus r =
	[Mov (Pop) (Reg r),
	Add (Reg r) (Ind A7)]
transOpStack Minus r =
	[Mov (Pop) (Reg r),
	Sub (Reg r) (Ind A7)]
transOpStack Times r =
	[Mov (Pop) (Reg r),
	Mul (Reg r) (Ind A7)]
transOpStack Plus r =
	[Mov (Pop) (Reg r),
	Div (Reg r) (Ind A7)]
transOpStack Max r =
	[Mov (Pop) (Reg r),
	Cmp (Ind A7) (Reg r),
	Bgt (addL labelSix),
	Mov (Reg r) (Ind A7),
	Label (addL labelSix)]

	where
		labelSix = 6 --REALLY end = newlabel
transOpStack Min r =
	[Mov (Pop) (Reg r),
	Cmp (Ind A7) (Reg r),
	Blt (addL labelSeven),
	Mov (Reg r) (Ind A7),
	Label (addL labelSeven)]

	where
		labelSeven = 7 -- REALLY end = newlabel

--=======================================================================

transOp :: Binop -> Register -> Register -> [Instruction]
--assume r1 will hold the result of the operation
transOp Plus r1 r2 	=	[Add (Reg r2) (Reg r1)]
transOp Minus r1 r2	=	[Sub (Reg r2) (Reg r1)]
transOp Times r1 r2 	=	[Mul (Reg r2) (Reg r1)]
transOp Divide r1 r2	=	[Div (Reg r2) (Reg r1)]
transOp Max r1 r2	=	[	Cmp (Reg r1) (Reg r2),
					Bgt (addL labelEight),
					Mov (Reg r2) (Reg r1),
					Label (addL labelEight)	]

				where
					labelEight = 8 --REALLY labelEight = newlabel
transOp Min r1 r2	=	[	Cmp (Reg r1) (Reg r2),
					Blt (addL labelNine),
					Mov (Reg r2) (Reg r1),
					Label (addL labelNine)	]

					where
					labelNine = 9 --REALLY labelNine = newlabel



--=======================================================================

genCode :: Prog -> [Instruction]
genCode (Program decls statements) = v ++ s
    where v = translateDecls decls
          (s,l) = translateStats statements availableRegs 1
          availableRegs = map D [0..7]



--=======================================================================

print_prog :: [Instruction] -> [Char]
-- pre  : [Instruction] is a valid list of 68000 assembler instructions.
-- post : Function returns an ASCII representation of the assembler.
-- use  : To make the output of the system a little more readable.
print_prog []                     = []
print_prog ((Data):t)             = ".data\n" ++ print_prog t
print_prog ((Text):t)             = ".text\n" ++ print_prog t
print_prog ((Comm x n):t)         = ".comm " ++ x ++ ", " ++ (show n) ++ "\n" ++ (print_prog t)
print_prog ((Label x):t)          = "\n" ++ x ++ ":\n" ++ (print_prog t)
print_prog ((Mov (ImmNum 0) b):t) = "clr.l  " ++ (print_operand b) ++ "\n" ++ (print_prog t)
print_prog ((Mov x x'):t) | x==x' = (print_prog t)
print_prog ((Mov a b):t)          = "move.l " ++ (print_operand a) ++ ", " ++ (print_operand b) ++ "\n" ++ (print_prog t)
print_prog ((Add a b):t)          = "add.l  " ++ (print_operand a) ++ ", " ++ (print_operand b) ++ "\n" ++ (print_prog t)
print_prog ((Sub a b):t)          = "sub.l  " ++ (print_operand a) ++ ", " ++ (print_operand b) ++ "\n" ++ (print_prog t)
print_prog ((Mul a b):t)          = "mul.l  " ++ (print_operand a) ++ ", " ++ (print_operand b) ++ "\n" ++ (print_prog t)
print_prog ((Div a b):t)          = "div.l  " ++ (print_operand a) ++ ", " ++ (print_operand b) ++ "\n" ++ (print_prog t)
print_prog ((Cmp a b):t)          = "cmp.l  " ++ (print_operand a) ++ ", " ++ (print_operand b) ++ "\n" ++ (print_prog t)
print_prog ((Bgt x):t)            = "bgt    " ++ x ++ "\n" ++ (print_prog t)
print_prog ((Blt x):t)            = "blt    " ++ x ++ "\n" ++ (print_prog t)
print_prog ((Bra x):t)            = "bra    " ++ x ++ "\n" ++ (print_prog t)

print_operand :: Operand -> [Char]
print_operand (ImmNum n)  = "#" ++ (show n)
print_operand (ImmName x) = "#" ++ x
print_operand (Reg r)     = show r
print_operand (Push)      = "-(A7)"
print_operand (Pop)       = "(A7)+"
print_operand (Abs x)     = x
print_operand (Ind r)     = "(" ++ (show r) ++ ")"

printCode :: Prog -> [Char]
-- pre  : The program is syntactically correct.
-- post : Compiles the program into 68000 assembler, and displays assembler listing.
-- use  : This is how to run your compiler.
--        Declare your program and compile with "printIntermediateCode <name>"
--        Test the routines by just having (say) one or two registers.

printCode prog = print_prog (genCode prog)




--
codetest :: Int -> IO ()

codetest 1 = putStr (printCode test1)
codetest 2 = putStr (printCode test2)
codetest 3 = putStr (printCode test3)
codetest 4 = putStr (printCode test4)
codetest 5 = putStr (printCode test5)
codetest 6 = putStr (printCode test6)


-- Various contrived example programs to test system
-- Call with "codetest 1", "codetest 2" etc.

-- Program "test1"
-- Tests the bare bones of the system

-- Program
--   declare a : Integer;
--   declare b : Integer;
-- begin
--   a := 7;
--   b := a + 4;
-- end

test1 = (Program [(Declare "a" Integer),(Declare "b" Integer)] [(Assign (Var "a") (Const 7)), (Assign (Var "b") (Binop Plus (Ref (Var "a")) (Const 4)))])

-- Program "test2"
-- Tests label generation

-- Program
--   declare x : Integer;
--   declare y : Integer;
-- begin
--   x := 3;
--   y := 7 Max x
-- end

test2 = (Program [(Declare "x" Integer),(Declare "y" Integer)] [(Assign (Var "x") (Const 3)), (Assign (Var "y") (Binop Max (Const 7) (Ref (Var "x"))))])

-- Program "test3"
-- Factorial program.
-- Result returned is 10!. Alter 10 to desired value.
-- Tests loops (with register allocation) and Mul.

-- Program
--   declare x : Integer
--   declare a : Integer
-- begin
--   a := 1
--   For x = 1 TO 10
--     a := a Times x
--   end
-- end

test3 = (Program [  (Declare "x" Integer),
	            (Declare "a" Integer)]
                 [(Assign (Var "a") (Const 1)),
                  (For "x" (Const 1) (Const 10)
                       [(Assign (Var "a") (Binop Times (Ref (Var "a")) (Ref (Var "x"))))]
        )])

-- Program "test4"
-- Tests weights functions.
-- Can also test no registers condition by changing definition of genCode.

-- Program
--   declare x : Integer
-- begin
--   x := 1 - (2 - (3 - 4))
-- end

test4 = (Program [(Declare "x" Integer)] [(Assign (Var "x") (Binop Minus (Const 1) (Binop Minus (Const 2) (Binop Minus (Const 3) (Const 4)))))])

-- Program "test5"
-- Bubble sort program given in notes
-- Tests pretty much everything :-)

test5 = (Program
          [(Declare "A" (Array 100)),
           (Declare "i" Integer),
           (Declare "j" Integer),
           (Declare "k" Integer),
           (Declare "t" Integer)]
          [(For "i" (Const 0) (Const 98)
            [(For "j" (Const 0) (Binop Minus (Const 98) (Ref (Var "i")))
              [
               (Assign (Var "t") (Binop Max (Ref (Arr "A" (Ref (Var "j")))) (Ref (Arr "A" (Binop Plus (Ref (Var "j")) (Const 1)))))),
               (Assign (Arr "A" (Binop Plus (Ref (Var "j")) (Const 1))) (Binop Min (Ref (Arr "A" (Ref (Var "j")))) (Ref (Arr "A" (Binop Plus (Ref (Var "j")) (Const 1)))))),
               (Assign (Arr "A" (Ref (Var "j"))) (Ref (Var "t")))
              ]
            )]
          )])

-- Program "test6"
-- Graph colouring example from tutorial
--         S1:   A = 100
--         S2:   B = 200
--         S3:   C = A + B
--         S4:   D = A * 2
--         S5:   E = B * 2
--         S6:   F = D - C
--         S7:   G = E + F

test6 = (Program
         [(Declare "A" Integer),
         (Declare "B" Integer),
         (Declare "C" Integer),
         (Declare "D" Integer),
         (Declare "E" Integer),
         (Declare "F" Integer),
         (Declare "G" Integer)]
         [Assign (Var "A") (Const 100),
          Assign (Var "B") (Const 200),
          Assign (Var "C") (Binop Plus  (Ref (Var "A")) (Ref (Var "B"))),
          Assign (Var "D") (Binop Times (Ref (Var "A")) (Const 2)),
          Assign (Var "E") (Binop Times (Ref (Var "B")) (Const 2)),
          Assign (Var "F") (Binop Minus (Ref (Var "D")) (Ref (Var "C"))),
          Assign (Var "G") (Binop Plus  (Ref (Var "E")) (Ref (Var "F")))
         ])

