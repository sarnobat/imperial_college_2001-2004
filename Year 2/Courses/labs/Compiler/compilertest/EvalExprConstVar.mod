MODULE  EvalExprConstVar;
(* FROM BIO IMPORT PutInteger,PutLine; *)

VAR x,y : NTEGER;
BEGIN
 x := 12;
 y := x*100+34;
 PutInteger(y);
 PutLine();
END EvalExprConstVar.
