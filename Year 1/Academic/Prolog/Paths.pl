?- use_module(library(lists)).

arc(a,b,5).
arc(a,e,1).
arc(b,c,2).
arc(c,d,6).
arc(e,c,3).
arc(e,f,9).
arc(f,d,4).

arc(f,a,6).
arc(a,a,0).


%%%%%%%%%%%%
%% TASK 1 %%
%%%%%%%%%%%%

path(X,Y,[X,Y]) 	:- arc(X,Y,_).
path(X,Z,[X|L]) 	:- arc(X,Y,_),path(Y,Z,L).


%%%%%%%%%%%%
%% TASK 2 %%
%%%%%%%%%%%%

%	This cannot avoid visiting nodes on multiple occassions because
%	it doesn't have knowledge of the nodes already visited.


%%%%%%%%%%%%
%% TASK 3 %%
%%%%%%%%%%%%


acpath(X,X,L,M) 	:-
	reverse([X|L],M).	

acpath(X,Z,L,T)		:-
	X\==Z,
	arc(X,Y,_),
	X\==Y,
	non_member(Y,L),
	acpath(Y,Z,[X|L],T).


%%%%%%%%%%%%
%% TASK 4 %%
%%%%%%%%%%%%


cycle(X,[X,X]) :- arc(X,X,_).

cycle(X,[X,Y|Rest])	:-
	arc(X,Y,_),
	X\==Y,
	acpath(Y,X,[],[Y|Rest]).


%%%%%%%%%%%%
%% TASK 5 %%
%%%%%%%%%%%%

pathcost([X,Y],PC) 	:-
	arc(X,Y,PC).
pathcost([X,Y|Rest],PC) :-
	pathcost([Y|Rest],Remcost),
	arc(X,Y,Thiscost),
	PC is Thiscost + Remcost.


%%%%%%%%%%%%
%% TASK 6 %%
%%%%%%%%%%%%

anypath(X,X,Path) 	:- cycle(X,Path).
anypath(X,Z,Path) 	:- acpath(X,Z,[],Path).

samecost(X1,Z1,X2,Z2,PC) :-
	anypath(X1,Z1,Path1),
	anypath(X2,Z2,Path2),
	X1 \== X2,
	pathcost(Path1,PC),
	pathcost(Path2,PC).