sig Component {
	name: Name,
	main: option Service,
	export: set Service,
	import: set Service,
	version: Number
}{ no import & export }

sig GAC {
	components: set Component
}{ components.import in components.ex rt }

fun AddComponent(G,G':GAC, c:Component){
	not c in G.components
	G'.compnents