:- op( 800, xfx, ends_at ).
:- op( 800, xfx, one_step_extension_of ).
:- use_module(library(lists)).
/* Framework for AI option assessed exercise: "Search Strategies"  */

/* Top level query:     start_search(<testnumber>, <heuristic>, <max depth> )

   where <heuristic> is one of : depth, breadth, tiles_misplaced, manhattan,
   and <max depth> is the maximum level of the search space tree
   to be explored

*/

/* In the lectures we looked simply at the "open nodes" and computed the 
   "children" of the selected one.
   At the end we wish to display the path from the start to any node in the
   list so here our nodes must actually be a list of the Node plus the path
   (in reverse) from the Start.
   To have one framework which will work with any heuristic we will always 
   select the first node (path) in the Open List which is always ordered by
   the evaluation function so the "best" node (path) is first.  
   The merge predicate will insert the children Nodes into the Open list
   according to their evaluation function valuation. 
   Remember that the evaluation function F = G' + H'  where G' is
   the no of steps from start and H' is the guessed future cost.

You need to write (for each of the heuristics) 
(1) the initial_tree predicate which sets up the initial state
    of the path being followed 
(2) the extend predicate which computes all the 
    one_step_extensions (children) from the chosen node - provided the 
    depth bound has not been exceeded 
(3) the merge predicate which maintains the Open list in evaluation 
    function order 
*/  

start_search(TestNo, Heuristic, MaxDepth ) :-
  initial(TestNo, InitialState ),
  goal( GoalState ),
  initial_tree( Heuristic, InitialState, Paths ),  % for you to write
  search( Heuristic, MaxDepth, 0, Paths, GoalState, Solution, TotalNodes ),
  displaySolution(Solution, TotalNodes).

displaySolution([(_,N)], TotalNodes) :-
  write( 'The goal and the start are the same : ' ), nl,nl,
  write_solution( [N] ),nl.

displaySolution([(_,N)|Rest], TotalNodes) :-
  write( 'The solution is achieved by the following moves : ' ), nl,nl,
  write_solution( [N|Rest] ),
  length(Rest, Length),
  write('The solution has length '), write(Length), nl,
  write('The solution has TotalNodes '), write(TotalNodes), nl,
  write('The solution has Penetrance '), P is TotalNodes/Length,
  write(P), nl.

% search( Heuristic, MaxDepth, NodesSoFar, [Weight_of_path,FirstPath|Paths],
%          Goal,SolutionPath, TotalNodes )

search( _ , _, TotalNodes, [FirstPath| _ ], Goal, FirstPath, TotalNodes )
  :- FirstPath ends_at ( _, Goal ).

search( Heuristic, MaxDepth, NodesSoFar, [FirstPath|Paths], Goal, SolutionPath,
  TotalNodes ) :-
  extend( Heuristic, MaxDepth, FirstPath, ChildPaths, Goal),% for you to write
  merge(ChildPaths,Paths,NewPaths), NodeCount is NodesSoFar + 1,
  write_extension( FirstPath, ChildPaths ),
  search(Heuristic, MaxDepth, NodeCount, NewPaths, Goal, SolutionPath,
  TotalNodes ).


[(Score, Node)|  _ ] ends_at (Score, Node).

/* You may use the following predicate within the body of your 'extend' 
  predicate. 
  NOTE that Score is a pair of logical variables and will be filled in later 
       by addscore in extend  */
% Score                                               PrevScore
[(_ , Child), Node | Rest] one_step_extension_of [(_, Node) | Rest] :-
  move_hole( Node, Child ),
  \+ looping(Child, Rest).   % Check child is not  the same as grandparent


looping(Child, [Child|_]). % note also fails if 2nd arg is an empty list (ie at start)

/*
 The square puzzle is represented as a list of 3 rows,
 top to bottom; each row contains 3 tiles, ordered left to right.
 The empty space or hole is represented by the letter 'h'.
*/

initial(0, [ [1, 2, 3], [8, h, 4], [7, 6, 5] ] ).
     % start at goal to test termination
initial(1, [ [1, 2, 3], [8, 4, h], [7, 6, 5] ] ).
     % start 1 away from goal to test termination
initial(2, [ [8, 1, 3], [2, h, 4], [7, 6, 5] ] ).  
     % easy (4 step soln) 
initial(3, [ [2, 8, 3], [1, 6, 4], [7, h, 5] ] ).  
     % the one on the diagram in the spec (5 step solution)

goal( [ [1, 2, 3], [8, h, 4], [7, 6, 5] ] ).

% place the x y coords on a position
positions([],[],_).
positions([H|T],P,Row) :- 
  xpos(H,H1,Row,1),Row1 is Row + 1, positions(T,Q,Row1),
  append(H1,Q,P).

% set the x values for a fixed y
xpos([],[],_,_).
xpos([H|T],[(Col,Row,H)|Z],Row,Col) :- Col1 is Col + 1, xpos(T,Z,Row,Col1).


% 'move_hole' is the top level call for these procedures.
% The order of the attempted movements is : left, right, up and down.


move_hole( State1, State2 ) :-  move_horiz( State1, State2, left ).
move_hole( State1, State2 ) :-  move_vert( State1, State2, up ).
move_hole( State1, State2 ) :-  move_horiz( State1, State2, right ).
move_hole( State1, State2 ) :-  move_vert( State1, State2, down ).

move_horiz( [Row1| Rest], [New1| Rest], Direction )  :-
  move( Row1, New1, Direction ).
move_horiz( [Row| Rest], [Row| NewRest], Direction ) :-
  move_horiz( Rest,  NewRest, Direction ).


move_vert( RowState1, RowState2, Dir1 ) :-
  transpose( RowState1, ColState1 ), swapdir(Dir1, Dir2),
  move_horiz( ColState1, ColState2, Dir2 ),
  transpose( ColState2, RowState2 ).

/* Transposition */
transpose( [ [E11, E12, E13], [E21, E22, E23], [E31, E32, E33] ],
           [ [E11, E21, E31], [E12, E22, E32], [E13, E23, E33] ] ).

swapdir(up, left).  
swapdir(down, right).

move( [h, B, C], [B, h ,C], right ).
move( [A, h, C], [h, A, C], left ).
move( [A, h, C], [A, C, h], right ).
move( [A, B, h], [A, h, B], left ).

/* 'write_extension' allows to provide a trace of the execution
   of the strategies, by showing the extensions (children) created for each
   candidate node.

   'write_solution' prints out, if found, the solution path for the 
   given configurations.
*/
 
% write_extension( _, _ ) :- !.            % to stop tracing un-comment this clause

write_extension( [(_, Node)|Rest], [] ) :-
  writeNode(Node, _,Rest),
  write( ' not extended (max. depth reached)' ),nl.

write_extension( [(_, Node)|Rest], NewPaths ) :-
  NewPaths \== [],
  writeNode(Node, Tabs,Rest), write( ' extended to : ' ), nl,
  write_extensions( Tabs, NewPaths ).

% write_extensions( _, _ ) :- !.           % to stop tracing un-comment this clause
write_extensions( _, [] ).
write_extensions( Tabs, [Path|Paths] ) :-
  Path ends_at (Weight, Node),  tab( 2 ), tab( Tabs ),
  write( Node ),      write( '  -  weight : ' ),
  write( Weight ), nl,    write_extensions( Tabs, Paths ).

writeNode(Node, Tabs,Rest)  :-
  length( Rest, L ),  Tabs is L * 2, tab( Tabs ),nl, write( Node ).

write_solution( [] ).
write_solution( [Node|Rest] ) :-
  write_solution( Rest ), write_cube( Node), nl.

write_cube( [] ).
write_cube( [Row|Rows] ) :- 
  write_row( Row ),nl,write_cube( Rows ).

write_row( [C1, C2, C3] ) :-  
  write( C1 ), tab( 1 ),write( C2 ),tab( 1 ),write( C3 ).


