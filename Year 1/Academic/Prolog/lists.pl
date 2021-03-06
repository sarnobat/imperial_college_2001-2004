child_of(amelia, frank).

ones([]).
ones([1|Y]) :- ones(Y).

ones_zeros([]).
ones_zeros([0|X]) :- ones_zeros(X).
ones_zeros([1|Y]) :- ones_zeros(Y).

alt_ones_zeros([1]).
alt_ones_zeros([1,0]).
alt_ones_zeros([1 |X]).

hasdups([X|Y]) :- member(X,Y).
hasdups([X|Y]) :- hasdups(Y).

scrub(U,[U|P],Q) 	:- scrub(U,P,Q).
scrub(U,[A|P],[A|Q])	:- scrub(U,P,Q),U\==A.
scrub(U,X,X). %but then you could just say scrub(2,[2,2,2],[2,2,2])

dups([],D,D).
dups([A|X],Y,D) :- dups(X,[A|Y],D),member(A,X).
dups([A|X],Y,D) :- dups(X,Y,D),non-memeber(A,X).