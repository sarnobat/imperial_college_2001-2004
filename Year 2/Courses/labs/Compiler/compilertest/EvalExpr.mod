MODULE  EvalExpr ;
(* FROM BIO IMPORT PutInteger,PutLine; *)

VAR x : INTEGER;
BEGIN
 x := 10;
 x := ((1*x+2)*x+3)*x+4;
 PutInteger(x);
 PutLine();
END EvalExpr.
