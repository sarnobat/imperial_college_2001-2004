MODULE  IfNested;
(* FROM BIO IMPORT PutInteger,PutLine; *)
VAR i,j : INTEGER;
BEGIN
 i := 3;
 j := 1230;
 IF i < 2
  THEN
   PutInteger(456)
  ELSE IF j > 1200
    THEN
      PutInteger(i+j+1)
    ELSE
      PutIn ger(234)
   END;
 END;
 PutLine();
END IfNested.
