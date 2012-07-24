% Example 1: Figure 1 of OOPSLA'95
:- consult(dpj).

rplParam(p):- not(rgnName(p)).

rgnName(links).

rgnName(pi1).
rgnName(pi2).

env(globalEnv, [root], nullEnv).

env(treeNodeEnv, [p, links, pi1, pi2], globalEnv).

%rplVar(pi1_rv, treeNodeEnv).
%rplVar(pi2_rv, treeNodeEnv).
rplVar(_X, _Y) :- fail.

example1(Pi1,Pi2) :-
    E1 = effectReads([links]),				% reads Links
    substitutionRpl([p], p, [Pi1], Rpl1),
    substitutionRpl([p], p, [Pi2], Rpl2),
    E2 = effectWrites(Rpl1),				% writes P[P<-Pi1]
    E3 = effectWrites(Rpl2),				% writes P[P<-Pi2]
    ESet1 = [E1, E2],
    ESet2 = [E1, E3],
    nonInterferingEffectSets(ESet1, ESet2),
    true.

