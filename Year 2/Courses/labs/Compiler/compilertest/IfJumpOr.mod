MODULE IfJumpOr;
(* FROM BIO IMPORT PutInteger,PutLine; *)

VAR x : INTEGER;
BEGIN
 x:=0;
 IF (x=0) OR  ((1 / x) > 0)
  THEN
   PutInteger(1234)
  ELSE
   PutInteger(456)
 END;
 PutLine();
END IfJumpOr.
