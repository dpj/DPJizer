% Example 3: Figure 5 of OOPSLA'95
:- consult(dpj).

rplParam(p):- not(rgnName(p)).

rgnName(links).
rgnName(mass).
rgnName(force).

rgnName(pi1).
rgnName(pi2).
rgnName(pi3).

env(globalEnv, [root], nullEnv).

env(treeNodeEnv, [p, links, mass, force, pi1, pi2, pi3], globalEnv).

% TODO perhaps rplVar should also have a list of valid formats. e.g., X, p:X, X:[i]
%rplVar(pi1_rv, treeNodeEnv).
%rplVar(pi2_rv, treeNodeEnv).
%rplVar(pi3_rv, treeNodeEnv).
rplVar(_X, _Y) :- fail.

example3(Pi1,Pi2,Pi3) :-
    Pi1 = [p, Pi1b],
    Pi2 = [p, Pi2b],
    E0 = effectReads([links]),  	                	% reads Links
    Er = effectReads([p,mass]),
    Ew = effectWrites([p,force]),

    substitutionRpl([p,star,force],  p, Pi1, Rpl1f),	% P:*:F [P<-P:pi1]
    substitutionRpl([p,star,force],  p, Pi2, Rpl2f),	% P:*:F [P<-P:pi2]
    Em1 = effectWrites(Rpl1f),							% Em1 = writes P:*:F[P<-pi1]
    Em2 = effectWrites(Rpl2f),							% Em2 = writes P:*:F[P<-pi2]
    substitutionRpl([p,star,mass],  p, Pi1, Rpl1m),		% P:*:F [P<-P:pi1]
    substitutionRpl([p,star,mass],  p, Pi2, Rpl2m),		% P:*:F [P<-P:pi2]
    Em1r = effectReads(Rpl1m),							% Em1 = writes P:*:F[P<-pi1]
    Em2r = effectReads(Rpl2m),							% Em2 = writes P:*:F[P<-pi2]
    substitutionRpl([p,mass], p, [Pi3], Rplr),     		% P:M[P<-pi3]
    Emr = effectReads(Rplr),

    ESet0 = [E0, Emr, Er, Ew],

    EmSet1 = [E0, Emr, Em1, Em1r],

    EmSet2 = [E0, Emr, Em2, Em2r],

	nonInterferingSetOfEffectSets([ESet0, EmSet1, EmSet2]),	
    true.

