MODULE  EvalExprDivVar;
(* FROM BIO IMPORT PutInteger,PutLine; *)

VAR x,y,z : INTEGER BEGIN
 y := 10;
 z := 1234 * y;
 x := z / 10;
 PutInteger(x);
 PutLine();
END EvalExprDivVar.
