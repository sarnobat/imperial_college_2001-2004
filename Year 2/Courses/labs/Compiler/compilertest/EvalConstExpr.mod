MODULE  EvalConstExpr ;
(* FROM BIO IMPORT PutInteger,PutLine; *)

VAR x : INTEGER;
BE N
 x := ((1*10+2)*(1+9)+3)*(2+8)+4;
 PutInteger(x);
 PutLine();
END EvalConstExpr.
