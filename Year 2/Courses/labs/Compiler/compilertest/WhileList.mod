MODULE WhileList;
(* FROM BIO IMPORT PutLine,PutInteger;*)
VAR x,y : INTEGER;
BEGIN
  x:= 123;  WHILE x < 1000
   DO
     x:= x*10;
     x:= x+4
  END;
  PutInteger(x);
  PutLine(); 
 END WhileList .
