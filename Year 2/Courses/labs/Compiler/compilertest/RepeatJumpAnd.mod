MODULE RepeatJumpAnd;
(* FROM BIO IMPORT PutInteger,PutLine; *)

VAR x : INTEGER;
BEGI  x:=-1;
 
 REPEAT
 x:=x+1;
 UNTIL x>0 AND 1 / x > 0;
 PutInteger(1234);
 PutLine();
END RepeatJumpAnd.
