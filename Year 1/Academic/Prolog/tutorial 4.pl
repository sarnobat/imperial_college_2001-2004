pairs([N|X],[(U,V)|Y]) :- 
	pairs(X,Y),
	U =:= N-1,
	V =:= N+1.
pairs([],[]).



arbpairs([N|X [(N,L)|Y]) :- 
	L =:= N*2,
	arbpairs(X,Y).
arbpairs([N|X],[(N,N)|Y]) :- 
	arbpairs(X,Y).
arbpairs([],[]).



numval(A,A) :-
	number(A).

numval(a(X,Y),V) :-
	numval(X,XX),
	numval(Y,YY),
	Z is XX+YY,
	numval(Z,V).

numval(m(X,Y),V) :-
	numval(X,XX),
	numval(Y,YY),
	Z is XX*YY,
	numval(Z,V).


numval_correct :-
	numval( 	    a(   m(3,a(2,5))  ,    a(2,3)      )     ,
			 25 ).

