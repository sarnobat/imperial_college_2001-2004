MODULE Repeat;
(*FROM BIO IMPORT PutLine,PutInteger;*)
VAR x,y : INTEGER;
BEGIN
 x := 4;
 y := 0 ;
 REPEAT 
  x := x+100;
  y := y+1;
 UNTIL y = 12;
 y := 0 ;
 REPEAT 
  x := x+10;
  y := y+1;
 UNTIL y = 3;
 PutInteger(x); PutLine();
END Repeat.
