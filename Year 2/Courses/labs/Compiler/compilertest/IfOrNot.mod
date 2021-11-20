MODULE IfOrNot;
(* FROM BIO IMPORT PutInteger,PutLine;*)

VAR i : INTEGER;
BEGIN
 i := 43;
  IF  NOT(i > 41) OR NOT (i = 98)
  THEN
   PutInteger(1234)
  ELSE
   PutInteger(456)
  END;
 PutLine();
END IfOrNot.
