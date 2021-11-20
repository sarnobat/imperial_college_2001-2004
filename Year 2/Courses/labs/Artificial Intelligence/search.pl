initial_tree(depth,InitialState,[[(0,InitialState)]]).
initial_tree(breadth,InitialState,[[(0,InitialState)]]).
initial_tree(tiles_misplaced,InitialState,[[(Score,InitialState)]])	:-
 	addscore(tiles_misplaced,[[(Score,InitialState)]],_).
initial_tree(manhattan,InitialState,[[(Score,InitialState)]])	:-
 	addscore(manhattan,[[(Score,InitialState)]],_).

extend(Heuristic,MaxDepth,[(Score,FirstPos)|FirstPathRest],ChildPaths,Goal)	:-
	count([(Score,FirstPos)|FirstPathRest],PathDepth),
	PathDepth < MaxDepth,
	findall(X,X one_step_extension_of [(Score,FirstPos)|FirstPathRest],ChildPaths),
	addscore(Heuristic,ChildPaths,Score).
extend(Heuristic,MaxDepth,APath,ChildPaths,Goal)	:-
	count(APath,MaxDepth1),
	MaxDepth1 is MaxDepth-1.




addscore(_,[],_).
addscore(depth, [ [(ChildPathScore,Pos)|_] | OtherChildPaths ] ,ParentScore)	:-
	count([ [(ChildPathScore,Pos)|_] | OtherChildPaths ],ChildCount),
	ChildPathScore is ParentScore - ChildCount,
	addscore(depth,OtherChildPaths,ParentScore).
addscore(breadth, [ [(ChildPathScore,Pos)|ChildTail] | OtherChildPaths ] ,ParentScore)	:-
	count([(ChildPathScore,Pos)|ChildTail],CurrPathLen),
	powerSeriesSum(CurrPathLen,PSSum),
	CurrPathLen1 is CurrPathLen-1,
	powerSeriesSum(CurrPathLen1,PSSum1),
	Minus is ParentScore-PSSum1,
	Times4 is Minus*4,
	count([ [(ChildPathScore,Pos)|ChildTail] | OtherChildPaths ],ChildCount),
	breadthMap(ChildCount,Offset),
	ChildPathScore is Times4+PSSum+Offset,
	addscore(breadth,OtherChildPaths,ParentScore).
addscore(tiles_misplaced,[ [(ChildPathScore,Pos)|ChildTail] | OtherChildPaths ],ParentScore)	:-
	count(ChildTail,Depth),
	misplaced(Pos,ChildPathScore1),
	ChildPathScore is ChildPathScore1+Depth,
	addscore(tiles_misplaced,OtherChildPaths,ParentScore).
addscore(manhattan,[ [(ChildPathScore,Pos)|ChildTail] | OtherChildPaths ],ParentScore)	:-
	count(ChildTail,Depth),
	manhattan(Pos,ChildPathScore1),
	ChildPathScore is ChildPathScore1+Depth,
	addscore(manhattan,OtherChildPaths,ParentScore).


merge([],Newpaths,Newpaths).
merge([FirstChildPath|RestChildPaths],Paths,NewPaths) :-
	insert(FirstChildPath,Paths,NewPaths1),
	%compares values of heuristic fn to create an ordering (an insertion sort function)
	merge(RestChildPaths,NewPaths1,NewPaths).


insert(ChildPath,[],[ChildPath]).
insert([(ChildScore,ChildNode)|ChildPathTail],
		[[(OldScore,Node)|OldTail]|RestOldPaths],NewPaths)	:-
	ChildScore =< OldScore,
	NewPaths = [ [(ChildScore,ChildNode)|ChildPathTail],[(OldScore,Node)|OldTail]|RestOldPaths].
insert([(ChildScore,ChildNode)|ChildPathTail],
		[[(OldScore,Node)|OldTail]|RestOldPaths],NewPaths)	:-
	ChildScore > OldScore,
	insert([(ChildScore,ChildNode)|ChildPathTail],RestOldPaths,PartialNewPaths),
	NewPaths = [[(OldScore,Node)|OldTail]|PartialNewPaths].

%**************************************************************************************************
%***************************      DEPTH FIRST HELPER FUNCTIONS         ****************************
%**************************************************************************************************

count([],0).
count([H|T],N)	:-
	count(T,N1),
	N is N1+1.

%**************************************************************************************************
%***************************      BREADTH FIRST HELPER FUNCTIONS       ****************************
%**************************************************************************************************

breadthMap(4,0).
breadthMap(3,1).
breadthMap(2,2).
breadthMap(1,3).

powerSeriesSum(1,1).
powerSeriesSum(N,Sum)	:-
	N1 is N-1,
	pow(4,N1,Res),
	powerSeriesSum(N1,Sum1),
	Sum is Res + Sum1.

pow(_,0,1).
pow(X,1,X).
pow(X,Index,Result)	:-
	Index1 is Index-1,
	pow(X,Index1,Result1),
	Result is X*Result1.


%**************************************************************************************************
%***************************      TILES MISPLACED HELPER FUNCTIONS     ****************************
%**************************************************************************************************

misplaced([[1,2,3],[8,h,4],[7,6,5]],0).
misplaced([[X1,X2,X3],[X8,Xh,X4],[X7,X6,X5]],Score)	:-
	X1 \== 1,
	misplaced([[1,X2,X3],[X8,Xh,X4],[X7,X6,X5]],Score1),
	Score is Score1+1.
misplaced([[X1,X2,X3],[X8,Xh,X4],[X7,X6,X5]],Score)	:-
	X2 \== 2,
	misplaced([[X1,2,X3],[X8,Xh,X4],[X7,X6,X5]],Score1),
	Score is Score1+1.
misplaced([[X1,X2,X3],[X8,Xh,X4],[X7,X6,X5]],Score)	:-
	X3 \== 3,
	misplaced([[X1,X2,3],[X8,Xh,X4],[X7,X6,X5]],Score1),
	Score is Score1+1.
misplaced([[X1,X2,X3],[X8,Xh,X4],[X7,X6,X5]],Score)	:-
	X4 \== 4,
	misplaced([[X1,X2,X3],[X8,Xh,4],[X7,X6,X5]],Score1),
	Score is Score1+1.
misplaced([[X1,X2,X3],[X8,Xh,X4],[X7,X6,X5]],Score)	:-
	X5 \== 5,
	misplaced([[X1,X2,X3],[X8,Xh,X4],[X7,X6,5]],Score1),
	Score is Score1+1.
misplaced([[X1,X2,X3],[X8,Xh,X4],[X7,X6,X5]],Score)	:-
	X6 \== 6,
	misplaced([[X1,X2,X3],[X8,Xh,X4],[X7,6,X5]],Score1),
	Score is Score1+1.
misplaced([[X1,X2,X3],[X8,Xh,X4],[X7,X6,X5]],Score)	:-
	X7 \== 7,
	misplaced([[X1,X2,X3],[X8,Xh,X4],[7,X6,X5]],Score1),
	Score is Score1+1.
misplaced([[X1,X2,X3],[X8,Xh,X4],[X7,X6,X5]],Score)	:-
	X8 \== 8,
	misplaced([[X1,X2,X3],[8,Xh,X4],[X7,X6,X5]],Score1),
	Score is Score1+1.
misplaced([[X1,X2,X3],[X8,Xh,X4],[X7,X6,X5]],Score)	:-
	Xh \== h,
	misplaced([[X1,X2,X3],[8,h,X4],[X7,X6,X5]],Score).


%**************************************************************************************************
%***************************      MANHATTAN DISTANCE HELPER FUNCTIONS  ****************************
%**************************************************************************************************

manhattan(Pos,Score)	:-
	man_dist(1,Pos,Dist1),
	man_dist(2,Pos,Dist2),
	man_dist(3,Pos,Dist3),
	man_dist(4,Pos,Dist4),
	man_dist(5,Pos,Dist5),
	man_dist(6,Pos,Dist6),
	man_dist(7,Pos,Dist7),
	man_dist(8,Pos,Dist8),
	Score is Dist1+Dist2+Dist3+Dist4+Dist5+Dist6+Dist7+Dist8.


man_dist(X,Pos,Dist)	:-
	vert_distance(X,Pos,D1),
	hor_distance(X,Pos,D2),
	Dist is D1+D2.

vert_distance(X,[[X1,X2,X3],[X8,Xh,X4],[X7,X6,X5]],VertDist)	:-
	(X is 1; X is 2; X is 3),
	(((X1 is X; X2 is X; X3 is X), VertDist is 0);
	 ((X8 is X; Xh is X; X4 is X), VertDist is 1);
	 ((X7 is X; X6 is X; X5 is X), VertDist is 2)).
vert_distance(X,[[X1,X2,X3],[X8,Xh,X4],[X7,X6,X5]],VertDist)	:-
	(X is 8; 	X is 4),
	(((X1 is X; X2 is X; X3 is X), VertDist is 1);
	 ((X8 is X; Xh is X; X4 is X), VertDist is 0);
	 ((X7 is X; X6 is X; X5 is X), VertDist is 1)).
vert_distance(X,[[X1,X2,X3],[X8,Xh,X4],[X7,X6,X5]],VertDist)	:-
	(X is 7; X is 6; X is 5),
	(((X1 is X; X2 is X; X3 is X), VertDist is 2);
	 ((X8 is X; Xh is X; X4 is X), VertDist is 1);
	 ((X7 is X; X6 is X; X5 is X), VertDist is 0)).


hor_distance(X,[[X1,X2,X3],[X8,Xh,X4],[X7,X6,X5]],HorDist)	:-
	(X is 1; X is 8; X is 7),
	(((X1 is X; X8 is X; X7 is X), HorDist is 0);
	 ((X2 is X; Xh is X; X6 is X), HorDist is 1);
	 ((X3 is X; X4 is X; X5 is X), HorDist is 2)).
hor_distance(X,[[X1,X2,X3],[X8,Xh,X4],[X7,X6,X5]],HorDist)	:-
	(X is 2; 	X is 6),
	(((X1 is X; X8 is X; X7 is X), HorDist is 1);
	 ((X2 is X; Xh is X; X6 is X), HorDist is 0);
	 ((X3 is X; X4 is X; X5 is X), HorDist is 1)).
hor_distance(X,[[X1,X2,X3],[X8,Xh,X4],[X7,X6,X5]],HorDist)	:-
	(X is 3; X is 4; X is 5),
	(((X1 is X; X8 is X; X7 is X), HorDist is 2);
	 ((X2 is X; Xh is X; X6 is X), HorDist is 1);
	 ((X3 is X; X4 is X; X5 is X), HorDist is 0)).
