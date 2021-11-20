MODULE IfList;
(* FROM BIO IMPORT PutLine,PutInteger; *)
VAR x,y : INTEGER;
BEGIN
  x:= 123;
  IF x > 0
   THEN
     x:= x*10;
     x:= x+4
  END;
  PutInteger(x);
  PutLine(); 
 END IfList .
