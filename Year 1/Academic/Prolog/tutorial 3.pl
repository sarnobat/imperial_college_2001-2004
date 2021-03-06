scrub(U,[],[]).
scrub(U,[U|X],Y) :- scrub(U,X,Y).
scrub(U,[V|X],[V|Y]) :- U\==V,scrub(U,X,Y).


d s([X|XS],YS,DS) :-
	scrub(X,XS,XSS),
	member(X,XS),
	non_member(X,YS),
	dups(XSS,[X|YS],DS).

dups([X|XS],YS,DS) :-
	non_member(X,XS),
	dups(XS,YS,DS).

dups([],D,D).