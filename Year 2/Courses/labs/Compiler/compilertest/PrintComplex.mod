MODULE  PrintExprPlus;
(* FROM BIO IMPORT PutInteger,PutLine; *)

VAR y : INTEGER;
BEG 
 y:=2;
 PutInteger(y+y*(200+(4*y*5)*10)+17*y-2);
 PutLine();
END PrintExprPlus.
