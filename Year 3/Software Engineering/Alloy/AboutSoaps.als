module AboutSoaps
sig Person {}
sig SoapOpera {
	cast: set Person,
	lead: cast,
	loves: cast - cast 
}
assert OfLovers {
	all S: SoapOpera |
	with S { all x,y :  cast | lead in x.loves
	&& x in y.loves => not lead in y.loves }
}
check OfLovers for 2 but 1 SoapOpera

fact NoSelfLove {
	all S: SoapOpera,
	p: S.cast |
	not p in p.(S.loves)
}