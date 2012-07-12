Code developed and tested with SWI-prolog 5.7.11 & 5.10.4

To try example1.pl:
* fire up prolog in interactive mode (pl, or swipl command usually) in the 
  same directory as exampleZ.pl and dpj.pl

* load example1.pl (remember the colon):
	?- ['example1.pl'].
  exampleZ.pl will also load its dependency, dpj.pl

* query the predicate:
	(a) to find solutions
		?- example1(X,Y).

	(b) to check if a specific solution is valid
		?- example1(root, star).

	(c) to check if given a partial solution, a full solution can be found
		?- example1(root, Y).

For examples 2 & 3 the procedure is similar, although I recommend you quit 
and start over the prolog interpreter to purge all the loaded predicates 
that may interfere from one example to the other. (I'm going to figure out  
a way to scope this predicates to prevent them from leaking from one example
to the other).

