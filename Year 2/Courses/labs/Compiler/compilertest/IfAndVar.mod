MODULE IfAndVar;
(* FROM BIO IMPORT PutInteger,PutLine; *)
VAR i,j : INTEGER;
BEGIN
 i := 43;
 j := 44;
  IF  (i < j) AND (i # (j+1))
  THEN
   PutInteger(1234)
  ELSE
   PutInteger(456)
  END;
 PutLine();
END IfAn ar.
