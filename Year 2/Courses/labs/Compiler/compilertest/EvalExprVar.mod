MODULE  EvalExprVar;
(* FROM BIO IMPORT PutInteger,PutLine; *)

VAR x,y : INTEGER;
BEGIN
 x := 2;
 y := x*100+34;
 PutInteger(y);
 PutLine();
END EvalExprVar.
