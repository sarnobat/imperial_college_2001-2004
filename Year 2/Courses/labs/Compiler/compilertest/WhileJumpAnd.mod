MODULE WhileJumpAnd;
(* FROM BIO IMPORT PutInteger,PutLine; *)

VAR x : INTEGER;
BEGIN x:=0;
 WHILE x>0 AND 1 / x > 0
  DO
   PutInteger(456)
  END;
 PutInteger(1234);
 PutLine();
END WhileJumpAnd.
