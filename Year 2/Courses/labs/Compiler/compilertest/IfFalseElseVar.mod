MODULE  IfFalseElseVar;
(* FROM BIO IMPORT PutInteger,PutLine; *)
VAR x,y : INTEGER;
B IN
 x := 2;
 y := 1;
 IF x-y # 1
  THEN
   PutInteger(456)
  ELSE
   PutInteger(1234)
 END;
 PutLine();
END IfFalseElseVar.
