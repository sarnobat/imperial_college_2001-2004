MODULE RepeatJumpOr;
(* FROM BIO IMPORT PutInteger,PutLine; *)
VAR x : INTEGER;
BEGIN
 :=0;
 REPEAT
   PutInteger(1234);
 UNTIL x=0 OR  1 / x = 0;
 PutLine();
END RepeatJumpOr.
