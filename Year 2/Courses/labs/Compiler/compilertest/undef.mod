MODULE undef;
(* What happens if a variable is not initialised *)
(* FROM BIO IMPORT PutLine,PutInteger; *)
VAR x,y :INTEGER;
BEGIN
 IF y > 0
  THEN
   x := 4
 END;
 PutInteger(1234+x);
 PutLine();
END undef.
