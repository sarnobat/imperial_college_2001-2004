MODULE EvalExprMinusVar;
(* FROM BIO IMPORT PutInteger,PutLine; *)

VAR x,y :  TEGER;
BEGIN
 y := 66;
 x := 1300-y;
 PutInteger(x);
 PutLine();
END EvalExprMinusVar.
