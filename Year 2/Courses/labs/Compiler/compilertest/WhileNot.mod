MODULE WhileNot ;
(* FROM BIO IMPORT PutInteger,PutLine; *)
VAR x : INTEGER ;
BEGIN
 x := 1230;
 WHILE NOT(x >= 1234) DO
  x:= x+2;
 END;
 PutInteger(x);
 PutLine();
END WhileNot.
