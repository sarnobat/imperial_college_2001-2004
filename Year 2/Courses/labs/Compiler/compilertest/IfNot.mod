MODULE IfNot;
(* FROM BIO IMPORT PutInteger,PutLine;*)

VAR x : INTEGER;
 BEGIN
  x := 20;
  IF NOT (x>20)
    THEN
     PutInteger(1234)
    ELSE
     PutInteger(x)
  END;
 PutLine();
END IfNot.
