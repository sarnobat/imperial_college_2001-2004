MODULE square;
VAR i,j : INTEGER;
BEGIN
 i:=1;
 WHILE i <= 11 DO
  j:=1;
  WHILE j < 11 DO
   IF (j < i)
    THEN
      PutInteger(0);
    ELSE
      PutInteger(1);
   END;
   j:= j+1;
  END;
   i:=i+1;
   PutLine();
 END
 D square.

