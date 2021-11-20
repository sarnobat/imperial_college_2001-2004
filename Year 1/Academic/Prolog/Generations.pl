?- use_module(library(lists)).
?- prolog_flag(toplevel_print_options, _,
               [quoted(true), numbervars(true), portrayed(true), max_depth(0)]).

%%%%%%%%%%%%
%% TASK 1 %%
%%%%%%%%%%%%

setup(L) :-
	findall(P,par(P,_),PsDup),
	findall(Q,par(_,Q),QsDup),
	append(PsDup,QsDup,NsDup),
	remove_duplicates(NsDup,LL),
	findall((N,K),member(N,LL),L),
	L = [(_,1)|_].
	

%%%%%%%%%%%%
%% TASK 2 %%
%%%%%%%%%%%%

forall(P,Q) :-
	\+ (P,\+Q).

labelpersons(X) :-
	forall(member((_,K),X),number(K)).

labelpersons(X) :-
	member((N,K),X),
	var(K),
	par(N,M),
	member((M,J),X),
	number(J),
	K is J-1,
	!,
	labelpersons(X).

labelpersons(X) :-
	member((N,K),X),
	var(K),
	par(M,N),
	member((M,J),X),
	number(J),
	K is J+1,
	!,
	labelpersons(X).


%%%%%%%%%%%%
%% TASK 3 %%
%%%%%%%%%%%%

generations(Gs) :-
	setup(L),
	labelpersons(L),
	findall(X,member((_,X),L),LL),
	remove_duplicates(LL,Nodups),
	sort(Nodups,Sorted),
	Sorted = [Kleast|_],
	Offset is 1-Kleast,
	offset(Sorted,Offset,Is),
	findall(  (I,G)   ,   (member(I,Is),J is I-Offset,is_gen(J,G,L))   ,  Gs   ).

is_gen(I,G,L) :-
	findall(X,member((X,I),L),G).

offset([],K,[]).
offset([L|LL],K,[M|MM]) :-
	M is L + K,
	offset(LL,K,MM).
	

%%%%%%%%%%%%
%% TASK 4 %%
%%%%%%%%%%%%

latest(Gs,G) :-
	member((G,_),Gs),
	forall(member((X,_),Gs),G >= X).


%%%%%%%%%%%%
%% TASK 5 %%
%%%%%%%%%%%%

mostpop(Gs,G) :-
	member((G,P),Gs),
	length(P,L),
	forall(member((_,Q),Gs),(length(Q,LL), LL =< L)).


%%%%%%%%%%%%
%% TASK 6 %%
%%%%%%%%%%%%

all_parents_known(Gs,G) :-
        member((G,_),Gs),
	forall((member_of_gen(X,G,Gs)),(par(Y,X),par(Z,X),Z \== Y)).

member_of_gen(X,G,Gs) :-
	member((G,P),Gs),
	member(X,P).




%%%%%%%%%%%%%%%%%%%%%%%%%
%	  DATA		%
%%%%%%%%%%%%%%%%%%%%%%%%%

par(f, j).
par(e, i).
par(e, j).
par(i, m).
par(g, l).
par(a, m).
par(a, n).
par(l, n).
par(m, o).
par(k, c).
par(c, g).
par(c, h).
par(d, g).
par(d, h).
par(b, f).
par(f, i).