MODULE square;
CONST size = 11;
VAR i,j : INTEGER;
BEGIN
 i:=1;
 WHILE i <= size DO
  j:=1;
  WHILE j < size DO
   IF (j < i)
    THEN
      PutInteger(0);
    ELSE
      PutInteger(1);
   END;
   j:= j+1;
  END;
   i:=i+1    PutLine();
 END
END square.
