%:- module(dpj,
%	[ isRpl/1, 						% true if arg is a valid RPL
%	  isUnder/2,					% true if arg1:rpl is under arg2:rpl
%	  isIncluded/2,					% true if arg1:rpl is included in arg2:rpl
%	  subEffectSets/2,				% true if arg1:effectSet is a subeffect of arg2:effectSet
%	  disjointRpl/2,				% true if arg1:rpl is disjoint from arg2:rpl
%	  nonInterferingEffectSets/2	% true if arg1:effectSet is non-interferring with arg2:effectSet	
%	]).



%rplVar(X) :- var(X).

%%	headRplElt(+X:rpl_elt) is det
%
%	True for Head RPL Elements, i.e., elements that can appear at the head of an RPL
%headRplElt(X) :- rplVar(X).

headRplElt(star).

headRplElt(root).			% cut to make det, order for performance

headRplElt(local).			% cut to make det, order for performance

headRplElt(X) :- rplParam(X).

headRplElt(X) :- rgnName(X).



% Non-Head RPL Elements, i.e., elements that can appear at non head spots

%nonHeadRplElt(qmark).
nonHeadRplElt(star).

nonHeadRplElt(X) :- rgnName(X).

rplElt(X) :- headRplElt(X).
rplElt(X) :- nonHeadRplElt(X).

%%	rpl(+Rpl:rpl) is det
%
%	True if Rpl is a valid RPL 
%isRpl(_X).

%isRpl([H]) :-
%	headRplElt(H).

isRpl([H|T]) :-
	isRplTail_(T),
	headRplElt(H).

%%	isRplTail is a helper of isRpl
isRplTail_([]).

%isRplTail_([H]) :-
%    nonHeadRplElt(H).

isRplTail_([H|T]) :-
	isRplTail_(T),
	nonHeadRplElt(H).

%%	lengthRpl(+Rpl:rpl, -Len:int) is det
%
%	Given a valid RPL, lengthRpl returns its Length.
lengthRpl(Rpl, Len):-
	isRpl(Rpl),
	length(Rpl, Len).

%%	isFullySpecified(+Rpl) is det 
%
%	True if Rpl does not contain '*'

%isFullySpecifiedRpl([H]) :- headRplElt(H).

%isFullySpecifiedRpl([H|T]) :- 
%	headRplElt(H),
%	isFullySpecifiedRpl(T).

%%	addToRpl(+Rpl1:rpl, -Rpl2:rpl, -Last:rpl_elt) is det
%%	addToRpl(-Rpl1:rpl, +Rpl2:rpl, +Last:rpl_elt) is det
%
%	Rpl1 = Rpl2 : Last
addToRpl([H1|T1], [H1|T2], Last) :-
	addToRpl(T1, T2, Last).

addToRpl([H], [], H).

%%	stripRoot(+Rpl:rpl, -SansRootRpl:rpl) is det
%
%	True if both Rpls are the same except possibly Rpl starts with Root
stripRoot([root|SansRootRpl], SansRootRpl):- !.

stripRoot(Rpl, Rpl).

% RplOut = Rpl1[Param <- Rpl2]
substitutionRpl(RplOut, Rpl1, Param, Rpl2) :- 
    nonvar(Param), rplParam(Param),
    nonvar(Rpl2),			% NOTE: nonvar([X]) = true
    %isRpl(Rpl1), 
    %isRpl(Rpl2),
    substitutionRpl_(RplOut, Rpl1, Param, Rpl2).

% NOTE: Param may only be at the head of an RPL.
substitutionRpl_([], [], _Param, _Rpl2).

substitutionRpl_(RplOut, [Param|T], Param, Rpl2) :- 
    append(Rpl2, T, RplOut).
    
substitutionRpl_( [H|T], [H|T], Param, _Rpl2) :- H \= Param.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%	isUnder(+Rpl1:rpl, +Rpl2:rpl) is det.
%
%	True if Rpl1 and Rpl2 are well formed and Rpl1 <= Rpl2

isUnder(Rpl1, Rpl2) :-
	isRpl(Rpl1),
	isRpl(Rpl2),
	stripRoot(Rpl1, SansRoot1),
	stripRoot(Rpl2, SansRoot2),
	isUnder_(SansRoot1, SansRoot2).

isUnder_( _, [root]). 				% [Under Root]

isUnder_(Rpl1, Rpl1).				% [Reflexive]

%isUnder_(Rpl1, Rpl3) :-			% [Transitive] hopefully not needed for computation
%	isUnder_(Rpl1, Rpl2),
%	isUnder_(Rpl2, Rpl3).

isUnder_(Rpl1, Rpl2) :- 			% [Under Name&Index&Star]
	addToRpl(Rpl1, Rpl3, _),  		% Rpl1 = Rpl3 : Tail
	isUnder_(Rpl3, Rpl2).

isUnder_(Rpl1, Rpl2) :-				% [Under Include
	isIncluded_(Rpl1, Rpl2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%	isIncluded(+Rpl1:rpl, +Rpl2:rpl) is det
%
%	True if Rpl1 and Rpl2 are well formed and Rpl1 C= Rpl2 (\subseteq)
isIncluded(Rpl1, Rpl2) :-
	isRpl(Rpl1),
	isRpl(Rpl2),
	stripRoot(Rpl1, SansRoot1),
	stripRoot(Rpl2, SansRoot2),
	isIncluded_(SansRoot1, SansRoot2) .

isIncluded_(Rpl1, Rpl1).			% [Reflexive]

%isIncluded_(Rpl1, Rpl3) :-			% [Transitive] hopefully not needed for computation
%	isIncluded_(Rpl1, Rpl2),
%	isIncluded_(Rpl2, Rpl3).

isIncluded_(Rpl1, Rpl2) :-			% [Include Name] & [Include Index]
	addToRpl(Rpl1, Rpl3, Tail),		% Rpl1 = Rpl3 : Tail
	addToRpl(Rpl2, Rpl4, Tail),		% Rpl2 = Rpl4 : Tail
	isIncluded_(Rpl3, Rpl4).

isIncluded_(Rpl1, Rpl2) :-			% [Include Star]
	addToRpl(Rpl2, Rpl3, star),		% Rpl2 = Rpl3 : *
	isUnder_(Rpl1, Rpl3).

% TODO
%isIncluded_(Rpl1, Rpl2) :-			% [Include Full] this creates an infinite 
									% loop, is it even needed? (isn't it 
									% captured by the reflexivity?
%	isFullySpecifiedRpl(Rpl1),
%	isIncluded_(Rpl2, Rpl1).

% TODO [Include Param]


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%		EFFECTS
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%	isEffect(+E:effect) is det
%
%	True if E is an effect. Gamma :- E
isEffect(effectPure).
isEffect(effectReads(Rpl)) :- isRpl(Rpl).
isEffect(effectWrites(Rpl)) :- isRpl(Rpl).
isEffect(effectInvokes(_Method, EffSet)) :- 
	% isMethod(Method) % TODO
	isEffectSet(EffSet).

%%	isEffectSet(+E:effect_set) is det
isEffectSet([]).
isEffectSet([H|T]) :-
	isEffect(H),
	isEffectSet(T).

%%	effectUnion(+ES1:effect_set, +ES2:effect_set, -ESUnion:effect_set) is det.
%
%	ESUnion = ES1 U ES2
%	Uses lists::union/3, whose complexity is |Set1|*|Set2|
%	TODO: this implementation does not simplify the union by removing E1 if 
%	it is a subeffect of some other E2 in the union.

effectUnion(EffSet1, EffSet2, ESUnion) :-
	isEffectSet(EffSet1),
	isEffectSet(EffSet2),
	union(EffSet1,EffSet2,ESUnion).

%%	subEffectSets(+EffectSet1:effect_set, +EffectSet2:effect_set) is det.
%
% 	True if EffectSet1 is a subeffect of EffectSet2
subEffectSets(EffSet1, EffSet2) :-
	isEffectSet(EffSet1),
	isEffectSet(EffSet2),
	subEffectSets_(EffSet1, EffSet2).

subEffectSets_([], _EffSet2).

subEffectSets_([E|R], EffSet2) :-
	subEffectSet_(E, EffSet2),
	subEffectSets_(R, EffSet2).

%%	subEffectSet_(+Effect:effect, +EffectSet:effect_set) is det.
%
%	True when Effect c= EffectSet
subEffectSet_(effectPure, _EffSet).							% [SE Pure]

subEffectSet_(E, [H|_]) :- 
	subEffect_(E, H).

subEffectSet_(E, [_|T]) :-
	subEffectSet_(E, T).

subEffectSet_( effectInvokes(_M, EffSet1), EffSet2):-		% [SE Invokes 2]
	subEffectSets_(EffSet1,EffSet2).						% NOTE we did not Unify E1 w. E2 bc they
															% are *unordered* sets.

%%	subEffect_(+E1:effect, +E2:effect) is det.
%
%	True when E1 c= E2
subEffect_(E, E).											% Reflexive

subEffect_(effectReads(Rpl1), effectReads(Rpl2)) :-			% [SE Reads]
	isIncluded(Rpl1, Rpl2).

subEffect_(effectWrites(Rpl1), effectWrites(Rpl2)) :-		% [SE Writes]
	isIncluded(Rpl1, Rpl2).

subEffect_(effectReads(Rpl1), effectWrites(Rpl2)) :-		% [SE Reads Writes]
	isIncluded(Rpl1, Rpl2).

subEffect_(	effectInvokes(M, EffSet1),						% [SE Invokes 1]
			effectInvokes(M, EffSet2)) :-
	subEffectSets_(EffSet1, EffSet2).

% TODO: is subEffect supposed to be transitive? 
% If so then subEffect_( E, invokes(_M, ES)) :- subEffectSet_(E, ES).
% Which I think is equivalent to discarding (not unifying) the M argument
% in the rule above [SE Invokes 1].


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%		DISJOINTNESS 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%	disjointRpl(+Rpl1:rpl, +Rpl2:rpl) is det.
%
%	True when Rpl1 and Rpl2 are valid RPLs and Rpl1 # Rpl2 (they are disjoint)
disjointRpl(Rpl1,Rpl2) :-
	isRpl(Rpl1),
	isRpl(Rpl2),
	stripRoot(Rpl1, SansRoot1),
	stripRoot(Rpl2, SansRoot2),
	disjointRplSansRoot_(SansRoot1, SansRoot2).

disjointRpl_(Rpl1,Rpl2) :-
	stripRoot(Rpl1, SansRoot1),
	stripRoot(Rpl2, SansRoot2),
	disjointRplSansRoot_(SansRoot1, SansRoot2).

disjointRplSansRoot_(Rpl1,Rpl2) :-
	(	disjointLeftRpl_(Rpl1, Rpl2), ! 
	;	disjointRightRpl_(Rpl1, Rpl2) ).

notStar(X) :- X \= star.	% TODO shouldn't it be nonvar(X), X \= star. OR X \== star.

disjointLeftRpl_([H|_T],[]) :-	% [Disjoint Left Name&Index]
	notStar(H). 		
disjointLeftRpl_([],[H|_T]) :-
	notStar(H).					

disjointLeftRpl_([H1|_T1],[H2|_T2]) :-
	notStar(H1),
    notStar(H2),
	H1 \== H2.

disjointLeftRpl_([H|T1], [H|T2]) :-
    notStar(H),
	disjointLeftRpl_(T1,T2).


disjointRightRpl_([H|_T],[]) :-
	notStar(H).	
disjointRightRpl_([],[H|_T]) :-
	notStar(H).		

disjointRightRpl_(Rpl1, Rpl2) :-			% [Disjoint Right Name&Index] 	
	addToRpl(Rpl1, _RplH1, LastElt1),
	addToRpl(Rpl2, _RplH2, LastElt2),
	notStar(LastElt1),
	notStar(LastElt2),
	LastElt1 \== LastElt2.

disjointRightRpl_(Rpl1, Rpl2) :-			% [Disjoint Name&Index]
	addToRpl(Rpl1, RplH1, LastElt),
	addToRpl(Rpl2, RplH2, LastElt),
    notStar(LastElt),
	disjointRightRpl_(RplH1, RplH2).	


%%	nonInterferingEffectSets(+ES1:effect_set, +ES2:effect_set) is det
%
%	True if ES1 # ES2 (they don't interfere) and ES1 and ES2 are well formed
nonInterferingEffectSets(ES1, ES2) :-
	isEffectSet(ES1), isEffectSet(ES2),
	nonInterferingEffectSets_(ES1,ES2).


nonInterferingEffectSets_([], _ES2).

nonInterferingEffectSets_([E|R], ES2) :-
	nonInterferingEffectSet_(E, ES2),
	nonInterferingEffectSets_(R, ES2).


nonInterferingEffectSet_(_E, []).

nonInterferingEffectSet_(effectPure, _ES).					% [NI Pure]

nonInterferingEffectSet_( effectInvokes(_M,ES), ES2) :-		% [NI Invokes 1]
	nonInterferingEffectSets_(ES, ES2).

nonInterferingEffectSet_(E1, [E2|R2]) :-
	nonInterferingEffect_(E1, E2),
	nonInterferingEffectSet_(E1,R2).


nonInterferingEffect_( effectReads(_Rpl1), 
                       effectReads(_Rpl2) ).				% [NI Reads]

nonInterferingEffect_( effectReads(Rpl1), 
                       effectWrites(Rpl2) ) :-				% [NI Reads-Writes]
	disjointRpl_(Rpl1,Rpl2).

nonInterferingEffect_( effectWrites(Rpl1), 
                       effectReads(Rpl2) ) :-				% [NI Reads-Writes: Symmetric]
	disjointRpl_(Rpl1,Rpl2).

nonInterferingEffect_( effectWrites(Rpl1), 
                       effectWrites(Rpl2) ) :-				% [NI Writes]
	disjointRpl_(Rpl1,Rpl2).

% TODO: enable if we add commutesWith
%nonInterferingEffect_( effectInvokes(M1, ES1), 
%                       effectInvokes(M2, ES2) ) :-			% [NI Invokes 2]
	% commutesWith(M1,M2) .

nonInterferingEffect_(E, effectInvokes(_M2, ES2) ) :-		% [NI Invokes 1: Symmetric] 
	nonInterferingEffectSet_(E, ES2).

nonInterferingEffect_(_E, effectPure).						% [NI Pure: Symmetric]


