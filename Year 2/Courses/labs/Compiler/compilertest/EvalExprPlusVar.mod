MODULE EvalExprPlusVar;
(*FROM BIO IMPORT PutInteger,PutLine;*)

VAR x,y : INTEGER;
BE N
 y := 34;
 x := 1200+y;
 PutInteger(x);
 PutLine();
END EvalExprPlusVar.
