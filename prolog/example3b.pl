% Example 3a: Figure 5 of OOPSLA'95 (Same as Example 3 but using substitutionEffectSet/4
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
   
    Ewf = effectWrites([p,star,force]),
    Erm = effectReads([p,star,mass]),
    ESetIn = [Ewf, Erm],
    substitutionEffectSet(ESetIn, p, Pi1, ESet1partial),
    substitutionEffectSet(ESetIn, p, Pi2, ESet2partial),
   
    substitutionRpl([p,mass], p, [Pi3], Rplr),     		% P:M[P<-pi3]
    Emr = effectReads(Rplr),

    ESet0 = [E0, Emr, Er, Ew],
    ESet1 = [E0, Emr| ESet1partial],
    ESet2 = [E0, Emr| ESet2partial],

	nonInterferingSetOfEffectSets([ESet0,ESet1,ESet2]),

    true.
 


