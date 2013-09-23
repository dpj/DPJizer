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


example2(Pi1,Pi2) :-
    E0 = effectReads([links]), 			% reads Links
    substitutionRpl(Rpl1, [p,mass],  p, [Pi1]),	
    substitutionRpl(Rpl2, [p,force], p, [Pi1]),	
    substitutionRpl(Rpl3, [p,mass],  p, [Pi2]),
    substitutionRpl(Rpl4, [p,force], p, [Pi2]),
    substitutionRpl(Rpl5, [p,mass],  p, [local]),
    substitutionRpl(Rpl6, [p,force], p, [local]),
    E1 = effectWrites(Rpl1),			% writes P:M[P<-Pi1]
    E2 = effectWrites(Rpl2),			% writes P:F[P<-Pi1]
    E3 = effectWrites(Rpl3),			% writes P:M[P<-Pi2]
    E4 = effectWrites(Rpl4),			% writes P:F[P<-Pi2]
    E5 = effectReads(Rpl5),			% writes P:M[P<-local]
    E6 = effectReads(Rpl6),			% writes P:F[P<-local]

    ESet1 = [E0, E1, E5],
    EUnionSet1 = [E0, E2, E3, E4, E5, E6], 	% ESet2 U ESet3 U ESet4
    ESet2 = [E0, E2, E6],
    EUnionSet2 = [E0, E1, E3, E4, E5, E6],	% ESet1 U ESet3 U ESet4
    ESet3 = [E0, E3, E5],
    EUnionSet3 = [E0, E1, E2, E4, E5, E6],	% ESet1 U ESet2 U ESet4
    ESet4 = [E0, E4, E6],
    EUnionSet4 = [E0, E1, E2, E3, E5, E6],	% ESet1 U ESet2 U ESet3

    %ESet = [E0, E1],
    %EUnionSet = [E0],
    %nonInterferingEffectSets(ESet, EUnionSet),
    nonInterferingEffectSets(ESet1, EUnionSet1),
    nonInterferingEffectSets(ESet2, EUnionSet2),
    nonInterferingEffectSets(ESet3, EUnionSet3),
    nonInterferingEffectSets(ESet4, EUnionSet4),
    true.

