% Example 1: Figure 1 of OOPSLA'09
:- consult(dpj).

rplParam(p):- not(rgnName(p)).

rgnName(links).

rgnName(pi1).
rgnName(pi2).

example1(Pi1,Pi2) :-
    E1 = effectReads([links]),			% reads Links
    substitutionRpl(Rpl1, [p], p, [Pi1]),
    substitutionRpl(Rpl2, [p], p, [Pi2]),
    E2 = effectWrites(Rpl1),			% writes P[P<-Pi1]
    E3 = effectWrites(Rpl2),			% writes P[P<-Pi2]
    ESet1 = [E1, E2],
    ESet2 = [E1, E3],
    nonInterferingEffectSets(ESet1, ESet2),
%    write('Pi1='), write(Pi1), nl, 
%    write('Pi2='), write(Pi2),
    true.

