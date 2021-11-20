MODULE IfAnd;
(* FROM BIO IMPORT PutInteger,PutLine;*)
VAR i : INTEGER;
BEGIN
 i := 43;
  IF  (i < 44) AND (i # 98)
  THEN
   PutInteger(1234)
  ELSE
   PutInteger(456)
  END;
 PutLine();
END IfAnd.
