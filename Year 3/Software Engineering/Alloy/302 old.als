module Plugins
open std/ord
	sig Bool {
	}
	sig Number {
	}

	//QUESTION 1
	sig Interface {
		//Upper boud for the installation of plugs for this interface
		//number: Number
	}
	
	
	sig Class {
		implements: set Interface,
		abstract: option Bool
	}
	sig Component {
	//A plugin
		holes: set Interface,
		pegs: set Class//Classes(i.e. Services) provided
		//'required'
		//Services that are NOT provided by this component, but may be implemented by another component's classes
	}
	sig Binding {
		from: Component,//Component containing a particular interface
		to: Component,	//Component containing implementation of the 'from' interface
		hole: Interface,
		peg: Class
	}
	fact noSelfImplementation {
		all b:Binding | with b {
			not from = to
		}
	}
	/**
	 * Ensures that bindings are only made if one of the pegs in one Component provides
	 * and implementation of one of the holes in the other Component
	 */
	fact BindingContainsImplementation {
		all b:Binding | with b {
			hole in from.holes//An interface
			peg in to.pegs//A Class implementing the interface
		}
	}
	sig System {
		components: set Component,//All components in the system?
		start: Component, //System's core component
		bindings: set Binding
	}
	fact SystemComponentsValid{
		//Core System component must be one of the system's available components
		all S:System | with S {start in components}
		//Binding must be only between components that exist in the system
		all S:System | with S {
			all b:Binding |
			(b in bindings => 
				b.hole in components.holes &&
				b.peg in components.pegs)
		}
	}

	//QUESTION 2
	fun Add(s,s':System,
		c:Component,		//TO		
		cl:c.pegs,		//classes provided by Component c
		c':s.components-c,	//FROM (component containing interface to be implemented)
		i:c'.holes		//Interfaces of FROM
		){

		//THIS BLOCK IS WRONG. Component does NOT need to be 'new'
		//Cannot add a component that already exists in the System before
		//not c in s.components
		//New set of components is old set plus the added component
		//s'.components = c'+c

		s'.components = s.components

		//A binding is added to the System's set of bindings; this binding
		//is intended for some interface that exists in the system (i.e. in set 'i');
		//the binding must involve a class that is contained in the new component 'c' (i.e. set cl)
		//AFTER THE FIRST THREE, THEY'RE PROBABLY WRONG
		some b:Binding | b.hole in i && b.peg in cl && s'.bindings = s.bindings + b //TODO && not b.hole.number = "0" && DECREMENT NUMBER

		//TODO: Somehow need to specify a number for the interface(s), and that if it's zero you can't add any more
		//TODO: Need to specify the constraint at the end of paragraph 2
		//and that if you do, you need to decrement the 'number' field in the 'Interface'
	}
	//run Add for 2
	fun AbstractAdd(s,s':System,
			c:Component){
		Add(s,s',c,c.pegs,s.components-c,s.components.holes)
	}
	//run AbstractAdd for 2

	//QUESTION 3
	fun Evolve() {
		all s0:System | some s1:System,c0:Component |  (
					AbstractAdd(s0,s1,c0)
					//&& s1 = Ord[System].next[s0]
					//&& not s0 = Ord[System].last
				
			
			//|| s0 = Ord[System].last
		)
	}
	run Evolve for 2


	//QUESTION 4
	fun AGuidedSimulations(){
		some s0,s1,s2:System,c:Component | {
			s0.components = c
			
		}
	}
