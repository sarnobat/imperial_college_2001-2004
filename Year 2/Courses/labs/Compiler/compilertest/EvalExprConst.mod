MODULE  EvalExprConst;
(* FROM BIO IMPORT PutInteger,PutLine; *)
CONST x = 12;
      z  34;
VAR y : INTEGER;
BEGIN
 y := x*100+z;
 PutInteger(y);
 PutLine();
END EvalExprConst.
