MODULE EvalExprUnaryPlus;
(* FROM BIO IMPORT PutInteger,PutLine; *)

VAR x : I EGER;
BEGIN
 x := 1300-(+66);
 PutInteger(x);
 PutLine();
END EvalExprUnaryPlus.
