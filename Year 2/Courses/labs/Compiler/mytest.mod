MODULE mytest;
VAR i,j,k : INTEGER;
BEGIN
  i:=1;
  j:=2;
  k:=3;
  IF  NOT ((NOT i<j OR k>i) AND ((NOT j<i OR i<i) AND k>i) AND i<j)
    THEN
    	PutInteger(1111)
    ELSE
    	PutInteger(2222)
  END
END mytest.
