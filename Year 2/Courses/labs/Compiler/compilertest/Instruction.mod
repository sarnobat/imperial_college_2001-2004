MODULE Instruction;
(* Are b and j translated correctly *)
(* FROM BIO IMPORT PutInteger,PutLi ; *)
VAR b,j : INTEGER;
BEGIN
 b := 11105;
 j := 1;
 b:=b+j;
 PutInteger(b / 9);
 PutLine();
END Instruction.
