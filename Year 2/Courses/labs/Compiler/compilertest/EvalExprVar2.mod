MODULE  EvalExprVar2 ;
(* FROM BIO IMPORT PutInteger,PutLine; *)

VAR x,y : INTEGER;
B IN
 y := 10;
 x := ((1*y+2)*y+3)*y+4;
 PutInteger(x);
 PutLine();
END EvalExprVar2.
