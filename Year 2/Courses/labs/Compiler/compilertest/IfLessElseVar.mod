MODULE  IfLessElseVar;
(* FROM BIO IMPORT PutInteger,PutLine; *)
VAR i : INTEGER;
BEGI  i := 3;
 IF i < 2
  THEN
   PutInteger(456)
  ELSE
   PutInteger(1234)
 END;
 PutLine();
END IfLessElseVar.
