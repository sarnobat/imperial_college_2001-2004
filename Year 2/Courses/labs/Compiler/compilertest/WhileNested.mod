MODULE WhileNested ;
(* FROM BIO IMPORT PutInteger,PutLine;*)

VAR x,y : INTEGER ;
BEGIN
 x := ;
 WHILE x < 1200 DO
   y := 60; 
   WHILE y < 100 DO
    y := y+20;
   END; 	
   x:= x+y;
 END;
 WHILE x < 1230 DO
  x:= x+10;
 END;
 x:=x+4;
 PutInteger(x);
 PutLine();
END WhileNested.
