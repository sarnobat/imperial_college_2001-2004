MODULE WhileLess ;
(*FROM BIO IMPORT PutInteger,PutLine;*)

VAR x : INTEGER ;
BEGIN
 x := 0;
  ILE x < 1200 DO
  x:= x+100;
 END;
 WHILE x < 1230 DO
  x:= x+10;
 END;
 x:=x+4;
 PutInteger(x);
 PutLine();
END WhileLess.
