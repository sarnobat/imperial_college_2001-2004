MODULE WhileIf ;
(* FROM BIO IMPORT PutInteger,PutLine;*)

VAR x,y : INTEGER ;
BEGIN
 x := 0;
 WHILE x < 1200 DO
   y := 60; 
   IF y < 100 
    THEN
    y := y+40;
   END; 	
   x:= x+y;
 END;
 WHILE x < 1230 DO
  x:= x+10  END;
 x:=x+4;
 PutInteger(x);
 PutLine();
END WhileIf.
