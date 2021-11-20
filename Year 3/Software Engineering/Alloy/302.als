module Plugins
open std/ord
/**
 * This is the sole effort of Sridhar Sarnobat (ss401@doc.ic.ac.uk), part of CS BEng 3
 */
		

	sig Bool {
	}
	sig Number {
	}
	
	/**
	 * QUESTION 1
	 */
	sig Interface {
		// I have named the number field as 'maxPlugs', as 'number' isn't descriptive enough
		maxPlugs :Number
	}
	sig Class {
		implements :set Interface,
		abstract :Bool
	}
		
	sig Component {
		holes: set Interface,
		pegs: set Class
	}
	{
		//An implementation of a service cannot be self contained
		all cl:pegs,i:holes | not i in cl.implements
	}
	sig Binding {
		from: Component,
		to: Component,
		hole: Interface,
		peg: Class
	}
	{
		//HOLE is a member of FROM
		hole in from.holes
		//PEG is a member of TO
		peg in to.pegs

		//PEG implements HOLE
		hole in peg.implements
	}

	sig System {
		components: set Component,
		start: Component,
		bindings: set Binding
	}
	{
		//START is in COMPONENTS
		start in components
	}


	/**
	 * QUESTION 2
	 */
	fun Add(s,s':System,
		c:Component,		//TO		
		cl:c.pegs,		//classes provided by Component c
		c':s.components-c,	//FROM (component containing interface to be implemented)
		i:c'.holes		//Interfaces of FROM
		){
		//Component c and c' already in system
		c in s.components
		//Components present in system do not change
		s'.components = s.components
		//Class cl implemenets the interface i
		i in cl.implements

		//new binding added
		some b:Binding | (
			not b in s.bindings 
			&& s'.bindings = s.bindings+b 
			&& b.from = c'
			&& b.to = c
			&& b.hole = i 
			&& b.peg = cl
		)

		//i isn't already implemented by the maximum number of permitted plugins
		not i.maxPlugs = Ord[Number].first
		some i':s'.components.holes {
			i.maxPlugs = Ord[Number].next[i'.maxPlugs]
		}
	}
	run Add for 2

	fun AbstractAdd(s,s':System,
			c:Component){
		Add(s,s',c,c.pegs,s.components-c,s.components.holes)
	}
	run AbstractAdd for 2
	
	/**
	 * QUESTION 3
	 */
	fun Evolve() {
		all s0:System-Ord[System].last | some s1:System | some c:Component | (
			not c in s0.components &&
			s1.components = s0.components+c &&
			s0.start = s1.start
		) 
	}
	run Evolve for 2

	/**
	 * QUESTION 4
	 */
	fun AGuidedSimulation(){
		some s0,s1,s2:System,c1,c2:Component,b:Binding | (
			
			//Component count incremented by 1 - twice
			#s0.components = 1 &&
			not c1 in s0.components	&& 
			s1.components = s0.components+c1 && 
			not c2 in s1.components	&& 
			s2.components = s1.components+c2 && 
			#s2.components = 3 &&

			//One binding added.
			#s0.bindings = 0 &&
			#s1.bindings = 0 &&
			#s2.bindings = 1 &&
			b = s2.bindings &&
			b.from not in s0.components &&
			b.to not in s0.components
		)
	}
	run AGuidedSimulation for 3 //NO SOLUTIONS IF RUN FOR 2

	/**
	 * QUESTION 5
	 */
	assert AbstractAddIsDeterministic {
		all s0,s1,s2:System | some c:Component | 
			#s0.components = 2 && AbstractAdd(s0,s1,c) && AbstractAdd(s0,s2,c)
				=> structuralSystemEquality(s1,s2)
		
	}
	fun structuralSystemEquality(s,s':System) {
		s.components = s'.components
		s.start = s'.start
		s.bindings = s'.bindings
	}
	check AbstractAddIsDeterministic for 3
	assert AddIsDeterministic {
		all s0,s1,s2:System | 
			some c:Component | 
				Add(s0,s1,c,c.pegs,s0.components-c,s0.components.holes) && 
				Add(s0,s2,c,c.pegs,s0.components-c,s0.components.holes) =>
				s1=s2
	}
	check AddIsDeterministic for 2
	assert AbstractImplementation {
		all cl:Class | cl.abstract = Ord[Bool].first => #cl.implements = 0
	}
	check AbstractImplementation for 2
	assert NoSelfPlugs {
		all s0,s1:System | some c:Component | some b:Binding | 
			AbstractAdd(s0,s1,c) && 
			s1.bindings - s0.bindings = b &&
			not b.from = b.to
	}
	check NoSelfPlugs for 2

	/**
	 * COMMENTS ENLIGHT OF ASSERTIONS
	 * 1. The deterministic assertion is satisfied.
	 * 2. The AbstractImplementation finds a counter example because nowhere is it specified that an abstract class
	 * cannot implement an interface. This is an easy constraint to add (given above in the class signature constraints,
	 * but commented out).
	 * 3. The noSelfPlugs assertion finds a solution, but it is not a genuine one. The example given is that the set of bindings
	 * is an empty set before and after. Extra constraints are needed to filter out such a case.
	 */