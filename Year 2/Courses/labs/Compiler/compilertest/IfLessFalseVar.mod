MODULE  IfLessFalseVar;
(* FROM BIO IMPORT PutInteger,PutLine;*)
VAR i : INTEGER;
BEGI  i := 3;
 IF 2 < i
  THEN
   PutInteger(1234)
  ELSE
   PutInteger(456)
 END;
 PutLine();
END IfLessFalseVar.
