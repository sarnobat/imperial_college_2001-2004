MODULE BigConst;
(* FROM BIO IMPORT PutLine,PutInteger; *)
VAR x,y : INTEGER;
BEGIN
 x := 1234000;
 y := 123*1000+4000;
 IF ( x = 1234000 )
  THEN
   PutInteger(x / 1000 );
  ELSE
   PutInteger(x / y);
  END;
   Pu ine();
END BigConst.
