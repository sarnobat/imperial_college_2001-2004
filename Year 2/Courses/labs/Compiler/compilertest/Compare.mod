MODULE Compare;
(* FROM BIO IMPORT PutInteger,PutLine;*)

VAR x : INTEGER;
 BEGIN
  x := 0;
  IF x = 0
   THEN
    x := 2
   ELSE
    x:=1
  END;
  IF x > 0
   THEN
    x := 34
   ELSE
    x:=2
  END;
  IF x < 40
   THEN
   x := 200+x
   ELSE
    x:=3
  END;
  IF x >= 200
   THEN
    x := x + 1000
   ELSE
    x:=4
  END;
  IF x # 0
   THEN
    x := x+1
   ELSE
    x:=5
  END;
  IF x <= 2000
   THEN
    x := x-1
   ELSE
    x:=6
  END;

  PutInteger(x);
  PutLine();
END Compare.


