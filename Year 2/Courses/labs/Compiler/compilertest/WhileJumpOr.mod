MODULE WhileJumpOr;
(* FROM BIO IMPORT PutInteger,PutLine; *)
VAR x : INTEGER;
BEGIN
 x:=0;
 W LE x=0 OR  1 / x = 0
  DO
   PutInteger(1234);
   x := 1;
 END;
 PutLine();
END WhileJumpOr.
