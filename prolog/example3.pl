% Example 3: Figure 5 of OOPSLA'09
%:- dynamic rgnName/1, rplParam/1, rplVar/1.

:- consult(dpj).
rgnName(pi1).
rgnName(pi2).
rgnName(pi3).

rplParam(p):- not(rgnName(p)).

rgnName(links).
rgnName(mass).
rgnName(force).


example3(Pi1,Pi2,Pi3) :-
    Pi1 = [p, Pi1b],
    Pi2 = [p, Pi2b],
    E0 = effectReads([links]),                  	% reads Links
    Er = effectReads([p,mass]),
    Ew = effectWrites([p,force]),
    substitutionRpl(Rpl1f, [p,star,force],  p, Pi1),	% P:*:F [P<-P:pi1]
    substitutionRpl(Rpl2f, [p,star,force],  p, Pi2),	% P:*:F [P<-P:pi2]
    Em1 = effectWrites(Rpl1f),				% Em1 = writes P:*:F[P<-pi1]
    Em2 = effectWrites(Rpl2f),				% Em2 = writes P:*:F[P<-pi2]
    substitutionRpl(Rpl1m, [p,star,mass],  p, Pi1),	% P:*:F [P<-P:pi1]
    substitutionRpl(Rpl2m, [p,star,mass],  p, Pi2),	% P:*:F [P<-P:pi2]
    Em1r = effectReads(Rpl1m),				% Em1 = writes P:*:F[P<-pi1]
    Em2r = effectReads(Rpl2m),				% Em2 = writes P:*:F[P<-pi2]
    substitutionRpl(Rplr, [p,mass], p, [Pi3]),     	% P:M[P<-pi3]
    Emr = effectReads(Rplr),

    ESet0 = [E0, Emr, Er, Ew],

    EmSet1 = [E0, Emr, Em1, Em1r],

    EmSet2 = [E0, Emr, Em2, Em2r],

    SetU0 = [E0, Emr, Em1, Em1r, Em2, Em2r],		% EmSet1 U EmSet2
    SetU1 = [E0, Emr, Er, Ew, Em2, Em2r],		% ESet0  U EmSet2
    SetU2 = [E0, Emr, Er, Ew, Em1, Em1r],		% ESet0  U EmSet1

    nonInterferingEffectSets(ESet0, SetU0),
    nonInterferingEffectSets(EmSet1, SetU1), 
    nonInterferingEffectSets(EmSet2, SetU2),
					
    true.

