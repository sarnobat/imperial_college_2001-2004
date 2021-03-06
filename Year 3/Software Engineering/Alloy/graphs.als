module AboutGraphs

sig Element {}
sig Graph{
	nodes: set Element,
	edges: nodes -> nodes
} 

assert RandSImplyT{
	all G: Graph | with G {
		iden[nodes] in edges && edges = ~edges => edges.edges in edges
	}
}
//check RandS plyT for 3 but 1 Graph

assert TwistedScenario {	
	all g:Graph | with g {
		not #nodes = 7 ||
		some n:nodes | not #n.~edges = 5
	}
}
//check TwistedScenario for 7 but 1 Graph

fun TwistedRun(g:Graph) {
	with g {
		# nodes = 7 ||
		all n:nodes | # n.^edges = 5
	}
}
run TwistedRun for 7 but 1 Graph