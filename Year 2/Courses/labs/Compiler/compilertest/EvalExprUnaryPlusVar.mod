MODULE EvalExprUnaryPlusVar;
(* FROM BIO IMPORT PutInteger,PutLine; *) VAR x,y : INTEGER;
BEGIN
 y := 66;
 x := 1300-(+y);
 PutInteger(x);
 PutLine();
END EvalExprUnaryPlusVar.
