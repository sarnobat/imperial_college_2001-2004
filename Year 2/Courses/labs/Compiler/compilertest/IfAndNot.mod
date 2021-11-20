MODULE IfAndNot;
(* FROM BIO IMPORT PutInteger,PutLine;*)

VAR i : INTEGER;
BEGIN
 i := 43;
  IF  NOT(i >= 44) AND NOT(i = 98)
  THEN
   PutInteger(1234)
  ELSE
   PutInteger(456)
  END;
 PutLine();
END IfAndNot.
