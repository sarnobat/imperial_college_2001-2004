module AboutSoaps   -- named module

sig Person {
  agent : option Person,
  contracts : set SoapOpera
}

sig SoapOpera {   -- composed signature
  cast : set Person,   -- has a set of persons as cast
  lead : cast,       -- has a lead which is a cast
  loves : cast -> cast  -- cast relationships, literally :-)
}

sig Firm {
  projects : set SoapOpera,
  exclusive : set Person
}

fun Hire(p : Person, o : SoapOpera, f, f' : Firm) {
  all x : f.projects & o | not p in x.cast
  o in f'.projects
  all x : f'.projects & o | p in x.cast
}
//run Hire for 3

fun Fire(p: Person, o: SoapOpera, f,f' : Firm){
	all x: f.projects & o | p in x.cast
	o in f.projects
	all x : f'.projects & o | not p in x.cast
}
run Fire for 3
//
fun Launch(o : SoapOpera, f,f' : Firm) {
  not o in f.projects
  o in f'.projects
} 
//run Launch for 3

fun Scrap(o : SoapOpera, f,f' : Firm) {
  o in f.projects
  not o in f'.projects
} 
//run Scrap for 3

fun AGuidedSimulation(p1, p2 : Person,
    o1,o2,o3 : SoapOpera, f,f' : Firm) {
  Hire(p1,o1,f,f')
  Launch(o1,f,f')
} 
//run AGuidedSimulation for 3

assert OfLovers {  -- a goal that we mean to check
  all S : SoapOpera |  -- goal has to be met by all soaps
    with S {           -- enables use of "lead" instead of "S.lead" etc
      all x, y : cast | lead in x.loves && x in y.loves =>
                        not lead in y.loves
    }
} 
//check OfLovers for 2 but 1 SoapOpera   -- specify scope/size of domains

/*   -- fact is commented out for first analysis of assertion
fact NoSelfLove {  -- a global constraint informed by the goal violation
  all S : SoapOpera, p : S.cast | not p in p.(S.loves)
}
*/
