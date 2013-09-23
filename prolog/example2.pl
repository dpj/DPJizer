% Example 2: Figure 3 of OOPSLA'09
%:- dynamic rgnName/1, rplParam/1, rplVar/1.

% TODO: why is the only solution I get Pi1=root, Pi2=p?
rgnName(pi1).
rgnName(pi2).
:- consult(dpj).

rplParam(p):- not(rgnName(p)).

rgnName(links).
rgnName(mass).
rgnName(force).

rgnName(pi1).
rgnName(pi2).

env(globalEnv, [root], nullEnv).

env(treeNodeEnv, [p, links, mass, force, pi1, pi2], globalEnv).

rplVar(_X, _Y) :- fail.

example2(Pi1,Pi2) :-
    E0 = effectReads([links]), 				% reads Links
    substitutionRpl([p,mass],  p, [Pi1], Rpl1),	
    substitutionRpl([p,force], p, [Pi1], Rpl2),	
    substitutionRpl([p,mass],  p, [Pi2], Rpl3),
    substitutionRpl([p,force], p, [Pi2], Rpl4),
    substitutionRpl([p,mass],  p, [local], Rpl5),
    substitutionRpl([p,force], p, [local], Rpl6),
    E1 = effectWrites(Rpl1),				% writes P:M[P<-Pi1]
    E2 = effectWrites(Rpl2),				% writes P:F[P<-Pi1]
    E3 = effectWrites(Rpl3),				% writes P:M[P<-Pi2]
    E4 = effectWrites(Rpl4),				% writes P:F[P<-Pi2]
    E5 = effectReads(Rpl5),					% writes P:M[P<-local]
    E6 = effectReads(Rpl6),					% writes P:F[P<-local]

    ESet1 = [E0, E1, E5],
    ESet2 = [E0, E2, E6],
    ESet3 = [E0, E3, E5],
    ESet4 = [E0, E4, E6],

	nonInterferingSetOfEffectSets([ESet1, ESet2, ESet3, ESet4]),
    true.

