MODULE IfAndNotVar;
(* FROM BIO IMPORT PutInteger,PutLine; *)

VAR i,j : INTEGER;
BEGIN
 i :=  ;
 j := 44;
  IF  NOT(i >= j) AND NOT(i = j+1)
  THEN
   PutInteger(1234)
  ELSE
   PutInteger(456)
  END;
 PutLine();
END IfAndNotVar.
